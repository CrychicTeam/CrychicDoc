package me.steinborn.krypton.mod.shared.network.pipeline;

import com.google.common.base.Preconditions;
import com.velocitypowered.natives.encryption.VelocityCipher;
import com.velocitypowered.natives.util.MoreByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

public class MinecraftCipherDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final VelocityCipher cipher;

    public MinecraftCipherDecoder(VelocityCipher cipher) {
        this.cipher = (VelocityCipher) Preconditions.checkNotNull(cipher, "cipher");
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf compatible = MoreByteBufUtils.ensureCompatible(ctx.alloc(), this.cipher, in).slice();
        try {
            this.cipher.process(compatible);
            out.add(compatible);
        } catch (Exception var6) {
            compatible.release();
            throw var6;
        }
    }

    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.cipher.close();
    }
}