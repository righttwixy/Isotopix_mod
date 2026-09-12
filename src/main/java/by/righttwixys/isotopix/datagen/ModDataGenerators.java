package by.righttwixys.isotopix.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class ModDataGenerators {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(
                event.includeClient(),
                new ModTextureProvider(packOutput)
        );

        generator.addProvider(
                event.includeClient(),
                new ModItemModelProvider(packOutput, existingFileHelper)
        );

        generator.addProvider(
                event.includeClient(),
                new ModLanguageProvider(packOutput, "en_us")
        );
        generator.addProvider(
                event.includeClient(),
                new ModLanguageProvider(packOutput, "ru_ru")
        );
    }
}