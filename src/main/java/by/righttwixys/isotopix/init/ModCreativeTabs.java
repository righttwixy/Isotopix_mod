package by.righttwixys.isotopix.init;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.api.ItemRegistryApi;
import by.righttwixys.isotopix.radiation.IsotopeRadiationProfile;
import by.righttwixys.isotopix.radiation.IsotopeRadiationRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Isotopix.MODID);

    public enum Category {
        STABLE,
        ALPHA,
        BETA,
        GAMMA,
        FISSION,
        ORES,
        MINERALS,
        COMPOUNDS
    }

    // 1. Оборудование и радиационная защита
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_EQUIPMENT =
            CREATIVE_MODE_TABS.register("isotopix_equipment_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.isotopix.equipment"))
                    .icon(() -> {
                        if (ModItems.DOSIMETER != null) {
                            return new ItemStack(ModItems.DOSIMETER.get());
                        }
                        if (ModBlocks.LEAD_BLOCK_ITEM != null) {
                            return new ItemStack(ModBlocks.LEAD_BLOCK_ITEM.get());
                        }
                        return new ItemStack(Items.COMPASS);
                    })
                    .displayItems((parameters, output) -> {
                        if (ModBlocks.LEAD_BLOCK_ITEM != null) {
                            output.accept(ModBlocks.LEAD_BLOCK_ITEM.get());
                        }
                        if (ModItems.DOSIMETER != null) {
                            output.accept(ModItems.DOSIMETER.get());
                        }
                    })
                    .build());

    // 2. Стабильные изотопы
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_STABLE =
            CREATIVE_MODE_TABS.register("isotopix_stable_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.isotopix.stable"))
                    .icon(() -> createIcon("fe56", "c12", "h1"))
                    .displayItems((parameters, output) -> populateCategory(output, Category.STABLE))
                    .build());

    // 3. Альфа-излучатели
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_ALPHA =
            CREATIVE_MODE_TABS.register("isotopix_alpha_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.isotopix.alpha"))
                    .icon(() -> createIcon("u238", "pu239", "po210"))
                    .displayItems((parameters, output) -> populateCategory(output, Category.ALPHA))
                    .build());

    // 4. Бета-излучатели
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_BETA =
            CREATIVE_MODE_TABS.register("isotopix_beta_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.isotopix.beta"))
                    .icon(() -> createIcon("sr90", "h3", "c14"))
                    .displayItems((parameters, output) -> populateCategory(output, Category.BETA))
                    .build());

    // 5. Гамма-излучатели
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_GAMMA =
            CREATIVE_MODE_TABS.register("isotopix_gamma_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.isotopix.gamma"))
                    .icon(() -> createIcon("co60", "cs137", "na24"))
                    .displayItems((parameters, output) -> populateCategory(output, Category.GAMMA))
                    .build());

    // 6. Деление, трансураны и нейтроны
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_FISSION =
            CREATIVE_MODE_TABS.register("isotopix_fission_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.isotopix.fission"))
                    .icon(() -> createIcon("cf252", "co252", "og294"))
                    .displayItems((parameters, output) -> populateCategory(output, Category.FISSION))
                    .build());

    // 7. Руды (Природные радиоактивные руды)
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_ORES =
            CREATIVE_MODE_TABS.register("isotopix_ores_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.isotopix.ores"))
                    .icon(() -> createIcon("ore_uraninite", "ore_pitchblende", "ore_thorite"))
                    .displayItems((parameters, output) -> populateCategory(output, Category.ORES))
                    .build());

    // 8. Минералы (Вторичные минералы урана и тория)
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_MINERALS =
            CREATIVE_MODE_TABS.register("isotopix_minerals_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.isotopix.minerals"))
                    .icon(() -> createIcon("mineral_carnotite", "mineral_autunite", "mineral_torbernite"))
                    .displayItems((parameters, output) -> populateCategory(output, Category.MINERALS))
                    .build());

    // 9. Химические соединения (Оксиды, гидроксиды, соли, карбиды, нитриды)
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_COMPOUNDS =
            CREATIVE_MODE_TABS.register("isotopix_compounds_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.isotopix.compounds"))
                    .icon(() -> createIcon("u_dioxide", "u_hexafluoride", "pu_dioxide"))
                    .displayItems((parameters, output) -> populateCategory(output, Category.COMPOUNDS))
                    .build());

    private static ItemStack createIcon(String... preferredIds) {
        for (String id : preferredIds) {
            Item item = ModItems.get(id);
            if (item != null) {
                return new ItemStack(item);
            }
        }
        if (!ItemRegistryApi.getAllItems().isEmpty()) {
            return new ItemStack(ItemRegistryApi.getAllItems().iterator().next().get());
        }
        return new ItemStack(Items.NETHER_STAR);
    }

    private static void populateCategory(CreativeModeTab.Output output, Category targetCategory) {
        for (DeferredItem<Item> itemHolder : ItemRegistryApi.getAllItems()) {
            String name = itemHolder.getId().getPath();
            IsotopeRadiationProfile profile = IsotopeRadiationRegistry.get(name);
            if (classifyIsotope(name, profile) == targetCategory) {
                output.accept(itemHolder.get());
            }
        }
    }

    public static Category classifyIsotope(String name, IsotopeRadiationProfile profile) {
        // 1. Руды
        if (name.startsWith("ore_")) {
            return Category.ORES;
        }

        // 2. Вторичные минералы
        if (name.startsWith("mineral_")) {
            return Category.MINERALS;
        }

        // 3. Химические соединения
        if (isChemicalCompound(name)) {
            return Category.COMPOUNDS;
        }

        // 4. Стабильные изотопы
        if (!profile.isRadioactive()) {
            return Category.STABLE;
        }

        // 5. Делящиеся нуклиды и спонтанное деление
        if (profile.getNeutronYieldPerSec() > 10.0
                || name.equals("co252")
                || name.startsWith("cf")
                || name.startsWith("es")
                || name.startsWith("fm")
                || name.startsWith("md")
                || name.startsWith("no")
                || name.startsWith("lr")
                || name.startsWith("rf")
                || name.startsWith("db")
                || name.startsWith("sg")
                || name.startsWith("bh")
                || name.startsWith("hs")
                || name.startsWith("mt")
                || name.startsWith("ds")
                || name.startsWith("rg")
                || name.startsWith("cn")
                || name.startsWith("nh")
                || name.startsWith("fl")
                || name.startsWith("mc")
                || name.startsWith("lv")
                || name.startsWith("ts")
                || name.startsWith("og")) {
            return Category.FISSION;
        }

        // 6. Альфа-актиниды
        if (profile.getAlphaContactDoseSv() > 0.00001) {
            return Category.ALPHA;
        }

        // 7. Гамма-источники
        if (profile.getGammaDoseConst() >= 0.035) {
            return Category.GAMMA;
        }

        // 8. Бета-излучатели
        if (profile.getBetaDoseConst() > 0.0) {
            return Category.BETA;
        }

        if (profile.getGammaDoseConst() > 0.0) {
            return Category.GAMMA;
        }

        return Category.STABLE;
    }

    private static boolean isChemicalCompound(String name) {
        return name.startsWith("u_")
                || name.startsWith("th_")
                || name.startsWith("pu_")
                || name.startsWith("np_")
                || name.startsWith("am_")
                || name.startsWith("cm_")
                || name.startsWith("po_")
                || name.startsWith("ra_")
                || name.startsWith("uranyl_")
                || name.startsWith("ammonium_")
                || name.startsWith("sodium_")
                || name.startsWith("magnesium_")
                || name.startsWith("cs137_")
                || name.startsWith("sr90_")
                || name.startsWith("co60_");
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}