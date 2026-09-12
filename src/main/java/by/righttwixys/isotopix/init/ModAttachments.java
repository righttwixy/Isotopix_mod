package by.righttwixys.isotopix.init;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.radiation.EntityRadiation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Isotopix.MODID);

    public static final Supplier<AttachmentType<EntityRadiation>> RADIATION =
            ATTACHMENT_TYPES.register("radiation", () -> AttachmentType.serializable(EntityRadiation::new).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}