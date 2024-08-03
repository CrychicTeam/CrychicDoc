package net.minecraft.client.resources.metadata.animation;

import com.google.gson.JsonObject;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.util.GsonHelper;

public class VillagerMetadataSectionSerializer implements MetadataSectionSerializer<VillagerMetaDataSection> {

    public VillagerMetaDataSection fromJson(JsonObject jsonObject0) {
        return new VillagerMetaDataSection(VillagerMetaDataSection.Hat.getByName(GsonHelper.getAsString(jsonObject0, "hat", "none")));
    }

    @Override
    public String getMetadataSectionName() {
        return "villager";
    }
}