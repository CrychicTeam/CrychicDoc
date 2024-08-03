package com.mna.tools;

import com.mna.tools.math.MathUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class ProjectileHelper {

    public static void ReflectProjectile(LivingEntity target, Projectile p, boolean atShooter, float outSpeed) {
        Vec3 reflected;
        if (!atShooter) {
            reflected = reflectVector(target, p, outSpeed);
        } else if (p.getOwner() != null) {
            Vec3 targetPos = p.getOwner().getEyePosition(0.0F).add(p.m_20184_());
            reflected = p.m_20182_().subtract(targetPos).normalize().scale((double) outSpeed);
        } else {
            reflected = reflectVector(target, p, outSpeed);
        }
        p.setOwner(target);
        p.f_19797_ = 0;
        if (p instanceof AbstractArrow) {
            ((AbstractArrow) p).pickup = AbstractArrow.Pickup.DISALLOWED;
            ((AbstractArrow) p).setBaseDamage(((AbstractArrow) p).getBaseDamage() * 2.0);
        }
        p.m_20256_(reflected);
    }

    private static Vec3 reflectVector(LivingEntity target, Projectile p, float outSpeed) {
        Vec3 direction = p.m_20184_();
        Vec3 look = Vec3.directionFromRotation(target.m_20155_());
        Vec3 normal = direction.add(look).scale(0.5).normalize();
        return MathUtils.reflect(direction, normal).normalize().scale((double) outSpeed);
    }
}