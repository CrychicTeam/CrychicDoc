package harmonised.pmmo.network.serverpackets;

import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.MsLoggy;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class SP_UpdateVeinTarget {

    BlockPos pos;

    public SP_UpdateVeinTarget(BlockPos pos) {
        this.pos = pos;
    }

    public SP_UpdateVeinTarget(FriendlyByteBuf buf) {
        this.pos = BlockPos.of(buf.readLong());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(this.pos.asLong());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Vein Target sent to Server for pos: " + this.pos.toString());
            Core.get(LogicalSide.SERVER).setMarkedPos(((NetworkEvent.Context) ctx.get()).getSender().m_20148_(), this.pos);
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}