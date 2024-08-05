package dev.xkmc.l2hostility.content.entity;

import dev.xkmc.l2library.init.explosion.BaseExplosion;
import dev.xkmc.l2library.init.explosion.BaseExplosionContext;
import dev.xkmc.l2library.init.explosion.ExplosionHandler;
import dev.xkmc.l2library.init.explosion.VanillaExplosionContext;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public enum BulletType {

    PLAIN(4.0F, true), EXPLODE(4.0F, true);

    private final float damage;

    private final boolean limit;

    private BulletType(float damage, boolean limit) {
        this.damage = damage;
        this.limit = limit;
    }

    public float getDamage(int level) {
        return this.damage * (float) level;
    }

    public void onHit(HostilityBullet bullet, HitResult result, int level) {
        if (this == EXPLODE) {
            Vec3 pos = result.getLocation();
            ExplosionHandler.explode(new BaseExplosion(new BaseExplosionContext(bullet.m_9236_(), pos.x, pos.y, pos.z, (float) (1 + level)), new VanillaExplosionContext(bullet, null, null, false, Explosion.BlockInteraction.KEEP), bullet::isTarget));
        }
    }

    public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
        if (event.getSource().getDirectEntity() instanceof ShulkerBullet) {
            event.setCanceled(true);
        } else {
            if (this == EXPLODE && event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
                event.setCanceled(true);
            }
        }
    }

    public boolean limit() {
        return this.limit;
    }
}