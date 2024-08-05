package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class SwitchMinigameTask extends LongRunningTask {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final long worldId;

    private final WorldTemplate worldTemplate;

    private final RealmsConfigureWorldScreen lastScreen;

    public SwitchMinigameTask(long long0, WorldTemplate worldTemplate1, RealmsConfigureWorldScreen realmsConfigureWorldScreen2) {
        this.worldId = long0;
        this.worldTemplate = worldTemplate1;
        this.lastScreen = realmsConfigureWorldScreen2;
    }

    public void run() {
        RealmsClient $$0 = RealmsClient.create();
        this.m_90409_(Component.translatable("mco.minigame.world.starting.screen.title"));
        for (int $$1 = 0; $$1 < 25; $$1++) {
            try {
                if (this.m_90411_()) {
                    return;
                }
                if ($$0.putIntoMinigameMode(this.worldId, this.worldTemplate.id)) {
                    m_90405_(this.lastScreen);
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
                LOGGER.error("Couldn't start mini game!");
                this.m_87791_(var5.toString());
            }
        }
    }
}