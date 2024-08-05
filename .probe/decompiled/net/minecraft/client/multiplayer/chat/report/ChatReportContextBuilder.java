package net.minecraft.client.multiplayer.chat.report;

import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.multiplayer.chat.ChatLog;
import net.minecraft.client.multiplayer.chat.LoggedChatEvent;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.PlayerChatMessage;

public class ChatReportContextBuilder {

    final int leadingCount;

    private final List<ChatReportContextBuilder.Collector> activeCollectors = new ArrayList();

    public ChatReportContextBuilder(int int0) {
        this.leadingCount = int0;
    }

    public void collectAllContext(ChatLog chatLog0, IntCollection intCollection1, ChatReportContextBuilder.Handler chatReportContextBuilderHandler2) {
        IntSortedSet $$3 = new IntRBTreeSet(intCollection1);
        for (int $$4 = $$3.lastInt(); $$4 >= chatLog0.start() && (this.isActive() || !$$3.isEmpty()); $$4--) {
            LoggedChatEvent $$6 = chatLog0.lookup($$4);
            if ($$6 instanceof LoggedChatMessage.Player) {
                LoggedChatMessage.Player $$5 = (LoggedChatMessage.Player) $$6;
                boolean $$6x = this.acceptContext($$5.message());
                if ($$3.remove($$4)) {
                    this.trackContext($$5.message());
                    chatReportContextBuilderHandler2.accept($$4, $$5);
                } else if ($$6x) {
                    chatReportContextBuilderHandler2.accept($$4, $$5);
                }
            }
        }
    }

    public void trackContext(PlayerChatMessage playerChatMessage0) {
        this.activeCollectors.add(new ChatReportContextBuilder.Collector(playerChatMessage0));
    }

    public boolean acceptContext(PlayerChatMessage playerChatMessage0) {
        boolean $$1 = false;
        Iterator<ChatReportContextBuilder.Collector> $$2 = this.activeCollectors.iterator();
        while ($$2.hasNext()) {
            ChatReportContextBuilder.Collector $$3 = (ChatReportContextBuilder.Collector) $$2.next();
            if ($$3.accept(playerChatMessage0)) {
                $$1 = true;
                if ($$3.isComplete()) {
                    $$2.remove();
                }
            }
        }
        return $$1;
    }

    public boolean isActive() {
        return !this.activeCollectors.isEmpty();
    }

    class Collector {

        private final Set<MessageSignature> lastSeenSignatures;

        private PlayerChatMessage lastChainMessage;

        private boolean collectingChain = true;

        private int count;

        Collector(PlayerChatMessage playerChatMessage0) {
            this.lastSeenSignatures = new ObjectOpenHashSet(playerChatMessage0.signedBody().lastSeen().entries());
            this.lastChainMessage = playerChatMessage0;
        }

        boolean accept(PlayerChatMessage playerChatMessage0) {
            if (playerChatMessage0.equals(this.lastChainMessage)) {
                return false;
            } else {
                boolean $$1 = this.lastSeenSignatures.remove(playerChatMessage0.signature());
                if (this.collectingChain && this.lastChainMessage.sender().equals(playerChatMessage0.sender())) {
                    if (this.lastChainMessage.link().isDescendantOf(playerChatMessage0.link())) {
                        $$1 = true;
                        this.lastChainMessage = playerChatMessage0;
                    } else {
                        this.collectingChain = false;
                    }
                }
                if ($$1) {
                    this.count++;
                }
                return $$1;
            }
        }

        boolean isComplete() {
            return this.count >= ChatReportContextBuilder.this.leadingCount || !this.collectingChain && this.lastSeenSignatures.isEmpty();
        }
    }

    public interface Handler {

        void accept(int var1, LoggedChatMessage.Player var2);
    }
}