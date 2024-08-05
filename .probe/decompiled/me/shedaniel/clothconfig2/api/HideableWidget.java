package me.shedaniel.clothconfig2.api;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public interface HideableWidget {

    boolean isDisplayed();

    void setDisplayRequirement(@Nullable Requirement var1);

    @Nullable
    Requirement getDisplayRequirement();
}