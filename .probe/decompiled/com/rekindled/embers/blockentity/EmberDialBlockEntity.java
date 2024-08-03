package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IDialEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class EmberDialBlockEntity extends BlockEntity implements IDialEntity {

    public double ember = 0.0;

    public double capacity = 0.0;

    public boolean display = false;

    public EmberDialBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.EMBER_DIAL_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        if (nbt.contains("embers:ember")) {
            this.ember = nbt.getDouble("embers:ember");
        }
        if (nbt.contains("embers:ember_capacity")) {
            this.capacity = nbt.getDouble("embers:ember_capacity");
        }
        if (nbt.contains("display")) {
            this.display = nbt.getBoolean("display");
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        BlockState state = this.f_58857_.getBlockState(this.f_58858_);
        boolean display = false;
        if (state.m_61138_(BlockStateProperties.FACING)) {
            Direction facing = (Direction) state.m_61143_(BlockStateProperties.FACING);
            BlockEntity blockEntity = this.f_58857_.getBlockEntity(this.f_58858_.relative(facing, -1));
            if (blockEntity != null) {
                IEmberCapability cap = (IEmberCapability) blockEntity.getCapability(EmbersCapabilities.EMBER_CAPABILITY, facing.getOpposite()).orElse((IEmberCapability) blockEntity.getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null));
                if (cap != null) {
                    nbt.putDouble("embers:ember", cap.getEmber());
                    nbt.putDouble("embers:ember_capacity", cap.getEmberCapacity());
                    display = true;
                }
            }
        }
        nbt.putBoolean("display", display);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket(int maxLines) {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}