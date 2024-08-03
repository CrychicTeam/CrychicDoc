package com.sihenzhang.crockpot.util;

import com.sihenzhang.crockpot.recipe.RangedItem;
import java.text.DecimalFormat;
import net.minecraft.util.random.WeightedEntry;

public final class StringUtils {

    public static String format(double d, String pattern) {
        return new DecimalFormat(pattern).format(d);
    }

    public static String format(float f, String pattern) {
        return new DecimalFormat(pattern).format((double) f);
    }

    public static String formatCountAndChance(WeightedEntry.Wrapper<RangedItem> weightedRangedItem, int totalWeight) {
        RangedItem rangedItem = weightedRangedItem.getData();
        float chance = (float) weightedRangedItem.getWeight().asInt() / (float) totalWeight;
        StringBuilder chanceTooltip = new StringBuilder();
        if (rangedItem.isRanged()) {
            chanceTooltip.append(rangedItem.min).append("-").append(rangedItem.max);
        } else {
            chanceTooltip.append(rangedItem.min);
        }
        chanceTooltip.append(" (").append(format(chance, "0.00%")).append(")");
        return chanceTooltip.toString();
    }
}