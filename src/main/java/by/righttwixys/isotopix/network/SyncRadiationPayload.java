package by.righttwixys.isotopix.network;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.init.ModAttachments;
import by.righttwixys.isotopix.radiation.EntityRadiation;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncRadiationPayload(
        float accumulatedDoseRoentgen,
        float currentDoseRateMicroRPerHour,
        int countsPerSecond,
        float dnaDamageUnits,
        double ingestedRadionuclidesBq
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncRadiationPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Isotopix.MODID, "sync_radiation"));

    public static final StreamCodec<FriendlyByteBuf, SyncRadiationPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> payload.write(buf),
            SyncRadiationPayload::new
    );

    public SyncRadiationPayload(FriendlyByteBuf buf) {
        this(buf.readFloat(), buf.readFloat(), buf.readVarInt(), buf.readFloat(), buf.readDouble());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(accumulatedDoseRoentgen);
        buf.writeFloat(currentDoseRateMicroRPerHour);
        buf.writeVarInt(countsPerSecond);
        buf.writeFloat(dnaDamageUnits);
        buf.writeDouble(ingestedRadionuclidesBq);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncRadiationPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                EntityRadiation rad = Minecraft.getInstance().player.getData(ModAttachments.RADIATION);
                rad.setAccumulatedDoseRoentgen(payload.accumulatedDoseRoentgen());
                rad.setCurrentDoseRateMicroRPerHour(payload.currentDoseRateMicroRPerHour());
                rad.setCountsPerSecond(payload.countsPerSecond());
                rad.setDnaDamageUnits(payload.dnaDamageUnits());
                rad.setIngestedRadionuclidesBq(payload.ingestedRadionuclidesBq());
            }
        });
    }
}