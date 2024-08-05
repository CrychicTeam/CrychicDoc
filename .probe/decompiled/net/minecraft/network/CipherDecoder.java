package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import javax.crypto.Cipher;

public class CipherDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final CipherBase cipher;

    public CipherDecoder(Cipher cipher0) {
        this.cipher = new CipherBase(cipher0);
    }

    protected void decode(ChannelHandlerContext channelHandlerContext0, ByteBuf byteBuf1, List<Object> listObject2) throws Exception {
        listObject2.add(this.cipher.decipher(channelHandlerContext0, byteBuf1));
    }
}