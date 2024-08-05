package net.blay09.mods.defaultoptions.api;

import net.blay09.mods.defaultoptions.DefaultOptionsHandlerException;

public interface DefaultOptionsHandler {

    String getId();

    DefaultOptionsCategory getCategory();

    DefaultOptionsLoadStage getLoadStage();

    void saveCurrentOptions();

    void saveCurrentOptionsAsDefault() throws DefaultOptionsHandlerException;

    boolean hasDefaults();

    boolean shouldLoadDefaults();

    void loadDefaults() throws DefaultOptionsHandlerException;
}