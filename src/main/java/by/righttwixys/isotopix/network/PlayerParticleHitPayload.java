package by.righttwixys.isotopix.network;

import by.righttwixys.isotopix.Isotopix;
import by.righttwixys.isotopix.init.ModAttachments;
import by.righttwixys.isotopix.radiation.EntityRadiation;
import by.righttwixys.isotopix.radiation.RadiationType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PlayerParticleHitPayload(
        float absorbedDoseRoentgen,
        int hitCount,
        byte dominantType
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PlayerParticleHitPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Isotopix.MODID, "player_particle_hit"));

    public static final StreamCodec<FriendlyByteBuf, PlayerParticleHitPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> payload.write(buf),
            PlayerParticleHitPayload::new
    );

    public PlayerParticleHitPayload(FriendlyByteBuf buf) {
        this(buf.readFloat(), buf.readVarInt(), buf.readByte());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(Math.max(0.0f, absorbedDoseRoentgen));
        buf.writeVarInt(Math.max(0, hitCount));
        buf.writeByte(dominantType);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PlayerParticleHitPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.isCreative() || serverPlayer.isSpectator()) {
                    return;
                }
                EntityRadiation rad = serverPlayer.getData(ModAttachments.RADIATION);
                RadiationType type = RadiationType.values()[Math.min(RadiationType.values().length - 1, Math.max(0, payload.dominantType()))];
                rad.recordParticleHit(type, Math.max(0.0, (double) payload.absorbedDoseRoentgen()), Math.max(1, payload.hitCount()));
            }
        });
    }
}