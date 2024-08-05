package com.simibubi.create.content.contraptions;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ContraptionDisassemblyPacket extends SimplePacketBase {

    int entityID;

    StructureTransform transform;

    public ContraptionDisassemblyPacket(int entityID, StructureTransform transform) {
        this.entityID = entityID;
        this.transform = transform;
    }

    public ContraptionDisassemblyPacket(FriendlyByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.transform = StructureTransform.fromBuffer(buffer);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityID);
        this.transform.writeToBuffer(buffer);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> AbstractContraptionEntity.handleDisassemblyPacket(this)));
        return true;
    }
}