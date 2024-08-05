package net.minecraft.network;

import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import org.slf4j.Logger;

public class PacketEncoder extends MessageToByteEncoder<Packet<?>> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final PacketFlow flow;

    public PacketEncoder(PacketFlow packetFlow0) {
        this.flow = packetFlow0;
    }

    protected void encode(ChannelHandlerContext channelHandlerContext0, Packet<?> packet1, ByteBuf byteBuf2) throws Exception {
        ConnectionProtocol $$3 = (ConnectionProtocol) channelHandlerContext0.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get();
        if ($$3 == null) {
            throw new RuntimeException("ConnectionProtocol unknown: " + packet1);
        } else {
            int $$4 = $$3.getPacketId(this.flow, packet1);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(Connection.PACKET_SENT_MARKER, "OUT: [{}:{}] {}", new Object[] { channelHandlerContext0.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get(), $$4, packet1.getClass().getName() });
            }
            if ($$4 == -1) {
                throw new IOException("Can't serialize unregistered packet");
            } else {
                FriendlyByteBuf $$5 = new FriendlyByteBuf(byteBuf2);
                $$5.writeVarInt($$4);
                try {
                    int $$6 = $$5.writerIndex();
                    packet1.write($$5);
                    int $$7 = $$5.writerIndex() - $$6;
                    if ($$7 > 8388608) {
                        throw new IllegalArgumentException("Packet too big (is " + $$7 + ", should be less than 8388608): " + packet1);
                    } else {
                        int $$8 = ((ConnectionProtocol) channelHandlerContext0.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get()).getId();
                        JvmProfiler.INSTANCE.onPacketSent($$8, $$4, channelHandlerContext0.channel().remoteAddress(), $$7);
                    }
                } catch (Throwable var10) {
                    LOGGER.error("Error receiving packet {}", $$4, var10);
                    if (packet1.isSkippable()) {
                        throw new SkipPacketException(var10);
                    } else {
                        throw var10;
                    }
                }
            }
        }
    }
}