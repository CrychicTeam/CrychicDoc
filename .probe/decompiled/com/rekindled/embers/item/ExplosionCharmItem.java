package com.rekindled.embers.item;

import com.rekindled.embers.compat.curios.CuriosCompat;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExplosionCharmItem extends Item implements IEmbersCurioItem {

    public static final int COOLDOWN = 100;

    public static final int MERCY_TIME = 5;

    public static String ITEM_COOLDOWN_KEY = "embers:cooldown_time";

    public ExplosionCharmItem(Item.Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static boolean hasItemCooledDown(ItemStack stack, long time, int cooldown) {
        if (stack.hasTag()) {
            CompoundTag nbt = stack.getTag();
            if (nbt.contains(ITEM_COOLDOWN_KEY)) {
                return nbt.getLong(ITEM_COOLDOWN_KEY) + (long) cooldown < time;
            }
        }
        return true;
    }

    public static boolean hasItemMercy(ItemStack stack, long time, int mercy) {
        if (stack.hasTag()) {
            CompoundTag nbt = stack.getTag();
            if (nbt.contains(ITEM_COOLDOWN_KEY)) {
                return nbt.getLong(ITEM_COOLDOWN_KEY) + (long) mercy > time;
            }
        }
        return true;
    }

    public static ItemStack setItemCooldown(ItemStack stack, long time) {
        stack.getOrCreateTag().putLong(ITEM_COOLDOWN_KEY, time);
        return stack;
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Start event) {
        if (!event.isCanceled()) {
            Explosion explosion = event.getExplosion();
            float f3 = 4.0F;
            Vec3 explosionPos = explosion.getPosition();
            int k1 = Mth.floor(explosionPos.x - (double) f3 - 1.0);
            int l1 = Mth.floor(explosionPos.x + (double) f3 + 1.0);
            int i2 = Mth.floor(explosionPos.y - (double) f3 - 1.0);
            int i1 = Mth.floor(explosionPos.y + (double) f3 + 1.0);
            int j2 = Mth.floor(explosionPos.z - (double) f3 - 1.0);
            int j1 = Mth.floor(explosionPos.z + (double) f3 + 1.0);
            for (Entity entity : event.getLevel().getEntities((Entity) null, new AABB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1), EntitySelector.NO_SPECTATORS)) {
                if (event.isCanceled()) {
                    return;
                }
                if (entity instanceof LivingEntity) {
                    CuriosCompat.checkForCurios((LivingEntity) entity, stack -> {
                        if (stack.getItem() != this) {
                            return false;
                        } else {
                            long time = event.getLevel().getGameTime();
                            if (hasItemCooledDown(stack, time, 100) || hasItemMercy(stack, time, 5)) {
                                event.getLevel().playSound(null, explosionPos.x, explosionPos.y, explosionPos.z, EmbersSounds.EXPLOSION_CHARM_ABSORB.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                                if (event.getLevel() instanceof ServerLevel server) {
                                    server.sendParticles(GlowParticleOptions.EMBER, explosionPos.x, explosionPos.y, explosionPos.z, 3, 0.0, 0.0, 0.0, 0.1);
                                    server.sendParticles(SmokeParticleOptions.BIG_SMOKE, explosionPos.x, explosionPos.y, explosionPos.z, 20, 0.25, 0.25, 0.25, 1.0);
                                }
                                event.setCanceled(true);
                                if (hasItemCooledDown(stack, time, 100)) {
                                    event.getLevel().playSound(null, explosionPos.x, explosionPos.y, explosionPos.z, EmbersSounds.EXPLOSION_CHARM_RECHARGE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                                    setItemCooldown(stack, time);
                                }
                            }
                            return true;
                        }
                    });
                }
            }
        }
    }
}