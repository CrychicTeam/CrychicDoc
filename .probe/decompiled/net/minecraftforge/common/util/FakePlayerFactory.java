package net.minecraftforge.common.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;

public class FakePlayerFactory {

    private static final GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");

    private static final Map<FakePlayerFactory.FakePlayerKey, FakePlayer> fakePlayers = Maps.newHashMap();

    public static FakePlayer getMinecraft(ServerLevel level) {
        return get(level, MINECRAFT);
    }

    public static FakePlayer get(ServerLevel level, GameProfile username) {
        FakePlayerFactory.FakePlayerKey key = new FakePlayerFactory.FakePlayerKey(level, username);
        return (FakePlayer) fakePlayers.computeIfAbsent(key, k -> new FakePlayer(k.level(), k.username()));
    }

    public static void unloadLevel(ServerLevel level) {
        fakePlayers.entrySet().removeIf(entry -> ((FakePlayer) entry.getValue()).m_9236_() == level);
    }

    private static record FakePlayerKey(ServerLevel level, GameProfile username) {
    }
}