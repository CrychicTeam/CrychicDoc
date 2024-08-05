package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class CrockPotFoodItem extends Item {

    private final CrockPotFoodProperties foodProperties;

    public CrockPotFoodItem(CrockPotFoodProperties foodProperties) {
        super(foodProperties.itemProperties);
        this.foodProperties = foodProperties;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        super.finishUsingItem(pStack, pLevel, pLivingEntity);
        if (pLivingEntity instanceof Player player) {
            this.foodProperties.addCooldown(this, player);
        }
        this.foodProperties.hurt(pLevel, pLivingEntity);
        this.foodProperties.heal(pLevel, pLivingEntity);
        this.foodProperties.removeEffects(pLevel, pLivingEntity);
        ItemStack containerStack = this.getCraftingRemainingItem(pStack);
        if (pStack.isEmpty()) {
            return containerStack;
        } else {
            if (pLivingEntity instanceof Player player && !player.getAbilities().instabuild && !player.getInventory().add(containerStack)) {
                player.drop(containerStack, false);
            }
            return pStack;
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return this.foodProperties.getUseDuration();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return this.foodProperties.getUseAnimation();
    }

    @Override
    public SoundEvent getEatingSound() {
        return this.foodProperties.getSound();
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return this.foodProperties.getSound();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.addAll(this.foodProperties.getTooltips());
        if (pLevel != null && Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> pTooltipComponents.addAll(this.foodProperties.getEffectTooltips(foodCounter.hasEaten(this))));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}