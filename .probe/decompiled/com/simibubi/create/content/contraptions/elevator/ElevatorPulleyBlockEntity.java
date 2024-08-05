package com.simibubi.create.content.contraptions.elevator;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.IControlContraption;
import com.simibubi.create.content.contraptions.pulley.PulleyBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ElevatorPulleyBlockEntity extends PulleyBlockEntity {

    private float prevSpeed = 0.0F;

    private boolean arrived = true;

    private int clientOffsetTarget;

    private boolean initialOffsetReceived = false;

    public ElevatorPulleyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private int getTargetOffset() {
        if (this.f_58857_.isClientSide) {
            return this.clientOffsetTarget;
        } else if (this.movedContraption != null && this.movedContraption.getContraption() instanceof ElevatorContraption ec) {
            Integer target = ec.getCurrentTargetY(this.f_58857_);
            return target == null ? (int) this.offset : this.f_58858_.m_123342_() - target + ec.contactYOffset - 1;
        } else {
            return (int) this.offset;
        }
    }

    @Override
    public void attach(ControlledContraptionEntity contraption) {
        super.attach(contraption);
        if (this.offset >= 0.0F) {
            this.resetContraptionToOffset();
        }
        if (this.f_58857_.isClientSide) {
            AllPackets.getChannel().sendToServer(new ElevatorFloorListPacket.RequestFloorList(contraption));
        } else {
            if (contraption.getContraption() instanceof ElevatorContraption ec) {
                ElevatorColumn.getOrCreate(this.f_58857_, ec.getGlobalColumn()).setActive(true);
            }
        }
    }

    @Override
    public void tick() {
        boolean wasArrived = this.arrived;
        super.tick();
        if (this.movedContraption != null) {
            if (this.movedContraption.getContraption() instanceof ElevatorContraption ec) {
                if (this.f_58857_.isClientSide()) {
                    ec.setClientYTarget(this.f_58858_.m_123342_() - this.clientOffsetTarget + ec.contactYOffset - 1);
                }
                this.waitingForSpeedChange = false;
                ec.arrived = wasArrived;
                if (this.arrived) {
                    double y = this.movedContraption.m_20186_();
                    int targetLevel = Mth.floor(0.5 + y) + ec.contactYOffset;
                    Integer ecCurrentTargetY = ec.getCurrentTargetY(this.f_58857_);
                    if (ecCurrentTargetY != null) {
                        targetLevel = ecCurrentTargetY;
                    }
                    if (this.f_58857_.isClientSide()) {
                        targetLevel = ec.clientYTarget;
                    }
                    if (!wasArrived && !this.f_58857_.isClientSide()) {
                        this.triggerContact(ec, targetLevel - ec.contactYOffset);
                        AllSoundEvents.CONTRAPTION_DISASSEMBLE.play(this.f_58857_, null, this.f_58858_.below((int) this.offset), 0.75F, 0.8F);
                    }
                    double diff = (double) targetLevel - y - (double) ec.contactYOffset;
                    if (Math.abs(diff) > 0.0078125) {
                        diff *= 0.25;
                    }
                    this.movedContraption.m_146884_(this.movedContraption.m_20182_().add(0.0, diff, 0.0));
                }
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!this.f_58857_.isClientSide() && this.arrived) {
            if (this.movedContraption != null && this.movedContraption.m_6084_()) {
                if (this.movedContraption.getContraption() instanceof ElevatorContraption ec) {
                    if (this.getTargetOffset() == (int) this.offset) {
                        double y = this.movedContraption.m_20186_();
                        int targetLevel = Mth.floor(0.5 + y);
                        this.triggerContact(ec, targetLevel);
                    }
                }
            }
        }
    }

    private void triggerContact(ElevatorContraption ec, int targetLevel) {
        ElevatorColumn.ColumnCoords coords = ec.getGlobalColumn();
        ElevatorColumn column = ElevatorColumn.get(this.f_58857_, coords);
        if (column != null) {
            BlockPos contactPos = column.contactAt(targetLevel + ec.contactYOffset);
            if (this.f_58857_.isLoaded(contactPos)) {
                BlockState contactState = this.f_58857_.getBlockState(contactPos);
                if (AllBlocks.ELEVATOR_CONTACT.has(contactState)) {
                    if (!(Boolean) contactState.m_61143_(ElevatorContactBlock.POWERING)) {
                        ElevatorContactBlock ecb = (ElevatorContactBlock) AllBlocks.ELEVATOR_CONTACT.get();
                        ecb.withBlockEntityDo(this.f_58857_, contactPos, be -> be.activateBlock = true);
                        ecb.scheduleActivation(this.f_58857_, contactPos);
                    }
                }
            }
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (clientPacket) {
            compound.putInt("ClientTarget", this.clientOffsetTarget);
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (clientPacket) {
            this.clientOffsetTarget = compound.getInt("ClientTarget");
            if (!this.initialOffsetReceived) {
                this.offset = compound.getFloat("Offset");
                this.initialOffsetReceived = true;
                this.resetContraptionToOffset();
            }
        }
    }

    @Override
    public float getMovementSpeed() {
        int currentTarget = this.getTargetOffset();
        if (!this.f_58857_.isClientSide() && currentTarget != this.clientOffsetTarget) {
            this.clientOffsetTarget = currentTarget;
            this.sendData();
        }
        float diff = (float) currentTarget - this.offset;
        float movementSpeed = Mth.clamp(convertToLinear(this.getSpeed() * 2.0F), -1.99F, 1.99F);
        float rpmLimit = Math.abs(movementSpeed);
        float configacc = Mth.lerp(Math.abs(movementSpeed), 0.0075F, 0.0175F);
        float decelleration = (float) Math.sqrt((double) (2.0F * Math.abs(diff) * configacc));
        float speed = Mth.clamp(diff, -rpmLimit, rpmLimit);
        speed = Mth.clamp(speed, this.prevSpeed - configacc, this.prevSpeed + configacc);
        speed = Mth.clamp(speed, -decelleration, decelleration);
        this.arrived = Math.abs(diff) < 0.5F;
        if (speed > 9.765625E-4F && !this.f_58857_.isClientSide()) {
            this.m_6596_();
        }
        return this.prevSpeed = speed;
    }

    @Override
    protected boolean shouldCreateRopes() {
        return false;
    }

    @Override
    public void disassemble() {
        if (this.movedContraption != null && this.movedContraption.getContraption() instanceof ElevatorContraption ec) {
            ElevatorColumn column = ElevatorColumn.get(this.f_58857_, ec.getGlobalColumn());
            if (column != null) {
                column.setActive(false);
            }
        }
        super.disassemble();
        this.offset = -1.0F;
        this.sendData();
    }

    public void clicked() {
        if (this.isPassive() && this.f_58857_.getBlockEntity(this.mirrorParent) instanceof ElevatorPulleyBlockEntity parent) {
            parent.clicked();
        } else {
            if (this.running) {
                this.disassemble();
            } else {
                this.assembleNextTick = true;
            }
        }
    }

    @Override
    protected boolean moveAndCollideContraption() {
        if (this.arrived) {
            return false;
        } else {
            super.moveAndCollideContraption();
            return false;
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.CONTRAPTION_ACTORS });
    }

    @Override
    protected void assemble() throws AssemblyException {
        if (this.f_58857_.getBlockState(this.f_58858_).m_60734_() instanceof ElevatorPulleyBlock) {
            if (this.getSpeed() != 0.0F) {
                int maxLength = AllConfigs.server().kinetics.maxRopeLength.get();
                int i;
                for (i = 1; i <= maxLength; i++) {
                    BlockPos ropePos = this.f_58858_.below(i);
                    BlockState ropeState = this.f_58857_.getBlockState(ropePos);
                    if (!ropeState.m_60812_(this.f_58857_, ropePos).isEmpty() && !ropeState.m_247087_()) {
                        break;
                    }
                }
                this.offset = (float) (i - 1);
                this.forceMove = true;
                if (!this.f_58857_.isClientSide && this.mirrorParent == null) {
                    this.needsContraption = false;
                    BlockPos anchor = this.f_58858_.below(Mth.floor(this.offset + 1.0F));
                    this.offset = (float) Mth.floor(this.offset);
                    ElevatorContraption contraption = new ElevatorContraption((int) this.offset);
                    float offsetOnSucess = this.offset;
                    this.offset = 0.0F;
                    boolean canAssembleStructure = contraption.assemble(this.f_58857_, anchor);
                    if (!canAssembleStructure && this.getSpeed() > 0.0F) {
                        return;
                    }
                    if (!contraption.getBlocks().isEmpty()) {
                        this.offset = offsetOnSucess;
                        contraption.removeBlocksFromWorld(this.f_58857_, BlockPos.ZERO);
                        this.movedContraption = ControlledContraptionEntity.create(this.f_58857_, this, contraption);
                        this.movedContraption.setPos((double) anchor.m_123341_(), (double) anchor.m_123342_(), (double) anchor.m_123343_());
                        contraption.maxContactY = this.f_58858_.m_123342_() + contraption.contactYOffset - 1;
                        contraption.minContactY = contraption.maxContactY - maxLength;
                        this.f_58857_.m_7967_(this.movedContraption);
                        this.forceMove = true;
                        this.needsContraption = true;
                        if (contraption.containsBlockBreakers()) {
                            this.award(AllAdvancements.CONTRAPTION_ACTORS);
                        }
                        for (BlockPos pos : contraption.createColliders(this.f_58857_, Direction.UP)) {
                            if (pos.m_123342_() == 0) {
                                pos = pos.offset(anchor);
                                if (this.f_58857_.getBlockEntity(new BlockPos(pos.m_123341_(), this.f_58858_.m_123342_(), pos.m_123343_())) instanceof ElevatorPulleyBlockEntity pbe) {
                                    pbe.startMirroringOther(this.f_58858_);
                                }
                            }
                        }
                        ElevatorColumn column = ElevatorColumn.getOrCreate(this.f_58857_, contraption.getGlobalColumn());
                        int target = (int) ((float) (this.f_58858_.m_123342_() + contraption.contactYOffset - 1) - this.offset);
                        column.target(target);
                        column.gatherAll();
                        column.setActive(true);
                        column.markDirty();
                        contraption.broadcastFloorData(this.f_58857_, column.contactAt(target));
                        this.clientOffsetTarget = column.getTargetedYLevel();
                        this.arrived = true;
                    }
                }
                this.clientOffsetDiff = 0.0F;
                this.running = true;
                this.sendData();
            }
        }
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        this.m_6596_();
    }

    @Override
    protected IControlContraption.MovementMode getMovementMode() {
        return IControlContraption.MovementMode.MOVE_NEVER_PLACE;
    }
}