package net.zanckor.questapi.api.file.dialog.codec;

import java.nio.file.Path;
import java.util.List;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.FileAbstract;

public class Conversation extends FileAbstract {

    String conversationID;

    List<NPCDialog.QuestDialog> dialog;

    String identifier;

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getConversationID() {
        return this.conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public List<NPCDialog.QuestDialog> getDialog() {
        return this.dialog;
    }

    public static Conversation createDialog(Path path, String conversationID) {
        Conversation dialogQuest = new Conversation();
        dialogQuest.setConversationID(conversationID);
        QuestDialogManager.registerConversationLocation(conversationID, path);
        return dialogQuest;
    }
}