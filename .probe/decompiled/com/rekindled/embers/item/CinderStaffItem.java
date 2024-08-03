package com.rekindled.embers.item;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.api.event.EmberProjectileEvent;
import com.rekindled.embers.api.event.ItemVisualEvent;
import com.rekindled.embers.api.item.IProjectileWeapon;
import com.rekindled.embers.api.projectile.EffectArea;
import com.rekindled.embers.api.projectile.EffectDamage;
import com.rekindled.embers.api.projectile.IProjectilePreset;
import com.rekindled.embers.api.projectile.ProjectileFireball;
import com.rekindled.embers.damage.DamageEmber;
import com.rekindled.embers.datagen.EmbersDamageTypes;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.util.EmberInventoryUtil;
import com.rekindled.embers.util.Misc;
import java.awt.Color;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.joml.Vector3f;

public class CinderStaffItem extends Item implements IProjectileWeapon {

    public static boolean soundPlaying = false;

    public static Random rand = new Random();

    public CinderStaffItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!level.isClientSide) {
            double charge = (double) Math.min(ConfigManager.CINDER_STAFF_MAX_CHARGE.get(), this.getUseDuration(stack) - timeLeft) / (double) ConfigManager.CINDER_STAFF_MAX_CHARGE.get().intValue();
            float spawnDistance = 2.0F;
            Vec3 eyesPos = entity.m_146892_();
            HitResult traceResult = m_41435_(entity.m_9236_(), (Player) entity, ClipContext.Fluid.NONE);
            if (traceResult.getType() == HitResult.Type.BLOCK) {
                spawnDistance = (float) Math.min((double) spawnDistance, traceResult.getLocation().distanceTo(eyesPos));
            }
            Vec3 launchPos = eyesPos.add(entity.m_20154_().scale((double) spawnDistance));
            float damage = (float) Math.max(charge * ConfigManager.CINDER_STAFF_DAMAGE.get(), 0.5);
            float size = (float) Math.max(charge * ConfigManager.CINDER_STAFF_SIZE.get(), 0.5);
            float aoeSize = (float) (charge * ConfigManager.CINDER_STAFF_AOE_SIZE.get());
            int lifetime = charge >= 0.06 ? ConfigManager.CINDER_STAFF_LIFETIME.get() : 5;
            Function<Entity, DamageSource> damageSource = e -> new DamageEmber(((Registry) level.registryAccess().registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(EmbersDamageTypes.EMBER_KEY), e, entity);
            EffectArea effect = new EffectArea(new EffectDamage(damage, damageSource, 1, 1.0), (double) aoeSize, false);
            ProjectileFireball fireball = new ProjectileFireball(entity, launchPos, entity.m_20154_().scale(0.85), (double) size, lifetime, effect);
            EmberProjectileEvent event = new EmberProjectileEvent(entity, stack, charge, fireball);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                for (IProjectilePreset projectile : event.getProjectiles()) {
                    projectile.shoot(level);
                }
            }
            SoundEvent sound;
            if (charge * ConfigManager.CINDER_STAFF_DAMAGE.get() >= 10.0) {
                sound = EmbersSounds.FIREBALL_BIG.get();
            } else if (charge * ConfigManager.CINDER_STAFF_DAMAGE.get() >= 1.0) {
                sound = EmbersSounds.FIREBALL.get();
            } else {
                sound = EmbersSounds.CINDER_STAFF_FAIL.get();
            }
            level.playSound(null, launchPos.x, launchPos.y, launchPos.z, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        stack.getOrCreateTag().putLong("lastUse", level.getGameTime());
        entity.swing(entity.getUsedItemHand());
        entity.stopUsingItem();
    }

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack stack, int count) {
        if (stack.hasTag() && stack.getTag().getLong("lastUse") + (long) ConfigManager.CINDER_STAFF_COOLDOWN.get().intValue() > level.getGameTime() && (!(player instanceof Player) || !((Player) player).isCreative())) {
            player.stopUsingItem();
        }
        double charge = (double) Math.min(ConfigManager.CINDER_STAFF_MAX_CHARGE.get(), this.getUseDuration(stack) - count) / (double) ConfigManager.CINDER_STAFF_MAX_CHARGE.get().intValue();
        boolean fullCharge = charge >= 1.0;
        ItemVisualEvent event = new ItemVisualEvent(player, Misc.handToSlot(player.getUsedItemHand()), stack, new Color(255, 64, 16), fullCharge ? EmbersSounds.CINDER_STAFF_LOOP.get() : null, 1.0F, 1.0F, "charge");
        MinecraftForge.EVENT_BUS.post(event);
        if (event.hasSound()) {
            if (!soundPlaying) {
                if (level.isClientSide()) {
                    EmbersSounds.playItemSoundClient(player, this, event.getSound(), SoundSource.PLAYERS, true, event.getVolume(), event.getPitch());
                }
                soundPlaying = true;
            }
        } else {
            soundPlaying = false;
        }
        if (event.hasParticles()) {
            Color color = event.getColor();
            float spawnDistance = 2.0F;
            Vec3 eyesPos = player.m_146892_();
            HitResult traceResult = m_41435_(level, (Player) player, ClipContext.Fluid.NONE);
            if (traceResult.getType() == HitResult.Type.BLOCK) {
                spawnDistance = (float) Math.min((double) spawnDistance, traceResult.getLocation().distanceTo(eyesPos));
            }
            Vec3 launchPos = eyesPos.add(player.m_20154_().scale((double) spawnDistance));
            GlowParticleOptions options = new GlowParticleOptions(new Vector3f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F), (float) (charge * ConfigManager.CINDER_STAFF_SIZE.get() / 2.0), 24);
            for (int i = 0; i < 4; i++) {
                level.addParticle(options, (double) ((float) launchPos.x + (rand.nextFloat() * 0.1F - 0.05F)), (double) ((float) launchPos.y + (rand.nextFloat() * 0.1F - 0.05F)), (double) ((float) launchPos.z + (rand.nextFloat() * 0.1F - 0.05F)), 0.0, 1.0E-6, 0.0);
            }
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.matches(oldStack, newStack);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (stack.hasTag() && stack.getTag().getLong("lastUse") + (long) ConfigManager.CINDER_STAFF_COOLDOWN.get().intValue() > level.getGameTime() && !player.isCreative()) {
            return InteractionResultHolder.pass(stack);
        } else if (!(EmberInventoryUtil.getEmberTotal(player) >= ConfigManager.CINDER_STAFF_COST.get()) && !player.isCreative()) {
            return InteractionResultHolder.fail(stack);
        } else {
            EmberInventoryUtil.removeEmber(player, ConfigManager.CINDER_STAFF_COST.get());
            player.m_6672_(hand);
            if (level.isClientSide()) {
                EmbersSounds.playItemSoundClient(player, this, EmbersSounds.CINDER_STAFF_CHARGE.get(), SoundSource.PLAYERS, false, 1.0F, 1.0F);
            } else {
                EmbersSounds.playItemSound(player, this, EmbersSounds.CINDER_STAFF_CHARGE.get(), SoundSource.PLAYERS, false, 1.0F, 1.0F);
            }
            return InteractionResultHolder.consume(stack);
        }
    }
}