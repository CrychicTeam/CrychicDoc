package me.shedaniel.clothconfig2.api;

import me.shedaniel.math.Rectangle;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public interface ScissorsScreen {

    @Nullable
    Rectangle handleScissor(@Nullable Rectangle var1);
}