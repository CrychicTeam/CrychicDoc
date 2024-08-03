package com.simibubi.create.content.kinetics.crank;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HandCrankBlockEntity extends GeneratingKineticBlockEntity {

    public int inUse;

    public boolean backwards;

    public float independentAngle;

    public float chasingVelocity;

    public HandCrankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void turn(boolean back) {
        boolean update = false;
        if (this.getGeneratedSpeed() == 0.0F || back != this.backwards) {
            update = true;
        }
        this.inUse = 10;
        this.backwards = back;
        if (update && !this.f_58857_.isClientSide) {
            this.updateGeneratedRotation();
        }
    }

    public float getIndependentAngle(float partialTicks) {
        return (this.independentAngle + partialTicks * this.chasingVelocity) / 360.0F;
    }

    @Override
    public float getGeneratedSpeed() {
        if (!(this.m_58900_().m_60734_() instanceof HandCrankBlock crank)) {
            return 0.0F;
        } else {
            int speed = (this.inUse == 0 ? 0 : (this.clockwise() ? -1 : 1)) * crank.getRotationSpeed();
            return convertToDirection((float) speed, (Direction) this.m_58900_().m_61143_(HandCrankBlock.FACING));
        }
    }

    protected boolean clockwise() {
        return this.backwards;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("InUse", this.inUse);
        compound.putBoolean("Backwards", this.backwards);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.inUse = compound.getInt("InUse");
        this.backwards = compound.getBoolean("Backwards");
        super.read(compound, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();
        float actualSpeed = this.getSpeed();
        this.chasingVelocity = this.chasingVelocity + (actualSpeed * 10.0F / 3.0F - this.chasingVelocity) * 0.25F;
        this.independentAngle = this.independentAngle + this.chasingVelocity;
        if (this.inUse > 0) {
            this.inUse--;
            if (this.inUse == 0 && !this.f_58857_.isClientSide) {
                this.sequenceContext = null;
                this.updateGeneratedRotation();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public SuperByteBuffer getRenderedHandle() {
        BlockState blockState = this.m_58900_();
        Direction facing = (Direction) blockState.m_61145_(HandCrankBlock.FACING).orElse(Direction.UP);
        return CachedBufferer.partialFacing(AllPartialModels.HAND_CRANK_HANDLE, blockState, facing.getOpposite());
    }

    @OnlyIn(Dist.CLIENT)
    public Instancer<ModelData> getRenderedHandleInstance(Material<ModelData> material) {
        BlockState blockState = this.m_58900_();
        Direction facing = (Direction) blockState.m_61145_(HandCrankBlock.FACING).orElse(Direction.UP);
        return material.getModel(AllPartialModels.HAND_CRANK_HANDLE, blockState, facing.getOpposite());
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderShaft() {
        return true;
    }

    @Override
    protected Block getStressConfigKey() {
        return AllBlocks.HAND_CRANK.has(this.m_58900_()) ? (Block) AllBlocks.HAND_CRANK.get() : (Block) AllBlocks.COPPER_VALVE_HANDLE.get();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void tickAudio() {
        super.tickAudio();
        if (this.inUse > 0 && AnimationTickHolder.getTicks() % 10 == 0) {
            if (!AllBlocks.HAND_CRANK.has(this.m_58900_())) {
                return;
            }
            AllSoundEvents.CRANKING.playAt(this.f_58857_, this.f_58858_, (float) this.inUse / 2.5F, 0.65F + (float) (10 - this.inUse) / 10.0F, true);
        }
    }
}