package journeymap.common.nbt;

import java.util.HashMap;
import java.util.Map;
import journeymap.common.LoaderHooks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class PlayerData extends SavedData {

    private static final String DAT_FILE = "JMPlayerSettings";

    private CompoundTag data = new CompoundTag();

    Map<String, PlayerData.Player> playerMap = new HashMap();

    public PlayerData() {
        LoaderHooks.getServer().getLevel(Level.OVERWORLD).getDataStorage().set("JMPlayerSettings", this);
        this.m_77762_();
    }

    public static PlayerData getPlayerData() {
        return get();
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("JMPlayerSettings", get().data);
        return compound;
    }

    private static PlayerData get() {
        ServerLevel level = LoaderHooks.getServer().getLevel(Level.OVERWORLD);
        return level.getDataStorage().computeIfAbsent(PlayerData::load, PlayerData::new, "JMPlayerSettings");
    }

    private static PlayerData load(CompoundTag nbt) {
        PlayerData playerData = new PlayerData();
        playerData.data = nbt.getCompound("JMPlayerSettings");
        return playerData;
    }

    public PlayerData.Player getPlayer(ServerPlayer serverPlayer) {
        String uuid = serverPlayer.m_20149_();
        PlayerData.Player player = (PlayerData.Player) this.playerMap.get(uuid);
        if (player == null) {
            CompoundTag playerTag;
            if (this.data.contains(uuid)) {
                playerTag = this.data.getCompound(uuid);
            } else {
                playerTag = new CompoundTag();
                this.data.put(uuid, playerTag);
            }
            player = new PlayerData.Player(this, uuid, playerTag);
            this.playerMap.put(uuid, player);
        }
        return player;
    }

    public static class Player {

        static final String HIDDEN_UNDERGROUND = "hidden_underground";

        static final String VISIBLE = "radar_visible";

        final String uuid;

        boolean hiddenUnderground;

        boolean visible;

        CompoundTag playerNbt;

        final PlayerData playerData;

        public Player(PlayerData playerData, String uuid, CompoundTag playerNbt) {
            this.playerData = playerData;
            this.uuid = uuid;
            this.playerNbt = playerNbt;
            this.readPlayerNbt();
        }

        public boolean isHiddenUnderground() {
            return this.hiddenUnderground;
        }

        public void setHiddenUnderground(boolean hiddenUnderground) {
            this.playerNbt.putBoolean("hidden_underground", hiddenUnderground);
            this.hiddenUnderground = hiddenUnderground;
            this.playerData.m_77762_();
        }

        public boolean isVisible() {
            return this.visible;
        }

        public void setVisible(boolean visible) {
            this.playerNbt.putBoolean("radar_visible", visible);
            this.visible = visible;
            this.playerData.m_77762_();
        }

        private void readPlayerNbt() {
            if (this.playerNbt.contains("hidden_underground")) {
                this.hiddenUnderground = this.playerNbt.getBoolean("hidden_underground");
            } else {
                this.setHiddenUnderground(false);
            }
            if (this.playerNbt.contains("radar_visible")) {
                this.visible = this.playerNbt.getBoolean("radar_visible");
            } else {
                this.setVisible(true);
            }
        }
    }
}