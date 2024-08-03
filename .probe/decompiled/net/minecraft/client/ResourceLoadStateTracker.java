package net.minecraft.client;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.server.packs.PackResources;
import org.slf4j.Logger;

public class ResourceLoadStateTracker {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Nullable
    private ResourceLoadStateTracker.ReloadState reloadState;

    private int reloadCount;

    public void startReload(ResourceLoadStateTracker.ReloadReason resourceLoadStateTrackerReloadReason0, List<PackResources> listPackResources1) {
        this.reloadCount++;
        if (this.reloadState != null && !this.reloadState.finished) {
            LOGGER.warn("Reload already ongoing, replacing");
        }
        this.reloadState = new ResourceLoadStateTracker.ReloadState(resourceLoadStateTrackerReloadReason0, (List<String>) listPackResources1.stream().map(PackResources::m_5542_).collect(ImmutableList.toImmutableList()));
    }

    public void startRecovery(Throwable throwable0) {
        if (this.reloadState == null) {
            LOGGER.warn("Trying to signal reload recovery, but nothing was started");
            this.reloadState = new ResourceLoadStateTracker.ReloadState(ResourceLoadStateTracker.ReloadReason.UNKNOWN, ImmutableList.of());
        }
        this.reloadState.recoveryReloadInfo = new ResourceLoadStateTracker.RecoveryInfo(throwable0);
    }

    public void finishReload() {
        if (this.reloadState == null) {
            LOGGER.warn("Trying to finish reload, but nothing was started");
        } else {
            this.reloadState.finished = true;
        }
    }

    public void fillCrashReport(CrashReport crashReport0) {
        CrashReportCategory $$1 = crashReport0.addCategory("Last reload");
        $$1.setDetail("Reload number", this.reloadCount);
        if (this.reloadState != null) {
            this.reloadState.fillCrashInfo($$1);
        }
    }

    static class RecoveryInfo {

        private final Throwable error;

        RecoveryInfo(Throwable throwable0) {
            this.error = throwable0;
        }

        public void fillCrashInfo(CrashReportCategory crashReportCategory0) {
            crashReportCategory0.setDetail("Recovery", "Yes");
            crashReportCategory0.setDetail("Recovery reason", (CrashReportDetail<String>) (() -> {
                StringWriter $$0 = new StringWriter();
                this.error.printStackTrace(new PrintWriter($$0));
                return $$0.toString();
            }));
        }
    }

    public static enum ReloadReason {

        INITIAL("initial"), MANUAL("manual"), UNKNOWN("unknown");

        final String name;

        private ReloadReason(String p_168579_) {
            this.name = p_168579_;
        }
    }

    static class ReloadState {

        private final ResourceLoadStateTracker.ReloadReason reloadReason;

        private final List<String> packs;

        @Nullable
        ResourceLoadStateTracker.RecoveryInfo recoveryReloadInfo;

        boolean finished;

        ReloadState(ResourceLoadStateTracker.ReloadReason resourceLoadStateTrackerReloadReason0, List<String> listString1) {
            this.reloadReason = resourceLoadStateTrackerReloadReason0;
            this.packs = listString1;
        }

        public void fillCrashInfo(CrashReportCategory crashReportCategory0) {
            crashReportCategory0.setDetail("Reload reason", this.reloadReason.name);
            crashReportCategory0.setDetail("Finished", this.finished ? "Yes" : "No");
            crashReportCategory0.setDetail("Packs", (CrashReportDetail<String>) (() -> String.join(", ", this.packs)));
            if (this.recoveryReloadInfo != null) {
                this.recoveryReloadInfo.fillCrashInfo(crashReportCategory0);
            }
        }
    }
}