package mezz.jei.common.network.packets;

import java.util.concurrent.CompletableFuture;
import mezz.jei.common.network.ClientPacketData;

@FunctionalInterface
public interface IClientPacketHandler {

    CompletableFuture<Void> readPacketData(ClientPacketData var1);
}