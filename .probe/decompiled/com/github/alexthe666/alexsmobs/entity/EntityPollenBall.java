package com.github.alexthe666.alexsmobs.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class EntityPollenBall extends EntityMobProjectile {

    public EntityPollenBall(EntityType type, Level level) {
        super(type, level);
    }

    public EntityPollenBall(Level worldIn, EntityFlutter flutter) {
        super(AMEntityRegistry.POLLEN_BALL.get(), worldIn, flutter);
        Vec3 vec3 = flutter.m_20182_().add(this.calcOffsetVec(new Vec3(0.0, (double) (0.4F * flutter.m_6134_()), 0.0), flutter.getFlutterPitch(), flutter.m_146908_()));
        this.m_6034_(vec3.x, vec3.y, vec3.z);
    }

    public EntityPollenBall(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.POLLEN_BALL.get(), world);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void doBehavior() {
        Entity entity = this.getShooter();
        if (entity instanceof Mob && ((Mob) entity).getTarget() != null) {
            LivingEntity target = ((Mob) entity).getTarget();
            if (target == null) {
                this.m_6074_();
            }
            double d0 = target.m_20185_() - this.m_20185_();
            double d1 = target.m_20186_() + (double) (target.m_20206_() * 0.5F) - this.m_20186_();
            double d2 = target.m_20189_() - this.m_20189_();
            float speed = 0.35F;
            this.shoot(d0, d1, d2, 0.35F, 0.0F);
            this.m_146922_(-((float) Mth.atan2(d0, d2)) * (180.0F / (float) Math.PI));
        }
        if (this.m_9236_().isClientSide && this.f_19796_.nextInt(2) == 0) {
            float r1 = (this.f_19796_.nextFloat() - 0.5F) * 0.5F;
            float r2 = (this.f_19796_.nextFloat() - 0.5F) * 0.5F;
            float r3 = (this.f_19796_.nextFloat() - 0.5F) * 0.5F;
            this.m_9236_().addParticle(ParticleTypes.FALLING_NECTAR, this.m_20185_() + (double) r1, this.m_20186_() + (double) r2, this.m_20189_() + (double) r3, (double) (r1 * 0.1F), (double) (r2 * 0.1F), (double) (r3 * 0.1F));
        }
    }

    @Override
    protected float getDamage() {
        return 3.0F;
    }
}