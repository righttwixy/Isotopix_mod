package by.righttwixys.isotopix.init;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.api.ItemRegistryApi;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Isotopix.MODID);

    public static final DeferredBlock<Block> LEAD_BLOCK = BLOCKS.register("lead_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .sound(SoundType.METAL)
                    .strength(4.0f, 12.0f)
                    .requiresCorrectToolForDrops()));

    public static final DeferredItem<BlockItem> LEAD_BLOCK_ITEM = ItemRegistryApi.ITEMS.registerSimpleBlockItem(LEAD_BLOCK);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}