package org.violetmoon.zeta.network;

import java.util.List;
import java.util.function.Function;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.Zeta;

public abstract class ZetaNetworkHandler {

    public ZetaMessageSerializer serializer = new ZetaMessageSerializer();

    protected final Zeta zeta;

    protected final int protocolVersion;

    public ZetaNetworkHandler(Zeta zeta, int protocolVersion) {
        this.zeta = zeta;
        this.protocolVersion = protocolVersion;
    }

    public ZetaMessageSerializer getSerializer() {
        return this.serializer;
    }

    public void sendToPlayers(IZetaMessage msg, Iterable<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            this.sendToPlayer(msg, player);
        }
    }

    public void sendToAllPlayers(IZetaMessage msg, MinecraftServer server) {
        this.sendToPlayers(msg, server.getPlayerList().getPlayers());
    }

    public abstract <T extends IZetaMessage> void register(Class<T> var1, ZetaNetworkDirection var2);

    public abstract <T extends ZetaHandshakeMessage> void registerLogin(Class<T> var1, ZetaNetworkDirection var2, int var3, boolean var4, @Nullable Function<Boolean, List<Pair<String, T>>> var5);

    public abstract void sendToPlayer(IZetaMessage var1, ServerPlayer var2);

    public abstract void sendToServer(IZetaMessage var1);

    public abstract Packet<?> wrapInVanilla(IZetaMessage var1, ZetaNetworkDirection var2);
}