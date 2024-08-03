package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class ExtendedWitherSkull extends WitherSkull implements AntiMagicSusceptible {

    protected float damage;

    public ExtendedWitherSkull(EntityType<? extends WitherSkull> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ExtendedWitherSkull(LivingEntity shooter, Level level, float speed, float damage) {
        super(EntityRegistry.WITHER_SKULL_PROJECTILE.get(), level);
        this.m_5602_(shooter);
        Vec3 power = shooter.m_20154_().normalize().scale((double) speed);
        this.f_36813_ = power.x;
        this.f_36814_ = power.y;
        this.f_36815_ = power.z;
        this.damage = damage;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.m_9236_().isClientSide) {
            float explosionRadius = 2.0F;
            for (Entity entity : this.m_9236_().m_45933_(this, this.m_20191_().inflate((double) explosionRadius))) {
                double distance = entity.distanceToSqr(hitResult.getLocation());
                if (distance < (double) (explosionRadius * explosionRadius) && this.m_5603_(entity)) {
                    float damage = (float) ((double) this.damage * (1.0 - distance / (double) (explosionRadius * explosionRadius)));
                    AbstractSpell spell = SpellRegistry.WITHER_SKULL_SPELL.get();
                    DamageSources.applyDamage(entity, damage, spell.getDamageSource(this, this.m_19749_()));
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