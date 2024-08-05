package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiUpdate;

public class SPacketQuestRemove extends PacketServerBasic {

    private int id;

    public SPacketQuestRemove(int id) {
        this.id = id;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_QUEST;
    }

    public static void encode(SPacketQuestRemove msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static SPacketQuestRemove decode(FriendlyByteBuf buf) {
        return new SPacketQuestRemove(buf.readInt());
    }

    @Override
    protected void handle() {
        Quest quest = (Quest) QuestController.instance.quests.get(this.id);
        if (quest != null) {
            QuestController.instance.removeQuest(quest);
            Packets.send(this.player, new PacketGuiUpdate());
        }
    }
}