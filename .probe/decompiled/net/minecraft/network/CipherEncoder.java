package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.Cipher;

public class CipherEncoder extends MessageToByteEncoder<ByteBuf> {

    private final CipherBase cipher;

    public CipherEncoder(Cipher cipher0) {
        this.cipher = new CipherBase(cipher0);
    }

    protected void encode(ChannelHandlerContext channelHandlerContext0, ByteBuf byteBuf1, ByteBuf byteBuf2) throws Exception {
        this.cipher.encipher(byteBuf1, byteBuf2);
    }
}