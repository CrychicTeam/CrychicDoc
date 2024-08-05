package com.github.einjerjar.mc.keymap.keys.wrappers.keys;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import java.util.List;
import net.minecraft.network.chat.Component;

public interface KeyHolder {

    List<Integer> getCode();

    Integer getSingleCode();

    Integer getKeyHash();

    boolean isComplex();

    KeyComboData getComplexCode();

    String getTranslatableName();

    String getCategory();

    Component getTranslatedName();

    String getTranslatableKey();

    Component getTranslatedKey();

    String getSearchString();

    boolean setKey(List<Integer> var1, boolean var2);

    String getModName();

    boolean resetKey();

    boolean isAssigned();
}