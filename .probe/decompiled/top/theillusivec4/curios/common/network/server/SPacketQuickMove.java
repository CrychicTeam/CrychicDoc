package top.theillusivec4.curios.common.network.server;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.common.network.client.ClientPacketHandler;

public class SPacketQuickMove {

    public final int windowId;

    public final int moveIndex;

    public SPacketQuickMove(int windowId, int moveIndex) {
        this.windowId = windowId;
        this.moveIndex = moveIndex;
    }

    public static void encode(SPacketQuickMove msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.windowId);
        buf.writeInt(msg.moveIndex);
    }

    public static SPacketQuickMove decode(FriendlyByteBuf buf) {
        return new SPacketQuickMove(buf.readInt(), buf.readInt());
    }

    public static void handle(SPacketQuickMove msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handlePacket(msg)));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}