package com.github.alexthe666.iceandfire.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemGeneric extends Item {

    int description = 0;

    public ItemGeneric() {
        super(new Item.Properties());
    }

    public ItemGeneric(int textLength) {
        super(new Item.Properties());
        this.description = textLength;
    }

    public ItemGeneric(int textLength, boolean hide) {
        super(new Item.Properties());
        this.description = textLength;
    }

    public ItemGeneric(int textLength, int stacksize) {
        super(new Item.Properties().stacksTo(stacksize));
        this.description = textLength;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return this == IafItemRegistry.CREATIVE_DRAGON_MEAL.get() ? true : super.isFoil(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (this.description > 0) {
            for (int i = 0; i < this.description; i++) {
                tooltip.add(Component.translatable(this.m_5524_() + ".desc_" + i).withStyle(ChatFormatting.GRAY));
            }
        }
    }
}