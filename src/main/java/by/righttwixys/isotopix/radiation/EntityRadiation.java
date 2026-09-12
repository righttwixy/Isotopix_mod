package by.righttwixys.isotopix.radiation;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class EntityRadiation implements INBTSerializable<CompoundTag> {

    private float accumulatedDoseRoentgen = 0.0f;
    private float currentDoseRateMicroRPerHour = 0.0f;
    private float dnaDamageUnits = 0.0f;
    private double ingestedRadionuclidesBq = 0.0;

    private int currentTickHits = 0;
    private int countsPerSecond = 0;
    private int hitAccumulatorSecond = 0;
    private double doseAccumulatorSecond = 0.0;
    private int tickTimer = 0;

    public void recordParticleHit(RadiationType type, double doseRoentgen, int count) {
        double safeDose = Math.max(0.0, doseRoentgen);
        int safeCount = Math.max(0, count);

        this.currentTickHits += safeCount;
        this.hitAccumulatorSecond += safeCount;
        this.doseAccumulatorSecond += safeDose;
        this.accumulatedDoseRoentgen += (float) safeDose;

        float qualityFactor = (float) type.getRadiationWeight();
        this.dnaDamageUnits += (float) (safeDose * 1000.0 * qualityFactor);
    }

    public void recordParticleHit(RadiationType type, double doseRoentgen) {
        recordParticleHit(type, doseRoentgen, 1);
    }

    public void ingestRadionuclide(double activityBq) {
        this.ingestedRadionuclidesBq = Math.min(500_000_000.0, Math.max(0.0, this.ingestedRadionuclidesBq + activityBq));
    }

    public void cureInternalContamination(float fraction) {
        this.ingestedRadionuclidesBq = Math.max(0.0, this.ingestedRadionuclidesBq * Math.max(0.0, 1.0 - fraction));
        this.dnaDamageUnits = Math.max(0.0f, this.dnaDamageUnits * 0.65f);
    }

    public void updateDetectionAndBiology(float envBackgroundMicroR) {
        this.tickTimer++;
        if (this.tickTimer >= 20) {
            this.countsPerSecond = Math.max(0, this.hitAccumulatorSecond);

            float measuredRate = (float) (Math.max(0.0, this.doseAccumulatorSecond) * 3600.0 * 1_000_000.0);
            this.currentDoseRateMicroRPerHour = Math.max(0.0f, measuredRate + Math.max(0.0f, envBackgroundMicroR));

            this.hitAccumulatorSecond = 0;
            this.doseAccumulatorSecond = 0.0;
            this.tickTimer = 0;
        }

        double dt = 0.05;

        if (this.ingestedRadionuclidesBq > 1.0) {
            double internalDoseTick = (this.ingestedRadionuclidesBq * 1.5e-11) * dt;
            this.accumulatedDoseRoentgen += (float) Math.max(0.0, internalDoseTick);
            this.dnaDamageUnits += (float) (internalDoseTick * 2000.0);

            double lambdaBio = Math.log(2.0) / 210.0;
            this.ingestedRadionuclidesBq = Math.max(0.0, this.ingestedRadionuclidesBq * Math.exp(-lambdaBio * dt));
        } else {
            this.ingestedRadionuclidesBq = 0.0;
        }

        if (this.currentTickHits == 0 && this.dnaDamageUnits > 0.0f) {
            this.dnaDamageUnits = Math.max(0.0f, this.dnaDamageUnits - 0.08f);
        }

        if (this.currentTickHits == 0 && this.accumulatedDoseRoentgen > 0.0f) {
            this.accumulatedDoseRoentgen = Math.max(0.0f, this.accumulatedDoseRoentgen * 0.99998f);
        }

        this.currentTickHits = 0;
    }

    public float getAccumulatedDoseRoentgen() {
        return Math.max(0.0f, accumulatedDoseRoentgen);
    }

    public void setAccumulatedDoseRoentgen(float accumulatedDoseRoentgen) {
        this.accumulatedDoseRoentgen = Math.max(0.0f, accumulatedDoseRoentgen);
    }

    public float getAccumulatedDoseSieverts() {
        return Math.max(0.0f, this.accumulatedDoseRoentgen * 0.0096f);
    }

    public float getCurrentDoseRateMicroRPerHour() {
        return Math.max(0.0f, currentDoseRateMicroRPerHour);
    }

    public void setCurrentDoseRateMicroRPerHour(float currentDoseRateMicroRPerHour) {
        this.currentDoseRateMicroRPerHour = Math.max(0.0f, currentDoseRateMicroRPerHour);
    }

    public int getCountsPerSecond() {
        return Math.max(0, countsPerSecond);
    }

    public void setCountsPerSecond(int countsPerSecond) {
        this.countsPerSecond = Math.max(0, countsPerSecond);
    }

    public float getDnaDamageUnits() {
        return Math.max(0.0f, dnaDamageUnits);
    }

    public void setDnaDamageUnits(float dnaDamageUnits) {
        this.dnaDamageUnits = Math.max(0.0f, dnaDamageUnits);
    }

    public double getInternalContaminationBq() {
        return Math.max(0.0, ingestedRadionuclidesBq);
    }

    public double getIngestedRadionuclidesBq() {
        return Math.max(0.0, ingestedRadionuclidesBq);
    }

    public void setIngestedRadionuclidesBq(double ingestedRadionuclidesBq) {
        this.ingestedRadionuclidesBq = Math.max(0.0, ingestedRadionuclidesBq);
    }

    public int getCurrentTickHits() {
        return Math.max(0, currentTickHits);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("AccumulatedDose", Math.max(0.0f, accumulatedDoseRoentgen));
        tag.putFloat("CurrentDoseRate", Math.max(0.0f, currentDoseRateMicroRPerHour));
        tag.putInt("CountsPerSecond", Math.max(0, countsPerSecond));
        tag.putFloat("DnaDamage", Math.max(0.0f, dnaDamageUnits));
        tag.putDouble("IngestedActivity", Math.max(0.0, ingestedRadionuclidesBq));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.accumulatedDoseRoentgen = Math.max(0.0f, tag.getFloat("AccumulatedDose"));
        this.currentDoseRateMicroRPerHour = Math.max(0.0f, tag.getFloat("CurrentDoseRate"));
        this.countsPerSecond = Math.max(0, tag.getInt("CountsPerSecond"));
        this.dnaDamageUnits = Math.max(0.0f, tag.getFloat("DnaDamage"));
        this.ingestedRadionuclidesBq = Math.max(0.0, tag.getDouble("IngestedActivity"));
    }
}