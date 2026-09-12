package by.righttwixys.isotopix;

import by.righttwixys.isotopix.client.gui.IsotopixConfigScreen;
import by.righttwixys.isotopix.command.ModCommands;
import by.righttwixys.isotopix.config.IsotopixConfig;
import by.righttwixys.isotopix.datagen.ModDataGenerators;
import by.righttwixys.isotopix.gpu.GpuParticleEngine;
import by.righttwixys.isotopix.init.ModAttachments;
import by.righttwixys.isotopix.init.ModBlocks;
import by.righttwixys.isotopix.init.ModCreativeTabs;
import by.righttwixys.isotopix.init.ModItems;
import by.righttwixys.isotopix.init.ModSounds;
import by.righttwixys.isotopix.network.ModNetworking;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(Isotopix.MODID)
public class Isotopix {
    public static final String MODID = "isotopix";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Isotopix(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Initializing Isotopix mod with custom acoustic dosimetry engine...");

        modContainer.registerConfig(ModConfig.Type.CLIENT, IsotopixConfig.CLIENT_SPEC);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModAttachments.register(modEventBus);

        modEventBus.addListener(ModNetworking::registerPayloads);
        modEventBus.addListener(ModDataGenerators::gatherData);

        modEventBus.addListener(this::onConfigLoad);
        modEventBus.addListener(this::onConfigReload);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, screen) -> new IsotopixConfigScreen(screen));
        }

        NeoForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == IsotopixConfig.CLIENT_SPEC) {
            GpuParticleEngine.resizeParticleBuffer(IsotopixConfig.MAX_PARTICLES.get());
        }
    }

    private void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == IsotopixConfig.CLIENT_SPEC) {
            GpuParticleEngine.resizeParticleBuffer(IsotopixConfig.MAX_PARTICLES.get());
        }
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}