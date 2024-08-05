package net.minecraft.client.particle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class ParticleDescription {

    private final List<ResourceLocation> textures;

    private ParticleDescription(List<ResourceLocation> listResourceLocation0) {
        this.textures = listResourceLocation0;
    }

    public List<ResourceLocation> getTextures() {
        return this.textures;
    }

    public static ParticleDescription fromJson(JsonObject jsonObject0) {
        JsonArray $$1 = GsonHelper.getAsJsonArray(jsonObject0, "textures", null);
        if ($$1 == null) {
            return new ParticleDescription(List.of());
        } else {
            List<ResourceLocation> $$2 = (List<ResourceLocation>) Streams.stream($$1).map(p_107284_ -> GsonHelper.convertToString(p_107284_, "texture")).map(ResourceLocation::new).collect(ImmutableList.toImmutableList());
            return new ParticleDescription($$2);
        }
    }
}