package top.theillusivec4.curios.common.network.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.common.data.CuriosSlotManager;
import top.theillusivec4.curios.server.command.CurioArgumentType;

public class SPacketSetIcons {

    private int entrySize;

    private Map<String, ResourceLocation> map;

    public SPacketSetIcons(Map<String, ResourceLocation> map) {
        this.entrySize = map.size();
        this.map = map;
    }

    public static void encode(SPacketSetIcons msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entrySize);
        for (Entry<String, ResourceLocation> entry : msg.map.entrySet()) {
            buf.writeUtf((String) entry.getKey());
            buf.writeUtf(((ResourceLocation) entry.getValue()).toString());
        }
    }

    public static SPacketSetIcons decode(FriendlyByteBuf buf) {
        int entrySize = buf.readInt();
        Map<String, ResourceLocation> map = new HashMap();
        for (int i = 0; i < entrySize; i++) {
            map.put(buf.readUtf(), new ResourceLocation(buf.readUtf()));
        }
        return new SPacketSetIcons(map);
    }

    public static void handle(SPacketSetIcons msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            Set<String> slotIds = new HashSet();
            if (world != null) {
                CuriosApi.getIconHelper().clearIcons();
                Map<String, ResourceLocation> icons = new HashMap();
                for (Entry<String, ResourceLocation> entry : msg.map.entrySet()) {
                    CuriosApi.getIconHelper().addIcon((String) entry.getKey(), (ResourceLocation) entry.getValue());
                    icons.put((String) entry.getKey(), (ResourceLocation) entry.getValue());
                    slotIds.add((String) entry.getKey());
                }
                CuriosSlotManager.CLIENT.setIcons(icons);
            }
            CurioArgumentType.slotIds = slotIds;
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}