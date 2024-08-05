package com.mna.api.items;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public interface IRelic {

    @Nullable
    default Component getHoverAddition(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        return Component.translatable("item.mna.relic").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD);
    }
}