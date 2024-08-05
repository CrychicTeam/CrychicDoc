package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.util.NetUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class EditObjectMessage extends BaseC2SMessage {

    private final long id;

    private final CompoundTag nbt;

    EditObjectMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
        this.nbt = buffer.readNbt();
    }

    public EditObjectMessage(QuestObjectBase o) {
        this.id = o.id;
        this.nbt = new CompoundTag();
        o.writeData(this.nbt);
        FTBQuests.getRecipeModHelper().refreshRecipes(o);
        ClientQuestFile.INSTANCE.clearCachedData();
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.EDIT_OBJECT;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
        buffer.writeNbt(this.nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (NetUtils.canEdit(context)) {
            QuestObjectBase object = ServerQuestFile.INSTANCE.getBase(this.id);
            if (object != null) {
                object.readData(this.nbt);
                object.editedFromGUIOnServer();
                ServerQuestFile.INSTANCE.clearCachedData();
                ServerQuestFile.INSTANCE.markDirty();
                new EditObjectResponseMessage(object).sendToAll(context.getPlayer().m_20194_());
            }
        }
    }
}