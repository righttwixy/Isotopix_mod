package by.righttwixys.isotopix.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtmosphericDispersionManager {
    private static final Map<BlockPos, GasCloud> ACTIVE_CLOUDS = new ConcurrentHashMap<>();
    private static final Map<BlockPos, HeliumCloud> ACTIVE_HELIUM_CLOUDS = new ConcurrentHashMap<>();

    public static class GasCloud {
        public Vec3 center;
        public float activityMicroR;
        public float radius;
        public int lifetimeTicks;

        public GasCloud(Vec3 center, float activityMicroR, float radius, int lifetimeTicks) {
            this.center = center;
            this.activityMicroR = activityMicroR;
            this.radius = radius;
            this.lifetimeTicks = lifetimeTicks;
        }
    }

    public static class HeliumCloud {
        public Vec3 center;
        public double moles;
        public float radius;
        public int lifetimeTicks;

        public HeliumCloud(Vec3 center, double moles, float radius, int lifetimeTicks) {
            this.center = center;
            this.moles = moles;
            this.radius = radius;
            this.lifetimeTicks = lifetimeTicks;
        }
    }

    public static boolean isVolatileGasOrAerosol(String isotopeId) {
        String id = isotopeId.toLowerCase();
        return id.equals("rn222") || id.equals("rn220") || id.equals("kr85") ||
                id.equals("xe133") || id.equals("xe135") || id.equals("i131") || id.equals("he4");
    }

    public static void releaseGaseousActivity(ServerLevel level, Vec3 origin, float activityMicroR) {
        BlockPos pos = BlockPos.containing(origin);
        ACTIVE_CLOUDS.compute(pos, (k, existing) -> {
            if (existing == null) {
                return new GasCloud(origin, activityMicroR, 3.5f, 600);
            } else {
                existing.activityMicroR = Math.min(100_000.0f, existing.activityMicroR + activityMicroR);
                existing.radius = Math.min(18.0f, existing.radius + 0.8f);
                existing.lifetimeTicks = Math.max(existing.lifetimeTicks, 600);
                return existing;
            }
        });
    }

    public static void depositNeutralHelium(ServerLevel level, Vec3 origin, double moles) {
        if (moles <= 0.0) return;
        BlockPos pos = BlockPos.containing(origin);

        ACTIVE_HELIUM_CLOUDS.compute(pos, (k, existing) -> {
            if (existing == null) {
                return new HeliumCloud(origin, moles, 1.2f, 1200);
            } else {
                existing.moles += moles;
                existing.radius = Math.min(12.0f, existing.radius + 0.2f);
                existing.lifetimeTicks = Math.max(existing.lifetimeTicks, 1200);
                return existing;
            }
        });
    }

    public static void tick(ServerLevel level) {
        if (!ACTIVE_CLOUDS.isEmpty()) {
            Iterator<Map.Entry<BlockPos, GasCloud>> it = ACTIVE_CLOUDS.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<BlockPos, GasCloud> entry = it.next();
                GasCloud cloud = entry.getValue();
                cloud.lifetimeTicks -= 2;

                cloud.radius += 0.025f;
                cloud.activityMicroR *= 0.997f;
                cloud.center = cloud.center.add(0.015, 0.035, 0.01);

                if (cloud.lifetimeTicks <= 0 || cloud.activityMicroR < 0.5f) {
                    it.remove();
                }
            }
        }

        if (!ACTIVE_HELIUM_CLOUDS.isEmpty()) {
            Iterator<Map.Entry<BlockPos, HeliumCloud>> itHe = ACTIVE_HELIUM_CLOUDS.entrySet().iterator();
            while (itHe.hasNext()) {
                Map.Entry<BlockPos, HeliumCloud> entry = itHe.next();
                HeliumCloud he = entry.getValue();
                he.lifetimeTicks -= 2;

                BlockPos bPos = BlockPos.containing(he.center);
                BlockState state = level.getBlockState(bPos);

                if (state.is(Blocks.WATER)) {
                    if (level.getRandom().nextFloat() < 0.35f) {
                        level.sendParticles(ParticleTypes.BUBBLE, he.center.x, he.center.y, he.center.z, 3, 0.15, 0.25, 0.15, 0.02);
                    }
                    he.center = he.center.add(0.0, 0.085, 0.0);
                } else if (state.isAir()) {
                    he.center = he.center.add(0.008, 0.065, 0.008);
                    he.radius += 0.035f;
                    he.moles *= 0.996;
                } else {
                    BlockPos above = bPos.above();
                    if (level.getBlockState(above).isAir() || level.getBlockState(above).is(Blocks.WATER)) {
                        he.center = he.center.add(0.0, 0.03, 0.0);
                    }
                }

                if (he.lifetimeTicks <= 0 || he.moles < 1.0e-9) {
                    itHe.remove();
                }
            }
        }
    }

    public static Map<BlockPos, GasCloud> getActiveClouds() {
        return ACTIVE_CLOUDS;
    }

    public static Map<BlockPos, HeliumCloud> getActiveHeliumClouds() {
        return ACTIVE_HELIUM_CLOUDS;
    }
}