package top.theillusivec4.curios.common.network.server.sync;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class SPacketSyncRender {

    private int entityId;

    private int slotId;

    private String curioId;

    private boolean value;

    public SPacketSyncRender(int entityId, String curioId, int slotId, boolean value) {
        this.entityId = entityId;
        this.slotId = slotId;
        this.curioId = curioId;
        this.value = value;
    }

    public static void encode(SPacketSyncRender msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeUtf(msg.curioId);
        buf.writeInt(msg.slotId);
        buf.writeBoolean(msg.value);
    }

    public static SPacketSyncRender decode(FriendlyByteBuf buf) {
        return new SPacketSyncRender(buf.readInt(), buf.readUtf(), buf.readInt(), buf.readBoolean());
    }

    public static void handle(SPacketSyncRender msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                Entity entity = world.getEntity(msg.entityId);
                if (entity instanceof LivingEntity) {
                    CuriosApi.getCuriosInventory((LivingEntity) entity).ifPresent(handler -> handler.getStacksHandler(msg.curioId).ifPresent(stacksHandler -> {
                        int index = msg.slotId;
                        NonNullList<Boolean> renderStatuses = stacksHandler.getRenders();
                        if (renderStatuses.size() > index) {
                            renderStatuses.set(index, msg.value);
                        }
                    }));
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}