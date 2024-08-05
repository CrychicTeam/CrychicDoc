package net.zanckor.questapi.api.file.dialog.codec;

import java.nio.file.Path;
import java.util.List;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.FileAbstract;

public class NPCDialog extends FileAbstract {

    private static String global_id;

    List<NPCDialog.QuestDialog> dialog;

    String identifier;

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getGlobal_id() {
        return global_id;
    }

    public List<NPCDialog.QuestDialog> getDialog() {
        return this.dialog;
    }

    public static NPCDialog createDialog(Path path) {
        NPCDialog dialogQuest = new NPCDialog();
        dialogQuest.setGlobal_id(global_id);
        QuestDialogManager.registerConversationLocation(global_id, path);
        return dialogQuest;
    }

    public void setGlobal_id(String global_id) {
        NPCDialog.global_id = global_id;
    }

    public static class DialogOption {

        String text;

        String type;

        String global_id;

        int dialog;

        String quest_id;

        public String getText() {
            return this.text;
        }

        public String getType() {
            return this.type;
        }

        public String getGlobal_id() {
            return this.global_id;
        }

        public int getDialog() {
            return this.dialog;
        }

        public String getQuest_id() {
            return this.quest_id;
        }

        private static NPCDialog.DialogOption createDialogOption(String text, String type, String global_id, int dialog, String quest_id) {
            NPCDialog.DialogOption option = new NPCDialog.DialogOption();
            option.text = text;
            option.type = type;
            option.global_id = global_id;
            option.dialog = dialog;
            option.quest_id = quest_id;
            return option;
        }
    }

    public static class DialogRequirement {

        String type;

        String global_id;

        int dialog_id;

        String quest_id;

        String requirement_status;

        public String getType() {
            return this.type;
        }

        public String getGlobal_id() {
            return this.global_id;
        }

        public int getDialogId() {
            return this.dialog_id;
        }

        public String getQuestId() {
            return this.quest_id;
        }

        public String getRequirement_status() {
            return this.requirement_status;
        }

        private static NPCDialog.DialogRequirement createRequirement(String type, String global_id, int dialog_id, String quest_id, String requirement_status) {
            NPCDialog.DialogRequirement requirement = new NPCDialog.DialogRequirement();
            requirement.type = type;
            requirement.global_id = global_id;
            requirement.dialog_id = dialog_id;
            requirement.quest_id = quest_id;
            requirement.requirement_status = requirement_status;
            return requirement;
        }
    }

    public static class QuestDialog {

        int id;

        String dialogText;

        NPCDialog.DialogRequirement requirements;

        List<NPCDialog.DialogOption> options;

        public int getId() {
            return this.id;
        }

        public String getDialogText() {
            return this.dialogText;
        }

        public NPCDialog.DialogRequirement getServerRequirements() {
            return this.requirements;
        }

        public List<NPCDialog.DialogOption> getOptions() {
            return this.options;
        }

        private static NPCDialog.QuestDialog createDialog(int id, String dialogText, NPCDialog.DialogRequirement requirements, List<NPCDialog.DialogOption> dialogOptions) {
            NPCDialog.QuestDialog questDialog = new NPCDialog.QuestDialog();
            questDialog.id = id;
            questDialog.dialogText = dialogText;
            questDialog.requirements = requirements;
            questDialog.options = dialogOptions;
            return questDialog;
        }
    }
}