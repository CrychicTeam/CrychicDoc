package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RetryCallException;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class SwitchSlotTask extends LongRunningTask {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final long worldId;

    private final int slot;

    private final Runnable callback;

    public SwitchSlotTask(long long0, int int1, Runnable runnable2) {
        this.worldId = long0;
        this.slot = int1;
        this.callback = runnable2;
    }

    public void run() {
        RealmsClient $$0 = RealmsClient.create();
        this.m_90409_(Component.translatable("mco.minigame.world.slot.screen.title"));
        for (int $$1 = 0; $$1 < 25; $$1++) {
            try {
                if (this.m_90411_()) {
                    return;
                }
                if ($$0.switchSlot(this.worldId, this.slot)) {
                    this.callback.run();
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
                LOGGER.error("Couldn't switch world!");
                this.m_87791_(var5.toString());
            }
        }
    }
}