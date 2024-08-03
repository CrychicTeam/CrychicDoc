package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityHydraBreath extends Fireball implements IDragonProjectile {

    public EntityHydraBreath(EntityType<? extends Fireball> t, Level worldIn) {
        super(t, worldIn);
    }

    public EntityHydraBreath(EntityType<? extends Fireball> t, Level worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(t, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
    }

    public EntityHydraBreath(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.HYDRA_BREATH.get(), worldIn);
    }

    public EntityHydraBreath(EntityType<? extends Fireball> t, Level worldIn, EntityHydra shooter, double accelX, double accelY, double accelZ) {
        super(t, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.f_36813_ = accelX / d0 * 0.02;
        this.f_36814_ = accelY / d0 * 0.02;
        this.f_36815_ = accelZ / d0 * 0.02;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public float getPickRadius() {
        return 0.0F;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void tick() {
        this.m_20095_();
        if (this.f_19797_ > 30) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        Entity shootingEntity = this.m_19749_();
        if (this.m_9236_().isClientSide || (shootingEntity == null || shootingEntity.isAlive()) && this.m_9236_().m_46805_(this.m_20183_())) {
            this.m_6075_();
            if (this.shouldBurn()) {
                this.m_20254_(1);
            }
            HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, x$0 -> this.m_5603_(x$0));
            if (raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }
            Vec3 Vector3d = this.m_20184_();
            double d0 = this.m_20185_() + Vector3d.x;
            double d1 = this.m_20186_() + Vector3d.y;
            double d2 = this.m_20189_() + Vector3d.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.m_6884_();
            if (this.m_9236_().isClientSide) {
                for (int i = 0; i < 15; i++) {
                    IceAndFire.PROXY.spawnParticle(EnumParticles.Hydra, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() - 0.5, this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, 0.1, 1.0, 0.1);
                }
            }
            this.m_20256_(Vector3d.add(this.f_36813_, this.f_36814_, this.f_36815_).scale((double) f));
            this.f_36813_ *= 0.95F;
            this.f_36814_ *= 0.95F;
            this.f_36815_ *= 0.95F;
            this.m_5997_(this.f_36813_, this.f_36814_, this.f_36815_);
            if (this.m_20069_()) {
                for (int i = 0; i < 4; i++) {
                    this.m_9236_().addParticle(ParticleTypes.BUBBLE, this.m_20185_() - this.m_20184_().x * 0.25, this.m_20186_() - this.m_20184_().y * 0.25, this.m_20189_() - this.m_20184_().z * 0.25, this.m_20184_().x, this.m_20184_().y, this.m_20184_().z);
                }
            }
            this.m_6034_(d0, d1, d2);
            this.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
        }
    }

    public boolean handleWaterMovement() {
        return true;
    }

    @Override
    protected void onHit(@NotNull HitResult movingObject) {
        this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        Entity shootingEntity = this.m_19749_();
        if (!this.m_9236_().isClientSide && movingObject.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) movingObject).getEntity();
            if (entity != null && entity instanceof EntityHydraHead) {
                return;
            }
            if (shootingEntity != null && shootingEntity instanceof EntityHydra dragon) {
                if (dragon.m_7307_(entity) || dragon.m_7306_(entity)) {
                    return;
                }
                entity.hurt(this.m_9236_().damageSources().mobAttack(dragon), 2.0F);
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
                }
            }
        }
    }
}