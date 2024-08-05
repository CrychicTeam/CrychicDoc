package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.power.IEmberPacketProducer;
import com.rekindled.embers.api.power.IEmberPacketReceiver;
import com.rekindled.embers.api.tile.ISparkable;
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
import net.minecraft.world.phys.Vec3;

public class BeamSplitterBlockEntity extends BlockEntity implements IEmberPacketProducer, IEmberPacketReceiver, ISparkable {

    public BlockPos target1 = null;

    public BlockPos target2 = null;

    public Random random = new Random();

    public boolean polled = false;

    public BeamSplitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.BEAM_SPLITTER_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("target1X")) {
            this.target1 = new BlockPos(nbt.getInt("target1X"), nbt.getInt("target1Y"), nbt.getInt("target1Z"));
        }
        if (nbt.contains("target2X")) {
            this.target2 = new BlockPos(nbt.getInt("target2X"), nbt.getInt("target2Y"), nbt.getInt("target2Z"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (this.target1 != null) {
            nbt.putInt("target1X", this.target1.m_123341_());
            nbt.putInt("target1Y", this.target1.m_123342_());
            nbt.putInt("target1Z", this.target1.m_123343_());
        }
        if (this.target2 != null) {
            nbt.putInt("target2X", this.target2.m_123341_());
            nbt.putInt("target2Y", this.target2.m_123342_());
            nbt.putInt("target2Z", this.target2.m_123343_());
        }
    }

    @Override
    public boolean hasRoomFor(double ember) {
        if (this.polled) {
            return false;
        } else {
            this.polled = true;
            if (this.hasRoomTarget(this.target1, ember / 2.0) && this.hasRoomTarget(this.target2, ember / 2.0)) {
                this.polled = false;
                return true;
            } else if (this.hasRoomTarget(this.target1, ember)) {
                this.polled = false;
                return true;
            } else if (this.hasRoomTarget(this.target2, ember)) {
                this.polled = false;
                return true;
            } else {
                this.polled = false;
                return false;
            }
        }
    }

    public boolean hasRoomTarget(BlockPos target, double ember) {
        return target != null && this.f_58857_.isLoaded(target) && this.f_58857_.getBlockEntity(target) instanceof IEmberPacketReceiver targetBE ? targetBE.hasRoomFor(ember) : false;
    }

    @Override
    public void sparkProgress(BlockEntity tile, double ember) {
        this.split(ember);
    }

    @Override
    public boolean onReceive(EmberPacketEntity packet) {
        if (packet.pos != this.m_58899_()) {
            this.split(packet.value);
        }
        return true;
    }

    public void split(double ember) {
        if ((this.target1 != null || this.target2 != null) && ember > 0.1) {
            Direction.Axis axis = (Direction.Axis) this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.AXIS);
            double value = ember / 2.0;
            boolean room1 = this.hasRoomTarget(this.target1, value) || this.target2 == null;
            boolean room2 = this.hasRoomTarget(this.target2, value) || this.target1 == null;
            if (room1 != room2) {
                value = ember;
            }
            if (room1 || !room1 && !room2) {
                EmberPacketEntity packet1 = RegistryManager.EMBER_PACKET.get().create(this.f_58857_);
                Vec3 velocity1 = EmberEmitterBlockEntity.getBurstVelocity(Direction.get(Direction.AxisDirection.POSITIVE, axis));
                packet1.initCustom(this.f_58858_, this.target1, velocity1.x, velocity1.y, velocity1.z, value);
                this.f_58857_.m_7967_(packet1);
            }
            if (room2 || !room1 && !room2) {
                EmberPacketEntity packet2 = RegistryManager.EMBER_PACKET.get().create(this.f_58857_);
                Vec3 velocity2 = EmberEmitterBlockEntity.getBurstVelocity(Direction.get(Direction.AxisDirection.NEGATIVE, axis));
                packet2.initCustom(this.f_58858_, this.target2, velocity2.x, velocity2.y, velocity2.z, value);
                this.f_58857_.m_7967_(packet2);
            }
            this.f_58857_.playSound(null, this.f_58858_, ember >= 100.0 ? EmbersSounds.EMBER_EMIT_BIG.get() : EmbersSounds.EMBER_EMIT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void setTargetPosition(BlockPos pos, Direction side) {
        if (pos != this.f_58858_ && side.getAxis() == this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.AXIS)) {
            if (side.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                this.target1 = pos;
            } else {
                this.target2 = pos;
            }
            this.m_6596_();
        }
    }

    @Override
    public Direction getEmittingDirection(Direction side) {
        return side.getAxis() == this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.AXIS) ? side : null;
    }
}