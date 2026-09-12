package by.righttwixys.isotopix.radiation;

import net.minecraft.network.FriendlyByteBuf;

public record RadiationSourceRecord(
        float posX,
        float posY,
        float posZ,
        int emitterId,
        float alphaRate,
        float betaRate,
        float gammaRate,
        float neutronRate,
        float alphaDoseNanoR,
        float betaDoseNanoR,
        float gammaDoseNanoR,
        float neutronDoseNanoR,
        float alphaEnergyMeV,
        float betaEnergyMeV,
        float gammaEnergyMeV,
        float neutronEnergyMeV,
        float totalEmissionWeight
) {
    public RadiationSourceRecord(FriendlyByteBuf buf) {
        this(
                buf.readFloat(), buf.readFloat(), buf.readFloat(),
                buf.readVarInt(),
                buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                buf.readFloat()
        );
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(posX);
        buf.writeFloat(posY);
        buf.writeFloat(posZ);
        buf.writeVarInt(emitterId);
        buf.writeFloat(alphaRate);
        buf.writeFloat(betaRate);
        buf.writeFloat(gammaRate);
        buf.writeFloat(neutronRate);
        buf.writeFloat(alphaDoseNanoR);
        buf.writeFloat(betaDoseNanoR);
        buf.writeFloat(gammaDoseNanoR);
        buf.writeFloat(neutronDoseNanoR);
        buf.writeFloat(alphaEnergyMeV);
        buf.writeFloat(betaEnergyMeV);
        buf.writeFloat(gammaEnergyMeV);
        buf.writeFloat(neutronEnergyMeV);
        buf.writeFloat(totalEmissionWeight);
    }
}