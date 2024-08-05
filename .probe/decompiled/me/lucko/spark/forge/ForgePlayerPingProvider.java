package me.lucko.spark.forge;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import me.lucko.spark.common.monitor.ping.PlayerPingProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ForgePlayerPingProvider implements PlayerPingProvider {

    private final MinecraftServer server;

    public ForgePlayerPingProvider(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public Map<String, Integer> poll() {
        Builder<String, Integer> builder = ImmutableMap.builder();
        for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
            builder.put(player.m_36316_().getName(), player.latency);
        }
        return builder.build();
    }
}