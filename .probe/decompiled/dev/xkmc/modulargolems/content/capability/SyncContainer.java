package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.modulargolems.init.ModularGolems;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class SyncContainer {

    private static final int LIFETIME = 200;

    private static final int HEARTBEAT = 100;

    public final Map<UUID, Long> players = new LinkedHashMap();

    private long lastUpdateTime = 0L;

    private long tick = 0L;

    public void serverUpdate(ServerLevel level) {
        long time = level.m_46467_();
        if (this.lastUpdateTime != time) {
            this.players.entrySet().removeIf(e -> level.getServer().getPlayerList().getPlayer((UUID) e.getKey()) == null || (Long) e.getValue() + 200L < time);
            this.lastUpdateTime = time;
        }
    }

    public boolean heartBeat(ServerLevel level, UUID uuid) {
        this.serverUpdate(level);
        return this.players.put(uuid, level.m_46467_()) == null;
    }

    public void sendToAllTracking(ServerLevel level, SerialPacketBase packet) {
        this.serverUpdate(level);
        for (UUID e : this.players.keySet()) {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(e);
            if (player != null) {
                ModularGolems.HANDLER.toClientPlayer(packet, player);
            }
        }
    }

    public void clientTick(GolemConfigEntry entry, Level level, boolean updated) {
        long current = level.getGameTime();
        if (updated) {
            this.tick = current;
        } else if (current - this.tick >= 100L) {
            ModularGolems.HANDLER.toServer(new ConfigHeartBeatToServer(entry.getID(), entry.getColor()));
            this.tick = current;
        }
    }

    public void clientReplace(SyncContainer sync) {
        this.tick = sync.tick;
    }
}