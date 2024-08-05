package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.gui.ErrorCallback;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public abstract class LongRunningTask implements ErrorCallback, Runnable {

    protected static final int NUMBER_OF_RETRIES = 25;

    private static final Logger LOGGER = LogUtils.getLogger();

    protected RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen;

    protected static void pause(long long0) {
        try {
            Thread.sleep(long0 * 1000L);
        } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
            LOGGER.error("", var3);
        }
    }

    public static void setScreen(Screen screen0) {
        Minecraft $$1 = Minecraft.getInstance();
        $$1.execute(() -> $$1.setScreen(screen0));
    }

    public void setScreen(RealmsLongRunningMcoTaskScreen realmsLongRunningMcoTaskScreen0) {
        this.longRunningMcoTaskScreen = realmsLongRunningMcoTaskScreen0;
    }

    @Override
    public void error(Component component0) {
        this.longRunningMcoTaskScreen.error(component0);
    }

    public void setTitle(Component component0) {
        this.longRunningMcoTaskScreen.setTitle(component0);
    }

    public boolean aborted() {
        return this.longRunningMcoTaskScreen.aborted();
    }

    public void tick() {
    }

    public void init() {
    }

    public void abortTask() {
    }
}