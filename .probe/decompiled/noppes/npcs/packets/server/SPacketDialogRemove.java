package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiUpdate;

public class SPacketDialogRemove extends PacketServerBasic {

    private int id;

    public SPacketDialogRemove(int id) {
        this.id = id;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_DIALOG;
    }

    public static void encode(SPacketDialogRemove msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static SPacketDialogRemove decode(FriendlyByteBuf buf) {
        return new SPacketDialogRemove(buf.readInt());
    }

    @Override
    protected void handle() {
        Dialog dialog = (Dialog) DialogController.instance.dialogs.get(this.id);
        if (dialog != null && dialog.category != null) {
            DialogController.instance.removeDialog(dialog);
            Packets.send(this.player, new PacketGuiUpdate());
        }
    }
}