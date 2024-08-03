package com.simibubi.create.content.contraptions.pulley;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.BlockMovementChecks;
import com.simibubi.create.content.contraptions.ContraptionCollider;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.piston.LinearActuatorBlockEntity;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchObservable;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PulleyBlockEntity extends LinearActuatorBlockEntity implements ThresholdSwitchObservable {

    protected int initialOffset;

    private float prevAnimatedOffset;

    protected BlockPos mirrorParent;

    protected List<BlockPos> mirrorChildren;

    public WeakReference<AbstractContraptionEntity> sharedMirrorContraption;

    public PulleyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        double expandY = (double) (-this.offset);
        if (this.sharedMirrorContraption != null) {
            AbstractContraptionEntity ace = (AbstractContraptionEntity) this.sharedMirrorContraption.get();
            if (ace != null) {
                expandY = ace.m_20186_() - (double) this.f_58858_.m_123342_();
            }
        }
        return super.createRenderBoundingBox().expandTowards(0.0, expandY, 0.0);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.PULLEY_MAXED });
    }

    @Override
    public void tick() {
        float prevOffset = this.offset;
        super.tick();
        if (this.f_58857_.isClientSide() && this.mirrorParent != null && (this.sharedMirrorContraption == null || this.sharedMirrorContraption.get() == null || !((AbstractContraptionEntity) this.sharedMirrorContraption.get()).m_6084_())) {
            this.sharedMirrorContraption = null;
            if (this.f_58857_.getBlockEntity(this.mirrorParent) instanceof PulleyBlockEntity pte && pte.movedContraption != null) {
                this.sharedMirrorContraption = new WeakReference(pte.movedContraption);
            }
        }
        if (this.isVirtual()) {
            this.prevAnimatedOffset = this.offset;
        }
        this.invalidateRenderBoundingBox();
        if (prevOffset < 200.0F && this.offset >= 200.0F) {
            this.award(AllAdvancements.PULLEY_MAXED);
        }
    }

    @Override
    protected boolean isPassive() {
        return this.mirrorParent != null;
    }

    @Nullable
    public AbstractContraptionEntity getAttachedContraption() {
        return this.mirrorParent != null && this.sharedMirrorContraption != null ? (AbstractContraptionEntity) this.sharedMirrorContraption.get() : this.movedContraption;
    }

    @Override
    protected void assemble() throws AssemblyException {
        if (this.f_58857_.getBlockState(this.f_58858_).m_60734_() instanceof PulleyBlock) {
            if (this.speed != 0.0F || this.mirrorParent != null) {
                int maxLength = AllConfigs.server().kinetics.maxRopeLength.get();
                int i;
                for (i = 1; i <= maxLength; i++) {
                    BlockPos ropePos = this.f_58858_.below(i);
                    BlockState ropeState = this.f_58857_.getBlockState(ropePos);
                    if (!AllBlocks.ROPE.has(ropeState) && !AllBlocks.PULLEY_MAGNET.has(ropeState)) {
                        break;
                    }
                }
                this.offset = (float) (i - 1);
                if (!(this.offset >= (float) this.getExtensionRange()) || !(this.getSpeed() > 0.0F)) {
                    if (!(this.offset <= 0.0F) || !(this.getSpeed() < 0.0F)) {
                        if (!this.f_58857_.isClientSide && this.mirrorParent == null) {
                            this.needsContraption = false;
                            BlockPos anchor = this.f_58858_.below(Mth.floor(this.offset + 1.0F));
                            this.initialOffset = Mth.floor(this.offset);
                            PulleyContraption contraption = new PulleyContraption(this.initialOffset);
                            boolean canAssembleStructure = contraption.assemble(this.f_58857_, anchor);
                            if (canAssembleStructure) {
                                Direction movementDirection = this.getSpeed() > 0.0F ? Direction.DOWN : Direction.UP;
                                if (ContraptionCollider.isCollidingWithWorld(this.f_58857_, contraption, anchor.relative(movementDirection), movementDirection)) {
                                    canAssembleStructure = false;
                                }
                            }
                            if (!canAssembleStructure && this.getSpeed() > 0.0F) {
                                return;
                            }
                            this.removeRopes();
                            if (!contraption.getBlocks().isEmpty()) {
                                contraption.removeBlocksFromWorld(this.f_58857_, BlockPos.ZERO);
                                this.movedContraption = ControlledContraptionEntity.create(this.f_58857_, this, contraption);
                                this.movedContraption.setPos((double) anchor.m_123341_(), (double) anchor.m_123342_(), (double) anchor.m_123343_());
                                this.f_58857_.m_7967_(this.movedContraption);
                                this.forceMove = true;
                                this.needsContraption = true;
                                if (contraption.containsBlockBreakers()) {
                                    this.award(AllAdvancements.CONTRAPTION_ACTORS);
                                }
                                for (BlockPos pos : contraption.createColliders(this.f_58857_, Direction.UP)) {
                                    if (pos.m_123342_() == 0) {
                                        pos = pos.offset(anchor);
                                        if (this.f_58857_.getBlockEntity(new BlockPos(pos.m_123341_(), this.f_58858_.m_123342_(), pos.m_123343_())) instanceof PulleyBlockEntity pbe) {
                                            pbe.startMirroringOther(this.f_58858_);
                                        }
                                    }
                                }
                            }
                        }
                        if (this.mirrorParent != null) {
                            this.removeRopes();
                        }
                        this.clientOffsetDiff = 0.0F;
                        this.running = true;
                        this.sendData();
                    }
                }
            }
        }
    }

    private void removeRopes() {
        for (int i = (int) this.offset; i > 0; i--) {
            BlockPos offset = this.f_58858_.below(i);
            BlockState oldState = this.f_58857_.getBlockState(offset);
            this.f_58857_.setBlock(offset, oldState.m_60819_().createLegacyBlock(), 66);
        }
    }

    @Override
    public void disassemble() {
        if (this.running || this.movedContraption != null || this.mirrorParent != null) {
            this.offset = (float) this.getGridOffset(this.offset);
            if (this.movedContraption != null) {
                this.resetContraptionToOffset();
            }
            if (!this.f_58857_.isClientSide) {
                if (this.shouldCreateRopes()) {
                    if (this.offset > 0.0F) {
                        BlockPos magnetPos = this.f_58858_.below((int) this.offset);
                        FluidState ifluidstate = this.f_58857_.getFluidState(magnetPos);
                        this.f_58857_.m_46961_(magnetPos, this.f_58857_.getBlockState(magnetPos).m_60812_(this.f_58857_, magnetPos).isEmpty());
                        this.f_58857_.setBlock(magnetPos, (BlockState) AllBlocks.PULLEY_MAGNET.getDefaultState().m_61124_(BlockStateProperties.WATERLOGGED, ifluidstate.getType() == Fluids.WATER), 66);
                    }
                    boolean[] waterlog = new boolean[(int) this.offset];
                    for (int i = 1; i <= (int) this.offset - 1; i++) {
                        BlockPos ropePos = this.f_58858_.below(i);
                        FluidState ifluidstate = this.f_58857_.getFluidState(ropePos);
                        waterlog[i] = ifluidstate.getType() == Fluids.WATER;
                        this.f_58857_.m_46961_(ropePos, this.f_58857_.getBlockState(ropePos).m_60812_(this.f_58857_, ropePos).isEmpty());
                    }
                    for (int i = 1; i <= (int) this.offset - 1; i++) {
                        this.f_58857_.setBlock(this.f_58858_.below(i), (BlockState) AllBlocks.ROPE.getDefaultState().m_61124_(BlockStateProperties.WATERLOGGED, waterlog[i]), 66);
                    }
                }
                if (this.movedContraption != null && this.mirrorParent == null) {
                    this.movedContraption.disassemble();
                }
                this.notifyMirrorsOfDisassembly();
            }
            if (this.movedContraption != null) {
                this.movedContraption.m_146870_();
            }
            this.movedContraption = null;
            this.initialOffset = 0;
            this.running = false;
            this.sendData();
        }
    }

    protected boolean shouldCreateRopes() {
        return !this.f_58859_;
    }

    @Override
    protected Vec3 toPosition(float offset) {
        if (this.movedContraption.getContraption() instanceof PulleyContraption) {
            PulleyContraption contraption = (PulleyContraption) this.movedContraption.getContraption();
            return Vec3.atLowerCornerOf(contraption.anchor).add(0.0, (double) ((float) contraption.getInitialOffset() - offset), 0.0);
        } else {
            return Vec3.ZERO;
        }
    }

    @Override
    protected void visitNewPosition() {
        super.visitNewPosition();
        if (!this.f_58857_.isClientSide) {
            if (this.movedContraption == null) {
                if (!(this.getSpeed() <= 0.0F)) {
                    BlockPos posBelow = this.f_58858_.below((int) (this.offset + this.getMovementSpeed()) + 1);
                    BlockState state = this.f_58857_.getBlockState(posBelow);
                    if (BlockMovementChecks.isMovementNecessary(state, this.f_58857_, posBelow)) {
                        if (!BlockMovementChecks.isBrittle(state)) {
                            this.disassemble();
                            this.assembleNextTick = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.initialOffset = compound.getInt("InitialOffset");
        this.needsContraption = compound.getBoolean("NeedsContraption");
        super.read(compound, clientPacket);
        BlockPos prevMirrorParent = this.mirrorParent;
        this.mirrorParent = null;
        this.mirrorChildren = null;
        if (compound.contains("MirrorParent")) {
            this.mirrorParent = NbtUtils.readBlockPos(compound.getCompound("MirrorParent"));
            this.offset = 0.0F;
            if (prevMirrorParent == null || !prevMirrorParent.equals(this.mirrorParent)) {
                this.sharedMirrorContraption = null;
            }
        }
        if (compound.contains("MirrorChildren")) {
            this.mirrorChildren = NBTHelper.readCompoundList(compound.getList("MirrorChildren", 10), NbtUtils::m_129239_);
        }
        if (this.mirrorParent == null) {
            this.sharedMirrorContraption = null;
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("InitialOffset", this.initialOffset);
        super.write(compound, clientPacket);
        if (this.mirrorParent != null) {
            compound.put("MirrorParent", NbtUtils.writeBlockPos(this.mirrorParent));
        }
        if (this.mirrorChildren != null) {
            compound.put("MirrorChildren", NBTHelper.writeCompoundList(this.mirrorChildren, NbtUtils::m_129224_));
        }
    }

    public void startMirroringOther(BlockPos parent) {
        if (!parent.equals(this.f_58858_)) {
            if (this.f_58857_.getBlockEntity(parent) instanceof PulleyBlockEntity pbe) {
                if (pbe.m_58903_() == this.m_58903_()) {
                    if (pbe.mirrorChildren == null) {
                        pbe.mirrorChildren = new ArrayList();
                    }
                    pbe.mirrorChildren.add(this.f_58858_);
                    pbe.notifyUpdate();
                    this.mirrorParent = parent;
                    try {
                        this.assemble();
                    } catch (AssemblyException var4) {
                    }
                    this.notifyUpdate();
                }
            }
        }
    }

    public void notifyMirrorsOfDisassembly() {
        if (this.mirrorChildren != null) {
            for (BlockPos blockPos : this.mirrorChildren) {
                if (this.f_58857_.getBlockEntity(blockPos) instanceof PulleyBlockEntity pbe) {
                    pbe.offset = this.offset;
                    pbe.disassemble();
                    pbe.mirrorParent = null;
                    pbe.notifyUpdate();
                }
            }
            this.mirrorChildren.clear();
            this.notifyUpdate();
        }
    }

    @Override
    protected int getExtensionRange() {
        return Math.max(0, Math.min(AllConfigs.server().kinetics.maxRopeLength.get(), this.f_58858_.m_123342_() - 1 - this.f_58857_.m_141937_()));
    }

    @Override
    protected int getInitialOffset() {
        return this.initialOffset;
    }

    @Override
    protected Vec3 toMotionVector(float speed) {
        return new Vec3(0.0, (double) (-speed), 0.0);
    }

    @Override
    protected ValueBoxTransform getMovementModeSlot() {
        return new CenteredSideValueBoxTransform((state, d) -> d == Direction.UP);
    }

    @Override
    public float getInterpolatedOffset(float partialTicks) {
        if (this.isVirtual()) {
            return Mth.lerp(partialTicks, this.prevAnimatedOffset, this.offset);
        } else {
            boolean moving = this.running && (this.movedContraption == null || !this.movedContraption.isStalled());
            return super.getInterpolatedOffset(moving ? partialTicks : 0.5F);
        }
    }

    public void animateOffset(float forcedOffset) {
        this.offset = forcedOffset;
    }

    @Override
    public float getPercent() {
        int distance = this.f_58858_.m_123342_() - this.f_58857_.m_141937_();
        return distance <= 0 ? 100.0F : 100.0F * this.getInterpolatedOffset(0.5F) / (float) distance;
    }

    public BlockPos getMirrorParent() {
        return this.mirrorParent;
    }
}