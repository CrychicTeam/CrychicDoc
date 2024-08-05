package dev.xkmc.l2backpack.content.capability;

import dev.xkmc.l2backpack.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

public enum PickupMode {

    NONE(LangData.IDS.MODE_NONE), STACKING(LangData.IDS.MODE_STACKING), ALL(LangData.IDS.MODE_ALL);

    private final LangData.IDS lang;

    private PickupMode(LangData.IDS lang) {
        this.lang = lang;
    }

    public MutableComponent getTooltip() {
        return this.lang.get().withStyle(ChatFormatting.AQUA);
    }
}