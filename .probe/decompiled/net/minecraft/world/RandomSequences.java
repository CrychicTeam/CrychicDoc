package net.minecraft.world;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;

public class RandomSequences extends SavedData {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final long seed;

    private final Map<ResourceLocation, RandomSequence> sequences = new Object2ObjectOpenHashMap();

    public RandomSequences(long long0) {
        this.seed = long0;
    }

    public RandomSource get(ResourceLocation resourceLocation0) {
        final RandomSource $$1 = ((RandomSequence) this.sequences.computeIfAbsent(resourceLocation0, p_287666_ -> new RandomSequence(this.seed, p_287666_))).random();
        return new RandomSource() {

            @Override
            public RandomSource fork() {
                RandomSequences.this.m_77762_();
                return $$1.fork();
            }

            @Override
            public PositionalRandomFactory forkPositional() {
                RandomSequences.this.m_77762_();
                return $$1.forkPositional();
            }

            @Override
            public void setSeed(long p_287659_) {
                RandomSequences.this.m_77762_();
                $$1.setSeed(p_287659_);
            }

            @Override
            public int nextInt() {
                RandomSequences.this.m_77762_();
                return $$1.nextInt();
            }

            @Override
            public int nextInt(int p_287717_) {
                RandomSequences.this.m_77762_();
                return $$1.nextInt(p_287717_);
            }

            @Override
            public long nextLong() {
                RandomSequences.this.m_77762_();
                return $$1.nextLong();
            }

            @Override
            public boolean nextBoolean() {
                RandomSequences.this.m_77762_();
                return $$1.nextBoolean();
            }

            @Override
            public float nextFloat() {
                RandomSequences.this.m_77762_();
                return $$1.nextFloat();
            }

            @Override
            public double nextDouble() {
                RandomSequences.this.m_77762_();
                return $$1.nextDouble();
            }

            @Override
            public double nextGaussian() {
                RandomSequences.this.m_77762_();
                return $$1.nextGaussian();
            }
        };
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag0) {
        this.sequences.forEach((p_287627_, p_287578_) -> compoundTag0.put(p_287627_.toString(), (Tag) RandomSequence.CODEC.encodeStart(NbtOps.INSTANCE, p_287578_).result().orElseThrow()));
        return compoundTag0;
    }

    public static RandomSequences load(long long0, CompoundTag compoundTag1) {
        RandomSequences $$2 = new RandomSequences(long0);
        for (String $$4 : compoundTag1.getAllKeys()) {
            try {
                RandomSequence $$5 = (RandomSequence) ((Pair) RandomSequence.CODEC.decode(NbtOps.INSTANCE, compoundTag1.get($$4)).result().get()).getFirst();
                $$2.sequences.put(new ResourceLocation($$4), $$5);
            } catch (Exception var8) {
                LOGGER.error("Failed to load random sequence {}", $$4, var8);
            }
        }
        return $$2;
    }
}