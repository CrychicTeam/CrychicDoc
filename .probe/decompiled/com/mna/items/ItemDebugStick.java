package com.mna.items;

import com.mna.entities.boss.attacks.OrangeChickenProjectile;
import com.mna.items.base.INoCreativeTab;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemDebugStick extends Item implements INoCreativeTab {

    public ItemDebugStick() {
        super(new Item.Properties().stacksTo(1).setNoRepair());
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1000;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (!world.isClientSide) {
            OrangeChickenProjectile ocp = new OrangeChickenProjectile(world, player);
            ocp.m_6686_(0.0, 0.0, 0.0, 1.0F, 0.0F);
            ocp.m_146884_(player.m_146892_());
            world.m_7967_(ocp);
        }
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity player, ItemStack stack, int count) {
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }
}