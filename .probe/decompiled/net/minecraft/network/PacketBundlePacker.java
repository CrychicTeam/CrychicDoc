package net.minecraft.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.BundlerInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;

public class PacketBundlePacker extends MessageToMessageDecoder<Packet<?>> {

    @Nullable
    private BundlerInfo.Bundler currentBundler;

    @Nullable
    private BundlerInfo infoForCurrentBundler;

    private final PacketFlow flow;

    public PacketBundlePacker(PacketFlow packetFlow0) {
        this.flow = packetFlow0;
    }

    protected void decode(ChannelHandlerContext channelHandlerContext0, Packet<?> packet1, List<Object> listObject2) throws Exception {
        BundlerInfo.Provider $$3 = (BundlerInfo.Provider) channelHandlerContext0.channel().attr(BundlerInfo.BUNDLER_PROVIDER).get();
        if ($$3 == null) {
            throw new DecoderException("Bundler not configured: " + packet1);
        } else {
            BundlerInfo $$4 = $$3.getBundlerInfo(this.flow);
            if (this.currentBundler != null) {
                if (this.infoForCurrentBundler != $$4) {
                    throw new DecoderException("Bundler handler changed during bundling");
                }
                Packet<?> $$5 = this.currentBundler.addPacket(packet1);
                if ($$5 != null) {
                    this.infoForCurrentBundler = null;
                    this.currentBundler = null;
                    listObject2.add($$5);
                }
            } else {
                BundlerInfo.Bundler $$6 = $$4.startPacketBundling(packet1);
                if ($$6 != null) {
                    this.currentBundler = $$6;
                    this.infoForCurrentBundler = $$4;
                } else {
                    listObject2.add(packet1);
                }
            }
        }
    }
}