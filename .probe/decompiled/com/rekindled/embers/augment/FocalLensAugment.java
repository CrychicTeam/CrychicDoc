package com.rekindled.embers.augment;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.event.EmberProjectileEvent;
import com.rekindled.embers.api.projectile.IProjectilePreset;
import com.rekindled.embers.api.projectile.ProjectileFireball;
import com.rekindled.embers.api.projectile.ProjectileRay;
import java.util.ListIterator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FocalLensAugment extends AugmentBase {

    public FocalLensAugment(ResourceLocation id) {
        super(id, 10.0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onProjectileFire(EmberProjectileEvent event) {
        ListIterator<IProjectilePreset> projectiles = event.getProjectiles().listIterator();
        ItemStack weapon = event.getStack();
        if (!weapon.isEmpty() && AugmentUtil.hasHeat(weapon)) {
            int level = AugmentUtil.getAugmentLevel(weapon, this);
            int index = 0;
            int modulo = 1 + (level - 1) * 2;
            if (level > 0) {
                for (; projectiles.hasNext(); index++) {
                    IProjectilePreset projectile = (IProjectilePreset) projectiles.next();
                    if (projectile instanceof ProjectileRay) {
                        ((ProjectileRay) projectile).setPierceEntities(true);
                    } else if (projectile instanceof ProjectileFireball) {
                        ((ProjectileFireball) projectile).setHoming(level * 10, 4.0 + (double) level * 1.0, index, modulo, EntitySelector.NO_SPECTATORS.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE).and(entity -> {
                            Entity shooter = projectile.getShooter();
                            if (shooter != null && entity.isAlliedTo(shooter)) {
                                return false;
                            } else {
                                return entity instanceof Player && shooter instanceof Player && !isPVPEnabled(entity.level()) ? false : shooter != entity;
                            }
                        }));
                    }
                }
            }
        }
    }

    public static boolean isPVPEnabled(Level world) {
        MinecraftServer server = world.getServer();
        return server != null && server.isPvpAllowed() && ConfigManager.PVP_EVERYBODY_IS_ENEMY.get();
    }
}