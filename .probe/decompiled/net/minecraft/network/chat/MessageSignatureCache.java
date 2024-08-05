package net.minecraft.network.chat;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

public class MessageSignatureCache {

    public static final int NOT_FOUND = -1;

    private static final int DEFAULT_CAPACITY = 128;

    private final MessageSignature[] entries;

    public MessageSignatureCache(int int0) {
        this.entries = new MessageSignature[int0];
    }

    public static MessageSignatureCache createDefault() {
        return new MessageSignatureCache(128);
    }

    public int pack(MessageSignature messageSignature0) {
        for (int $$1 = 0; $$1 < this.entries.length; $$1++) {
            if (messageSignature0.equals(this.entries[$$1])) {
                return $$1;
            }
        }
        return -1;
    }

    @Nullable
    public MessageSignature unpack(int int0) {
        return this.entries[int0];
    }

    public void push(PlayerChatMessage playerChatMessage0) {
        List<MessageSignature> $$1 = playerChatMessage0.signedBody().lastSeen().entries();
        ArrayDeque<MessageSignature> $$2 = new ArrayDeque($$1.size() + 1);
        $$2.addAll($$1);
        MessageSignature $$3 = playerChatMessage0.signature();
        if ($$3 != null) {
            $$2.add($$3);
        }
        this.push($$2);
    }

    @VisibleForTesting
    void push(List<MessageSignature> listMessageSignature0) {
        this.push(new ArrayDeque(listMessageSignature0));
    }

    private void push(ArrayDeque<MessageSignature> arrayDequeMessageSignature0) {
        Set<MessageSignature> $$1 = new ObjectOpenHashSet(arrayDequeMessageSignature0);
        for (int $$2 = 0; !arrayDequeMessageSignature0.isEmpty() && $$2 < this.entries.length; $$2++) {
            MessageSignature $$3 = this.entries[$$2];
            this.entries[$$2] = (MessageSignature) arrayDequeMessageSignature0.removeLast();
            if ($$3 != null && !$$1.contains($$3)) {
                arrayDequeMessageSignature0.addFirst($$3);
            }
        }
    }
}