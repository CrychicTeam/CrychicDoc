package snownee.kiwi.network;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface IPacketHandler {

    CompletableFuture<FriendlyByteBuf> receive(Function<Runnable, CompletableFuture<FriendlyByteBuf>> var1, FriendlyByteBuf var2, @Nullable ServerPlayer var3);

    default KiwiPacket.Direction getDirection() {
        return null;
    }
}