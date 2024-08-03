package org.violetmoon.zeta.network.message;

import java.util.BitSet;
import org.violetmoon.zeta.config.SyncedFlagHandler;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;
import org.violetmoon.zeta.network.ZetaHandshakeMessage;

public class C2SLoginFlag extends ZetaHandshakeMessage {

    public BitSet flags = SyncedFlagHandler.compileFlagInfo();

    public int expectedLength = SyncedFlagHandler.expectedLength();

    public int expectedHash = SyncedFlagHandler.expectedHash();

    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        if (this.expectedLength == SyncedFlagHandler.expectedLength() && this.expectedHash == SyncedFlagHandler.expectedHash()) {
            SyncedFlagHandler.receiveFlagInfoFromPlayer(context.getSender(), this.flags);
        }
        return true;
    }
}