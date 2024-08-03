package net.minecraftforge.registries.tags;

import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;

public interface ITag<V> extends Iterable<V> {

    TagKey<V> getKey();

    Stream<V> stream();

    boolean isEmpty();

    int size();

    boolean contains(V var1);

    Optional<V> getRandomElement(RandomSource var1);

    boolean isBound();
}