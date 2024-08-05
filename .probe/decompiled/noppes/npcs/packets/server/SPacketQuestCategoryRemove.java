package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiUpdate;

public class SPacketQuestCategoryRemove extends PacketServerBasic {

    private int id;

    public SPacketQuestCategoryRemove(int id) {
        this.id = id;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_QUEST;
    }

    public static void encode(SPacketQuestCategoryRemove msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static SPacketQuestCategoryRemove decode(FriendlyByteBuf buf) {
        return new SPacketQuestCategoryRemove(buf.readInt());
    }

    @Override
    protected void handle() {
        QuestController.instance.removeCategory(this.id);
        Packets.send(this.player, new PacketGuiUpdate());
    }
}