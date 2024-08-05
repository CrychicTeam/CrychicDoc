package net.minecraft.client.gui.screens.reporting;

import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Optionull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.GenericWaitingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.client.multiplayer.chat.report.ChatReportBuilder;
import net.minecraft.client.multiplayer.chat.report.ReportReason;
import net.minecraft.client.multiplayer.chat.report.ReportingContext;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ThrowingComponent;
import org.slf4j.Logger;

public class ChatReportScreen extends Screen {

    private static final int BUTTON_WIDTH = 120;

    private static final int BUTTON_HEIGHT = 20;

    private static final int BUTTON_MARGIN = 20;

    private static final int BUTTON_MARGIN_HALF = 10;

    private static final int LABEL_HEIGHT = 25;

    private static final int SCREEN_WIDTH = 280;

    private static final int SCREEN_HEIGHT = 300;

    private static final Component OBSERVED_WHAT_LABEL = Component.translatable("gui.chatReport.observed_what");

    private static final Component SELECT_REASON = Component.translatable("gui.chatReport.select_reason");

    private static final Component MORE_COMMENTS_LABEL = Component.translatable("gui.chatReport.more_comments");

    private static final Component DESCRIBE_PLACEHOLDER = Component.translatable("gui.chatReport.describe");

    private static final Component REPORT_SENT_MESSAGE = Component.translatable("gui.chatReport.report_sent_msg");

    private static final Component SELECT_CHAT_MESSAGE = Component.translatable("gui.chatReport.select_chat");

    private static final Component REPORT_SENDING_TITLE = Component.translatable("gui.abuseReport.sending.title").withStyle(ChatFormatting.BOLD);

    private static final Component REPORT_SENT_TITLE = Component.translatable("gui.abuseReport.sent.title").withStyle(ChatFormatting.BOLD);

    private static final Component REPORT_ERROR_TITLE = Component.translatable("gui.abuseReport.error.title").withStyle(ChatFormatting.BOLD);

    private static final Component REPORT_SEND_GENERIC_ERROR = Component.translatable("gui.abuseReport.send.generic_error");

    private static final Logger LOGGER = LogUtils.getLogger();

    @Nullable
    final Screen lastScreen;

    private final ReportingContext reportingContext;

    @Nullable
    private MultiLineLabel reasonDescriptionLabel;

    @Nullable
    private MultiLineEditBox commentBox;

    private Button sendButton;

    private ChatReportBuilder reportBuilder;

    @Nullable
    private ChatReportBuilder.CannotBuildReason cannotBuildReason;

    private ChatReportScreen(@Nullable Screen screen0, ReportingContext reportingContext1, ChatReportBuilder chatReportBuilder2) {
        super(Component.translatable("gui.chatReport.title"));
        this.lastScreen = screen0;
        this.reportingContext = reportingContext1;
        this.reportBuilder = chatReportBuilder2;
    }

    public ChatReportScreen(@Nullable Screen screen0, ReportingContext reportingContext1, UUID uUID2) {
        this(screen0, reportingContext1, new ChatReportBuilder(uUID2, reportingContext1.sender().reportLimits()));
    }

    public ChatReportScreen(@Nullable Screen screen0, ReportingContext reportingContext1, ChatReportBuilder.ChatReport chatReportBuilderChatReport2) {
        this(screen0, reportingContext1, new ChatReportBuilder(chatReportBuilderChatReport2, reportingContext1.sender().reportLimits()));
    }

    @Override
    protected void init() {
        AbuseReportLimits $$0 = this.reportingContext.sender().reportLimits();
        int $$1 = this.f_96543_ / 2;
        ReportReason $$2 = this.reportBuilder.reason();
        if ($$2 != null) {
            this.reasonDescriptionLabel = MultiLineLabel.create(this.f_96547_, $$2.description(), 280);
        } else {
            this.reasonDescriptionLabel = null;
        }
        IntSet $$3 = this.reportBuilder.reportedMessages();
        Component $$4;
        if ($$3.isEmpty()) {
            $$4 = SELECT_CHAT_MESSAGE;
        } else {
            $$4 = Component.translatable("gui.chatReport.selected_chat", $$3.size());
        }
        this.m_142416_(Button.builder($$4, p_280882_ -> this.f_96541_.setScreen(new ChatSelectionScreen(this, this.reportingContext, this.reportBuilder, p_239697_ -> {
            this.reportBuilder = p_239697_;
            this.onReportChanged();
        }))).bounds(this.contentLeft(), this.selectChatTop(), 280, 20).build());
        Component $$6 = Optionull.mapOrDefault($$2, ReportReason::m_239342_, SELECT_REASON);
        this.m_142416_(Button.builder($$6, p_280881_ -> this.f_96541_.setScreen(new ReportReasonSelectionScreen(this, this.reportBuilder.reason(), p_239513_ -> {
            this.reportBuilder.setReason(p_239513_);
            this.onReportChanged();
        }))).bounds(this.contentLeft(), this.selectInfoTop(), 280, 20).build());
        this.commentBox = (MultiLineEditBox) this.m_142416_(new MultiLineEditBox(this.f_96541_.font, this.contentLeft(), this.commentBoxTop(), 280, this.commentBoxBottom() - this.commentBoxTop(), DESCRIBE_PLACEHOLDER, Component.translatable("gui.chatReport.comments")));
        this.commentBox.setValue(this.reportBuilder.comments());
        this.commentBox.setCharacterLimit($$0.maxOpinionCommentsLength());
        this.commentBox.setValueListener(p_240036_ -> {
            this.reportBuilder.setComments(p_240036_);
            this.onReportChanged();
        });
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_239971_ -> this.onClose()).bounds($$1 - 120, this.completeButtonTop(), 120, 20).build());
        this.sendButton = (Button) this.m_142416_(Button.builder(Component.translatable("gui.chatReport.send"), p_239742_ -> this.sendReport()).bounds($$1 + 10, this.completeButtonTop(), 120, 20).build());
        this.onReportChanged();
    }

    private void onReportChanged() {
        this.cannotBuildReason = this.reportBuilder.checkBuildable();
        this.sendButton.f_93623_ = this.cannotBuildReason == null;
        this.sendButton.m_257544_(Optionull.map(this.cannotBuildReason, p_258134_ -> Tooltip.create(p_258134_.message())));
    }

    private void sendReport() {
        this.reportBuilder.build(this.reportingContext).ifLeft(p_280883_ -> {
            CompletableFuture<?> $$1 = this.reportingContext.sender().send(p_280883_.id(), p_280883_.report());
            this.f_96541_.setScreen(GenericWaitingScreen.createWaiting(REPORT_SENDING_TITLE, CommonComponents.GUI_CANCEL, () -> {
                this.f_96541_.setScreen(this);
                $$1.cancel(true);
            }));
            $$1.handleAsync((p_240236_, p_240237_) -> {
                if (p_240237_ == null) {
                    this.onReportSendSuccess();
                } else {
                    if (p_240237_ instanceof CancellationException) {
                        return null;
                    }
                    this.onReportSendError(p_240237_);
                }
                return null;
            }, this.f_96541_);
        }).ifRight(p_242967_ -> this.displayReportSendError(p_242967_.message()));
    }

    private void onReportSendSuccess() {
        this.clearDraft();
        this.f_96541_.setScreen(GenericWaitingScreen.createCompleted(REPORT_SENT_TITLE, REPORT_SENT_MESSAGE, CommonComponents.GUI_DONE, () -> this.f_96541_.setScreen(null)));
    }

    private void onReportSendError(Throwable throwable0) {
        LOGGER.error("Encountered error while sending abuse report", throwable0);
        Component $$2;
        if (throwable0.getCause() instanceof ThrowingComponent $$1) {
            $$2 = $$1.getComponent();
        } else {
            $$2 = REPORT_SEND_GENERIC_ERROR;
        }
        this.displayReportSendError($$2);
    }

    private void displayReportSendError(Component component0) {
        Component $$1 = component0.copy().withStyle(ChatFormatting.RED);
        this.f_96541_.setScreen(GenericWaitingScreen.createCompleted(REPORT_ERROR_TITLE, $$1, CommonComponents.GUI_BACK, () -> this.f_96541_.setScreen(this)));
    }

    void saveDraft() {
        if (this.reportBuilder.hasContent()) {
            this.reportingContext.setChatReportDraft(this.reportBuilder.report().copy());
        }
    }

    void clearDraft() {
        this.reportingContext.setChatReportDraft(null);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        int $$4 = this.f_96543_ / 2;
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, $$4, 10, 16777215);
        guiGraphics0.drawCenteredString(this.f_96547_, OBSERVED_WHAT_LABEL, $$4, this.selectChatTop() - 9 - 6, 16777215);
        if (this.reasonDescriptionLabel != null) {
            this.reasonDescriptionLabel.renderLeftAligned(guiGraphics0, this.contentLeft(), this.selectInfoTop() + 20 + 5, 9, 16777215);
        }
        guiGraphics0.drawString(this.f_96547_, MORE_COMMENTS_LABEL, this.contentLeft(), this.commentBoxTop() - 9 - 6, 16777215);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public void tick() {
        this.commentBox.tick();
        super.tick();
    }

    @Override
    public void onClose() {
        if (this.reportBuilder.hasContent()) {
            this.f_96541_.setScreen(new ChatReportScreen.DiscardReportWarningScreen());
        } else {
            this.f_96541_.setScreen(this.lastScreen);
        }
    }

    @Override
    public void removed() {
        this.saveDraft();
        super.removed();
    }

    @Override
    public boolean mouseReleased(double double0, double double1, int int2) {
        return super.m_6348_(double0, double1, int2) ? true : this.commentBox.m_6348_(double0, double1, int2);
    }

    private int contentLeft() {
        return this.f_96543_ / 2 - 140;
    }

    private int contentRight() {
        return this.f_96543_ / 2 + 140;
    }

    private int contentTop() {
        return Math.max((this.f_96544_ - 300) / 2, 0);
    }

    private int contentBottom() {
        return Math.min((this.f_96544_ + 300) / 2, this.f_96544_);
    }

    private int selectChatTop() {
        return this.contentTop() + 40;
    }

    private int selectInfoTop() {
        return this.selectChatTop() + 10 + 20;
    }

    private int commentBoxTop() {
        int $$0 = this.selectInfoTop() + 20 + 25;
        if (this.reasonDescriptionLabel != null) {
            $$0 += (this.reasonDescriptionLabel.getLineCount() + 1) * 9;
        }
        return $$0;
    }

    private int commentBoxBottom() {
        return this.completeButtonTop() - 20;
    }

    private int completeButtonTop() {
        return this.contentBottom() - 20 - 10;
    }

    class DiscardReportWarningScreen extends WarningScreen {

        private static final Component TITLE = Component.translatable("gui.chatReport.discard.title").withStyle(ChatFormatting.BOLD);

        private static final Component MESSAGE = Component.translatable("gui.chatReport.discard.content");

        private static final Component RETURN = Component.translatable("gui.chatReport.discard.return");

        private static final Component DRAFT = Component.translatable("gui.chatReport.discard.draft");

        private static final Component DISCARD = Component.translatable("gui.chatReport.discard.discard");

        protected DiscardReportWarningScreen() {
            super(TITLE, MESSAGE, MESSAGE);
        }

        @Override
        protected void initButtons(int int0) {
            int $$1 = 150;
            this.m_142416_(Button.builder(RETURN, p_239525_ -> this.onClose()).bounds(this.f_96543_ / 2 - 155, 100 + int0, 150, 20).build());
            this.m_142416_(Button.builder(DRAFT, p_280885_ -> {
                ChatReportScreen.this.saveDraft();
                this.f_96541_.setScreen(ChatReportScreen.this.lastScreen);
            }).bounds(this.f_96543_ / 2 + 5, 100 + int0, 150, 20).build());
            this.m_142416_(Button.builder(DISCARD, p_280886_ -> {
                ChatReportScreen.this.clearDraft();
                this.f_96541_.setScreen(ChatReportScreen.this.lastScreen);
            }).bounds(this.f_96543_ / 2 - 75, 130 + int0, 150, 20).build());
        }

        @Override
        public void onClose() {
            this.f_96541_.setScreen(ChatReportScreen.this);
        }

        @Override
        public boolean shouldCloseOnEsc() {
            return false;
        }

        @Override
        protected void renderTitle(GuiGraphics guiGraphics0) {
            guiGraphics0.drawString(this.f_96547_, this.f_96539_, this.f_96543_ / 2 - 155, 30, 16777215);
        }
    }
}