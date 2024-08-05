package fuzs.puzzleslib.api.init.v3.tags;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

public final class TypedTagFactory<T> {

    private static final Map<ResourceKey<Registry<?>>, TypedTagFactory<?>> VALUES = Maps.newConcurrentMap();

    public static final TypedTagFactory<Block> BLOCK = make(Registries.BLOCK);

    public static final TypedTagFactory<Item> ITEM = make(Registries.ITEM);

    public static final TypedTagFactory<Fluid> FLUID = make(Registries.FLUID);

    public static final TypedTagFactory<EntityType<?>> ENTITY_TYPE = make(Registries.ENTITY_TYPE);

    public static final TypedTagFactory<Enchantment> ENCHANTMENT = make(Registries.ENCHANTMENT);

    public static final TypedTagFactory<Biome> BIOME = make(Registries.BIOME);

    public static final TypedTagFactory<GameEvent> GAME_EVENT = make(Registries.GAME_EVENT);

    public static final TypedTagFactory<DamageType> DAMAGE_TYPE = make(Registries.DAMAGE_TYPE);

    private final ResourceKey<Registry<T>> registryKey;

    private TypedTagFactory(ResourceKey<Registry<T>> registryKey) {
        this.registryKey = registryKey;
    }

    public static <T> TypedTagFactory<T> make(ResourceKey<Registry<T>> registryKey) {
        return (TypedTagFactory<T>) VALUES.computeIfAbsent(registryKey, TypedTagFactory::new);
    }

    public TagKey<T> make(String namespace, String path) {
        Objects.requireNonNull(namespace, "namespace is null");
        Objects.requireNonNull(path, "path is null");
        return this.make(new ResourceLocation(namespace, path));
    }

    public TagKey<T> make(ResourceLocation resourceLocation) {
        Objects.requireNonNull(resourceLocation, "resource location is null");
        return TagKey.create(this.registryKey, resourceLocation);
    }

    public TagKey<T> minecraft(String path) {
        return this.make("minecraft", path);
    }

    public TagKey<T> common(String path) {
        return this.make("c", path);
    }

    public TagKey<T> fabric(String path) {
        return this.make("fabric", path);
    }

    public TagKey<T> forge(String path) {
        return this.make("forge", path);
    }

    public TagKey<T> curios(String path) {
        return this.make("curios", path);
    }

    public TagKey<T> trinkets(String path) {
        return this.make("trinkets", path);
    }
}