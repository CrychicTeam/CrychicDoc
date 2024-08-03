package io.redspace.ironsspellbooks.entity.spells.shield;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class ShieldEntity extends AbstractShieldEntity {

    protected ShieldPart[] subEntities;

    protected final Vec3[] subPositions;

    protected final int LIFETIME;

    protected int width = 5;

    protected int height = 5;

    protected int age;

    public ShieldEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.subEntities = new ShieldPart[this.width * this.height];
        this.subPositions = new Vec3[this.width * this.height];
        this.setHealth(10.0F);
        this.LIFETIME = 400;
        this.createShield();
    }

    public ShieldEntity(Level level, float health) {
        this(EntityRegistry.SHIELD_ENTITY.get(), level);
        this.setHealth(health);
    }

    @Override
    protected void createShield() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                int i = x * this.height + y;
                this.subEntities[i] = new ShieldPart(this, "part" + (i + 1), 0.5F, 0.5F, true);
                this.subPositions[i] = new Vec3((double) (((float) x - (float) this.width / 2.0F) * 0.5F + 0.25F), (double) (((float) y - (float) this.height / 2.0F) * 0.5F), 0.0);
            }
        }
    }

    public void setRotation(float x, float y) {
        this.m_146926_(x);
        this.f_19860_ = x;
        this.m_146922_(y);
        this.f_19859_ = y;
    }

    @Override
    public void takeDamage(DamageSource source, float amount, @Nullable Vec3 location) {
        if (!this.m_6673_(source)) {
            this.setHealth(this.getHealth() - amount);
            if (!this.m_9236_().isClientSide && location != null) {
                MagicManager.spawnParticles(this.m_9236_(), ParticleTypes.ELECTRIC_SPARK, location.x, location.y, location.z, 30, 0.1, 0.1, 0.1, 0.5, false);
                this.m_9236_().playSound(null, location.x, location.y, location.z, SoundRegistry.FORCE_IMPACT.get(), SoundSource.NEUTRAL, 0.8F, 1.0F);
            }
        }
    }

    @Override
    public void tick() {
        this.hurtThisTick = false;
        if (this.getHealth() <= 0.0F) {
            this.destroy();
        } else if (++this.age > this.LIFETIME) {
            if (!this.m_9236_().isClientSide) {
                this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.NEUTRAL, 1.0F, 1.4F);
            }
            this.m_146870_();
        } else {
            for (int i = 0; i < this.subEntities.length; i++) {
                ShieldPart subEntity = this.subEntities[i];
                Vec3 pos = this.subPositions[i].xRot((float) (Math.PI / 180.0) * -this.m_146909_()).yRot((float) (Math.PI / 180.0) * -this.m_146908_()).add(this.m_20182_());
                subEntity.m_146884_(pos);
                subEntity.f_19854_ = pos.x;
                subEntity.f_19855_ = pos.y;
                subEntity.f_19856_ = pos.z;
                subEntity.f_19790_ = pos.x;
                subEntity.f_19791_ = pos.y;
                subEntity.f_19792_ = pos.z;
            }
        }
    }

    @Override
    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    @Override
    protected void destroy() {
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 2.0F, 0.65F);
        }
        super.destroy();
    }
}