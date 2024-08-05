package dev.xkmc.l2backpack.content.capability;

import dev.xkmc.l2backpack.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

public enum DestroyMode {

    NONE(LangData.IDS.DESTROY_NONE, true), EXCESS(LangData.IDS.DESTROY_EXCESS, true), MATCH(LangData.IDS.DESTROY_MATCH, false), ALL(LangData.IDS.DESTROY_ALL, false);

    private final LangData.IDS lang;

    public final boolean attemptInsert;

    private DestroyMode(LangData.IDS lang, boolean attemptInsert) {
        this.lang = lang;
        this.attemptInsert = attemptInsert;
    }

    public MutableComponent getTooltip() {
        return this.lang.get().withStyle(ChatFormatting.RED);
    }
}