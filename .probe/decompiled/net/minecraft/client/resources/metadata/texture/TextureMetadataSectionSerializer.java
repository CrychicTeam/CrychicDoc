package net.minecraft.client.resources.metadata.texture;

import com.google.gson.JsonObject;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.util.GsonHelper;

public class TextureMetadataSectionSerializer implements MetadataSectionSerializer<TextureMetadataSection> {

    public TextureMetadataSection fromJson(JsonObject jsonObject0) {
        boolean $$1 = GsonHelper.getAsBoolean(jsonObject0, "blur", false);
        boolean $$2 = GsonHelper.getAsBoolean(jsonObject0, "clamp", false);
        return new TextureMetadataSection($$1, $$2);
    }

    @Override
    public String getMetadataSectionName() {
        return "texture";
    }
}