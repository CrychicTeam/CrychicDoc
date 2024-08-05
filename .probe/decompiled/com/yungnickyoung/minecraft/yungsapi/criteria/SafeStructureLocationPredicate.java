package com.yungnickyoung.minecraft.yungsapi.criteria;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.levelgen.structure.Structure;

public class SafeStructureLocationPredicate {

    private final ResourceKey<Structure> configuredStructureFeature;

    public SafeStructureLocationPredicate(ResourceKey<Structure> configuredStructureFeature) {
        this.configuredStructureFeature = configuredStructureFeature;
    }

    public boolean matches(ServerLevel serverLevel, double x, double y, double z) {
        return this.matches(serverLevel, (float) x, (float) y, (float) z);
    }

    public boolean matches(ServerLevel serverLevel, float x, float y, float z) {
        BlockPos blockpos = new BlockPos((int) x, (int) y, (int) z);
        return this.configuredStructureFeature != null && serverLevel.m_46749_(blockpos) && serverLevel.structureManager().getStructureWithPieceAt(blockpos, this.configuredStructureFeature).isValid();
    }

    public JsonElement serializeToJson() {
        JsonObject jsonObject = new JsonObject();
        if (this.configuredStructureFeature != null) {
            jsonObject.addProperty("feature", this.configuredStructureFeature.location().toString());
        }
        return jsonObject;
    }

    public static SafeStructureLocationPredicate fromJson(JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "location");
            ResourceKey<Structure> featureResourceKey = jsonObject.has("feature") ? (ResourceKey) ResourceLocation.CODEC.parse(JsonOps.INSTANCE, jsonObject.get("feature")).resultOrPartial(YungsApiCommon.LOGGER::error).map(resourceLocation -> ResourceKey.create(Registries.STRUCTURE, resourceLocation)).orElse(null) : null;
            return new SafeStructureLocationPredicate(featureResourceKey);
        } else {
            return new SafeStructureLocationPredicate(null);
        }
    }
}