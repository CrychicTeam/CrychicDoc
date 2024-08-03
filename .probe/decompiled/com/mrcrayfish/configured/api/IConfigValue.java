package com.mrcrayfish.configured.api;

import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public interface IConfigValue<T> {

    T get();

    T getDefault();

    void set(T var1);

    boolean isValid(T var1);

    boolean isDefault();

    boolean isChanged();

    void restore();

    @Nullable
    Component getComment();

    @Nullable
    String getTranslationKey();

    @Nullable
    Component getValidationHint();

    String getName();

    void cleanCache();

    boolean requiresWorldRestart();

    boolean requiresGameRestart();
}