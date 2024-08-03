package net.zanckor.questapi.example.common.handler.dialogrequirement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogRequirement;
import net.zanckor.questapi.api.file.dialog.codec.Conversation;
import net.zanckor.questapi.api.file.dialog.codec.NPCDialog;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.mod.common.network.SendQuestPacket;
import net.zanckor.questapi.mod.common.network.packet.dialogoption.DisplayDialog;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogReq;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogReqStatus;
import net.zanckor.questapi.util.GsonManager;

public class QuestRequirement extends AbstractDialogRequirement {

    @Override
    public boolean handler(Player player, Conversation dialog, int option_id, Entity entity) throws IOException {
        if (player.m_9236_().isClientSide) {
            return false;
        } else {
            NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) dialog.getDialog().get(option_id)).getServerRequirements();
            String requirementType = requirement.getType() != null ? requirement.getType() : "NONE";
            if (!requirementType.equals(EnumDialogReq.QUEST.toString())) {
                return false;
            } else {
                EnumDialogReqStatus requirementStatus = EnumDialogReqStatus.valueOf(requirement.getRequirement_status());
                Path questPath = QuestDialogManager.getQuestPathByID(requirement.getQuestId());
                if (questPath == null) {
                    if (requirementStatus == EnumDialogReqStatus.NOT_OBTAINED) {
                        this.displayDialog(player, option_id, dialog, entity);
                        return true;
                    }
                } else {
                    File questFile = questPath.toFile();
                    UserQuest playerQuest = questFile.exists() ? (UserQuest) GsonManager.getJsonClass(questFile, UserQuest.class) : null;
                    switch(requirementStatus) {
                        case IN_PROGRESS:
                            if (questFile.exists() && !playerQuest.isCompleted()) {
                                this.displayDialog(player, option_id, dialog, entity);
                                return true;
                            }
                            break;
                        case COMPLETED:
                            if (questFile.exists() && playerQuest.isCompleted()) {
                                this.displayDialog(player, option_id, dialog, entity);
                                return true;
                            }
                            break;
                        case NOT_OBTAINED:
                            if (!questFile.exists()) {
                                this.displayDialog(player, option_id, dialog, entity);
                                return true;
                            }
                    }
                }
                return false;
            }
        }
    }

    @Override
    public boolean handler(Player player, Conversation dialog, int option_id, String resourceLocation) throws IOException {
        if (player.m_9236_().isClientSide) {
            return false;
        } else {
            NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) dialog.getDialog().get(option_id)).getServerRequirements();
            String requirementType = requirement.getType();
            if (!requirementType.equals(EnumDialogReq.QUEST.toString())) {
                return false;
            } else {
                EnumDialogReqStatus requirementStatus = EnumDialogReqStatus.valueOf(requirement.getRequirement_status());
                Path questPath = QuestDialogManager.getQuestPathByID(requirement.getQuestId());
                if (questPath == null) {
                    if (requirementStatus == EnumDialogReqStatus.NOT_OBTAINED) {
                        this.displayDialog(player, option_id, dialog, resourceLocation);
                        return true;
                    }
                } else {
                    File questFile = questPath.toFile();
                    UserQuest playerQuest = questFile.exists() ? (UserQuest) GsonManager.getJsonClass(questFile, UserQuest.class) : null;
                    switch(requirementStatus) {
                        case IN_PROGRESS:
                            if (questFile.exists() && !playerQuest.isCompleted()) {
                                this.displayDialog(player, option_id, dialog, resourceLocation);
                                return true;
                            }
                            break;
                        case COMPLETED:
                            if (questFile.exists() && playerQuest.isCompleted()) {
                                this.displayDialog(player, option_id, dialog, resourceLocation);
                                return true;
                            }
                            break;
                        case NOT_OBTAINED:
                            if (!questFile.exists()) {
                                this.displayDialog(player, option_id, dialog, resourceLocation);
                                return true;
                            }
                    }
                }
                return false;
            }
        }
    }

    @Override
    public boolean handler(Player player, Conversation dialog, int option_id, Item item) throws IOException {
        if (player.m_9236_().isClientSide) {
            return false;
        } else {
            NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) dialog.getDialog().get(option_id)).getServerRequirements();
            String requirementType = requirement.getType();
            if (!requirementType.equals(EnumDialogReq.QUEST.toString())) {
                return false;
            } else {
                EnumDialogReqStatus requirementStatus = EnumDialogReqStatus.valueOf(requirement.getRequirement_status());
                Path questPath = QuestDialogManager.getQuestPathByID(requirement.getQuestId());
                if (questPath == null) {
                    if (requirementStatus == EnumDialogReqStatus.NOT_OBTAINED) {
                        this.displayDialog(player, option_id, dialog, item);
                        return true;
                    }
                } else {
                    File questFile = questPath.toFile();
                    UserQuest playerQuest = questFile.exists() ? (UserQuest) GsonManager.getJsonClass(questFile, UserQuest.class) : null;
                    switch(requirementStatus) {
                        case IN_PROGRESS:
                            if (questFile.exists() && !playerQuest.isCompleted()) {
                                this.displayDialog(player, option_id, dialog, item);
                                return true;
                            }
                            break;
                        case COMPLETED:
                            if (questFile.exists() && playerQuest.isCompleted()) {
                                this.displayDialog(player, option_id, dialog, item);
                                return true;
                            }
                            break;
                        case NOT_OBTAINED:
                            if (!questFile.exists()) {
                                this.displayDialog(player, option_id, dialog, item);
                                return true;
                            }
                    }
                }
                return false;
            }
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