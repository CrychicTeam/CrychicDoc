package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "crockpot")
public class FoodValuesTooltip {

    private static final MutableComponent DELIMITER = Component.literal(", ").withStyle(ChatFormatting.WHITE);

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (CrockPotConfigs.SHOW_FOOD_VALUES_TOOLTIP.get()) {
            Player player = event.getEntity();
            if (player != null && player.m_9236_() != null) {
                FoodValues foodValues = FoodValuesDefinition.getFoodValues(event.getItemStack(), player.m_9236_());
                if (!foodValues.isEmpty()) {
                    MutableComponent tooltip = (MutableComponent) foodValues.entrySet().stream().map(entry -> I18nUtils.createTooltipComponent("food_values", Component.translatable("item.crockpot.food_category_" + ((FoodCategory) entry.getKey()).name().toLowerCase()), entry.getValue()).withStyle(Style.EMPTY.withColor(((FoodCategory) entry.getKey()).color)).withStyle(Style.EMPTY.withColor(((FoodCategory) entry.getKey()).color))).reduce(null, (acc, foodValuesText) -> acc == null ? foodValuesText : acc.append(DELIMITER).append(foodValuesText));
                    event.getToolTip().add(tooltip);
                }
            }
        }
    }
}