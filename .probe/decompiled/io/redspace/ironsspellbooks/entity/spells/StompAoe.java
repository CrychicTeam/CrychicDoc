package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StompAoe extends AbstractMagicProjectile {

    int step;

    int maxSteps;

    @Override
    public void trailParticles() {
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return 0.0F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    public StompAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_19794_ = true;
        this.m_20242_(true);
        this.maxSteps = 5;
    }

    public StompAoe(Level level, int steps, float yRot) {
        this(EntityRegistry.STOMP_AOE.get(), level);
        this.maxSteps = steps;
        this.m_146922_(yRot);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_19853_.isClientSide) {
            if (this.f_19797_ % 1 == 0) {
                this.checkHits();
            }
            if (this.step > this.maxSteps) {
                this.m_146870_();
            }
        }
    }

    protected void checkHits() {
        if (!this.f_19853_.isClientSide) {
            this.step++;
            int width = Math.max(this.step / 2, 2);
            float angle = this.m_146908_() * (float) (Math.PI / 180.0);
            Vec3 forward = new Vec3((double) Mth.sin(-angle), 0.0, (double) Mth.cos(-angle));
            Vec3 orth = new Vec3(-forward.z, 0.0, forward.x);
            Vec3 center = this.m_20182_().add(forward.scale((double) this.step));
            Vec3 leftBound = Utils.moveToRelativeGroundLevel(this.f_19853_, center.subtract(orth.scale((double) width)), 2).add(0.0, 0.75, 0.0);
            Vec3 rightBound = Utils.moveToRelativeGroundLevel(this.f_19853_, center.add(orth.scale((double) width)), 2).add(0.0, 0.75, 0.0);
            this.f_19853_.m_45933_(this, new AABB(leftBound.add(0.0, -1.0, 0.0), rightBound.add(0.0, 1.0, 0.0))).forEach(entity -> {
                if (this.m_5603_(entity) && Utils.checkEntityIntersecting(entity, leftBound, rightBound, 0.5F).getType() != HitResult.Type.MISS && DamageSources.applyDamage(entity, this.getDamage(), SpellRegistry.STOMP_SPELL.get().getDamageSource(this, this.m_19749_())) && entity instanceof LivingEntity livingEntity) {
                    livingEntity.knockback((double) (this.explosionRadius * -0.35F), forward.x, forward.z);
                }
            });
            for (int i = 0; i < this.step; i++) {
                Vec3 pos = leftBound.add(rightBound.subtract(leftBound).scale((double) ((float) i / (float) this.step)));
                BlockPos blockPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(this.f_19853_, pos, 2)).below();
                float impulseStrength = Utils.random.nextFloat() * 0.15F + 0.2F;
                Utils.createTremorBlock(this.f_19853_, blockPos, impulseStrength);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("stompStep", this.step);
        pCompound.putInt("maxSteps", this.maxSteps);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.step = pCompound.getInt("stompStep");
        this.maxSteps = pCompound.getInt("maxSteps");
    }
}