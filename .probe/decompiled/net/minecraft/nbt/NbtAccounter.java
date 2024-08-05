package net.minecraft.nbt;

import com.google.common.annotations.VisibleForTesting;

public class NbtAccounter {

    public static final NbtAccounter UNLIMITED = new NbtAccounter(0L) {

        @Override
        public void accountBytes(long p_128927_) {
        }
    };

    private final long quota;

    private long usage;

    public NbtAccounter(long long0) {
        this.quota = long0;
    }

    public void accountBytes(long long0) {
        this.usage += long0;
        if (this.usage > this.quota) {
            throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.usage + "bytes where max allowed: " + this.quota);
        }
    }

    @VisibleForTesting
    public long getUsage() {
        return this.usage;
    }
}