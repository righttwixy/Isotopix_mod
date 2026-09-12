package by.righttwixys.isotopix.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class AtmosphericPlumeAndBiokinetics {

    public static final float WT_LUNG = 0.12f;
    public static final float WT_BONE_MARROW = 0.12f;
    public static final float WT_COLON = 0.12f;
    public static final float WT_THYROID = 0.04f;
    public static final float WT_SKIN = 0.01f;

    private double thyroidIodine131Bq = 0.0; //(T_bio = 80 сут, T_phys = 8.02 сут)
    private double boneStrontium90Bq = 0.0;  //(T_bio = 30 лет, T_phys = 28.8 лет)
    private double muscleCesium137Bq = 0.0;  //(T_bio = 110 сут, T_phys = 30.17 лет)

    /**
     * Аналитический расчет гауссовой концентрации C(x,y,z) атмосферного шлейфа по Пасквиллу-Гилфорду
     */
    public static double calculateGaussianPlumeConcentration(
            double qEmissionBq,
            Vec3 releasePoint,
            Vec3 targetPoint,
            Vec3 windVector,
            boolean isRaining
    ) {
        Vec3 diff = targetPoint.subtract(releasePoint);
        double windSpeed = Math.max(0.5, windVector.length());
        Vec3 windDir = windVector.normalize();

        double downwindX = diff.dot(windDir);
        if (downwindX <= 0.5) return 0.0;

        Vec3 crosswindVec = diff.subtract(windDir.scale(downwindX));
        double crosswindY = Math.sqrt(crosswindVec.x * crosswindVec.x + crosswindVec.z * crosswindVec.z);
        double verticalZ = targetPoint.y;
        double releaseH = releasePoint.y;


        double sigmaY = 0.08 * downwindX * Math.pow(1.0 + 0.0001 * downwindX, -0.5);
        double sigmaZ = 0.06 * downwindX * Math.pow(1.0 + 0.0015 * downwindX, -0.5);

        double expY = Math.exp(- (crosswindY * crosswindY) / (2.0 * sigmaY * sigmaY));
        double expZ1 = Math.exp(- Math.pow(verticalZ - releaseH, 2.0) / (2.0 * sigmaZ * sigmaZ));
        double expZ2 = Math.exp(- Math.pow(verticalZ + releaseH, 2.0) / (2.0 * sigmaZ * sigmaZ));

        double plumeConc = (qEmissionBq / (2.0 * Math.PI * windSpeed * sigmaY * sigmaZ)) * expY * (expZ1 + expZ2);


        if (isRaining) {
            double timeInFlight = downwindX / windSpeed;
            plumeConc *= Math.exp(- 1.0e-4 * timeInFlight);
        }

        return Math.max(0.0, plumeConc);
    }

    /**
     * Поглощение радионуклидов организмом и распределение по органам-мишеням
     */
    public void ingestNuclideSpecies(String isotopeId, double activityBq) {
        String lower = isotopeId.toLowerCase();
        if (lower.contains("i131") || lower.contains("iodine")) {

            this.thyroidIodine131Bq += activityBq * 0.30;
        } else if (lower.contains("sr90") || lower.contains("strontium")) {

            this.boneStrontium90Bq += activityBq * 0.50;
        } else if (lower.contains("cs137") || lower.contains("cesium")) {

            this.muscleCesium137Bq += activityBq * 0.80;
        }
    }

    /**
     * Симуляция внутреннего облучения органов за 1 серверный такт (0.05 с)
     */
    public float tickBiokinetics(ServerPlayer player, EntityRadiation rad) {
        double dt = 0.05;


        // I-131: T_eff ~ 7.3 дня
        double lambdaEffI131 = (Math.log(2.0) / (7.3 * 86400.0));
        // Sr-90: T_eff ~ 14.7 лет
        double lambdaEffSr90 = (Math.log(2.0) / (14.7 * 365.25 * 86400.0));
        // Cs-137: T_eff ~ 108 дней
        double lambdaEffCs137 = (Math.log(2.0) / (108.0 * 86400.0));


        // I-131: E_beta_avg = 0.19 МэВ + gamma
        double thyroidDoseSv = (this.thyroidIodine131Bq * 3.8e-14) * dt;
        // Sr-90 (+Y-90): E_beta_avg = 0.93 МэВ
        double boneDoseSv = (this.boneStrontium90Bq * 1.5e-13) * dt;
        // Cs-137 (+Ba-137m): E_beta_avg = 0.17 МэВ, E_gamma = 0.662 МэВ
        double muscleDoseSv = (this.muscleCesium137Bq * 5.2e-14) * dt;

        // E = sum(w_T * H_T)
        double totalEffectiveDoseSv = (thyroidDoseSv * WT_THYROID) + (boneDoseSv * WT_BONE_MARROW) + (muscleDoseSv * WT_COLON);
        float roentgenDose = (float) (totalEffectiveDoseSv / 0.0096);

        if (roentgenDose > 0.0f) {
            rad.recordParticleHit(RadiationType.BETA, roentgenDose, 1);
        }


        this.thyroidIodine131Bq = Math.max(0.0, this.thyroidIodine131Bq * (1.0 - lambdaEffI131 * dt));
        this.boneStrontium90Bq = Math.max(0.0, this.boneStrontium90Bq * (1.0 - lambdaEffSr90 * dt));
        this.muscleCesium137Bq = Math.max(0.0, this.muscleCesium137Bq * (1.0 - lambdaEffCs137 * dt));

        return roentgenDose;
    }

    public double getThyroidIodine131Bq() { return thyroidIodine131Bq; }
    public double getBoneStrontium90Bq() { return boneStrontium90Bq; }
    public double getMuscleCesium137Bq() { return muscleCesium137Bq; }

    public void writeNbt(CompoundTag tag) {
        tag.putDouble("ThyroidI131", this.thyroidIodine131Bq);
        tag.putDouble("BoneSr90", this.boneStrontium90Bq);
        tag.putDouble("MuscleCs137", this.muscleCesium137Bq);
    }

    public void readNbt(CompoundTag tag) {
        this.thyroidIodine131Bq = tag.getDouble("ThyroidI131");
        this.boneStrontium90Bq = tag.getDouble("BoneSr90");
        this.muscleCesium137Bq = tag.getDouble("MuscleCs137");
    }
}