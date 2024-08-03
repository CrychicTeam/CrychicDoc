package net.minecraft.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import net.minecraft.network.protocol.BundlerInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;

public class PacketBundleUnpacker extends MessageToMessageEncoder<Packet<?>> {

    private final PacketFlow flow;

    public PacketBundleUnpacker(PacketFlow packetFlow0) {
        this.flow = packetFlow0;
    }

    protected void encode(ChannelHandlerContext channelHandlerContext0, Packet<?> packet1, List<Object> listObject2) throws Exception {
        BundlerInfo.Provider $$3 = (BundlerInfo.Provider) channelHandlerContext0.channel().attr(BundlerInfo.BUNDLER_PROVIDER).get();
        if ($$3 == null) {
            throw new EncoderException("Bundler not configured: " + packet1);
        } else {
            $$3.getBundlerInfo(this.flow).unbundlePacket(packet1, listObject2::add);
        }
    }
}