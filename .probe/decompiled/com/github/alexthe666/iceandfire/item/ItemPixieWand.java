package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityPixieCharge;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemPixieWand extends Item {

    public ItemPixieWand() {
        super(new Item.Properties().stacksTo(1).durability(500));
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand hand) {
        ItemStack itemStackIn = playerIn.m_21120_(hand);
        boolean flag = playerIn.isCreative() || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, itemStackIn) > 0;
        ItemStack itemstack = this.findAmmo(playerIn);
        playerIn.m_6672_(hand);
        playerIn.m_6674_(hand);
        if (!itemstack.isEmpty() || flag) {
            boolean flag1 = playerIn.isCreative() || this.isInfinite(itemstack, itemStackIn, playerIn);
            if (!flag1) {
                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    playerIn.getInventory().removeItem(itemstack);
                }
            }
            double d2 = playerIn.m_20154_().x;
            double d3 = playerIn.m_20154_().y;
            double d4 = playerIn.m_20154_().z;
            float inaccuracy = 1.0F;
            d2 += playerIn.m_217043_().nextGaussian() * 0.0075F * (double) inaccuracy;
            d3 += playerIn.m_217043_().nextGaussian() * 0.0075F * (double) inaccuracy;
            d4 += playerIn.m_217043_().nextGaussian() * 0.0075F * (double) inaccuracy;
            EntityPixieCharge charge = new EntityPixieCharge(IafEntityRegistry.PIXIE_CHARGE.get(), worldIn, playerIn, d2, d3, d4);
            charge.m_6034_(playerIn.m_20185_(), playerIn.m_20186_() + 1.0, playerIn.m_20189_());
            if (!worldIn.isClientSide) {
                worldIn.m_7967_(charge);
            }
            playerIn.playSound(IafSoundRegistry.PIXIE_WAND, 1.0F, 0.75F + 0.5F * playerIn.m_217043_().nextFloat());
            itemstack.hurtAndBreak(1, playerIn, player -> player.m_21190_(playerIn.m_7655_()));
            playerIn.getCooldowns().addCooldown(this, 5);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        int enchant = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow);
        return enchant > 0 && stack.getItem() == IafItemRegistry.PIXIE_DUST.get();
    }

    private ItemStack findAmmo(Player player) {
        if (this.isAmmo(player.m_21120_(InteractionHand.OFF_HAND))) {
            return player.m_21120_(InteractionHand.OFF_HAND);
        } else if (this.isAmmo(player.m_21120_(InteractionHand.MAIN_HAND))) {
            return player.m_21120_(InteractionHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack itemstack = player.getInventory().getItem(i);
                if (this.isAmmo(itemstack)) {
                    return itemstack;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    protected boolean isAmmo(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == IafItemRegistry.PIXIE_DUST.get();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.pixie_wand.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.pixie_wand.desc_1").withStyle(ChatFormatting.GRAY));
    }
}