package fuzs.puzzleslib.api.init.v3.tags;

import com.google.common.collect.Maps;
import java.util.Map;
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

public final class BoundTagFactory {

    private static final Map<String, BoundTagFactory> VALUES = Maps.newConcurrentMap();

    public static final BoundTagFactory MINECRAFT = make("minecraft");

    public static final BoundTagFactory COMMON = make("c");

    public static final BoundTagFactory FABRIC = make("fabric");

    public static final BoundTagFactory FORGE = make("forge");

    public static final BoundTagFactory CURIOS = make("curios");

    public static final BoundTagFactory TRINKETS = make("trinkets");

    private final String namespace;

    private BoundTagFactory(String namespace) {
        this.namespace = namespace;
    }

    public static BoundTagFactory make(String namespace) {
        return (BoundTagFactory) VALUES.computeIfAbsent(namespace, BoundTagFactory::new);
    }

    public <T> TagKey<T> registerTagKey(ResourceKey<? extends Registry<T>> registryKey, String path) {
        return TagKey.create(registryKey, new ResourceLocation(this.namespace, path));
    }

    public TagKey<Block> registerBlockTag(String path) {
        return this.registerTagKey(Registries.BLOCK, path);
    }

    public TagKey<Item> registerItemTag(String path) {
        return this.registerTagKey(Registries.ITEM, path);
    }

    public TagKey<Fluid> registerFluidTag(String path) {
        return this.registerTagKey(Registries.FLUID, path);
    }

    public TagKey<EntityType<?>> registerEntityTypeTag(String path) {
        return this.registerTagKey(Registries.ENTITY_TYPE, path);
    }

    public TagKey<Enchantment> registerEnchantmentTag(String path) {
        return this.registerTagKey(Registries.ENCHANTMENT, path);
    }

    public TagKey<Biome> registerBiomeTag(String path) {
        return this.registerTagKey(Registries.BIOME, path);
    }

    public TagKey<GameEvent> registerGameEventTag(String path) {
        return this.registerTagKey(Registries.GAME_EVENT, path);
    }

    public TagKey<DamageType> registerDamageTypeTag(String path) {
        return this.registerTagKey(Registries.DAMAGE_TYPE, path);
    }
}