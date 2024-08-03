package dev.xkmc.modulargolems.content.client.armor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public record GolemModelPath(ModelLayerLocation models, List<List<String>> paths) {

    public static final Map<ResourceLocation, GolemModelPath> MAP = new HashMap();

    public static GolemModelPath register(ResourceLocation id, GolemModelPath path) {
        MAP.put(id, path);
        return path;
    }

    public static GolemModelPath get(ResourceLocation id) {
        return (GolemModelPath) MAP.get(id);
    }

    static {
        register(GolemModelPaths.HELMETS, new GolemModelPath(GolemEquipmentModels.HELMET_LAYER, List.of(List.of("head", "helmet3"), List.of("head", "helmet17"), List.of("head", "helmet16"), List.of("head", "helmet15"), List.of("head", "helmet8"), List.of("head", "helmet9"), List.of("head", "helmet10"), List.of("head", "helmet11"), List.of("head", "helmet4"), List.of("head", "helmet6"), List.of("head", "helmet5"), List.of("head", "helmet7"), List.of("head", "helmet"))));
        register(GolemModelPaths.CHESTPLATES, new GolemModelPath(GolemEquipmentModels.CHESTPLATE_LAYER, List.of(List.of("body", "main_body"), List.of("right_arm", "main_rightarm2"), List.of("right_arm", "right_forearm", "main_rightforearm"), List.of("right_arm", "main_rightarm1"), List.of("left_arm", "left_forearm", "main_leftforearm"), List.of("left_arm", "main_leftarm1"), List.of("left_arm", "main_leftarm2"))));
        register(GolemModelPaths.LEGGINGS, new GolemModelPath(GolemEquipmentModels.SHINGUARD_LAYER, List.of(List.of("body", "main_shinguard"), List.of("right_leg", "shinguard1"), List.of("left_leg", "shinguard2"))));
    }
}