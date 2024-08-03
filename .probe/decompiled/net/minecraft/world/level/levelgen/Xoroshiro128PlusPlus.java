package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import java.util.stream.LongStream;
import net.minecraft.Util;

public class Xoroshiro128PlusPlus {

    private long seedLo;

    private long seedHi;

    public static final Codec<Xoroshiro128PlusPlus> CODEC = Codec.LONG_STREAM.comapFlatMap(p_287733_ -> Util.fixedSize(p_287733_, 2).map(p_287742_ -> new Xoroshiro128PlusPlus(p_287742_[0], p_287742_[1])), p_287687_ -> LongStream.of(new long[] { p_287687_.seedLo, p_287687_.seedHi }));

    public Xoroshiro128PlusPlus(RandomSupport.Seed128bit randomSupportSeedBit0) {
        this(randomSupportSeedBit0.seedLo(), randomSupportSeedBit0.seedHi());
    }

    public Xoroshiro128PlusPlus(long long0, long long1) {
        this.seedLo = long0;
        this.seedHi = long1;
        if ((this.seedLo | this.seedHi) == 0L) {
            this.seedLo = -7046029254386353131L;
            this.seedHi = 7640891576956012809L;
        }
    }

    public long nextLong() {
        long $$0 = this.seedLo;
        long $$1 = this.seedHi;
        long $$2 = Long.rotateLeft($$0 + $$1, 17) + $$0;
        $$1 ^= $$0;
        this.seedLo = Long.rotateLeft($$0, 49) ^ $$1 ^ $$1 << 21;
        this.seedHi = Long.rotateLeft($$1, 28);
        return $$2;
    }
}