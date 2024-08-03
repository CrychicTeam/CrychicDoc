package dev.xkmc.l2library.capability.conditionals;

import dev.xkmc.l2library.init.L2Library;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface NetworkSensitiveToken<T extends ConditionalToken> {

    void onSync(@Nullable T var1, Player var2);

    default void sync(TokenKey<T> key, T token, ServerPlayer sp) {
        L2Library.PACKET_HANDLER.toClientPlayer(TokenToClient.of(key, token), sp);
    }
}