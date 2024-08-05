package com.mna.entities.boss.attacks;

import com.mna.entities.EntityInit;
import com.mna.tools.math.MathUtils;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

public class OrangeChickenProjectile extends AbstractHurtingProjectile {

    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(OrangeChickenProjectile.class, EntityDataSerializers.INT);

    private LivingEntity target = null;

    private int ticksInAir;

    public OrangeChickenProjectile(EntityType<OrangeChickenProjectile> type, Level world) {
        super(EntityInit.ORANGE_CHICKEN.get(), world);
        this.m_20242_(true);
    }

    public OrangeChickenProjectile(Level world, LivingEntity target) {
        this(EntityInit.ORANGE_CHICKEN.get(), world);
        if (target != null) {
            this.f_19804_.set(TARGET_ID, target.m_19879_());
            this.target = target;
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(TARGET_ID, -1);
    }

    @Override
    protected void onHit(HitResult raytraceResultIn) {
        if (!this.m_9236_().isClientSide()) {
            this.explode();
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.IS_EXPLOSION) || this.m_213877_()) {
            this.explode();
        }
        return true;
    }

    private void explode() {
        if (!this.m_9236_().isClientSide() && !this.m_213877_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            this.m_9236_().explode(this, this.m_20185_(), this.m_20186_(), this.m_20189_(), 5.0F, Level.ExplosionInteraction.TNT);
            int cookies = 5 + (int) (Math.random() * 10.0);
            for (int i = 0; i < cookies; i++) {
                ItemEntity cookie = new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), new ItemStack(Items.COOKIE));
                cookie.m_20256_(new Vec3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5));
                this.m_9236_().m_7967_(cookie);
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Nullable
    private LivingEntity getTarget() {
        if (this.target == null) {
            Entity e = this.m_9236_().getEntity(this.f_19804_.get(TARGET_ID));
            if (e instanceof LivingEntity) {
                this.target = (LivingEntity) e;
            }
        }
        return this.target;
    }

    @Override
    public void tick() {
        if (!this.m_213877_()) {
            LivingEntity target = this.getTarget();
            if (target == null) {
                if (!this.m_9236_().isClientSide()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            } else {
                Vec3 motion = this.m_20184_();
                float homingStrength = 5.234F;
                Vec3 myPos = this.m_20182_();
                Vec3 theirPos = target.m_20182_().add(0.0, (double) (target.m_20206_() / 2.0F), 0.0);
                float tickTheta = 15.0F * MathUtils.clamp01(homingStrength);
                if (tickTheta > 0.0F) {
                    Vec3 desiredHeading = theirPos.subtract(myPos).normalize();
                    Vec3 calculatedHeading = MathUtils.rotateTowards(this.m_20184_(), desiredHeading, tickTheta).normalize().scale(1.25);
                    this.m_20256_(calculatedHeading);
                }
                if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
                    double f = motion.horizontalDistance();
                    this.m_146922_((float) (Mth.atan2(motion.x, motion.z) * 180.0F / (float) Math.PI));
                    this.m_146926_((float) (Mth.atan2(motion.y, f) * 180.0F / (float) Math.PI));
                    this.f_19859_ = this.m_146908_();
                    this.f_19860_ = this.m_146909_();
                }
                this.ticksInAir++;
                Vec3 Vector3d2 = this.m_20182_();
                Vec3 Vector3d3 = Vector3d2.add(motion);
                HitResult raytraceresult = this.m_9236_().m_45547_(new ClipContext(Vector3d2, Vector3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                if (raytraceresult.getType() != HitResult.Type.MISS) {
                    Vector3d3 = raytraceresult.getLocation();
                }
                EntityHitResult entityraytraceresult = this.rayTraceEntities(Vector3d2, Vector3d3);
                if (entityraytraceresult != null) {
                    raytraceresult = entityraytraceresult;
                }
                if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                    this.f_19812_ = true;
                }
                motion = this.m_20184_();
                double d3 = motion.x;
                double d4 = motion.y;
                double d0 = motion.z;
                double d5 = this.m_20185_() + d3;
                double d1 = this.m_20186_() + d4;
                double d2 = this.m_20189_() + d0;
                double f1 = motion.horizontalDistance();
                this.m_146922_((float) (Mth.atan2(d3, d0) * 180.0F / (float) Math.PI));
                this.m_146926_((float) (Mth.atan2(d4, f1) * 180.0F / (float) Math.PI));
                while (this.m_146909_() - this.f_19860_ < -180.0F) {
                    this.f_19860_ -= 360.0F;
                }
                while (this.m_146909_() - this.f_19860_ >= 180.0F) {
                    this.f_19860_ += 360.0F;
                }
                while (this.m_146908_() - this.f_19859_ < -180.0F) {
                    this.f_19859_ -= 360.0F;
                }
                while (this.m_146908_() - this.f_19859_ >= 180.0F) {
                    this.f_19859_ += 360.0F;
                }
                this.m_146926_(Mth.lerp(0.2F, this.f_19860_, this.m_146909_()));
                this.m_146922_(Mth.lerp(0.2F, this.f_19859_, this.m_146908_()));
                this.m_6034_(d5, d1, d2);
                this.m_20101_();
                if (this.f_19797_ > 400) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    @Nullable
    protected EntityHitResult rayTraceEntities(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(this.m_9236_(), this, startVec, endVec, this.m_20191_().expandTowards(this.m_20184_()).inflate(1.0), candidate -> !candidate.isSpectator() && candidate.isAlive() && candidate.isPickable() || this.ticksInAir >= 5);
    }
}