package net.mehvahdjukaar.moonlight.core.map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.map.CustomMapData;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.api.map.type.CustomDecorationType;
import net.mehvahdjukaar.moonlight.api.map.type.JsonDecorationType;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.api.misc.TriFunction;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.moonlight.core.map.forge.MapDataInternalImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class MapDataInternal {

    public static final Codec<MapDecorationType<?, ?>> CODEC = Codec.either(CustomDecorationType.CODEC, JsonDecorationType.CODEC).xmap(either -> (MapDecorationType) either.map(s -> s, c -> c), type -> {
        if (type == null) {
            Moonlight.LOGGER.error("map decoration type cant be null. how did this happen?");
        }
        return type instanceof CustomDecorationType<?, ?> c ? Either.left(c) : Either.right((JsonDecorationType) type);
    });

    public static final Codec<MapDecorationType<?, ?>> NETWORK_CODEC = Codec.either(CustomDecorationType.CODEC, JsonDecorationType.NETWORK_CODEC).xmap(either -> (MapDecorationType) either.map(s -> s, c -> c), type -> {
        if (type == null) {
            Moonlight.LOGGER.error("map decoration type cant be null. how did this happen?");
        }
        return type instanceof CustomDecorationType<?, ?> c ? Either.left(c) : Either.right((JsonDecorationType) type);
    });

    @Internal
    public static final Map<ResourceLocation, CustomMapData.Type<?>> CUSTOM_MAP_DATA_TYPES = new LinkedHashMap();

    public static final ResourceKey<Registry<MapDecorationType<?, ?>>> KEY = ResourceKey.createRegistryKey(Moonlight.res("map_markers"));

    public static final ResourceLocation GENERIC_STRUCTURE_ID = Moonlight.res("generic_structure");

    private static final BiMap<ResourceLocation, Supplier<CustomDecorationType<?, ?>>> CODE_TYPES_FACTORIES = HashBiMap.create();

    private static final List<TriFunction<Player, Integer, MapItemSavedData, Set<MapBlockMarker<?>>>> DYNAMIC_SERVER = new ArrayList();

    private static final List<BiFunction<Integer, MapItemSavedData, Set<MapBlockMarker<?>>>> DYNAMIC_CLIENT = new ArrayList();

    public static <T extends CustomMapData<?>> CustomMapData.Type<T> registerCustomMapSavedData(CustomMapData.Type<T> type) {
        if (CUSTOM_MAP_DATA_TYPES.containsKey(type.id())) {
            throw new IllegalArgumentException("Duplicate custom map data registration " + type.id());
        } else {
            CUSTOM_MAP_DATA_TYPES.put(type.id(), type);
            return type;
        }
    }

    public static MapDecorationType<?, ?> getGenericStructure() {
        return get(GENERIC_STRUCTURE_ID);
    }

    public static void registerCustomType(ResourceLocation id, Supplier<CustomDecorationType<?, ?>> decorationType) {
        CODE_TYPES_FACTORIES.put(id, decorationType);
    }

    public static CustomDecorationType<?, ?> createCustomType(ResourceLocation factoryID) {
        Supplier<CustomDecorationType<?, ?>> factory = (Supplier<CustomDecorationType<?, ?>>) Objects.requireNonNull((Supplier) CODE_TYPES_FACTORIES.get(factoryID), "No map decoration type with id: " + factoryID);
        CustomDecorationType<?, ? extends MapBlockMarker<?>> t = (CustomDecorationType<?, ? extends MapBlockMarker<?>>) factory.get();
        t.factoryId = factoryID;
        return t;
    }

    public static MapDecorationType<?, ?> getAssociatedType(Holder<Structure> structure) {
        for (MapDecorationType<?, ?> v : getValues()) {
            Optional<HolderSet<Structure>> associatedStructure = v.getAssociatedStructure();
            if (associatedStructure.isPresent() && ((HolderSet) associatedStructure.get()).contains(structure)) {
                return v;
            }
        }
        return getGenericStructure();
    }

    @Internal
    @ExpectPlatform
    @Transformed
    public static void init() {
        MapDataInternalImpl.init();
    }

    public static Registry<MapDecorationType<?, ?>> hackyGetRegistry() {
        return Utils.hackyGetRegistryAccess().registryOrThrow(KEY);
    }

    public static Registry<MapDecorationType<?, ?>> getRegistry(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(KEY);
    }

    public static Collection<MapDecorationType<?, ?>> getValues() {
        return hackyGetRegistry().stream().toList();
    }

    public static Set<Entry<ResourceKey<MapDecorationType<?, ?>>, MapDecorationType<?, ?>>> getEntries() {
        return hackyGetRegistry().entrySet();
    }

    @Nullable
    public static MapDecorationType<? extends CustomMapDecoration, ?> get(String id) {
        return (MapDecorationType<? extends CustomMapDecoration, ?>) get(new ResourceLocation(id));
    }

    public static MapDecorationType<?, ?> get(ResourceLocation id) {
        Registry<MapDecorationType<?, ?>> reg = hackyGetRegistry();
        MapDecorationType<?, ? extends MapBlockMarker<?>> r = (MapDecorationType<?, ? extends MapBlockMarker<?>>) reg.get(id);
        return r == null ? reg.get(GENERIC_STRUCTURE_ID) : r;
    }

    public static Optional<MapDecorationType<?, ?>> getOptional(ResourceLocation id) {
        return hackyGetRegistry().getOptional(id);
    }

    public static Set<MapBlockMarker<?>> getDynamicServer(Player player, int mapId, MapItemSavedData data) {
        Set<MapBlockMarker<?>> dynamic = new HashSet();
        for (TriFunction<Player, Integer, MapItemSavedData, Set<MapBlockMarker<?>>> v : DYNAMIC_SERVER) {
            dynamic.addAll((Collection) v.apply(player, mapId, data));
        }
        return dynamic;
    }

    public static Set<MapBlockMarker<?>> getDynamicClient(int mapId, MapItemSavedData data) {
        Set<MapBlockMarker<?>> dynamic = new HashSet();
        for (BiFunction<Integer, MapItemSavedData, Set<MapBlockMarker<?>>> v : DYNAMIC_CLIENT) {
            dynamic.addAll((Collection) v.apply(mapId, data));
        }
        return dynamic;
    }

    @Nullable
    public static MapBlockMarker<?> readWorldMarker(CompoundTag compound) {
        Iterator var1 = compound.getAllKeys().iterator();
        if (var1.hasNext()) {
            String id = (String) var1.next();
            return get(new ResourceLocation(id)).loadMarkerFromNBT(compound.getCompound(id));
        } else {
            return null;
        }
    }

    public static List<MapBlockMarker<?>> getMarkersFromWorld(BlockGetter reader, BlockPos pos) {
        List<MapBlockMarker<?>> list = new ArrayList();
        for (MapDecorationType<?, ?> type : getValues()) {
            MapBlockMarker<?> c = type.getWorldMarkerFromWorld(reader, pos);
            if (c != null) {
                list.add(c);
            }
        }
        return list;
    }

    public static void addDynamicClientMarkersEvent(BiFunction<Integer, MapItemSavedData, Set<MapBlockMarker<?>>> event) {
        DYNAMIC_CLIENT.add(event);
    }

    public static void addDynamicServerMarkersEvent(TriFunction<Player, Integer, MapItemSavedData, Set<MapBlockMarker<?>>> event) {
        DYNAMIC_SERVER.add(event);
    }
}