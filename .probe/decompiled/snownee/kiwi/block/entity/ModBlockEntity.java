package snownee.kiwi.block.entity;

import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ModBlockEntity extends BlockEntity {

    public boolean persistData = false;

    public ModBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag compoundtag = pkt.getTag();
        if (compoundtag != null) {
            this.readPacketData(compoundtag);
        }
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return this.writePacketData(new CompoundTag());
    }

    protected abstract void readPacketData(CompoundTag var1);

    @Nonnull
    protected abstract CompoundTag writePacketData(CompoundTag var1);

    public void refresh() {
        if (this.m_58898_() && !this.f_58857_.isClientSide) {
            BlockState state = this.m_58900_();
            this.f_58857_.sendBlockUpdated(this.f_58858_, state, state, 11);
            this.m_6596_();
        }
    }
}