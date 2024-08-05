package com.github.einjerjar.mc.keymap.keys.wrappers.categories;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

public class AllCategory implements CategoryHolder {

    protected static final String CAT_ALL = "keymap.listCatAll";

    protected final Component translatedName = Component.translatable("keymap.listCatAll");

    @Override
    public String getTranslatableName() {
        return "keymap.listCatAll";
    }

    @Override
    public Component getTranslatedName() {
        return this.translatedName;
    }

    @Override
    public String getModName() {
        return Language.getInstance().getOrDefault("keymap.listCatAll");
    }

    @Override
    public String getFilterSlug() {
        return "";
    }
}