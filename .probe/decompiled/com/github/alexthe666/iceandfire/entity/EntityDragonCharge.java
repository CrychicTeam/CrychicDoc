package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public abstract class EntityDragonCharge extends Fireball implements IDragonProjectile {

    public EntityDragonCharge(EntityType<? extends Fireball> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityDragonCharge(EntityType<? extends Fireball> type, Level worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.f_36813_ = accelX / d0 * 0.07;
        this.f_36814_ = accelY / d0 * 0.07;
        this.f_36815_ = accelZ / d0 * 0.07;
    }

    public EntityDragonCharge(EntityType<? extends Fireball> type, Level worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.f_36813_ = accelX / d0 * 0.07;
        this.f_36814_ = accelY / d0 * 0.07;
        this.f_36815_ = accelZ / d0 * 0.07;
    }

    @Override
    public void tick() {
        Entity shootingEntity = this.m_19749_();
        if (this.m_9236_().isClientSide || (shootingEntity == null || shootingEntity.isAlive()) && this.m_9236_().m_46805_(this.m_20183_())) {
            super.m_6075_();
            HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitMob);
            if (raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }
            this.m_20101_();
            Vec3 vector3d = this.m_20184_();
            double d0 = this.m_20185_() + vector3d.x;
            double d1 = this.m_20186_() + vector3d.y;
            double d2 = this.m_20189_() + vector3d.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.m_6884_();
            if (this.m_20069_()) {
                for (int i = 0; i < 4; i++) {
                    this.m_9236_().addParticle(ParticleTypes.BUBBLE, this.m_20185_() - this.m_20184_().x * 0.25, this.m_20186_() - this.m_20184_().y * 0.25, this.m_20189_() - this.m_20184_().z * 0.25, this.m_20184_().x, this.m_20184_().y, this.m_20184_().z);
                }
                f = 0.8F;
            }
            this.m_20256_(vector3d.add(this.f_36813_, this.f_36814_, this.f_36815_).scale((double) f));
            this.m_9236_().addParticle(this.m_5967_(), this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), 0.0, 0.0, 0.0);
            this.m_6034_(d0, d1, d2);
        } else {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHit(@NotNull HitResult movingObject) {
        Entity shootingEntity = this.m_19749_();
        if (!this.m_9236_().isClientSide) {
            if (movingObject.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) movingObject).getEntity();
                if (entity instanceof IDragonProjectile) {
                    return;
                }
                if (shootingEntity != null && shootingEntity instanceof EntityDragonBase dragon && (dragon.isAlliedTo(entity) || dragon.m_7306_(entity) || dragon.isPart(entity))) {
                    return;
                }
                if (entity == null || !(entity instanceof IDragonProjectile) && entity != shootingEntity && shootingEntity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) shootingEntity;
                    if (shootingEntity != null && (entity == shootingEntity || entity instanceof TamableAnimal && ((EntityDragonBase) shootingEntity).m_21830_(((EntityDragonBase) shootingEntity).m_269323_()))) {
                        return;
                    }
                    if (dragon != null) {
                        dragon.randomizeAttacks();
                    }
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
                if (entity != null && !(entity instanceof IDragonProjectile) && !entity.is(shootingEntity)) {
                    if (shootingEntity != null && (entity.is(shootingEntity) || shootingEntity instanceof EntityDragonBase && entity instanceof TamableAnimal && ((EntityDragonBase) shootingEntity).m_269323_() == ((TamableAnimal) entity).m_269323_())) {
                        return;
                    }
                    if (shootingEntity instanceof EntityDragonBase) {
                        float damageAmount = this.getDamage() * (float) ((EntityDragonBase) shootingEntity).getDragonStage();
                        EntityDragonBase shootingDragon = (EntityDragonBase) shootingEntity;
                        Entity cause = (Entity) (shootingDragon.getRidingPlayer() != null ? shootingDragon.getRidingPlayer() : shootingDragon);
                        DamageSource source = this.causeDamage(cause);
                        entity.hurt(source, damageAmount);
                        if (entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() == 0.0F) {
                            ((EntityDragonBase) shootingEntity).randomizeAttacks();
                        }
                    }
                    if (shootingEntity instanceof LivingEntity) {
                        this.m_19970_((LivingEntity) shootingEntity, entity);
                    }
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
            if (movingObject.getType() != HitResult.Type.MISS) {
                if (shootingEntity instanceof EntityDragonBase && DragonUtils.canGrief((EntityDragonBase) shootingEntity)) {
                    this.destroyArea(this.m_9236_(), BlockPos.containing(this.m_20185_(), this.m_20186_(), this.m_20189_()), (EntityDragonBase) shootingEntity);
                }
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    public abstract DamageSource causeDamage(@Nullable Entity var1);

    public abstract void destroyArea(Level var1, BlockPos var2, EntityDragonBase var3);

    public abstract float getDamage();

    @Override
    public boolean isPickable() {
        return false;
    }

    protected boolean canHitMob(Entity hitMob) {
        Entity shooter = this.m_19749_();
        return hitMob != this && super.m_5603_(hitMob) && shooter != null && !hitMob.isAlliedTo(shooter) && !(hitMob instanceof EntityDragonPart);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public float getPickRadius() {
        return 0.0F;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}