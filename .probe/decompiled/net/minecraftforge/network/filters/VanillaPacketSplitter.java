package net.minecraftforge.network.filters;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ConnectionData;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VanillaPacketSplitter {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation CHANNEL = new ResourceLocation("forge", "split");

    private static final String VERSION = "1.1";

    private static final int PROTOCOL_MAX = 8388608;

    private static final int PAYLOAD_TO_CLIENT_MAX = 1048576;

    private static final int PART_SIZE = 1048570;

    private static final byte STATE_FIRST = 1;

    private static final byte STATE_LAST = 2;

    private static final List<FriendlyByteBuf> receivedBuffers = new ArrayList();

    public static void register() {
        Predicate<String> versionCheck = NetworkRegistry.acceptMissingOr("1.1");
        EventNetworkChannel channel = NetworkRegistry.newEventChannel(CHANNEL, () -> "1.1", versionCheck, versionCheck);
        channel.addListener(VanillaPacketSplitter::onClientPacket);
    }

    public static void appendPackets(ConnectionProtocol protocol, PacketFlow direction, Packet<?> packet, List<? super Packet<?>> out) {
        if (heuristicIsDefinitelySmallEnough(packet)) {
            out.add(packet);
        } else {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            packet.write(buf);
            if (buf.readableBytes() <= 8388608) {
                buf.release();
                out.add(packet);
            } else {
                int parts = (int) Math.ceil((double) buf.readableBytes() / 1048570.0);
                if (parts == 1) {
                    buf.release();
                    out.add(packet);
                } else {
                    for (int part = 0; part < parts; part++) {
                        ByteBuf partPrefix;
                        if (part == 0) {
                            partPrefix = Unpooled.buffer(5);
                            partPrefix.writeByte(1);
                            new FriendlyByteBuf(partPrefix).writeVarInt(protocol.getPacketId(direction, packet));
                        } else {
                            partPrefix = Unpooled.buffer(1);
                            partPrefix.writeByte(part == parts - 1 ? 2 : 0);
                        }
                        int partSize = Math.min(1048570, buf.readableBytes());
                        ByteBuf partBuf = Unpooled.wrappedBuffer(new ByteBuf[] { partPrefix, buf.retainedSlice(buf.readerIndex(), partSize) });
                        buf.skipBytes(partSize);
                        out.add(new ClientboundCustomPayloadPacket(CHANNEL, new FriendlyByteBuf(partBuf)));
                    }
                    buf.release();
                }
            }
        }
    }

    private static boolean heuristicIsDefinitelySmallEnough(Packet<?> packet) {
        return false;
    }

    private static void onClientPacket(NetworkEvent.ServerCustomPayloadEvent event) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) event.getSource().get();
        PacketFlow direction = ctx.getDirection() == NetworkDirection.PLAY_TO_CLIENT ? PacketFlow.CLIENTBOUND : PacketFlow.SERVERBOUND;
        ConnectionProtocol protocol = ConnectionProtocol.PLAY;
        ctx.setPacketHandled(true);
        FriendlyByteBuf buf = event.getPayload();
        byte state = buf.readByte();
        if (state == 1 && !receivedBuffers.isEmpty()) {
            LOGGER.warn("forge:split received out of order - inbound buffer not empty when receiving first");
            receivedBuffers.clear();
        }
        buf.retain();
        receivedBuffers.add(buf);
        if (state == 2) {
            FriendlyByteBuf full = new FriendlyByteBuf(Unpooled.wrappedBuffer((ByteBuf[]) receivedBuffers.toArray(new FriendlyByteBuf[0])));
            int packetId = full.readVarInt();
            Packet<?> packet = protocol.createPacket(direction, packetId, full);
            if (packet == null) {
                LOGGER.error("Received invalid packet ID {} in forge:split", packetId);
            } else {
                receivedBuffers.clear();
                full.release();
                ctx.enqueueWork(() -> genericsFtw(packet, ((NetworkEvent.Context) event.getSource().get()).getNetworkManager().getPacketListener()));
            }
        }
    }

    private static <T extends PacketListener> void genericsFtw(Packet<T> pkt, Object listener) {
        pkt.handle((T) listener);
    }

    public static VanillaPacketSplitter.RemoteCompatibility getRemoteCompatibility(Connection manager) {
        ConnectionData connectionData = NetworkHooks.getConnectionData(manager);
        return connectionData != null && connectionData.getChannels().containsKey(CHANNEL) ? VanillaPacketSplitter.RemoteCompatibility.PRESENT : VanillaPacketSplitter.RemoteCompatibility.ABSENT;
    }

    public static boolean isRemoteCompatible(Connection manager) {
        return getRemoteCompatibility(manager) != VanillaPacketSplitter.RemoteCompatibility.ABSENT;
    }

    public static enum RemoteCompatibility {

        ABSENT, PRESENT
    }
}