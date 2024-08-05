package net.minecraft.client.multiplayer.chat;

import com.google.common.collect.Queues;
import com.mojang.authlib.GameProfile;
import java.time.Instant;
import java.util.Deque;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FilterMask;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.util.StringDecomposer;
import org.apache.commons.lang3.StringUtils;

public class ChatListener {

    private final Minecraft minecraft;

    private final Deque<ChatListener.Message> delayedMessageQueue = Queues.newArrayDeque();

    private long messageDelay;

    private long previousMessageTime;

    public ChatListener(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void tick() {
        if (this.messageDelay != 0L) {
            if (Util.getMillis() >= this.previousMessageTime + this.messageDelay) {
                ChatListener.Message $$0 = (ChatListener.Message) this.delayedMessageQueue.poll();
                while ($$0 != null && !$$0.accept()) {
                    $$0 = (ChatListener.Message) this.delayedMessageQueue.poll();
                }
            }
        }
    }

    public void setMessageDelay(double double0) {
        long $$1 = (long) (double0 * 1000.0);
        if ($$1 == 0L && this.messageDelay > 0L) {
            this.delayedMessageQueue.forEach(ChatListener.Message::m_240698_);
            this.delayedMessageQueue.clear();
        }
        this.messageDelay = $$1;
    }

    public void acceptNextDelayedMessage() {
        ((ChatListener.Message) this.delayedMessageQueue.remove()).accept();
    }

    public long queueSize() {
        return (long) this.delayedMessageQueue.size();
    }

    public void clearQueue() {
        this.delayedMessageQueue.forEach(ChatListener.Message::m_240698_);
        this.delayedMessageQueue.clear();
    }

    public boolean removeFromDelayedMessageQueue(MessageSignature messageSignature0) {
        return this.delayedMessageQueue.removeIf(p_247887_ -> messageSignature0.equals(p_247887_.signature()));
    }

    private boolean willDelayMessages() {
        return this.messageDelay > 0L && Util.getMillis() < this.previousMessageTime + this.messageDelay;
    }

    private void handleMessage(@Nullable MessageSignature messageSignature0, BooleanSupplier booleanSupplier1) {
        if (this.willDelayMessages()) {
            this.delayedMessageQueue.add(new ChatListener.Message(messageSignature0, booleanSupplier1));
        } else {
            booleanSupplier1.getAsBoolean();
        }
    }

    public void handlePlayerChatMessage(PlayerChatMessage playerChatMessage0, GameProfile gameProfile1, ChatType.Bound chatTypeBound2) {
        boolean $$3 = this.minecraft.options.onlyShowSecureChat().get();
        PlayerChatMessage $$4 = $$3 ? playerChatMessage0.removeUnsignedContent() : playerChatMessage0;
        Component $$5 = chatTypeBound2.decorate($$4.decoratedContent());
        Instant $$6 = Instant.now();
        this.handleMessage(playerChatMessage0.signature(), () -> {
            boolean $$6x = this.showMessageToPlayer(chatTypeBound2, playerChatMessage0, $$5, gameProfile1, $$3, $$6);
            ClientPacketListener $$7 = this.minecraft.getConnection();
            if ($$7 != null) {
                $$7.markMessageAsProcessed(playerChatMessage0, $$6x);
            }
            return $$6x;
        });
    }

    public void handleDisguisedChatMessage(Component component0, ChatType.Bound chatTypeBound1) {
        Instant $$2 = Instant.now();
        this.handleMessage(null, () -> {
            Component $$3 = chatTypeBound1.decorate(component0);
            this.minecraft.gui.getChat().addMessage($$3);
            this.narrateChatMessage(chatTypeBound1, component0);
            this.logSystemMessage($$3, $$2);
            this.previousMessageTime = Util.getMillis();
            return true;
        });
    }

    private boolean showMessageToPlayer(ChatType.Bound chatTypeBound0, PlayerChatMessage playerChatMessage1, Component component2, GameProfile gameProfile3, boolean boolean4, Instant instant5) {
        ChatTrustLevel $$6 = this.evaluateTrustLevel(playerChatMessage1, component2, instant5);
        if (boolean4 && $$6.isNotSecure()) {
            return false;
        } else if (!this.minecraft.isBlocked(playerChatMessage1.sender()) && !playerChatMessage1.isFullyFiltered()) {
            GuiMessageTag $$7 = $$6.createTag(playerChatMessage1);
            MessageSignature $$8 = playerChatMessage1.signature();
            FilterMask $$9 = playerChatMessage1.filterMask();
            if ($$9.isEmpty()) {
                this.minecraft.gui.getChat().addMessage(component2, $$8, $$7);
                this.narrateChatMessage(chatTypeBound0, playerChatMessage1.decoratedContent());
            } else {
                Component $$10 = $$9.applyWithFormatting(playerChatMessage1.signedContent());
                if ($$10 != null) {
                    this.minecraft.gui.getChat().addMessage(chatTypeBound0.decorate($$10), $$8, $$7);
                    this.narrateChatMessage(chatTypeBound0, $$10);
                }
            }
            this.logPlayerMessage(playerChatMessage1, chatTypeBound0, gameProfile3, $$6);
            this.previousMessageTime = Util.getMillis();
            return true;
        } else {
            return false;
        }
    }

    private void narrateChatMessage(ChatType.Bound chatTypeBound0, Component component1) {
        this.minecraft.getNarrator().sayChat(chatTypeBound0.decorateNarration(component1));
    }

    private ChatTrustLevel evaluateTrustLevel(PlayerChatMessage playerChatMessage0, Component component1, Instant instant2) {
        return this.isSenderLocalPlayer(playerChatMessage0.sender()) ? ChatTrustLevel.SECURE : ChatTrustLevel.evaluate(playerChatMessage0, component1, instant2);
    }

    private void logPlayerMessage(PlayerChatMessage playerChatMessage0, ChatType.Bound chatTypeBound1, GameProfile gameProfile2, ChatTrustLevel chatTrustLevel3) {
        ChatLog $$4 = this.minecraft.getReportingContext().chatLog();
        $$4.push(LoggedChatMessage.player(gameProfile2, playerChatMessage0, chatTrustLevel3));
    }

    private void logSystemMessage(Component component0, Instant instant1) {
        ChatLog $$2 = this.minecraft.getReportingContext().chatLog();
        $$2.push(LoggedChatMessage.system(component0, instant1));
    }

    public void handleSystemMessage(Component component0, boolean boolean1) {
        if (!this.minecraft.options.hideMatchedNames().get() || !this.minecraft.isBlocked(this.guessChatUUID(component0))) {
            if (boolean1) {
                this.minecraft.gui.setOverlayMessage(component0, false);
            } else {
                this.minecraft.gui.getChat().addMessage(component0);
                this.logSystemMessage(component0, Instant.now());
            }
            this.minecraft.getNarrator().say(component0);
        }
    }

    private UUID guessChatUUID(Component component0) {
        String $$1 = StringDecomposer.getPlainText(component0);
        String $$2 = StringUtils.substringBetween($$1, "<", ">");
        return $$2 == null ? Util.NIL_UUID : this.minecraft.getPlayerSocialManager().getDiscoveredUUID($$2);
    }

    private boolean isSenderLocalPlayer(UUID uUID0) {
        if (this.minecraft.isLocalServer() && this.minecraft.player != null) {
            UUID $$1 = this.minecraft.player.m_36316_().getId();
            return $$1.equals(uUID0);
        } else {
            return false;
        }
    }

    static record Message(@Nullable MessageSignature f_244535_, BooleanSupplier f_244088_) {

        @Nullable
        private final MessageSignature signature;

        private final BooleanSupplier handler;

        Message(@Nullable MessageSignature f_244535_, BooleanSupplier f_244088_) {
            this.signature = f_244535_;
            this.handler = f_244088_;
        }

        public boolean accept() {
            return this.handler.getAsBoolean();
        }
    }
}