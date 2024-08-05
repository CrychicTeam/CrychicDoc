package com.simibubi.create.content.contraptions;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ContraptionStallPacket extends SimplePacketBase {

    int entityID;

    double x;

    double y;

    double z;

    float angle;

    public ContraptionStallPacket(int entityID, double posX, double posY, double posZ, float angle) {
        this.entityID = entityID;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.angle = angle;
    }

    public ContraptionStallPacket(FriendlyByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.angle = buffer.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityID);
        this.writeAll(buffer, this.x, this.y, this.z);
        buffer.writeFloat(this.angle);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> AbstractContraptionEntity.handleStallPacket(this)));
        return true;
    }

    private void writeAll(FriendlyByteBuf buffer, double... doubles) {
        for (double d : doubles) {
            buffer.writeDouble(d);
        }
    }
}