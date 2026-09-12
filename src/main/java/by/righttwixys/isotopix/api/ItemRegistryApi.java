package by.righttwixys.isotopix.api;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.item.DescriptiveItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemRegistryApi {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Isotopix.MODID);

    private static final Map<String, DeferredItem<Item>> ITEMS_BY_NAME = new LinkedHashMap<>();
    private static final Map<String, String> EN_DESCRIPTIONS = new HashMap<>();
    private static final Map<String, String> RU_DESCRIPTIONS = new HashMap<>();

    public static DeferredItem<Item> addItem(String name) {
        return addItem(name, new Item.Properties(), null, null);
    }

    public static void addItems(String... names) {
        for (String name : names) {
            addItem(name);
        }
    }

    public static DeferredItem<Item> addItem(String name, String enDesc, String ruDesc) {
        return addItem(name, new Item.Properties(), enDesc, ruDesc);
    }

    public static DeferredItem<Item> addItem(String name, Item.Properties properties) {
        return addItem(name, properties, null, null);
    }

    public static DeferredItem<Item> addItem(String name, Item.Properties properties, String enDesc, String ruDesc) {
        if (ITEMS_BY_NAME.containsKey(name)) {
            return ITEMS_BY_NAME.get(name);
        }

        DeferredItem<Item> itemHolder = ITEMS.registerItem(name, DescriptiveItem::new, properties);
        ITEMS_BY_NAME.put(name, itemHolder);

        if (enDesc != null && !enDesc.isBlank()) {
            EN_DESCRIPTIONS.put(name, enDesc);
        }
        if (ruDesc != null && !ruDesc.isBlank()) {
            RU_DESCRIPTIONS.put(name, ruDesc);
        }

        return itemHolder;
    }

    public static DeferredItem<Item> getHolder(String name) {
        return ITEMS_BY_NAME.get(name);
    }

    public static Item getItem(String name) {
        DeferredItem<Item> holder = ITEMS_BY_NAME.get(name);
        return holder != null ? holder.get() : null;
    }

    public static ItemStack getStack(String name, int count) {
        Item item = getItem(name);
        return item != null ? new ItemStack(item, count) : ItemStack.EMPTY;
    }

    public static Collection<DeferredItem<Item>> getAllItems() {
        return Collections.unmodifiableCollection(ITEMS_BY_NAME.values());
    }

    public static Map<String, String> getEnDescriptions() {
        return Collections.unmodifiableMap(EN_DESCRIPTIONS);
    }

    public static Map<String, String> getRuDescriptions() {
        return Collections.unmodifiableMap(RU_DESCRIPTIONS);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}