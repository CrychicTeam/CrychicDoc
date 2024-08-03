package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class CloseServerTask extends LongRunningTask {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final RealmsServer serverData;

    private final RealmsConfigureWorldScreen configureScreen;

    public CloseServerTask(RealmsServer realmsServer0, RealmsConfigureWorldScreen realmsConfigureWorldScreen1) {
        this.serverData = realmsServer0;
        this.configureScreen = realmsConfigureWorldScreen1;
    }

    public void run() {
        this.m_90409_(Component.translatable("mco.configure.world.closing"));
        RealmsClient $$0 = RealmsClient.create();
        for (int $$1 = 0; $$1 < 25; $$1++) {
            if (this.m_90411_()) {
                return;
            }
            try {
                boolean $$2 = $$0.close(this.serverData.id);
                if ($$2) {
                    this.configureScreen.stateChanged();
                    this.serverData.state = RealmsServer.State.CLOSED;
                    m_90405_(this.configureScreen);
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
                LOGGER.error("Failed to close server", var5);
                this.m_87791_("Failed to close the server");
            }
        }
    }
}