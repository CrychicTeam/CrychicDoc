package me.steinborn.krypton.mod.shared.network.compression;

import com.velocitypowered.natives.compression.VelocityCompressor;
import com.velocitypowered.natives.util.MoreByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.network.FriendlyByteBuf;

public class MinecraftCompressEncoder extends MessageToByteEncoder<ByteBuf> {

    private int threshold;

    private final VelocityCompressor compressor;

    public MinecraftCompressEncoder(int threshold, VelocityCompressor compressor) {
        this.threshold = threshold;
        this.compressor = compressor;
    }

    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        FriendlyByteBuf wrappedBuf = new FriendlyByteBuf(out);
        int uncompressed = msg.readableBytes();
        if (uncompressed < this.threshold) {
            wrappedBuf.writeVarInt(0);
            out.writeBytes(msg);
        } else {
            wrappedBuf.writeVarInt(uncompressed);
            ByteBuf compatibleIn = MoreByteBufUtils.ensureCompatible(ctx.alloc(), this.compressor, msg);
            try {
                this.compressor.deflate(compatibleIn, out);
            } finally {
                compatibleIn.release();
            }
        }
    }

    protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect) throws Exception {
        int initialBufferSize = msg.readableBytes() + 1;
        return MoreByteBufUtils.preferredBuffer(ctx.alloc(), this.compressor, initialBufferSize);
    }

    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.compressor.close();
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}