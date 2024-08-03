package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.DyeColor;

public class LlamaDecorLayer extends RenderLayer<Llama, LlamaModel<Llama>> {

    private static final ResourceLocation[] TEXTURE_LOCATION = new ResourceLocation[] { new ResourceLocation("textures/entity/llama/decor/white.png"), new ResourceLocation("textures/entity/llama/decor/orange.png"), new ResourceLocation("textures/entity/llama/decor/magenta.png"), new ResourceLocation("textures/entity/llama/decor/light_blue.png"), new ResourceLocation("textures/entity/llama/decor/yellow.png"), new ResourceLocation("textures/entity/llama/decor/lime.png"), new ResourceLocation("textures/entity/llama/decor/pink.png"), new ResourceLocation("textures/entity/llama/decor/gray.png"), new ResourceLocation("textures/entity/llama/decor/light_gray.png"), new ResourceLocation("textures/entity/llama/decor/cyan.png"), new ResourceLocation("textures/entity/llama/decor/purple.png"), new ResourceLocation("textures/entity/llama/decor/blue.png"), new ResourceLocation("textures/entity/llama/decor/brown.png"), new ResourceLocation("textures/entity/llama/decor/green.png"), new ResourceLocation("textures/entity/llama/decor/red.png"), new ResourceLocation("textures/entity/llama/decor/black.png") };

    private static final ResourceLocation TRADER_LLAMA = new ResourceLocation("textures/entity/llama/decor/trader_llama.png");

    private final LlamaModel<Llama> model;

    public LlamaDecorLayer(RenderLayerParent<Llama, LlamaModel<Llama>> renderLayerParentLlamaLlamaModelLlama0, EntityModelSet entityModelSet1) {
        super(renderLayerParentLlamaLlamaModelLlama0);
        this.model = new LlamaModel<>(entityModelSet1.bakeLayer(ModelLayers.LLAMA_DECOR));
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Llama llama3, float float4, float float5, float float6, float float7, float float8, float float9) {
        DyeColor $$10 = llama3.getSwag();
        ResourceLocation $$11;
        if ($$10 != null) {
            $$11 = TEXTURE_LOCATION[$$10.getId()];
        } else {
            if (!llama3.isTraderLlama()) {
                return;
            }
            $$11 = TRADER_LLAMA;
        }
        ((LlamaModel) this.m_117386_()).m_102624_(this.model);
        this.model.setupAnim(llama3, float4, float5, float7, float8, float9);
        VertexConsumer $$14 = multiBufferSource1.getBuffer(RenderType.entityCutoutNoCull($$11));
        this.model.renderToBuffer(poseStack0, $$14, int2, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}