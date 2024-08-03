package net.zanckor.questapi.example.common.handler.dialogoption;

import java.io.IOException;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogOption;
import net.zanckor.questapi.api.file.dialog.codec.Conversation;
import net.zanckor.questapi.api.file.dialog.codec.NPCDialog;
import net.zanckor.questapi.mod.common.network.SendQuestPacket;
import net.zanckor.questapi.mod.common.network.packet.dialogoption.CloseDialog;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogOption;

public class DialogCloseDialog extends AbstractDialogOption {

    @Override
    public void handler(Player player, Conversation dialog, int option_id, Entity entity) throws IOException {
        int currentDialog = (Integer) QuestDialogManager.currentDialog.get(player);
        NPCDialog.DialogOption option = (NPCDialog.DialogOption) ((NPCDialog.QuestDialog) dialog.getDialog().get(currentDialog)).getOptions().get(option_id);
        if (option.getType().equals(EnumDialogOption.CLOSE_DIALOG.toString())) {
            SendQuestPacket.TO_CLIENT(player, new CloseDialog());
        }
    }

    @Override
    public void handler(Player player, Conversation dialog, int option_id, String resourceLocation) throws IOException {
        this.handler(player, dialog, option_id, (Entity) null);
    }

    @Override
    public void handler(Player player, Conversation dialog, int option_id, Item item) throws IOException {
        this.handler(player, dialog, option_id, (Entity) null);
    }
}