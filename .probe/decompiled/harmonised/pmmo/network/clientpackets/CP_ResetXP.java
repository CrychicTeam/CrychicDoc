package harmonised.pmmo.network.clientpackets;

import harmonised.pmmo.core.Core;
import java.util.HashMap;
import java.util.function.Supplier;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class CP_ResetXP {

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> Core.get(LogicalSide.CLIENT).getData().setXpMap(null, new HashMap()));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}