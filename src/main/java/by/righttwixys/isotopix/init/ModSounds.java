package by.righttwixys.isotopix.init;

import by.righttwixys.isotopix.Isotopix;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Isotopix.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER_CLICK =
            SOUND_EVENTS.register("geiger_click", () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Isotopix.MODID, "geiger_click")
            ));

    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER_OVERLOAD =
            SOUND_EVENTS.register("geiger_overload", () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(Isotopix.MODID, "geiger_overload")
            ));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}