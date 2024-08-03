package org.violetmoon.zeta.network.message;

import java.util.BitSet;
import org.violetmoon.zeta.config.SyncedFlagHandler;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class C2SUpdateFlag implements IZetaMessage {

    private static final long serialVersionUID = 5243197411999379903L;

    public BitSet flags;

    public int expectedLength;

    public int expectedHash;

    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        if (this.expectedLength == SyncedFlagHandler.expectedLength() && this.expectedHash == SyncedFlagHandler.expectedHash()) {
            SyncedFlagHandler.receiveFlagInfoFromPlayer(context.getSender(), this.flags);
        }
        return true;
    }

    public C2SUpdateFlag() {
    }

    private C2SUpdateFlag(BitSet flags, int expectedLength, int expectedHash) {
        this.flags = flags;
        this.expectedLength = expectedLength;
        this.expectedHash = expectedHash;
    }

    public static C2SUpdateFlag createPacket() {
        return new C2SUpdateFlag(SyncedFlagHandler.compileFlagInfo(), SyncedFlagHandler.expectedLength(), SyncedFlagHandler.expectedHash());
    }
}