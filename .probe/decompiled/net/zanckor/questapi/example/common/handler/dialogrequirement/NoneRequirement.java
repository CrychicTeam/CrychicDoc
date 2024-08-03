package net.zanckor.questapi.example.common.handler.dialogrequirement;

import java.io.IOException;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogRequirement;
import net.zanckor.questapi.api.file.dialog.codec.Conversation;
import net.zanckor.questapi.api.file.dialog.codec.NPCDialog;
import net.zanckor.questapi.mod.common.network.SendQuestPacket;
import net.zanckor.questapi.mod.common.network.packet.dialogoption.DisplayDialog;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogReq;

public class NoneRequirement extends AbstractDialogRequirement {

    @Override
    public boolean handler(Player player, Conversation dialog, int option_id, Entity entity) throws IOException {
        NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) dialog.getDialog().get(option_id)).getServerRequirements();
        String requirementType = requirement.getType() != null ? requirement.getType() : "NONE";
        if (!requirementType.equals(EnumDialogReq.NONE.toString())) {
            return false;
        } else {
            this.displayDialog(player, option_id, dialog, entity);
            return true;
        }
    }

    @Override
    public boolean handler(Player player, Conversation dialog, int option_id, String resourceLocation) throws IOException {
        NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) dialog.getDialog().get(option_id)).getServerRequirements();
        String requirementType = requirement.getType();
        if (!requirementType.equals(EnumDialogReq.NONE.toString())) {
            return false;
        } else {
            this.displayDialog(player, option_id, dialog, resourceLocation);
            return true;
        }
    }

    @Override
    public boolean handler(Player player, Conversation dialog, int option_id, Item item) throws IOException {
        NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) dialog.getDialog().get(option_id)).getServerRequirements();
        String requirementType = requirement.getType();
        if (!requirementType.equals(EnumDialogReq.NONE.toString())) {
            return false;
        } else {
            this.displayDialog(player, option_id, dialog, item);
            return true;
        }
    }

    private void displayDialog(Player player, int dialog_id, Conversation dialog, Entity entity) throws IOException {
        QuestDialogManager.currentDialog.put(player, dialog_id);
        SendQuestPacket.TO_CLIENT(player, new DisplayDialog(dialog, dialog.getIdentifier(), dialog_id, player, entity));
    }

    private void displayDialog(Player player, int dialog_id, Conversation dialog, String resourceLocation) throws IOException {
        QuestDialogManager.currentDialog.put(player, dialog_id);
        SendQuestPacket.TO_CLIENT(player, new DisplayDialog(dialog, dialog.getIdentifier(), dialog_id, player, resourceLocation));
    }

    private void displayDialog(Player player, int dialog_id, Conversation dialog, Item item) throws IOException {
        QuestDialogManager.currentDialog.put(player, dialog_id);
        SendQuestPacket.TO_CLIENT(player, new DisplayDialog(dialog, dialog.getIdentifier(), dialog_id, player, item));
    }
}