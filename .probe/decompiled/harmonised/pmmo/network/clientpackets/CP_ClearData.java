package harmonised.pmmo.network.clientpackets;

import harmonised.pmmo.client.utils.ClientUtils;
import harmonised.pmmo.core.Core;
import java.util.function.Supplier;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class CP_ClearData {

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Core.get(LogicalSide.CLIENT).resetDataForReload();
            Core.get(LogicalSide.CLIENT).getLoader().resetData();
            Core.get(LogicalSide.CLIENT).getTooltipRegistry().clearRegistry();
            ClientUtils.invalidateUnlocksCache();
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}