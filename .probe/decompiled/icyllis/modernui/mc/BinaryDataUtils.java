package icyllis.modernui.mc;

import icyllis.modernui.util.DataSet;
import icyllis.modernui.util.IOStreamParcel;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;

public final class BinaryDataUtils {

    private BinaryDataUtils() {
    }

    @Nonnull
    public static FriendlyByteBuf writeDataSet(@Nonnull FriendlyByteBuf buf, @Nullable DataSet source) {
        try {
            try (IOStreamParcel p = new IOStreamParcel(null, new ByteBufOutputStream(buf))) {
                p.writeDataSet(source);
            }
            return buf;
        } catch (Exception var7) {
            throw new EncoderException(var7);
        }
    }

    @Nullable
    public static DataSet readDataSet(@Nonnull FriendlyByteBuf buf, @Nullable ClassLoader loader) {
        try {
            DataSet var3;
            try (IOStreamParcel p = new IOStreamParcel(new ByteBufInputStream(buf), null)) {
                var3 = p.readDataSet(loader);
            }
            return var3;
        } catch (Exception var7) {
            throw new DecoderException(var7);
        }
    }
}