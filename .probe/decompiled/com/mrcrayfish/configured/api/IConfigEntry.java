package com.mrcrayfish.configured.api;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public interface IConfigEntry {

    List<IConfigEntry> getChildren();

    boolean isRoot();

    boolean isLeaf();

    @Nullable
    IConfigValue<?> getValue();

    String getEntryName();

    @Nullable
    Component getTooltip();

    @Nullable
    String getTranslationKey();
}