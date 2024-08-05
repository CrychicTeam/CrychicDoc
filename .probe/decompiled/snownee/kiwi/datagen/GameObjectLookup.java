package snownee.kiwi.datagen;

import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.KiwiModules;

public interface GameObjectLookup {

    static <T> Stream<T> all(ResourceKey<Registry<T>> type, String modId) {
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(type.location());
        return registry.stream().filter($ -> registry.getKey((T) $).getNamespace().equals(modId));
    }

    static <T> Stream<GameObjectLookup.OptionalEntry<T>> fromModules(ResourceKey<Registry<T>> type, String... ids) {
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(type.location());
        return Stream.of(ids).map(ResourceLocation::new).map(KiwiModules::get).mapMulti(($, consumer) -> {
            boolean optional = $.module.getClass().getDeclaredAnnotation(KiwiModule.Optional.class) != null;
            $.getRegistries(registry).stream().map($$ -> $$).map($$ -> new GameObjectLookup.OptionalEntry<>($$, optional)).forEach(consumer);
        });
    }

    public static record OptionalEntry<T>(T object, boolean optional) {
    }
}