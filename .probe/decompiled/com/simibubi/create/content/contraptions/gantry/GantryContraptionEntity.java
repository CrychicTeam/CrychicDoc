package com.simibubi.create.content.contraptions.gantry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionCollider;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlockEntity;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public class GantryContraptionEntity extends AbstractContraptionEntity {

    Direction movementAxis;

    double clientOffsetDiff;

    double axisMotion;

    public double sequencedOffsetLimit = -1.0;

    public GantryContraptionEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public static GantryContraptionEntity create(Level world, Contraption contraption, Direction movementAxis) {
        GantryContraptionEntity entity = new GantryContraptionEntity((EntityType<?>) AllEntityTypes.GANTRY_CONTRAPTION.get(), world);
        entity.setContraption(contraption);
        entity.movementAxis = movementAxis;
        return entity;
    }

    public void limitMovement(double maxOffset) {
        this.sequencedOffsetLimit = maxOffset;
    }

    @Override
    protected void tickContraption() {
        if (this.contraption instanceof GantryContraption) {
            double prevAxisMotion = this.axisMotion;
            if (this.m_9236_().isClientSide) {
                this.clientOffsetDiff *= 0.75;
                this.updateClientMotion();
            }
            this.checkPinionShaft();
            this.tickActors();
            Vec3 movementVec = this.m_20184_();
            if (ContraptionCollider.collideBlocks(this)) {
                if (!this.m_9236_().isClientSide) {
                    this.disassemble();
                }
            } else {
                if (!this.isStalled() && this.f_19797_ > 2) {
                    if (this.sequencedOffsetLimit >= 0.0) {
                        movementVec = VecHelper.clampComponentWise(movementVec, (float) this.sequencedOffsetLimit);
                    }
                    this.move(movementVec.x, movementVec.y, movementVec.z);
                    if (this.sequencedOffsetLimit > 0.0) {
                        this.sequencedOffsetLimit = Math.max(0.0, this.sequencedOffsetLimit - movementVec.length());
                    }
                }
                if (Math.signum(prevAxisMotion) != Math.signum(this.axisMotion) && prevAxisMotion != 0.0) {
                    this.contraption.stop(this.m_9236_());
                }
                if (!this.m_9236_().isClientSide && (prevAxisMotion != this.axisMotion || this.f_19797_ % 3 == 0)) {
                    this.sendPacket();
                }
            }
        }
    }

    @Override
    public void disassemble() {
        this.sequencedOffsetLimit = -1.0;
        super.disassemble();
    }

    protected void checkPinionShaft() {
        Direction facing = ((GantryContraption) this.contraption).getFacing();
        Vec3 currentPosition = this.getAnchorVec().add(0.5, 0.5, 0.5);
        BlockPos gantryShaftPos = BlockPos.containing(currentPosition).relative(facing.getOpposite());
        BlockEntity be = this.m_9236_().getBlockEntity(gantryShaftPos);
        if (be instanceof GantryShaftBlockEntity && AllBlocks.GANTRY_SHAFT.has(be.getBlockState())) {
            BlockState blockState = be.getBlockState();
            Direction direction = (Direction) blockState.m_61143_(GantryShaftBlock.FACING);
            GantryShaftBlockEntity gantryShaftBlockEntity = (GantryShaftBlockEntity) be;
            float pinionMovementSpeed = gantryShaftBlockEntity.getPinionMovementSpeed();
            if (!(Boolean) blockState.m_61143_(GantryShaftBlock.POWERED) && pinionMovementSpeed != 0.0F) {
                if (this.sequencedOffsetLimit >= 0.0) {
                    pinionMovementSpeed = (float) Mth.clamp((double) pinionMovementSpeed, -this.sequencedOffsetLimit, this.sequencedOffsetLimit);
                }
                Vec3 movementVec = Vec3.atLowerCornerOf(direction.getNormal()).scale((double) pinionMovementSpeed);
                Vec3 nextPosition = currentPosition.add(movementVec);
                double currentCoord = direction.getAxis().choose(currentPosition.x, currentPosition.y, currentPosition.z);
                double nextCoord = direction.getAxis().choose(nextPosition.x, nextPosition.y, nextPosition.z);
                if ((double) ((float) Mth.floor(currentCoord) + 0.5F) < nextCoord != pinionMovementSpeed * (float) direction.getAxisDirection().getStep() < 0.0F && !gantryShaftBlockEntity.canAssembleOn()) {
                    this.setContraptionMotion(Vec3.ZERO);
                    if (!this.m_9236_().isClientSide) {
                        this.disassemble();
                    }
                } else if (!this.m_9236_().isClientSide) {
                    this.axisMotion = (double) pinionMovementSpeed;
                    this.setContraptionMotion(movementVec);
                }
            } else {
                this.setContraptionMotion(Vec3.ZERO);
                if (!this.m_9236_().isClientSide) {
                    this.disassemble();
                }
            }
        } else {
            if (!this.m_9236_().isClientSide) {
                this.setContraptionMotion(Vec3.ZERO);
                this.disassemble();
            }
        }
    }

    @Override
    protected void writeAdditional(CompoundTag compound, boolean spawnPacket) {
        NBTHelper.writeEnum(compound, "GantryAxis", this.movementAxis);
        if (this.sequencedOffsetLimit >= 0.0) {
            compound.putDouble("SequencedOffsetLimit", this.sequencedOffsetLimit);
        }
        super.writeAdditional(compound, spawnPacket);
    }

    @Override
    protected void readAdditional(CompoundTag compound, boolean spawnData) {
        this.movementAxis = NBTHelper.readEnum(compound, "GantryAxis", Direction.class);
        this.sequencedOffsetLimit = compound.contains("SequencedOffsetLimit") ? compound.getDouble("SequencedOffsetLimit") : -1.0;
        super.readAdditional(compound, spawnData);
    }

    @Override
    public Vec3 applyRotation(Vec3 localPos, float partialTicks) {
        return localPos;
    }

    @Override
    public Vec3 reverseRotation(Vec3 localPos, float partialTicks) {
        return localPos;
    }

    @Override
    protected StructureTransform makeStructureTransform() {
        return new StructureTransform(BlockPos.containing(this.getAnchorVec().add(0.5, 0.5, 0.5)), 0.0F, 0.0F, 0.0F);
    }

    @Override
    protected float getStalledAngle() {
        return 0.0F;
    }

    @Override
    public void teleportTo(double p_70634_1_, double p_70634_3_, double p_70634_5_) {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double x, double y, double z, float yw, float pt, int inc, boolean t) {
    }

    @Override
    protected void handleStallInformation(double x, double y, double z, float angle) {
        this.m_20343_(x, y, z);
        this.clientOffsetDiff = 0.0;
    }

    @Override
    public AbstractContraptionEntity.ContraptionRotationState getRotationState() {
        return AbstractContraptionEntity.ContraptionRotationState.NONE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void applyLocalTransforms(PoseStack matrixStack, float partialTicks) {
    }

    public void updateClientMotion() {
        float modifier = (float) this.movementAxis.getAxisDirection().getStep();
        Vec3 motion = Vec3.atLowerCornerOf(this.movementAxis.getNormal()).scale((this.axisMotion + this.clientOffsetDiff * (double) modifier / 2.0) * (double) ServerSpeedProvider.get());
        if (this.sequencedOffsetLimit >= 0.0) {
            motion = VecHelper.clampComponentWise(motion, (float) this.sequencedOffsetLimit);
        }
        this.setContraptionMotion(motion);
    }

    public double getAxisCoord() {
        Vec3 anchorVec = this.getAnchorVec();
        return this.movementAxis.getAxis().choose(anchorVec.x, anchorVec.y, anchorVec.z);
    }

    public void sendPacket() {
        AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new GantryContraptionUpdatePacket(this.m_19879_(), this.getAxisCoord(), this.axisMotion, this.sequencedOffsetLimit));
    }

    @OnlyIn(Dist.CLIENT)
    public static void handlePacket(GantryContraptionUpdatePacket packet) {
        if (Minecraft.getInstance().level.getEntity(packet.entityID) instanceof GantryContraptionEntity ce) {
            ce.axisMotion = packet.motion;
            ce.clientOffsetDiff = packet.coord - ce.getAxisCoord();
            ce.sequencedOffsetLimit = packet.sequenceLimit;
        }
    }
}