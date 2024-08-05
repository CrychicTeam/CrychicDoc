package net.minecraft.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;

public class RegistrySynchronization {

    private static final Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> NETWORKABLE_REGISTRIES = Util.make(() -> {
        Builder<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> $$0 = ImmutableMap.builder();
        put($$0, Registries.BIOME, Biome.NETWORK_CODEC);
        put($$0, Registries.CHAT_TYPE, ChatType.CODEC);
        put($$0, Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC);
        put($$0, Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC);
        put($$0, Registries.DIMENSION_TYPE, DimensionType.DIRECT_CODEC);
        put($$0, Registries.DAMAGE_TYPE, DamageType.CODEC);
        return $$0.build();
    });

    public static final Codec<RegistryAccess> NETWORK_CODEC = makeNetworkCodec();

    private static <E> void put(Builder<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> builderResourceKeyExtendsRegistryRegistrySynchronizationNetworkedRegistryData0, ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE1, Codec<E> codecE2) {
        builderResourceKeyExtendsRegistryRegistrySynchronizationNetworkedRegistryData0.put(resourceKeyExtendsRegistryE1, new RegistrySynchronization.NetworkedRegistryData<>(resourceKeyExtendsRegistryE1, codecE2));
    }

    private static Stream<RegistryAccess.RegistryEntry<?>> ownedNetworkableRegistries(RegistryAccess registryAccess0) {
        return registryAccess0.registries().filter(p_250129_ -> NETWORKABLE_REGISTRIES.containsKey(p_250129_.key()));
    }

    private static <E> DataResult<? extends Codec<E>> getNetworkCodec(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0) {
        return (DataResult<? extends Codec<E>>) Optional.ofNullable((RegistrySynchronization.NetworkedRegistryData) NETWORKABLE_REGISTRIES.get(resourceKeyExtendsRegistryE0)).map(p_250582_ -> p_250582_.networkCodec()).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown or not serializable registry: " + resourceKeyExtendsRegistryE0));
    }

    private static <E> Codec<RegistryAccess> makeNetworkCodec() {
        Codec<ResourceKey<? extends Registry<E>>> $$0 = ResourceLocation.CODEC.xmap(ResourceKey::m_135788_, ResourceKey::m_135782_);
        Codec<Registry<E>> $$1 = $$0.partialDispatch("type", p_258198_ -> DataResult.success(p_258198_.key()), p_250682_ -> getNetworkCodec(p_250682_).map(p_252116_ -> RegistryCodecs.networkCodec(p_250682_, Lifecycle.experimental(), p_252116_)));
        UnboundedMapCodec<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> $$2 = Codec.unboundedMap($$0, $$1);
        return captureMap($$2);
    }

    private static <K extends ResourceKey<? extends Registry<?>>, V extends Registry<?>> Codec<RegistryAccess> captureMap(UnboundedMapCodec<K, V> unboundedMapCodecKV0) {
        return unboundedMapCodecKV0.xmap(RegistryAccess.ImmutableRegistryAccess::new, p_251578_ -> (Map) ownedNetworkableRegistries(p_251578_).collect(ImmutableMap.toImmutableMap(p_250395_ -> p_250395_.key(), p_248951_ -> p_248951_.value())));
    }

    public static Stream<RegistryAccess.RegistryEntry<?>> networkedRegistries(LayeredRegistryAccess<RegistryLayer> layeredRegistryAccessRegistryLayer0) {
        return ownedNetworkableRegistries(layeredRegistryAccessRegistryLayer0.getAccessFrom(RegistryLayer.WORLDGEN));
    }

    public static Stream<RegistryAccess.RegistryEntry<?>> networkSafeRegistries(LayeredRegistryAccess<RegistryLayer> layeredRegistryAccessRegistryLayer0) {
        Stream<RegistryAccess.RegistryEntry<?>> $$1 = layeredRegistryAccessRegistryLayer0.getLayer(RegistryLayer.STATIC).m_206193_();
        Stream<RegistryAccess.RegistryEntry<?>> $$2 = networkedRegistries(layeredRegistryAccessRegistryLayer0);
        return Stream.concat($$2, $$1);
    }

    static record NetworkedRegistryData<E>(ResourceKey<? extends Registry<E>> f_244545_, Codec<E> f_244392_) {

        private final ResourceKey<? extends Registry<E>> key;

        private final Codec<E> networkCodec;

        NetworkedRegistryData(ResourceKey<? extends Registry<E>> f_244545_, Codec<E> f_244392_) {
            this.key = f_244545_;
            this.networkCodec = f_244392_;
        }
    }
}