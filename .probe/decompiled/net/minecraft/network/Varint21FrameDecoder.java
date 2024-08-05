package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

public class Varint21FrameDecoder extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext channelHandlerContext0, ByteBuf byteBuf1, List<Object> listObject2) {
        byteBuf1.markReaderIndex();
        byte[] $$3 = new byte[3];
        for (int $$4 = 0; $$4 < $$3.length; $$4++) {
            if (!byteBuf1.isReadable()) {
                byteBuf1.resetReaderIndex();
                return;
            }
            $$3[$$4] = byteBuf1.readByte();
            if ($$3[$$4] >= 0) {
                FriendlyByteBuf $$5 = new FriendlyByteBuf(Unpooled.wrappedBuffer($$3));
                try {
                    int $$6 = $$5.readVarInt();
                    if (byteBuf1.readableBytes() >= $$6) {
                        listObject2.add(byteBuf1.readBytes($$6));
                        return;
                    }
                    byteBuf1.resetReaderIndex();
                } finally {
                    $$5.release();
                }
                return;
            }
        }
        throw new CorruptedFrameException("length wider than 21-bit");
    }
}