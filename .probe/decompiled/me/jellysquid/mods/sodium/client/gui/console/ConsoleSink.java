package me.jellysquid.mods.sodium.client.gui.console;

import me.jellysquid.mods.sodium.client.gui.console.message.MessageLevel;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public interface ConsoleSink {

    void logMessage(@NotNull MessageLevel var1, @NotNull Component var2, double var3);
}