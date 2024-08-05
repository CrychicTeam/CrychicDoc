package mezz.jei.common.network.packets;

import java.util.concurrent.CompletableFuture;
import mezz.jei.common.network.ServerPacketData;

@FunctionalInterface
public interface IServerPacketHandler {

    CompletableFuture<Void> readPacketData(ServerPacketData var1);
}