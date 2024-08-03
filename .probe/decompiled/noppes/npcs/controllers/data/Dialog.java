package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.ICompatibilty;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.handler.data.IAvailability;
import noppes.npcs.api.handler.data.IDialog;
import noppes.npcs.api.handler.data.IDialogCategory;
import noppes.npcs.api.handler.data.IDialogOption;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.db.DatabaseColumn;

public class Dialog implements ICompatibilty, IDialog {

    public int version = VersionCompatibility.ModRev;

    @DatabaseColumn(name = "id", type = DatabaseColumn.Type.INT)
    public int id = -1;

    @DatabaseColumn(name = "title", type = DatabaseColumn.Type.VARCHAR)
    public String title = "";

    @DatabaseColumn(name = "text", type = DatabaseColumn.Type.TEXT)
    public String text = "";

    @DatabaseColumn(name = "quest", type = DatabaseColumn.Type.INT)
    public int quest = -1;

    @DatabaseColumn(name = "category", type = DatabaseColumn.Type.VARCHAR)
    public String categoryName;

    public final DialogCategory category;

    public HashMap<Integer, DialogOption> options = new HashMap();

    public Availability availability = new Availability();

    public FactionOptions factionOptions = new FactionOptions();

    public String sound;

    public String command = "";

    public PlayerMail mail = new PlayerMail();

    public boolean hideNPC = false;

    public boolean showWheel = false;

    public boolean disableEsc = false;

    public Dialog(DialogCategory category) {
        this.category = category;
    }

    public boolean hasDialogs(Player player) {
        for (DialogOption option : this.options.values()) {
            if (option != null && option.optionType == 1 && option.hasDialog() && option.isAvailable(player)) {
                return true;
            }
        }
        return false;
    }

    public void readNBT(CompoundTag compound) {
        this.id = compound.getInt("DialogId");
        this.readNBTPartial(compound);
    }

    public void readNBTPartial(CompoundTag compound) {
        this.version = compound.getInt("ModRev");
        VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
        this.title = compound.getString("DialogTitle");
        this.text = compound.getString("DialogText");
        this.quest = compound.getInt("DialogQuest");
        this.sound = compound.getString("DialogSound");
        this.command = compound.getString("DialogCommand");
        this.mail.readNBT(compound.getCompound("DialogMail"));
        this.hideNPC = compound.getBoolean("DialogHideNPC");
        this.showWheel = compound.getBoolean("DialogShowWheel");
        this.disableEsc = compound.getBoolean("DialogDisableEsc");
        ListTag options = compound.getList("Options", 10);
        HashMap<Integer, DialogOption> newoptions = new HashMap();
        for (int iii = 0; iii < options.size(); iii++) {
            CompoundTag option = options.getCompound(iii);
            int opslot = option.getInt("OptionSlot");
            DialogOption dia = new DialogOption();
            dia.readNBT(option.getCompound("Option"));
            if (dia.hasDialog()) {
            }
            newoptions.put(opslot, dia);
            dia.slot = opslot;
        }
        this.options = newoptions;
        this.availability.load(compound);
        this.factionOptions.load(compound);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("DialogId", this.id);
        return this.writeToNBTPartial(compound);
    }

    public CompoundTag writeToNBTPartial(CompoundTag compound) {
        compound.putString("DialogTitle", this.title);
        compound.putString("DialogText", this.text);
        compound.putInt("DialogQuest", this.quest);
        compound.putString("DialogCommand", this.command);
        compound.put("DialogMail", this.mail.writeNBT());
        compound.putBoolean("DialogHideNPC", this.hideNPC);
        compound.putBoolean("DialogShowWheel", this.showWheel);
        compound.putBoolean("DialogDisableEsc", this.disableEsc);
        if (this.sound != null && !this.sound.isEmpty()) {
            compound.putString("DialogSound", this.sound);
        }
        ListTag options = new ListTag();
        for (int opslot : this.options.keySet()) {
            CompoundTag listcompound = new CompoundTag();
            listcompound.putInt("OptionSlot", opslot);
            listcompound.put("Option", ((DialogOption) this.options.get(opslot)).writeNBT());
            options.add(listcompound);
        }
        compound.put("Options", options);
        this.availability.save(compound);
        this.factionOptions.save(compound);
        compound.putInt("ModRev", this.version);
        return compound;
    }

    public boolean hasQuest() {
        return this.getQuest() != null;
    }

    public Quest getQuest() {
        return QuestController.instance == null ? null : (Quest) QuestController.instance.quests.get(this.quest);
    }

    public boolean hasOtherOptions() {
        for (DialogOption option : this.options.values()) {
            if (option != null && option.optionType != 2) {
                return true;
            }
        }
        return false;
    }

    public Dialog copy(Player player) {
        Dialog dialog = new Dialog(this.category);
        dialog.id = this.id;
        dialog.text = this.text;
        dialog.title = this.title;
        dialog.quest = this.quest;
        dialog.sound = this.sound;
        dialog.mail = this.mail;
        dialog.command = this.command;
        dialog.hideNPC = this.hideNPC;
        dialog.showWheel = this.showWheel;
        dialog.disableEsc = this.disableEsc;
        for (int slot : this.options.keySet()) {
            DialogOption option = (DialogOption) this.options.get(slot);
            if (option.optionType != 1 || option.hasDialog() && option.isAvailable(player)) {
                dialog.options.put(slot, option);
            }
        }
        return dialog;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.title;
    }

    @Override
    public List<IDialogOption> getOptions() {
        return new ArrayList(this.options.values());
    }

    @Override
    public IDialogOption getOption(int slot) {
        IDialogOption option = (IDialogOption) this.options.get(slot);
        if (option == null) {
            throw new CustomNPCsException("There is no DialogOption for slot: " + slot);
        } else {
            return option;
        }
    }

    @Override
    public IAvailability getAvailability() {
        return this.availability;
    }

    @Override
    public IDialogCategory getCategory() {
        return this.category;
    }

    @Override
    public void save() {
        DialogController.instance.saveDialog(this.category, this);
    }

    @Override
    public void setName(String name) {
        this.title = name;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setQuest(IQuest quest) {
        if (quest == null) {
            this.quest = -1;
        } else {
            if (quest.getId() < 0) {
                throw new CustomNPCsException("Quest id is lower than 0");
            }
            this.quest = quest.getId();
        }
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }
}