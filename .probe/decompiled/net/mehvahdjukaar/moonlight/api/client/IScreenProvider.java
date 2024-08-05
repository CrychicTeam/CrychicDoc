package net.mehvahdjukaar.moonlight.api.client;

import net.mehvahdjukaar.moonlight.core.network.ClientBoundOpenScreenPacket;
import net.mehvahdjukaar.moonlight.core.network.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IScreenProvider {

    @Deprecated(forRemoval = true)
    @OnlyIn(Dist.CLIENT)
    void openScreen(Level var1, BlockPos var2, Player var3);

    @OnlyIn(Dist.CLIENT)
    default void openScreen(Level level, BlockPos pos, Player player, Direction direction) {
        this.openScreen(level, pos, player);
    }

    default void sendOpenGuiPacket(Level level, BlockPos pos, Player player) {
        this.sendOpenGuiPacket(level, pos, player, Direction.NORTH);
    }

    default void sendOpenGuiPacket(Level level, BlockPos pos, Player player, Direction hitFace) {
        ModMessages.CHANNEL.sendToClientPlayer((ServerPlayer) player, new ClientBoundOpenScreenPacket(pos, hitFace));
    }
}