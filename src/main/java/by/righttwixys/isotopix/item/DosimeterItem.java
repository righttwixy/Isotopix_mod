package by.righttwixys.isotopix.item;

import by.righttwixys.isotopix.init.ModAttachments;
import by.righttwixys.isotopix.radiation.EntityRadiation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DosimeterItem extends Item {

    public DosimeterItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide()) return;

        if (entity instanceof Player player) {
            boolean isHeld = player.getMainHandItem() == stack || player.getOffhandItem() == stack;
            if (isHeld && player.tickCount % 4 == 0) {
                EntityRadiation rad = player.getData(ModAttachments.RADIATION);
                float rate = Math.max(0.0f, rad.getCurrentDoseRateMicroRPerHour());
                int cps = Math.max(0, rad.getCountsPerSecond());

                String formattedRate;
                if (rate < 1000.0f) {
                    formattedRate = String.format("§a%.1f мкР/ч", rate);
                } else if (rate < 1_000_000.0f) {
                    formattedRate = String.format("§e%.2f мР/ч", rate / 1000.0f);
                } else {
                    formattedRate = String.format("§c%.2f Р/ч", rate / 1_000_000.0f);
                }

                player.displayClientMessage(
                        Component.literal("§2[РАД] " + formattedRate + " §7| §f" + cps + " CPS"),
                        true
                );
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            EntityRadiation rad = player.getData(ModAttachments.RADIATION);
            float rate = Math.max(0.0f, rad.getCurrentDoseRateMicroRPerHour());
            int cps = Math.max(0, rad.getCountsPerSecond());
            float accR = Math.max(0.0f, rad.getAccumulatedDoseRoentgen());
            float accSv = Math.max(0.0f, rad.getAccumulatedDoseSieverts());
            double internalBq = Math.max(0.0, rad.getInternalContaminationBq());

            String formattedRate;
            if (rate < 1000.0f) {
                formattedRate = String.format("%.1f мкР/ч", rate);
            } else if (rate < 1_000_000.0f) {
                formattedRate = String.format("%.2f мР/ч", rate / 1000.0f);
            } else {
                formattedRate = String.format("%.2f Р/ч", rate / 1_000_000.0f);
            }

            player.sendSystemMessage(Component.literal("§7Мощность дозы: §f" + formattedRate + " §8(§f" + cps + " CPS§8)"));

            if (internalBq > 0.1) {
              //оставил чисто для будущего мода
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}