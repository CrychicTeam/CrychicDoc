package net.minecraftforge.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.event.EventNetworkChannel;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus.Internal;

public class LoginWrapper {

    private static final Logger LOGGER = LogManager.getLogger();

    @Internal
    public static final ResourceLocation WRAPPER = new ResourceLocation("fml:loginwrapper");

    private EventNetworkChannel wrapperChannel = NetworkRegistry.ChannelBuilder.named(WRAPPER).clientAcceptedVersions(a -> true).serverAcceptedVersions(a -> true).networkProtocolVersion(() -> "FML3").eventNetworkChannel();

    LoginWrapper() {
        this.wrapperChannel.addListener(this::wrapperReceived);
    }

    private <T extends NetworkEvent> void wrapperReceived(T packet) {
        if (!(packet instanceof NetworkEvent.ChannelRegistrationChangeEvent)) {
            NetworkEvent.Context wrappedContext = (NetworkEvent.Context) packet.getSource().get();
            FriendlyByteBuf payload = packet.getPayload();
            ResourceLocation targetNetworkReceiver = NetworkConstants.FML_HANDSHAKE_RESOURCE;
            FriendlyByteBuf data = null;
            if (payload != null) {
                targetNetworkReceiver = payload.readResourceLocation();
                int payloadLength = payload.readVarInt();
                data = new FriendlyByteBuf(payload.readBytes(payloadLength));
            }
            int loginSequence = packet.getLoginIndex();
            LOGGER.debug(HandshakeHandler.FMLHSMARKER, "Recieved login wrapper packet event for channel {} with index {}", targetNetworkReceiver, loginSequence);
            NetworkEvent.Context context = new NetworkEvent.Context(wrappedContext.getNetworkManager(), wrappedContext.getDirection(), (rl, buf) -> {
                LOGGER.debug(HandshakeHandler.FMLHSMARKER, "Dispatching wrapped packet reply for channel {} with index {}", rl, loginSequence);
                wrappedContext.getPacketDispatcher().sendPacket(WRAPPER, this.wrapPacket(rl, buf));
            });
            NetworkEvent.LoginPayloadEvent loginPayloadEvent = new NetworkEvent.LoginPayloadEvent(data, () -> context, loginSequence);
            NetworkRegistry.findTarget(targetNetworkReceiver).ifPresent(ni -> {
                ni.dispatchLoginPacket(loginPayloadEvent);
                wrappedContext.setPacketHandled(context.getPacketHandled());
            });
        }
    }

    private FriendlyByteBuf wrapPacket(ResourceLocation rl, FriendlyByteBuf buf) {
        FriendlyByteBuf pb = new FriendlyByteBuf(Unpooled.buffer(buf.capacity()));
        pb.writeResourceLocation(rl);
        pb.writeVarInt(buf.readableBytes());
        pb.writeBytes(buf);
        return pb;
    }

    void sendServerToClientLoginPacket(ResourceLocation resourceLocation, FriendlyByteBuf buffer, int index, Connection manager) {
        FriendlyByteBuf pb = this.wrapPacket(resourceLocation, buffer);
        manager.send(NetworkDirection.LOGIN_TO_CLIENT.<Packet<?>>buildPacket(Pair.of(pb, index), WRAPPER).getThis());
    }
}