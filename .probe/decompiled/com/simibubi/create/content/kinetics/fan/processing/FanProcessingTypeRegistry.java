package com.simibubi.create.content.kinetics.fan.processing;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class FanProcessingTypeRegistry {

    private static final Map<ResourceLocation, FanProcessingType> TYPES = new Object2ReferenceOpenHashMap();

    private static final Map<FanProcessingType, ResourceLocation> IDS = new Reference2ObjectOpenHashMap();

    private static final List<FanProcessingType> SORTED_TYPES = new ReferenceArrayList();

    private static final List<FanProcessingType> SORTED_TYPES_VIEW = Collections.unmodifiableList(SORTED_TYPES);

    public static void register(ResourceLocation id, FanProcessingType type) {
        if (TYPES.put(id, type) != null) {
            throw new IllegalArgumentException("Tried to override FanProcessingType registration for id '" + id + "'. This is not supported!");
        } else {
            ResourceLocation prevId = (ResourceLocation) IDS.put(type, id);
            if (prevId != null) {
                throw new IllegalArgumentException("Tried to register same FanProcessingType instance for multiple ids '" + prevId + "' and '" + id + "'. This is not supported!");
            } else {
                insertSortedType(type, id);
            }
        }
    }

    private static void insertSortedType(FanProcessingType type, ResourceLocation id) {
        int index = Collections.binarySearch(SORTED_TYPES, type, (type1, type2) -> type2.getPriority() - type1.getPriority());
        if (index >= 0) {
            throw new IllegalStateException();
        } else {
            SORTED_TYPES.add(-index - 1, type);
        }
    }

    @Nullable
    public static FanProcessingType getType(ResourceLocation id) {
        return (FanProcessingType) TYPES.get(id);
    }

    public static FanProcessingType getTypeOrThrow(ResourceLocation id) {
        FanProcessingType type = getType(id);
        if (type == null) {
            throw new IllegalArgumentException("Could not get FanProcessingType for id '" + id + "'!");
        } else {
            return type;
        }
    }

    @Nullable
    public static ResourceLocation getId(FanProcessingType type) {
        return (ResourceLocation) IDS.get(type);
    }

    public static ResourceLocation getIdOrThrow(FanProcessingType type) {
        ResourceLocation id = getId(type);
        if (id == null) {
            throw new IllegalArgumentException("Could not get id for FanProcessingType " + type + "!");
        } else {
            return id;
        }
    }

    public static List<FanProcessingType> getSortedTypesView() {
        return SORTED_TYPES_VIEW;
    }
}