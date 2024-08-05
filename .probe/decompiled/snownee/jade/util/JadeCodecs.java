package snownee.jade.util;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;
import snownee.jade.api.config.TargetBlocklist;

public class JadeCodecs {

    public static final Codec<OptionalInt> OPTIONAL_INT = new Codec<OptionalInt>() {

        public <T> DataResult<Pair<OptionalInt, T>> decode(DynamicOps<T> ops, T input) {
            return DataResult.success((Pair) ops.getNumberValue(input).map(number -> Pair.of(OptionalInt.of(number.intValue()), ops.empty())).get().left().orElseGet(() -> Pair.of(OptionalInt.empty(), ops.empty())));
        }

        public <T> DataResult<T> encode(OptionalInt input, DynamicOps<T> ops, T prefix) {
            return input.isPresent() ? DataResult.success(ops.createInt(input.getAsInt())) : DataResult.success(ops.empty());
        }
    };

    public static final Codec<TargetBlocklist> TARGET_BLOCKLIST_CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING.optionalFieldOf("__comment", "").forGetter($ -> $.__comment), Codec.STRING.listOf().fieldOf("values").forGetter($ -> $.values), ExtraCodecs.POSITIVE_INT.optionalFieldOf("version", 1).forGetter($ -> $.version)).apply(i, (comment, values, version) -> Util.make(new TargetBlocklist(), it -> {
        it.values = values;
        it.version = version;
    })));

    public static <T> T createFromEmptyMap(Codec<T> codec) {
        return (T) codec.parse(JsonOps.INSTANCE, (JsonElement) JsonOps.INSTANCE.emptyMap()).get().left().orElseThrow();
    }
}