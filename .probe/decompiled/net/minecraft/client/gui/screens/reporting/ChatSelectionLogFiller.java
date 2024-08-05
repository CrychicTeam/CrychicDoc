package net.minecraft.client.gui.screens.reporting;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.chat.ChatLog;
import net.minecraft.client.multiplayer.chat.LoggedChatEvent;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
import net.minecraft.client.multiplayer.chat.report.ChatReportContextBuilder;
import net.minecraft.client.multiplayer.chat.report.ReportingContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.SignedMessageLink;

public class ChatSelectionLogFiller {

    private final ChatLog log;

    private final ChatReportContextBuilder contextBuilder;

    private final Predicate<LoggedChatMessage.Player> canReport;

    @Nullable
    private SignedMessageLink previousLink = null;

    private int eventId;

    private int missedCount;

    @Nullable
    private PlayerChatMessage lastMessage;

    public ChatSelectionLogFiller(ReportingContext reportingContext0, Predicate<LoggedChatMessage.Player> predicateLoggedChatMessagePlayer1) {
        this.log = reportingContext0.chatLog();
        this.contextBuilder = new ChatReportContextBuilder(reportingContext0.sender().reportLimits().leadingContextMessageCount());
        this.canReport = predicateLoggedChatMessagePlayer1;
        this.eventId = this.log.end();
    }

    public void fillNextPage(int int0, ChatSelectionLogFiller.Output chatSelectionLogFillerOutput1) {
        int $$2 = 0;
        while ($$2 < int0) {
            LoggedChatEvent $$3 = this.log.lookup(this.eventId);
            if ($$3 == null) {
                break;
            }
            int $$4 = this.eventId--;
            if ($$3 instanceof LoggedChatMessage.Player $$5 && !$$5.message().equals(this.lastMessage)) {
                if (this.acceptMessage(chatSelectionLogFillerOutput1, $$5)) {
                    if (this.missedCount > 0) {
                        chatSelectionLogFillerOutput1.acceptDivider(Component.translatable("gui.chatSelection.fold", this.missedCount));
                        this.missedCount = 0;
                    }
                    chatSelectionLogFillerOutput1.acceptMessage($$4, $$5);
                    $$2++;
                } else {
                    this.missedCount++;
                }
                this.lastMessage = $$5.message();
            }
        }
    }

    private boolean acceptMessage(ChatSelectionLogFiller.Output chatSelectionLogFillerOutput0, LoggedChatMessage.Player loggedChatMessagePlayer1) {
        PlayerChatMessage $$2 = loggedChatMessagePlayer1.message();
        boolean $$3 = this.contextBuilder.acceptContext($$2);
        if (this.canReport.test(loggedChatMessagePlayer1)) {
            this.contextBuilder.trackContext($$2);
            if (this.previousLink != null && !this.previousLink.isDescendantOf($$2.link())) {
                chatSelectionLogFillerOutput0.acceptDivider(Component.translatable("gui.chatSelection.join", loggedChatMessagePlayer1.profile().getName()).withStyle(ChatFormatting.YELLOW));
            }
            this.previousLink = $$2.link();
            return true;
        } else {
            return $$3;
        }
    }

    public interface Output {

        void acceptMessage(int var1, LoggedChatMessage.Player var2);

        void acceptDivider(Component var1);
    }
}