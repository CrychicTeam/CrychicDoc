package noppes.npcs.packets.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.shared.common.PacketBasic;

public class PacketSyncUpdate extends PacketBasic {

    private final int id;

    private final int type;

    private final CompoundTag data;

    public PacketSyncUpdate(int id, int type, CompoundTag data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public static void encode(PacketSyncUpdate msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeInt(msg.type);
        buf.writeNbt(msg.data);
    }

    public static PacketSyncUpdate decode(FriendlyByteBuf buf) {
        return new PacketSyncUpdate(buf.readInt(), buf.readInt(), buf.readNbt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        if (this.type == 1) {
            Faction faction = new Faction();
            faction.readNBT(this.data);
            FactionController.instance.factions.put(faction.id, faction);
        } else if (this.type == 4) {
            DialogCategory category = (DialogCategory) DialogController.instance.categories.get(this.id);
            Dialog dialog = new Dialog(category);
            dialog.readNBT(this.data);
            DialogController.instance.dialogs.put(dialog.id, dialog);
            category.dialogs.put(dialog.id, dialog);
        } else if (this.type == 5) {
            DialogCategory category = new DialogCategory();
            category.readNBT(this.data);
            DialogController.instance.categories.put(category.id, category);
        } else if (this.type == 2) {
            QuestCategory category = (QuestCategory) QuestController.instance.categories.get(this.id);
            Quest quest = new Quest(category);
            quest.readNBT(this.data);
            QuestController.instance.quests.put(quest.id, quest);
            category.quests.put(quest.id, quest);
        } else if (this.type == 3) {
            QuestCategory category = new QuestCategory();
            category.readNBT(this.data);
            QuestController.instance.categories.put(category.id, category);
        }
    }

    public void clientSync(boolean syncEnd) {
    }
}