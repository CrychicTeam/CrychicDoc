package noppes.npcs.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.shared.common.PacketBasic;

public class PacketSyncRemove extends PacketBasic {

    private final int id;

    private final int type;

    public PacketSyncRemove(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public static void encode(PacketSyncRemove msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeInt(msg.type);
    }

    public static PacketSyncRemove decode(FriendlyByteBuf buf) {
        return new PacketSyncRemove(buf.readInt(), buf.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        if (this.type == 1) {
            FactionController.instance.factions.remove(this.id);
        } else if (this.type == 4) {
            Dialog dialog = (Dialog) DialogController.instance.dialogs.remove(this.id);
            if (dialog != null) {
                dialog.category.dialogs.remove(this.id);
            }
        } else if (this.type == 5) {
            DialogCategory category = (DialogCategory) DialogController.instance.categories.remove(this.id);
            if (category != null) {
                DialogController.instance.dialogs.keySet().removeAll(category.dialogs.keySet());
            }
        } else if (this.type == 2) {
            Quest quest = (Quest) QuestController.instance.quests.remove(this.id);
            if (quest != null) {
                quest.category.quests.remove(this.id);
            }
        } else if (this.type == 3) {
            QuestCategory category = (QuestCategory) QuestController.instance.categories.remove(this.id);
            if (category != null) {
                QuestController.instance.quests.keySet().removeAll(category.quests.keySet());
            }
        }
    }

    public void clientSync(boolean syncEnd) {
    }
}