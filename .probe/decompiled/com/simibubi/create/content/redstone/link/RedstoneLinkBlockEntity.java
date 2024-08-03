package com.simibubi.create.content.redstone.link;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

public class RedstoneLinkBlockEntity extends SmartBlockEntity {

    private boolean receivedSignalChanged;

    private int receivedSignal;

    private int transmittedSignal;

    private LinkBehaviour link;

    private boolean transmitter;

    public RedstoneLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void addBehavioursDeferred(List<BlockEntityBehaviour> behaviours) {
        this.createLink();
        behaviours.add(this.link);
    }

    protected void createLink() {
        Pair<ValueBoxTransform, ValueBoxTransform> slots = ValueBoxTransform.Dual.makeSlots(RedstoneLinkFrequencySlot::new);
        this.link = this.transmitter ? LinkBehaviour.transmitter(this, slots, this::getSignal) : LinkBehaviour.receiver(this, slots, this::setSignal);
    }

    public int getSignal() {
        return this.transmittedSignal;
    }

    public void setSignal(int power) {
        if (this.receivedSignal != power) {
            this.receivedSignalChanged = true;
        }
        this.receivedSignal = power;
    }

    public void transmit(int strength) {
        this.transmittedSignal = strength;
        if (this.link != null) {
            this.link.notifySignalChange();
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Transmitter", this.transmitter);
        compound.putInt("Receive", this.getReceivedSignal());
        compound.putBoolean("ReceivedChanged", this.receivedSignalChanged);
        compound.putInt("Transmit", this.transmittedSignal);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.transmitter = compound.getBoolean("Transmitter");
        super.read(compound, clientPacket);
        this.receivedSignal = compound.getInt("Receive");
        this.receivedSignalChanged = compound.getBoolean("ReceivedChanged");
        if (this.f_58857_ == null || this.f_58857_.isClientSide || !this.link.newPosition) {
            this.transmittedSignal = compound.getInt("Transmit");
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isTransmitterBlock() != this.transmitter) {
            this.transmitter = this.isTransmitterBlock();
            LinkBehaviour prevlink = this.link;
            this.removeBehaviour(LinkBehaviour.TYPE);
            this.createLink();
            this.link.copyItemsFrom(prevlink);
            this.attachBehaviourLate(this.link);
        }
        if (!this.transmitter) {
            if (!this.f_58857_.isClientSide) {
                BlockState blockState = this.m_58900_();
                if (AllBlocks.REDSTONE_LINK.has(blockState)) {
                    if (this.getReceivedSignal() > 0 != (Boolean) blockState.m_61143_(RedstoneLinkBlock.POWERED)) {
                        this.receivedSignalChanged = true;
                        this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) blockState.m_61122_(RedstoneLinkBlock.POWERED));
                    }
                    if (this.receivedSignalChanged) {
                        Direction attachedFace = ((Direction) blockState.m_61143_(RedstoneLinkBlock.f_52588_)).getOpposite();
                        BlockPos attachedPos = this.f_58858_.relative(attachedFace);
                        this.f_58857_.m_6289_(this.f_58858_, this.f_58857_.getBlockState(this.f_58858_).m_60734_());
                        this.f_58857_.m_6289_(attachedPos, this.f_58857_.getBlockState(attachedPos).m_60734_());
                        this.receivedSignalChanged = false;
                    }
                }
            }
        }
    }

    protected Boolean isTransmitterBlock() {
        return !(Boolean) this.m_58900_().m_61143_(RedstoneLinkBlock.RECEIVER);
    }

    public int getReceivedSignal() {
        return this.receivedSignal;
    }
}