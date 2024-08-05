package com.mna.items.artifice.charms;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemCancelFatalFallCharm extends CharmBaseItem {

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.mna.fall_charm.flavor").withStyle(ChatFormatting.ITALIC));
        super.m_7373_(stack, world, tooltip, tooltipFlag);
    }
}