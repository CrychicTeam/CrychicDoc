package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;

public class CompressionEncoder extends MessageToByteEncoder<ByteBuf> {

    private final byte[] encodeBuf = new byte[8192];

    private final Deflater deflater;

    private int threshold;

    public CompressionEncoder(int int0) {
        this.threshold = int0;
        this.deflater = new Deflater();
    }

    protected void encode(ChannelHandlerContext channelHandlerContext0, ByteBuf byteBuf1, ByteBuf byteBuf2) {
        int $$3 = byteBuf1.readableBytes();
        FriendlyByteBuf $$4 = new FriendlyByteBuf(byteBuf2);
        if ($$3 < this.threshold) {
            $$4.writeVarInt(0);
            $$4.writeBytes(byteBuf1);
        } else {
            byte[] $$5 = new byte[$$3];
            byteBuf1.readBytes($$5);
            $$4.writeVarInt($$5.length);
            this.deflater.setInput($$5, 0, $$3);
            this.deflater.finish();
            while (!this.deflater.finished()) {
                int $$6 = this.deflater.deflate(this.encodeBuf);
                $$4.writeBytes(this.encodeBuf, 0, $$6);
            }
            this.deflater.reset();
        }
    }

    public int getThreshold() {
        return this.threshold;
    }

    public void setThreshold(int int0) {
        this.threshold = int0;
    }
}