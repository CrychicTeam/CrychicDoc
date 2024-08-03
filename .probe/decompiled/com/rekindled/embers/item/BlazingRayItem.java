package com.rekindled.embers.item;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.api.event.EmberProjectileEvent;
import com.rekindled.embers.api.item.IProjectileWeapon;
import com.rekindled.embers.api.projectile.EffectDamage;
import com.rekindled.embers.api.projectile.IProjectilePreset;
import com.rekindled.embers.api.projectile.ProjectileRay;
import com.rekindled.embers.damage.DamageEmber;
import com.rekindled.embers.datagen.EmbersDamageTypes;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.util.EmberInventoryUtil;
import java.util.Random;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class BlazingRayItem extends Item implements IProjectileWeapon {

    public static Random rand = new Random();

    public BlazingRayItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!level.isClientSide) {
            double charge = (double) Math.min(ConfigManager.BLAZING_RAY_MAX_CHARGE.get(), this.getUseDuration(stack) - timeLeft) / (double) ConfigManager.BLAZING_RAY_MAX_CHARGE.get().intValue();
            double handmod = entity.getUsedItemHand() == InteractionHand.MAIN_HAND ? 1.0 : -1.0;
            handmod *= entity.getMainArm() == HumanoidArm.RIGHT ? 1.0 : -1.0;
            double posX = entity.m_20185_() + entity.m_20154_().x + handmod * ((double) entity.m_20205_() / 2.0) * Math.sin(Math.toRadians((double) (-entity.getYHeadRot() - 90.0F)));
            double posY = entity.m_20186_() + (double) entity.m_20192_() - 0.2 + entity.m_20154_().y;
            double posZ = entity.m_20189_() + entity.m_20154_().z + handmod * ((double) entity.m_20205_() / 2.0) * Math.cos(Math.toRadians((double) (-entity.getYHeadRot() - 90.0F)));
            double targX = entity.m_20185_() + entity.m_20154_().x * ConfigManager.BLAZING_RAY_MAX_DISTANCE.get() + ConfigManager.BLAZING_RAY_MAX_SPREAD.get() * (1.0 - charge) * ((double) rand.nextFloat() - 0.5);
            double targY = entity.m_20186_() + entity.m_20154_().y * ConfigManager.BLAZING_RAY_MAX_DISTANCE.get() + ConfigManager.BLAZING_RAY_MAX_SPREAD.get() * (1.0 - charge) * ((double) rand.nextFloat() - 0.5);
            double targZ = entity.m_20189_() + entity.m_20154_().z * ConfigManager.BLAZING_RAY_MAX_DISTANCE.get() + ConfigManager.BLAZING_RAY_MAX_SPREAD.get() * (1.0 - charge) * ((double) rand.nextFloat() - 0.5);
            DamageSource damage = new DamageEmber(((Registry) level.registryAccess().registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(EmbersDamageTypes.EMBER_KEY), entity, true);
            EffectDamage effect = new EffectDamage(ConfigManager.BLAZING_RAY_DAMAGE.get().floatValue(), e -> damage, 1, 1.0);
            ProjectileRay ray = new ProjectileRay(entity, new Vec3(posX, posY, posZ), new Vec3(targX, targY, targZ), false, effect);
            EmberProjectileEvent event = new EmberProjectileEvent(entity, stack, charge, ray);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                for (IProjectilePreset projectile : event.getProjectiles()) {
                    projectile.shoot(level);
                }
            }
            level.playSound(null, entity, EmbersSounds.BLAZING_RAY_FIRE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            stack.getOrCreateTag().putLong("lastUse", level.getGameTime());
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
        if (stack.hasTag() && stack.getTag().getLong("lastUse") + (long) ConfigManager.BLAZING_RAY_COOLDOWN.get().intValue() > level.getGameTime() && !player.isCreative()) {
            return InteractionResultHolder.pass(stack);
        } else if (!(EmberInventoryUtil.getEmberTotal(player) >= ConfigManager.BLAZING_RAY_COST.get()) && !player.isCreative()) {
            level.playSound(null, player, EmbersSounds.BLAZING_RAY_EMPTY.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.fail(stack);
        } else {
            EmberInventoryUtil.removeEmber(player, ConfigManager.BLAZING_RAY_COST.get());
            player.m_6672_(hand);
            return InteractionResultHolder.consume(stack);
        }
    }
}