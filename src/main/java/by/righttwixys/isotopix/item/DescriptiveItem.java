package by.righttwixys.isotopix.item;

import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class DescriptiveItem extends Item {
    public DescriptiveItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        String descKey = this.getDescriptionId() + ".desc";
        Language lang = Language.getInstance();

        if (lang.has(descKey)) {
            String translated = lang.getOrDefault(descKey);
            if (!translated.isBlank()) {
                String[] lines = translated.split("\n");
                for (String line : lines) {
                    tooltipComponents.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }
}