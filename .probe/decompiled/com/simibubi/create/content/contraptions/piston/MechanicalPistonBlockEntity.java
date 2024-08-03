package com.simibubi.create.content.contraptions.piston;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionCollider;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.DirectionalExtenderScrollOptionSlot;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class MechanicalPistonBlockEntity extends LinearActuatorBlockEntity {

    protected boolean hadCollisionWithOtherPiston;

    protected int extensionLength;

    public MechanicalPistonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.extensionLength = compound.getInt("ExtensionLength");
        super.read(compound, clientPacket);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.putInt("ExtensionLength", this.extensionLength);
        super.write(tag, clientPacket);
    }

    @Override
    public void assemble() throws AssemblyException {
        if (this.f_58857_.getBlockState(this.f_58858_).m_60734_() instanceof MechanicalPistonBlock) {
            Direction direction = (Direction) this.m_58900_().m_61143_(BlockStateProperties.FACING);
            PistonContraption contraption = new PistonContraption(direction, this.getMovementSpeed() < 0.0F);
            if (contraption.assemble(this.f_58857_, this.f_58858_)) {
                Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, direction.getAxis());
                Direction movementDirection = this.getSpeed() > 0.0F ^ direction.getAxis() != Direction.Axis.Z ? positive : positive.getOpposite();
                BlockPos anchor = contraption.anchor.relative(direction, contraption.initialExtensionProgress);
                if (!ContraptionCollider.isCollidingWithWorld(this.f_58857_, contraption, anchor.relative(movementDirection), movementDirection)) {
                    this.extensionLength = contraption.extensionLength;
                    float resultingOffset = (float) contraption.initialExtensionProgress + Math.signum(this.getMovementSpeed()) * 0.5F;
                    if (!(resultingOffset <= 0.0F) && !(resultingOffset >= (float) this.extensionLength)) {
                        this.running = true;
                        this.offset = (float) contraption.initialExtensionProgress;
                        this.sendData();
                        this.clientOffsetDiff = 0.0F;
                        BlockPos startPos = BlockPos.ZERO.relative(direction, contraption.initialExtensionProgress);
                        contraption.removeBlocksFromWorld(this.f_58857_, startPos);
                        this.movedContraption = ControlledContraptionEntity.create(this.m_58904_(), this, contraption);
                        this.resetContraptionToOffset();
                        this.forceMove = true;
                        this.f_58857_.m_7967_(this.movedContraption);
                        AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(this.f_58857_, this.f_58858_);
                        if (contraption.containsBlockBreakers()) {
                            this.award(AllAdvancements.CONTRAPTION_ACTORS);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void disassemble() {
        if (this.running || this.movedContraption != null) {
            if (!this.f_58859_) {
                this.m_58904_().setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(MechanicalPistonBlock.STATE, MechanicalPistonBlock.PistonState.EXTENDED), 19);
            }
            if (this.movedContraption != null) {
                this.resetContraptionToOffset();
                this.movedContraption.disassemble();
                AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(this.f_58857_, this.f_58858_);
            }
            this.running = false;
            this.movedContraption = null;
            this.sendData();
            if (this.f_58859_) {
                ((MechanicalPistonBlock) AllBlocks.MECHANICAL_PISTON.get()).playerWillDestroy(this.f_58857_, this.f_58858_, this.m_58900_(), null);
            }
        }
    }

    @Override
    protected void collided() {
        super.collided();
        if (!this.running && this.getMovementSpeed() > 0.0F) {
            this.assembleNextTick = true;
        }
    }

    @Override
    public float getMovementSpeed() {
        float movementSpeed = Mth.clamp(convertToLinear(this.getSpeed()), -0.49F, 0.49F);
        if (this.f_58857_.isClientSide) {
            movementSpeed *= ServerSpeedProvider.get();
        }
        Direction pistonDirection = (Direction) this.m_58900_().m_61143_(BlockStateProperties.FACING);
        int movementModifier = pistonDirection.getAxisDirection().getStep() * (pistonDirection.getAxis() == Direction.Axis.Z ? -1 : 1);
        movementSpeed = movementSpeed * (float) (-movementModifier) + this.clientOffsetDiff / 2.0F;
        int extensionRange = this.getExtensionRange();
        movementSpeed = Mth.clamp(movementSpeed, 0.0F - this.offset, (float) extensionRange - this.offset);
        if (this.sequencedOffsetLimit >= 0.0) {
            movementSpeed = (float) Mth.clamp((double) movementSpeed, -this.sequencedOffsetLimit, this.sequencedOffsetLimit);
        }
        return movementSpeed;
    }

    @Override
    protected int getExtensionRange() {
        return this.extensionLength;
    }

    @Override
    protected void visitNewPosition() {
    }

    @Override
    protected Vec3 toMotionVector(float speed) {
        Direction pistonDirection = (Direction) this.m_58900_().m_61143_(BlockStateProperties.FACING);
        return Vec3.atLowerCornerOf(pistonDirection.getNormal()).scale((double) speed);
    }

    @Override
    protected Vec3 toPosition(float offset) {
        Vec3 position = Vec3.atLowerCornerOf(((Direction) this.m_58900_().m_61143_(BlockStateProperties.FACING)).getNormal()).scale((double) offset);
        return position.add(Vec3.atLowerCornerOf(this.movedContraption.getContraption().anchor));
    }

    @Override
    protected ValueBoxTransform getMovementModeSlot() {
        return new DirectionalExtenderScrollOptionSlot((state, d) -> {
            Direction.Axis axis = d.getAxis();
            Direction.Axis extensionAxis = ((Direction) state.m_61143_(MechanicalPistonBlock.FACING)).getAxis();
            Direction.Axis shaftAxis = ((IRotate) state.m_60734_()).getRotationAxis(state);
            return extensionAxis != axis && shaftAxis != axis;
        });
    }

    @Override
    protected int getInitialOffset() {
        return this.movedContraption == null ? 0 : ((PistonContraption) this.movedContraption.getContraption()).initialExtensionProgress;
    }
}