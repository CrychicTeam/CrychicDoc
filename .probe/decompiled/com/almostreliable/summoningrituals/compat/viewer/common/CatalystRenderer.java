package com.almostreliable.summoningrituals.compat.viewer.common;

import com.almostreliable.summoningrituals.util.TextUtils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class CatalystRenderer extends SizedItemRenderer {

    public CatalystRenderer(int size) {
        super(size);
    }

    @Override
    public List<Component> getTooltip(ItemStack stack, TooltipFlag tooltipFlag) {
        try {
            List<Component> tooltip = stack.getTooltipLines(this.mc.player, tooltipFlag);
            tooltip.set(0, TextUtils.translate("tooltip", "catalyst", ChatFormatting.GOLD).append(": ").append(TextUtils.colorize(((Component) tooltip.get(0)).getString(), ChatFormatting.WHITE)));
            tooltip.add(TextUtils.translate("tooltip", TextUtils.f("{}_desc", "catalyst"), ChatFormatting.GRAY));
            return tooltip;
        } catch (LinkageError | RuntimeException var4) {
            return List.of(Component.literal("Error rendering tooltip!").append(var4.getMessage()).withStyle(ChatFormatting.DARK_RED));
        }
    }
}