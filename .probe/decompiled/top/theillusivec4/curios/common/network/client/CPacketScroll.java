package top.theillusivec4.curios.common.network.client;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;

public class CPacketScroll {

    private int windowId;

    private int index;

    public CPacketScroll(int windowId, int index) {
        this.windowId = windowId;
        this.index = index;
    }

    public static void encode(CPacketScroll msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.windowId);
        buf.writeInt(msg.index);
    }

    public static CPacketScroll decode(FriendlyByteBuf buf) {
        return new CPacketScroll(buf.readInt(), buf.readInt());
    }

    public static void handle(CPacketScroll msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context) ctx.get()).getSender();
            if (sender != null) {
                AbstractContainerMenu container = sender.f_36096_;
                if (container instanceof CuriosContainer && container.containerId == msg.windowId) {
                    ((CuriosContainer) container).scrollToIndex(msg.index);
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}