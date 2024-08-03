package fuzs.puzzleslib.api.core.v1;

import fuzs.puzzleslib.impl.core.ProxyImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface Proxy {

    Proxy INSTANCE = ProxyImpl.INSTANCE;

    Player getClientPlayer();

    Level getClientLevel();

    ClientPacketListener getClientPacketListener();

    @Deprecated(forRemoval = true)
    default MinecraftServer getGameServer() {
        return CommonAbstractions.INSTANCE.getMinecraftServer();
    }

    boolean hasControlDown();

    boolean hasShiftDown();

    boolean hasAltDown();

    Component getKeyMappingComponent(String var1);
}