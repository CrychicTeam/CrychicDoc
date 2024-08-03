package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketNpcFactionSet extends PacketServerBasic {

    private int faction;

    public SPacketNpcFactionSet(int faction) {
        this.faction = faction;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_ADVANCED;
    }

    public static void encode(SPacketNpcFactionSet msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.faction);
    }

    public static SPacketNpcFactionSet decode(FriendlyByteBuf buf) {
        return new SPacketNpcFactionSet(buf.readInt());
    }

    @Override
    protected void handle() {
        this.npc.setFaction(this.faction);
    }
}