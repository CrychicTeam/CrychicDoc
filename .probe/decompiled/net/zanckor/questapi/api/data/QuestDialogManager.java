package net.zanckor.questapi.api.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.file.dialog.codec.Conversation;
import net.zanckor.questapi.api.file.npc.entity_type.codec.EntityTypeDialog;
import net.zanckor.questapi.util.GsonManager;

public class QuestDialogManager {

    public static HashMap<Player, Integer> currentDialog = new HashMap();

    public static HashMap<Player, String> currentGlobalDialog = new HashMap();

    public static HashMap<String, Path> clientQuestByIDLocation = new HashMap();

    public static HashMap<Enum<?>, List<Path>> clientQuestTypeLocation = new HashMap();

    public static HashMap<String, Path> conversationLocation = new HashMap();

    public static HashMap<String, List<String>> conversationByEntityType = new HashMap();

    public static HashMap<String, File> conversationByCompoundTag = new HashMap();

    public static HashMap<String, Conversation> conversationByID = new HashMap();

    public static void registerDialogByCompoundTag() {
        for (File file : CommonMain.compoundTag_List.toFile().listFiles()) {
            conversationByCompoundTag.put(file.getName(), file);
        }
    }

    public static void registerDialogByEntityType() throws IOException {
        List<String> dialogs = new ArrayList();
        for (File file : CommonMain.entity_type_list.toFile().listFiles()) {
            EntityTypeDialog entityTypeDialog = (EntityTypeDialog) GsonManager.getJsonClass(file, EntityTypeDialog.class);
            for (String entity_type : entityTypeDialog.getEntity_type()) {
                dialogs.addAll(entityTypeDialog.getDialog_list());
                conversationByEntityType.put(entity_type, dialogs);
            }
        }
    }

    public static void registerQuestTypeLocation(Enum<?> type, Path path) {
        clientQuestTypeLocation.computeIfAbsent(type, anEnum -> new ArrayList());
        if (!getQuestTypePathLocation(type).contains(path)) {
            List<Path> questList = (List<Path>) clientQuestTypeLocation.get(type);
            questList.add(path);
            clientQuestTypeLocation.put(type, questList);
        }
    }

    public static void registerQuestByID(String id, Path path) {
        clientQuestByIDLocation.put(id, path);
    }

    public static void registerConversationLocation(String id, Path path) {
        String conversationID = id.substring(0, id.length() - 5);
        conversationLocation.put(conversationID, path);
        try {
            Conversation conversation = (Conversation) GsonManager.getJsonClass(path.toFile(), Conversation.class);
            conversationByID.put(conversationID, conversation);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    public static File getDialogPathByCompoundTag(String compoundTag) {
        return !conversationByCompoundTag.containsKey(compoundTag) ? null : (File) conversationByCompoundTag.get(compoundTag);
    }

    public static List<String> getConversationByEntityType(String entityType) {
        return !conversationByEntityType.containsKey(entityType) ? null : (List) conversationByEntityType.get(entityType);
    }

    public static List<Path> getQuestTypePathLocation(Enum<?> type) {
        return (List<Path>) clientQuestTypeLocation.get(type);
    }

    public static Path getQuestPathByID(String id) {
        return (Path) clientQuestByIDLocation.get(id);
    }

    public static Path getConversationPathLocation(String conversationID) {
        return (Path) conversationLocation.get(conversationID);
    }

    public static Conversation getConversationByID(String id) {
        return (Conversation) conversationByID.get(id);
    }

    public static void movePathQuest(String id, Path newPath, Enum<?> enumQuestType) {
        removeQuest(id, enumQuestType);
        registerQuestTypeLocation(enumQuestType, newPath);
        registerQuestByID(id, newPath);
    }

    public static void removeQuest(String id, Enum<?> enumQuestType) {
        List<Path> oldPathList = getQuestTypePathLocation(enumQuestType);
        oldPathList.removeIf(listId -> listId.toString().contains(id + ".json"));
        clientQuestTypeLocation.replace(enumQuestType, oldPathList, oldPathList);
        clientQuestByIDLocation.remove(id);
    }
}