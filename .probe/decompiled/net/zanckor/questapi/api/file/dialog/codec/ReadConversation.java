package net.zanckor.questapi.api.file.dialog.codec;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.zanckor.questapi.api.file.FileAbstract;

public class ReadConversation extends FileAbstract {

    ConcurrentHashMap<String, List<Integer>> conversations = new ConcurrentHashMap();

    public List<Integer> getConversation(String conversationID) {
        this.conversations.computeIfAbsent(conversationID, dialogList -> new ArrayList());
        return (List<Integer>) this.conversations.get(conversationID);
    }

    public void setConversations(ConcurrentHashMap<String, List<Integer>> conversations) {
        this.conversations = conversations;
    }

    public void addConversation(String conversationID, Integer dialogID) {
        List<Integer> readDialog = (List<Integer>) this.conversations.get(conversationID);
        readDialog.removeIf(id -> id.equals(dialogID));
        readDialog.add(dialogID);
        this.conversations.put(conversationID, readDialog);
    }
}