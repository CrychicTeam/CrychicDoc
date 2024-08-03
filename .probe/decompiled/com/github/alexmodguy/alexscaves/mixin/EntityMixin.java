package com.github.alexmodguy.alexscaves.mixin;

import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import com.github.alexmodguy.alexscaves.server.entity.util.MagneticEntityAccessor;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Entity.class })
public abstract class EntityMixin implements MagneticEntityAccessor {

    @Shadow
    @Final
    protected SynchedEntityData entityData;

    @Shadow
    private Level level;

    @Shadow
    private Vec3 position;

    @Shadow
    private EntityDimensions dimensions;

    @Shadow
    protected boolean wasTouchingWater;

    private static final EntityDataAccessor<Float> MAGNET_DELTA_X = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> MAGNET_DELTA_Y = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> MAGNET_DELTA_Z = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Direction> MAGNET_ATTACHMENT_DIRECTION = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.DIRECTION);

    private float attachChangeProgress = 0.0F;

    private float prevAttachChangeProgress = 0.0F;

    private Direction prevAttachDir = Direction.DOWN;

    private int jumpFlipCooldown = 0;

    private BlockPos lastStepPos;

    @Shadow
    protected abstract void playStepSound(BlockPos var1, BlockState var2);

    @Shadow
    public abstract void tick();

    @Shadow
    public abstract void refreshDimensions();

    @Shadow
    public abstract AABB getBoundingBox();

    @Shadow
    public abstract Level level();

    @Shadow
    public abstract boolean onGround();

    @Shadow
    public abstract double getY();

    @Inject(at = { @At("TAIL") }, remap = true, method = { "Lnet/minecraft/world/entity/Entity;<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V" })
    private void citadel_registerData(CallbackInfo ci) {
        this.entityData.define(MAGNET_DELTA_X, 0.0F);
        this.entityData.define(MAGNET_DELTA_Y, 0.0F);
        this.entityData.define(MAGNET_DELTA_Z, 0.0F);
        this.entityData.define(MAGNET_ATTACHMENT_DIRECTION, Direction.DOWN);
    }

    @Inject(method = { "Lnet/minecraft/world/entity/Entity;tick()V" }, remap = true, at = { @At("TAIL") })
    public void ac_tick(CallbackInfo ci) {
        Entity thisEntity = (Entity) this;
        this.prevAttachChangeProgress = this.attachChangeProgress;
        if (this.prevAttachDir != this.getMagneticAttachmentFace()) {
            if (this.attachChangeProgress < 1.0F) {
                this.attachChangeProgress += 0.1F;
            } else if (this.attachChangeProgress >= 1.0F) {
                this.prevAttachDir = this.getMagneticAttachmentFace();
            }
        } else {
            this.attachChangeProgress = 1.0F;
        }
        if (MagnetUtil.isPulledByMagnets(thisEntity)) {
            MagnetUtil.tickMagnetism(thisEntity);
            if (this.jumpFlipCooldown > 0) {
                this.jumpFlipCooldown--;
            }
        } else if (this.getMagneticAttachmentFace() != Direction.DOWN) {
            this.setMagneticAttachmentFace(Direction.DOWN);
            this.refreshDimensions();
        }
    }

    @Inject(method = { "Lnet/minecraft/world/entity/Entity;onSyncedDataUpdated(Lnet/minecraft/network/syncher/EntityDataAccessor;)V" }, remap = true, at = { @At("TAIL") })
    public void ac_onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor, CallbackInfo ci) {
        if (MAGNET_ATTACHMENT_DIRECTION.equals(entityDataAccessor)) {
            this.prevAttachChangeProgress = 0.0F;
            this.attachChangeProgress = 0.0F;
        }
    }

    @Inject(method = { "Lnet/minecraft/world/entity/Entity;getEyePosition()Lnet/minecraft/world/phys/Vec3;" }, remap = true, cancellable = true, at = { @At("HEAD") })
    public void ac_getEyePosition(CallbackInfoReturnable<Vec3> cir) {
        if (this.getMagneticAttachmentFace() != Direction.DOWN) {
            cir.setReturnValue(MagnetUtil.getEyePositionForAttachment((Entity) this, this.getMagneticAttachmentFace(), 1.0F));
        }
    }

    @Inject(method = { "Lnet/minecraft/world/entity/Entity;getEyePosition(F)Lnet/minecraft/world/phys/Vec3;" }, remap = true, cancellable = true, at = { @At("HEAD") })
    public void ac_getEyePosition_lerp(float partialTick, CallbackInfoReturnable<Vec3> cir) {
        if (this.getMagneticAttachmentFace() != Direction.DOWN && this.getMagneticAttachmentFace() != Direction.UP) {
            cir.setReturnValue(MagnetUtil.getEyePositionForAttachment((Entity) this, this.getMagneticAttachmentFace(), partialTick));
        }
    }

    @Inject(method = { "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;" }, remap = true, cancellable = true, at = { @At("HEAD") })
    public void ac_collide(Vec3 deltaIn, CallbackInfoReturnable<Vec3> cir) {
        AABB aabb = this.getBoundingBox();
        Entity thisEntity = (Entity) this;
        List<VoxelShape> list;
        if (this.getY() > (double) (this.level().m_141937_() - 200)) {
            list = this.level().m_183134_(thisEntity, aabb.expandTowards(deltaIn));
            List<VoxelShape> list2 = MagnetUtil.getMovingBlockCollisions(thisEntity, aabb);
            list = ImmutableList.builder().addAll(list).addAll(list2).build();
        } else {
            list = List.of();
        }
        Vec3 vec3 = deltaIn.lengthSqr() == 0.0 ? deltaIn : Entity.collideBoundingBox(thisEntity, deltaIn, aabb, this.level(), list);
        boolean flag = deltaIn.x != vec3.x;
        boolean flag1 = deltaIn.y != vec3.y;
        boolean flag2 = deltaIn.z != vec3.z;
        boolean flag3 = this.onGround() || flag1 && deltaIn.y < 0.0;
        float stepHeight = thisEntity.getStepHeight();
        if (stepHeight > 0.0F && flag3 && (flag || flag2)) {
            Vec3 vec31 = Entity.collideBoundingBox(thisEntity, new Vec3(deltaIn.x, (double) stepHeight, deltaIn.z), aabb, this.level, list);
            Vec3 vec32 = Entity.collideBoundingBox(thisEntity, new Vec3(0.0, (double) stepHeight, 0.0), aabb.expandTowards(deltaIn.x, 0.0, deltaIn.z), this.level, list);
            if (vec32.y < (double) stepHeight) {
                Vec3 vec33 = Entity.collideBoundingBox(thisEntity, new Vec3(deltaIn.x, 0.0, deltaIn.z), aabb.move(vec32), this.level(), list).add(vec32);
                if (vec33.horizontalDistanceSqr() > vec31.horizontalDistanceSqr()) {
                    vec31 = vec33;
                }
            }
            if (vec31.horizontalDistanceSqr() > vec3.horizontalDistanceSqr()) {
                cir.setReturnValue(vec31.add(Entity.collideBoundingBox(thisEntity, new Vec3(0.0, -vec31.y + deltaIn.y, 0.0), aabb.move(vec31), this.level(), list)));
                return;
            }
        }
        cir.setReturnValue(vec3);
    }

    @Inject(method = { "Lnet/minecraft/world/entity/Entity;turn(DD)V" }, remap = true, cancellable = true, at = { @At("HEAD") })
    public void ac_turn(double yBy, double xBy, CallbackInfo ci) {
        if (this.getMagneticAttachmentFace() != Direction.DOWN) {
            ci.cancel();
            MagnetUtil.turnEntityOnMagnet((Entity) this, xBy, yBy, this.getMagneticAttachmentFace());
        }
    }

    @Inject(method = { "Lnet/minecraft/world/entity/Entity;makeBoundingBox()Lnet/minecraft/world/phys/AABB;" }, remap = true, cancellable = true, at = { @At("HEAD") })
    public void ac_makeBoundingBox(CallbackInfoReturnable<AABB> cir) {
        if (this.entityData.isDirty() && this.getMagneticAttachmentFace() != Direction.DOWN) {
            cir.setReturnValue(MagnetUtil.rotateBoundingBox(this.dimensions, this.getMagneticAttachmentFace(), this.position));
        }
    }

    @Inject(method = { "Lnet/minecraft/world/entity/Entity;isInWater()Z" }, remap = true, cancellable = true, at = { @At("HEAD") })
    public void ac_isInWater(CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof LivingEntity living && living.getActiveEffects() != null && living.hasEffect(ACEffectRegistry.BUBBLED.get()) && (living.canBreatheUnderwater() || living.getMobType() == MobType.WATER) && !living.m_6095_().is(ACTagRegistry.RESISTS_BUBBLED)) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public float getMagneticDeltaX() {
        return this.entityData.hasItem(MAGNET_DELTA_X) ? this.entityData.get(MAGNET_DELTA_X) : 0.0F;
    }

    @Override
    public float getMagneticDeltaY() {
        return this.entityData.hasItem(MAGNET_DELTA_Y) ? this.entityData.get(MAGNET_DELTA_Y) : 0.0F;
    }

    @Override
    public float getMagneticDeltaZ() {
        return this.entityData.hasItem(MAGNET_DELTA_Z) ? this.entityData.get(MAGNET_DELTA_Z) : 0.0F;
    }

    @Override
    public Direction getMagneticAttachmentFace() {
        return this.entityData.hasItem(MAGNET_ATTACHMENT_DIRECTION) ? this.entityData.get(MAGNET_ATTACHMENT_DIRECTION) : Direction.DOWN;
    }

    @Override
    public Direction getPrevMagneticAttachmentFace() {
        return this.prevAttachDir;
    }

    @Override
    public float getAttachmentProgress(float partialTicks) {
        return this.prevAttachChangeProgress + (this.attachChangeProgress - this.prevAttachChangeProgress) * partialTicks;
    }

    @Override
    public void setMagneticDeltaX(float f) {
        if (this.entityData.hasItem(MAGNET_DELTA_X)) {
            this.entityData.set(MAGNET_DELTA_X, f);
        }
    }

    @Override
    public void setMagneticDeltaY(float f) {
        if (this.entityData.hasItem(MAGNET_DELTA_Y)) {
            this.entityData.set(MAGNET_DELTA_Y, f);
        }
    }

    @Override
    public void setMagneticDeltaZ(float f) {
        if (this.entityData.hasItem(MAGNET_DELTA_Z)) {
            this.entityData.set(MAGNET_DELTA_Z, f);
        }
    }

    @Override
    public void setMagneticAttachmentFace(Direction dir) {
        if (this.entityData.hasItem(MAGNET_ATTACHMENT_DIRECTION)) {
            this.entityData.set(MAGNET_ATTACHMENT_DIRECTION, dir);
        }
    }

    @Override
    public void postMagnetJump() {
        this.jumpFlipCooldown = 20;
    }

    @Override
    public boolean canChangeDirection() {
        return this.jumpFlipCooldown <= 0 && this.getAttachmentProgress(1.0F) == 1.0F;
    }

    @Override
    public void stepOnMagnetBlock(BlockPos pos) {
        if (this.lastStepPos == null || this.lastStepPos.m_123331_(pos) > 2.0) {
            this.lastStepPos = pos;
            this.playStepSound(pos, this.level.getBlockState(pos));
        }
    }
}