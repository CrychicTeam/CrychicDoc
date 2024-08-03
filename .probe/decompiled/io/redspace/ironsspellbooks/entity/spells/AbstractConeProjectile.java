package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.entity.NoKnockbackProjectile;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public abstract class AbstractConeProjectile extends Projectile implements NoKnockbackProjectile {

    protected static final int FAILSAFE_EXPIRE_TIME = 400;

    protected int age;

    protected float damage;

    protected boolean dealDamageActive = true;

    protected final ConePart[] subEntities;

    public AbstractConeProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level, LivingEntity entity) {
        this(entityType, level);
        this.m_5602_(entity);
    }

    public AbstractConeProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
        this.f_19794_ = true;
        this.f_19850_ = false;
        this.subEntities = new ConePart[] { new ConePart(this, "part1", 1.0F, 1.0F), new ConePart(this, "part2", 2.5F, 1.5F), new ConePart(this, "part3", 3.5F, 2.0F), new ConePart(this, "part4", 4.5F, 3.0F) };
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public abstract void spawnParticles();

    @Override
    protected abstract void onHitEntity(EntityHitResult var1);

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    @Override
    public void setId(int id) {
        super.m_20234_(id);
        for (int i = 0; i < this.subEntities.length; i++) {
            this.subEntities[i].m_20234_(id + i + 1);
        }
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    protected void defineSynchedData() {
    }

    protected static Vec3 rayTrace(Entity owner) {
        float f = owner.getXRot();
        float f1 = owner.getYRot();
        float f2 = Mth.cos(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f4 = -Mth.cos(-f * (float) (Math.PI / 180.0));
        float f5 = Mth.sin(-f * (float) (Math.PI / 180.0));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        return new Vec3((double) f6, (double) f5, (double) f7);
    }

    @Override
    public void tick() {
        super.tick();
        if (++this.age > 400) {
            this.m_146870_();
        }
        Entity owner = this.m_19749_();
        if (owner != null) {
            Vec3 rayTraceVector = rayTrace(owner);
            Vec3 ownerEyePos = owner.getEyePosition(1.0F).subtract(0.0, 0.8, 0.0);
            this.m_146884_(ownerEyePos);
            this.m_146926_(owner.getXRot());
            this.m_146922_(owner.getYRot());
            this.f_19859_ = this.m_146908_();
            this.f_19860_ = this.m_146909_();
            double scale = 1.0;
            for (int i = 0; i < this.subEntities.length; i++) {
                ConePart subEntity = this.subEntities[i];
                double distance = 1.0 + (double) i * scale * (double) subEntity.getDimensions(null).width / 2.0;
                Vec3 newVector = ownerEyePos.add(rayTraceVector.multiply(distance, distance, distance));
                subEntity.m_146884_(newVector);
                subEntity.m_20256_(newVector);
                Vec3 vec3 = new Vec3(subEntity.m_20185_(), subEntity.m_20186_(), subEntity.m_20189_());
                subEntity.f_19854_ = vec3.x;
                subEntity.f_19855_ = vec3.y;
                subEntity.f_19856_ = vec3.z;
                subEntity.f_19790_ = vec3.x;
                subEntity.f_19791_ = vec3.y;
                subEntity.f_19792_ = vec3.z;
            }
        }
        if (!this.m_9236_().isClientSide) {
            if (this.dealDamageActive) {
                for (Entity entity : this.getSubEntityCollisions()) {
                    this.onHitEntity(new EntityHitResult(entity));
                }
                this.dealDamageActive = false;
            }
        } else {
            this.spawnParticles();
        }
    }

    public void setDealDamageActive() {
        this.dealDamageActive = true;
    }

    protected Set<Entity> getSubEntityCollisions() {
        List<Entity> collisions = new ArrayList();
        for (Entity conepart : this.subEntities) {
            collisions.addAll(this.m_9236_().m_45933_(conepart, conepart.getBoundingBox()));
        }
        return (Set<Entity>) collisions.stream().filter(target -> target != this.m_19749_() && target instanceof LivingEntity && hasLineOfSight(this, target)).collect(Collectors.toSet());
    }

    protected static boolean hasLineOfSight(Entity start, Entity target) {
        Vec3 vec3 = new Vec3(start.getX(), start.getEyeY(), start.getZ());
        Vec3 vec31 = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        boolean isShieldBlockingLOS = Utils.raycastForEntity(start.level(), start, vec3, vec31, false, 0.0F, entity -> entity instanceof ShieldEntity).getType() == HitResult.Type.ENTITY;
        return !isShieldBlockingLOS && start.level().m_45547_(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, start)).getType() == HitResult.Type.MISS;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("Damage", this.damage);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.damage = pCompound.getFloat("Damage");
    }
}