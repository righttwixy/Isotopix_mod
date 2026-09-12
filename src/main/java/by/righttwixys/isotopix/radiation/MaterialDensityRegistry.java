package by.righttwixys.isotopix.radiation;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class MaterialDensityRegistry {
    private static final Map<Block, Float> CUSTOM_DENSITIES = new HashMap<>();

    static {
        register(Blocks.AIR, 0.0012f);
        register(Blocks.CAVE_AIR, 0.0012f);
        register(Blocks.VOID_AIR, 0.0000f);

        register(Blocks.WATER, 1.00f);
        register(Blocks.ICE, 0.92f);
        register(Blocks.BLUE_ICE, 0.95f);
        register(Blocks.PACKED_ICE, 0.93f);
        register(Blocks.OAK_PLANKS, 0.65f);
        register(Blocks.SPRUCE_PLANKS, 0.60f);
        register(Blocks.BIRCH_PLANKS, 0.67f);
        register(Blocks.JUNGLE_PLANKS, 0.65f);
        register(Blocks.ACACIA_PLANKS, 0.70f);
        register(Blocks.DARK_OAK_PLANKS, 0.75f);
        register(Blocks.MANGROVE_PLANKS, 0.68f);
        register(Blocks.CHERRY_PLANKS, 0.62f);
        register(Blocks.BAMBOO_PLANKS, 0.50f);

        register(Blocks.GLASS, 2.50f);
        register(Blocks.TINTED_GLASS, 3.20f);
        register(Blocks.DIRT, 1.50f);
        register(Blocks.STONE, 2.70f);
        register(Blocks.COBBLESTONE, 2.40f);
        register(Blocks.STONE_BRICKS, 2.60f);
        register(Blocks.SMOOTH_STONE, 2.80f);
        register(Blocks.ANDESITE, 2.70f);
        register(Blocks.DIORITE, 2.80f);
        register(Blocks.GRANITE, 2.75f);
        register(Blocks.SANDSTONE, 2.20f);
        register(Blocks.BRICKS, 2.00f);
        register(Blocks.GRAY_CONCRETE, 2.40f);

        // Тяжёлые защитные экраны (высокое экранирование)
        register(Blocks.DEEPSLATE, 3.40f);
        register(Blocks.COBBLED_DEEPSLATE, 3.20f);
        register(Blocks.POLISHED_DEEPSLATE, 3.50f);
        register(Blocks.OBSIDIAN, 4.50f);
        register(Blocks.CRYING_OBSIDIAN, 4.80f);
        register(Blocks.BEDROCK, 15.00f);

        // Металлы
        register(Blocks.COPPER_BLOCK, 8.96f);
        register(Blocks.IRON_BLOCK, 7.87f);
        register(Blocks.NETHERITE_BLOCK, 14.50f);
        register(Blocks.GOLD_BLOCK, 19.32f);
    }

    public static void register(Block block, float density) {
        CUSTOM_DENSITIES.put(block, density);
    }

    public static float getDensity(BlockState state) {
        if (state.isAir()) {
            return 0.0012f;
        }

        Block block = state.getBlock();
        Float density = CUSTOM_DENSITIES.get(block);
        if (density != null) {
            return density;
        }


        if (state.liquid()) {
            return 1.0f;
        }

        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        String path = blockId.getPath();

        if (path.contains("lead")) return 11.34f;
        if (path.contains("uranium")) return 19.10f;
        if (path.contains("concrete")) return 2.40f;
        if (path.contains("metal") || path.contains("steel")) return 7.85f;
        if (path.contains("glass")) return 2.50f;
        if (path.contains("leaves") || path.contains("wool")) return 0.20f;

        return 2.2f; // Средняя плотность твёрдого блока по умолчанию
    }
}