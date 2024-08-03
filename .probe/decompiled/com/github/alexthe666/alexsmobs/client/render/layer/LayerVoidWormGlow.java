package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelVoidWormBody;
import com.github.alexthe666.alexsmobs.client.model.ModelVoidWormTail;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.client.render.misc.VoidWormMetadataSection;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWormPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.LivingEntity;

public abstract class LayerVoidWormGlow<T extends LivingEntity> extends RenderLayer<T, EntityModel<T>> {

    private final ResourceManager resourceManager;

    private final Object2BooleanMap<ResourceLocation> mcmetaData;

    private EntityModel<T> layerModel;

    private final EntityModel bodyModel = new ModelVoidWormBody(1.001F);

    private final EntityModel tailModel = new ModelVoidWormTail(1.001F);

    public LayerVoidWormGlow(RenderLayerParent<T, EntityModel<T>> renderer, ResourceManager resourceManager, EntityModel<T> layerModel) {
        super(renderer);
        this.resourceManager = resourceManager;
        this.mcmetaData = new Object2BooleanOpenHashMap();
        this.layerModel = layerModel;
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T worm, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ResourceLocation texture = this.getGlowTexture(worm);
        boolean special = this.isSpecialRenderer(texture);
        if (this.isGlowing(worm) || special) {
            if (special) {
                if (worm instanceof EntityVoidWormPart body) {
                    this.layerModel = body.isTail() ? this.tailModel : this.bodyModel;
                }
                this.layerModel.setupAnim(worm, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer consumer = AMRenderTypes.createMergedVertexConsumer(bufferIn.getBuffer(AMRenderTypes.VOID_WORM_PORTAL_OVERLAY), bufferIn.getBuffer(RenderType.entityCutoutNoCull(texture)));
                this.layerModel.m_7695_(matrixStackIn, consumer, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                float f = this.getAlpha(worm);
                this.m_117386_().m_7695_(matrixStackIn, bufferIn.getBuffer(RenderType.eyes(texture)), 240, LivingEntityRenderer.getOverlayCoords(worm, 1.0F), 1.0F, 1.0F, 1.0F, f);
            }
        }
    }

    public abstract ResourceLocation getGlowTexture(LivingEntity var1);

    public abstract boolean isGlowing(LivingEntity var1);

    public abstract float getAlpha(LivingEntity var1);

    private boolean isSpecialRenderer(ResourceLocation resourceLocation) {
        if (this.mcmetaData.containsKey(resourceLocation)) {
            return this.mcmetaData.getBoolean(resourceLocation);
        } else if (this.resourceManager.m_213713_(resourceLocation).isPresent()) {
            Resource resource = (Resource) this.resourceManager.m_213713_(resourceLocation).get();
            try {
                VoidWormMetadataSection section = (VoidWormMetadataSection) resource.metadata().getSection(VoidWormMetadataSection.SERIALIZER).orElse(new VoidWormMetadataSection());
                this.mcmetaData.put(resourceLocation, section.isEndPortalTexture());
                return section.isEndPortalTexture();
            } catch (Exception var4) {
                var4.printStackTrace();
                this.mcmetaData.put(resourceLocation, false);
                return false;
            }
        } else {
            this.mcmetaData.put(resourceLocation, false);
            return false;
        }
    }
}