package com.rekindled.embers.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class FluidAmounts {

    public static final int NUGGET_AMOUNT = 10;

    public static final int INGOT_AMOUNT = 90;

    public static final int BLOCK_AMOUNT = 810;

    public static final int RAW_AMOUNT = 120;

    public static final int ORE_AMOUNT = 240;

    public static final int RAW_BLOCK_AMOUNT = 1080;

    public static final int PLATE_AMOUNT = 90;

    public static final int GEAR_AMOUNT = 180;

    public static MutableComponent getIngotTooltip(int amount) {
        MutableComponent tooltip = null;
        MutableComponent ingots = null;
        MutableComponent nuggets = null;
        MutableComponent mb = null;
        if (amount >= 90) {
            int count = amount / 90;
            if (count == 1) {
                ingots = Component.translatable("embers.tooltip.fluiddial.ingot");
            } else {
                ingots = Component.translatable("embers.tooltip.fluiddial.ingots", count);
            }
        }
        if (amount % 90 >= 10) {
            int count = amount % 90 / 10;
            if (count == 1) {
                nuggets = Component.translatable("embers.tooltip.fluiddial.nugget");
            } else {
                nuggets = Component.translatable("embers.tooltip.fluiddial.nuggets", count);
            }
        }
        if (amount % 10 > 0) {
            int count = amount % 10;
            mb = Component.translatable("embers.tooltip.fluiddial.millibucket", count);
        }
        if (ingots == null && nuggets == null) {
            return Component.empty();
        } else {
            if (ingots != null) {
                tooltip = ingots;
            }
            if (nuggets != null) {
                if (tooltip == null) {
                    tooltip = nuggets;
                } else {
                    tooltip = Component.translatable("embers.tooltip.fluiddial.separator", tooltip, nuggets);
                }
            }
            if (mb != null) {
                tooltip = Component.translatable("embers.tooltip.fluiddial.separator", tooltip, mb);
            }
            return tooltip;
        }
    }
}