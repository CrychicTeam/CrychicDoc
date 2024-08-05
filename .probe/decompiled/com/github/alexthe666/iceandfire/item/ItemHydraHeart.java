package com.github.alexthe666.iceandfire.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemHydraHeart extends Item {

    public ItemHydraHeart() {
        super(new Item.Properties().stacksTo(1));
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player && itemSlot >= 0 && itemSlot <= 8) {
            double healthPercentage = (double) (((Player) entity).m_21223_() / Math.max(1.0F, ((Player) entity).m_21233_()));
            if (healthPercentage < 1.0) {
                int level = 0;
                if (healthPercentage < 0.25) {
                    level = 3;
                } else if (healthPercentage < 0.5) {
                    level = 2;
                } else if (healthPercentage < 0.75) {
                    level = 1;
                }
                if (!((Player) entity).m_21023_(MobEffects.REGENERATION) || ((Player) entity).m_21124_(MobEffects.REGENERATION).getAmplifier() < level) {
                    ((Player) entity).m_7292_(new MobEffectInstance(MobEffects.REGENERATION, 900, level, true, false));
                }
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.hydra_heart.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.hydra_heart.desc_1").withStyle(ChatFormatting.GRAY));
    }
}