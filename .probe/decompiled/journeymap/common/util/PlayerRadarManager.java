package journeymap.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import journeymap.common.network.data.model.PlayerLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerRadarManager {

    private final Map<UUID, Player> playersOnServer;

    private static PlayerRadarManager INSTANCE;

    private final Object lock = new Object();

    private PlayerRadarManager() {
        this.playersOnServer = new HashMap();
    }

    public static PlayerRadarManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerRadarManager();
        }
        return INSTANCE;
    }

    public Map<UUID, Player> getPlayers() {
        synchronized (this.lock) {
            return this.playersOnServer;
        }
    }

    public void updatePlayers(PlayerLocation player) {
        synchronized (this.lock) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.getConnection() != null && mc.getConnection().getPlayerInfo(player.getUniqueId()) != null) {
                Player playerEntity = this.buildPlayer(player);
                this.playersOnServer.put(player.getUniqueId(), playerEntity);
            }
        }
    }

    private Player buildPlayer(PlayerLocation player) {
        Minecraft mc = Minecraft.getInstance();
        if (!player.getUniqueId().equals(mc.player.m_20148_()) && player.isVisible()) {
            Player fakeRadarPlayer;
            if (this.getPlayers().get(player.getUniqueId()) == null) {
                PlayerInfo info = mc.getConnection().getPlayerInfo(player.getUniqueId());
                fakeRadarPlayer = new RemotePlayer(mc.level, info.getProfile());
            } else {
                fakeRadarPlayer = (Player) this.getPlayers().get(player.getUniqueId());
            }
            float f = (float) (player.getYaw() * 360) / 256.0F;
            float f1 = (float) (player.getPitch() * 360) / 256.0F;
            fakeRadarPlayer.m_20234_(player.getEntityId());
            fakeRadarPlayer.m_6034_(player.getX(), player.getY(), player.getZ());
            fakeRadarPlayer.m_217006_(player.getX(), player.getY(), player.getZ());
            fakeRadarPlayer.m_19890_(player.getX(), player.getY(), player.getZ(), f, f1);
            fakeRadarPlayer.m_20260_(false);
            return fakeRadarPlayer;
        } else {
            return null;
        }
    }
}