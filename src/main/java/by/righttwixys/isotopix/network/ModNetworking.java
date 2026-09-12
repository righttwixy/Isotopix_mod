package by.righttwixys.isotopix.network;

import by.righttwixys.isotopix.client.ClientRadiationRenderer;
import by.righttwixys.isotopix.gpu.GpuParticleEngine;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetworking {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0.0");

        registrar.playToClient(
                RadiationRaysPayload.TYPE,
                RadiationRaysPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> ClientRadiationRenderer.handleRaysPacket(payload))
        );

        registrar.playToClient(
                RadiationParticleBurstPayload.TYPE,
                RadiationParticleBurstPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> GpuParticleEngine.handleBurstPacket(payload))
        );

        registrar.playToClient(
                SyncRadiationPayload.TYPE,
                SyncRadiationPayload.STREAM_CODEC,
                SyncRadiationPayload::handle
        );

        registrar.playToClient(
                SyncRadiationSourcesPayload.TYPE,
                SyncRadiationSourcesPayload.STREAM_CODEC,
                SyncRadiationSourcesPayload::handle
        );

        registrar.playToServer(
                PlayerParticleHitPayload.TYPE,
                PlayerParticleHitPayload.STREAM_CODEC,
                PlayerParticleHitPayload::handle
        );
    }
}