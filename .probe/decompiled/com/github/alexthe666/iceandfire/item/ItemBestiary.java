package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.google.common.primitives.Ints;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemBestiary extends Item {

    public ItemBestiary() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void onCraftedBy(ItemStack stack, @NotNull Level worldIn, @NotNull Player playerIn) {
        stack.setTag(new CompoundTag());
        stack.getTag().putIntArray("Pages", new int[] { 0 });
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.m_21120_(handIn);
        if (worldIn.isClientSide) {
            IceAndFire.PROXY.openBestiaryGui(itemStackIn);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
            stack.getTag().putIntArray("Pages", new int[] { EnumBestiaryPages.INTRODUCTION.ordinal() });
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (stack.getTag() != null) {
            if (IceAndFire.PROXY.shouldSeeBestiaryContents()) {
                tooltip.add(Component.translatable("bestiary.contains").withStyle(ChatFormatting.GRAY));
                for (EnumBestiaryPages page : EnumBestiaryPages.containedPages(Ints.asList(stack.getTag().getIntArray("Pages")))) {
                    tooltip.add(Component.literal(ChatFormatting.WHITE + "-").append(Component.translatable("bestiary." + EnumBestiaryPages.values()[page.ordinal()].toString().toLowerCase())).withStyle(ChatFormatting.GRAY));
                }
            } else {
                tooltip.add(Component.translatable("bestiary.hold_shift").withStyle(ChatFormatting.GRAY));
            }
        }
    }
}