package noppes.npcs.packets.client;

import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.shared.common.PacketBasic;

public class PacketSync extends PacketBasic {

    private final int type;

    private final CompoundTag data;

    private final boolean syncEnd;

    public PacketSync(int type, CompoundTag data, boolean syncEnd) {
        this.type = type;
        this.data = data;
        this.syncEnd = syncEnd;
    }

    public static void encode(PacketSync msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.type);
        buf.writeNbt(msg.data);
        buf.writeBoolean(msg.syncEnd);
    }

    public static PacketSync decode(FriendlyByteBuf buf) {
        return new PacketSync(buf.readInt(), buf.readNbt(), buf.readBoolean());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        if (this.type == 1) {
            ListTag list = this.data.getList("Data", 10);
            for (int i = 0; i < list.size(); i++) {
                Faction faction = new Faction();
                faction.readNBT(list.getCompound(i));
                FactionController.instance.factionsSync.put(faction.id, faction);
            }
            if (this.syncEnd) {
                FactionController.instance.factions = FactionController.instance.factionsSync;
                FactionController.instance.factionsSync = new HashMap();
            }
        } else if (this.type == 3) {
            if (!this.data.isEmpty()) {
                QuestCategory category = new QuestCategory();
                category.readNBT(this.data);
                QuestController.instance.categoriesSync.put(category.id, category);
            }
            if (this.syncEnd) {
                HashMap<Integer, Quest> quests = new HashMap();
                for (QuestCategory category : QuestController.instance.categoriesSync.values()) {
                    for (Quest quest : category.quests.values()) {
                        quests.put(quest.id, quest);
                    }
                }
                QuestController.instance.categories = QuestController.instance.categoriesSync;
                QuestController.instance.quests = quests;
                QuestController.instance.categoriesSync = new HashMap();
            }
        } else if (this.type == 5) {
            if (!this.data.isEmpty()) {
                DialogCategory category = new DialogCategory();
                category.readNBT(this.data);
                DialogController.instance.categoriesSync.put(category.id, category);
            }
            if (this.syncEnd) {
                HashMap<Integer, Dialog> dialogs = new HashMap();
                for (DialogCategory category : DialogController.instance.categoriesSync.values()) {
                    for (Dialog dialog : category.dialogs.values()) {
                        dialogs.put(dialog.id, dialog);
                    }
                }
                DialogController.instance.categories = DialogController.instance.categoriesSync;
                DialogController.instance.dialogs = dialogs;
                DialogController.instance.categoriesSync = new HashMap();
            }
        } else if (this.type == 8) {
            ClientProxy.playerData.setNBT(this.data);
        }
    }

    public void clientSync(boolean syncEnd) {
    }
}