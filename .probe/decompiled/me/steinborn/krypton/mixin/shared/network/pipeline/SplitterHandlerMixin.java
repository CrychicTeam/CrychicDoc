package me.steinborn.krypton.mixin.shared.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import me.steinborn.krypton.mod.shared.network.VarintByteDecoder;
import me.steinborn.krypton.mod.shared.network.util.WellKnownExceptions;
import net.minecraft.network.Varint21FrameDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ Varint21FrameDecoder.class })
public class SplitterHandlerMixin {

    private final VarintByteDecoder reader = new VarintByteDecoder();

    @Overwrite
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!ctx.channel().isActive()) {
            in.clear();
        } else {
            this.reader.reset();
            int varintEnd = in.forEachByte(this.reader);
            if (varintEnd == -1) {
                if (this.reader.getResult() == VarintByteDecoder.DecodeResult.RUN_OF_ZEROES) {
                    in.clear();
                }
            } else {
                if (this.reader.getResult() == VarintByteDecoder.DecodeResult.RUN_OF_ZEROES) {
                    in.readerIndex(varintEnd);
                } else if (this.reader.getResult() == VarintByteDecoder.DecodeResult.SUCCESS) {
                    int readVarint = this.reader.getReadVarint();
                    int bytesRead = this.reader.getBytesRead();
                    if (readVarint < 0) {
                        in.clear();
                        throw WellKnownExceptions.BAD_LENGTH_CACHED;
                    }
                    if (readVarint == 0) {
                        in.readerIndex(varintEnd + 1);
                    } else {
                        int minimumRead = bytesRead + readVarint;
                        if (in.isReadable(minimumRead)) {
                            out.add(in.retainedSlice(varintEnd + 1, readVarint));
                            in.skipBytes(minimumRead);
                        }
                    }
                } else if (this.reader.getResult() == VarintByteDecoder.DecodeResult.TOO_BIG) {
                    in.clear();
                    throw WellKnownExceptions.VARINT_BIG_CACHED;
                }
            }
        }
    }
}