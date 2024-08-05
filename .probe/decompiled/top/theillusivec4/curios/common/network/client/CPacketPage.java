package top.theillusivec4.curios.common.network.client;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;

public class CPacketPage {

    private final int windowId;

    private final boolean next;

    public CPacketPage(int windowId, boolean next) {
        this.windowId = windowId;
        this.next = next;
    }

    public static void encode(CPacketPage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.windowId);
        buf.writeBoolean(msg.next);
    }

    public static CPacketPage decode(FriendlyByteBuf buf) {
        return new CPacketPage(buf.readInt(), buf.readBoolean());
    }

    public static void handle(CPacketPage msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context) ctx.get()).getSender();
            if (sender != null) {
                AbstractContainerMenu container = sender.f_36096_;
                if (container instanceof CuriosContainerV2 && container.containerId == msg.windowId) {
                    if (msg.next) {
                        ((CuriosContainerV2) container).nextPage();
                    } else {
                        ((CuriosContainerV2) container).prevPage();
                    }
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}