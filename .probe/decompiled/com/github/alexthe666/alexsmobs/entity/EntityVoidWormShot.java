package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntityVoidWormShot extends Entity {

    private UUID ownerUUID;

    private int ownerNetworkId;

    private boolean leftOwner;

    private static final EntityDataAccessor<Float> STOP_HOMING_PROGRESS = SynchedEntityData.defineId(EntityVoidWormShot.class, EntityDataSerializers.FLOAT);

    public float prevStopHomingProgress = 0.0F;

    public static final float HOME_FOR = 40.0F;

    public EntityVoidWormShot(EntityType p_i50162_1_, Level p_i50162_2_) {
        super(p_i50162_1_, p_i50162_2_);
    }

    public EntityVoidWormShot(Level worldIn, EntityVoidWorm p_i47273_2_) {
        this(AMEntityRegistry.VOID_WORM_SHOT.get(), worldIn);
        this.setShooter(p_i47273_2_);
        this.m_6034_(p_i47273_2_.m_20185_() - (double) (p_i47273_2_.m_20205_() + 1.0F) * 0.35 * (double) Mth.sin(p_i47273_2_.f_20883_ * (float) (Math.PI / 180.0)), p_i47273_2_.m_20186_() + 1.0, p_i47273_2_.m_20189_() + (double) (p_i47273_2_.m_20205_() + 1.0F) * 0.35 * (double) Mth.cos(p_i47273_2_.f_20883_ * (float) (Math.PI / 180.0)));
    }

    public EntityVoidWormShot(Level worldIn, LivingEntity p_i47273_2_, boolean right) {
        this(AMEntityRegistry.VOID_WORM_SHOT.get(), worldIn);
        this.setShooter(p_i47273_2_);
        float rot = p_i47273_2_.yHeadRot + (float) (right ? 60 : -60);
        this.m_6034_(p_i47273_2_.m_20185_() - (double) p_i47273_2_.m_20205_() * 0.9F * (double) Mth.sin(rot * (float) (Math.PI / 180.0)), p_i47273_2_.m_20186_() + 1.0, p_i47273_2_.m_20189_() + (double) p_i47273_2_.m_20205_() * 0.9 * (double) Mth.cos(rot * (float) (Math.PI / 180.0)));
    }

    @OnlyIn(Dist.CLIENT)
    public EntityVoidWormShot(Level worldIn, double x, double y, double z, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
        this(AMEntityRegistry.VOID_WORM_SHOT.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_20334_(p_i47274_8_, p_i47274_10_, p_i47274_12_);
    }

    public EntityVoidWormShot(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.VOID_WORM_SHOT.get(), world);
    }

    protected static float lerpRotation(float p_234614_0_, float p_234614_1_) {
        while (p_234614_1_ - p_234614_0_ < -180.0F) {
            p_234614_0_ -= 360.0F;
        }
        while (p_234614_1_ - p_234614_0_ >= 180.0F) {
            p_234614_0_ += 360.0F;
        }
        return Mth.lerp(0.2F, p_234614_0_, p_234614_1_);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        this.prevStopHomingProgress = this.getStopHomingProgress();
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        if (this.f_19797_ > 400) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (this.f_19797_ > 40) {
            Entity entity = this.getShooter();
            float stopHomingProgress = this.getStopHomingProgress();
            if (stopHomingProgress < 40.0F) {
                this.setStopHomingProgress(++stopHomingProgress);
            }
            float homeScale = 1.0F - stopHomingProgress / 40.0F;
            if (entity instanceof Mob && ((Mob) entity).getTarget() != null && homeScale > 0.0F) {
                LivingEntity target = ((Mob) entity).getTarget();
                if (target == null) {
                    this.m_6074_();
                }
                double d0 = target.m_20185_() - this.m_20185_();
                double d1 = target.m_20188_() - this.m_20186_();
                double d2 = target.m_20189_() - this.m_20189_();
                Vec3 vec = new Vec3(d0, d1, d2).normalize().scale((double) (Math.max(homeScale, 0.5F) * 1.2F));
                this.m_20256_(vec);
            } else {
                this.m_20256_(this.m_20184_().add(0.0, -0.09, 0.0));
            }
        }
        super.tick();
        Vec3 vector3d = this.m_20184_();
        HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
            this.onImpact(raytraceresult);
        }
        double d0 = this.m_20185_() + vector3d.x;
        double d1 = this.m_20186_() + vector3d.y;
        double d2 = this.m_20189_() + vector3d.z;
        this.m_20242_(true);
        this.updateRotation();
        if (this.m_9236_().m_45556_(this.m_20191_()).noneMatch(BlockBehaviour.BlockStateBase::m_60795_)) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else if (this.m_20072_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            this.m_20256_(vector3d.scale(0.99F));
            this.m_6034_(d0, d1, d2);
        }
    }

    protected void onEntityHit(EntityHitResult p_213868_1_) {
        Entity entity = this.getShooter();
        if (entity instanceof LivingEntity && !(p_213868_1_.getEntity() instanceof EntityVoidWorm) && !(p_213868_1_.getEntity() instanceof EntityVoidWormPart)) {
            boolean b = this.wormAttack(p_213868_1_.getEntity(), this.m_269291_().mobProjectile(this, (LivingEntity) entity), (float) (AMConfig.voidWormDamageModifier * 4.0));
            if (b && p_213868_1_.getEntity() instanceof Player) {
                Player player = (Player) p_213868_1_.getEntity();
                if (player.m_21211_().canPerformAction(ToolActions.SHIELD_BLOCK)) {
                    player.disableShield(true);
                }
            }
        }
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    private boolean wormAttack(Entity entity, DamageSource source, float dmg) {
        return entity.hurt(source, dmg);
    }

    protected void onHitBlock(BlockHitResult p_230299_1_) {
        BlockState blockstate = this.m_9236_().getBlockState(p_230299_1_.getBlockPos());
        if (!this.m_9236_().isClientSide) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(STOP_HOMING_PROGRESS, 0.0F);
    }

    public float getStopHomingProgress() {
        return this.f_19804_.get(STOP_HOMING_PROGRESS);
    }

    public void setStopHomingProgress(float progress) {
        this.f_19804_.set(STOP_HOMING_PROGRESS, progress);
    }

    public void setShooter(@Nullable Entity entityIn) {
        if (entityIn != null) {
            this.ownerUUID = entityIn.getUUID();
            this.ownerNetworkId = entityIn.getId();
        }
    }

    @Nullable
    public Entity getShooter() {
        if (this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            return ((ServerLevel) this.m_9236_()).getEntity(this.ownerUUID);
        } else {
            return this.ownerNetworkId != 0 ? this.m_9236_().getEntity(this.ownerNetworkId) : null;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
        if (this.leftOwner) {
            compound.putBoolean("LeftOwner", true);
        }
        compound.putFloat("HomeTime", this.getStopHomingProgress());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.setStopHomingProgress(compound.getFloat("HomeTime"));
        this.leftOwner = compound.getBoolean("LeftOwner");
    }

    private boolean checkLeftOwner() {
        Entity entity = this.getShooter();
        if (entity != null) {
            for (Entity entity1 : this.m_9236_().getEntities(this, this.m_20191_().expandTowards(this.m_20184_()).inflate(1.0), p_234613_0_ -> !p_234613_0_.isSpectator() && p_234613_0_.isPickable())) {
                if (entity1.getRootVehicle() == entity.getRootVehicle()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = new Vec3(x, y, z).normalize().add(this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy).scale((double) velocity);
        this.m_20256_(this.m_20184_().add(vector3d));
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        this.m_146922_((float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    protected void onImpact(HitResult result) {
        HitResult.Type raytraceresult$type = result.getType();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult) result);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) result);
        }
        this.m_146850_(GameEvent.ENTITY_DIE);
        this.m_5496_(SoundEvents.GLASS_BREAK, 1.0F, 0.5F);
        Entity entity = this.getShooter();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpMotion(double x, double y, double z) {
        this.m_20334_(x, y, z);
        if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
            float f = Mth.sqrt((float) (x * x + z * z));
            this.m_146926_((float) (Mth.atan2(y, (double) f) * 180.0F / (float) Math.PI));
            this.m_146922_((float) (Mth.atan2(x, z) * 180.0F / (float) Math.PI));
            this.f_19860_ = this.m_146909_();
            this.f_19859_ = this.m_146908_();
            this.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
        }
    }

    protected boolean canHitEntity(Entity p_230298_1_) {
        if (!p_230298_1_.isSpectator() && p_230298_1_.isAlive() && p_230298_1_.isPickable()) {
            Entity entity = this.getShooter();
            return (entity == null || this.leftOwner || !entity.isPassengerOfSameVehicle(p_230298_1_)) && !(p_230298_1_ instanceof EntityVoidWormShot) && !(p_230298_1_ instanceof EntityVoidWormPart);
        } else {
            return false;
        }
    }

    protected void updateRotation() {
        Vec3 vector3d = this.m_20184_();
        float f = Mth.sqrt((float) vector3d.horizontalDistance());
        this.m_146926_(lerpRotation(this.f_19860_, (float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI)));
        this.m_146922_(lerpRotation(this.f_19859_, (float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI)));
    }
}