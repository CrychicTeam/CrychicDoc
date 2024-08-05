package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import java.util.zip.Inflater;

public class CompressionDecoder extends ByteToMessageDecoder {

    public static final int MAXIMUM_COMPRESSED_LENGTH = 2097152;

    public static final int MAXIMUM_UNCOMPRESSED_LENGTH = 8388608;

    private final Inflater inflater;

    private int threshold;

    private boolean validateDecompressed;

    public CompressionDecoder(int int0, boolean boolean1) {
        this.threshold = int0;
        this.validateDecompressed = boolean1;
        this.inflater = new Inflater();
    }

    protected void decode(ChannelHandlerContext channelHandlerContext0, ByteBuf byteBuf1, List<Object> listObject2) throws Exception {
        if (byteBuf1.readableBytes() != 0) {
            FriendlyByteBuf $$3 = new FriendlyByteBuf(byteBuf1);
            int $$4 = $$3.readVarInt();
            if ($$4 == 0) {
                listObject2.add($$3.readBytes($$3.readableBytes()));
            } else {
                if (this.validateDecompressed) {
                    if ($$4 < this.threshold) {
                        throw new DecoderException("Badly compressed packet - size of " + $$4 + " is below server threshold of " + this.threshold);
                    }
                    if ($$4 > 8388608) {
                        throw new DecoderException("Badly compressed packet - size of " + $$4 + " is larger than protocol maximum of 8388608");
                    }
                }
                byte[] $$5 = new byte[$$3.readableBytes()];
                $$3.readBytes($$5);
                this.inflater.setInput($$5);
                byte[] $$6 = new byte[$$4];
                this.inflater.inflate($$6);
                listObject2.add(Unpooled.wrappedBuffer($$6));
                this.inflater.reset();
            }
        }
    }

    public void setThreshold(int int0, boolean boolean1) {
        this.threshold = int0;
        this.validateDecompressed = boolean1;
    }
}