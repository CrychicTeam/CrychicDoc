package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.Backup;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class RestoreTask extends LongRunningTask {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Backup backup;

    private final long worldId;

    private final RealmsConfigureWorldScreen lastScreen;

    public RestoreTask(Backup backup0, long long1, RealmsConfigureWorldScreen realmsConfigureWorldScreen2) {
        this.backup = backup0;
        this.worldId = long1;
        this.lastScreen = realmsConfigureWorldScreen2;
    }

    public void run() {
        this.m_90409_(Component.translatable("mco.backup.restoring"));
        RealmsClient $$0 = RealmsClient.create();
        int $$1 = 0;
        while ($$1 < 25) {
            try {
                if (this.m_90411_()) {
                    return;
                }
                $$0.restoreWorld(this.worldId, this.backup.backupId);
                m_167655_(1L);
                if (this.m_90411_()) {
                    return;
                }
                m_90405_(this.lastScreen.getNewScreen());
                return;
            } catch (RetryCallException var4) {
                if (this.m_90411_()) {
                    return;
                }
                m_167655_((long) var4.delaySeconds);
                $$1++;
            } catch (RealmsServiceException var5) {
                if (this.m_90411_()) {
                    return;
                }
                LOGGER.error("Couldn't restore backup", var5);
                m_90405_(new RealmsGenericErrorScreen(var5, this.lastScreen));
                return;
            } catch (Exception var6) {
                if (this.m_90411_()) {
                    return;
                }
                LOGGER.error("Couldn't restore backup", var6);
                this.m_87791_(var6.getLocalizedMessage());
                return;
            }
        }
    }
}