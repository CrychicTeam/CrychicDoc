package net.minecraft.network.protocol.game;

import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ClientboundBlockEntityDataPacket implements Packet<ClientGamePacketListener> {

    private final BlockPos pos;

    private final BlockEntityType<?> type;

    @Nullable
    private final CompoundTag tag;

    public static ClientboundBlockEntityDataPacket create(BlockEntity blockEntity0, Function<BlockEntity, CompoundTag> functionBlockEntityCompoundTag1) {
        return new ClientboundBlockEntityDataPacket(blockEntity0.getBlockPos(), blockEntity0.getType(), (CompoundTag) functionBlockEntityCompoundTag1.apply(blockEntity0));
    }

    public static ClientboundBlockEntityDataPacket create(BlockEntity blockEntity0) {
        return create(blockEntity0, BlockEntity::m_5995_);
    }

    private ClientboundBlockEntityDataPacket(BlockPos blockPos0, BlockEntityType<?> blockEntityType1, CompoundTag compoundTag2) {
        this.pos = blockPos0;
        this.type = blockEntityType1;
        this.tag = compoundTag2.isEmpty() ? null : compoundTag2;
    }

    public ClientboundBlockEntityDataPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.pos = friendlyByteBuf0.readBlockPos();
        this.type = friendlyByteBuf0.readById(BuiltInRegistries.BLOCK_ENTITY_TYPE);
        this.tag = friendlyByteBuf0.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeId(BuiltInRegistries.BLOCK_ENTITY_TYPE, this.type);
        friendlyByteBuf0.writeNbt(this.tag);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleBlockEntityData(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public BlockEntityType<?> getType() {
        return this.type;
    }

    @Nullable
    public CompoundTag getTag() {
        return this.tag;
    }
}