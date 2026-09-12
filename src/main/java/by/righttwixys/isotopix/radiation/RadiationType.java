package by.righttwixys.isotopix.radiation;

public enum RadiationType {
    /**
     * Альфа-излучение (ядра He-4): дальность в нормальном воздухе всего 2-9 см (0.25 блока).
     * Мгновенно задерживается любым препятствием, кожей, бумагой.
     */
    ALPHA(20.0f, 0.25f, 95.0f, false),

    /**
     * Бета-излучение (электроны e-): дальность от миллиметров до 11 метров (11 блоков).
     * Испытывает кулоновское рассеяние и торможение в металлах.
     */
    BETA(1.0f, 11.5f, 1.25f, false),

    /**
     * Гамма-излучение (фотоны высокой энергии): дальность в атмосфере сотни метров (до 380+ блоков).
     * Экспоненциальное затухание (слой половинного ослабления воздуха ~90м).
     */
    GAMMA(1.0f, 384.0f, 0.0075f, true),

    /**
     * Нейтроны деления: в воздухе пролетают 150-300 метров (до 256 блоков).
     * Замедляются только на лёгких ядрах (водород в воде и дереве).
     */
    NEUTRON(10.0f, 256.0f, 0.012f, true);

    private final float radiationWeight;      // Коэффициент качества излучения wR
    private final float maxAirRange;          // Предельная физическая дистанция в воздухе
    private final float attenuationCoeff;     // Коэффициент линейного ослабления в воздухе
    private final boolean penetratesThinWalls;

    RadiationType(float radiationWeight, float maxAirRange, float attenuationCoeff, boolean penetratesThinWalls) {
        this.radiationWeight = radiationWeight;
        this.maxAirRange = maxAirRange;
        this.attenuationCoeff = attenuationCoeff;
        this.penetratesThinWalls = penetratesThinWalls;
    }

    public float getRadiationWeight() {
        return radiationWeight;
    }

    public float getMaxAirRange() {
        return maxAirRange;
    }

    public float getAttenuationCoeff() {
        return attenuationCoeff;
    }

    public boolean canPenetrateThinWalls() {
        return penetratesThinWalls;
    }
}