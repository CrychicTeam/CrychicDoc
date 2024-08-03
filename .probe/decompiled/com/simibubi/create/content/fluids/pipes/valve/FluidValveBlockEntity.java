package com.simibubi.create.content.fluids.pipes.valve;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class FluidValveBlockEntity extends KineticBlockEntity {

    LerpedFloat pointer = LerpedFloat.linear().startWithValue(0.0).chase(0.0, 0.0, LerpedFloat.Chaser.LINEAR);

    public FluidValveBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        float speed = this.getSpeed();
        this.pointer.chase(speed > 0.0F ? 1.0 : 0.0, (double) this.getChaseSpeed(), LerpedFloat.Chaser.LINEAR);
        this.sendData();
    }

    @Override
    public void tick() {
        super.tick();
        this.pointer.tickChaser();
        if (!this.f_58857_.isClientSide) {
            BlockState blockState = this.m_58900_();
            if (blockState.m_60734_() instanceof FluidValveBlock) {
                boolean stateOpen = (Boolean) blockState.m_61143_(FluidValveBlock.ENABLED);
                if (stateOpen && this.pointer.getValue() == 0.0F) {
                    switchToBlockState(this.f_58857_, this.f_58858_, (BlockState) blockState.m_61124_(FluidValveBlock.ENABLED, false));
                } else if (!stateOpen && this.pointer.getValue() == 1.0F) {
                    switchToBlockState(this.f_58857_, this.f_58858_, (BlockState) blockState.m_61124_(FluidValveBlock.ENABLED, true));
                }
            }
        }
    }

    private float getChaseSpeed() {
        return Mth.clamp(Math.abs(this.getSpeed()) / 16.0F / 20.0F, 0.0F, 1.0F);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Pointer", this.pointer.writeNBT());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.pointer.readNBT(compound.getCompound("Pointer"), clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new FluidValveBlockEntity.ValvePipeBehaviour(this));
        this.registerAwardables(behaviours, FluidPropagator.getSharedTriggers());
    }

    class ValvePipeBehaviour extends StraightPipeBlockEntity.StraightPipeFluidTransportBehaviour {

        public ValvePipeBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public boolean canHaveFlowToward(BlockState state, Direction direction) {
            return FluidValveBlock.getPipeAxis(state) == direction.getAxis();
        }

        @Override
        public boolean canPullFluidFrom(FluidStack fluid, BlockState state, Direction direction) {
            return state.m_61138_(FluidValveBlock.ENABLED) && state.m_61143_(FluidValveBlock.ENABLED) ? super.canPullFluidFrom(fluid, state, direction) : false;
        }
    }
}