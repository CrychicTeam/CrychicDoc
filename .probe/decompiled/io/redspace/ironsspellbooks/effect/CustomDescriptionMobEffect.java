package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public abstract class CustomDescriptionMobEffect extends MagicMobEffect {

    protected CustomDescriptionMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static void handleCustomPotionTooltip(ItemStack itemStack, List<Component> tooltipLines, boolean isAdvanced, MobEffectInstance mobEffectInstance, CustomDescriptionMobEffect customDescriptionMobEffect) {
        Component description = customDescriptionMobEffect.getDescriptionLine(mobEffectInstance);
        MutableComponent header = Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE);
        ArrayList<Component> newLines = new ArrayList();
        int i = TooltipsUtils.indexOfComponent(tooltipLines, "potion.whenDrank");
        if (i < 0) {
            newLines.add(Component.empty());
            newLines.add(header);
            newLines.add(description);
            i = isAdvanced ? TooltipsUtils.indexOfAdvancedText(tooltipLines, itemStack) : tooltipLines.size();
        } else {
            newLines.add(description);
            i++;
        }
        tooltipLines.addAll(i, newLines);
    }

    public abstract Component getDescriptionLine(MobEffectInstance var1);
}