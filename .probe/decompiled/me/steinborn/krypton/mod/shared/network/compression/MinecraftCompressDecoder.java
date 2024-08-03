package me.steinborn.krypton.mod.shared.network.compression;

import com.velocitypowered.natives.compression.VelocityCompressor;
import com.velocitypowered.natives.util.MoreByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;

public class MinecraftCompressDecoder extends ByteToMessageDecoder {

    private static final int UNCOMPRESSED_CAP = 8388608;

    private int threshold;

    private final boolean validate;

    private final VelocityCompressor compressor;

    public MinecraftCompressDecoder(int threshold, boolean validate, VelocityCompressor compressor) {
        this.threshold = threshold;
        this.validate = validate;
        this.compressor = compressor;
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() != 0) {
            FriendlyByteBuf packetBuf = new FriendlyByteBuf(in);
            int claimedUncompressedSize = packetBuf.readVarInt();
            if (claimedUncompressedSize == 0) {
                out.add(packetBuf.readBytes(packetBuf.readableBytes()));
            } else {
                if (this.validate) {
                    if (claimedUncompressedSize < this.threshold) {
                        throw new DecoderException("Badly compressed packet - size of " + claimedUncompressedSize + " is below server threshold of " + this.threshold);
                    }
                    if (claimedUncompressedSize > 8388608) {
                        throw new DecoderException("Badly compressed packet - size of " + claimedUncompressedSize + " is larger than maximum of 8388608");
                    }
                }
                ByteBuf compatibleIn = MoreByteBufUtils.ensureCompatible(ctx.alloc(), this.compressor, in);
                ByteBuf uncompressed = MoreByteBufUtils.preferredBuffer(ctx.alloc(), this.compressor, claimedUncompressedSize);
                try {
                    this.compressor.inflate(compatibleIn, uncompressed, claimedUncompressedSize);
                    out.add(uncompressed);
                    in.clear();
                } catch (Exception var12) {
                    uncompressed.release();
                    throw var12;
                } finally {
                    compatibleIn.release();
                }
            }
        }
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        this.compressor.close();
    }
}