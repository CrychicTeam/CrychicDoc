package me.shedaniel.clothconfig2.api;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public interface DisableableWidget {

    boolean isEnabled();

    void setRequirement(@Nullable Requirement var1);

    @Nullable
    Requirement getRequirement();
}