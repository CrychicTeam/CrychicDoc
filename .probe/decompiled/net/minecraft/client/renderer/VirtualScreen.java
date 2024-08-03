package net.minecraft.client.renderer;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.Window;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;

public final class VirtualScreen implements AutoCloseable {

    private final Minecraft minecraft;

    private final ScreenManager screenManager;

    public VirtualScreen(Minecraft minecraft0) {
        this.minecraft = minecraft0;
        this.screenManager = new ScreenManager(Monitor::new);
    }

    public Window newWindow(DisplayData displayData0, @Nullable String string1, String string2) {
        return new Window(this.minecraft, this.screenManager, displayData0, string1, string2);
    }

    public void close() {
        this.screenManager.shutdown();
    }
}