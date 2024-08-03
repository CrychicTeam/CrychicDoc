package net.minecraft.network;

import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import org.slf4j.Logger;

public class PacketDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final PacketFlow flow;

    public PacketDecoder(PacketFlow packetFlow0) {
        this.flow = packetFlow0;
    }

    protected void decode(ChannelHandlerContext channelHandlerContext0, ByteBuf byteBuf1, List<Object> listObject2) throws Exception {
        int $$3 = byteBuf1.readableBytes();
        if ($$3 != 0) {
            FriendlyByteBuf $$4 = new FriendlyByteBuf(byteBuf1);
            int $$5 = $$4.readVarInt();
            Packet<?> $$6 = ((ConnectionProtocol) channelHandlerContext0.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get()).createPacket(this.flow, $$5, $$4);
            if ($$6 == null) {
                throw new IOException("Bad packet id " + $$5);
            } else {
                int $$7 = ((ConnectionProtocol) channelHandlerContext0.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get()).getId();
                JvmProfiler.INSTANCE.onPacketReceived($$7, $$5, channelHandlerContext0.channel().remoteAddress(), $$3);
                if ($$4.readableBytes() > 0) {
                    throw new IOException("Packet " + ((ConnectionProtocol) channelHandlerContext0.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get()).getId() + "/" + $$5 + " (" + $$6.getClass().getSimpleName() + ") was larger than I expected, found " + $$4.readableBytes() + " bytes extra whilst reading packet " + $$5);
                } else {
                    listObject2.add($$6);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(Connection.PACKET_RECEIVED_MARKER, " IN: [{}:{}] {}", new Object[] { channelHandlerContext0.channel().attr(Connection.ATTRIBUTE_PROTOCOL).get(), $$5, $$6.getClass().getName() });
                    }
                }
            }
        }
    }
}