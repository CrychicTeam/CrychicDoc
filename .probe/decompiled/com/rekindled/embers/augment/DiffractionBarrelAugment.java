package com.rekindled.embers.augment;

import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.event.EmberProjectileEvent;
import com.rekindled.embers.api.projectile.EffectArea;
import com.rekindled.embers.api.projectile.EffectDamage;
import com.rekindled.embers.api.projectile.EffectMulti;
import com.rekindled.embers.api.projectile.IProjectileEffect;
import com.rekindled.embers.api.projectile.IProjectilePreset;
import com.rekindled.embers.api.projectile.ProjectileFireball;
import com.rekindled.embers.api.projectile.ProjectileRay;
import com.rekindled.embers.util.Misc;
import java.util.ListIterator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DiffractionBarrelAugment extends AugmentBase {

    public DiffractionBarrelAugment(ResourceLocation id) {
        super(id, 2.0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onProjectileFire(EmberProjectileEvent event) {
        ListIterator<IProjectilePreset> projectiles = event.getProjectiles().listIterator();
        ItemStack weapon = event.getStack();
        if (!weapon.isEmpty() && AugmentUtil.hasHeat(weapon)) {
            int level = AugmentUtil.getAugmentLevel(weapon, this);
            if (level > 0) {
                while (projectiles.hasNext()) {
                    IProjectilePreset projectile = (IProjectilePreset) projectiles.next();
                    Vec3 velocity = projectile.getVelocity();
                    double speed = velocity.length();
                    int bullets = 3 + level;
                    IProjectileEffect effect = projectile.getEffect();
                    if (projectile instanceof ProjectileRay) {
                        double newspeed = 1.0;
                        this.adjustEffect(effect, 0.3333333333333333);
                        projectiles.remove();
                        for (int i = 0; i < bullets; i++) {
                            double spread = 0.1 * (double) level;
                            Vec3 newVelocity = velocity.add((Misc.random.nextDouble() - 0.5) * speed * 2.0 * spread, (Misc.random.nextDouble() - 0.5) * speed * 2.0 * spread, (Misc.random.nextDouble() - 0.5) * speed * 2.0 * spread).scale(newspeed / speed);
                            projectiles.add(new ProjectileFireball(projectile.getShooter(), projectile.getPos(), newVelocity, 2.4, 80, effect));
                        }
                    } else if (projectile instanceof ProjectileFireball fireball) {
                        this.adjustEffect(effect, 0.3333333333333333);
                        projectiles.remove();
                        for (int i = 0; i < bullets; i++) {
                            double spread = 0.1 * (double) level;
                            Vec3 newVelocity = velocity.add((Misc.random.nextDouble() - 0.5) * speed * 2.0 * spread, (Misc.random.nextDouble() - 0.5) * speed * 2.0 * spread, (Misc.random.nextDouble() - 0.5) * speed * 2.0 * spread);
                            projectiles.add(new ProjectileFireball(projectile.getShooter(), projectile.getPos(), newVelocity, fireball.getSize() / 3.0, fireball.getLifetime() / 2, effect));
                        }
                    }
                }
            }
        }
    }

    private void adjustEffect(IProjectileEffect effect, double multiplier) {
        if (effect instanceof EffectArea areaEffect) {
            this.adjustEffect(areaEffect.getEffect(), multiplier);
        } else if (effect instanceof EffectMulti) {
            for (IProjectileEffect subEffect : ((EffectMulti) effect).getEffects()) {
                this.adjustEffect(subEffect, multiplier);
            }
        } else if (effect instanceof EffectDamage damageEffect) {
            damageEffect.setDamage((float) ((double) damageEffect.getDamage() * multiplier));
            damageEffect.setInvinciblityMultiplier(0.0);
        }
    }
}