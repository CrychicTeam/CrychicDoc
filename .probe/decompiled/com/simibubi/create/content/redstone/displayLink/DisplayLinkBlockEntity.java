package com.simibubi.create.content.redstone.displayLink;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.ComputerCraftProxy;
import com.simibubi.create.content.redstone.displayLink.source.DisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTarget;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisplayLinkBlockEntity extends SmartBlockEntity {

    protected BlockPos targetOffset = BlockPos.ZERO;

    public DisplaySource activeSource;

    private CompoundTag sourceConfig = new CompoundTag();

    public DisplayTarget activeTarget;

    public int targetLine = 0;

    public LerpedFloat glow = LerpedFloat.linear().startWithValue(0.0);

    private boolean sendPulse;

    public int refreshTicks;

    public AbstractComputerBehaviour computerBehaviour;

    public DisplayLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.glow.chase(0.0, 0.5, LerpedFloat.Chaser.EXP);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.computerBehaviour = ComputerCraftProxy.behaviour(this));
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.DISPLAY_LINK, AllAdvancements.DISPLAY_BOARD });
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isVirtual()) {
            this.glow.tickChaser();
        } else if (this.activeSource != null) {
            if (this.f_58857_.isClientSide) {
                this.glow.tickChaser();
            } else {
                this.refreshTicks++;
                if (this.refreshTicks >= this.activeSource.getPassiveRefreshTicks() && this.activeSource.shouldPassiveReset()) {
                    this.tickSource();
                }
            }
        }
    }

    public void tickSource() {
        this.refreshTicks = 0;
        if (!(Boolean) this.m_58900_().m_61145_(DisplayLinkBlock.POWERED).orElse(true)) {
            if (!this.f_58857_.isClientSide) {
                this.updateGatheredData();
            }
        }
    }

    public void onNoLongerPowered() {
        if (this.activeSource != null) {
            this.refreshTicks = 0;
            this.activeSource.onSignalReset(new DisplayLinkContext(this.f_58857_, this));
            this.updateGatheredData();
        }
    }

    public void updateGatheredData() {
        BlockPos sourcePosition = this.getSourcePosition();
        BlockPos targetPosition = this.getTargetPosition();
        if (this.f_58857_.isLoaded(targetPosition) && this.f_58857_.isLoaded(sourcePosition)) {
            DisplayTarget target = AllDisplayBehaviours.targetOf(this.f_58857_, targetPosition);
            List<DisplaySource> sources = AllDisplayBehaviours.sourcesOf(this.f_58857_, sourcePosition);
            boolean notify = false;
            if (this.activeTarget != target) {
                this.activeTarget = target;
                notify = true;
            }
            if (this.activeSource != null && !sources.contains(this.activeSource)) {
                this.activeSource = null;
                this.sourceConfig = new CompoundTag();
                notify = true;
            }
            if (notify) {
                this.notifyUpdate();
            }
            if (this.activeSource != null && this.activeTarget != null) {
                DisplayLinkContext context = new DisplayLinkContext(this.f_58857_, this);
                this.activeSource.transferData(context, this.activeTarget, this.targetLine);
                this.sendPulse = true;
                this.sendData();
                this.award(AllAdvancements.DISPLAY_LINK);
            }
        }
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        super.writeSafe(tag);
        this.writeGatheredData(tag);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        this.writeGatheredData(tag);
        if (clientPacket && this.activeTarget != null) {
            tag.putString("TargetType", this.activeTarget.id.toString());
        }
        if (clientPacket && this.sendPulse) {
            this.sendPulse = false;
            NBTHelper.putMarker(tag, "Pulse");
        }
    }

    private void writeGatheredData(CompoundTag tag) {
        tag.put("TargetOffset", NbtUtils.writeBlockPos(this.targetOffset));
        tag.putInt("TargetLine", this.targetLine);
        if (this.activeSource != null) {
            CompoundTag data = this.sourceConfig.copy();
            data.putString("Id", this.activeSource.id.toString());
            tag.put("Source", data);
        }
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.targetOffset = NbtUtils.readBlockPos(tag.getCompound("TargetOffset"));
        this.targetLine = tag.getInt("TargetLine");
        if (clientPacket && tag.contains("TargetType")) {
            this.activeTarget = AllDisplayBehaviours.getTarget(new ResourceLocation(tag.getString("TargetType")));
        }
        if (clientPacket && tag.contains("Pulse")) {
            this.glow.setValue(2.0);
        }
        if (tag.contains("Source")) {
            CompoundTag data = tag.getCompound("Source");
            this.activeSource = AllDisplayBehaviours.getSource(new ResourceLocation(data.getString("Id")));
            this.sourceConfig = new CompoundTag();
            if (this.activeSource != null) {
                this.sourceConfig = data.copy();
            }
        }
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.computerBehaviour.isPeripheralCap(cap) ? this.computerBehaviour.getPeripheralCapability() : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.computerBehaviour.removePeripheral();
    }

    public void target(BlockPos targetPosition) {
        this.targetOffset = targetPosition.subtract(this.f_58858_);
    }

    public BlockPos getSourcePosition() {
        return this.f_58858_.relative(this.getDirection());
    }

    public CompoundTag getSourceConfig() {
        return this.sourceConfig;
    }

    public void setSourceConfig(CompoundTag sourceConfig) {
        this.sourceConfig = sourceConfig;
    }

    public Direction getDirection() {
        return ((Direction) this.m_58900_().m_61145_(DisplayLinkBlock.f_52588_).orElse(Direction.UP)).getOpposite();
    }

    public BlockPos getTargetPosition() {
        return this.f_58858_.offset(this.targetOffset);
    }
}