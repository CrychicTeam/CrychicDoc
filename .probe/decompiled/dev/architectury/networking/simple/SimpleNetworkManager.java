package dev.architectury.networking.simple;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.transformers.PacketTransformer;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.Experimental;

public class SimpleNetworkManager {

    public final String namespace;

    public static SimpleNetworkManager create(String namespace) {
        return new SimpleNetworkManager(namespace);
    }

    private SimpleNetworkManager(String namespace) {
        this.namespace = namespace;
    }

    public MessageType registerS2C(String id, MessageDecoder<BaseS2CMessage> decoder) {
        return this.registerS2C(id, decoder, List.of());
    }

    @Experimental
    public MessageType registerS2C(String id, MessageDecoder<BaseS2CMessage> decoder, List<PacketTransformer> transformers) {
        MessageType messageType = new MessageType(this, new ResourceLocation(this.namespace, id), NetworkManager.s2c());
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.NetworkReceiver receiver = decoder.createReceiver();
            NetworkManager.registerReceiver(NetworkManager.s2c(), messageType.getId(), transformers, receiver);
        }
        return messageType;
    }

    public MessageType registerC2S(String id, MessageDecoder<BaseC2SMessage> decoder) {
        return this.registerC2S(id, decoder, List.of());
    }

    @Experimental
    public MessageType registerC2S(String id, MessageDecoder<BaseC2SMessage> decoder, List<PacketTransformer> transformers) {
        MessageType messageType = new MessageType(this, new ResourceLocation(this.namespace, id), NetworkManager.c2s());
        NetworkManager.NetworkReceiver receiver = decoder.createReceiver();
        NetworkManager.registerReceiver(NetworkManager.c2s(), messageType.getId(), transformers, receiver);
        return messageType;
    }
}