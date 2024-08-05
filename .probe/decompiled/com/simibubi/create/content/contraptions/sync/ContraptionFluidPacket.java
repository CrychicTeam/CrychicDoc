package com.simibubi.create.content.contraptions.sync;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

public class ContraptionFluidPacket extends SimplePacketBase {

    private int entityId;

    private BlockPos localPos;

    private FluidStack containedFluid;

    public ContraptionFluidPacket(int entityId, BlockPos localPos, FluidStack containedFluid) {
        this.entityId = entityId;
        this.localPos = localPos;
        this.containedFluid = containedFluid;
    }

    public ContraptionFluidPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.localPos = buffer.readBlockPos();
        this.containedFluid = FluidStack.readFromPacket(buffer);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBlockPos(this.localPos);
        this.containedFluid.writeToPacket(buffer);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getEntity(this.entityId) instanceof AbstractContraptionEntity contraptionEntity) {
                contraptionEntity.getContraption().handleContraptionFluidPacket(this.localPos, this.containedFluid);
            }
        });
        return true;
    }
}