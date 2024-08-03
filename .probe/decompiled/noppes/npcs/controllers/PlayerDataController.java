package noppes.npcs.controllers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.CustomNpcs;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.controllers.data.PlayerBankData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.shared.common.util.LogWriter;
import noppes.npcs.util.NBTJsonUtil;

public class PlayerDataController {

    public static PlayerDataController instance;

    public Map<String, String> nameUUIDs;

    public PlayerDataController() {
        instance = this;
        File dir = CustomNpcs.getLevelSaveDirectory("playerdata");
        Map<String, String> map = new HashMap();
        for (File file : dir.listFiles()) {
            if (!file.isDirectory() && file.getName().endsWith(".json")) {
                try {
                    CompoundTag compound = NBTJsonUtil.LoadFile(file);
                    if (compound.contains("PlayerName")) {
                        map.put(compound.getString("PlayerName"), file.getName().substring(0, file.getName().length() - 5));
                    }
                } catch (Exception var8) {
                    LogWriter.error("Error loading: " + file.getAbsolutePath(), var8);
                }
            }
        }
        this.nameUUIDs = map;
    }

    public PlayerBankData getBankData(Player player, int bankId) {
        Bank bank = BankController.getInstance().getBank(bankId);
        PlayerBankData data = PlayerData.get(player).bankData;
        if (!data.hasBank(bank.id)) {
            data.loadNew(bank.id);
        }
        return data;
    }

    public String hasPlayer(String username) {
        for (String name : this.nameUUIDs.keySet()) {
            if (name.equalsIgnoreCase(username)) {
                return name;
            }
        }
        return "";
    }

    public PlayerData getDataFromUsername(MinecraftServer server, String username) {
        Player player = server.getPlayerList().getPlayerByName(username);
        PlayerData data = null;
        if (player == null) {
            for (String name : this.nameUUIDs.keySet()) {
                if (name.equalsIgnoreCase(username)) {
                    data = new PlayerData();
                    data.setNBT(PlayerData.loadPlayerData((String) this.nameUUIDs.get(name)));
                    break;
                }
            }
        } else {
            data = PlayerData.get(player);
        }
        return data;
    }

    public void addPlayerMessage(MinecraftServer server, String username, PlayerMail mail) {
        mail.time = System.currentTimeMillis();
        PlayerData data = this.getDataFromUsername(server, username);
        data.mailData.playermail.add(mail.copy());
        data.save(false);
    }
}