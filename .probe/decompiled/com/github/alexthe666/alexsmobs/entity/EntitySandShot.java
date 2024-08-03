package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntitySandShot extends Entity {

    private UUID ownerUUID;

    private int ownerNetworkId;

    private boolean leftOwner;

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntitySandShot.class, EntityDataSerializers.INT);

    public EntitySandShot(EntityType p_i50162_1_, Level p_i50162_2_) {
        super(p_i50162_1_, p_i50162_2_);
    }

    public EntitySandShot(Level worldIn, EntityGuster p_i47273_2_) {
        this(AMEntityRegistry.SAND_SHOT.get(), worldIn);
        this.setShooter(p_i47273_2_);
        this.m_6034_(p_i47273_2_.m_20185_() - (double) (p_i47273_2_.m_20205_() + 1.0F) * 0.35 * (double) Mth.sin(p_i47273_2_.f_20883_ * (float) (Math.PI / 180.0)), p_i47273_2_.getEyeY() + 0.2F, p_i47273_2_.m_20189_() + (double) (p_i47273_2_.m_20205_() + 1.0F) * 0.35 * (double) Mth.cos(p_i47273_2_.f_20883_ * (float) (Math.PI / 180.0)));
    }

    public EntitySandShot(Level worldIn, LivingEntity p_i47273_2_, boolean right) {
        this(AMEntityRegistry.SAND_SHOT.get(), worldIn);
        this.setShooter(p_i47273_2_);
        float rot = p_i47273_2_.yHeadRot + (float) (right ? 60 : -60);
        this.m_6034_(p_i47273_2_.m_20185_() - (double) p_i47273_2_.m_20205_() * 0.5 * (double) Mth.sin(rot * (float) (Math.PI / 180.0)), p_i47273_2_.m_20188_() - 0.2F, p_i47273_2_.m_20189_() + (double) p_i47273_2_.m_20205_() * 0.5 * (double) Mth.cos(rot * (float) (Math.PI / 180.0)));
    }

    @OnlyIn(Dist.CLIENT)
    public EntitySandShot(Level worldIn, double x, double y, double z, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
        this(AMEntityRegistry.SAND_SHOT.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_20334_(p_i47274_8_, p_i47274_10_, p_i47274_12_);
    }

    public EntitySandShot(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.SAND_SHOT.get(), world);
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

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        ParticleOptions type = this.getVariant() == 2 ? AMParticleRegistry.GUSTER_SAND_SHOT_SOUL.get() : (this.getVariant() == 1 ? AMParticleRegistry.GUSTER_SAND_SHOT_RED.get() : AMParticleRegistry.GUSTER_SAND_SHOT.get());
        for (int i = 0; i < 3 + this.f_19796_.nextInt(6); i++) {
            double d0 = 0.1 + 0.3 * (double) i;
            this.m_9236_().addParticle(type, this.m_20185_() + (double) (0.25F * (this.f_19796_.nextFloat() - 0.5F)), this.m_20186_() + (double) (0.25F * (this.f_19796_.nextFloat() - 0.5F)), this.m_20189_() + (double) (0.25F * (this.f_19796_.nextFloat() - 0.5F)), this.m_20184_().x * d0, this.m_20184_().y, this.m_20184_().z * d0);
        }
        super.tick();
        Vec3 vector3d = this.m_20184_();
        HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
            this.onImpact(raytraceresult);
        }
        this.updateRotation();
        if (this.m_9236_().m_45556_(this.m_20191_()).noneMatch(BlockBehaviour.BlockStateBase::m_60795_)) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else if (this.m_20072_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            double d0 = this.m_20185_() + vector3d.x;
            double d1 = this.m_20186_() + vector3d.y;
            double d2 = this.m_20189_() + vector3d.z;
            this.m_20256_(vector3d.scale(0.99F));
            this.m_20256_(this.m_20184_().add(0.0, -0.03F, 0.0));
            if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.03F, 0.0));
            }
            this.m_6034_(d0, d1, d2);
        }
    }

    protected void onEntityHit(EntityHitResult p_213868_1_) {
        Entity entity = this.getSandShooter();
        if (entity instanceof LivingEntity) {
            p_213868_1_.getEntity().hurt(this.m_269291_().mobProjectile(this, (LivingEntity) entity), 2.5F);
        }
        if (entity instanceof Player && p_213868_1_.getEntity() instanceof LivingEntity) {
            ((LivingEntity) p_213868_1_.getEntity()).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, true, false));
        }
    }

    protected void onHitBlock(BlockHitResult p_230299_1_) {
        BlockState blockstate = this.m_9236_().getBlockState(p_230299_1_.getBlockPos());
        if (!this.m_9236_().isClientSide) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(VARIANT, 0);
    }

    public void setShooter(@Nullable Entity entityIn) {
        if (entityIn != null) {
            this.ownerUUID = entityIn.getUUID();
            this.ownerNetworkId = entityIn.getId();
        }
    }

    @Nullable
    public Entity getSandShooter() {
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
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.leftOwner = compound.getBoolean("LeftOwner");
    }

    private boolean checkLeftOwner() {
        Entity entity = this.getSandShooter();
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
        this.m_20256_(vector3d);
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        this.m_146922_((float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    public void shootFromRotation(Entity p_234612_1_, float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
        float f = -Mth.sin(p_234612_3_ * (float) (Math.PI / 180.0)) * Mth.cos(p_234612_2_ * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((p_234612_2_ + p_234612_4_) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(p_234612_3_ * (float) (Math.PI / 180.0)) * Mth.cos(p_234612_2_ * (float) (Math.PI / 180.0));
        this.shoot((double) f, (double) f1, (double) f2, p_234612_5_, p_234612_6_);
        Vec3 vector3d = p_234612_1_.getDeltaMovement();
        this.m_20256_(this.m_20184_().add(vector3d.x, p_234612_1_.onGround() ? 0.0 : vector3d.y, vector3d.z));
    }

    protected void onImpact(HitResult result) {
        HitResult.Type raytraceresult$type = result.getType();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult) result);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) result);
        }
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
            Entity entity = this.getSandShooter();
            return entity == null || this.leftOwner || !entity.isPassengerOfSameVehicle(p_230298_1_);
        } else {
            return false;
        }
    }

    protected void updateRotation() {
        Vec3 vector3d = this.m_20184_();
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        this.m_146926_(lerpRotation(this.f_19860_, (float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI)));
        this.m_146922_(lerpRotation(this.f_19859_, (float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI)));
    }
}