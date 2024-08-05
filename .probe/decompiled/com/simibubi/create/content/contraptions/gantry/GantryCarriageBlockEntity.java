package com.simibubi.create.content.contraptions.gantry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionCollider;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlockEntity;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencerInstructions;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GantryCarriageBlockEntity extends KineticBlockEntity implements IDisplayAssemblyExceptions {

    boolean assembleNextTick;

    protected AssemblyException lastException;

    public GantryCarriageBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.CONTRAPTION_ACTORS });
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
    }

    public void checkValidGantryShaft() {
        if (this.shouldAssemble()) {
            this.queueAssembly();
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!this.m_58900_().m_60710_(this.f_58857_, this.f_58858_)) {
            this.f_58857_.m_46961_(this.f_58858_, true);
        }
    }

    public void queueAssembly() {
        this.assembleNextTick = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_58857_.isClientSide) {
            if (this.assembleNextTick) {
                this.tryAssemble();
                this.assembleNextTick = false;
            }
        }
    }

    @Override
    public AssemblyException getLastAssemblyException() {
        return this.lastException;
    }

    private void tryAssemble() {
        BlockState blockState = this.m_58900_();
        if (blockState.m_60734_() instanceof GantryCarriageBlock) {
            Direction direction = (Direction) blockState.m_61143_(GantryCarriageBlock.FACING);
            GantryContraption contraption = new GantryContraption(direction);
            if (this.f_58857_.getBlockEntity(this.f_58858_.relative(direction.getOpposite())) instanceof GantryShaftBlockEntity shaftBE) {
                BlockState shaftState = shaftBE.m_58900_();
                if (AllBlocks.GANTRY_SHAFT.has(shaftState)) {
                    float pinionMovementSpeed = shaftBE.getPinionMovementSpeed();
                    Direction shaftOrientation = (Direction) shaftState.m_61143_(GantryShaftBlock.FACING);
                    Direction movementDirection = shaftOrientation;
                    if (pinionMovementSpeed < 0.0F) {
                        movementDirection = shaftOrientation.getOpposite();
                    }
                    try {
                        this.lastException = null;
                        if (!contraption.assemble(this.f_58857_, this.f_58858_)) {
                            return;
                        }
                        this.sendData();
                    } catch (AssemblyException var12) {
                        this.lastException = var12;
                        this.sendData();
                        return;
                    }
                    if (!ContraptionCollider.isCollidingWithWorld(this.f_58857_, contraption, this.f_58858_.relative(movementDirection), movementDirection)) {
                        if (contraption.containsBlockBreakers()) {
                            this.award(AllAdvancements.CONTRAPTION_ACTORS);
                        }
                        contraption.removeBlocksFromWorld(this.f_58857_, BlockPos.ZERO);
                        GantryContraptionEntity movedContraption = GantryContraptionEntity.create(this.f_58857_, contraption, shaftOrientation);
                        BlockPos anchor = this.f_58858_;
                        movedContraption.m_6034_((double) anchor.m_123341_(), (double) anchor.m_123342_(), (double) anchor.m_123343_());
                        AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(this.f_58857_, this.f_58858_);
                        this.f_58857_.m_7967_(movedContraption);
                        if (shaftBE.sequenceContext != null && shaftBE.sequenceContext.instruction() == SequencerInstructions.TURN_DISTANCE) {
                            movedContraption.limitMovement(shaftBE.sequenceContext.getEffectiveValue((double) shaftBE.getTheoreticalSpeed()));
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        AssemblyException.write(compound, this.lastException);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.lastException = AssemblyException.read(compound);
        super.read(compound, clientPacket);
    }

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        float defaultModifier = super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
        if (connectedViaAxes) {
            return defaultModifier;
        } else if (!AllBlocks.GANTRY_SHAFT.has(stateTo)) {
            return defaultModifier;
        } else if (!(Boolean) stateTo.m_61143_(GantryShaftBlock.POWERED)) {
            return defaultModifier;
        } else {
            Direction direction = Direction.getNearest((float) diff.m_123341_(), (float) diff.m_123342_(), (float) diff.m_123343_());
            return stateFrom.m_61143_(GantryCarriageBlock.FACING) != direction.getOpposite() ? defaultModifier : getGantryPinionModifier((Direction) stateTo.m_61143_(GantryShaftBlock.FACING), (Direction) stateFrom.m_61143_(GantryCarriageBlock.FACING));
        }
    }

    public static float getGantryPinionModifier(Direction shaft, Direction pinionDirection) {
        Direction.Axis shaftAxis = shaft.getAxis();
        float directionModifier = (float) shaft.getAxisDirection().getStep();
        if (shaftAxis != Direction.Axis.Y || pinionDirection != Direction.NORTH && pinionDirection != Direction.EAST) {
            if (shaftAxis != Direction.Axis.X || pinionDirection != Direction.DOWN && pinionDirection != Direction.SOUTH) {
                return shaftAxis != Direction.Axis.Z || pinionDirection != Direction.UP && pinionDirection != Direction.WEST ? directionModifier : -directionModifier;
            } else {
                return -directionModifier;
            }
        } else {
            return -directionModifier;
        }
    }

    private boolean shouldAssemble() {
        BlockState blockState = this.m_58900_();
        if (!(blockState.m_60734_() instanceof GantryCarriageBlock)) {
            return false;
        } else {
            Direction facing = ((Direction) blockState.m_61143_(GantryCarriageBlock.FACING)).getOpposite();
            BlockState shaftState = this.f_58857_.getBlockState(this.f_58858_.relative(facing));
            if (!(shaftState.m_60734_() instanceof GantryShaftBlock)) {
                return false;
            } else if ((Boolean) shaftState.m_61143_(GantryShaftBlock.POWERED)) {
                return false;
            } else {
                BlockEntity be = this.f_58857_.getBlockEntity(this.f_58858_.relative(facing));
                return be instanceof GantryShaftBlockEntity && ((GantryShaftBlockEntity) be).canAssembleOn();
            }
        }
    }
}