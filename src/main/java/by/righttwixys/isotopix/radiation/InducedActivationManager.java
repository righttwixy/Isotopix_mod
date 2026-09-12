package by.righttwixys.isotopix.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InducedActivationManager {
    private static final Map<BlockPos, ContaminatedBlockSource> CONTAMINATED_BLOCKS = new ConcurrentHashMap<>();

    public enum ContaminationType {
        SORBED_FALLOUT,
        NEUTRON_TRANSMUTATION
    }

    public static class ContaminatedBlockSource {
        public final BlockPos pos;
        public double activityBq;
        public final double halfLifeSeconds;
        public final byte emissionType; // 0=Alpha, 1=Beta, 2=Gamma
        public final ContaminationType type;

        public ContaminatedBlockSource(BlockPos pos, double activityBq, double halfLifeSeconds, byte emissionType, ContaminationType type) {
            this.pos = pos;
            this.activityBq = activityBq;
            this.halfLifeSeconds = halfLifeSeconds;
            this.emissionType = emissionType;
            this.type = type;
        }

        public float getDoseRateMicroR() {
            double factor = (emissionType == 2) ? 0.035 : (emissionType == 1 ? 0.018 : 0.008);
            return (float) Math.min(250_000.0, this.activityBq * factor);
        }
    }

    public static void depositIsotopeContamination(ServerLevel level, BlockPos pos, double transferredBq, byte emissionByte, double halfLifeSeconds) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.is(Blocks.WATER) || transferredBq <= 0.05) {
            return;
        }

        double maxCapacityBq = 5_000_000.0;
        if (state.is(Blocks.CLAY) || state.is(Blocks.MUD)) {
            maxCapacityBq = 12_000_000.0;
        } else if (state.is(Blocks.GLASS) || state.is(Blocks.SMOOTH_STONE)) {
            maxCapacityBq = 400_000.0;
        }

        final double capacityLimit = maxCapacityBq;

        CONTAMINATED_BLOCKS.compute(pos.immutable(), (k, existing) -> {
            if (existing == null) {
                double initialBq = Math.min(capacityLimit, transferredBq);
                return new ContaminatedBlockSource(pos.immutable(), initialBq, halfLifeSeconds, emissionByte, ContaminationType.SORBED_FALLOUT);
            } else {
                existing.activityBq = Math.min(capacityLimit, existing.activityBq + transferredBq);
                return existing;
            }
        });
    }

    public static void irradiateWithNeutrons(ServerLevel level, BlockPos pos, double neutronYield) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.is(Blocks.WATER) || neutronYield <= 0.0) {
            return;
        }

        double activationCrossSection = 0.0;
        double realHalfLifeSeconds = 53856.0;
        byte emissionType = 2;

        if (state.is(Blocks.IRON_BLOCK) || state.is(Blocks.IRON_BARS) || state.is(Blocks.ANVIL)) {
            activationCrossSection = 0.045;
            realHalfLifeSeconds = 8.6152e7; // Fe-55 (2.73 года)
            emissionType = 2;
        } else if (state.is(Blocks.COPPER_BLOCK)) {
            activationCrossSection = 0.030;
            realHalfLifeSeconds = 45720.0;  // Cu-64 (12.70 часов)
            emissionType = 1;
        } else if (state.is(Blocks.GOLD_BLOCK)) {
            activationCrossSection = 0.085;
            realHalfLifeSeconds = 232848.0; // Au-198 (2.70 суток)
            emissionType = 2;
        } else if (state.is(Blocks.STONE) || state.is(Blocks.DEEPSLATE) || state.is(Blocks.COBBLESTONE)) {
            activationCrossSection = 0.012;
            realHalfLifeSeconds = 53856.0;  // Na-24 (14.96 часов)
            emissionType = 1;
        }

        if (activationCrossSection > 0.0) {
            double activatedBq = neutronYield * activationCrossSection;
            final double finalHalfLife = realHalfLifeSeconds;
            final byte finalEmission = emissionType;

            CONTAMINATED_BLOCKS.compute(pos.immutable(), (k, existing) -> {
                if (existing == null) {
                    return new ContaminatedBlockSource(pos.immutable(), activatedBq, finalHalfLife, finalEmission, ContaminationType.NEUTRON_TRANSMUTATION);
                } else {
                    existing.activityBq = Math.min(2_000_000.0, existing.activityBq + activatedBq);
                    return existing;
                }
            });
        }
    }

    public static boolean decontaminateBlock(BlockPos pos) {
        return CONTAMINATED_BLOCKS.remove(pos) != null;
    }

    public static void tick(ServerLevel level) {
        if (CONTAMINATED_BLOCKS.isEmpty()) {
            return;
        }

        double dtSeconds = 0.05; // 1 серверный тик = 0.05 с

        Iterator<Map.Entry<BlockPos, ContaminatedBlockSource>> it = CONTAMINATED_BLOCKS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<BlockPos, ContaminatedBlockSource> entry = it.next();
            BlockPos pos = entry.getKey();
            ContaminatedBlockSource source = entry.getValue();

            boolean isRaining = level.isRainingAt(pos.above());
            BlockState state = level.getBlockState(pos);
            BlockState aboveState = level.getBlockState(pos.above());
            boolean isWashedByWater = state.is(Blocks.WATER) || aboveState.is(Blocks.WATER);

            double effectiveHalfLife = source.halfLifeSeconds;

            if (source.type == ContaminationType.SORBED_FALLOUT) {
                double ecoHalfLifeSeconds = 900.0; // 15 минут

                if (isWashedByWater) {
                    ecoHalfLifeSeconds = 8.0; // 8 секунд
                } else if (isRaining) {
                    ecoHalfLifeSeconds = 25.0; // 25 секунд
                }

                if (effectiveHalfLife > 0.0 && !Double.isInfinite(effectiveHalfLife)) {
                    effectiveHalfLife = (effectiveHalfLife * ecoHalfLifeSeconds) / (effectiveHalfLife + ecoHalfLifeSeconds);
                } else {
                    effectiveHalfLife = ecoHalfLifeSeconds;
                }
            } else {
                if (isWashedByWater) {
                    effectiveHalfLife = Math.min(effectiveHalfLife, 1800.0);
                }
            }

            if (effectiveHalfLife > 0.0) {
                double lambda = Math.log(2.0) / effectiveHalfLife;
                source.activityBq *= Math.exp(-lambda * dtSeconds);
            }

            if (source.activityBq < 1.0) {
                it.remove();
            }
        }
    }

    public static Map<BlockPos, ContaminatedBlockSource> getActivatedBlocks() {
        return CONTAMINATED_BLOCKS;
    }
}