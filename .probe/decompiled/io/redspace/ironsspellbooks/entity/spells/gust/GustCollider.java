package io.redspace.ironsspellbooks.entity.spells.gust;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class GustCollider extends AbstractConeProjectile {

    public float strength;

    public float range;

    public int amplifier;

    public GustCollider(Level level, LivingEntity owner) {
        this(EntityRegistry.GUST_COLLIDER.get(), level);
        this.m_5602_(owner);
        this.m_19915_(owner.m_146908_(), owner.m_146909_());
    }

    public GustCollider(EntityType<GustCollider> gustColliderEntityType, Level level) {
        super(gustColliderEntityType, level);
    }

    @Override
    public void spawnParticles() {
        if (this.f_19853_.isClientSide && this.f_19797_ <= 2) {
            Vec3 rotation = this.m_20154_().normalize();
            Vec3 pos = this.m_20182_().add(rotation.scale(1.6));
            double x = pos.x;
            double y = pos.y;
            double z = pos.z;
            double speed = this.f_19796_.nextDouble() * 0.4 + 0.45;
            for (int i = 0; i < 5; i++) {
                double offset = 0.25;
                double ox = Math.random() * 2.0 * offset - offset;
                double oy = Math.random() * 2.0 * offset - offset;
                double oz = Math.random() * 2.0 * offset - offset;
                double angularness = 0.8;
                Vec3 randomVec = new Vec3(Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness).normalize();
                Vec3 result = rotation.scale(3.0).add(randomVec).normalize().scale(speed);
                this.f_19853_.addParticle(ParticleTypes.POOF, x + ox, y + oy, z + oz, result.x, result.y, result.z);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = this.getOwner();
        if (entity != null && entityHitResult.getEntity() instanceof LivingEntity target && target.m_20280_(entity) < (double) (this.range * this.range) && !DamageSources.isFriendlyFireBetween(entity, target)) {
            knockback(target, (double) this.strength, entity.getX() - target.m_20185_(), entity.getY() - target.m_20186_(), entity.getZ() - target.m_20189_());
            target.f_19864_ = true;
            target.addEffect(new MobEffectInstance(MobEffectRegistry.AIRBORNE.get(), 60, this.amplifier));
        }
    }

    private static void knockback(LivingEntity target, double pStrength, double x, double y, double z) {
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(target, (float) pStrength, x, z);
        if (!event.isCanceled()) {
            pStrength = (double) event.getStrength();
            x = event.getRatioX();
            z = event.getRatioZ();
            pStrength *= 1.0 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * 0.5;
            if (!(pStrength <= 0.0)) {
                target.f_19812_ = true;
                Vec3 vec3 = target.m_20184_();
                Vec3 vec31 = new Vec3(x, y, z).normalize().scale(pStrength);
                target.m_20334_(vec3.x / 2.0 - vec31.x, target.m_20096_() ? Math.min(0.4, vec3.y / 2.0 + pStrength) : vec3.y, vec3.z / 2.0 - vec31.z);
            }
        }
    }

    @Override
    public void tick() {
        double x = this.m_20185_();
        double y = this.m_20186_();
        double z = this.m_20189_();
        if (this.f_19797_ > 8) {
            this.m_146870_();
        } else {
            super.tick();
        }
        this.m_20343_(x, y, z);
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return this.f_19797_ >= 1 ? null : super.m_19749_();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}