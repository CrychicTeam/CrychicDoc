package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.rhino.mod.wrapper.UUIDWrapper;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PlayerSelector {

    static PlayerSelector of(Object o) {
        if (o instanceof ServerPlayer sp) {
            return identity(sp);
        } else if (o instanceof UUID uuid) {
            return uuid(uuid);
        } else {
            String name = Objects.toString(o, "").trim().toLowerCase();
            if (name.isEmpty()) {
                return identity(null);
            } else {
                UUID uuid = UUIDWrapper.fromString(name);
                return uuid != null ? uuid(uuid) : name(name).or(fuzzyName(name));
            }
        }
    }

    @Nullable
    ServerPlayer getPlayer(MinecraftServer var1);

    static PlayerSelector identity(ServerPlayer player) {
        return server -> player;
    }

    static PlayerSelector uuid(UUID uuid) {
        return server -> server.getPlayerList().getPlayer(uuid);
    }

    static PlayerSelector name(String name) {
        return server -> server.getPlayerList().getPlayerByName(name);
    }

    static PlayerSelector fuzzyName(String name) {
        return server -> {
            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                if (p.m_6302_().toLowerCase(Locale.ROOT).contains(name)) {
                    return p;
                }
            }
            return null;
        };
    }

    default PlayerSelector or(PlayerSelector fallback) {
        return server -> {
            ServerPlayer p = this.getPlayer(server);
            return p == null ? fallback.getPlayer(server) : p;
        };
    }
}