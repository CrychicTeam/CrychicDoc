package me.jellysquid.mods.sodium.client.gui.prompt;

import me.jellysquid.mods.sodium.client.util.Dim2i;
import org.jetbrains.annotations.Nullable;

@Deprecated(forRemoval = true)
public interface ScreenPromptable {

    void setPrompt(@Nullable ScreenPrompt var1);

    @Nullable
    ScreenPrompt getPrompt();

    Dim2i getDimensions();
}