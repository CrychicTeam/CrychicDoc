package net.minecraft.server.level;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.Set;

public final class PlayerMap {

    private final Object2BooleanMap<ServerPlayer> players = new Object2BooleanOpenHashMap();

    public Set<ServerPlayer> getPlayers(long long0) {
        return this.players.keySet();
    }

    public void addPlayer(long long0, ServerPlayer serverPlayer1, boolean boolean2) {
        this.players.put(serverPlayer1, boolean2);
    }

    public void removePlayer(long long0, ServerPlayer serverPlayer1) {
        this.players.removeBoolean(serverPlayer1);
    }

    public void ignorePlayer(ServerPlayer serverPlayer0) {
        this.players.replace(serverPlayer0, true);
    }

    public void unIgnorePlayer(ServerPlayer serverPlayer0) {
        this.players.replace(serverPlayer0, false);
    }

    public boolean ignoredOrUnknown(ServerPlayer serverPlayer0) {
        return this.players.getOrDefault(serverPlayer0, true);
    }

    public boolean ignored(ServerPlayer serverPlayer0) {
        return this.players.getBoolean(serverPlayer0);
    }

    public void updatePlayer(long long0, long long1, ServerPlayer serverPlayer2) {
    }
}