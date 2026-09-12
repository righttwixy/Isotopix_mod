package by.righttwixys.isotopix.radiation;

import java.util.Random;

public final class RadiationSpectra {

    private static final float ELECTRON_REST_MASS_MEV = 0.51099895f;


    private static final double WATT_A_CF252 = 1.025;
    private static final double WATT_B_CF252 = 2.926;
    private static final double WATT_PEAK_CF252 = 0.85;
    private static final double WATT_MAX_VAL_CF252 = Math.exp(-WATT_PEAK_CF252 / WATT_A_CF252) * Math.sinh(Math.sqrt(WATT_B_CF252 * WATT_PEAK_CF252));

    private RadiationSpectra() {
    }
    public static float sampleWattFissionNeutron(Random rnd) {
        for (int i = 0; i < 64; i++) {
            double candidateEnergy = 0.05 + rnd.nextDouble() * 12.0; // Спектр от 50 кэВ до 12 МэВ
            double prob = Math.exp(-candidateEnergy / WATT_A_CF252) * Math.sinh(Math.sqrt(WATT_B_CF252 * candidateEnergy));
            if (rnd.nextDouble() * (WATT_MAX_VAL_CF252 * 1.05) <= prob) {
                return (float) candidateEnergy;
            }
        }

        double xi1 = Math.max(1e-7, rnd.nextDouble());
        double xi2 = Math.max(1e-7, rnd.nextDouble());
        return (float) (-WATT_A_CF252 * (Math.log(xi1) + Math.log(xi2) * Math.pow(Math.cos(Math.PI * 0.5 * rnd.nextDouble()), 2.0)));
    }

    /**
     * Выборка энергии бета-электрона по спектру Ферми (трёхчастичный распад)
     */
    public static float sampleFermiBeta(Random rnd, float eMaxMeV) {
        float eMax = Math.max(0.05f, eMaxMeV);
        // E_max / 3
        float ePeak = eMax * 0.333f;
        float maxVal = fermiDensity(ePeak, eMax);

        for (int i = 0; i < 48; i++) {
            float eCandidate = 0.005f + rnd.nextFloat() * (eMax - 0.005f);
            float p = fermiDensity(eCandidate, eMax);
            if (rnd.nextFloat() * maxVal <= p) {
                return eCandidate;
            }
        }
        return eMax * (0.20f + rnd.nextFloat() * 0.35f);
    }

    private static float fermiDensity(float e, float eMax) {
        if (e <= 0.0f || e >= eMax) return 0.0f;
        float momentum = (float) Math.sqrt(e * (e + 2.0f * ELECTRON_REST_MASS_MEV));
        float totalEnergy = e + ELECTRON_REST_MASS_MEV;
        float neutrinoFactor = (eMax - e) * (eMax - e);
        return momentum * totalEnergy * neutrinoFactor;
    }

    /**
     * Выборка энергии альфа-частицы с физическим уширением пика Брэгга
     */
    public static float sampleAlphaEnergy(Random rnd, float nominalMeV) {

        float straggling = (float) (rnd.nextGaussian() * 0.035);
        return Math.max(0.5f, nominalMeV + straggling);
    }

    /**
     * Комплексная дисперсия энергии кванта в зависимости от типа излучения
     */
    public static float sampleParticleEnergy(Random rnd, byte particleType, float baseEnergyMeV) {
        return switch (particleType) {
            case 0 -> sampleAlphaEnergy(rnd, baseEnergyMeV);
            case 1 -> sampleFermiBeta(rnd, baseEnergyMeV > 0 ? baseEnergyMeV : 1.2f);
            case 2 -> {
                if (rnd.nextFloat() < 0.30f) {
                    yield baseEnergyMeV * (0.25f + rnd.nextFloat() * 0.70f);
                }
                yield baseEnergyMeV;
            }
            case 3 -> sampleWattFissionNeutron(rnd);
            default -> baseEnergyMeV;
        };
    }
}
// Я заебался