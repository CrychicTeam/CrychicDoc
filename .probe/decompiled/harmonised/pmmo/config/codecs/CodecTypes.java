package harmonised.pmmo.config.codecs;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import harmonised.pmmo.util.Functions;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

public class CodecTypes {

    public static final Codec<Map<String, Double>> DOUBLE_CODEC = Codec.unboundedMap(Codec.STRING, Codec.DOUBLE);

    public static final Codec<Map<String, Long>> LONG_CODEC = Codec.unboundedMap(Codec.STRING, Codec.LONG);

    public static final Codec<Map<String, Integer>> INTEGER_CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT);

    public static final Codec<Map<String, Map<String, Long>>> DAMAGE_XP_CODEC = Codec.unboundedMap(Codec.STRING, LONG_CODEC);

    public static final Codec<CodecTypes.SalvageData> SALVAGE_CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.unboundedMap(Codec.STRING, Codec.DOUBLE).fieldOf("chancePerLevel").forGetter(CodecTypes.SalvageData::chancePerLevel), Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("levelReq").forGetter(CodecTypes.SalvageData::levelReq), Codec.unboundedMap(Codec.STRING, Codec.LONG).fieldOf("xpPerItem").forGetter(CodecTypes.SalvageData::xpAward), Codec.INT.fieldOf("salvageMax").forGetter(CodecTypes.SalvageData::salvageMax), Codec.DOUBLE.fieldOf("baseChance").forGetter(CodecTypes.SalvageData::baseChance), Codec.DOUBLE.fieldOf("maxChance").forGetter(CodecTypes.SalvageData::maxChance)).apply(instance, CodecTypes.SalvageData::new));

    public static final PrimitiveCodec<UUID> UUID_CODEC = new PrimitiveCodec<UUID>() {

        public <T> DataResult<UUID> read(DynamicOps<T> ops, T input) {
            return DataResult.success(UUID.fromString((String) ops.getStringValue(input).getOrThrow(false, null)));
        }

        public <T> T write(DynamicOps<T> ops, UUID value) {
            return (T) ops.createString(value.toString());
        }

        public String toString() {
            return "uuid";
        }
    };

    public static final PrimitiveCodec<BlockPos> BLOCKPOS_CODEC = new PrimitiveCodec<BlockPos>() {

        public <T> DataResult<BlockPos> read(DynamicOps<T> ops, T input) {
            return DataResult.success(BlockPos.of((Long) ops.getStringValue(input).map(Long::valueOf).getOrThrow(false, null)));
        }

        public <T> T write(DynamicOps<T> ops, BlockPos value) {
            return (T) ops.createString(String.valueOf(value.asLong()));
        }

        public String toString() {
            return "blockpos";
        }
    };

    public static final PrimitiveCodec<ChunkPos> CHUNKPOS_CODEC = new PrimitiveCodec<ChunkPos>() {

        public <T> DataResult<ChunkPos> read(DynamicOps<T> ops, T input) {
            return DataResult.success(new ChunkPos((Long) ops.getNumberValue(input).map(Number::longValue).getOrThrow(false, null)));
        }

        public <T> T write(DynamicOps<T> ops, ChunkPos value) {
            return (T) ops.createLong(value.toLong());
        }

        public String toString() {
            return "chunkpos";
        }
    };

    public static record SalvageData(Map<String, Double> chancePerLevel, Map<String, Integer> levelReq, Map<String, Long> xpAward, int salvageMax, double baseChance, double maxChance) {

        public static CodecTypes.SalvageData combine(CodecTypes.SalvageData one, CodecTypes.SalvageData two, boolean oneOverride, boolean twoOverride) {
            Map<String, Double> chancePerLevel = new HashMap();
            Map<String, Integer> levelReq = new HashMap();
            Map<String, Long> xpAward = new HashMap();
            AtomicInteger salvageMax = new AtomicInteger(0);
            AtomicDouble baseChance = new AtomicDouble(0.0);
            AtomicDouble maxChance = new AtomicDouble(0.0);
            BiConsumer<CodecTypes.SalvageData, CodecTypes.SalvageData> bothOrNeither = (o, t) -> {
                chancePerLevel.putAll(o.chancePerLevel());
                t.chancePerLevel().forEach((skill, mod) -> chancePerLevel.merge(skill, mod, (o1, n1) -> o1 > n1 ? o1 : n1));
                levelReq.putAll(o.levelReq());
                t.levelReq().forEach((skill, level) -> levelReq.merge(skill, level, (o1, n1) -> o1 > n1 ? o1 : n1));
                xpAward.putAll(o.xpAward());
                t.xpAward().forEach((skill, xp) -> xpAward.merge(skill, xp, (o1, n1) -> o1 > n1 ? o1 : n1));
                salvageMax.set(o.salvageMax() > t.salvageMax() ? o.salvageMax() : t.salvageMax());
                baseChance.set(o.baseChance() > t.baseChance() ? o.baseChance() : t.baseChance());
                maxChance.set(o.maxChance() > t.maxChance() ? o.maxChance() : t.maxChance());
            };
            Functions.biPermutation(one, two, oneOverride, twoOverride, (o, t) -> {
                chancePerLevel.putAll(o.chancePerLevel().isEmpty() ? t.chancePerLevel() : o.chancePerLevel());
                levelReq.putAll(o.levelReq().isEmpty() ? t.levelReq() : o.levelReq());
                xpAward.putAll(o.xpAward().isEmpty() ? t.xpAward() : o.xpAward());
                salvageMax.set(t.salvageMax());
                baseChance.set(t.baseChance());
                maxChance.set(t.maxChance());
            }, bothOrNeither, bothOrNeither);
            return new CodecTypes.SalvageData(chancePerLevel, levelReq, xpAward, salvageMax.get(), baseChance.get(), maxChance.get());
        }
    }
}