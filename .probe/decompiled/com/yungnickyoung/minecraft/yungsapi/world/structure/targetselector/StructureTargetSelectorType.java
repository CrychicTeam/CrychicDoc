package com.yungnickyoung.minecraft.yungsapi.world.structure.targetselector;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;

public interface StructureTargetSelectorType<C extends StructureTargetSelector> {

    Map<ResourceLocation, StructureTargetSelectorType<?>> TARGET_SELECTOR_TYPES_BY_NAME = new HashMap();

    Map<StructureTargetSelectorType<?>, ResourceLocation> NAME_BY_TARGET_SELECTOR_TYPES = new HashMap();

    Codec<StructureTargetSelectorType<?>> TARGET_SELECTOR_TYPE_CODEC = ResourceLocation.CODEC.flatXmap(resourceLocation -> (DataResult) Optional.ofNullable((StructureTargetSelectorType) TARGET_SELECTOR_TYPES_BY_NAME.get(resourceLocation)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown target selector type: " + resourceLocation)), targetSelectorType -> (DataResult) Optional.of((ResourceLocation) NAME_BY_TARGET_SELECTOR_TYPES.get(targetSelectorType)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "No ID found for target selector type " + targetSelectorType + ". Is it registered?")));

    Codec<StructureTargetSelector> TARGET_SELECTOR_CODEC = TARGET_SELECTOR_TYPE_CODEC.dispatch("type", StructureTargetSelector::type, StructureTargetSelectorType::codec);

    StructureTargetSelectorType<SelfTargetSelector> SELF = register("self", SelfTargetSelector.CODEC);

    static <C extends StructureTargetSelector> StructureTargetSelectorType<C> register(ResourceLocation resourceLocation, Codec<C> codec) {
        StructureTargetSelectorType<C> targetSelectorType = () -> codec;
        TARGET_SELECTOR_TYPES_BY_NAME.put(resourceLocation, targetSelectorType);
        NAME_BY_TARGET_SELECTOR_TYPES.put(targetSelectorType, resourceLocation);
        return targetSelectorType;
    }

    private static <C extends StructureTargetSelector> StructureTargetSelectorType<C> register(String id, Codec<C> codec) {
        return register(new ResourceLocation("yungsapi", id), codec);
    }

    Codec<C> codec();
}