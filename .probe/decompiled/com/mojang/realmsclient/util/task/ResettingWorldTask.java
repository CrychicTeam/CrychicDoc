package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public abstract class ResettingWorldTask extends LongRunningTask {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final long serverId;

    private final Component title;

    private final Runnable callback;

    public ResettingWorldTask(long long0, Component component1, Runnable runnable2) {
        this.serverId = long0;
        this.title = component1;
        this.callback = runnable2;
    }

    protected abstract void sendResetRequest(RealmsClient var1, long var2) throws RealmsServiceException;

    public void run() {
        RealmsClient $$0 = RealmsClient.create();
        this.m_90409_(this.title);
        int $$1 = 0;
        while ($$1 < 25) {
            try {
                if (this.m_90411_()) {
                    return;
                }
                this.sendResetRequest($$0, this.serverId);
                if (this.m_90411_()) {
                    return;
                }
                this.callback.run();
                return;
            } catch (RetryCallException var4) {
                if (this.m_90411_()) {
                    return;
                }
                m_167655_((long) var4.delaySeconds);
                $$1++;
            } catch (Exception var5) {
                if (this.m_90411_()) {
                    return;
                }
                LOGGER.error("Couldn't reset world");
                this.m_87791_(var5.toString());
                return;
            }
        }
    }
}