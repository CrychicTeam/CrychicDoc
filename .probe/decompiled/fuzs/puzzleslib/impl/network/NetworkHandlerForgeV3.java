package fuzs.puzzleslib.impl.network;

import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import fuzs.puzzleslib.api.network.v3.serialization.MessageSerializers;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandlerForgeV3 extends NetworkHandlerRegistryImpl {

    private static final String PROTOCOL_VERSION = Integer.toString(1);

    private SimpleChannel channel;

    private final AtomicInteger discriminator = new AtomicInteger();

    public NetworkHandlerForgeV3(ResourceLocation channelIdentifier) {
        super(channelIdentifier);
    }

    @Override
    public <T extends Record & ClientboundMessage<T>> void registerClientbound$Internal(Class<?> clazz) {
        this.register((Class<T>) clazz, (x$0, x$1) -> rec$.registerClientReceiverV2((T) x$0, x$1), NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public <T extends Record & ServerboundMessage<T>> void registerServerbound$Internal(Class<?> clazz) {
        this.register((Class<T>) clazz, (x$0, x$1) -> rec$.registerServerReceiverV2((T) x$0, x$1), NetworkDirection.PLAY_TO_SERVER);
    }

    private <T> void register(Class<T> clazz, BiConsumer<T, Supplier<NetworkEvent.Context>> handle, NetworkDirection networkDirection) {
        if (!clazz.isRecord()) {
            throw new IllegalArgumentException("Message of type %s is not a record".formatted(clazz));
        } else {
            Objects.requireNonNull(this.channel, "channel is null");
            BiConsumer<T, FriendlyByteBuf> encode = (t, friendlyByteBuf) -> MessageSerializers.findByType(clazz).write(friendlyByteBuf, t);
            Function<FriendlyByteBuf, T> decode = MessageSerializers.findByType(clazz)::read;
            this.channel.registerMessage(this.discriminator.getAndIncrement(), clazz, encode, decode, handle, Optional.of(networkDirection));
        }
    }

    @Override
    public <T extends Record & ClientboundMessage<T>> Packet<ClientGamePacketListener> toClientboundPacket(T message) {
        Objects.requireNonNull(this.channel, "channel is null");
        Objects.requireNonNull(message, "message is null");
        return (Packet<ClientGamePacketListener>) this.channel.toVanillaPacket(message, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public <T extends Record & ServerboundMessage<T>> Packet<ServerGamePacketListener> toServerboundPacket(T message) {
        Objects.requireNonNull(this.channel, "channel is null");
        Objects.requireNonNull(message, "message is null");
        return (Packet<ServerGamePacketListener>) this.channel.toVanillaPacket(message, NetworkDirection.PLAY_TO_SERVER);
    }

    @Override
    public void build() {
        if (this.channel != null) {
            throw new IllegalStateException("channel is already built");
        } else {
            this.channel = buildSimpleChannel(this.channelIdentifier, this.clientAcceptsVanillaOrMissing, this.serverAcceptsVanillaOrMissing);
            super.build();
        }
    }

    private static SimpleChannel buildSimpleChannel(ResourceLocation resourceLocation, boolean clientAcceptsVanillaOrMissing, boolean serverAcceptsVanillaOrMissing) {
        return NetworkRegistry.ChannelBuilder.named(resourceLocation).networkProtocolVersion(() -> PROTOCOL_VERSION).clientAcceptedVersions(clientAcceptsVanillaOrMissing ? NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION) : PROTOCOL_VERSION::equals).serverAcceptedVersions(serverAcceptsVanillaOrMissing ? NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION) : PROTOCOL_VERSION::equals).simpleChannel();
    }
}