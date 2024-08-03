package net.mehvahdjukaar.moonlight.api.map;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.api.map.type.CustomDecorationType;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.api.misc.TriFunction;
import net.mehvahdjukaar.moonlight.core.map.MapDataInternal;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

public class MapDataRegistry {

    public static final ResourceKey<Registry<MapDecorationType<?, ?>>> REGISTRY_KEY = MapDataInternal.KEY;

    public static <T extends CustomMapData<?>> CustomMapData.Type<T> registerCustomMapSavedData(CustomMapData.Type<T> type) {
        return MapDataInternal.registerCustomMapSavedData(type);
    }

    public static <T extends CustomMapData<?>> CustomMapData.Type<T> registerCustomMapSavedData(ResourceLocation id, Supplier<T> factory) {
        return registerCustomMapSavedData(new CustomMapData.Type<>(id, factory));
    }

    public static MapDecorationType<?, ?> getDefaultType() {
        return MapDataInternal.getGenericStructure();
    }

    @Deprecated(forRemoval = true)
    public static <T extends CustomDecorationType<?, ?>> T registerCustomType(T decorationType) {
        MapDataInternal.registerCustomType(decorationType.getCustomFactoryID(), () -> decorationType);
        return decorationType;
    }

    public static void registerCustomType(ResourceLocation factoryId, Supplier<CustomDecorationType<?, ?>> decorationTypeFactory) {
        MapDataInternal.registerCustomType(factoryId, decorationTypeFactory);
    }

    public static void addDynamicClientMarkersEvent(BiFunction<Integer, MapItemSavedData, Set<MapBlockMarker<?>>> event) {
        MapDataInternal.addDynamicClientMarkersEvent(event);
    }

    public static void addDynamicServerMarkersEvent(TriFunction<Player, Integer, MapItemSavedData, Set<MapBlockMarker<?>>> event) {
        MapDataInternal.addDynamicServerMarkersEvent(event);
    }

    public static CustomDecorationType<?, ?> getCustomType(ResourceLocation resourceLocation) {
        return MapDataInternal.createCustomType(resourceLocation);
    }

    public static MapDecorationType<?, ?> getAssociatedType(Holder<Structure> structure) {
        return MapDataInternal.getAssociatedType(structure);
    }

    public static Registry<MapDecorationType<?, ?>> getRegistry(RegistryAccess registryAccess) {
        return MapDataInternal.getRegistry(registryAccess);
    }

    public static MapDecorationType<?, ?> get(ResourceLocation id) {
        return MapDataInternal.get(id);
    }

    public static Optional<MapDecorationType<?, ?>> getOptional(ResourceLocation id) {
        return MapDataInternal.getOptional(id);
    }

    @Nullable
    public static MapBlockMarker<?> readMarker(CompoundTag tag) {
        return MapDataInternal.readWorldMarker(tag);
    }
}