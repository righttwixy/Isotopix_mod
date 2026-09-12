package by.righttwixys.isotopix.network;

import by.righttwixys.isotopix.Isotopix;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RadiationParticleBurstPayload(
        float x, float y, float z,
        float dirX, float dirY, float dirZ,
        float spread,
        int count,
        byte particleType,
        float energyMeV,
        float doseNanoRPerParticle,
        int emitterId
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RadiationParticleBurstPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Isotopix.MODID, "radiation_burst"));

    public static final StreamCodec<FriendlyByteBuf, RadiationParticleBurstPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> payload.write(buf),
            RadiationParticleBurstPayload::new
    );

    public RadiationParticleBurstPayload(FriendlyByteBuf buf) {
        this(
                buf.readFloat(), buf.readFloat(), buf.readFloat(),
                buf.readFloat(), buf.readFloat(), buf.readFloat(),
                buf.readFloat(),
                buf.readVarInt(),
                buf.readByte(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readVarInt()
        );
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
        buf.writeFloat(dirX);
        buf.writeFloat(dirY);
        buf.writeFloat(dirZ);
        buf.writeFloat(spread);
        buf.writeVarInt(count);
        buf.writeByte(particleType);
        buf.writeFloat(energyMeV);
        buf.writeFloat(doseNanoRPerParticle);
        buf.writeVarInt(emitterId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}