package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class OpenServerTask extends LongRunningTask {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final RealmsServer serverData;

    private final Screen returnScreen;

    private final boolean join;

    private final RealmsMainScreen mainScreen;

    private final Minecraft minecraft;

    public OpenServerTask(RealmsServer realmsServer0, Screen screen1, RealmsMainScreen realmsMainScreen2, boolean boolean3, Minecraft minecraft4) {
        this.serverData = realmsServer0;
        this.returnScreen = screen1;
        this.join = boolean3;
        this.mainScreen = realmsMainScreen2;
        this.minecraft = minecraft4;
    }

    public void run() {
        this.m_90409_(Component.translatable("mco.configure.world.opening"));
        RealmsClient $$0 = RealmsClient.create();
        for (int $$1 = 0; $$1 < 25; $$1++) {
            if (this.m_90411_()) {
                return;
            }
            try {
                boolean $$2 = $$0.open(this.serverData.id);
                if ($$2) {
                    this.minecraft.execute(() -> {
                        if (this.returnScreen instanceof RealmsConfigureWorldScreen) {
                            ((RealmsConfigureWorldScreen) this.returnScreen).stateChanged();
                        }
                        this.serverData.state = RealmsServer.State.OPEN;
                        if (this.join) {
                            this.mainScreen.play(this.serverData, this.returnScreen);
                        } else {
                            this.minecraft.setScreen(this.returnScreen);
                        }
                    });
                    break;
                }
            } catch (RetryCallException var4) {
                if (this.m_90411_()) {
                    return;
                }
                m_167655_((long) var4.delaySeconds);
            } catch (Exception var5) {
                if (this.m_90411_()) {
                    return;
                }
                LOGGER.error("Failed to open server", var5);
                this.m_87791_("Failed to open the server");
            }
        }
    }
}