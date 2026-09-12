package by.righttwixys.isotopix.datagen;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.api.ItemRegistryApi;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Isotopix.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (DeferredItem<Item> itemHolder : ItemRegistryApi.getAllItems()) {
            String name = itemHolder.getId().getPath();
            ResourceLocation textureLoc = ResourceLocation.fromNamespaceAndPath(Isotopix.MODID, "item/" + name);

            this.existingFileHelper.trackGenerated(textureLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

            withExistingParent(name, ResourceLocation.withDefaultNamespace("item/generated"))
                    .texture("layer0", textureLoc);
        }
    }
}