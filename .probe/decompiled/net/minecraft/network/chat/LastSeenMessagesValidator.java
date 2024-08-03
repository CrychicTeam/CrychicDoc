package net.minecraft.network.chat;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Optional;
import javax.annotation.Nullable;

public class LastSeenMessagesValidator {

    private final int lastSeenCount;

    private final ObjectList<LastSeenTrackedEntry> trackedMessages = new ObjectArrayList();

    @Nullable
    private MessageSignature lastPendingMessage;

    public LastSeenMessagesValidator(int int0) {
        this.lastSeenCount = int0;
        for (int $$1 = 0; $$1 < int0; $$1++) {
            this.trackedMessages.add(null);
        }
    }

    public void addPending(MessageSignature messageSignature0) {
        if (!messageSignature0.equals(this.lastPendingMessage)) {
            this.trackedMessages.add(new LastSeenTrackedEntry(messageSignature0, true));
            this.lastPendingMessage = messageSignature0;
        }
    }

    public int trackedMessagesCount() {
        return this.trackedMessages.size();
    }

    public boolean applyOffset(int int0) {
        int $$1 = this.trackedMessages.size() - this.lastSeenCount;
        if (int0 >= 0 && int0 <= $$1) {
            this.trackedMessages.removeElements(0, int0);
            return true;
        } else {
            return false;
        }
    }

    public Optional<LastSeenMessages> applyUpdate(LastSeenMessages.Update lastSeenMessagesUpdate0) {
        if (!this.applyOffset(lastSeenMessagesUpdate0.offset())) {
            return Optional.empty();
        } else {
            ObjectList<MessageSignature> $$1 = new ObjectArrayList(lastSeenMessagesUpdate0.acknowledged().cardinality());
            if (lastSeenMessagesUpdate0.acknowledged().length() > this.lastSeenCount) {
                return Optional.empty();
            } else {
                for (int $$2 = 0; $$2 < this.lastSeenCount; $$2++) {
                    boolean $$3 = lastSeenMessagesUpdate0.acknowledged().get($$2);
                    LastSeenTrackedEntry $$4 = (LastSeenTrackedEntry) this.trackedMessages.get($$2);
                    if ($$3) {
                        if ($$4 == null) {
                            return Optional.empty();
                        }
                        this.trackedMessages.set($$2, $$4.acknowledge());
                        $$1.add($$4.signature());
                    } else {
                        if ($$4 != null && !$$4.pending()) {
                            return Optional.empty();
                        }
                        this.trackedMessages.set($$2, null);
                    }
                }
                return Optional.of(new LastSeenMessages($$1));
            }
        }
    }
}