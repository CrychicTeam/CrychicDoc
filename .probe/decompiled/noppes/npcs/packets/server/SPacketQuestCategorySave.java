package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiUpdate;

public class SPacketQuestCategorySave extends PacketServerBasic {

    private CompoundTag data;

    public SPacketQuestCategorySave(CompoundTag data) {
        this.data = data;
    }

    public SPacketQuestCategorySave(FriendlyByteBuf buf) {
        this.data = buf.readNbt();
    }

    public static SPacketQuestCategorySave decode(FriendlyByteBuf buf) {
        return new SPacketQuestCategorySave(buf);
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_QUEST;
    }

    @Override
    public void handle() {
        QuestCategory category = new QuestCategory();
        category.readNBT(this.data);
        QuestController.instance.saveCategory(category);
        Packets.send(this.player, new PacketGuiUpdate());
    }

    public static void encode(SPacketQuestCategorySave msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
    }
}