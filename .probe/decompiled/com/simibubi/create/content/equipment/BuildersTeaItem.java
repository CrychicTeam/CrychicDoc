package com.simibubi.create.content.equipment;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class BuildersTeaItem extends Item {

    public BuildersTeaItem(Item.Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        Player playerentity = entity instanceof Player ? (Player) entity : null;
        if (playerentity instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) playerentity, stack);
        }
        if (!world.isClientSide) {
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 3600, 0, false, false, false));
        }
        if (playerentity != null) {
            playerentity.awardStat(Stats.ITEM_USED.get(this));
            playerentity.getFoodData().eat(1, 0.6F);
            if (!playerentity.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        if (playerentity == null || !playerentity.getAbilities().instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if (playerentity != null) {
                playerentity.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 42;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_77661_1_) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_77659_1_, Player p_77659_2_, InteractionHand p_77659_3_) {
        p_77659_2_.m_6672_(p_77659_3_);
        return InteractionResultHolder.success(p_77659_2_.m_21120_(p_77659_3_));
    }
}