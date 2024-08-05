package journeymap.common.network.data;

import net.minecraft.server.level.ServerPlayer;

public interface NetworkHandler {

    <T> void sendToServer(T var1);

    <T> void sendToClient(T var1, ServerPlayer var2);
}