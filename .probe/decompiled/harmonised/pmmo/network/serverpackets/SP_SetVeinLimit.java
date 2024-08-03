package harmonised.pmmo.network.serverpackets;

import harmonised.pmmo.features.veinmining.VeinMiningLogic;
import harmonised.pmmo.util.MsLoggy;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SP_SetVeinLimit {

    int limit;

    public SP_SetVeinLimit(int limit) {
        this.limit = limit;
    }

    public SP_SetVeinLimit(FriendlyByteBuf buf) {
        this.limit = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.limit);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            VeinMiningLogic.maxBlocksPerPlayer.put(((NetworkEvent.Context) ctx.get()).getSender().m_20148_(), this.limit);
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "VeinLimit updated for: " + ((NetworkEvent.Context) ctx.get()).getSender().m_6302_() + " with: " + this.limit);
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}