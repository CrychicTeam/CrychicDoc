package net.minecraftforge.registries.holdersets;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class NotHolderSet<T> implements ICustomHolderSet<T> {

    private final List<Runnable> owners = new ArrayList();

    private final HolderLookup.RegistryLookup<T> registryLookup;

    private final HolderSet<T> value;

    @Nullable
    private List<Holder<T>> list = null;

    public static <T> Codec<? extends ICustomHolderSet<T>> codec(ResourceKey<? extends Registry<T>> registryKey, Codec<Holder<T>> holderCodec, boolean forceList) {
        return RecordCodecBuilder.create(builder -> builder.group(RegistryOps.retrieveRegistryLookup(registryKey).forGetter(NotHolderSet::registryLookup), HolderSetCodec.create(registryKey, holderCodec, forceList).fieldOf("value").forGetter(NotHolderSet::value)).apply(builder, NotHolderSet::new));
    }

    public HolderLookup.RegistryLookup<T> registryLookup() {
        return this.registryLookup;
    }

    public HolderSet<T> value() {
        return this.value;
    }

    public NotHolderSet(HolderLookup.RegistryLookup<T> registryLookup, HolderSet<T> value) {
        this.registryLookup = registryLookup;
        this.value = value;
        this.value.addInvalidationListener(this::invalidate);
    }

    @Override
    public HolderSetType type() {
        return ForgeMod.NOT_HOLDER_SET.get();
    }

    public void addInvalidationListener(Runnable runnable) {
        this.owners.add(runnable);
    }

    public Iterator<Holder<T>> iterator() {
        return this.getList().iterator();
    }

    @Override
    public Stream<Holder<T>> stream() {
        return this.getList().stream();
    }

    @Override
    public int size() {
        return this.getList().size();
    }

    @Override
    public Either<TagKey<T>, List<Holder<T>>> unwrap() {
        return Either.right(this.getList());
    }

    @Override
    public Optional<Holder<T>> getRandomElement(RandomSource random) {
        List<Holder<T>> list = this.getList();
        int size = list.size();
        return size > 0 ? Optional.of((Holder) list.get(random.nextInt(size))) : Optional.empty();
    }

    @Override
    public Holder<T> get(int i) {
        return (Holder<T>) this.getList().get(i);
    }

    @Override
    public boolean contains(Holder<T> holder) {
        return !this.value.contains(holder);
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> holderOwner) {
        return this.registryLookup.m_254921_(holderOwner);
    }

    @Override
    public Optional<TagKey<T>> unwrapKey() {
        return Optional.empty();
    }

    public String toString() {
        return "NotSet(" + this.value + ")";
    }

    private List<Holder<T>> getList() {
        List<Holder<T>> thisList = this.list;
        if (thisList == null) {
            List<Holder<T>> list = this.registryLookup.m_214062_().filter(holder -> !this.value.contains(holder)).map(holder -> holder).toList();
            this.list = list;
            return list;
        } else {
            return thisList;
        }
    }

    private void invalidate() {
        this.list = null;
        for (Runnable runnable : this.owners) {
            runnable.run();
        }
    }
}