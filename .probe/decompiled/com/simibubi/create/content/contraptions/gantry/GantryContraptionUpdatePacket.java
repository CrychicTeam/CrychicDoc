package com.simibubi.create.content.contraptions.gantry;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class GantryContraptionUpdatePacket extends SimplePacketBase {

    int entityID;

    double coord;

    double motion;

    double sequenceLimit;

    public GantryContraptionUpdatePacket(int entityID, double coord, double motion, double sequenceLimit) {
        this.entityID = entityID;
        this.coord = coord;
        this.motion = motion;
        this.sequenceLimit = sequenceLimit;
    }

    public GantryContraptionUpdatePacket(FriendlyByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.coord = (double) buffer.readFloat();
        this.motion = (double) buffer.readFloat();
        this.sequenceLimit = (double) buffer.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeFloat((float) this.coord);
        buffer.writeFloat((float) this.motion);
        buffer.writeFloat((float) this.sequenceLimit);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> GantryContraptionEntity.handlePacket(this)));
        return true;
    }
}