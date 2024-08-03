package org.violetmoon.zeta.network.message;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.violetmoon.zeta.config.SyncedFlagHandler;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;
import org.violetmoon.zeta.network.ZetaHandshakeMessage;

public class S2CLoginFlag extends ZetaHandshakeMessage {

    public BitSet flags = SyncedFlagHandler.compileFlagInfo();

    public int expectedLength = SyncedFlagHandler.expectedLength();

    public int expectedHash = SyncedFlagHandler.expectedHash();

    public static List<Pair<String, S2CLoginFlag>> generateRegistryPackets(boolean isLocal) {
        return !isLocal ? Collections.singletonList(Pair.of(S2CLoginFlag.class.getName(), new S2CLoginFlag())) : Collections.emptyList();
    }

    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        if (this.expectedLength == SyncedFlagHandler.expectedLength() && this.expectedHash == SyncedFlagHandler.expectedHash()) {
            SyncedFlagHandler.receiveFlagInfoFromServer(this.flags);
        }
        context.reply(new C2SLoginFlag());
        return true;
    }
}