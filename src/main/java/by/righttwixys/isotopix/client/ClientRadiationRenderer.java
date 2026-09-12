package by.righttwixys.isotopix.client;
import by.righttwixys.isotopix.gpu.GpuParticleEngine;
import by.righttwixys.isotopix.network.RadiationRaysPayload;
public class ClientRadiationRenderer {
    public static void handleRaysPacket(RadiationRaysPayload payload) {
       GpuParticleEngine.setVisualMode(payload.visualMode());
    }
}
