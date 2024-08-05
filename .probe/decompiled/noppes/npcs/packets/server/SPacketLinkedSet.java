package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketLinkedSet extends PacketServerBasic {

    private String name;

    public SPacketLinkedSet(String name) {
        this.name = name;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_ADVANCED;
    }

    public static void encode(SPacketLinkedSet msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.name, 32767);
    }

    public static SPacketLinkedSet decode(FriendlyByteBuf buf) {
        return new SPacketLinkedSet(buf.readUtf(32767));
    }

    @Override
    protected void handle() {
        this.npc.linkedName = this.name;
        LinkedNpcController.Instance.loadNpcData(this.npc);
    }
}