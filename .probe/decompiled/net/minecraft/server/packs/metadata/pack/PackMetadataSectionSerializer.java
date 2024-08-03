package net.minecraft.server.packs.metadata.pack;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.util.GsonHelper;

public class PackMetadataSectionSerializer implements MetadataSectionType<PackMetadataSection> {

    public PackMetadataSection fromJson(JsonObject jsonObject0) {
        Component $$1 = Component.Serializer.fromJson(jsonObject0.get("description"));
        if ($$1 == null) {
            throw new JsonParseException("Invalid/missing description!");
        } else {
            int $$2 = GsonHelper.getAsInt(jsonObject0, "pack_format");
            return new PackMetadataSection($$1, $$2);
        }
    }

    public JsonObject toJson(PackMetadataSection packMetadataSection0) {
        JsonObject $$1 = new JsonObject();
        $$1.add("description", Component.Serializer.toJsonTree(packMetadataSection0.getDescription()));
        $$1.addProperty("pack_format", packMetadataSection0.getPackFormat());
        return $$1;
    }

    @Override
    public String getMetadataSectionName() {
        return "pack";
    }
}