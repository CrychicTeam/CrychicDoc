package io.redspace.ironsspellbooks.entity.spells.fire_breath;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireBreathProjectile extends AbstractConeProjectile {

    public FireBreathProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public FireBreathProjectile(Level level, LivingEntity entity) {
        super(EntityRegistry.FIRE_BREATH_PROJECTILE.get(), level, entity);
    }

    @Override
    public void tick() {
        if (!this.f_19853_.isClientSide && this.dealDamageActive && ServerConfigs.SPELL_GREIFING.get()) {
            float range = (float) (Math.PI / 12);
            for (int i = 0; i < 3; i++) {
                Vec3 cast = this.m_19749_().getLookAngle().normalize().xRot(Utils.random.nextFloat() * range * 2.0F - range).yRot(Utils.random.nextFloat() * range * 2.0F - range);
                HitResult hitResult = this.f_19853_.m_45547_(new ClipContext(this.m_19749_().getEyePosition(), this.m_19749_().getEyePosition().add(cast.scale(10.0)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    HitResult shieldResult = Utils.raycastForEntityOfClass(this.f_19853_, this, this.m_19749_().getEyePosition(), hitResult.getLocation(), false, AbstractShieldEntity.class);
                    if (shieldResult.getType() == HitResult.Type.MISS) {
                        Vec3 pos = hitResult.getLocation().subtract(cast.scale(0.5));
                        BlockPos blockPos = BlockPos.containing(pos.x, pos.y, pos.z);
                        if (this.f_19853_.getBlockState(blockPos).m_60795_()) {
                            this.f_19853_.setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.f_19853_, blockPos));
                        }
                    }
                }
            }
        }
        super.tick();
    }

    @Override
    public void spawnParticles() {
        Entity owner = this.m_19749_();
        if (this.f_19853_.isClientSide && owner != null) {
            Vec3 rotation = owner.getLookAngle().normalize();
            Vec3 pos = owner.position().add(rotation.scale(1.6));
            double x = pos.x;
            double y = pos.y + (double) (owner.getEyeHeight() * 0.9F);
            double z = pos.z;
            double speed = this.f_19796_.nextDouble() * 0.35 + 0.35;
            for (int i = 0; i < 10; i++) {
                double offset = 0.15;
                double ox = Math.random() * 2.0 * offset - offset;
                double oy = Math.random() * 2.0 * offset - offset;
                double oz = Math.random() * 2.0 * offset - offset;
                double angularness = 0.5;
                Vec3 randomVec = new Vec3(Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness).normalize();
                Vec3 result = rotation.scale(3.0).add(randomVec).normalize().scale(speed);
                this.f_19853_.addParticle(ParticleHelper.FIRE, x + ox, y + oy, z + oz, result.x, result.y, result.z);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        DamageSources.applyDamage(entity, this.damage, SpellRegistry.FIRE_BREATH_SPELL.get().getDamageSource(this, this.m_19749_()));
    }
}