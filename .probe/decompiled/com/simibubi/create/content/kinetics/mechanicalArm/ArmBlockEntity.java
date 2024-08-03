package com.simibubi.create.content.kinetics.mechanicalArm;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class ArmBlockEntity extends KineticBlockEntity implements ITransformableBlockEntity {

    List<ArmInteractionPoint> inputs;

    List<ArmInteractionPoint> outputs;

    ListTag interactionPointTag;

    float chasedPointProgress;

    int chasedPointIndex;

    ItemStack heldItem;

    ArmBlockEntity.Phase phase;

    boolean goggles;

    ArmAngleTarget previousTarget;

    LerpedFloat lowerArmAngle;

    LerpedFloat upperArmAngle;

    LerpedFloat baseAngle;

    LerpedFloat headAngle;

    LerpedFloat clawAngle;

    float previousBaseAngle;

    boolean updateInteractionPoints;

    int tooltipWarmup;

    protected ScrollOptionBehaviour<ArmBlockEntity.SelectionMode> selectionMode;

    protected int lastInputIndex = -1;

    protected int lastOutputIndex = -1;

    protected boolean redstoneLocked;

    public ArmBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.inputs = new ArrayList();
        this.outputs = new ArrayList();
        this.interactionPointTag = null;
        this.heldItem = ItemStack.EMPTY;
        this.phase = ArmBlockEntity.Phase.SEARCH_INPUTS;
        this.previousTarget = ArmAngleTarget.NO_TARGET;
        this.baseAngle = LerpedFloat.angular();
        this.baseAngle.startWithValue((double) this.previousTarget.baseAngle);
        this.lowerArmAngle = LerpedFloat.angular();
        this.lowerArmAngle.startWithValue((double) this.previousTarget.lowerArmAngle);
        this.upperArmAngle = LerpedFloat.angular();
        this.upperArmAngle.startWithValue((double) this.previousTarget.upperArmAngle);
        this.headAngle = LerpedFloat.angular();
        this.headAngle.startWithValue((double) this.previousTarget.headAngle);
        this.clawAngle = LerpedFloat.angular();
        this.previousBaseAngle = this.previousTarget.baseAngle;
        this.updateInteractionPoints = true;
        this.redstoneLocked = false;
        this.tooltipWarmup = 15;
        this.goggles = false;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.selectionMode = new ScrollOptionBehaviour(ArmBlockEntity.SelectionMode.class, Lang.translateDirect("logistics.when_multiple_outputs_available"), this, new ArmBlockEntity.SelectionModeValueBox());
        behaviours.add(this.selectionMode);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.ARM_BLAZE_BURNER, AllAdvancements.ARM_MANY_TARGETS, AllAdvancements.MECHANICAL_ARM, AllAdvancements.MUSICAL_ARM });
    }

    @Override
    public void tick() {
        super.tick();
        this.initInteractionPoints();
        boolean targetReached = this.tickMovementProgress();
        if (this.tooltipWarmup > 0) {
            this.tooltipWarmup--;
        }
        if (this.chasedPointProgress < 1.0F) {
            if (this.phase == ArmBlockEntity.Phase.MOVE_TO_INPUT) {
                ArmInteractionPoint point = this.getTargetedInteractionPoint();
                if (point != null) {
                    point.keepAlive();
                }
            }
        } else if (!this.f_58857_.isClientSide) {
            if (this.phase == ArmBlockEntity.Phase.MOVE_TO_INPUT) {
                this.collectItem();
            } else if (this.phase == ArmBlockEntity.Phase.MOVE_TO_OUTPUT) {
                this.depositItem();
            } else if (this.phase == ArmBlockEntity.Phase.SEARCH_INPUTS || this.phase == ArmBlockEntity.Phase.DANCING) {
                this.searchForItem();
            }
            if (targetReached) {
                this.lazyTick();
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!this.f_58857_.isClientSide) {
            if (!(this.chasedPointProgress < 0.5F)) {
                if (this.phase == ArmBlockEntity.Phase.SEARCH_INPUTS || this.phase == ArmBlockEntity.Phase.DANCING) {
                    this.checkForMusic();
                }
                if (this.phase == ArmBlockEntity.Phase.SEARCH_OUTPUTS) {
                    this.searchForDestination();
                }
            }
        }
    }

    private void checkForMusic() {
        boolean hasMusic = this.checkForMusicAmong(this.inputs) || this.checkForMusicAmong(this.outputs);
        if (hasMusic != (this.phase == ArmBlockEntity.Phase.DANCING)) {
            this.phase = hasMusic ? ArmBlockEntity.Phase.DANCING : ArmBlockEntity.Phase.SEARCH_INPUTS;
            this.m_6596_();
            this.sendData();
        }
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(3.0);
    }

    private boolean checkForMusicAmong(List<ArmInteractionPoint> list) {
        for (ArmInteractionPoint armInteractionPoint : list) {
            if (armInteractionPoint instanceof AllArmInteractionPointTypes.JukeboxPoint) {
                BlockState state = this.f_58857_.getBlockState(armInteractionPoint.getPos());
                if ((Boolean) state.m_61145_(JukeboxBlock.HAS_RECORD).orElse(false)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean tickMovementProgress() {
        boolean targetReachedPreviously = this.chasedPointProgress >= 1.0F;
        this.chasedPointProgress = this.chasedPointProgress + Math.min(256.0F, Math.abs(this.getSpeed())) / 1024.0F;
        if (this.chasedPointProgress > 1.0F) {
            this.chasedPointProgress = 1.0F;
        }
        if (this.f_58857_.isClientSide) {
            ArmInteractionPoint targetedInteractionPoint = this.getTargetedInteractionPoint();
            ArmAngleTarget previousTarget = this.previousTarget;
            ArmAngleTarget target = targetedInteractionPoint == null ? ArmAngleTarget.NO_TARGET : targetedInteractionPoint.getTargetAngles(this.f_58858_, this.isOnCeiling());
            this.baseAngle.setValue((double) AngleHelper.angleLerp((double) this.chasedPointProgress, (double) this.previousBaseAngle, target == ArmAngleTarget.NO_TARGET ? (double) this.previousBaseAngle : (double) target.baseAngle));
            if (this.chasedPointProgress < 0.5F) {
                target = ArmAngleTarget.NO_TARGET;
            } else {
                previousTarget = ArmAngleTarget.NO_TARGET;
            }
            float progress = this.chasedPointProgress == 1.0F ? 1.0F : this.chasedPointProgress % 0.5F * 2.0F;
            this.lowerArmAngle.setValue((double) Mth.lerp(progress, previousTarget.lowerArmAngle, target.lowerArmAngle));
            this.upperArmAngle.setValue((double) Mth.lerp(progress, previousTarget.upperArmAngle, target.upperArmAngle));
            this.headAngle.setValue((double) AngleHelper.angleLerp((double) progress, (double) (previousTarget.headAngle % 360.0F), (double) (target.headAngle % 360.0F)));
            return false;
        } else {
            return !targetReachedPreviously && this.chasedPointProgress >= 1.0F;
        }
    }

    protected boolean isOnCeiling() {
        BlockState state = this.m_58900_();
        return this.m_58898_() && (Boolean) state.m_61145_(ArmBlock.CEILING).orElse(false);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (!this.heldItem.isEmpty()) {
            Block.popResource(this.f_58857_, this.f_58858_, this.heldItem);
        }
    }

    @Nullable
    private ArmInteractionPoint getTargetedInteractionPoint() {
        if (this.chasedPointIndex == -1) {
            return null;
        } else if (this.phase == ArmBlockEntity.Phase.MOVE_TO_INPUT && this.chasedPointIndex < this.inputs.size()) {
            return (ArmInteractionPoint) this.inputs.get(this.chasedPointIndex);
        } else {
            return this.phase == ArmBlockEntity.Phase.MOVE_TO_OUTPUT && this.chasedPointIndex < this.outputs.size() ? (ArmInteractionPoint) this.outputs.get(this.chasedPointIndex) : null;
        }
    }

    protected void searchForItem() {
        if (!this.redstoneLocked) {
            boolean foundInput = false;
            int startIndex = this.selectionMode.get() == ArmBlockEntity.SelectionMode.PREFER_FIRST ? 0 : this.lastInputIndex + 1;
            int scanRange = this.selectionMode.get() == ArmBlockEntity.SelectionMode.FORCED_ROUND_ROBIN ? this.lastInputIndex + 2 : this.inputs.size();
            if (scanRange > this.inputs.size()) {
                scanRange = this.inputs.size();
            }
            label56: for (int i = startIndex; i < scanRange; i++) {
                ArmInteractionPoint armInteractionPoint = (ArmInteractionPoint) this.inputs.get(i);
                if (armInteractionPoint.isValid()) {
                    for (int j = 0; j < armInteractionPoint.getSlotCount(); j++) {
                        if (this.getDistributableAmount(armInteractionPoint, j) != 0) {
                            this.selectIndex(true, i);
                            foundInput = true;
                            break label56;
                        }
                    }
                }
            }
            if (!foundInput && this.selectionMode.get() == ArmBlockEntity.SelectionMode.ROUND_ROBIN) {
                this.lastInputIndex = -1;
            }
            if (this.lastInputIndex == this.inputs.size() - 1) {
                this.lastInputIndex = -1;
            }
        }
    }

    protected void searchForDestination() {
        ItemStack held = this.heldItem.copy();
        boolean foundOutput = false;
        int startIndex = this.selectionMode.get() == ArmBlockEntity.SelectionMode.PREFER_FIRST ? 0 : this.lastOutputIndex + 1;
        int scanRange = this.selectionMode.get() == ArmBlockEntity.SelectionMode.FORCED_ROUND_ROBIN ? this.lastOutputIndex + 2 : this.outputs.size();
        if (scanRange > this.outputs.size()) {
            scanRange = this.outputs.size();
        }
        for (int i = startIndex; i < scanRange; i++) {
            ArmInteractionPoint armInteractionPoint = (ArmInteractionPoint) this.outputs.get(i);
            if (armInteractionPoint.isValid()) {
                ItemStack remainder = armInteractionPoint.insert(held, true);
                if (!remainder.equals(this.heldItem, false)) {
                    this.selectIndex(false, i);
                    foundOutput = true;
                    break;
                }
            }
        }
        if (!foundOutput && this.selectionMode.get() == ArmBlockEntity.SelectionMode.ROUND_ROBIN) {
            this.lastOutputIndex = -1;
        }
        if (this.lastOutputIndex == this.outputs.size() - 1) {
            this.lastOutputIndex = -1;
        }
    }

    private void selectIndex(boolean input, int index) {
        this.phase = input ? ArmBlockEntity.Phase.MOVE_TO_INPUT : ArmBlockEntity.Phase.MOVE_TO_OUTPUT;
        this.chasedPointIndex = index;
        this.chasedPointProgress = 0.0F;
        if (input) {
            this.lastInputIndex = index;
        } else {
            this.lastOutputIndex = index;
        }
        this.sendData();
        this.m_6596_();
    }

    protected int getDistributableAmount(ArmInteractionPoint armInteractionPoint, int i) {
        ItemStack stack = armInteractionPoint.extract(i, true);
        ItemStack remainder = this.simulateInsertion(stack);
        return ItemStack.isSameItem(stack, remainder) ? stack.getCount() - remainder.getCount() : stack.getCount();
    }

    private ItemStack simulateInsertion(ItemStack stack) {
        for (ArmInteractionPoint armInteractionPoint : this.outputs) {
            if (armInteractionPoint.isValid()) {
                stack = armInteractionPoint.insert(stack, true);
            }
            if (stack.isEmpty()) {
                break;
            }
        }
        return stack;
    }

    protected void depositItem() {
        ArmInteractionPoint armInteractionPoint = this.getTargetedInteractionPoint();
        if (armInteractionPoint != null && armInteractionPoint.isValid()) {
            ItemStack toInsert = this.heldItem.copy();
            ItemStack remainder = armInteractionPoint.insert(toInsert, false);
            this.heldItem = remainder;
            if (armInteractionPoint instanceof AllArmInteractionPointTypes.JukeboxPoint && remainder.isEmpty()) {
                this.award(AllAdvancements.MUSICAL_ARM);
            }
        }
        this.phase = this.heldItem.isEmpty() ? ArmBlockEntity.Phase.SEARCH_INPUTS : ArmBlockEntity.Phase.SEARCH_OUTPUTS;
        this.chasedPointProgress = 0.0F;
        this.chasedPointIndex = -1;
        this.sendData();
        this.m_6596_();
        if (!this.f_58857_.isClientSide) {
            this.award(AllAdvancements.MECHANICAL_ARM);
        }
    }

    protected void collectItem() {
        ArmInteractionPoint armInteractionPoint = this.getTargetedInteractionPoint();
        if (armInteractionPoint != null && armInteractionPoint.isValid()) {
            for (int i = 0; i < armInteractionPoint.getSlotCount(); i++) {
                int amountExtracted = this.getDistributableAmount(armInteractionPoint, i);
                if (amountExtracted != 0) {
                    ItemStack prevHeld = this.heldItem;
                    this.heldItem = armInteractionPoint.extract(i, amountExtracted, false);
                    this.phase = ArmBlockEntity.Phase.SEARCH_OUTPUTS;
                    this.chasedPointProgress = 0.0F;
                    this.chasedPointIndex = -1;
                    this.sendData();
                    this.m_6596_();
                    if (!ItemStack.isSameItem(this.heldItem, prevHeld)) {
                        this.f_58857_.playSound(null, this.f_58858_, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.125F, 0.5F + Create.RANDOM.nextFloat() * 0.25F);
                    }
                    return;
                }
            }
        }
        this.phase = ArmBlockEntity.Phase.SEARCH_INPUTS;
        this.chasedPointProgress = 0.0F;
        this.chasedPointIndex = -1;
        this.sendData();
        this.m_6596_();
    }

    public void redstoneUpdate() {
        if (!this.f_58857_.isClientSide) {
            boolean blockPowered = this.f_58857_.m_276867_(this.f_58858_);
            if (blockPowered != this.redstoneLocked) {
                this.redstoneLocked = blockPowered;
                this.sendData();
                if (!this.redstoneLocked) {
                    this.searchForItem();
                }
            }
        }
    }

    @Override
    public void transform(StructureTransform transform) {
        if (this.interactionPointTag != null) {
            for (Tag tag : this.interactionPointTag) {
                ArmInteractionPoint.transformPos((CompoundTag) tag, transform);
            }
            this.notifyUpdate();
        }
    }

    protected boolean isAreaActuallyLoaded(BlockPos center, int range) {
        if (!this.f_58857_.isAreaLoaded(center, range)) {
            return false;
        } else {
            if (this.f_58857_.isClientSide) {
                int minY = center.m_123342_() - range;
                int maxY = center.m_123342_() + range;
                if (maxY < this.f_58857_.m_141937_() || minY >= this.f_58857_.m_151558_()) {
                    return false;
                }
                int minX = center.m_123341_() - range;
                int minZ = center.m_123343_() - range;
                int maxX = center.m_123341_() + range;
                int maxZ = center.m_123343_() + range;
                int minChunkX = SectionPos.blockToSectionCoord(minX);
                int maxChunkX = SectionPos.blockToSectionCoord(maxX);
                int minChunkZ = SectionPos.blockToSectionCoord(minZ);
                int maxChunkZ = SectionPos.blockToSectionCoord(maxZ);
                ChunkSource chunkSource = this.f_58857_.m_7726_();
                for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
                    for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                        if (!chunkSource.hasChunk(chunkX, chunkZ)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    protected void initInteractionPoints() {
        if (this.updateInteractionPoints && this.interactionPointTag != null) {
            if (this.isAreaActuallyLoaded(this.f_58858_, getRange() + 1)) {
                this.inputs.clear();
                this.outputs.clear();
                boolean hasBlazeBurner = false;
                for (Tag tag : this.interactionPointTag) {
                    ArmInteractionPoint point = ArmInteractionPoint.deserialize((CompoundTag) tag, this.f_58857_, this.f_58858_);
                    if (point != null) {
                        if (point.getMode() == ArmInteractionPoint.Mode.DEPOSIT) {
                            this.outputs.add(point);
                        } else if (point.getMode() == ArmInteractionPoint.Mode.TAKE) {
                            this.inputs.add(point);
                        }
                        hasBlazeBurner |= point instanceof AllArmInteractionPointTypes.BlazeBurnerPoint;
                    }
                }
                if (!this.f_58857_.isClientSide) {
                    if (this.outputs.size() >= 10) {
                        this.award(AllAdvancements.ARM_MANY_TARGETS);
                    }
                    if (hasBlazeBurner) {
                        this.award(AllAdvancements.ARM_BLAZE_BURNER);
                    }
                }
                this.updateInteractionPoints = false;
                this.sendData();
                this.m_6596_();
            }
        }
    }

    public void writeInteractionPoints(CompoundTag compound) {
        if (this.updateInteractionPoints && this.interactionPointTag != null) {
            compound.put("InteractionPoints", this.interactionPointTag);
        } else {
            ListTag pointsNBT = new ListTag();
            this.inputs.stream().map(aip -> aip.serialize(this.f_58858_)).forEach(pointsNBT::add);
            this.outputs.stream().map(aip -> aip.serialize(this.f_58858_)).forEach(pointsNBT::add);
            compound.put("InteractionPoints", pointsNBT);
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        this.writeInteractionPoints(compound);
        NBTHelper.writeEnum(compound, "Phase", this.phase);
        compound.putBoolean("Powered", this.redstoneLocked);
        compound.putBoolean("Goggles", this.goggles);
        compound.put("HeldItem", this.heldItem.serializeNBT());
        compound.putInt("TargetPointIndex", this.chasedPointIndex);
        compound.putFloat("MovementProgress", this.chasedPointProgress);
    }

    @Override
    public void writeSafe(CompoundTag compound) {
        super.writeSafe(compound);
        this.writeInteractionPoints(compound);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        int previousIndex = this.chasedPointIndex;
        ArmBlockEntity.Phase previousPhase = this.phase;
        ListTag interactionPointTagBefore = this.interactionPointTag;
        super.read(compound, clientPacket);
        this.heldItem = ItemStack.of(compound.getCompound("HeldItem"));
        this.phase = NBTHelper.readEnum(compound, "Phase", ArmBlockEntity.Phase.class);
        this.chasedPointIndex = compound.getInt("TargetPointIndex");
        this.chasedPointProgress = compound.getFloat("MovementProgress");
        this.interactionPointTag = compound.getList("InteractionPoints", 10);
        this.redstoneLocked = compound.getBoolean("Powered");
        boolean hadGoggles = this.goggles;
        this.goggles = compound.getBoolean("Goggles");
        if (clientPacket) {
            if (hadGoggles != this.goggles) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate(this));
            }
            boolean ceiling = this.isOnCeiling();
            if (interactionPointTagBefore == null || interactionPointTagBefore.size() != this.interactionPointTag.size()) {
                this.updateInteractionPoints = true;
            }
            if (previousIndex != this.chasedPointIndex || previousPhase != this.phase) {
                ArmInteractionPoint previousPoint = null;
                if (previousPhase == ArmBlockEntity.Phase.MOVE_TO_INPUT && previousIndex < this.inputs.size()) {
                    previousPoint = (ArmInteractionPoint) this.inputs.get(previousIndex);
                }
                if (previousPhase == ArmBlockEntity.Phase.MOVE_TO_OUTPUT && previousIndex < this.outputs.size()) {
                    previousPoint = (ArmInteractionPoint) this.outputs.get(previousIndex);
                }
                this.previousTarget = previousPoint == null ? ArmAngleTarget.NO_TARGET : previousPoint.getTargetAngles(this.f_58858_, ceiling);
                if (previousPoint != null) {
                    this.previousBaseAngle = this.previousTarget.baseAngle;
                }
                ArmInteractionPoint targetedPoint = this.getTargetedInteractionPoint();
                if (targetedPoint != null) {
                    targetedPoint.updateCachedState();
                }
            }
        }
    }

    public static int getRange() {
        return AllConfigs.server().logistics.mechanicalArmRange.get();
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (super.addToTooltip(tooltip, isPlayerSneaking)) {
            return true;
        } else if (isPlayerSneaking) {
            return false;
        } else if (this.tooltipWarmup > 0) {
            return false;
        } else if (!this.inputs.isEmpty()) {
            return false;
        } else if (!this.outputs.isEmpty()) {
            return false;
        } else {
            TooltipHelper.addHint(tooltip, "hint.mechanical_arm_no_targets");
            return true;
        }
    }

    @Override
    public void setLevel(Level level) {
        super.m_142339_(level);
        for (ArmInteractionPoint input : this.inputs) {
            input.setLevel(level);
        }
        for (ArmInteractionPoint output : this.outputs) {
            output.setLevel(level);
        }
    }

    public static enum Phase {

        SEARCH_INPUTS, MOVE_TO_INPUT, SEARCH_OUTPUTS, MOVE_TO_OUTPUT, DANCING
    }

    public static enum SelectionMode implements INamedIconOptions {

        ROUND_ROBIN(AllIcons.I_ARM_ROUND_ROBIN), FORCED_ROUND_ROBIN(AllIcons.I_ARM_FORCED_ROUND_ROBIN), PREFER_FIRST(AllIcons.I_ARM_PREFER_FIRST);

        private final String translationKey;

        private final AllIcons icon;

        private SelectionMode(AllIcons icon) {
            this.icon = icon;
            this.translationKey = "mechanical_arm.selection_mode." + Lang.asId(this.name());
        }

        @Override
        public AllIcons getIcon() {
            return this.icon;
        }

        @Override
        public String getTranslationKey() {
            return this.translationKey;
        }
    }

    private class SelectionModeValueBox extends CenteredSideValueBoxTransform {

        public SelectionModeValueBox() {
            super((blockState, direction) -> !direction.getAxis().isVertical());
        }

        @Override
        public Vec3 getLocalOffset(BlockState state) {
            int yPos = state.m_61143_(ArmBlock.CEILING) ? 13 : 3;
            Vec3 location = VecHelper.voxelSpace(8.0, (double) yPos, 15.5);
            return VecHelper.rotateCentered(location, (double) AngleHelper.horizontalAngle(this.getSide()), Direction.Axis.Y);
        }

        @Override
        public float getScale() {
            return super.getScale();
        }
    }
}