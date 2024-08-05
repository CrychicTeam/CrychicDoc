package net.minecraft.client.gui.components;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Optionull;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.ChatVisiblity;
import org.slf4j.Logger;

public class ChatComponent {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int MAX_CHAT_HISTORY = 100;

    private static final int MESSAGE_NOT_FOUND = -1;

    private static final int MESSAGE_INDENT = 4;

    private static final int MESSAGE_TAG_MARGIN_LEFT = 4;

    private static final int BOTTOM_MARGIN = 40;

    private static final int TIME_BEFORE_MESSAGE_DELETION = 60;

    private static final Component DELETED_CHAT_MESSAGE = Component.translatable("chat.deleted_marker").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

    private final Minecraft minecraft;

    private final List<String> recentChat = Lists.newArrayList();

    private final List<GuiMessage> allMessages = Lists.newArrayList();

    private final List<GuiMessage.Line> trimmedMessages = Lists.newArrayList();

    private int chatScrollbarPos;

    private boolean newMessageSinceScroll;

    private final List<ChatComponent.DelayedMessageDeletion> messageDeletionQueue = new ArrayList();

    public ChatComponent(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void tick() {
        if (!this.messageDeletionQueue.isEmpty()) {
            this.processMessageDeletionQueue();
        }
    }

    public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3) {
        if (!this.isChatHidden()) {
            int $$4 = this.getLinesPerPage();
            int $$5 = this.trimmedMessages.size();
            if ($$5 > 0) {
                boolean $$6 = this.isChatFocused();
                float $$7 = (float) this.getScale();
                int $$8 = Mth.ceil((float) this.getWidth() / $$7);
                int $$9 = guiGraphics0.guiHeight();
                guiGraphics0.pose().pushPose();
                guiGraphics0.pose().scale($$7, $$7, 1.0F);
                guiGraphics0.pose().translate(4.0F, 0.0F, 0.0F);
                int $$10 = Mth.floor((float) ($$9 - 40) / $$7);
                int $$11 = this.getMessageEndIndexAt(this.screenToChatX((double) int2), this.screenToChatY((double) int3));
                double $$12 = this.minecraft.options.chatOpacity().get() * 0.9F + 0.1F;
                double $$13 = this.minecraft.options.textBackgroundOpacity().get();
                double $$14 = this.minecraft.options.chatLineSpacing().get();
                int $$15 = this.getLineHeight();
                int $$16 = (int) Math.round(-8.0 * ($$14 + 1.0) + 4.0 * $$14);
                int $$17 = 0;
                for (int $$18 = 0; $$18 + this.chatScrollbarPos < this.trimmedMessages.size() && $$18 < $$4; $$18++) {
                    int $$19 = $$18 + this.chatScrollbarPos;
                    GuiMessage.Line $$20 = (GuiMessage.Line) this.trimmedMessages.get($$19);
                    if ($$20 != null) {
                        int $$21 = int1 - $$20.addedTime();
                        if ($$21 < 200 || $$6) {
                            double $$22 = $$6 ? 1.0 : getTimeFactor($$21);
                            int $$23 = (int) (255.0 * $$22 * $$12);
                            int $$24 = (int) (255.0 * $$22 * $$13);
                            $$17++;
                            if ($$23 > 3) {
                                int $$25 = 0;
                                int $$26 = $$10 - $$18 * $$15;
                                int $$27 = $$26 + $$16;
                                guiGraphics0.pose().pushPose();
                                guiGraphics0.pose().translate(0.0F, 0.0F, 50.0F);
                                guiGraphics0.fill(-4, $$26 - $$15, 0 + $$8 + 4 + 4, $$26, $$24 << 24);
                                GuiMessageTag $$28 = $$20.tag();
                                if ($$28 != null) {
                                    int $$29 = $$28.indicatorColor() | $$23 << 24;
                                    guiGraphics0.fill(-4, $$26 - $$15, -2, $$26, $$29);
                                    if ($$19 == $$11 && $$28.icon() != null) {
                                        int $$30 = this.getTagIconLeft($$20);
                                        int $$31 = $$27 + 9;
                                        this.drawTagIcon(guiGraphics0, $$30, $$31, $$28.icon());
                                    }
                                }
                                guiGraphics0.pose().translate(0.0F, 0.0F, 50.0F);
                                guiGraphics0.drawString(this.minecraft.font, $$20.content(), 0, $$27, 16777215 + ($$23 << 24));
                                guiGraphics0.pose().popPose();
                            }
                        }
                    }
                }
                long $$32 = this.minecraft.getChatListener().queueSize();
                if ($$32 > 0L) {
                    int $$33 = (int) (128.0 * $$12);
                    int $$34 = (int) (255.0 * $$13);
                    guiGraphics0.pose().pushPose();
                    guiGraphics0.pose().translate(0.0F, (float) $$10, 50.0F);
                    guiGraphics0.fill(-2, 0, $$8 + 4, 9, $$34 << 24);
                    guiGraphics0.pose().translate(0.0F, 0.0F, 50.0F);
                    guiGraphics0.drawString(this.minecraft.font, Component.translatable("chat.queue", $$32), 0, 1, 16777215 + ($$33 << 24));
                    guiGraphics0.pose().popPose();
                }
                if ($$6) {
                    int $$35 = this.getLineHeight();
                    int $$36 = $$5 * $$35;
                    int $$37 = $$17 * $$35;
                    int $$38 = this.chatScrollbarPos * $$37 / $$5 - $$10;
                    int $$39 = $$37 * $$37 / $$36;
                    if ($$36 != $$37) {
                        int $$40 = $$38 > 0 ? 170 : 96;
                        int $$41 = this.newMessageSinceScroll ? 13382451 : 3355562;
                        int $$42 = $$8 + 4;
                        guiGraphics0.fill($$42, -$$38, $$42 + 2, -$$38 - $$39, $$41 + ($$40 << 24));
                        guiGraphics0.fill($$42 + 2, -$$38, $$42 + 1, -$$38 - $$39, 13421772 + ($$40 << 24));
                    }
                }
                guiGraphics0.pose().popPose();
            }
        }
    }

    private void drawTagIcon(GuiGraphics guiGraphics0, int int1, int int2, GuiMessageTag.Icon guiMessageTagIcon3) {
        int $$4 = int2 - guiMessageTagIcon3.height - 1;
        guiMessageTagIcon3.draw(guiGraphics0, int1, $$4);
    }

    private int getTagIconLeft(GuiMessage.Line guiMessageLine0) {
        return this.minecraft.font.width(guiMessageLine0.content()) + 4;
    }

    private boolean isChatHidden() {
        return this.minecraft.options.chatVisibility().get() == ChatVisiblity.HIDDEN;
    }

    private static double getTimeFactor(int int0) {
        double $$1 = (double) int0 / 200.0;
        $$1 = 1.0 - $$1;
        $$1 *= 10.0;
        $$1 = Mth.clamp($$1, 0.0, 1.0);
        return $$1 * $$1;
    }

    public void clearMessages(boolean boolean0) {
        this.minecraft.getChatListener().clearQueue();
        this.messageDeletionQueue.clear();
        this.trimmedMessages.clear();
        this.allMessages.clear();
        if (boolean0) {
            this.recentChat.clear();
        }
    }

    public void addMessage(Component component0) {
        this.addMessage(component0, null, this.minecraft.isSingleplayer() ? GuiMessageTag.systemSinglePlayer() : GuiMessageTag.system());
    }

    public void addMessage(Component component0, @Nullable MessageSignature messageSignature1, @Nullable GuiMessageTag guiMessageTag2) {
        this.logChatMessage(component0, guiMessageTag2);
        this.addMessage(component0, messageSignature1, this.minecraft.gui.getGuiTicks(), guiMessageTag2, false);
    }

    private void logChatMessage(Component component0, @Nullable GuiMessageTag guiMessageTag1) {
        String $$2 = component0.getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
        String $$3 = Optionull.map(guiMessageTag1, GuiMessageTag::f_240342_);
        if ($$3 != null) {
            LOGGER.info("[{}] [CHAT] {}", $$3, $$2);
        } else {
            LOGGER.info("[CHAT] {}", $$2);
        }
    }

    private void addMessage(Component component0, @Nullable MessageSignature messageSignature1, int int2, @Nullable GuiMessageTag guiMessageTag3, boolean boolean4) {
        int $$5 = Mth.floor((double) this.getWidth() / this.getScale());
        if (guiMessageTag3 != null && guiMessageTag3.icon() != null) {
            $$5 -= guiMessageTag3.icon().width + 4 + 2;
        }
        List<FormattedCharSequence> $$6 = ComponentRenderUtils.wrapComponents(component0, $$5, this.minecraft.font);
        boolean $$7 = this.isChatFocused();
        for (int $$8 = 0; $$8 < $$6.size(); $$8++) {
            FormattedCharSequence $$9 = (FormattedCharSequence) $$6.get($$8);
            if ($$7 && this.chatScrollbarPos > 0) {
                this.newMessageSinceScroll = true;
                this.scrollChat(1);
            }
            boolean $$10 = $$8 == $$6.size() - 1;
            this.trimmedMessages.add(0, new GuiMessage.Line(int2, $$9, guiMessageTag3, $$10));
        }
        while (this.trimmedMessages.size() > 100) {
            this.trimmedMessages.remove(this.trimmedMessages.size() - 1);
        }
        if (!boolean4) {
            this.allMessages.add(0, new GuiMessage(int2, component0, messageSignature1, guiMessageTag3));
            while (this.allMessages.size() > 100) {
                this.allMessages.remove(this.allMessages.size() - 1);
            }
        }
    }

    private void processMessageDeletionQueue() {
        int $$0 = this.minecraft.gui.getGuiTicks();
        this.messageDeletionQueue.removeIf(p_250713_ -> $$0 >= p_250713_.deletableAfter() ? this.deleteMessageOrDelay(p_250713_.signature()) == null : false);
    }

    public void deleteMessage(MessageSignature messageSignature0) {
        ChatComponent.DelayedMessageDeletion $$1 = this.deleteMessageOrDelay(messageSignature0);
        if ($$1 != null) {
            this.messageDeletionQueue.add($$1);
        }
    }

    @Nullable
    private ChatComponent.DelayedMessageDeletion deleteMessageOrDelay(MessageSignature messageSignature0) {
        int $$1 = this.minecraft.gui.getGuiTicks();
        ListIterator<GuiMessage> $$2 = this.allMessages.listIterator();
        while ($$2.hasNext()) {
            GuiMessage $$3 = (GuiMessage) $$2.next();
            if (messageSignature0.equals($$3.signature())) {
                int $$4 = $$3.addedTime() + 60;
                if ($$1 >= $$4) {
                    $$2.set(this.createDeletedMarker($$3));
                    this.refreshTrimmedMessage();
                    return null;
                }
                return new ChatComponent.DelayedMessageDeletion(messageSignature0, $$4);
            }
        }
        return null;
    }

    private GuiMessage createDeletedMarker(GuiMessage guiMessage0) {
        return new GuiMessage(guiMessage0.addedTime(), DELETED_CHAT_MESSAGE, null, GuiMessageTag.system());
    }

    public void rescaleChat() {
        this.resetChatScroll();
        this.refreshTrimmedMessage();
    }

    private void refreshTrimmedMessage() {
        this.trimmedMessages.clear();
        for (int $$0 = this.allMessages.size() - 1; $$0 >= 0; $$0--) {
            GuiMessage $$1 = (GuiMessage) this.allMessages.get($$0);
            this.addMessage($$1.content(), $$1.signature(), $$1.addedTime(), $$1.tag(), true);
        }
    }

    public List<String> getRecentChat() {
        return this.recentChat;
    }

    public void addRecentChat(String string0) {
        if (this.recentChat.isEmpty() || !((String) this.recentChat.get(this.recentChat.size() - 1)).equals(string0)) {
            this.recentChat.add(string0);
        }
    }

    public void resetChatScroll() {
        this.chatScrollbarPos = 0;
        this.newMessageSinceScroll = false;
    }

    public void scrollChat(int int0) {
        this.chatScrollbarPos += int0;
        int $$1 = this.trimmedMessages.size();
        if (this.chatScrollbarPos > $$1 - this.getLinesPerPage()) {
            this.chatScrollbarPos = $$1 - this.getLinesPerPage();
        }
        if (this.chatScrollbarPos <= 0) {
            this.chatScrollbarPos = 0;
            this.newMessageSinceScroll = false;
        }
    }

    public boolean handleChatQueueClicked(double double0, double double1) {
        if (this.isChatFocused() && !this.minecraft.options.hideGui && !this.isChatHidden()) {
            ChatListener $$2 = this.minecraft.getChatListener();
            if ($$2.queueSize() == 0L) {
                return false;
            } else {
                double $$3 = double0 - 2.0;
                double $$4 = (double) this.minecraft.getWindow().getGuiScaledHeight() - double1 - 40.0;
                if ($$3 <= (double) Mth.floor((double) this.getWidth() / this.getScale()) && $$4 < 0.0 && $$4 > (double) Mth.floor(-9.0 * this.getScale())) {
                    $$2.acceptNextDelayedMessage();
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Nullable
    public Style getClickedComponentStyleAt(double double0, double double1) {
        double $$2 = this.screenToChatX(double0);
        double $$3 = this.screenToChatY(double1);
        int $$4 = this.getMessageLineIndexAt($$2, $$3);
        if ($$4 >= 0 && $$4 < this.trimmedMessages.size()) {
            GuiMessage.Line $$5 = (GuiMessage.Line) this.trimmedMessages.get($$4);
            return this.minecraft.font.getSplitter().componentStyleAtWidth($$5.content(), Mth.floor($$2));
        } else {
            return null;
        }
    }

    @Nullable
    public GuiMessageTag getMessageTagAt(double double0, double double1) {
        double $$2 = this.screenToChatX(double0);
        double $$3 = this.screenToChatY(double1);
        int $$4 = this.getMessageEndIndexAt($$2, $$3);
        if ($$4 >= 0 && $$4 < this.trimmedMessages.size()) {
            GuiMessage.Line $$5 = (GuiMessage.Line) this.trimmedMessages.get($$4);
            GuiMessageTag $$6 = $$5.tag();
            if ($$6 != null && this.hasSelectedMessageTag($$2, $$5, $$6)) {
                return $$6;
            }
        }
        return null;
    }

    private boolean hasSelectedMessageTag(double double0, GuiMessage.Line guiMessageLine1, GuiMessageTag guiMessageTag2) {
        if (double0 < 0.0) {
            return true;
        } else {
            GuiMessageTag.Icon $$3 = guiMessageTag2.icon();
            if ($$3 == null) {
                return false;
            } else {
                int $$4 = this.getTagIconLeft(guiMessageLine1);
                int $$5 = $$4 + $$3.width;
                return double0 >= (double) $$4 && double0 <= (double) $$5;
            }
        }
    }

    private double screenToChatX(double double0) {
        return double0 / this.getScale() - 4.0;
    }

    private double screenToChatY(double double0) {
        double $$1 = (double) this.minecraft.getWindow().getGuiScaledHeight() - double0 - 40.0;
        return $$1 / (this.getScale() * (double) this.getLineHeight());
    }

    private int getMessageEndIndexAt(double double0, double double1) {
        int $$2 = this.getMessageLineIndexAt(double0, double1);
        if ($$2 == -1) {
            return -1;
        } else {
            while ($$2 >= 0) {
                if (((GuiMessage.Line) this.trimmedMessages.get($$2)).endOfEntry()) {
                    return $$2;
                }
                $$2--;
            }
            return $$2;
        }
    }

    private int getMessageLineIndexAt(double double0, double double1) {
        if (this.isChatFocused() && !this.minecraft.options.hideGui && !this.isChatHidden()) {
            if (!(double0 < -4.0) && !(double0 > (double) Mth.floor((double) this.getWidth() / this.getScale()))) {
                int $$2 = Math.min(this.getLinesPerPage(), this.trimmedMessages.size());
                if (double1 >= 0.0 && double1 < (double) $$2) {
                    int $$3 = Mth.floor(double1 + (double) this.chatScrollbarPos);
                    if ($$3 >= 0 && $$3 < this.trimmedMessages.size()) {
                        return $$3;
                    }
                }
                return -1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    private boolean isChatFocused() {
        return this.minecraft.screen instanceof ChatScreen;
    }

    public int getWidth() {
        return getWidth(this.minecraft.options.chatWidth().get());
    }

    public int getHeight() {
        return getHeight(this.isChatFocused() ? this.minecraft.options.chatHeightFocused().get() : this.minecraft.options.chatHeightUnfocused().get());
    }

    public double getScale() {
        return this.minecraft.options.chatScale().get();
    }

    public static int getWidth(double double0) {
        int $$1 = 320;
        int $$2 = 40;
        return Mth.floor(double0 * 280.0 + 40.0);
    }

    public static int getHeight(double double0) {
        int $$1 = 180;
        int $$2 = 20;
        return Mth.floor(double0 * 160.0 + 20.0);
    }

    public static double defaultUnfocusedPct() {
        int $$0 = 180;
        int $$1 = 20;
        return 70.0 / (double) (getHeight(1.0) - 20);
    }

    public int getLinesPerPage() {
        return this.getHeight() / this.getLineHeight();
    }

    private int getLineHeight() {
        return (int) (9.0 * (this.minecraft.options.chatLineSpacing().get() + 1.0));
    }

    static record DelayedMessageDeletion(MessageSignature f_244186_, int f_244411_) {

        private final MessageSignature signature;

        private final int deletableAfter;

        DelayedMessageDeletion(MessageSignature f_244186_, int f_244411_) {
            this.signature = f_244186_;
            this.deletableAfter = f_244411_;
        }
    }
}