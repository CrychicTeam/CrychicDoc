package dev.ftb.mods.ftbteams.data;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.util.UUIDTypeAdapter;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.math.MathUtils;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import org.jetbrains.annotations.Nullable;

public class FTBTUtils {

    public static final GameProfile NO_PROFILE = new GameProfile(new UUID(0L, 0L), "-");

    @Nullable
    public static ServerPlayer getPlayerByUUID(MinecraftServer server, @Nullable UUID id) {
        return id != null && id != Util.NIL_UUID ? server.getPlayerList().getPlayer(id) : null;
    }

    public static GameProfile normalize(@Nullable GameProfile profile) {
        if (profile == null || profile.getId() == null || profile.getName() == null || profile.equals(NO_PROFILE)) {
            return NO_PROFILE;
        } else {
            return !profile.getProperties().isEmpty() ? new GameProfile(profile.getId(), profile.getName()) : profile;
        }
    }

    public static String serializeProfile(@Nullable GameProfile profile) {
        return normalize(profile) == NO_PROFILE ? "" : UUIDTypeAdapter.fromUUID(profile.getId()) + ":" + profile.getName();
    }

    public static GameProfile deserializeProfile(String string) {
        if (string.isEmpty()) {
            return NO_PROFILE;
        } else {
            try {
                String[] s = string.split(":", 2);
                UUID uuid = UUIDTypeAdapter.fromString(s[0]);
                String name = s[1];
                return normalize(new GameProfile(uuid, name));
            } catch (Exception var4) {
                return NO_PROFILE;
            }
        }
    }

    public static Color4I randomColor() {
        return Color4I.hsb(MathUtils.RAND.nextFloat(), 0.65F, 1.0F);
    }

    public static boolean canPlayerUseCommand(ServerPlayer player, String command) {
        List<String> parts = Arrays.asList(command.split("\\."));
        CommandNode<CommandSourceStack> node = player.m_20194_().getCommands().getDispatcher().findNode(parts);
        return node != null && node.canUse(player.m_20203_());
    }

    public static String getDefaultPartyName(MinecraftServer server, UUID playerId, @Nullable ServerPlayer player) {
        String playerName;
        if (player != null) {
            playerName = player.m_36316_().getName();
        } else {
            GameProfileCache profileCache = server.getProfileCache();
            if (profileCache == null) {
                playerName = playerId.toString();
            } else {
                playerName = ((GameProfile) profileCache.get(playerId).orElse(new GameProfile(playerId, playerId.toString()))).getName();
            }
        }
        return playerName + "'s Party";
    }
}