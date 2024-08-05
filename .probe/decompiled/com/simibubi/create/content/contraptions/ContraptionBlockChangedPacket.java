package com.simibubi.create.content.contraptions;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ContraptionBlockChangedPacket extends SimplePacketBase {

    int entityID;

    BlockPos localPos;

    BlockState newState;

    public ContraptionBlockChangedPacket(int id, BlockPos pos, BlockState state) {
        this.entityID = id;
        this.localPos = pos;
        this.newState = state;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeBlockPos(this.localPos);
        buffer.writeNbt(NbtUtils.writeBlockState(this.newState));
    }

    public ContraptionBlockChangedPacket(FriendlyByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.localPos = buffer.readBlockPos();
        this.newState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.m_255303_(), buffer.readNbt());
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> AbstractContraptionEntity.handleBlockChangedPacket(this)));
        return true;
    }
}