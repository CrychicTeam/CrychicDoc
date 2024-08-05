package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class CipherBase {

    private final Cipher cipher;

    private byte[] heapIn = new byte[0];

    private byte[] heapOut = new byte[0];

    protected CipherBase(Cipher cipher0) {
        this.cipher = cipher0;
    }

    private byte[] bufToByte(ByteBuf byteBuf0) {
        int $$1 = byteBuf0.readableBytes();
        if (this.heapIn.length < $$1) {
            this.heapIn = new byte[$$1];
        }
        byteBuf0.readBytes(this.heapIn, 0, $$1);
        return this.heapIn;
    }

    protected ByteBuf decipher(ChannelHandlerContext channelHandlerContext0, ByteBuf byteBuf1) throws ShortBufferException {
        int $$2 = byteBuf1.readableBytes();
        byte[] $$3 = this.bufToByte(byteBuf1);
        ByteBuf $$4 = channelHandlerContext0.alloc().heapBuffer(this.cipher.getOutputSize($$2));
        $$4.writerIndex(this.cipher.update($$3, 0, $$2, $$4.array(), $$4.arrayOffset()));
        return $$4;
    }

    protected void encipher(ByteBuf byteBuf0, ByteBuf byteBuf1) throws ShortBufferException {
        int $$2 = byteBuf0.readableBytes();
        byte[] $$3 = this.bufToByte(byteBuf0);
        int $$4 = this.cipher.getOutputSize($$2);
        if (this.heapOut.length < $$4) {
            this.heapOut = new byte[$$4];
        }
        byteBuf1.writeBytes(this.heapOut, 0, this.cipher.update($$3, 0, $$2, this.heapOut));
    }
}