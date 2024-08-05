package top.theillusivec4.curios.common.network.client;

import java.util.function.Supplier;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncRender;

public class CPacketToggleRender {

    String id;

    int index;

    public CPacketToggleRender(String id, int index) {
        this.id = id;
        this.index = index;
    }

    public static void encode(CPacketToggleRender msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.id);
        buf.writeInt(msg.index);
    }

    public static CPacketToggleRender decode(FriendlyByteBuf buf) {
        return new CPacketToggleRender(buf.readUtf(), buf.readInt());
    }

    public static void handle(CPacketToggleRender msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context) ctx.get()).getSender();
            if (sender != null) {
                CuriosApi.getCuriosInventory(sender).ifPresent(handler -> handler.getStacksHandler(msg.id).ifPresent(stacksHandler -> {
                    NonNullList<Boolean> renderStatuses = stacksHandler.getRenders();
                    if (renderStatuses.size() > msg.index) {
                        boolean value = !renderStatuses.get(msg.index);
                        renderStatuses.set(msg.index, value);
                        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> sender), new SPacketSyncRender(sender.m_19879_(), msg.id, msg.index, value));
                    }
                }));
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}