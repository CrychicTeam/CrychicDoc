package com.github.einjerjar.mc.keymap.keys.wrappers.keys;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.wrappers.categories.VanillaCategory;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

public class VanillaKeymap implements KeyHolder {

    protected final List<Integer> codes = new ArrayList();

    protected KeyMapping map;

    protected Component translatedName;

    protected Component translatedKey;

    protected boolean complex;

    protected String searchString;

    public VanillaKeymap(KeyMapping map) {
        this.map = map;
        this.translatedName = Component.translatable(map.getName());
        this.updateProps(map.key);
    }

    @Override
    public List<Integer> getCode() {
        return this.codes;
    }

    @Override
    public Integer getSingleCode() {
        return (Integer) this.codes.get(0);
    }

    @Override
    public Integer getKeyHash() {
        return Objects.hash(new Object[] { this.map.key.getValue() });
    }

    @Override
    public boolean isComplex() {
        return this.complex;
    }

    @Override
    public KeyComboData getComplexCode() {
        return null;
    }

    @Override
    public String getTranslatableName() {
        return this.map.getName();
    }

    @Override
    public String getCategory() {
        return this.map.getCategory();
    }

    @Override
    public Component getTranslatedName() {
        return this.translatedName;
    }

    @Override
    public String getTranslatableKey() {
        return this.map.key.getName();
    }

    @Override
    public Component getTranslatedKey() {
        return this.translatedKey;
    }

    @Override
    public String getSearchString() {
        return this.searchString;
    }

    @Override
    public boolean setKey(List<Integer> keys, boolean mouse) {
        if (keys != null && !keys.isEmpty()) {
            InputConstants.Type type = mouse ? InputConstants.Type.MOUSE : InputConstants.Type.KEYSYM;
            InputConstants.Key key = type.getOrCreate((Integer) keys.get(0));
            this.updateProps(key);
            Minecraft.getInstance().options.setKey(this.map, key);
            KeyMapping.resetMapping();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getModName() {
        String s = this.getCategory();
        if (VanillaCategory.MC_CATEGORIES.contains(this.getCategory())) {
            s = "advancements.story.root.title";
        }
        return Language.getInstance().getOrDefault(s);
    }

    @Override
    public boolean resetKey() {
        this.updateProps(this.map.getDefaultKey());
        return true;
    }

    @Override
    public boolean isAssigned() {
        return this.map.key.getValue() != -1 || KeymapRegistry.contains(this.map);
    }

    protected String searchableKey() {
        if (KeymapRegistry.bindMap().containsKey(this.map)) {
            KeyComboData k = (KeyComboData) KeymapRegistry.bindMap().get(this.map);
            return k.searchString();
        } else {
            return this.translatedKey.getString();
        }
    }

    protected void updateSearchString() {
        String cat = Language.getInstance().getOrDefault(this.getCategory());
        this.searchString = String.format("%s [%s] $%s {%s} #%s (%s) @%s", this.translatedName.getString(), this.searchableKey(), Utils.slugify(this.searchableKey()), this.getModName(), this.getModName(), cat, cat).toLowerCase();
    }

    public void updateProps(InputConstants.Key key) {
        this.map.setKey(key);
        this.codes.clear();
        this.codes.add(key.getValue());
        if (KeymapRegistry.bindMap().containsKey(this.map)) {
            KeyComboData k = (KeyComboData) KeymapRegistry.bindMap().get(this.map);
            String kk = k.toKeyString();
            this.translatedKey = Component.literal(kk);
        } else {
            this.translatedKey = key.getDisplayName();
        }
        this.updateSearchString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            VanillaKeymap that = (VanillaKeymap) o;
            return this.map.equals(that.map) && this.codes.equals(that.codes);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.map, this.codes });
    }

    public KeyMapping map() {
        return this.map;
    }
}