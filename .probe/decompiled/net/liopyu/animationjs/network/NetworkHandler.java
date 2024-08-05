package net.liopyu.animationjs.network;

import net.liopyu.animationjs.network.packet.AnimationStateUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("animationjs", "channel"), () -> "1.0", "1.0"::equals, "1.0"::equals);

    public static void init() {
        CHANNEL.registerMessage(0, AnimationStateUpdatePacket.class, AnimationStateUpdatePacket::encode, AnimationStateUpdatePacket::decode, AnimationStateUpdatePacket::handle);
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }
}