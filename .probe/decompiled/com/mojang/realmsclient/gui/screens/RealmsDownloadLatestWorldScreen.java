package com.mojang.realmsclient.gui.screens;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.Unit;
import com.mojang.realmsclient.client.FileDownload;
import com.mojang.realmsclient.dto.WorldDownload;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import org.slf4j.Logger;

public class RealmsDownloadLatestWorldScreen extends RealmsScreen {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ReentrantLock DOWNLOAD_LOCK = new ReentrantLock();

    private static final int BAR_WIDTH = 200;

    private static final int BAR_TOP = 80;

    private static final int BAR_BOTTOM = 95;

    private static final int BAR_BORDER = 1;

    private final Screen lastScreen;

    private final WorldDownload worldDownload;

    private final Component downloadTitle;

    private final RateLimiter narrationRateLimiter;

    private Button cancelButton;

    private final String worldName;

    private final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus;

    @Nullable
    private volatile Component errorMessage;

    private volatile Component status = Component.translatable("mco.download.preparing");

    @Nullable
    private volatile String progress;

    private volatile boolean cancelled;

    private volatile boolean showDots = true;

    private volatile boolean finished;

    private volatile boolean extracting;

    @Nullable
    private Long previousWrittenBytes;

    @Nullable
    private Long previousTimeSnapshot;

    private long bytesPersSecond;

    private int animTick;

    private static final String[] DOTS = new String[] { "", ".", ". .", ". . ." };

    private int dotIndex;

    private boolean checked;

    private final BooleanConsumer callback;

    public RealmsDownloadLatestWorldScreen(Screen screen0, WorldDownload worldDownload1, String string2, BooleanConsumer booleanConsumer3) {
        super(GameNarrator.NO_TITLE);
        this.callback = booleanConsumer3;
        this.lastScreen = screen0;
        this.worldName = string2;
        this.worldDownload = worldDownload1;
        this.downloadStatus = new RealmsDownloadLatestWorldScreen.DownloadStatus();
        this.downloadTitle = Component.translatable("mco.download.title");
        this.narrationRateLimiter = RateLimiter.create(0.1F);
    }

    @Override
    public void init() {
        this.cancelButton = (Button) this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_88642_ -> {
            this.cancelled = true;
            this.backButtonClicked();
        }).bounds((this.f_96543_ - 200) / 2, this.f_96544_ - 42, 200, 20).build());
        this.checkDownloadSize();
    }

    private void checkDownloadSize() {
        if (!this.finished) {
            if (!this.checked && this.getContentLength(this.worldDownload.downloadLink) >= 5368709120L) {
                Component $$0 = Component.translatable("mco.download.confirmation.line1", Unit.humanReadable(5368709120L));
                Component $$1 = Component.translatable("mco.download.confirmation.line2");
                this.f_96541_.setScreen(new RealmsLongConfirmationScreen(p_280727_ -> {
                    this.checked = true;
                    this.f_96541_.setScreen(this);
                    this.downloadSave();
                }, RealmsLongConfirmationScreen.Type.WARNING, $$0, $$1, false));
            } else {
                this.downloadSave();
            }
        }
    }

    private long getContentLength(String string0) {
        FileDownload $$1 = new FileDownload();
        return $$1.contentLength(string0);
    }

    @Override
    public void tick() {
        super.m_86600_();
        this.animTick++;
        if (this.status != null && this.narrationRateLimiter.tryAcquire(1)) {
            Component $$0 = this.createProgressNarrationMessage();
            this.f_96541_.getNarrator().sayNow($$0);
        }
    }

    private Component createProgressNarrationMessage() {
        List<Component> $$0 = Lists.newArrayList();
        $$0.add(this.downloadTitle);
        $$0.add(this.status);
        if (this.progress != null) {
            $$0.add(Component.literal(this.progress + "%"));
            $$0.add(Component.literal(Unit.humanReadable(this.bytesPersSecond) + "/s"));
        }
        if (this.errorMessage != null) {
            $$0.add(this.errorMessage);
        }
        return CommonComponents.joinLines($$0);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.cancelled = true;
            this.backButtonClicked();
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    private void backButtonClicked() {
        if (this.finished && this.callback != null && this.errorMessage == null) {
            this.callback.accept(true);
        }
        this.f_96541_.setScreen(this.lastScreen);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.downloadTitle, this.f_96543_ / 2, 20, 16777215);
        guiGraphics0.drawCenteredString(this.f_96547_, this.status, this.f_96543_ / 2, 50, 16777215);
        if (this.showDots) {
            this.drawDots(guiGraphics0);
        }
        if (this.downloadStatus.bytesWritten != 0L && !this.cancelled) {
            this.drawProgressBar(guiGraphics0);
            this.drawDownloadSpeed(guiGraphics0);
        }
        if (this.errorMessage != null) {
            guiGraphics0.drawCenteredString(this.f_96547_, this.errorMessage, this.f_96543_ / 2, 110, 16711680);
        }
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    private void drawDots(GuiGraphics guiGraphics0) {
        int $$1 = this.f_96547_.width(this.status);
        if (this.animTick % 10 == 0) {
            this.dotIndex++;
        }
        guiGraphics0.drawString(this.f_96547_, DOTS[this.dotIndex % DOTS.length], this.f_96543_ / 2 + $$1 / 2 + 5, 50, 16777215, false);
    }

    private void drawProgressBar(GuiGraphics guiGraphics0) {
        double $$1 = Math.min((double) this.downloadStatus.bytesWritten / (double) this.downloadStatus.totalBytes, 1.0);
        this.progress = String.format(Locale.ROOT, "%.1f", $$1 * 100.0);
        int $$2 = (this.f_96543_ - 200) / 2;
        int $$3 = $$2 + (int) Math.round(200.0 * $$1);
        guiGraphics0.fill($$2 - 1, 79, $$3 + 1, 96, -2501934);
        guiGraphics0.fill($$2, 80, $$3, 95, -8355712);
        guiGraphics0.drawCenteredString(this.f_96547_, Component.translatable("mco.download.percent", this.progress), this.f_96543_ / 2, 84, 16777215);
    }

    private void drawDownloadSpeed(GuiGraphics guiGraphics0) {
        if (this.animTick % 20 == 0) {
            if (this.previousWrittenBytes != null) {
                long $$1 = Util.getMillis() - this.previousTimeSnapshot;
                if ($$1 == 0L) {
                    $$1 = 1L;
                }
                this.bytesPersSecond = 1000L * (this.downloadStatus.bytesWritten - this.previousWrittenBytes) / $$1;
                this.drawDownloadSpeed0(guiGraphics0, this.bytesPersSecond);
            }
            this.previousWrittenBytes = this.downloadStatus.bytesWritten;
            this.previousTimeSnapshot = Util.getMillis();
        } else {
            this.drawDownloadSpeed0(guiGraphics0, this.bytesPersSecond);
        }
    }

    private void drawDownloadSpeed0(GuiGraphics guiGraphics0, long long1) {
        if (long1 > 0L) {
            int $$2 = this.f_96547_.width(this.progress);
            guiGraphics0.drawString(this.f_96547_, Component.translatable("mco.download.speed", Unit.humanReadable(long1)), this.f_96543_ / 2 + $$2 / 2 + 15, 84, 16777215, false);
        }
    }

    private void downloadSave() {
        new Thread(() -> {
            try {
                try {
                    if (!DOWNLOAD_LOCK.tryLock(1L, TimeUnit.SECONDS)) {
                        this.status = Component.translatable("mco.download.failed");
                        return;
                    }
                    if (this.cancelled) {
                        this.downloadCancelled();
                        return;
                    }
                    this.status = Component.translatable("mco.download.downloading", this.worldName);
                    FileDownload $$0 = new FileDownload();
                    $$0.contentLength(this.worldDownload.downloadLink);
                    $$0.download(this.worldDownload, this.worldName, this.downloadStatus, this.f_96541_.getLevelSource());
                    while (!$$0.isFinished()) {
                        if ($$0.isError()) {
                            $$0.cancel();
                            this.errorMessage = Component.translatable("mco.download.failed");
                            this.cancelButton.m_93666_(CommonComponents.GUI_DONE);
                            return;
                        }
                        if ($$0.isExtracting()) {
                            if (!this.extracting) {
                                this.status = Component.translatable("mco.download.extracting");
                            }
                            this.extracting = true;
                        }
                        if (this.cancelled) {
                            $$0.cancel();
                            this.downloadCancelled();
                            return;
                        }
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException var8) {
                            LOGGER.error("Failed to check Realms backup download status");
                        }
                    }
                    this.finished = true;
                    this.status = Component.translatable("mco.download.done");
                    this.cancelButton.m_93666_(CommonComponents.GUI_DONE);
                    return;
                } catch (InterruptedException var9) {
                    LOGGER.error("Could not acquire upload lock");
                } catch (Exception var10) {
                    this.errorMessage = Component.translatable("mco.download.failed");
                    var10.printStackTrace();
                }
            } finally {
                if (!DOWNLOAD_LOCK.isHeldByCurrentThread()) {
                    return;
                } else {
                    DOWNLOAD_LOCK.unlock();
                    this.showDots = false;
                    this.finished = true;
                }
            }
        }).start();
    }

    private void downloadCancelled() {
        this.status = Component.translatable("mco.download.cancelled");
    }

    public static class DownloadStatus {

        public volatile long bytesWritten;

        public volatile long totalBytes;
    }
}