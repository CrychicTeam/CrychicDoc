package noppes.npcs.packets.server;

import java.text.DecimalFormat;
import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiScrollSelected;

public class SPacketRemoteNpcsGet extends PacketServerBasic {

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_GUI;
    }

    public static void encode(SPacketRemoteNpcsGet msg, FriendlyByteBuf buf) {
    }

    public static SPacketRemoteNpcsGet decode(FriendlyByteBuf buf) {
        return new SPacketRemoteNpcsGet();
    }

    @Override
    protected void handle() {
        sendNearbyNpcs(this.player);
        Packets.send(this.player, new PacketGuiScrollSelected(CustomNpcs.FreezeNPCs ? "Unfreeze Npcs" : "Freeze Npcs"));
    }

    public static void sendNearbyNpcs(ServerPlayer player) {
        HashMap<String, Integer> map = new HashMap();
        for (Entity entity : ((ServerLevel) player.m_9236_()).getAllEntities()) {
            if (entity instanceof EntityNPCInterface) {
                EntityNPCInterface npc = (EntityNPCInterface) entity;
                if (!npc.m_213877_()) {
                    float distance = player.m_20270_(npc);
                    DecimalFormat df = new DecimalFormat("#.#");
                    String s = df.format((double) distance);
                    if (distance < 10.0F) {
                        s = "0" + s;
                    }
                    map.put(s + " : " + npc.display.getName(), npc.m_19879_());
                }
            }
        }
        NoppesUtilServer.sendScrollData(player, map);
    }
}