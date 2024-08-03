package net.minecraftforge.client.model.generators.loaders;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SeparateTransformsModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    private T base;

    private final Map<String, T> childModels = new LinkedHashMap();

    public static <T extends ModelBuilder<T>> SeparateTransformsModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
        return new SeparateTransformsModelBuilder<>(parent, existingFileHelper);
    }

    protected SeparateTransformsModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(new ResourceLocation("forge:separate_transforms"), parent, existingFileHelper);
    }

    public SeparateTransformsModelBuilder<T> base(T modelBuilder) {
        Preconditions.checkNotNull(modelBuilder, "modelBuilder must not be null");
        this.base = modelBuilder;
        return this;
    }

    public SeparateTransformsModelBuilder<T> perspective(ItemDisplayContext perspective, T modelBuilder) {
        Preconditions.checkNotNull(perspective, "layer must not be null");
        Preconditions.checkNotNull(modelBuilder, "modelBuilder must not be null");
        this.childModels.put(perspective.getSerializedName(), modelBuilder);
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);
        if (this.base != null) {
            json.add("base", this.base.toJson());
        }
        JsonObject parts = new JsonObject();
        for (Entry<String, T> entry : this.childModels.entrySet()) {
            parts.add((String) entry.getKey(), ((ModelBuilder) entry.getValue()).toJson());
        }
        json.add("perspectives", parts);
        return json;
    }
}