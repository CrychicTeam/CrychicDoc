package noppes.npcs.controllers.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.api.handler.data.IDialogOption;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.db.DatabaseColumn;

public class DialogOption implements IDialogOption {

    @DatabaseColumn(name = "id", type = DatabaseColumn.Type.INT)
    public int id = -1;

    @DatabaseColumn(name = "dialog", type = DatabaseColumn.Type.INT)
    public int dialogId = -1;

    @DatabaseColumn(name = "option", type = DatabaseColumn.Type.VARCHAR)
    public String option = "Talk";

    @DatabaseColumn(name = "text", type = DatabaseColumn.Type.TEXT)
    public String title = "Talk";

    @DatabaseColumn(name = "type", type = DatabaseColumn.Type.SMALLINT)
    public int optionType = 1;

    @DatabaseColumn(name = "color", type = DatabaseColumn.Type.SMALLINT)
    public int optionColor = 14737632;

    @DatabaseColumn(name = "command", type = DatabaseColumn.Type.TEXT)
    public String command = "";

    @DatabaseColumn(name = "order", type = DatabaseColumn.Type.SMALLINT)
    public int slot = -1;

    public void readNBT(CompoundTag compound) {
        if (compound != null) {
            this.title = compound.getString("Title");
            this.dialogId = compound.getInt("Dialog");
            this.optionColor = compound.getInt("DialogColor");
            this.optionType = compound.getInt("OptionType");
            this.command = compound.getString("DialogCommand");
            if (this.optionColor == 0) {
                this.optionColor = 14737632;
            }
        }
    }

    public CompoundTag writeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putString("Title", this.title);
        compound.putInt("OptionType", this.optionType);
        compound.putInt("Dialog", this.dialogId);
        compound.putInt("DialogColor", this.optionColor);
        compound.putString("DialogCommand", this.command);
        return compound;
    }

    public boolean hasDialog() {
        return this.dialogId <= 0 || this.optionType != 1 ? false : DialogController.instance.hasDialog(this.dialogId);
    }

    public Dialog getDialog() {
        return !this.hasDialog() ? null : (Dialog) DialogController.instance.dialogs.get(this.dialogId);
    }

    public boolean isAvailable(Player player) {
        if (this.optionType == 2) {
            return false;
        } else if (this.optionType != 1) {
            return true;
        } else {
            Dialog dialog = this.getDialog();
            return dialog == null ? false : dialog.availability.isAvailable(player);
        }
    }

    public boolean isValid() {
        return this.optionType == 2 ? false : this.optionType != 1 || this.hasDialog();
    }

    public boolean canClose() {
        return this.optionType != 1 || !this.hasDialog();
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public String getName() {
        return this.title;
    }

    @Override
    public int getType() {
        return this.optionType;
    }
}