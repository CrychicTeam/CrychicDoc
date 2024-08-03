package harmonised.pmmo.network.clientpackets;

import harmonised.pmmo.client.utils.DataMirror;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.MsLoggy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class CP_UpdateLevelCache {

    List<Long> levelCache = new ArrayList();

    public CP_UpdateLevelCache(List<Long> levelCache) {
        this.levelCache = levelCache;
    }

    public CP_UpdateLevelCache(FriendlyByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            this.levelCache.add(buf.readLong());
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.levelCache.size());
        for (int i = 0; i < this.levelCache.size(); i++) {
            buf.writeLong((Long) this.levelCache.get(i));
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ((DataMirror) Core.get(LogicalSide.CLIENT).getData()).setLevelCache(this.levelCache);
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.XP, "Client Packet Handled for updating levelCache");
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}