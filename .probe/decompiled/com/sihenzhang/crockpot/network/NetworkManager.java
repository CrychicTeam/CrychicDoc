package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.util.RLUtils;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkManager {

    private static final String PROTOCOL_VERSION = "1.0";

    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(RLUtils.createRL("main"), () -> "1.0", "1.0"::equals, "1.0"::equals);

    private static int id = 0;

    private NetworkManager() {
        throw new UnsupportedOperationException("No instance");
    }

    public static <MSG> void registerPacket(Class<MSG> msg, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler, NetworkDirection direction) {
        INSTANCE.registerMessage(id++, msg, encoder, decoder, handler, Optional.of(direction));
    }

    public static <MSG> void sendToPlayer(ServerPlayer player, MSG msg) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MSG> void sendToAll(MSG msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}