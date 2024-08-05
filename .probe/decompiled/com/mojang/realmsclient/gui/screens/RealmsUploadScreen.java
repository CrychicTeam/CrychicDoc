package com.mojang.realmsclient.gui.screens;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.Unit;
import com.mojang.realmsclient.client.FileUpload;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.client.UploadStatus;
import com.mojang.realmsclient.dto.UploadInfo;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.util.UploadTokenCache;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPOutputStream;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.world.level.storage.LevelSummary;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;

public class RealmsUploadScreen extends RealmsScreen {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ReentrantLock UPLOAD_LOCK = new ReentrantLock();

    private static final int BAR_WIDTH = 200;

    private static final int BAR_TOP = 80;

    private static final int BAR_BOTTOM = 95;

    private static final int BAR_BORDER = 1;

    private static final String[] DOTS = new String[] { "", ".", ". .", ". . ." };

    private static final Component VERIFYING_TEXT = Component.translatable("mco.upload.verifying");

    private final RealmsResetWorldScreen lastScreen;

    private final LevelSummary selectedLevel;

    private final long worldId;

    private final int slotId;

    private final UploadStatus uploadStatus;

    private final RateLimiter narrationRateLimiter;

    @Nullable
    private volatile Component[] errorMessage;

    private volatile Component status = Component.translatable("mco.upload.preparing");

    private volatile String progress;

    private volatile boolean cancelled;

    private volatile boolean uploadFinished;

    private volatile boolean showDots = true;

    private volatile boolean uploadStarted;

    private Button backButton;

    private Button cancelButton;

    private int tickCount;

    @Nullable
    private Long previousWrittenBytes;

    @Nullable
    private Long previousTimeSnapshot;

    private long bytesPersSecond;

    private final Runnable callback;

    public RealmsUploadScreen(long long0, int int1, RealmsResetWorldScreen realmsResetWorldScreen2, LevelSummary levelSummary3, Runnable runnable4) {
        super(GameNarrator.NO_TITLE);
        this.worldId = long0;
        this.slotId = int1;
        this.lastScreen = realmsResetWorldScreen2;
        this.selectedLevel = levelSummary3;
        this.uploadStatus = new UploadStatus();
        this.narrationRateLimiter = RateLimiter.create(0.1F);
        this.callback = runnable4;
    }

    @Override
    public void init() {
        this.backButton = (Button) this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_90118_ -> this.onBack()).bounds((this.f_96543_ - 200) / 2, this.f_96544_ - 42, 200, 20).build());
        this.backButton.f_93624_ = false;
        this.cancelButton = (Button) this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_90104_ -> this.onCancel()).bounds((this.f_96543_ - 200) / 2, this.f_96544_ - 42, 200, 20).build());
        if (!this.uploadStarted) {
            if (this.lastScreen.slot == -1) {
                this.upload();
            } else {
                this.lastScreen.switchSlot(() -> {
                    if (!this.uploadStarted) {
                        this.uploadStarted = true;
                        this.f_96541_.setScreen(this);
                        this.upload();
                    }
                });
            }
        }
    }

    private void onBack() {
        this.callback.run();
    }

    private void onCancel() {
        this.cancelled = true;
        this.f_96541_.setScreen(this.lastScreen);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            if (this.showDots) {
                this.onCancel();
            } else {
                this.onBack();
            }
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        if (!this.uploadFinished && this.uploadStatus.bytesWritten != 0L && this.uploadStatus.bytesWritten == this.uploadStatus.totalBytes) {
            this.status = VERIFYING_TEXT;
            this.cancelButton.f_93623_ = false;
        }
        guiGraphics0.drawCenteredString(this.f_96547_, this.status, this.f_96543_ / 2, 50, 16777215);
        if (this.showDots) {
            this.drawDots(guiGraphics0);
        }
        if (this.uploadStatus.bytesWritten != 0L && !this.cancelled) {
            this.drawProgressBar(guiGraphics0);
            this.drawUploadSpeed(guiGraphics0);
        }
        if (this.errorMessage != null) {
            for (int $$4 = 0; $$4 < this.errorMessage.length; $$4++) {
                guiGraphics0.drawCenteredString(this.f_96547_, this.errorMessage[$$4], this.f_96543_ / 2, 110 + 12 * $$4, 16711680);
            }
        }
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    private void drawDots(GuiGraphics guiGraphics0) {
        int $$1 = this.f_96547_.width(this.status);
        guiGraphics0.drawString(this.f_96547_, DOTS[this.tickCount / 10 % DOTS.length], this.f_96543_ / 2 + $$1 / 2 + 5, 50, 16777215, false);
    }

    private void drawProgressBar(GuiGraphics guiGraphics0) {
        double $$1 = Math.min((double) this.uploadStatus.bytesWritten / (double) this.uploadStatus.totalBytes, 1.0);
        this.progress = String.format(Locale.ROOT, "%.1f", $$1 * 100.0);
        int $$2 = (this.f_96543_ - 200) / 2;
        int $$3 = $$2 + (int) Math.round(200.0 * $$1);
        guiGraphics0.fill($$2 - 1, 79, $$3 + 1, 96, -2501934);
        guiGraphics0.fill($$2, 80, $$3, 95, -8355712);
        guiGraphics0.drawCenteredString(this.f_96547_, this.progress + " %", this.f_96543_ / 2, 84, 16777215);
    }

    private void drawUploadSpeed(GuiGraphics guiGraphics0) {
        if (this.tickCount % 20 == 0) {
            if (this.previousWrittenBytes != null) {
                long $$1 = Util.getMillis() - this.previousTimeSnapshot;
                if ($$1 == 0L) {
                    $$1 = 1L;
                }
                this.bytesPersSecond = 1000L * (this.uploadStatus.bytesWritten - this.previousWrittenBytes) / $$1;
                this.drawUploadSpeed0(guiGraphics0, this.bytesPersSecond);
            }
            this.previousWrittenBytes = this.uploadStatus.bytesWritten;
            this.previousTimeSnapshot = Util.getMillis();
        } else {
            this.drawUploadSpeed0(guiGraphics0, this.bytesPersSecond);
        }
    }

    private void drawUploadSpeed0(GuiGraphics guiGraphics0, long long1) {
        if (long1 > 0L) {
            int $$2 = this.f_96547_.width(this.progress);
            String $$3 = "(" + Unit.humanReadable(long1) + "/s)";
            guiGraphics0.drawString(this.f_96547_, $$3, this.f_96543_ / 2 + $$2 / 2 + 15, 84, 16777215, false);
        }
    }

    @Override
    public void tick() {
        super.m_86600_();
        this.tickCount++;
        if (this.status != null && this.narrationRateLimiter.tryAcquire(1)) {
            Component $$0 = this.createProgressNarrationMessage();
            this.f_96541_.getNarrator().sayNow($$0);
        }
    }

    private Component createProgressNarrationMessage() {
        List<Component> $$0 = Lists.newArrayList();
        $$0.add(this.status);
        if (this.progress != null) {
            $$0.add(Component.literal(this.progress + "%"));
        }
        if (this.errorMessage != null) {
            $$0.addAll(Arrays.asList(this.errorMessage));
        }
        return CommonComponents.joinLines($$0);
    }

    private void upload() {
        this.uploadStarted = true;
        new Thread(() -> {
            File $$0 = null;
            RealmsClient $$1 = RealmsClient.create();
            long $$2 = this.worldId;
            try {
                if (!UPLOAD_LOCK.tryLock(1L, TimeUnit.SECONDS)) {
                    this.status = Component.translatable("mco.upload.close.failure");
                } else {
                    UploadInfo $$3 = null;
                    for (int $$4 = 0; $$4 < 20; $$4++) {
                        try {
                            if (this.cancelled) {
                                this.uploadCancelled();
                                return;
                            }
                            $$3 = $$1.requestUploadInfo($$2, UploadTokenCache.get($$2));
                            if ($$3 != null) {
                                break;
                            }
                        } catch (RetryCallException var20) {
                            Thread.sleep((long) (var20.delaySeconds * 1000));
                        }
                    }
                    if ($$3 == null) {
                        this.status = Component.translatable("mco.upload.close.failure");
                    } else {
                        UploadTokenCache.put($$2, $$3.getToken());
                        if (!$$3.isWorldClosed()) {
                            this.status = Component.translatable("mco.upload.close.failure");
                        } else if (this.cancelled) {
                            this.uploadCancelled();
                        } else {
                            File $$6 = new File(this.f_96541_.gameDirectory.getAbsolutePath(), "saves");
                            $$0 = this.tarGzipArchive(new File($$6, this.selectedLevel.getLevelId()));
                            if (this.cancelled) {
                                this.uploadCancelled();
                            } else if (this.verify($$0)) {
                                this.status = Component.translatable("mco.upload.uploading", this.selectedLevel.getLevelName());
                                FileUpload $$11 = new FileUpload($$0, this.worldId, this.slotId, $$3, this.f_96541_.getUser(), SharedConstants.getCurrentVersion().getName(), this.uploadStatus);
                                $$11.upload(p_167557_ -> {
                                    if (p_167557_.statusCode >= 200 && p_167557_.statusCode < 300) {
                                        this.uploadFinished = true;
                                        this.status = Component.translatable("mco.upload.done");
                                        this.backButton.m_93666_(CommonComponents.GUI_DONE);
                                        UploadTokenCache.invalidate($$2);
                                    } else if (p_167557_.statusCode == 400 && p_167557_.errorMessage != null) {
                                        this.setErrorMessage(Component.translatable("mco.upload.failed", p_167557_.errorMessage));
                                    } else {
                                        this.setErrorMessage(Component.translatable("mco.upload.failed", p_167557_.statusCode));
                                    }
                                });
                                while (!$$11.isFinished()) {
                                    if (this.cancelled) {
                                        $$11.cancel();
                                        this.uploadCancelled();
                                        return;
                                    }
                                    try {
                                        Thread.sleep(500L);
                                    } catch (InterruptedException var19) {
                                        LOGGER.error("Failed to check Realms file upload status");
                                    }
                                }
                            } else {
                                long $$7 = $$0.length();
                                Unit $$8 = Unit.getLargest($$7);
                                Unit $$9 = Unit.getLargest(5368709120L);
                                if (Unit.humanReadable($$7, $$8).equals(Unit.humanReadable(5368709120L, $$9)) && $$8 != Unit.B) {
                                    Unit $$10 = Unit.values()[$$8.ordinal() - 1];
                                    this.setErrorMessage(Component.translatable("mco.upload.size.failure.line1", this.selectedLevel.getLevelName()), Component.translatable("mco.upload.size.failure.line2", Unit.humanReadable($$7, $$10), Unit.humanReadable(5368709120L, $$10)));
                                } else {
                                    this.setErrorMessage(Component.translatable("mco.upload.size.failure.line1", this.selectedLevel.getLevelName()), Component.translatable("mco.upload.size.failure.line2", Unit.humanReadable($$7, $$8), Unit.humanReadable(5368709120L, $$9)));
                                }
                            }
                        }
                    }
                }
            } catch (IOException var21) {
                this.setErrorMessage(Component.translatable("mco.upload.failed", var21.getMessage()));
            } catch (RealmsServiceException var22) {
                this.setErrorMessage(Component.translatable("mco.upload.failed", var22.toString()));
            } catch (InterruptedException var23) {
                LOGGER.error("Could not acquire upload lock");
            } finally {
                this.uploadFinished = true;
                if (UPLOAD_LOCK.isHeldByCurrentThread()) {
                    UPLOAD_LOCK.unlock();
                    this.showDots = false;
                    this.backButton.f_93624_ = true;
                    this.cancelButton.f_93624_ = false;
                    if ($$0 != null) {
                        LOGGER.debug("Deleting file {}", $$0.getAbsolutePath());
                        $$0.delete();
                    }
                } else {
                    return;
                }
            }
        }).start();
    }

    private void setErrorMessage(Component... component0) {
        this.errorMessage = component0;
    }

    private void uploadCancelled() {
        this.status = Component.translatable("mco.upload.cancelled");
        LOGGER.debug("Upload was cancelled");
    }

    private boolean verify(File file0) {
        return file0.length() < 5368709120L;
    }

    private File tarGzipArchive(File file0) throws IOException {
        TarArchiveOutputStream $$1 = null;
        File var4;
        try {
            File $$2 = File.createTempFile("realms-upload-file", ".tar.gz");
            $$1 = new TarArchiveOutputStream(new GZIPOutputStream(new FileOutputStream($$2)));
            $$1.setLongFileMode(3);
            this.addFileToTarGz($$1, file0.getAbsolutePath(), "world", true);
            $$1.finish();
            var4 = $$2;
        } finally {
            if ($$1 != null) {
                $$1.close();
            }
        }
        return var4;
    }

    private void addFileToTarGz(TarArchiveOutputStream tarArchiveOutputStream0, String string1, String string2, boolean boolean3) throws IOException {
        if (!this.cancelled) {
            File $$4 = new File(string1);
            String $$5 = boolean3 ? string2 : string2 + $$4.getName();
            TarArchiveEntry $$6 = new TarArchiveEntry($$4, $$5);
            tarArchiveOutputStream0.putArchiveEntry($$6);
            if ($$4.isFile()) {
                IOUtils.copy(new FileInputStream($$4), tarArchiveOutputStream0);
                tarArchiveOutputStream0.closeArchiveEntry();
            } else {
                tarArchiveOutputStream0.closeArchiveEntry();
                File[] $$7 = $$4.listFiles();
                if ($$7 != null) {
                    for (File $$8 : $$7) {
                        this.addFileToTarGz(tarArchiveOutputStream0, $$8.getAbsolutePath(), $$5 + "/", false);
                    }
                }
            }
        }
    }
}