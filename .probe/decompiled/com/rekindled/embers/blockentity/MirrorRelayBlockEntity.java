package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.power.IEmberPacketProducer;
import com.rekindled.embers.api.power.IEmberPacketReceiver;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.entity.EmberPacketEntity;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MirrorRelayBlockEntity extends BlockEntity implements IEmberPacketProducer, IEmberPacketReceiver {

    public BlockPos target = null;

    public Random random = new Random();

    public boolean polled = false;

    public MirrorRelayBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.MIRROR_RELAY_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("targetX")) {
            this.target = new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (this.target != null) {
            nbt.putInt("targetX", this.target.m_123341_());
            nbt.putInt("targetY", this.target.m_123342_());
            nbt.putInt("targetZ", this.target.m_123343_());
        }
    }

    @Override
    public boolean hasRoomFor(double ember) {
        if (this.polled) {
            return this.target != null;
        } else {
            this.polled = true;
            if (this.target != null && this.f_58857_.isLoaded(this.target) && this.f_58857_.getBlockEntity(this.target) instanceof IEmberPacketReceiver targetBE) {
                boolean hasRoom = targetBE.hasRoomFor(ember);
                this.polled = false;
                return hasRoom;
            } else {
                this.polled = false;
                return false;
            }
        }
    }

    @Override
    public boolean onReceive(EmberPacketEntity packet) {
        if (this.target != null && packet.pos != this.m_58899_()) {
            Direction.Axis axis = ((Direction) this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.FACING)).getAxis();
            packet.lifetime = 78;
            packet.dest = this.target;
            packet.pos = this.m_58899_();
            packet.m_20256_(packet.m_20184_().multiply(axis == Direction.Axis.X ? -1.7 : 1.7, axis == Direction.Axis.Y ? -1.7 : 1.7, axis == Direction.Axis.Z ? -1.7 : 1.7));
            this.f_58857_.playLocalSound(packet.m_20185_(), packet.m_20186_(), packet.m_20189_(), EmbersSounds.EMBER_RELAY.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        return false;
    }

    @Override
    public void setTargetPosition(BlockPos pos, Direction side) {
        if (pos != this.f_58858_) {
            this.target = pos;
            this.m_6596_();
        }
    }

    @Override
    public Direction getEmittingDirection(Direction side) {
        return (Direction) this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.FACING);
    }
}