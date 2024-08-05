package se.mickelus.mutil.util;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class HexCodec implements PrimitiveCodec<Integer> {

    public static final HexCodec instance = new HexCodec();

    public <T> DataResult<Integer> read(DynamicOps<T> ops, T input) {
        return ops.getStringValue(input).map(val -> (int) Long.parseLong(val, 16));
    }

    public <T> T write(DynamicOps<T> ops, Integer value) {
        return (T) ops.createString(Integer.toHexString(value));
    }

    public String toString() {
        return "mutil-hex";
    }
}