package com.craisinlord.integrated_api.world.terrainadaptation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;

public interface EnhancedTerrainAdaptationType<C extends EnhancedTerrainAdaptation> {

    Map<ResourceLocation, EnhancedTerrainAdaptationType<?>> ADAPTATION_TYPES_BY_NAME = new HashMap();

    Map<EnhancedTerrainAdaptationType<?>, ResourceLocation> NAME_BY_ADAPTATION_TYPES = new HashMap();

    Codec<EnhancedTerrainAdaptationType<?>> ADAPTATION_TYPE_CODEC = ResourceLocation.CODEC.flatXmap(resourceLocation -> (DataResult) Optional.ofNullable((EnhancedTerrainAdaptationType) ADAPTATION_TYPES_BY_NAME.get(resourceLocation)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown enhanced terrain adaptation type: " + resourceLocation)), adaptationType -> (DataResult) Optional.of((ResourceLocation) NAME_BY_ADAPTATION_TYPES.get(adaptationType)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "No ID found for enhanced terrain adaptation type " + adaptationType + ". Is it registered?")));

    Codec<EnhancedTerrainAdaptation> ADAPTATION_CODEC = ADAPTATION_TYPE_CODEC.dispatch("type", EnhancedTerrainAdaptation::type, EnhancedTerrainAdaptationType::codec);

    EnhancedTerrainAdaptationType<NoneAdaptation> NONE = register("none", NoneAdaptation.CODEC);

    EnhancedTerrainAdaptationType<LargeCarvedTopNoBeardAdaptation> LARGE_CARVED_TOP_NO_BEARD = register("carved_top_no_beard_large", LargeCarvedTopNoBeardAdaptation.CODEC);

    EnhancedTerrainAdaptationType<SmallCarvedTopNoBeardAdaptation> SMALL_CARVED_TOP_NO_BEARD = register("carved_top_no_beard_small", SmallCarvedTopNoBeardAdaptation.CODEC);

    EnhancedTerrainAdaptationType<CustomAdaptation> CUSTOM = register("custom", CustomAdaptation.CODEC);

    static <C extends EnhancedTerrainAdaptation> EnhancedTerrainAdaptationType<C> register(ResourceLocation resourceLocation, Codec<C> codec) {
        EnhancedTerrainAdaptationType<C> adaptationType = () -> codec;
        ADAPTATION_TYPES_BY_NAME.put(resourceLocation, adaptationType);
        NAME_BY_ADAPTATION_TYPES.put(adaptationType, resourceLocation);
        return adaptationType;
    }

    private static <C extends EnhancedTerrainAdaptation> EnhancedTerrainAdaptationType<C> register(String id, Codec<C> codec) {
        return register(new ResourceLocation("integrated_api", id), codec);
    }

    Codec<C> codec();
}