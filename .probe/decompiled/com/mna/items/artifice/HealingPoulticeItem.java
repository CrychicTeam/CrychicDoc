package com.mna.items.artifice;

import com.mna.api.items.TieredItem;
import com.mna.effects.EffectInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class HealingPoulticeItem extends TieredItem {

    public HealingPoulticeItem() {
        super(new Item.Properties().stacksTo(16));
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 20;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_77661_1_) {
        return UseAnim.BOW;
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity living) {
        living.getPersistentData().remove("bind_wounds_last_pos");
        if (world.isClientSide) {
            return stack;
        } else {
            stack.shrink(1);
            if (living instanceof Player) {
                ((Player) living).getCooldowns().addCooldown(this, 40);
            }
            living.addEffect(new MobEffectInstance(EffectInit.BIND_WOUNDS.get(), 600, 0));
            return stack;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        player.m_6672_(hand);
        return InteractionResultHolder.consume(itemstack);
    }
}