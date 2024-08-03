package snownee.loquat.network;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import snownee.kiwi.network.KiwiPacket;
import snownee.kiwi.network.PacketHandler;
import snownee.loquat.core.select.SelectionManager;

@KiwiPacket(value = "selection_click", dir = KiwiPacket.Direction.PLAY_TO_SERVER)
public class CSelectionClickPacket extends PacketHandler {

    public static CSelectionClickPacket I;

    @Override
    public CompletableFuture<FriendlyByteBuf> receive(Function<Runnable, CompletableFuture<FriendlyByteBuf>> executor, FriendlyByteBuf buf, ServerPlayer sender) {
        BlockPos pos = buf.readBlockPos();
        return (CompletableFuture<FriendlyByteBuf>) executor.apply((Runnable) () -> SelectionManager.of(sender).leftClickBlock(sender.serverLevel(), pos, sender));
    }

    public static void send(BlockPos pos) {
        I.sendToServer(buf -> buf.writeBlockPos(pos));
    }
}