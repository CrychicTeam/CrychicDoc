package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.util.I18nUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CollectedDustItem extends CrockPotBaseItem {

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(I18nUtils.createTooltipComponent("collected_dust").withStyle(ChatFormatting.DARK_AQUA));
        super.m_7373_(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}