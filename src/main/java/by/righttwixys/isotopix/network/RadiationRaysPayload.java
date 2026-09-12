package by.righttwixys.isotopix.network;

import by.righttwixys.isotopix.Isotopix;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record RadiationRaysPayload(byte visualMode, List<PolylineRay> rays) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RadiationRaysPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Isotopix.MODID, "radiation_rays"));

    public static final StreamCodec<FriendlyByteBuf, RadiationRaysPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> payload.write(buf),
            RadiationRaysPayload::new
    );

    public record PolylineRay(byte type, float[] points) {
        public void write(FriendlyByteBuf buf) {
            buf.writeByte(type);
            buf.writeVarInt(points.length);
            for (float p : points) {
                buf.writeFloat(p);
            }
        }

        public static PolylineRay read(FriendlyByteBuf buf) {
            byte type = buf.readByte();
            int len = buf.readVarInt();
            float[] pts = new float[len];
            for (int i = 0; i < len; i++) {
                pts[i] = buf.readFloat();
            }
            return new PolylineRay(type, pts);
        }
    }

    public RadiationRaysPayload(FriendlyByteBuf buf) {
        this(buf.readByte(), readRays(buf));
    }

    private static List<PolylineRay> readRays(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        List<PolylineRay> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(PolylineRay.read(buf));
        }
        return list;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByte(visualMode);
        buf.writeVarInt(rays != null ? rays.size() : 0);
        if (rays != null) {
            for (PolylineRay ray : rays) {
                ray.write(buf);
            }
        }
    }

    public byte mode() {
        return visualMode;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}