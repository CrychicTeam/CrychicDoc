package com.craisinlord.integrated_api.world.structures.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;

public interface StructureActionType<C extends StructureAction> {

    Map<ResourceLocation, StructureActionType<?>> ACTION_TYPES_BY_NAME = new HashMap();

    Map<StructureActionType<?>, ResourceLocation> NAME_BY_ACTION_TYPES = new HashMap();

    Codec<StructureActionType<?>> ACTION_TYPE_CODEC = ResourceLocation.CODEC.flatXmap(resourceLocation -> (DataResult) Optional.ofNullable((StructureActionType) ACTION_TYPES_BY_NAME.get(resourceLocation)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown structure action type: " + resourceLocation)), actionType -> (DataResult) Optional.of((ResourceLocation) NAME_BY_ACTION_TYPES.get(actionType)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "No ID found for structure action type " + actionType + ". Is it registered?")));

    Codec<StructureAction> ACTION_CODEC = ACTION_TYPE_CODEC.dispatch("type", StructureAction::type, StructureActionType::codec);

    StructureActionType<TransformAction> TRANSFORM = register("transform", TransformAction.CODEC);

    StructureActionType<DelayGenerationAction> DELAY_GENERATION = register("delay_generation", DelayGenerationAction.CODEC);

    static <C extends StructureAction> StructureActionType<C> register(ResourceLocation resourceLocation, Codec<C> codec) {
        StructureActionType<C> actionType = () -> codec;
        ACTION_TYPES_BY_NAME.put(resourceLocation, actionType);
        NAME_BY_ACTION_TYPES.put(actionType, resourceLocation);
        return actionType;
    }

    private static <C extends StructureAction> StructureActionType<C> register(String id, Codec<C> codec) {
        return register(new ResourceLocation("integrated_api", id), codec);
    }

    Codec<C> codec();
}