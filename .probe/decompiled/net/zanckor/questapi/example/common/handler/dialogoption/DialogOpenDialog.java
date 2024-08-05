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
import net.zanckor.questapi.mod.common.network.packet.dialogoption.DisplayDialog;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogOption;

public class DialogOpenDialog extends AbstractDialogOption {

    @Override
    public void handler(Player player, Conversation dialog, int option_id, Entity entity) throws IOException {
        int currentDialog = (Integer) QuestDialogManager.currentDialog.get(player);
        NPCDialog.DialogOption option = (NPCDialog.DialogOption) ((NPCDialog.QuestDialog) dialog.getDialog().get(currentDialog)).getOptions().get(option_id);
        if (option.getType().equals(EnumDialogOption.OPEN_DIALOG.toString())) {
            SendQuestPacket.TO_CLIENT(player, new DisplayDialog(dialog, dialog.getIdentifier(), option.getDialog(), player, entity));
        }
    }

    @Override
    public void handler(Player player, Conversation dialog, int option_id, String resourceLocation) throws IOException {
        int currentDialog = (Integer) QuestDialogManager.currentDialog.get(player);
        NPCDialog.DialogOption option = (NPCDialog.DialogOption) ((NPCDialog.QuestDialog) dialog.getDialog().get(currentDialog)).getOptions().get(option_id);
        if (option.getType().equals(EnumDialogOption.OPEN_DIALOG.toString())) {
            SendQuestPacket.TO_CLIENT(player, new DisplayDialog(dialog, dialog.getIdentifier(), option.getDialog(), player, resourceLocation));
        }
    }

    @Override
    public void handler(Player player, Conversation dialog, int option_id, Item item) throws IOException {
        int currentDialog = (Integer) QuestDialogManager.currentDialog.get(player);
        NPCDialog.DialogOption option = (NPCDialog.DialogOption) ((NPCDialog.QuestDialog) dialog.getDialog().get(currentDialog)).getOptions().get(option_id);
        if (option.getType().equals(EnumDialogOption.OPEN_DIALOG.toString())) {
            SendQuestPacket.TO_CLIENT(player, new DisplayDialog(dialog, dialog.getIdentifier(), option.getDialog(), player, item));
        }
    }
}