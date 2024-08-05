package net.minecraftforge.registries;

import com.mojang.serialization.Codec;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IForgeRegistry<V> extends Iterable<V> {

    ResourceKey<Registry<V>> getRegistryKey();

    ResourceLocation getRegistryName();

    void register(String var1, V var2);

    void register(ResourceLocation var1, V var2);

    boolean containsKey(ResourceLocation var1);

    boolean containsValue(V var1);

    boolean isEmpty();

    @Nullable
    V getValue(ResourceLocation var1);

    @Nullable
    ResourceLocation getKey(V var1);

    @Nullable
    ResourceLocation getDefaultKey();

    @NotNull
    Optional<ResourceKey<V>> getResourceKey(V var1);

    @NotNull
    Set<ResourceLocation> getKeys();

    @NotNull
    Collection<V> getValues();

    @NotNull
    Set<Entry<ResourceKey<V>, V>> getEntries();

    @NotNull
    Codec<V> getCodec();

    @NotNull
    Optional<Holder<V>> getHolder(ResourceKey<V> var1);

    @NotNull
    Optional<Holder<V>> getHolder(ResourceLocation var1);

    @NotNull
    Optional<Holder<V>> getHolder(V var1);

    @Nullable
    ITagManager<V> tags();

    @NotNull
    Optional<Holder.Reference<V>> getDelegate(ResourceKey<V> var1);

    @NotNull
    Holder.Reference<V> getDelegateOrThrow(ResourceKey<V> var1);

    @NotNull
    Optional<Holder.Reference<V>> getDelegate(ResourceLocation var1);

    @NotNull
    Holder.Reference<V> getDelegateOrThrow(ResourceLocation var1);

    @NotNull
    Optional<Holder.Reference<V>> getDelegate(V var1);

    @NotNull
    Holder.Reference<V> getDelegateOrThrow(V var1);

    <T> T getSlaveMap(ResourceLocation var1, Class<T> var2);

    @FunctionalInterface
    public interface AddCallback<V> {

        void onAdd(IForgeRegistryInternal<V> var1, RegistryManager var2, int var3, ResourceKey<V> var4, V var5, @Nullable V var6);
    }

    @FunctionalInterface
    public interface BakeCallback<V> {

        void onBake(IForgeRegistryInternal<V> var1, RegistryManager var2);
    }

    @FunctionalInterface
    public interface ClearCallback<V> {

        void onClear(IForgeRegistryInternal<V> var1, RegistryManager var2);
    }

    @FunctionalInterface
    public interface CreateCallback<V> {

        void onCreate(IForgeRegistryInternal<V> var1, RegistryManager var2);
    }

    @FunctionalInterface
    public interface MissingFactory<V> {

        V createMissing(ResourceLocation var1, boolean var2);
    }

    @FunctionalInterface
    public interface ValidateCallback<V> {

        void onValidate(IForgeRegistryInternal<V> var1, RegistryManager var2, int var3, ResourceLocation var4, V var5);
    }
}