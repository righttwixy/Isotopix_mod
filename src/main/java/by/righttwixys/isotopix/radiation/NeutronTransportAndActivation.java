package by.righttwixys.isotopix.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public final class NeutronTransportAndActivation {

    private NeutronTransportAndActivation() {}

    public enum NeutronGroup {
        FAST(1),       // E > 100 кэВ (пороги деления, скалывание)
        EPITHERMAL(2), // 1 эВ <= E <= 100 кэВ (резонансный захват U-238)
        THERMAL(3);    // E ~ 0.025 эВ (деление U-235, радиационный захват)

        public final int id;
        NeutronGroup(int id) { this.id = id; }
    }

    /**
     * расчет среднего логарифмического декремента энергии \xi по массовому числу A
     * \xi = 1 + ((A - 1)^2 / (2A)) * ln((A - 1) / (A + 1))
     */
    public static double calculateLogarithmicEnergyDecrement(double atomicMassA) {
        if (atomicMassA <= 1.008) {
            return 1.0; // Водород (вода, парафин)
        }
        double term = ((atomicMassA - 1.0) * (atomicMassA - 1.0)) / (2.0 * atomicMassA);
        return 1.0 + term * Math.log((atomicMassA - 1.0) / (atomicMassA + 1.0));
    }

    /**
     * Моделдирование столкновения нейтрона с вокселем замедлителя: термализация и деградация энергии
     */
    public static float processNeutronCollision(BlockState state, float currentEnergyMeV, Random rnd) {
        double atomicMassA;
        if (state.is(Blocks.WATER)) {
            atomicMassA = 1.0;
        } else if (state.getBlock().toString().contains("wood") || state.getBlock().toString().contains("planks")) {
            atomicMassA = 1.6;
        } else if (state.getBlock().toString().contains("coal") || state.is(Blocks.BLACKSTONE)) {
            atomicMassA = 12.0;
        } else if (state.is(Blocks.IRON_BLOCK)) {
            atomicMassA = 55.85;
        } else if (state.is(Blocks.GOLD_BLOCK)) {
            atomicMassA = 196.97;
        } else {
            atomicMassA = 28.08;
        }

        double xi = calculateLogarithmicEnergyDecrement(atomicMassA);
        // ln(E_initial / E_final) = xi
        double energyLossRatio = Math.exp(-xi * (0.65 + rnd.nextDouble() * 0.70));
        float newEnergy = (float) (currentEnergyMeV * energyLossRatio);

        // 20°C: kT ~ 0.0253 эВ = 2.53e-8 МэВ
        return Math.max(2.53e-8f, newEnergy);
    }

    public static NeutronGroup classifyEnergyGroup(float energyMeV) {
        if (energyMeV > 0.1f) return NeutronGroup.FAST;
        if (energyMeV >= 1.0e-6f) return NeutronGroup.EPITHERMAL;
        return NeutronGroup.THERMAL;
    }


    public static void processThermalNeutronCapture(ServerLevel level, BlockPos pos, BlockState state, float thermalFlux) {
        if (thermalFlux <= 1.0f) return;

        // Co-59(n, gamma)Co-60 (T_1/2 = 5.27 лет, 1.17 и 1.33 МэВ гамма)
        if (state.is(Blocks.IRON_BLOCK) || state.is(Blocks.ANVIL) || state.is(Blocks.HEAVY_CORE)) {
            double activatedBq = thermalFlux * 1.8e-4; // Сечение захвата Co-59 ~ 37.2 барн
            InducedActivationManager.depositIsotopeContamination(level, pos, activatedBq, (byte) 2, 5.27 * 365.25 * 86400.0);
        }

        // O-16(n, p)N-16 (T_1/2 = 7.13 с, сверхжесткая гамма 6.13 МэВ)
        else if (state.is(Blocks.WATER)) {
            double activatedBq = thermalFlux * 4.5e-5;
            InducedActivationManager.depositIsotopeContamination(level, pos, activatedBq, (byte) 2, 7.13);
        }

        // Na-23(n, gamma)Na-24 (T_1/2 = 15 ч)
        else if (state.is(Blocks.STONE) || state.is(Blocks.COBBLESTONE) || state.is(Blocks.SMOOTH_STONE)) {
            double activatedBq = thermalFlux * 8.2e-5;
            InducedActivationManager.depositIsotopeContamination(level, pos, activatedBq, (byte) 2, 15.0 * 3600.0);
        }
    }
}