package net.zanckor.questapi.mod.server.startdialog;

import java.io.IOException;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogRequirement;
import net.zanckor.questapi.api.file.dialog.codec.Conversation;
import net.zanckor.questapi.api.file.dialog.codec.NPCDialog;
import net.zanckor.questapi.api.file.quest.register.QuestTemplateRegistry;
import net.zanckor.questapi.api.registry.EnumRegistry;

public class StartDialog {

    public static void loadDialog(Player player, String conversationID, Entity entity) throws IOException {
        Conversation conversation = QuestDialogManager.getConversationByID(conversationID);
        QuestDialogManager.currentGlobalDialog.put(player, conversation.getConversationID());
        for (int dialogID = 0; dialogID <= conversation.getDialog().size() - 1; dialogID++) {
            NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) conversation.getDialog().get(dialogID)).getServerRequirements();
            String requirementType = requirement.getType() != null ? requirement.getType() : "NONE";
            Enum<?> requirementEnum = EnumRegistry.getEnum(requirementType, EnumRegistry.getDialogRequirement());
            AbstractDialogRequirement dialogRequirement = QuestTemplateRegistry.getDialogRequirement(requirementEnum);
            if (dialogRequirement != null && dialogRequirement.handler(player, conversation, dialogID, entity)) {
                return;
            }
        }
    }

    public static void loadDialog(Player player, String globalDialogID, String resourceLocation) throws IOException {
        Conversation conversation = QuestDialogManager.getConversationByID(globalDialogID);
        QuestDialogManager.currentGlobalDialog.put(player, conversation.getConversationID());
        for (int dialogID = 0; dialogID <= conversation.getDialog().size() - 1; dialogID++) {
            NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) conversation.getDialog().get(dialogID)).getServerRequirements();
            String requirementType = requirement.getType() != null ? requirement.getType() : "NONE";
            Enum<?> requirementEnum = EnumRegistry.getEnum(requirementType, EnumRegistry.getDialogRequirement());
            AbstractDialogRequirement dialogRequirement = QuestTemplateRegistry.getDialogRequirement(requirementEnum);
            if (dialogRequirement != null && dialogRequirement.handler(player, conversation, dialogID, resourceLocation)) {
                return;
            }
        }
    }

    public static void loadDialog(Player player, String globalDialogID, Item item) throws IOException {
        Conversation conversation = QuestDialogManager.getConversationByID(globalDialogID);
        QuestDialogManager.currentGlobalDialog.put(player, conversation.getConversationID());
        for (int dialogID = 0; dialogID <= conversation.getDialog().size() - 1; dialogID++) {
            NPCDialog.DialogRequirement requirement = ((NPCDialog.QuestDialog) conversation.getDialog().get(dialogID)).getServerRequirements();
            String requirementType = requirement.getType() != null ? requirement.getType() : "NONE";
            Enum<?> requirementEnum = EnumRegistry.getEnum(requirementType, EnumRegistry.getDialogRequirement());
            AbstractDialogRequirement dialogRequirement = QuestTemplateRegistry.getDialogRequirement(requirementEnum);
            if (dialogRequirement != null && dialogRequirement.handler(player, conversation, dialogID, item)) {
                return;
            }
        }
    }
}