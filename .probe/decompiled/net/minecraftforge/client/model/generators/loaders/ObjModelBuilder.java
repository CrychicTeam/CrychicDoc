package net.minecraftforge.client.model.generators.loaders;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ObjModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    private ResourceLocation modelLocation;

    private Boolean automaticCulling;

    private Boolean shadeQuads;

    private Boolean flipV;

    private Boolean emissiveAmbient;

    private ResourceLocation mtlOverride;

    public static <T extends ModelBuilder<T>> ObjModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
        return new ObjModelBuilder<>(parent, existingFileHelper);
    }

    protected ObjModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(new ResourceLocation("forge:obj"), parent, existingFileHelper);
    }

    public ObjModelBuilder<T> modelLocation(ResourceLocation modelLocation) {
        Preconditions.checkNotNull(modelLocation, "modelLocation must not be null");
        Preconditions.checkArgument(this.existingFileHelper.exists(modelLocation, PackType.CLIENT_RESOURCES), "OBJ Model %s does not exist in any known resource pack", modelLocation);
        this.modelLocation = modelLocation;
        return this;
    }

    public ObjModelBuilder<T> automaticCulling(boolean automaticCulling) {
        this.automaticCulling = automaticCulling;
        return this;
    }

    public ObjModelBuilder<T> shadeQuads(boolean shadeQuads) {
        this.shadeQuads = shadeQuads;
        return this;
    }

    public ObjModelBuilder<T> flipV(boolean flipV) {
        this.flipV = flipV;
        return this;
    }

    public ObjModelBuilder<T> emissiveAmbient(boolean ambientEmissive) {
        this.emissiveAmbient = ambientEmissive;
        return this;
    }

    public ObjModelBuilder<T> overrideMaterialLibrary(ResourceLocation mtlOverride) {
        Preconditions.checkNotNull(mtlOverride, "mtlOverride must not be null");
        Preconditions.checkArgument(this.existingFileHelper.exists(mtlOverride, PackType.CLIENT_RESOURCES), "OBJ Model %s does not exist in any known resource pack", mtlOverride);
        this.mtlOverride = mtlOverride;
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);
        Preconditions.checkNotNull(this.modelLocation, "modelLocation must not be null");
        json.addProperty("model", this.modelLocation.toString());
        if (this.automaticCulling != null) {
            json.addProperty("automatic_culling", this.automaticCulling);
        }
        if (this.shadeQuads != null) {
            json.addProperty("shade_quads", this.shadeQuads);
        }
        if (this.flipV != null) {
            json.addProperty("flip_v", this.flipV);
        }
        if (this.emissiveAmbient != null) {
            json.addProperty("emissive_ambient", this.emissiveAmbient);
        }
        if (this.mtlOverride != null) {
            json.addProperty("mtl_override", this.mtlOverride.toString());
        }
        return json;
    }
}