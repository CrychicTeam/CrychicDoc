package com.github.einjerjar.mc.keymap.keys.wrappers.categories;

import java.util.List;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

public class VanillaCategory implements CategoryHolder {

    public static final List<String> MC_CATEGORIES = List.of("key.categories.movement", "key.categories.misc", "key.categories.multiplayer", "key.categories.gameplay", "key.categories.ui", "key.categories.inventory", "key.categories.creative");

    protected final String category;

    protected final Component translatedName;

    public VanillaCategory(String category) {
        this.category = category;
        this.translatedName = Component.translatable(category);
    }

    @Override
    public String getTranslatableName() {
        return this.category;
    }

    @Override
    public Component getTranslatedName() {
        return this.translatedName;
    }

    @Override
    public String getModName() {
        String s = this.category;
        if (MC_CATEGORIES.contains(this.category)) {
            s = "advancements.story.root.title";
        }
        return Language.getInstance().getOrDefault(s);
    }

    @Override
    public String getFilterSlug() {
        return String.format("@%s", Language.getInstance().getOrDefault(this.category));
    }
}