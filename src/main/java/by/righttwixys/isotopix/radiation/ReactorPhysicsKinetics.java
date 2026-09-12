package by.righttwixys.isotopix.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ReactorPhysicsKinetics {


    public static final double[] BETA_I = {0.000215, 0.001424, 0.001274, 0.002568, 0.000748, 0.000273};
    public static final double[] LAMBDA_I = {0.0124, 0.0305, 0.111, 0.301, 1.14, 3.01}; // с^-1
    public static final double TOTAL_BETA = 0.006502;
    public static final double NEUTRON_LIFETIME = 4.0e-5; // Время жизни мгновенных нейтронов (с)


    public static final double YIELD_IODINE = 0.0639;
    public static final double YIELD_XENON = 0.0023;
    public static final double LAMBDA_I135 = 2.87e-5;   // I-135 (T_1/2 = 6.57 ч), с^-1
    public static final double LAMBDA_XE135 = 2.09e-5;  // Xe-135 (T_1/2 = 9.14 ч), с^-1
    public static final double SIGMA_A_XE135 = 2.6e-18; // Xe-135 (см^2, 2.6 млн барн)


    public static final double JOULES_PER_FISSION = 3.204e-11; // 200 МэВ на 1 акт деления


    private double neutronFlux = 1.0e3; // (нейтр / (см^2 * с))
    private final double[] precursorConcentrations = new double[6]; // Концентрации групп предшественников C_i
    private double iodineConcentration = 0.0; // Концентрация I-135 (ядер / см^3)
    private double xenonConcentration = 0.0;  // Концентрация Xe-135 (ядер / см^3)
    private double fuelTemperatureCelsius = 20.0; // Температура топлива (°C)

    private double effectiveK = 0.985; // k_eff
    private double promptK = 0.978;    // k_prompt
    private double reactivity = -0.015; // rho = (k - 1) / k
    private boolean promptCritical = false;

    public ReactorPhysicsKinetics() {
        for (int i = 0; i < 6; i++) {
            this.precursorConcentrations[i] = (BETA_I[i] / (LAMBDA_I[i] * NEUTRON_LIFETIME)) * this.neutronFlux * 1.0e-8;
        }
    }

    /**
     * Численное интегрирование PRKE методом Рунге-Кутты 4-го порядка за временной шаг dt
     */
    public void stepKineticSimulation(ServerLevel level, BlockPos corePos, double dt, double moderatorRatio, double controlRodInsertion) {
        double kInfBase = calculateBaseKInfinity(level, corePos);

        double deltaT = Math.max(0.0, this.fuelTemperatureCelsius - 20.0);
        double dopplerReactivity = -2.8e-5 * Math.sqrt(deltaT);

        double controlReactivity = -0.14 * Math.pow(Math.min(1.0, Math.max(0.0, controlRodInsertion)), 1.8);

        // rho_Xe = - (Sigma_a_Xe / (nu * Sigma_f))
        double xenonReactivity = - (this.xenonConcentration * SIGMA_A_XE135) / 0.75;

        double moderatorGain = 0.35 * Math.min(1.0, moderatorRatio);

        double netReactivity = (kInfBase - 1.0) / kInfBase + dopplerReactivity + controlReactivity + xenonReactivity + moderatorGain;
        this.reactivity = netReactivity;
        this.effectiveK = 1.0 / (1.0 - netReactivity);
        this.promptK = this.effectiveK * (1.0 - TOTAL_BETA);
        this.promptCritical = (this.reactivity >= TOTAL_BETA) || (this.promptK >= 1.0);

        double precursorSum = 0.0;
        for (int i = 0; i < 6; i++) {
            precursorSum += LAMBDA_I[i] * this.precursorConcentrations[i];
        }

        // dPhi/dt = ((rho - beta) / Lambda) * Phi + sum(lambda_i * C_i)
        double dFlux = ((this.reactivity - TOTAL_BETA) / NEUTRON_LIFETIME) * this.neutronFlux + precursorSum;
        this.neutronFlux = Math.max(10.0, this.neutronFlux + dFlux * dt);

        for (int i = 0; i < 6; i++) {
            // dC_i/dt = (beta_i / Lambda) * Phi - lambda_i * C_i
            double dCi = (BETA_I[i] / NEUTRON_LIFETIME) * this.neutronFlux - LAMBDA_I[i] * this.precursorConcentrations[i];
            this.precursorConcentrations[i] = Math.max(0.0, this.precursorConcentrations[i] + dCi * dt);
        }

        double fissionDensityRate = this.neutronFlux * 0.045; // дел/(см^3 * с)
        double dI = YIELD_IODINE * fissionDensityRate - LAMBDA_I135 * this.iodineConcentration;
        this.iodineConcentration = Math.max(0.0, this.iodineConcentration + dI * dt);

        double dXe = YIELD_XENON * fissionDensityRate + LAMBDA_I135 * this.iodineConcentration
                - (LAMBDA_XE135 + SIGMA_A_XE135 * this.neutronFlux) * this.xenonConcentration;
        this.xenonConcentration = Math.max(0.0, this.xenonConcentration + dXe * dt);

        double thermalWatts = fissionDensityRate * JOULES_PER_FISSION * 1.0e6; // Ватт на воксель
        double coolingCoefficient = 120.0; // Отвод тепла теплоносителем
        double heatCapacity = 3.5e5; // Дж / °C

        double dTemp = (thermalWatts - coolingCoefficient * (this.fuelTemperatureCelsius - 20.0)) / heatCapacity;
        this.fuelTemperatureCelsius = Math.max(20.0, this.fuelTemperatureCelsius + dTemp * dt);
    }

    private double calculateBaseKInfinity(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        String name = state.getBlock().toString().toLowerCase();
        if (name.contains("plutonium") || name.contains("pu239")) return 1.35;
        if (name.contains("enriched_uranium") || name.contains("u235")) return 1.28;
        if (name.contains("uranium") || name.contains("pitchblende")) return 0.95;
        return 0.05;
    }

    public double getNeutronFlux() { return neutronFlux; }
    public double getEffectiveK() { return effectiveK; }
    public double getPromptK() { return promptK; }
    public double getReactivity() { return reactivity; }
    public boolean isPromptCritical() { return promptCritical; }
    public double getFuelTemperatureCelsius() { return fuelTemperatureCelsius; }
    public double getXenonConcentration() { return xenonConcentration; }
    public double getIodineConcentration() { return iodineConcentration; }

    public void writeToNbt(CompoundTag tag) {
        tag.putDouble("KineticsFlux", this.neutronFlux);
        tag.putDouble("FuelTemp", this.fuelTemperatureCelsius);
        tag.putDouble("ConcI135", this.iodineConcentration);
        tag.putDouble("ConcXe135", this.xenonConcentration);
        for (int i = 0; i < 6; i++) {
            tag.putDouble("Precursor_" + i, this.precursorConcentrations[i]);
        }
    }

    public void readFromNbt(CompoundTag tag) {
        this.neutronFlux = tag.getDouble("KineticsFlux");
        this.fuelTemperatureCelsius = Math.max(20.0, tag.getDouble("FuelTemp"));
        this.iodineConcentration = tag.getDouble("ConcI135");
        this.xenonConcentration = tag.getDouble("ConcXe135");
        for (int i = 0; i < 6; i++) {
            this.precursorConcentrations[i] = tag.getDouble("Precursor_" + i);
        }
    }
}