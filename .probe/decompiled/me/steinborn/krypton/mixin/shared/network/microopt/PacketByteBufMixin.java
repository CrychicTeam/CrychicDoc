package me.steinborn.krypton.mixin.shared.network.microopt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.EncoderException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import me.steinborn.krypton.mod.shared.network.util.VarIntUtil;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ FriendlyByteBuf.class })
public abstract class PacketByteBufMixin extends ByteBuf {

    @Shadow
    @Final
    private ByteBuf source;

    @Shadow
    public abstract int writeCharSequence(CharSequence var1, Charset var2);

    @Overwrite
    public static int getVarIntSize(int value) {
        return VarIntUtil.getVarIntLength(value);
    }

    @Overwrite
    public FriendlyByteBuf writeUtf(String string, int i) {
        if (string.length() > i) {
            throw new EncoderException("String too big (was " + string.length() + " characters, max " + i + ")");
        } else {
            int utf8Bytes = ByteBufUtil.utf8Bytes(string);
            if (utf8Bytes > i * 3) {
                throw new EncoderException("String too big (was " + utf8Bytes + " bytes encoded, max " + i * 3 + ")");
            } else {
                this.writeVarInt(utf8Bytes);
                this.writeCharSequence(string, StandardCharsets.UTF_8);
                return new FriendlyByteBuf(this.source);
            }
        }
    }

    @Overwrite
    public FriendlyByteBuf writeVarInt(int value) {
        if ((value & -128) == 0) {
            this.source.writeByte(value);
        } else if ((value & -16384) == 0) {
            int w = (value & 127 | 128) << 8 | value >>> 7;
            this.source.writeShort(w);
        } else {
            writeVarIntFull(this.source, value);
        }
        return new FriendlyByteBuf(this.source);
    }

    private static void writeVarIntFull(ByteBuf buf, int value) {
        if ((value & -128) == 0) {
            buf.writeByte(value);
        } else if ((value & -16384) == 0) {
            int w = (value & 127 | 128) << 8 | value >>> 7;
            buf.writeShort(w);
        } else if ((value & -2097152) == 0) {
            int w = (value & 127 | 128) << 16 | (value >>> 7 & 127 | 128) << 8 | value >>> 14;
            buf.writeMedium(w);
        } else if ((value & -268435456) == 0) {
            int w = (value & 127 | 128) << 24 | (value >>> 7 & 127 | 128) << 16 | (value >>> 14 & 127 | 128) << 8 | value >>> 21;
            buf.writeInt(w);
        } else {
            int w = (value & 127 | 128) << 24 | (value >>> 7 & 127 | 128) << 16 | (value >>> 14 & 127 | 128) << 8 | value >>> 21 & 127 | 128;
            buf.writeInt(w);
            buf.writeByte(value >>> 28);
        }
    }
}