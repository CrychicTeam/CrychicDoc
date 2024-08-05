package com.simibubi.create.content.contraptions;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ContraptionRelocationPacket extends SimplePacketBase {

    int entityID;

    public ContraptionRelocationPacket(int entityID) {
        this.entityID = entityID;
    }

    public ContraptionRelocationPacket(FriendlyByteBuf buffer) {
        this.entityID = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityID);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> OrientedContraptionEntity.handleRelocationPacket(this)));
        return true;
    }
}