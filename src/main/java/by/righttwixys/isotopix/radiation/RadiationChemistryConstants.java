package by.righttwixys.isotopix.radiation;

import java.util.HashMap;
import java.util.Map;

public class RadiationChemistryConstants {
    private static final Map<String, Double> SPECIFIC_THERMAL_POWER = new HashMap<>();

    static {
        SPECIFIC_THERMAL_POWER.put("po210", 140.0);
        SPECIFIC_THERMAL_POWER.put("po209", 0.53);
        SPECIFIC_THERMAL_POWER.put("cm242", 120.0);
        SPECIFIC_THERMAL_POWER.put("cm243", 1.6);
        SPECIFIC_THERMAL_POWER.put("cm244", 2.8);
        SPECIFIC_THERMAL_POWER.put("pu238", 0.57);
        SPECIFIC_THERMAL_POWER.put("pu236", 18.0);
        SPECIFIC_THERMAL_POWER.put("co60",  17.4);
        SPECIFIC_THERMAL_POWER.put("cs137", 0.42);
        SPECIFIC_THERMAL_POWER.put("sr90",  0.93);
        SPECIFIC_THERMAL_POWER.put("cf250", 4.8);
        SPECIFIC_THERMAL_POWER.put("cf252", 38.5);
        SPECIFIC_THERMAL_POWER.put("co252", 38.5);
        SPECIFIC_THERMAL_POWER.put("es253", 1000.0);
        SPECIFIC_THERMAL_POWER.put("es254", 75.0);
        SPECIFIC_THERMAL_POWER.put("fm255", 3500.0);
        SPECIFIC_THERMAL_POWER.put("ac225", 9.2);
        SPECIFIC_THERMAL_POWER.put("ra223", 22.0);
    }

    public static double getSpecificHeatWattsPerGram(String isotopeId) {
        return SPECIFIC_THERMAL_POWER.getOrDefault(isotopeId.toLowerCase(), 0.0);
    }

    public static double calculateBremsstrahlungYield(float materialDensity, double betaEnergyMeV) {
        double effectiveZ = Math.min(82.0, Math.max(1.0, materialDensity * 7.5));
        return Math.min(0.25, 3.5e-4 * effectiveZ * betaEnergyMeV);
    }
}