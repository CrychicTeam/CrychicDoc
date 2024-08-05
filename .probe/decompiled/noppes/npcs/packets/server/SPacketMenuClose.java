package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketMenuClose extends PacketServerBasic {

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_GUI;
    }

    public static void encode(SPacketMenuClose msg, FriendlyByteBuf buf) {
    }

    public static SPacketMenuClose decode(FriendlyByteBuf buf) {
        return new SPacketMenuClose();
    }

    @Override
    protected void handle() {
        this.npc.reset();
        if (this.npc.linkedData != null) {
            LinkedNpcController.Instance.saveNpcData(this.npc);
        }
        NoppesUtilServer.setEditingNpc(this.player, null);
    }
}