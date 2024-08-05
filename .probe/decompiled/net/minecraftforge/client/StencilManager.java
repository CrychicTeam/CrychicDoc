package net.minecraftforge.client;

import java.util.BitSet;

public final class StencilManager {

    private static final BitSet BITS = new BitSet(8);

    public static int reserveBit() {
        int bit = BITS.nextClearBit(0);
        if (bit >= 0) {
            BITS.set(bit);
        }
        return bit;
    }

    public static void releaseBit(int bit) {
        if (bit >= 0 && bit < BITS.length()) {
            BITS.clear(bit);
        }
    }

    private StencilManager() {
    }
}