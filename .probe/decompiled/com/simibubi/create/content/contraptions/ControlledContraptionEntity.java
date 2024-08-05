package com.simibubi.create.content.contraptions;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.content.contraptions.bearing.BearingContraption;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ControlledContraptionEntity extends AbstractContraptionEntity {

    protected BlockPos controllerPos;

    protected Direction.Axis rotationAxis;

    protected float prevAngle;

    protected float angle;

    protected float angleDelta;

    public ControlledContraptionEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    public static ControlledContraptionEntity create(Level world, IControlContraption controller, Contraption contraption) {
        ControlledContraptionEntity entity = new ControlledContraptionEntity((EntityType<?>) AllEntityTypes.CONTROLLED_CONTRAPTION.get(), world);
        entity.controllerPos = controller.getBlockPosition();
        entity.setContraption(contraption);
        return entity;
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        if (this.m_9236_().isClientSide()) {
            for (Entity entity : this.m_20197_()) {
                this.m_7332_(entity);
            }
        }
    }

    @Override
    public Vec3 getContactPointMotion(Vec3 globalContactPoint) {
        return this.contraption instanceof TranslatingContraption ? this.m_20184_() : super.getContactPointMotion(globalContactPoint);
    }

    @Override
    protected void setContraption(Contraption contraption) {
        super.setContraption(contraption);
        if (contraption instanceof BearingContraption) {
            this.rotationAxis = ((BearingContraption) contraption).getFacing().getAxis();
        }
    }

    @Override
    protected void readAdditional(CompoundTag compound, boolean spawnPacket) {
        super.readAdditional(compound, spawnPacket);
        if (compound.contains("Controller")) {
            this.controllerPos = NbtUtils.readBlockPos(compound.getCompound("Controller"));
        } else {
            this.controllerPos = NbtUtils.readBlockPos(compound.getCompound("ControllerRelative")).offset(this.m_20183_());
        }
        if (compound.contains("Axis")) {
            this.rotationAxis = NBTHelper.readEnum(compound, "Axis", Direction.Axis.class);
        }
        this.angle = compound.getFloat("Angle");
    }

    @Override
    protected void writeAdditional(CompoundTag compound, boolean spawnPacket) {
        super.writeAdditional(compound, spawnPacket);
        compound.put("ControllerRelative", NbtUtils.writeBlockPos(this.controllerPos.subtract(this.m_20183_())));
        if (this.rotationAxis != null) {
            NBTHelper.writeEnum(compound, "Axis", this.rotationAxis);
        }
        compound.putFloat("Angle", this.angle);
    }

    @Override
    public AbstractContraptionEntity.ContraptionRotationState getRotationState() {
        AbstractContraptionEntity.ContraptionRotationState crs = new AbstractContraptionEntity.ContraptionRotationState();
        if (this.rotationAxis == Direction.Axis.X) {
            crs.xRotation = this.angle;
        }
        if (this.rotationAxis == Direction.Axis.Y) {
            crs.yRotation = this.angle;
        }
        if (this.rotationAxis == Direction.Axis.Z) {
            crs.zRotation = this.angle;
        }
        return crs;
    }

    @Override
    public Vec3 applyRotation(Vec3 localPos, float partialTicks) {
        return VecHelper.rotate(localPos, (double) this.getAngle(partialTicks), this.rotationAxis);
    }

    @Override
    public Vec3 reverseRotation(Vec3 localPos, float partialTicks) {
        return VecHelper.rotate(localPos, (double) (-this.getAngle(partialTicks)), this.rotationAxis);
    }

    public void setAngle(float angle) {
        this.angle = angle;
        if (this.m_9236_().isClientSide()) {
            for (Entity entity : this.m_20197_()) {
                this.m_7332_(entity);
            }
        }
    }

    public float getAngle(float partialTicks) {
        return partialTicks == 1.0F ? this.angle : AngleHelper.angleLerp((double) partialTicks, (double) this.prevAngle, (double) this.angle);
    }

    public void setRotationAxis(Direction.Axis rotationAxis) {
        this.rotationAxis = rotationAxis;
    }

    public Direction.Axis getRotationAxis() {
        return this.rotationAxis;
    }

    @Override
    public void teleportTo(double p_70634_1_, double p_70634_3_, double p_70634_5_) {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double x, double y, double z, float yw, float pt, int inc, boolean t) {
    }

    @Override
    protected void tickContraption() {
        this.angleDelta = this.angle - this.prevAngle;
        this.prevAngle = this.angle;
        this.tickActors();
        if (this.controllerPos != null) {
            if (this.m_9236_().isLoaded(this.controllerPos)) {
                IControlContraption controller = this.getController();
                if (controller == null) {
                    this.m_146870_();
                } else {
                    if (!controller.isAttachedTo(this)) {
                        controller.attach(this);
                        if (this.m_9236_().isClientSide) {
                            this.setPos(this.m_20185_(), this.m_20186_(), this.m_20189_());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected boolean shouldActorTrigger(MovementContext context, StructureTemplate.StructureBlockInfo blockInfo, MovementBehaviour actor, Vec3 actorPosition, BlockPos gridPosition) {
        if (super.shouldActorTrigger(context, blockInfo, actor, actorPosition, gridPosition)) {
            return true;
        } else if (!(this.contraption instanceof BearingContraption bc)) {
            return false;
        } else {
            Direction facing = bc.getFacing();
            Vec3 activeAreaOffset = actor.getActiveAreaOffset(context);
            if (!activeAreaOffset.multiply(VecHelper.axisAlingedPlaneOf(Vec3.atLowerCornerOf(facing.getNormal()))).equals(Vec3.ZERO)) {
                return false;
            } else if (!VecHelper.onSameAxis(blockInfo.pos(), BlockPos.ZERO, facing.getAxis())) {
                return false;
            } else {
                context.motion = Vec3.atLowerCornerOf(facing.getNormal()).scale((double) this.angleDelta / 360.0);
                context.relativeMotion = context.motion;
                int timer = context.data.getInt("StationaryTimer");
                if (timer > 0) {
                    context.data.putInt("StationaryTimer", timer - 1);
                    return false;
                } else {
                    context.data.putInt("StationaryTimer", 20);
                    return true;
                }
            }
        }
    }

    protected IControlContraption getController() {
        if (this.controllerPos == null) {
            return null;
        } else if (!this.m_9236_().isLoaded(this.controllerPos)) {
            return null;
        } else {
            BlockEntity be = this.m_9236_().getBlockEntity(this.controllerPos);
            return !(be instanceof IControlContraption) ? null : (IControlContraption) be;
        }
    }

    @Override
    protected StructureTransform makeStructureTransform() {
        BlockPos offset = BlockPos.containing(this.getAnchorVec().add(0.5, 0.5, 0.5));
        float xRot = this.rotationAxis == Direction.Axis.X ? this.angle : 0.0F;
        float yRot = this.rotationAxis == Direction.Axis.Y ? this.angle : 0.0F;
        float zRot = this.rotationAxis == Direction.Axis.Z ? this.angle : 0.0F;
        return new StructureTransform(offset, xRot, yRot, zRot);
    }

    @Override
    protected void onContraptionStalled() {
        IControlContraption controller = this.getController();
        if (controller != null) {
            controller.onStall();
        }
        super.onContraptionStalled();
    }

    @Override
    protected float getStalledAngle() {
        return this.angle;
    }

    @Override
    protected void handleStallInformation(double x, double y, double z, float angle) {
        this.m_20343_(x, y, z);
        this.angle = this.prevAngle = angle;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void applyLocalTransforms(PoseStack matrixStack, float partialTicks) {
        float angle = this.getAngle(partialTicks);
        Direction.Axis axis = this.getRotationAxis();
        ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(matrixStack).nudge(this.m_19879_())).centre()).rotate((double) angle, axis)).unCentre();
    }
}