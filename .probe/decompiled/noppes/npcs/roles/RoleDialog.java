package noppes.npcs.roles;

import java.util.HashMap;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.data.role.IRoleDialog;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleDialog extends RoleInterface implements IRoleDialog {

    public String dialog = "";

    public int questId = -1;

    public HashMap<Integer, String> options = new HashMap();

    public HashMap<Integer, String> optionsTexts = new HashMap();

    public RoleDialog(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("RoleQuestId", this.questId);
        compound.putString("RoleDialog", this.dialog);
        compound.put("RoleOptions", NBTTags.nbtIntegerStringMap(this.options));
        compound.put("RoleOptionTexts", NBTTags.nbtIntegerStringMap(this.optionsTexts));
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        this.questId = compound.getInt("RoleQuestId");
        this.dialog = compound.getString("RoleDialog");
        this.options = NBTTags.getIntegerStringMap(compound.getList("RoleOptions", 10));
        this.optionsTexts = NBTTags.getIntegerStringMap(compound.getList("RoleOptionTexts", 10));
    }

    @Override
    public void interact(Player player) {
        if (this.dialog.isEmpty()) {
            this.npc.say(player, this.npc.advanced.getInteractLine());
        } else {
            Dialog d = new Dialog(null);
            d.text = this.dialog;
            for (Entry<Integer, String> entry : this.options.entrySet()) {
                if (!((String) entry.getValue()).isEmpty()) {
                    DialogOption option = new DialogOption();
                    String text = (String) this.optionsTexts.get(entry.getKey());
                    if (text != null && !text.isEmpty()) {
                        option.optionType = 3;
                    } else {
                        option.optionType = 0;
                    }
                    option.title = (String) entry.getValue();
                    d.options.put((Integer) entry.getKey(), option);
                }
            }
            NoppesUtilServer.openDialog(player, this.npc, d);
        }
        Quest quest = (Quest) QuestController.instance.quests.get(this.questId);
        if (quest != null) {
            PlayerQuestController.addActiveQuest(quest, player);
        }
    }

    @Override
    public String getDialog() {
        return this.dialog;
    }

    @Override
    public void setDialog(String text) {
        this.dialog = text;
    }

    @Override
    public String getOption(int option) {
        return (String) this.options.get(option);
    }

    @Override
    public void setOption(int option, String text) {
        if (option >= 1 && option <= 6) {
            this.options.put(option, text);
        } else {
            throw new CustomNPCsException("Wrong dialog option slot given: " + option);
        }
    }

    @Override
    public String getOptionDialog(int option) {
        return (String) this.optionsTexts.get(option);
    }

    @Override
    public void setOptionDialog(int option, String text) {
        if (option >= 1 && option <= 6) {
            this.optionsTexts.put(option, text);
        } else {
            throw new CustomNPCsException("Wrong dialog option slot given: " + option);
        }
    }

    @Override
    public int getType() {
        return 7;
    }
}