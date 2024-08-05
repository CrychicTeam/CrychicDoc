package com.simibubi.create.content.trains.signal;

import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SignalBlockEntity extends SmartBlockEntity implements ITransformableBlockEntity {

    public TrackTargetingBehaviour<SignalBoundary> edgePoint;

    private SignalBlockEntity.SignalState state = SignalBlockEntity.SignalState.INVALID;

    private SignalBlockEntity.OverlayState overlay = SignalBlockEntity.OverlayState.SKIP;

    private int switchToRedAfterTrainEntered;

    private boolean lastReportedPower = false;

    public SignalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        NBTHelper.writeEnum(tag, "State", this.state);
        NBTHelper.writeEnum(tag, "Overlay", this.overlay);
        tag.putBoolean("Power", this.lastReportedPower);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.state = NBTHelper.readEnum(tag, "State", SignalBlockEntity.SignalState.class);
        this.overlay = NBTHelper.readEnum(tag, "Overlay", SignalBlockEntity.OverlayState.class);
        this.lastReportedPower = tag.getBoolean("Power");
        this.invalidateRenderBoundingBox();
    }

    @Nullable
    public SignalBoundary getSignal() {
        return this.edgePoint.getEdgePoint();
    }

    public boolean isPowered() {
        return this.state == SignalBlockEntity.SignalState.RED;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.edgePoint = new TrackTargetingBehaviour<>(this, EdgePointType.SIGNAL);
        behaviours.add(this.edgePoint);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_58857_.isClientSide) {
            SignalBoundary boundary = this.getSignal();
            if (boundary == null) {
                this.enterState(SignalBlockEntity.SignalState.INVALID);
                this.setOverlay(SignalBlockEntity.OverlayState.RENDER);
            } else {
                BlockState blockState = this.m_58900_();
                blockState.m_61145_(SignalBlock.POWERED).ifPresent(powered -> {
                    if (this.lastReportedPower != powered) {
                        this.lastReportedPower = powered;
                        boundary.updateBlockEntityPower(this);
                        this.notifyUpdate();
                    }
                });
                blockState.m_61145_(SignalBlock.TYPE).ifPresent(stateType -> {
                    SignalBlock.SignalType targetType = boundary.getTypeFor(this.f_58858_);
                    if (stateType != targetType) {
                        this.f_58857_.setBlock(this.f_58858_, (BlockState) blockState.m_61124_(SignalBlock.TYPE, targetType), 3);
                        this.refreshBlockState();
                    }
                });
                this.enterState(boundary.getStateFor(this.f_58858_));
                this.setOverlay(boundary.getOverlayFor(this.f_58858_));
            }
        }
    }

    public boolean getReportedPower() {
        return this.lastReportedPower;
    }

    public SignalBlockEntity.SignalState getState() {
        return this.state;
    }

    public SignalBlockEntity.OverlayState getOverlay() {
        return this.overlay;
    }

    public void setOverlay(SignalBlockEntity.OverlayState state) {
        if (this.overlay != state) {
            this.overlay = state;
            this.notifyUpdate();
        }
    }

    public void enterState(SignalBlockEntity.SignalState state) {
        if (this.switchToRedAfterTrainEntered > 0) {
            this.switchToRedAfterTrainEntered--;
        }
        if (this.state != state) {
            if (state != SignalBlockEntity.SignalState.RED || this.switchToRedAfterTrainEntered <= 0) {
                this.state = state;
                this.switchToRedAfterTrainEntered = state != SignalBlockEntity.SignalState.GREEN && state != SignalBlockEntity.SignalState.YELLOW ? 0 : 15;
                this.notifyUpdate();
            }
        }
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_, this.edgePoint.getGlobalPosition()).inflate(2.0);
    }

    @Override
    public void transform(StructureTransform transform) {
        this.edgePoint.transform(transform);
    }

    public static enum OverlayState {

        RENDER, SKIP, DUAL
    }

    public static enum SignalState {

        RED, YELLOW, GREEN, INVALID;

        public boolean isRedLight(float renderTime) {
            return this == RED || this == INVALID && renderTime % 40.0F < 3.0F;
        }

        public boolean isYellowLight(float renderTime) {
            return this == YELLOW;
        }

        public boolean isGreenLight(float renderTime) {
            return this == GREEN;
        }
    }
}