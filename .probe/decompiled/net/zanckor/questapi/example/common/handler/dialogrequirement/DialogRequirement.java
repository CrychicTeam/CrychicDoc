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
import net.zanckor.questapi.mod.common.util.MCUtil;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogReq;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogReqStatus;

public class DialogRequirement extends AbstractDialogRequirement {

    @Override
    public boolean handler(Player player, Conversation conversation, int option_id, Entity entity) throws IOException {
        NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) conversation.getDialog().get(option_id)).getServerRequirements();
        String requirementType = requirement.getType() != null ? requirement.getType() : "NONE";
        if (!requirementType.equals(EnumDialogReq.DIALOG.toString())) {
            return false;
        } else {
            EnumDialogReqStatus requirementStatus = EnumDialogReqStatus.valueOf(requirement.getRequirement_status());
            int dialog_requirement = requirement.getDialogId();
            switch(requirementStatus) {
                case READ:
                    if (MCUtil.isReadDialog(player, conversation.getConversationID(), dialog_requirement)) {
                        this.displayDialog(player, option_id, conversation, entity);
                        return true;
                    }
                    break;
                case NOT_READ:
                    if (!MCUtil.isReadDialog(player, conversation.getConversationID(), option_id)) {
                        this.displayDialog(player, option_id, conversation, entity);
                        return true;
                    }
            }
            return false;
        }
    }

    @Override
    public boolean handler(Player player, Conversation conversation, int option_id, String resourceLocation) throws IOException {
        NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) conversation.getDialog().get(option_id)).getServerRequirements();
        String requirementType = requirement.getType();
        if (!requirementType.equals(EnumDialogReq.DIALOG.toString())) {
            return false;
        } else {
            EnumDialogReqStatus requirementStatus = EnumDialogReqStatus.valueOf(requirement.getRequirement_status());
            int dialog_requirement = requirement.getDialogId();
            switch(requirementStatus) {
                case READ:
                    if (MCUtil.isReadDialog(player, conversation.getConversationID(), dialog_requirement)) {
                        this.displayDialog(player, option_id, conversation, resourceLocation);
                        return true;
                    }
                    break;
                case NOT_READ:
                    if (!MCUtil.isReadDialog(player, conversation.getConversationID(), option_id)) {
                        this.displayDialog(player, option_id, conversation, resourceLocation);
                        return true;
                    }
            }
            return false;
        }
    }

    @Override
    public boolean handler(Player player, Conversation conversation, int option_id, Item item) throws IOException {
        NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) conversation.getDialog().get(option_id)).getServerRequirements();
        String requirementType = requirement.getType();
        if (!requirementType.equals(EnumDialogReq.DIALOG.toString())) {
            return false;
        } else {
            EnumDialogReqStatus requirementStatus = EnumDialogReqStatus.valueOf(requirement.getRequirement_status());
            int dialog_requirement = requirement.getDialogId();
            switch(requirementStatus) {
                case READ:
                    if (MCUtil.isReadDialog(player, conversation.getConversationID(), dialog_requirement)) {
                        this.displayDialog(player, option_id, conversation, item);
                        return true;
                    }
                    break;
                case NOT_READ:
                    if (!MCUtil.isReadDialog(player, conversation.getConversationID(), option_id)) {
                        this.displayDialog(player, option_id, conversation, item);
                        return true;
                    }
            }
            return false;
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