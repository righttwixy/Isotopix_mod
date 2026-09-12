package by.righttwixys.isotopix.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RadiationRayTracer {

    private static final double ROENTGEN_PER_MEV = 0.0000000093;

    public static Vec3 getRandomSphereDirection(RandomSource random) {
        double z = random.nextDouble() * 2.0 - 1.0;
        double phi = random.nextDouble() * 2.0 * Math.PI;
        double r = Math.sqrt(Math.max(0.0, 1.0 - z * z));
        double x = r * Math.cos(phi);
        double y = r * Math.sin(phi);
        return new Vec3(x, y, z).normalize();
    }

    public static float getAtmosphericDensity(double y) {
        if (y < 64.0) {
            return 1.05f;
        } else if (y > 320.0) {
            return 0.01f;
        } else {
            return (float) Math.exp(-(y - 64.0) / 140.0);
        }
    }

    public static void traceAlphaParticle(ServerLevel level, Vec3 origin, Vec3 dir,
                                          LivingEntity emitter, List<LivingEntity> targets, RandomSource random) {
        double energyMeV = 4.5 + (random.nextDouble() * 3.5);
        double maxDist = 0.04 + (energyMeV * 0.022);
        double stepSize = 0.035;
        double currentDist = 0.02;

        Vec3 currentPos = origin;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        while (currentDist < maxDist) {
            currentPos = currentPos.add(dir.scale(stepSize));

            pos.set(Math.floor(currentPos.x), Math.floor(currentPos.y), Math.floor(currentPos.z));
            BlockState state = level.getBlockState(pos);
            if (MaterialDensityRegistry.getDensity(state) > 0.05f) {
                break;
            }

            double hitDoseRoentgen = energyMeV * ROENTGEN_PER_MEV * RadiationType.ALPHA.getRadiationWeight();
            if (checkHitAndCapture(targets, emitter, currentPos, RadiationType.ALPHA, hitDoseRoentgen)) {
                break;
            }

            currentDist += stepSize;
        }
    }

    public static void traceBetaParticle(ServerLevel level, Vec3 origin, Vec3 initialDir,
                                         LivingEntity emitter, List<LivingEntity> targets, RandomSource random) {
        float airDensity = getAtmosphericDensity(origin.y);
        double energyFactor = Math.pow(random.nextDouble(), 1.8);
        double energyMeV = 0.05 + (energyFactor * 2.25);
        double maxDist = (energyMeV * 4.8) / Math.max(0.1f, airDensity);

        double stepSize = 0.35;
        double currentDist = 0.15;

        Vec3 currentPos = origin;
        Vec3 curDir = initialDir;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        while (currentDist < maxDist) {
            currentPos = currentPos.add(curDir.scale(stepSize));

            pos.set(Math.floor(currentPos.x), Math.floor(currentPos.y), Math.floor(currentPos.z));
            BlockState state = level.getBlockState(pos);
            float density = MaterialDensityRegistry.getDensity(state);

            if (density > 0.05f) {
                if (density > 1.8f) {
                    double bremsYield = RadiationChemistryConstants.calculateBremsstrahlungYield(density, energyMeV);
                    if (random.nextDouble() < bremsYield) {
                        traceGammaParticle(level, currentPos, curDir, emitter, targets, random);
                    }
                }
                break;
            }

            double hitDoseRoentgen = energyMeV * ROENTGEN_PER_MEV * RadiationType.BETA.getRadiationWeight();
            if (checkHitAndCapture(targets, emitter, currentPos, RadiationType.BETA, hitDoseRoentgen)) {
                break;
            }

            double sx = (random.nextDouble() - 0.5) * 0.28;
            double sy = (random.nextDouble() - 0.5) * 0.28;
            double sz = (random.nextDouble() - 0.5) * 0.28;
            curDir = curDir.add(sx, sy, sz).normalize();

            currentDist += stepSize;
        }
    }

    public static void traceGammaParticle(ServerLevel level, Vec3 origin, Vec3 initialDir,
                                          LivingEntity emitter, List<LivingEntity> targets, RandomSource random) {
        float airDensity = getAtmosphericDensity(origin.y);
        double energyMeV = 0.2 + (random.nextDouble() * 1.6);
        double meanFreePath = (60.0 + (energyMeV * 35.0)) / Math.max(0.02f, airDensity);
        double maxDist = -Math.log(Math.max(0.0005, random.nextDouble())) * meanFreePath;
        maxDist = Math.max(2.0, Math.min(RadiationType.GAMMA.getMaxAirRange(), maxDist));

        double currentDist = 0.25;
        float opticalDepth = 0.0f;

        Vec3 currentPos = origin;
        Vec3 curDir = initialDir;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        while (currentDist < maxDist) {
            double stepSize = 1.35;
            currentPos = currentPos.add(curDir.scale(stepSize));

            pos.set(Math.floor(currentPos.x), Math.floor(currentPos.y), Math.floor(currentPos.z));
            BlockState state = level.getBlockState(pos);
            float density = MaterialDensityRegistry.getDensity(state);

            if (density > 0.05f) {
                opticalDepth += RadiationType.GAMMA.getAttenuationCoeff() * density * (float) stepSize * 8.0f;
            } else {
                opticalDepth += RadiationType.GAMMA.getAttenuationCoeff() * airDensity * (float) stepSize;
            }

            if (opticalDepth > 4.5f) {
                break;
            }

            if (curDir.y > 0.35 && currentPos.y > (origin.y + 15.0) && level.canSeeSky(pos)) {
                if (random.nextFloat() < 0.045f) {
                    traceStochasticAtmosphericScatterRay(level, currentPos, energyMeV * 0.45, targets, random);
                    break;
                }
            }

            double hitDoseRoentgen = energyMeV * ROENTGEN_PER_MEV * RadiationType.GAMMA.getRadiationWeight();
            if (checkHitAndCapture(targets, emitter, currentPos, RadiationType.GAMMA, hitDoseRoentgen)) {
                break;
            }

            currentDist += stepSize;
        }
    }

    private static void traceStochasticAtmosphericScatterRay(ServerLevel level, Vec3 skyScatterPoint, double scatterEnergyMeV, List<LivingEntity> targets, RandomSource random) {
        double phi = random.nextDouble() * 2.0 * Math.PI;
        double cosTheta = -(0.35 + random.nextDouble() * 0.65);
        double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);

        Vec3 scatterDir = new Vec3(sinTheta * Math.cos(phi), cosTheta, sinTheta * Math.sin(phi)).normalize();

        Vec3 currentPos = skyScatterPoint;
        double currentDist = 0.5;
        double maxDist = 160.0;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        while (currentDist < maxDist) {
            double stepSize = 1.6;
            currentPos = currentPos.add(scatterDir.scale(stepSize));

            pos.set(Math.floor(currentPos.x), Math.floor(currentPos.y), Math.floor(currentPos.z));
            BlockState state = level.getBlockState(pos);
            if (MaterialDensityRegistry.getDensity(state) > 0.05f) {
                break;
            }

            double hitDoseRoentgen = scatterEnergyMeV * ROENTGEN_PER_MEV * RadiationType.GAMMA.getRadiationWeight();
            if (checkHitAndCapture(targets, null, currentPos, RadiationType.GAMMA, hitDoseRoentgen)) {
                break;
            }

            currentDist += stepSize;
        }
    }

    public static void traceDiffuseEnvironmentalBackground(ServerLevel level, double worldX, double worldZ, List<LivingEntity> targets, RandomSource random) {
        if (random.nextFloat() < 0.35f) {
            double startY = Math.min(319.0, level.getMaxBuildHeight() - 5.0);
            Vec3 muonStart = new Vec3(worldX, startY, worldZ);

            double theta = random.nextDouble() * 0.35;
            double phi = random.nextDouble() * 2.0 * Math.PI;
            Vec3 muonDir = new Vec3(Math.sin(theta) * Math.cos(phi), -Math.cos(theta), Math.sin(theta) * Math.sin(phi)).normalize();

            Vec3 cur = muonStart;
            BlockPos.MutableBlockPos bPos = new BlockPos.MutableBlockPos();
            for (int i = 0; i < 90; i++) {
                cur = cur.add(muonDir.scale(2.5));

                bPos.set(Math.floor(cur.x), Math.floor(cur.y), Math.floor(cur.z));
                if (MaterialDensityRegistry.getDensity(level.getBlockState(bPos)) > 3.0f && random.nextFloat() < 0.08f) {
                    break;
                }

                if (checkHitAndCapture(targets, null, cur, RadiationType.BETA, 0.000000025)) {
                    break;
                }
            }
        }

        if (random.nextFloat() < 0.25f) {
            int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) worldX, (int) worldZ);
            if (groundY > level.getMinBuildHeight()) {
                double gy = groundY - random.nextInt(20);
                Vec3 gStart = new Vec3(worldX, gy, worldZ);
                Vec3 gDir = getRandomSphereDirection(random);

                Vec3 cur = gStart;
                for (int i = 0; i < 8; i++) {
                    cur = cur.add(gDir.scale(0.8));

                    if (checkHitAndCapture(targets, null, cur, RadiationType.GAMMA, 0.000000018)) {
                        break;
                    }
                }
            }
        }
    }

    public static void traceNeutronParticle(ServerLevel level, Vec3 origin, Vec3 dir,
                                            LivingEntity emitter, List<LivingEntity> targets, RandomSource random) {
        float airDensity = getAtmosphericDensity(origin.y);
        double energyMeV = 1.2 + Math.pow(random.nextDouble(), 2.0) * 10.0;
        double maxDist = (100.0 + (energyMeV * 14.0)) / Math.max(0.05f, airDensity);
        double currentDist = 0.30;

        Vec3 currentPos = origin;
        Vec3 curDir = dir;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        while (currentDist < maxDist) {
            double stepSize = 1.40;
            currentPos = currentPos.add(curDir.scale(stepSize));

            pos.set(Math.floor(currentPos.x), Math.floor(currentPos.y), Math.floor(currentPos.z));
            BlockState state = level.getBlockState(pos);

            if (state.is(Blocks.WATER) || state.getBlock().toString().contains("wood") || state.getBlock().toString().contains("planks")) {
                if (random.nextFloat() < 0.35f) {
                    break;
                }
                curDir = getRandomSphereDirection(random);
            } else if (MaterialDensityRegistry.getDensity(state) > 7.0f) {
                InducedActivationManager.irradiateWithNeutrons(level, pos, 450.0);
                if (random.nextFloat() < 0.50f) {
                    break;
                }
            }

            double hitDoseRoentgen = energyMeV * ROENTGEN_PER_MEV * RadiationType.NEUTRON.getRadiationWeight();
            if (checkHitAndCapture(targets, emitter, currentPos, RadiationType.NEUTRON, hitDoseRoentgen)) {
                break;
            }

            currentDist += stepSize;
        }
    }

    private static boolean checkHitAndCapture(List<LivingEntity> targets, LivingEntity emitter, Vec3 point, RadiationType type, double doseRoentgen) {
        for (LivingEntity target : targets) {
            if (target == emitter) {
                continue;
            }

            AABB hitbox = target.getBoundingBox().inflate(0.12);
            if (hitbox.contains(point)) {
                EntityRadiation rad = target.getData(by.righttwixys.isotopix.init.ModAttachments.RADIATION);
                rad.recordParticleHit(type, doseRoentgen);
                return true;
            }
        }
        return false;
    }
}