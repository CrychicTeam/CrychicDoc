package top.theillusivec4.curios.common.network.server.sync;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ICuriosMenu;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.common.inventory.CurioStacksHandler;

public class SPacketSyncCurios {

    private int entityId;

    private int entrySize;

    private Map<String, CompoundTag> map;

    public SPacketSyncCurios(int entityId, Map<String, ICurioStacksHandler> map) {
        Map<String, CompoundTag> result = new LinkedHashMap();
        for (Entry<String, ICurioStacksHandler> entry : map.entrySet()) {
            result.put((String) entry.getKey(), ((ICurioStacksHandler) entry.getValue()).getSyncTag());
        }
        this.entityId = entityId;
        this.entrySize = map.size();
        this.map = result;
    }

    public SPacketSyncCurios(Map<String, CompoundTag> map, int entityId) {
        this.entityId = entityId;
        this.entrySize = map.size();
        this.map = map;
    }

    public static void encode(SPacketSyncCurios msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.entrySize);
        for (Entry<String, CompoundTag> entry : msg.map.entrySet()) {
            buf.writeUtf((String) entry.getKey());
            buf.writeNbt((CompoundTag) entry.getValue());
        }
    }

    public static SPacketSyncCurios decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int entrySize = buf.readInt();
        Map<String, CompoundTag> map = new LinkedHashMap();
        for (int i = 0; i < entrySize; i++) {
            String key = buf.readUtf();
            map.put(key, buf.readNbt());
        }
        return new SPacketSyncCurios(map, entityId);
    }

    public static void handle(SPacketSyncCurios msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                Entity entity = world.getEntity(msg.entityId);
                if (entity instanceof LivingEntity) {
                    CuriosApi.getCuriosInventory((LivingEntity) entity).ifPresent(handler -> {
                        Map<String, ICurioStacksHandler> stacks = new LinkedHashMap();
                        for (Entry<String, CompoundTag> entry : msg.map.entrySet()) {
                            ICurioStacksHandler stacksHandler = new CurioStacksHandler(handler, (String) entry.getKey());
                            stacksHandler.applySyncTag((CompoundTag) entry.getValue());
                            stacks.put((String) entry.getKey(), stacksHandler);
                        }
                        handler.setCurios(stacks);
                        if (entity instanceof LocalPlayer player && player.f_36096_ instanceof ICuriosMenu curiosContainer) {
                            curiosContainer.resetSlots();
                        }
                    });
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}