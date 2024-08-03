package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldDownload;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class DownloadTask extends LongRunningTask {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final long worldId;

    private final int slot;

    private final Screen lastScreen;

    private final String downloadName;

    public DownloadTask(long long0, int int1, String string2, Screen screen3) {
        this.worldId = long0;
        this.slot = int1;
        this.lastScreen = screen3;
        this.downloadName = string2;
    }

    public void run() {
        this.m_90409_(Component.translatable("mco.download.preparing"));
        RealmsClient $$0 = RealmsClient.create();
        int $$1 = 0;
        while ($$1 < 25) {
            try {
                if (this.m_90411_()) {
                    return;
                }
                WorldDownload $$2 = $$0.requestDownloadInfo(this.worldId, this.slot);
                m_167655_(1L);
                if (this.m_90411_()) {
                    return;
                }
                m_90405_(new RealmsDownloadLatestWorldScreen(this.lastScreen, $$2, this.downloadName, p_90325_ -> {
                }));
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
                LOGGER.error("Couldn't download world data");
                m_90405_(new RealmsGenericErrorScreen(var5, this.lastScreen));
                return;
            } catch (Exception var6) {
                if (this.m_90411_()) {
                    return;
                }
                LOGGER.error("Couldn't download world data", var6);
                this.m_87791_(var6.getLocalizedMessage());
                return;
            }
        }
    }
}