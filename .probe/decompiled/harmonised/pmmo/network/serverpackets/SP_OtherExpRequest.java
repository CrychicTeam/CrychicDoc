package harmonised.pmmo.network.serverpackets;

import harmonised.pmmo.core.Core;
import harmonised.pmmo.network.Networking;
import harmonised.pmmo.network.clientpackets.CP_SetOtherExperience;
import harmonised.pmmo.util.MsLoggy;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class SP_OtherExpRequest {

    UUID pid;

    public SP_OtherExpRequest(UUID playerID) {
        this.pid = playerID;
    }

    public SP_OtherExpRequest(FriendlyByteBuf buf) {
        this.pid = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.pid);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Networking.sendToClient(new CP_SetOtherExperience(Core.get(LogicalSide.SERVER).getData().getXpMap(this.pid)), ((NetworkEvent.Context) ctx.get()).getSender());
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.NETWORK, "Client request for Other Experience handled");
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}