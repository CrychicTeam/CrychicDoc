package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.minecraft.UserApiService;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.reporting.ChatReportScreen;
import net.minecraft.client.multiplayer.chat.ChatLog;
import net.minecraft.network.chat.Component;

public final class ReportingContext {

    private static final int LOG_CAPACITY = 1024;

    private final AbuseReportSender sender;

    private final ReportEnvironment environment;

    private final ChatLog chatLog;

    @Nullable
    private ChatReportBuilder.ChatReport chatReportDraft;

    public ReportingContext(AbuseReportSender abuseReportSender0, ReportEnvironment reportEnvironment1, ChatLog chatLog2) {
        this.sender = abuseReportSender0;
        this.environment = reportEnvironment1;
        this.chatLog = chatLog2;
    }

    public static ReportingContext create(ReportEnvironment reportEnvironment0, UserApiService userApiService1) {
        ChatLog $$2 = new ChatLog(1024);
        AbuseReportSender $$3 = AbuseReportSender.create(reportEnvironment0, userApiService1);
        return new ReportingContext($$3, reportEnvironment0, $$2);
    }

    public void draftReportHandled(Minecraft minecraft0, @Nullable Screen screen1, Runnable runnable2, boolean boolean3) {
        if (this.chatReportDraft != null) {
            ChatReportBuilder.ChatReport $$4 = this.chatReportDraft.copy();
            minecraft0.setScreen(new ConfirmScreen(p_261387_ -> {
                this.setChatReportDraft(null);
                if (p_261387_) {
                    minecraft0.setScreen(new ChatReportScreen(screen1, this, $$4));
                } else {
                    runnable2.run();
                }
            }, Component.translatable(boolean3 ? "gui.chatReport.draft.quittotitle.title" : "gui.chatReport.draft.title"), Component.translatable(boolean3 ? "gui.chatReport.draft.quittotitle.content" : "gui.chatReport.draft.content"), Component.translatable("gui.chatReport.draft.edit"), Component.translatable("gui.chatReport.draft.discard")));
        } else {
            runnable2.run();
        }
    }

    public AbuseReportSender sender() {
        return this.sender;
    }

    public ChatLog chatLog() {
        return this.chatLog;
    }

    public boolean matches(ReportEnvironment reportEnvironment0) {
        return Objects.equals(this.environment, reportEnvironment0);
    }

    public void setChatReportDraft(@Nullable ChatReportBuilder.ChatReport chatReportBuilderChatReport0) {
        this.chatReportDraft = chatReportBuilderChatReport0;
    }

    public boolean hasDraftReport() {
        return this.chatReportDraft != null;
    }

    public boolean hasDraftReportFor(UUID uUID0) {
        return this.hasDraftReport() && this.chatReportDraft.isReportedPlayer(uUID0);
    }
}