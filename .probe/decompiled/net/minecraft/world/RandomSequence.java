package net.minecraft.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public class RandomSequence {

    public static final Codec<RandomSequence> CODEC = RecordCodecBuilder.create(p_287586_ -> p_287586_.group(XoroshiroRandomSource.CODEC.fieldOf("source").forGetter(p_287757_ -> p_287757_.source)).apply(p_287586_, RandomSequence::new));

    private final XoroshiroRandomSource source;

    public RandomSequence(XoroshiroRandomSource xoroshiroRandomSource0) {
        this.source = xoroshiroRandomSource0;
    }

    public RandomSequence(long long0, ResourceLocation resourceLocation1) {
        this(createSequence(long0, resourceLocation1));
    }

    private static XoroshiroRandomSource createSequence(long long0, ResourceLocation resourceLocation1) {
        return new XoroshiroRandomSource(RandomSupport.upgradeSeedTo128bitUnmixed(long0).xor(seedForKey(resourceLocation1)).mixed());
    }

    public static RandomSupport.Seed128bit seedForKey(ResourceLocation resourceLocation0) {
        return RandomSupport.seedFromHashOf(resourceLocation0.toString());
    }

    public RandomSource random() {
        return this.source;
    }
}