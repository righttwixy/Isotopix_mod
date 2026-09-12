package by.righttwixys.isotopix.radiation;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.init.ModAttachments;
import by.righttwixys.isotopix.item.DosimeterItem;
import by.righttwixys.isotopix.network.RadiationParticleBurstPayload;
import by.righttwixys.isotopix.network.RadiationRaysPayload;
import by.righttwixys.isotopix.network.SyncRadiationPayload;
import by.righttwixys.isotopix.network.SyncRadiationSourcesPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

@EventBusSubscriber(modid = Isotopix.MODID)
public class RadiationSimulationHandler {
    private static RayVisualMode visualMode = RayVisualMode.OFF;

    private static final Map<BlockPos, ReactorPhysicsKinetics> ACTIVE_REACTORS = new HashMap<>();
    private static final Map<Player, AtmosphericPlumeAndBiokinetics> PLAYER_BIOKINETICS = new HashMap<>();

    public static RayVisualMode getVisualMode() {
        return visualMode;
    }

    public static void setVisualMode(RayVisualMode mode) {
        visualMode = mode;
        PacketDistributor.sendToAllPlayers(new RadiationRaysPayload((byte) mode.ordinal(), List.of()));
    }

    public static void recordPolyline(float[] points, byte type) {
    }

    public static boolean hasDosimeter(Player player) {
        if (player.getMainHandItem().getItem() instanceof DosimeterItem ||
                player.getOffhandItem().getItem() instanceof DosimeterItem) {
            return true;
        }
        for (ItemStack item : player.getInventory().items) {
            if (item.getItem() instanceof DosimeterItem) {
                return true;
            }
        }
        return false;
    }

    public static double calculateInverseSquareDose(double doseConstantAtOneMeter, double distanceSq, double effectiveRadius) {
        double effRadiusSq = effectiveRadius * effectiveRadius;
        return doseConstantAtOneMeter / Math.max(0.001, distanceSq + effRadiusSq);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!event.getLevel().isClientSide()) {
            ACTIVE_REACTORS.remove(event.getPos());
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerLevel level : event.getServer().getAllLevels()) {
            simulatePhysicalRadiation(level);
            AtmosphericDispersionManager.tick(level);
        }
    }

    private static void simulatePhysicalRadiation(ServerLevel level) {
        List<ServerPlayer> players = level.players();
        if (players.isEmpty()) {
            return;
        }

        RandomSource random = level.getRandom();

        for (ServerPlayer player : players) {
            boolean isSpectator = player.isSpectator();
            List<RadiationSourceRecord> playerVisibleSources = new ArrayList<>();

            AABB searchBox = new AABB(
                    player.getX() - 128, player.getY() - 64, player.getZ() - 128,
                    player.getX() + 128, player.getY() + 64, player.getZ() + 128
            );


            List<ItemEntity> droppedItems = level.getEntitiesOfClass(ItemEntity.class, searchBox, ItemEntity::isAlive);
            for (ItemEntity itemEntity : droppedItems) {
                ItemStack stack = itemEntity.getItem();
                String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
                IsotopeRadiationProfile profile = IsotopeRadiationRegistry.get(id);

                if (DecayChainManager.isSpentNuclearFuel(id)) {
                    DecayChainManager.ageSnfStack(stack, 1);
                    profile = DecayChainManager.getDynamicSnfProfile(stack, profile);
                }

                if (profile.isRadioactive()) {
                    Vec3 itemPos = itemEntity.position().add(0, 0.12, 0);

                    if (DecayChainManager.isUraniumBearingSource(id)) {
                        DecayChainManager.processRadonExhalation(level, itemPos, stack.getCount(), (float) profile.getActivityBq());
                    }

                    if (profile.getAlphaContactDoseSv() > 0.0) {
                        double alphaDecaysPerSec = profile.getActivityBq() * stack.getCount();
                        double molesHePerTick = (alphaDecaysPerSec * 0.05) / 6.02214076e23;
                        AtmosphericDispersionManager.depositNeutralHelium(level, itemPos, molesHePerTick);
                    }

                    if (AtmosphericDispersionManager.isVolatileGasOrAerosol(id)) {
                        AtmosphericDispersionManager.releaseGaseousActivity(level, itemPos, (float) (profile.getActivityBq() * 0.0001));
                    }

                    double thermalWatts = DecayChainManager.isSpentNuclearFuel(id) ?
                            DecayChainManager.getSnfThermalWatts(stack, stack.getCount()) :
                            RadiationChemistryConstants.getSpecificHeatWattsPerGram(id) * stack.getCount();

                    applyThermodynamicHeatEffects(level, itemEntity.blockPosition(), thermalWatts, random);

                    BlockPos bPos = itemEntity.blockPosition();
                    MaterialDegradationAndCorium.applyRadiationBrowning(level, bPos, profile.getGammaDoseConst() * stack.getCount());
                    MaterialDegradationAndCorium.processWaterRadiolysis(level, bPos, profile.getGammaDoseConst() * stack.getCount() * 1000.0);
                    MaterialDegradationAndCorium.tickCoriumMcciInteraction(level, bPos, thermalWatts * 4.0);

                    if (!isSpectator) {
                        double distSq = player.distanceToSqr(itemEntity);
                        if (distSq < 1024.0) {
                            double directGammaRPerHour = calculateInverseSquareDose(profile.getGammaDoseConst() * stack.getCount(), distSq, 0.20);
                            if (directGammaRPerHour > 0.0001) {
                                player.getData(ModAttachments.RADIATION).recordParticleHit(RadiationType.GAMMA, directGammaRPerHour / 72000.0, 1);
                            }
                        }
                    }

                    playerVisibleSources.add(createSourceRecord(itemPos, profile, stack.getCount(), -1));
                }
            }


            List<ItemFrame> frames = level.getEntitiesOfClass(ItemFrame.class, searchBox, ItemFrame::isAlive);
            for (ItemFrame frame : frames) {
                ItemStack stack = frame.getItem();
                if (!stack.isEmpty()) {
                    String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
                    IsotopeRadiationProfile profile = IsotopeRadiationRegistry.get(id);

                    if (DecayChainManager.isSpentNuclearFuel(id)) {
                        DecayChainManager.ageSnfStack(stack, 1);
                        profile = DecayChainManager.getDynamicSnfProfile(stack, profile);
                    }

                    if (profile.isRadioactive()) {
                        Vec3 framePos = frame.position();
                        if (DecayChainManager.isUraniumBearingSource(id)) {
                            DecayChainManager.processRadonExhalation(level, framePos, stack.getCount(), (float) profile.getActivityBq());
                        }

                        if (profile.getAlphaContactDoseSv() > 0.0) {
                            double alphaDecaysPerSec = profile.getActivityBq() * stack.getCount();
                            double molesHePerTick = (alphaDecaysPerSec * 0.05) / 6.02214076e23;
                            AtmosphericDispersionManager.depositNeutralHelium(level, framePos, molesHePerTick);
                        }

                        if (!isSpectator) {
                            double distSq = player.distanceToSqr(frame);
                            if (distSq < 1024.0) {
                                double directGammaRPerHour = calculateInverseSquareDose(profile.getGammaDoseConst() * stack.getCount(), distSq, 0.20);
                                if (directGammaRPerHour > 0.0001) {
                                    player.getData(ModAttachments.RADIATION).recordParticleHit(RadiationType.GAMMA, directGammaRPerHour / 72000.0, 1);
                                }
                            }
                        }

                        playerVisibleSources.add(createSourceRecord(framePos, profile, stack.getCount(), -1));
                    }
                }
            }


            BlockPos pPos = player.blockPosition();
            int radH = 24, radV = 12;
            for (BlockPos cPos : BlockPos.betweenClosed(pPos.offset(-radH, -radV, -radH), pPos.offset(radH, radV, radH))) {
                BlockState cState = level.getBlockState(cPos);
                String blockName = cState.getBlock().toString().toLowerCase();

                if (blockName.contains("uranium") || blockName.contains("reactor") || blockName.contains("fission")) {
                    ReactorPhysicsKinetics kinetics = ACTIVE_REACTORS.computeIfAbsent(cPos.immutable(), k -> new ReactorPhysicsKinetics());

                    double moderatorCount = 0;
                    double controlRodCount = 0;
                    for (BlockPos near : BlockPos.betweenClosed(cPos.offset(-1, -1, -1), cPos.offset(1, 1, 1))) {
                        BlockState ns = level.getBlockState(near);
                        if (ns.is(Blocks.WATER) || ns.getBlock().toString().contains("blackstone")) moderatorCount += 1.0;
                        if (ns.is(Blocks.IRON_BLOCK) || ns.is(Blocks.CHAIN)) controlRodCount += 1.0;
                    }

                    kinetics.stepKineticSimulation(level, cPos, 0.05, moderatorCount / 18.0, controlRodCount / 8.0);

                    if (kinetics.isPromptCritical()) {
                        level.explode(null, cPos.getX() + 0.5, cPos.getY() + 0.5, cPos.getZ() + 0.5, 6.0f, Level.ExplosionInteraction.BLOCK);
                        ACTIVE_REACTORS.remove(cPos);
                        continue;
                    }

                    MaterialDegradationAndCorium.checkZirconiumSteamReaction(level, cPos, kinetics.getFuelTemperatureCelsius());
                    MaterialDegradationAndCorium.tickCoriumMcciInteraction(level, cPos, kinetics.getFuelTemperatureCelsius());
                }

                BlockEntity be = level.getBlockEntity(cPos);
                if (be instanceof Container container) {
                    double cAct = 0, cGamma = 0, cBeta = 0, cAlpha = 0, cNeutrons = 0;
                    for (int s = 0; s < container.getContainerSize(); s++) {
                        ItemStack cStack = container.getItem(s);
                        if (!cStack.isEmpty()) {
                            String cId = BuiltInRegistries.ITEM.getKey(cStack.getItem()).getPath();
                            IsotopeRadiationProfile cProf = IsotopeRadiationRegistry.get(cId);

                            if (DecayChainManager.isSpentNuclearFuel(cId)) {
                                DecayChainManager.ageSnfStack(cStack, 1);
                                cProf = DecayChainManager.getDynamicSnfProfile(cStack, cProf);
                            }

                            if (cProf.isRadioactive()) {
                                int cnt = cStack.getCount();
                                cAct += cProf.getActivityBq() * cnt;
                                cGamma += cProf.getGammaDoseConst() * cnt;
                                cBeta += cProf.getBetaDoseConst() * cnt;
                                cAlpha += cProf.getAlphaContactDoseSv() * cnt;
                                cNeutrons += cProf.getNeutronYieldPerSec() * cnt;

                                if (DecayChainManager.isUraniumBearingSource(cId)) {
                                    DecayChainManager.processRadonExhalation(level, new Vec3(cPos.getX() + 0.5, cPos.getY() + 0.5, cPos.getZ() + 0.5), cnt, (float) cProf.getActivityBq());
                                }

                                if (cProf.getAlphaContactDoseSv() > 0.0) {
                                    double aDecays = cProf.getActivityBq() * cnt;
                                    double mHe = (aDecays * 0.05) / 6.02214076e23;
                                    AtmosphericDispersionManager.depositNeutralHelium(level, new Vec3(cPos.getX() + 0.5, cPos.getY() + 0.5, cPos.getZ() + 0.5), mHe);
                                }
                            }
                        }
                    }

                    if (cAct > 0.0 || cNeutrons > 0.0) {
                        IsotopeRadiationProfile chestProf = new IsotopeRadiationProfile(cAct, cGamma, cBeta, cAlpha, cNeutrons);
                        Vec3 chestCenter = new Vec3(cPos.getX() + 0.5, cPos.getY() + 0.5, cPos.getZ() + 0.5);

                        if (!isSpectator) {
                            double distSq = player.position().distanceToSqr(chestCenter);
                            if (distSq < 1024.0) {
                                double directGamma = calculateInverseSquareDose(cGamma, distSq, 0.35);
                                if (directGamma > 0.0001) {
                                    player.getData(ModAttachments.RADIATION).recordParticleHit(RadiationType.GAMMA, directGamma / 72000.0, 1);
                                }
                            }
                        }

                        playerVisibleSources.add(createSourceRecord(chestCenter, chestProf, 1, -1));
                    }
                }
            }


            double actBq = 0, gammaC = 0, betaC = 0, alphaD = 0, neutrons = 0;
            double totalThermalWatts = 0.0;

            for (int i = 0; i < player.getInventory().items.size(); i++) {
                ItemStack stack = player.getInventory().items.get(i);
                if (!stack.isEmpty()) {
                    String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
                    IsotopeRadiationProfile profile = IsotopeRadiationRegistry.get(id);

                    if (DecayChainManager.isSpentNuclearFuel(id)) {
                        DecayChainManager.ageSnfStack(stack, 1);
                        profile = DecayChainManager.getDynamicSnfProfile(stack, profile);
                    }

                    if (profile.isRadioactive()) {
                        int count = stack.getCount();
                        actBq += profile.getActivityBq() * count;
                        gammaC += profile.getGammaDoseConst() * count;
                        betaC += profile.getBetaDoseConst() * count;
                        alphaD += profile.getAlphaContactDoseSv() * count;
                        neutrons += profile.getNeutronYieldPerSec() * count;

                        if (DecayChainManager.isSpentNuclearFuel(id)) {
                            totalThermalWatts += DecayChainManager.getSnfThermalWatts(stack, count);
                        } else {
                            totalThermalWatts += RadiationChemistryConstants.getSpecificHeatWattsPerGram(id) * count;
                        }

                        if (DecayChainManager.isUraniumBearingSource(id)) {
                            DecayChainManager.processRadonExhalation(level, player.position(), count, (float) profile.getActivityBq());
                        }

                        if (profile.getAlphaContactDoseSv() > 0.0) {
                            double aDecays = profile.getActivityBq() * count;
                            double mHe = (aDecays * 0.05) / 6.02214076e23;
                            AtmosphericDispersionManager.depositNeutralHelium(level, player.position().add(0, 0.5, 0), mHe);
                        }
                    }
                }
            }

            if (!isSpectator && totalThermalWatts > 250.0 && random.nextFloat() < 0.10f) {
                player.igniteForSeconds(2);
            }

            if (actBq > 0.0 || neutrons > 0.0) {
                IsotopeRadiationProfile combined = new IsotopeRadiationProfile(actBq, gammaC, betaC, alphaD, neutrons);

                double contactGammaRPerHour = calculateInverseSquareDose(gammaC, 0.15 * 0.15, 0.08);
                double contactBetaRPerHour  = calculateInverseSquareDose(betaC, 0.15 * 0.15, 0.10) * 0.30;
                double contactAlphaRPerHour = 0.0;
                double contactNeutronRPerHour = calculateInverseSquareDose(neutrons * 1.002e-10, 0.15 * 0.15, 0.08);

                double totalContactRPerHour = contactBetaRPerHour + contactGammaRPerHour + contactAlphaRPerHour + contactNeutronRPerHour;
                double doseThisTickR = totalContactRPerHour / 72000.0;

                RadiationType dominantType = RadiationType.BETA;
                if (contactGammaRPerHour > contactBetaRPerHour) {
                    dominantType = RadiationType.GAMMA;
                }
                if (contactNeutronRPerHour > contactGammaRPerHour && contactNeutronRPerHour > contactBetaRPerHour) {
                    dominantType = RadiationType.NEUTRON;
                }

                double expectedCPS = (totalContactRPerHour * 10_000.0) + (actBq * 1.5e-6);
                int tickHits = 0;
                if (expectedCPS > 0.0) {
                    double hitsPerTick = expectedCPS / 20.0;
                    tickHits = (int) hitsPerTick;
                    if (random.nextDouble() < (hitsPerTick - tickHits)) {
                        tickHits++;
                    }
                }

                if (!isSpectator) {
                    player.getData(ModAttachments.RADIATION).recordParticleHit(dominantType, doseThisTickR, tickHits);
                }

                playerVisibleSources.add(createSourceRecord(player.position().add(0, 0.75, 0), combined, 1, player.getId()));
            }

            for (Map.Entry<BlockPos, AtmosphericDispersionManager.GasCloud> entry : AtmosphericDispersionManager.getActiveClouds().entrySet()) {
                AtmosphericDispersionManager.GasCloud cloud = entry.getValue();
                Vec3 wind = new Vec3(0.8, 0.0, 0.2);
                double plumeConc = AtmosphericPlumeAndBiokinetics.calculateGaussianPlumeConcentration(
                        cloud.activityMicroR * 1000.0,
                        cloud.center,
                        player.position(),
                        wind,
                        level.isRaining()
                );

                if (plumeConc > 0.01 && !isSpectator) {
                    EntityRadiation rad = player.getData(ModAttachments.RADIATION);
                    rad.recordParticleHit(RadiationType.ALPHA, (float) (plumeConc * 1.0e-9), 1);

                    AtmosphericPlumeAndBiokinetics bio = PLAYER_BIOKINETICS.computeIfAbsent(player, p -> new AtmosphericPlumeAndBiokinetics());
                    bio.ingestNuclideSpecies("I131", plumeConc * 0.05);
                }
            }

            if (!isSpectator) {
                AtmosphericPlumeAndBiokinetics playerBio = PLAYER_BIOKINETICS.computeIfAbsent(player, p -> new AtmosphericPlumeAndBiokinetics());
                playerBio.tickBiokinetics(player, player.getData(ModAttachments.RADIATION));
            }

            EntityRadiation rad = player.getData(ModAttachments.RADIATION);

            if (isSpectator) {
                rad.setAccumulatedDoseRoentgen(0.0f);
                rad.setDnaDamageUnits(0.0f);
                rad.setIngestedRadionuclidesBq(0.0);
            } else {
                float envBackgroundMicroR = NaturalBackgroundManager.calculateEnvironmentalBackgroundMicroR(level, player.blockPosition());
                rad.updateDetectionAndBiology(envBackgroundMicroR);
            }

            dispatchNaturalBackgroundParticles(player, random);

            if (player.tickCount % 2 == 0) {
                PacketDistributor.sendToPlayer(player, new SyncRadiationPayload(
                        rad.getAccumulatedDoseRoentgen(),
                        rad.getCurrentDoseRateMicroRPerHour(),
                        rad.getCountsPerSecond(),
                        rad.getDnaDamageUnits(),
                        rad.getIngestedRadionuclidesBq()
                ));

                PacketDistributor.sendToPlayer(player, new SyncRadiationSourcesPayload(playerVisibleSources));
            }
        }
    }

    private static RadiationSourceRecord createSourceRecord(Vec3 pos, IsotopeRadiationProfile profile, int count, int emitterId) {
        double act = profile.getActivityBq() * count;
        double nYield = profile.getNeutronYieldPerSec() * count;

        float alphaRate = profile.getAlphaContactDoseSv() > 0.0 ? (float) Math.min(250.0, Math.max(0.05, Math.pow(Math.max(0.0, Math.log10(act + 1.0) - 3.0), 2.2))) : 0.0f;
        float betaRate  = profile.getBetaDoseConst() > 0.0 ? (float) Math.min(450.0, Math.max(0.1, Math.pow(Math.max(0.0, Math.log10(act + 1.0) - 2.5), 2.4))) : 0.0f;
        float gammaRate = profile.getGammaDoseConst() > 0.0 ? (float) Math.min(400.0, Math.max(0.1, Math.pow(Math.max(0.0, Math.log10(act + 1.0) - 2.5), 2.3))) : 0.0f;
        float neutronRate = nYield > 0.0 ? (float) Math.min(500.0, Math.max(0.2, Math.pow(Math.max(0.0, Math.log10(nYield + 1.0) - 1.0), 2.5))) : 0.0f;

        float doseAlphaNanoR = (float) Math.max(0.0001, (profile.getAlphaContactDoseSv() * count * 1.0e9) / 72000.0);
        float doseBetaNanoR  = (float) Math.max(0.0001, (profile.getBetaDoseConst() * count * 1.0e9) / 72000.0);
        float doseGammaNanoR = (float) Math.max(0.0001, (profile.getGammaDoseConst() * count * 1.0e9) / 72000.0);
        float doseNeutronNanoR = (float) Math.max(0.0001, (nYield * 1.002e-10 * 1.0e9) / 72000.0);

        float totalWeight = alphaRate + betaRate + gammaRate + neutronRate;

        return new RadiationSourceRecord(
                (float) pos.x, (float) pos.y, (float) pos.z,
                emitterId,
                alphaRate, betaRate, gammaRate, neutronRate,
                doseAlphaNanoR, doseBetaNanoR, doseGammaNanoR, doseNeutronNanoR,
                5.5f, 1.2f, 1.33f, 2.13f,
                totalWeight
        );
    }

    public static void dispatchGpuParticleStreams(Vec3 origin, IsotopeRadiationProfile profile, int count, int emitterId, RandomSource random) {
        double act = profile.getActivityBq() * count;
        double nYield = profile.getNeutronYieldPerSec() * count;
        if (act <= 1.0 && nYield <= 0.0) return;

        double logA = Math.log10(Math.max(1.0, act));

        int totalToSpawn;
        if (logA <= 4.2) {
            totalToSpawn = random.nextDouble() < 0.20 ? 1 : 0;
        } else if (logA < 7.0) {
            totalToSpawn = (int) Math.max(4, (logA - 4.0) * 16.0);
        } else if (logA < 10.0) {
            totalToSpawn = (int) Math.max(48, (logA - 6.0) * 110.0);
        } else {
            totalToSpawn = (int) Math.min(640, Math.max(200, (logA - 8.0) * 220.0));
        }

        if (profile.getAlphaContactDoseSv() > 0.0 && totalToSpawn > 0 && act > 1.0) {
            int alphaCount = Math.min(128, Math.max(1, totalToSpawn / 2));
            double alphaDecaysPerParticle = (act * 0.05) / (double) alphaCount;
            float doseAlphaNanoR = (float) (alphaDecaysPerParticle * 5.5 * 0.00026 * 20.0);

            PacketDistributor.sendToAllPlayers(new RadiationParticleBurstPayload(
                    (float) origin.x, (float) origin.y, (float) origin.z,
                    0.0f, 1.0f, 0.0f, 3.0f, alphaCount, (byte) 0, 5.5f, doseAlphaNanoR, emitterId
            ));
        }

        if (profile.getBetaDoseConst() > 0.0 && totalToSpawn > 0 && act > 1.0) {
            int betaCount = totalToSpawn;
            double betaDoseRPerHour = profile.getBetaDoseConst() * count;
            float doseBetaNanoR = (float) Math.max(0.0001, (betaDoseRPerHour * 1.0e9) / (72000.0 * betaCount));

            PacketDistributor.sendToAllPlayers(new RadiationParticleBurstPayload(
                    (float) origin.x, (float) origin.y, (float) origin.z,
                    0.0f, 1.0f, 0.0f, 3.0f, betaCount, (byte) 1, 1.2f, doseBetaNanoR, emitterId
            ));
        }

        if (profile.getGammaDoseConst() > 0.0 && totalToSpawn > 0 && act > 1.0) {
            int gammaCount = Math.min(320, Math.max(1, totalToSpawn / 2));
            double gammaDoseRPerHour = profile.getGammaDoseConst() * count;
            float doseGammaNanoR = (float) Math.max(0.0001, (gammaDoseRPerHour * 1.0e9) / (72000.0 * gammaCount));

            PacketDistributor.sendToAllPlayers(new RadiationParticleBurstPayload(
                    (float) origin.x, (float) origin.y, (float) origin.z,
                    0.0f, 1.0f, 0.0f, 3.0f, gammaCount, (byte) 2, 1.33f, doseGammaNanoR, emitterId
            ));
        }

        if (nYield > 0.0) {
            double logN = Math.log10(nYield + 1.0);
            int nBurst;
            if (logN < 4.0) {
                nBurst = (int) Math.max(1, Math.round(logN));
            } else if (logN < 8.0) {
                nBurst = (int) Math.max(16, (logN - 3.0) * 45.0);
            } else {
                nBurst = (int) Math.min(640, Math.max(180, (logN - 6.0) * 150.0));
            }

            double nDoseRPerHour = nYield * 1.002e-10;
            float doseNeutronNanoR = (float) Math.max(0.0001, (nDoseRPerHour * 1.0e9) / (72000.0 * nBurst));

            PacketDistributor.sendToAllPlayers(new RadiationParticleBurstPayload(
                    (float) origin.x, (float) origin.y, (float) origin.z,
                    0.0f, 1.0f, 0.0f, 3.0f, nBurst, (byte) 3, 2.13f, doseNeutronNanoR, emitterId
            ));
        }
    }

    private static void dispatchNaturalBackgroundParticles(ServerPlayer player, RandomSource random) {
        if (random.nextFloat() < 0.25f) {
            double rx = player.getX() + (random.nextDouble() - 0.5) * 40.0;
            double rz = player.getZ() + (random.nextDouble() - 0.5) * 40.0;
            double ry = player.getY() + 35.0;

            PacketDistributor.sendToAllPlayers(new RadiationParticleBurstPayload(
                    (float) rx, (float) ry, (float) rz,
                    0.0f, -1.0f, 0.0f, 0.08f, 1, (byte) 1, 3500.0f, 0.035f, -1
            ));
        }

        if (random.nextFloat() < 0.20f) {
            double gx = player.getX() + (random.nextDouble() - 0.5) * 24.0;
            double gz = player.getZ() + (random.nextDouble() - 0.5) * 24.0;
            double gy = player.getY() - 1.0 + (random.nextDouble() - 0.5) * 3.0;

            PacketDistributor.sendToAllPlayers(new RadiationParticleBurstPayload(
                    (float) gx, (float) gy, (float) gz,
                    0.0f, 1.0f, 0.0f, 3.0f, 1, (byte) 2, 1.46f, 0.015f, -1
            ));
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.isSpectator()) {
                return;
            }
            ItemStack stack = event.getItem();
            if (stack.is(Items.ROTTEN_FLESH)) {
                player.getData(ModAttachments.RADIATION).ingestRadionuclide(15_000.0);
            } else if (stack.is(Items.MILK_BUCKET)) {
                EntityRadiation rad = player.getData(ModAttachments.RADIATION);
                rad.cureInternalContamination(0.80f);
                player.playNotifySound(SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 0.5f, 1.5f);
            }
        }
    }

    private static void applyThermodynamicHeatEffects(ServerLevel level, BlockPos pos, double watts, RandomSource random) {
        if (watts < 50.0) return;

        if (random.nextFloat() < 0.20f) {
            level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 2, 0.1, 0.1, 0.1, 0.01);
        }

        if (watts > 200.0) {
            for (BlockPos near : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                BlockState nearState = level.getBlockState(near);
                if (nearState.is(Blocks.ICE) || nearState.is(Blocks.FROSTED_ICE) || nearState.is(Blocks.SNOW)) {
                    level.setBlockAndUpdate(near, Blocks.WATER.defaultBlockState());
                } else if (nearState.is(Blocks.WATER) && random.nextFloat() < 0.15f) {
                    level.sendParticles(ParticleTypes.BUBBLE, near.getX() + 0.5, near.getY() + 0.8, near.getZ() + 0.5, 3, 0.1, 0.2, 0.1, 0.02);
                }
            }
        }

        if (watts > 1500.0 && random.nextFloat() < 0.04f) {
            BlockPos above = pos.above();
            if (level.getBlockState(above).isAir()) {
                level.setBlockAndUpdate(above, Blocks.FIRE.defaultBlockState());
            }
        }
    }
}