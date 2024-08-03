package com.rekindled.embers.render;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

public class PipeModel implements IUnbakedGeometry<PipeModel> {

    BlockModel centerModel;

    BlockModel connectionModel;

    BlockModel connectionModel2;

    BlockModel endModel;

    BlockModel endModel2;

    public PipeModel(BlockModel centerModel, BlockModel connectionModel, BlockModel connectionModel2, BlockModel endModel, BlockModel endModel2) {
        this.centerModel = centerModel;
        this.connectionModel = connectionModel;
        this.connectionModel2 = connectionModel2;
        this.endModel = endModel;
        this.endModel2 = endModel2;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        BakedModel[] connectionModels = getRotatedModels(context, baker, spriteGetter, modelLocation, this.connectionModel, this.connectionModel2);
        BakedModel[] endModels = getRotatedModels(context, baker, spriteGetter, modelLocation, this.endModel, this.endModel2);
        return new BakedPipeModel(this.centerModel.bake(baker, this.centerModel, spriteGetter, modelState, modelLocation, context.useBlockLight()), connectionModels, endModels);
    }

    public static BakedModel[] getRotatedModels(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ResourceLocation modelLocation, BlockModel model, BlockModel model2) {
        return new BakedModel[] { model.bake(baker, model, spriteGetter, BlockModelRotation.X0_Y0, modelLocation, context.useBlockLight()), model2.bake(baker, model2, spriteGetter, BlockModelRotation.X180_Y0, modelLocation, context.useBlockLight()), model.bake(baker, model, spriteGetter, BlockModelRotation.X90_Y180, modelLocation, context.useBlockLight()), model2.bake(baker, model2, spriteGetter, BlockModelRotation.X90_Y0, modelLocation, context.useBlockLight()), model.bake(baker, model, spriteGetter, BlockModelRotation.X90_Y90, modelLocation, context.useBlockLight()), model2.bake(baker, model2, spriteGetter, BlockModelRotation.X90_Y270, modelLocation, context.useBlockLight()) };
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
        this.centerModel.resolveParents(modelGetter);
        this.connectionModel.resolveParents(modelGetter);
        this.connectionModel2.resolveParents(modelGetter);
        this.endModel.resolveParents(modelGetter);
        this.endModel2.resolveParents(modelGetter);
    }

    public static final class Loader implements IGeometryLoader<PipeModel> {

        public static final PipeModel.Loader INSTANCE = new PipeModel.Loader();

        public PipeModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
            BlockModel centerModel = (BlockModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "center"), BlockModel.class);
            BlockModel connectionModel = (BlockModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "connection"), BlockModel.class);
            BlockModel connectionModel2 = (BlockModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "connection2"), BlockModel.class);
            BlockModel endModel = (BlockModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "end"), BlockModel.class);
            BlockModel endModel2 = (BlockModel) deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "end2"), BlockModel.class);
            return new PipeModel(centerModel, connectionModel, connectionModel2, endModel, endModel2);
        }
    }
}