package noppes.npcs.shared.common;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import noppes.npcs.shared.common.util.LogWriter;

public class CommonUtil {

    public static void NotifyOPs(MinecraftServer server, String message, Object... obs) {
        NotifyOPs(server, Component.translatable(message, obs));
    }

    public static void NotifyOPs(MinecraftServer server, Component message) {
        MutableComponent chatcomponenttranslation = Component.literal("").append(message).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
        for (Player entityplayer : server.getPlayerList().getPlayers()) {
            if (entityplayer.m_6102_() && isOp(entityplayer)) {
                entityplayer.m_213846_(chatcomponenttranslation);
            }
        }
        if (server.getLevel(Level.OVERWORLD).m_46469_().getBoolean(GameRules.RULE_LOGADMINCOMMANDS)) {
            LogWriter.info(chatcomponenttranslation.getString());
        }
    }

    public static boolean isOp(Player player) {
        return player.m_20194_().getPlayerList().isOp(player.getGameProfile());
    }
}