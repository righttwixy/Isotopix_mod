package by.righttwixys.isotopix.network;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.gpu.GpuParticleEngine;
import by.righttwixys.isotopix.radiation.RadiationSourceRecord;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record SyncRadiationSourcesPayload(List<RadiationSourceRecord> sources) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncRadiationSourcesPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Isotopix.MODID, "sync_radiation_sources"));

    public static final StreamCodec<FriendlyByteBuf, SyncRadiationSourcesPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> payload.write(buf),
            SyncRadiationSourcesPayload::new
    );

    public SyncRadiationSourcesPayload(FriendlyByteBuf buf) {
        this(readSources(buf));
    }

    private static List<RadiationSourceRecord> readSources(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        List<RadiationSourceRecord> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(new RadiationSourceRecord(buf));
        }
        return list;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(sources.size());
        for (RadiationSourceRecord source : sources) {
            source.write(buf);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncRadiationSourcesPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> GpuParticleEngine.updateActiveSources(payload.sources()));
    }
}