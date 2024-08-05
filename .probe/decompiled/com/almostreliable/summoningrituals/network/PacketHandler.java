package com.almostreliable.summoningrituals.network;

import com.almostreliable.summoningrituals.util.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketHandler {

    private static final ResourceLocation ID = Utils.getRL("network");

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(ID).networkProtocolVersion(() -> "1").clientAcceptedVersions("1"::equals).serverAcceptedVersions("1"::equals).simpleChannel();

    private PacketHandler() {
    }

    public static void init() {
        int packetId = -1;
        register(++packetId, ClientAltarUpdatePacket.class, new ClientAltarUpdatePacket());
        register(++packetId, SacrificeParticlePacket.class, new SacrificeParticlePacket());
    }

    private static <T> void register(int packetId, Class<T> clazz, Packet<T> packet) {
        CHANNEL.registerMessage(packetId, clazz, packet::encode, packet::decode, packet::handle);
    }
}