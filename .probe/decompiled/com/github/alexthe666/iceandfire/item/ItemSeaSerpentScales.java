package com.github.alexthe666.iceandfire.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemSeaSerpentScales extends ItemGeneric {

    private final ChatFormatting color;

    private final String colorName;

    public ItemSeaSerpentScales(String colorName, ChatFormatting color) {
        this.color = color;
        this.colorName = colorName;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("sea_serpent." + this.colorName).withStyle(this.color));
    }
}