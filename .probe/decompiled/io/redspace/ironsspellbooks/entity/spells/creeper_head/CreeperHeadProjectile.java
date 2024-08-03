package io.redspace.ironsspellbooks.entity.spells.creeper_head;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.evocation.ChainCreeperSpell;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class CreeperHeadProjectile extends WitherSkull implements AntiMagicSusceptible {

    protected float damage;

    protected boolean chainOnKill = false;

    public CreeperHeadProjectile(EntityType<? extends WitherSkull> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public CreeperHeadProjectile(LivingEntity shooter, Level level, float speed, float damage) {
        this(EntityRegistry.CREEPER_HEAD_PROJECTILE.get(), level);
        this.m_5602_(shooter);
        Vec3 power = shooter.m_20154_().normalize().scale((double) speed);
        this.f_36813_ = power.x;
        this.f_36814_ = power.y;
        this.f_36815_ = power.z;
        this.m_20334_(this.f_36813_, this.f_36814_, this.f_36815_);
        this.damage = damage;
    }

    public CreeperHeadProjectile(LivingEntity shooter, Level level, Vec3 power, float damage) {
        this(EntityRegistry.CREEPER_HEAD_PROJECTILE.get(), level);
        this.m_5602_(shooter);
        this.f_36813_ = power.x;
        this.f_36814_ = power.y;
        this.f_36815_ = power.z;
        this.m_20334_(this.f_36813_, this.f_36814_, this.f_36815_);
        this.damage = damage;
    }

    public void setChainOnKill(boolean chain) {
        this.chainOnKill = chain;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
    }

    @Override
    public void tick() {
        if (!this.m_9236_().isClientSide) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, x$0 -> this.m_5603_(x$0));
            if (hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
            }
        } else {
            this.m_9236_().addParticle(this.m_5967_(), this.m_20182_().x, this.m_20182_().y + 0.25, this.m_20182_().z, 0.0, 0.0, 0.0);
        }
        ProjectileUtil.rotateTowardsMovement(this, 1.0F);
        this.m_146884_(this.m_20182_().add(this.m_20184_()));
        if (!this.m_20068_()) {
            Vec3 vec34 = this.m_20184_();
            this.m_20334_(vec34.x, vec34.y - 0.05F, vec34.z);
        }
        this.m_6075_();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.m_9236_().isClientSide) {
            float explosionRadius = 3.5F;
            for (Entity entity : this.m_9236_().m_45933_(this, this.m_20191_().inflate((double) explosionRadius))) {
                double distance = entity.position().distanceTo(hitResult.getLocation());
                if (distance < (double) explosionRadius) {
                    if (entity instanceof LivingEntity livingEntity && livingEntity.isDeadOrDying() && !this.m_5603_(entity)) {
                        break;
                    }
                    float damage = (float) ((double) this.damage * (1.0 - Math.pow(distance / (double) explosionRadius, 2.0)));
                    DamageSources.applyDamage(entity, damage, SpellRegistry.LOB_CREEPER_SPELL.get().getDamageSource(this, this.m_19749_()));
                    entity.invulnerableTime = 0;
                    if (this.chainOnKill && entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        if (livingEntity.isDeadOrDying()) {
                            ChainCreeperSpell.summonCreeperRing(this.m_9236_(), this.m_19749_() instanceof LivingEntity livingOwner ? livingOwner : null, livingEntity.m_146892_(), this.damage * 0.85F, 3);
                        }
                    }
                }
            }
            this.m_9236_().explode(this, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0F, false, Level.ExplosionInteraction.NONE);
            this.m_146870_();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.m_146870_();
    }
}