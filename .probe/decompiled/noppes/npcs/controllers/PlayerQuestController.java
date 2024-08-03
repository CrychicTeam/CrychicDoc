package noppes.npcs.controllers;

import java.util.Vector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.EventHooks;
import noppes.npcs.constants.EnumQuestRepeat;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketAchievement;
import noppes.npcs.packets.client.PacketChat;
import noppes.npcs.quests.QuestDialog;
import noppes.npcs.shared.common.util.LogWriter;

public class PlayerQuestController {

    public static boolean hasActiveQuests(Player player) {
        PlayerQuestData data = PlayerData.get(player).questData;
        return !data.activeQuests.isEmpty();
    }

    public static boolean isQuestActive(Player player, int quest) {
        PlayerQuestData data = PlayerData.get(player).questData;
        return data.activeQuests.containsKey(quest);
    }

    public static boolean isQuestCompleted(Player player, int quest) {
        PlayerQuestData data = PlayerData.get(player).questData;
        QuestData q = (QuestData) data.activeQuests.get(quest);
        return q == null ? false : q.isCompleted;
    }

    public static boolean isQuestFinished(Player player, int questid) {
        PlayerQuestData data = PlayerData.get(player).questData;
        return data.finishedQuests.containsKey(questid);
    }

    public static boolean canQuestBeAccepted(Player player, int questId) {
        Quest quest = (Quest) QuestController.instance.quests.get(questId);
        if (quest == null) {
            return false;
        } else {
            PlayerQuestData data = PlayerData.get(player).questData;
            if (data.activeQuests.containsKey(quest.id)) {
                return false;
            } else if (!data.finishedQuests.containsKey(quest.id) || quest.repeat == EnumQuestRepeat.REPEATABLE) {
                return true;
            } else if (quest.repeat == EnumQuestRepeat.NONE) {
                return false;
            } else {
                long questTime = (Long) data.finishedQuests.get(quest.id);
                if (quest.repeat == EnumQuestRepeat.MCDAILY) {
                    return player.m_9236_().getGameTime() - questTime >= 24000L;
                } else if (quest.repeat == EnumQuestRepeat.MCWEEKLY) {
                    return player.m_9236_().getGameTime() - questTime >= 168000L;
                } else if (quest.repeat == EnumQuestRepeat.RLDAILY) {
                    return System.currentTimeMillis() - questTime >= 86400000L;
                } else {
                    return quest.repeat == EnumQuestRepeat.RLWEEKLY ? System.currentTimeMillis() - questTime >= 604800000L : false;
                }
            }
        }
    }

    public static void addActiveQuest(Quest quest, Player player) {
        PlayerData playerdata = PlayerData.get(player);
        LogWriter.debug("AddActiveQuest: " + quest.title + " + " + playerdata);
        PlayerQuestData data = playerdata.questData;
        if (playerdata.scriptData.getPlayer().canQuestBeAccepted(quest.id)) {
            if (EventHooks.onQuestStarted(playerdata.scriptData, quest)) {
                return;
            }
            data.activeQuests.put(quest.id, new QuestData(quest));
            Packets.send((ServerPlayer) player, new PacketAchievement(Component.translatable("quest.newquest"), Component.translatable(quest.title), 2));
            Packets.send((ServerPlayer) player, new PacketChat(Component.translatable("quest.newquest").append(":").append(Component.translatable(quest.title))));
            playerdata.updateClient = true;
        }
    }

    public static void setQuestFinished(Quest quest, Player player) {
        PlayerData playerdata = PlayerData.get(player);
        PlayerQuestData data = playerdata.questData;
        data.activeQuests.remove(quest.id);
        if (quest.repeat != EnumQuestRepeat.RLDAILY && quest.repeat != EnumQuestRepeat.RLWEEKLY) {
            data.finishedQuests.put(quest.id, player.m_9236_().getGameTime());
        } else {
            data.finishedQuests.put(quest.id, System.currentTimeMillis());
        }
        if (quest.repeat != EnumQuestRepeat.NONE && quest.type == 1) {
            QuestDialog questdialog = (QuestDialog) quest.questInterface;
            for (int dialog : questdialog.dialogs.values()) {
                playerdata.dialogData.dialogsRead.remove(dialog);
            }
        }
        playerdata.updateClient = true;
    }

    public static Vector<Quest> getActiveQuests(Player player) {
        Vector<Quest> quests = new Vector();
        PlayerQuestData data = PlayerData.get(player).questData;
        for (QuestData questdata : data.activeQuests.values()) {
            if (questdata.quest != null) {
                quests.add(questdata.quest);
            }
        }
        return quests;
    }
}