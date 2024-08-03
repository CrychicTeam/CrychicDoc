package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Llama;

public class LlamaRenderer extends MobRenderer<Llama, LlamaModel<Llama>> {

    private static final ResourceLocation CREAMY = new ResourceLocation("textures/entity/llama/creamy.png");

    private static final ResourceLocation WHITE = new ResourceLocation("textures/entity/llama/white.png");

    private static final ResourceLocation BROWN = new ResourceLocation("textures/entity/llama/brown.png");

    private static final ResourceLocation GRAY = new ResourceLocation("textures/entity/llama/gray.png");

    public LlamaRenderer(EntityRendererProvider.Context entityRendererProviderContext0, ModelLayerLocation modelLayerLocation1) {
        super(entityRendererProviderContext0, new LlamaModel<>(entityRendererProviderContext0.bakeLayer(modelLayerLocation1)), 0.7F);
        this.m_115326_(new LlamaDecorLayer(this, entityRendererProviderContext0.getModelSet()));
    }

    public ResourceLocation getTextureLocation(Llama llama0) {
        return switch(llama0.getVariant()) {
            case CREAMY ->
                CREAMY;
            case WHITE ->
                WHITE;
            case BROWN ->
                BROWN;
            case GRAY ->
                GRAY;
        };
    }
}