package io.github.apace100.origins.origin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum Impact {

    NONE(0, "none", ChatFormatting.GRAY), LOW(1, "low", ChatFormatting.GREEN), MEDIUM(2, "medium", ChatFormatting.YELLOW), HIGH(3, "high", ChatFormatting.RED);

    private final int impactValue;

    private final String translationKey;

    private final ChatFormatting textStyle;

    private Impact(int impactValue, String translationKey, ChatFormatting textStyle) {
        this.translationKey = "origins.gui.impact." + translationKey;
        this.impactValue = impactValue;
        this.textStyle = textStyle;
    }

    public int getImpactValue() {
        return this.impactValue;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public ChatFormatting getTextStyle() {
        return this.textStyle;
    }

    public MutableComponent getTextComponent() {
        return Component.translatable(this.getTranslationKey()).withStyle(this.getTextStyle());
    }

    public static Impact getByValue(int impactValue) {
        return values()[impactValue];
    }
}