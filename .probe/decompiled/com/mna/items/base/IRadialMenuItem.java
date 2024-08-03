package com.mna.items.base;

import com.mna.KeybindInit;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public interface IRadialMenuItem {

    default void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        String txt = I18n.get(((KeyMapping) KeybindInit.RadialMenuOpen.get()).getKey().getDisplayName().getString());
        tooltip.add(Component.translatable("item.mna.item-with-gui.radial-open", txt).withStyle(ChatFormatting.AQUA));
    }
}