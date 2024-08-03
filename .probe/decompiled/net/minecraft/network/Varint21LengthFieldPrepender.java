package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class Varint21LengthFieldPrepender extends MessageToByteEncoder<ByteBuf> {

    private static final int MAX_BYTES = 3;

    protected void encode(ChannelHandlerContext channelHandlerContext0, ByteBuf byteBuf1, ByteBuf byteBuf2) {
        int $$3 = byteBuf1.readableBytes();
        int $$4 = FriendlyByteBuf.getVarIntSize($$3);
        if ($$4 > 3) {
            throw new IllegalArgumentException("unable to fit " + $$3 + " into 3");
        } else {
            FriendlyByteBuf $$5 = new FriendlyByteBuf(byteBuf2);
            $$5.ensureWritable($$4 + $$3);
            $$5.writeVarInt($$3);
            $$5.writeBytes(byteBuf1, byteBuf1.readerIndex(), $$3);
        }
    }
}