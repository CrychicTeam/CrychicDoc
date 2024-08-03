package noppes.npcs.packets.server;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumPlayerData;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.PlayerBankData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerDialogData;
import noppes.npcs.controllers.data.PlayerFactionData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.PlayerTransportData;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.TransportLocation;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketPlayerDataGet extends PacketServerBasic {

    private EnumPlayerData type;

    private String name;

    public SPacketPlayerDataGet(EnumPlayerData type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_GUI;
    }

    public static void encode(SPacketPlayerDataGet msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.type);
        buf.writeUtf(msg.name);
    }

    public static SPacketPlayerDataGet decode(FriendlyByteBuf buf) {
        return new SPacketPlayerDataGet(buf.readEnum(EnumPlayerData.class), buf.readUtf(32767));
    }

    @Override
    protected void handle() {
        sendPlayerData(this.type, this.player, this.name);
    }

    public static void sendPlayerData(EnumPlayerData type, ServerPlayer player, String name) {
        Map<String, Integer> map = new HashMap();
        if (type == EnumPlayerData.Players) {
            for (String username : PlayerDataController.instance.nameUUIDs.keySet()) {
                map.put(username, 0);
            }
            for (String username : player.m_20194_().getPlayerList().getPlayerNamesArray()) {
                map.put(username, 0);
            }
        } else {
            PlayerData playerdata = PlayerDataController.instance.getDataFromUsername(player.m_20194_(), name);
            if (type == EnumPlayerData.Dialog) {
                PlayerDialogData data = playerdata.dialogData;
                for (int questId : data.dialogsRead) {
                    Dialog dialog = (Dialog) DialogController.instance.dialogs.get(questId);
                    if (dialog != null) {
                        map.put(dialog.category.title + ": " + dialog.title, questId);
                    }
                }
            } else if (type == EnumPlayerData.Quest) {
                PlayerQuestData data = playerdata.questData;
                for (int questIdx : data.activeQuests.keySet()) {
                    Quest quest = (Quest) QuestController.instance.quests.get(questIdx);
                    if (quest != null) {
                        map.put(quest.category.title + ": " + quest.title + "(Active quest)", questIdx);
                    }
                }
                for (int questIdxx : data.finishedQuests.keySet()) {
                    Quest quest = (Quest) QuestController.instance.quests.get(questIdxx);
                    if (quest != null) {
                        map.put(quest.category.title + ": " + quest.title + "(Finished quest)", questIdxx);
                    }
                }
            } else if (type == EnumPlayerData.Transport) {
                PlayerTransportData data = playerdata.transportData;
                for (int questIdxxx : data.transports) {
                    TransportLocation location = TransportController.getInstance().getTransport(questIdxxx);
                    if (location != null) {
                        map.put(location.category.title + ": " + location.name, questIdxxx);
                    }
                }
            } else if (type == EnumPlayerData.Bank) {
                PlayerBankData data = playerdata.bankData;
                for (int bankId : data.banks.keySet()) {
                    Bank bank = (Bank) BankController.getInstance().banks.get(bankId);
                    if (bank != null) {
                        map.put(bank.name, bankId);
                    }
                }
            } else if (type == EnumPlayerData.Factions) {
                PlayerFactionData data = playerdata.factionData;
                for (int factionId : data.factionData.keySet()) {
                    Faction faction = (Faction) FactionController.instance.factions.get(factionId);
                    if (faction != null) {
                        map.put(faction.name + "(" + data.getFactionPoints(player, factionId) + ")", factionId);
                    }
                }
            }
        }
        NoppesUtilServer.sendScrollData(player, map);
    }
}