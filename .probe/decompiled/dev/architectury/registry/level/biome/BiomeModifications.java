package dev.architectury.registry.level.biome;

import com.google.common.base.Predicates;
import dev.architectury.hooks.level.biome.BiomeProperties;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.level.biome.forge.BiomeModificationsImpl;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class BiomeModifications {

    public static void addProperties(BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        addProperties(Predicates.alwaysTrue(), modifier);
    }

    @ExpectPlatform
    @Transformed
    public static void addProperties(Predicate<BiomeModifications.BiomeContext> predicate, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        BiomeModificationsImpl.addProperties(predicate, modifier);
    }

    public static void postProcessProperties(BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        postProcessProperties(Predicates.alwaysTrue(), modifier);
    }

    @ExpectPlatform
    @Transformed
    public static void postProcessProperties(Predicate<BiomeModifications.BiomeContext> predicate, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        BiomeModificationsImpl.postProcessProperties(predicate, modifier);
    }

    public static void removeProperties(BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        removeProperties(Predicates.alwaysTrue(), modifier);
    }

    @ExpectPlatform
    @Transformed
    public static void removeProperties(Predicate<BiomeModifications.BiomeContext> predicate, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        BiomeModificationsImpl.removeProperties(predicate, modifier);
    }

    public static void replaceProperties(BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        replaceProperties(Predicates.alwaysTrue(), modifier);
    }

    @ExpectPlatform
    @Transformed
    public static void replaceProperties(Predicate<BiomeModifications.BiomeContext> predicate, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        BiomeModificationsImpl.replaceProperties(predicate, modifier);
    }

    public interface BiomeContext {

        Optional<ResourceLocation> getKey();

        BiomeProperties getProperties();

        boolean hasTag(TagKey<Biome> var1);
    }
}