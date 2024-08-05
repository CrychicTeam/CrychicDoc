package net.minecraft.network.chat;

public record LastSeenTrackedEntry(MessageSignature f_243846_, boolean f_243942_) {

    private final MessageSignature signature;

    private final boolean pending;

    public LastSeenTrackedEntry(MessageSignature f_243846_, boolean f_243942_) {
        this.signature = f_243846_;
        this.pending = f_243942_;
    }

    public LastSeenTrackedEntry acknowledge() {
        return this.pending ? new LastSeenTrackedEntry(this.signature, false) : this;
    }
}