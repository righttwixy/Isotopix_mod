package by.righttwixys.isotopix.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public final class MaterialDegradationAndCorium {

    private MaterialDegradationAndCorium() {}

    private static final Map<BlockPos, Double> WIGNER_ENERGY_STORE = new HashMap<>();
    private static final Map<BlockPos, Double> GLASS_DOSE_ACCUMULATOR = new HashMap<>();
    private static final Map<BlockPos, Double> RADIOLYTIC_HYDROGEN_MOLES = new HashMap<>();

    public static double calculateDisplacementsPerAtom(float neutronFluence, float neutronEnergyMeV) {
        double thresholdEnergyEV = 25.0;
        double damageEnergyEV = neutronEnergyMeV * 1.0e6 * (4.0 / 56.0);
        if (damageEnergyEV < thresholdEnergyEV) return 0.0;
        double displacementsPerCollision = (0.8 * damageEnergyEV) / (2.0 * thresholdEnergyEV);
        return (neutronFluence * 1.2e-24) * displacementsPerCollision;
    }

    public static void accumulateWignerEnergy(ServerLevel level, BlockPos pos, float fastNeutronFlux, double tempCelsius) {
        BlockState state = level.getBlockState(pos);
        if (!state.getBlock().toString().toLowerCase().contains("coal") && !state.is(Blocks.BLACKSTONE)) {
            return;
        }

        double currentEnergy = WIGNER_ENERGY_STORE.getOrDefault(pos, 0.0);
        if (tempCelsius > 250.0) {
            if (currentEnergy > 120.0) {
                level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                level.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.5f, 0.8f);
                level.sendParticles(ParticleTypes.LAVA, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 20, 0.3, 0.3, 0.3, 0.1);
                WIGNER_ENERGY_STORE.remove(pos);
                return;
            } else {
                currentEnergy = Math.max(0.0, currentEnergy - 4.5);
            }
        } else {
            currentEnergy += (fastNeutronFlux * 1.5e-7);
        }
        WIGNER_ENERGY_STORE.put(pos, currentEnergy);
    }

    public static void applyRadiationBrowning(ServerLevel level, BlockPos pos, double doseRoentgen) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(Blocks.GLASS) && !state.is(Blocks.GLASS_PANE)) return;

        double accumulated = GLASS_DOSE_ACCUMULATOR.getOrDefault(pos, 0.0) + doseRoentgen;
        GLASS_DOSE_ACCUMULATOR.put(pos, accumulated);

        if (accumulated > 25_000.0) {
            if (state.is(Blocks.GLASS)) {
                level.setBlockAndUpdate(pos, Blocks.BROWN_STAINED_GLASS.defaultBlockState());
            } else {
                level.setBlockAndUpdate(pos, Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState());
            }
            GLASS_DOSE_ACCUMULATOR.remove(pos);
        }
    }

    public static void processWaterRadiolysis(ServerLevel level, BlockPos pos, double absorbedDoseRateRPerHour) {
        if (!level.getBlockState(pos).is(Blocks.WATER)) return;
        if (absorbedDoseRateRPerHour < 5000.0) return;

        double molesGenerated = (absorbedDoseRateRPerHour * 1.2e-10);
        double currentMoles = RADIOLYTIC_HYDROGEN_MOLES.getOrDefault(pos, 0.0) + molesGenerated;

        BlockPos above = pos.above();
        if (!level.getBlockState(above).isAir()) {
            if (currentMoles > 0.85) {
                for (BlockPos near : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                    BlockState s = level.getBlockState(near);
                    if (s.is(Blocks.TORCH) || s.is(Blocks.LAVA) || s.is(Blocks.FIRE)) {
                        level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.2f, Level.ExplosionInteraction.BLOCK);
                        RADIOLYTIC_HYDROGEN_MOLES.remove(pos);
                        return;
                    }
                }
            }
            RADIOLYTIC_HYDROGEN_MOLES.put(pos, currentMoles);
        } else {
            RADIOLYTIC_HYDROGEN_MOLES.remove(pos);
        }
    }

    public static boolean checkZirconiumSteamReaction(ServerLevel level, BlockPos pos, double coreTempCelsius) {
        if (coreTempCelsius < 900.0) return false;

        boolean hasWaterNearby = false;
        for (BlockPos near : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (level.getBlockState(near).is(Blocks.WATER)) {
                hasWaterNearby = true;
                level.setBlockAndUpdate(near, Blocks.AIR.defaultBlockState());
                break;
            }
        }

        if (hasWaterNearby) {
            level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 12, 0.2, 0.4, 0.2, 0.05);
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 4.5f, Level.ExplosionInteraction.BLOCK);
            return true;
        }
        return false;
    }

    public static void tickCoriumMcciInteraction(ServerLevel level, BlockPos pos, double coreTempCelsius) {
        if (coreTempCelsius < 1600.0) return;

        BlockPos down = pos.below();
        BlockState floorState = level.getBlockState(down);

        if (!floorState.isAir() && !floorState.is(Blocks.BEDROCK)) {
            level.setBlockAndUpdate(down, Blocks.LAVA.defaultBlockState());
            level.sendParticles(ParticleTypes.LARGE_SMOKE, down.getX() + 0.5, down.getY() + 0.8, down.getZ() + 0.5, 8, 0.2, 0.2, 0.2, 0.02);
            level.playSound(null, down, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 0.7f);

            InducedActivationManager.depositIsotopeContamination(level, down, 2.5e11, (byte) 2, 30.0 * 365.0 * 86400.0);
        }
    }
}