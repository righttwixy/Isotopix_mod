package by.righttwixys.isotopix.radiation;

import by.righttwixys.isotopix.Isotopix;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class DecayChainManager {

    public static final String TAG_SNF_AGE_TICKS = "IsotopixSnfAge";
    public static final String TAG_DECAY_PROGRESS = "IsotopixDecayProgress";

    private DecayChainManager() {
    }


    public static boolean isUraniumBearingSource(String itemId) {
        String lower = itemId.toLowerCase();
        return lower.contains("uranium") || lower.contains("pitchblende") || lower.contains("uraninite");
    }
    public static boolean isSpentNuclearFuel(String itemId) {
        String lower = itemId.toLowerCase();
        return lower.contains("spent_fuel") || lower.contains("snf") || lower.contains("spent_rod") || lower.contains("spent_pellet");
    }
    public static void processRadonExhalation(ServerLevel level, Vec3 emissionPos, int stackCount, float baseActivityBq) {
        if (baseActivityBq <= 1.0f) return;

        float radonYieldBq = (float) (baseActivityBq * stackCount * 0.00045);
        if (radonYieldBq < 1.0f) return;


        BlockPos currentPos = BlockPos.containing(emissionPos);
        BlockPos lowestFloor = currentPos;

        for (int dy = 1; dy <= 6; dy++) {
            BlockPos down = currentPos.below(dy);
            if (!level.getBlockState(down).isAir()) {
                lowestFloor = currentPos.below(dy - 1);
                break;
            }
        }

        Vec3 settledCenter = new Vec3(lowestFloor.getX() + 0.5, lowestFloor.getY() + 0.15, lowestFloor.getZ() + 0.5);
        AtmosphericDispersionManager.releaseGaseousActivity(level, settledCenter, radonYieldBq);
    }

    /**
     * Вычисление динамического радиационного профиля ОЯТ с учётом его возраста
     */
    public static IsotopeRadiationProfile getDynamicSnfProfile(ItemStack stack, IsotopeRadiationProfile baseProfile) {
        long ageTicks = getSnfAgeTicks(stack);

        double days = ageTicks / 24000.0;


        double shortLivedFactor = Math.exp(-days / 1.5);


        double mediumLivedFactor = 0.85 * Math.exp(-days / 1200.0);


        double actinideBuildup = 1.0 + 0.45 * (1.0 - Math.exp(-days / 5.0));


        double gammaConstant = baseProfile.getGammaDoseConst() * (0.05 + 0.65 * shortLivedFactor + 0.30 * mediumLivedFactor);
        double betaConstant = baseProfile.getBetaDoseConst() * (0.08 + 0.70 * shortLivedFactor + 0.22 * mediumLivedFactor);
        double alphaDose = baseProfile.getAlphaContactDoseSv() * actinideBuildup;
        double neutronYield = baseProfile.getNeutronYieldPerSec() * (0.90 + 0.35 * (1.0 - Math.exp(-days / 8.0)));
        double totalActivity = baseProfile.getActivityBq() * (0.10 + 0.75 * shortLivedFactor + 0.15 * mediumLivedFactor);

        return new IsotopeRadiationProfile(totalActivity, gammaConstant, betaConstant, alphaDose, neutronYield);
    }

    /**
     * Тепловая мощность ОЯТ (Ватты) в зависимости от кривой остаточного тепловыделения Вигнера-Вэя
     */
    public static double getSnfThermalWatts(ItemStack stack, int count) {
        long ageTicks = getSnfAgeTicks(stack);
        double timeSec = Math.max(1.0, ageTicks * 0.05);

        // ОЯТ: P(t) ~ P_0 * t^(-0.2)
        double baseHeatPerItem = 420.0 * Math.pow(1.0 + timeSec / 600.0, -0.28);
        return Math.max(12.0, baseHeatPerItem) * count;
    }

    public static long getSnfAgeTicks(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        return tag.getLong(TAG_SNF_AGE_TICKS);
    }

    public static void ageSnfStack(ItemStack stack, long deltaTicks) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        long currentAge = tag.getLong(TAG_SNF_AGE_TICKS);
        tag.putLong(TAG_SNF_AGE_TICKS, currentAge + deltaTicks);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}