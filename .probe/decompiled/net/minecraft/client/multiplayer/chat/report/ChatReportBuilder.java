package net.minecraft.client.multiplayer.chat.report;

import com.google.common.collect.Lists;
import com.mojang.authlib.minecraft.report.AbuseReport;
import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.authlib.minecraft.report.ReportChatMessage;
import com.mojang.authlib.minecraft.report.ReportEvidence;
import com.mojang.authlib.minecraft.report.ReportedEntity;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.Optionull;
import net.minecraft.client.multiplayer.chat.ChatLog;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.network.chat.SignedMessageLink;
import org.apache.commons.lang3.StringUtils;

public class ChatReportBuilder {

    private final ChatReportBuilder.ChatReport report;

    private final AbuseReportLimits limits;

    public ChatReportBuilder(ChatReportBuilder.ChatReport chatReportBuilderChatReport0, AbuseReportLimits abuseReportLimits1) {
        this.report = chatReportBuilderChatReport0;
        this.limits = abuseReportLimits1;
    }

    public ChatReportBuilder(UUID uUID0, AbuseReportLimits abuseReportLimits1) {
        this.report = new ChatReportBuilder.ChatReport(UUID.randomUUID(), Instant.now(), uUID0);
        this.limits = abuseReportLimits1;
    }

    public ChatReportBuilder.ChatReport report() {
        return this.report;
    }

    public UUID reportedProfileId() {
        return this.report.reportedProfileId;
    }

    public IntSet reportedMessages() {
        return this.report.reportedMessages;
    }

    public String comments() {
        return this.report.comments;
    }

    public void setComments(String string0) {
        this.report.comments = string0;
    }

    @Nullable
    public ReportReason reason() {
        return this.report.reason;
    }

    public void setReason(ReportReason reportReason0) {
        this.report.reason = reportReason0;
    }

    public void toggleReported(int int0) {
        this.report.toggleReported(int0, this.limits);
    }

    public boolean isReported(int int0) {
        return this.report.reportedMessages.contains(int0);
    }

    public boolean hasContent() {
        return StringUtils.isNotEmpty(this.comments()) || !this.reportedMessages().isEmpty() || this.reason() != null;
    }

    @Nullable
    public ChatReportBuilder.CannotBuildReason checkBuildable() {
        if (this.report.reportedMessages.isEmpty()) {
            return ChatReportBuilder.CannotBuildReason.NO_REPORTED_MESSAGES;
        } else if (this.report.reportedMessages.size() > this.limits.maxReportedMessageCount()) {
            return ChatReportBuilder.CannotBuildReason.TOO_MANY_MESSAGES;
        } else if (this.report.reason == null) {
            return ChatReportBuilder.CannotBuildReason.NO_REASON;
        } else {
            return this.report.comments.length() > this.limits.maxOpinionCommentsLength() ? ChatReportBuilder.CannotBuildReason.COMMENTS_TOO_LONG : null;
        }
    }

    public Either<ChatReportBuilder.Result, ChatReportBuilder.CannotBuildReason> build(ReportingContext reportingContext0) {
        ChatReportBuilder.CannotBuildReason $$1 = this.checkBuildable();
        if ($$1 != null) {
            return Either.right($$1);
        } else {
            String $$2 = ((ReportReason) Objects.requireNonNull(this.report.reason)).backendName();
            ReportEvidence $$3 = this.buildEvidence(reportingContext0.chatLog());
            ReportedEntity $$4 = new ReportedEntity(this.report.reportedProfileId);
            AbuseReport $$5 = new AbuseReport(this.report.comments, $$2, $$3, $$4, this.report.createdAt);
            return Either.left(new ChatReportBuilder.Result(this.report.reportId, $$5));
        }
    }

    private ReportEvidence buildEvidence(ChatLog chatLog0) {
        List<ReportChatMessage> $$1 = new ArrayList();
        ChatReportContextBuilder $$2 = new ChatReportContextBuilder(this.limits.leadingContextMessageCount());
        $$2.collectAllContext(chatLog0, this.report.reportedMessages, (p_247891_, p_247892_) -> $$1.add(this.buildReportedChatMessage(p_247892_, this.isReported(p_247891_))));
        return new ReportEvidence(Lists.reverse($$1));
    }

    private ReportChatMessage buildReportedChatMessage(LoggedChatMessage.Player loggedChatMessagePlayer0, boolean boolean1) {
        SignedMessageLink $$2 = loggedChatMessagePlayer0.message().link();
        SignedMessageBody $$3 = loggedChatMessagePlayer0.message().signedBody();
        List<ByteBuffer> $$4 = $$3.lastSeen().entries().stream().map(MessageSignature::m_241929_).toList();
        ByteBuffer $$5 = Optionull.map(loggedChatMessagePlayer0.message().signature(), MessageSignature::m_241929_);
        return new ReportChatMessage($$2.index(), $$2.sender(), $$2.sessionId(), $$3.timeStamp(), $$3.salt(), $$4, $$3.content(), $$5, boolean1);
    }

    public ChatReportBuilder copy() {
        return new ChatReportBuilder(this.report.copy(), this.limits);
    }

    public static record CannotBuildReason(Component f_238631_) {

        private final Component message;

        public static final ChatReportBuilder.CannotBuildReason NO_REASON = new ChatReportBuilder.CannotBuildReason(Component.translatable("gui.chatReport.send.no_reason"));

        public static final ChatReportBuilder.CannotBuildReason NO_REPORTED_MESSAGES = new ChatReportBuilder.CannotBuildReason(Component.translatable("gui.chatReport.send.no_reported_messages"));

        public static final ChatReportBuilder.CannotBuildReason TOO_MANY_MESSAGES = new ChatReportBuilder.CannotBuildReason(Component.translatable("gui.chatReport.send.too_many_messages"));

        public static final ChatReportBuilder.CannotBuildReason COMMENTS_TOO_LONG = new ChatReportBuilder.CannotBuildReason(Component.translatable("gui.chatReport.send.comments_too_long"));

        public CannotBuildReason(Component f_238631_) {
            this.message = f_238631_;
        }
    }

    public class ChatReport {

        final UUID reportId;

        final Instant createdAt;

        final UUID reportedProfileId;

        final IntSet reportedMessages = new IntOpenHashSet();

        String comments = "";

        @Nullable
        ReportReason reason;

        ChatReport(UUID uUID0, Instant instant1, UUID uUID2) {
            this.reportId = uUID0;
            this.createdAt = instant1;
            this.reportedProfileId = uUID2;
        }

        public void toggleReported(int int0, AbuseReportLimits abuseReportLimits1) {
            if (this.reportedMessages.contains(int0)) {
                this.reportedMessages.remove(int0);
            } else if (this.reportedMessages.size() < abuseReportLimits1.maxReportedMessageCount()) {
                this.reportedMessages.add(int0);
            }
        }

        public ChatReportBuilder.ChatReport copy() {
            ChatReportBuilder.ChatReport $$0 = ChatReportBuilder.this.new ChatReport(this.reportId, this.createdAt, this.reportedProfileId);
            $$0.reportedMessages.addAll(this.reportedMessages);
            $$0.comments = this.comments;
            $$0.reason = this.reason;
            return $$0;
        }

        public boolean isReportedPlayer(UUID uUID0) {
            return uUID0.equals(this.reportedProfileId);
        }
    }

    public static record Result(UUID f_238815_, AbuseReport f_238727_) {

        private final UUID id;

        private final AbuseReport report;

        public Result(UUID f_238815_, AbuseReport f_238727_) {
            this.id = f_238815_;
            this.report = f_238727_;
        }
    }
}