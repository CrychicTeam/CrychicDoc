package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.LuxtructosaurusModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class LuxtructosaurusRenderer extends MobRenderer<LuxtructosaurusEntity, LuxtructosaurusModel> implements CustomBookEntityRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/luxtructosaurus.png");

    private static final ResourceLocation TEXTURE_ENRAGED = new ResourceLocation("alexscaves:textures/entity/luxtructosaurus_enraged.png");

    private static final ResourceLocation TEXTURE_ENRAGED_GLOW = new ResourceLocation("alexscaves:textures/entity/luxtructosaurus_enraged_glow.png");

    private static final HashMap<Integer, Vec3> mouthParticlePositions = new HashMap();

    private boolean sepia = false;

    public LuxtructosaurusRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new LuxtructosaurusModel(), 4.0F);
        this.m_115326_(new LuxtructosaurusRenderer.LayerGlow());
    }

    protected void scale(LuxtructosaurusEntity mob, PoseStack matrixStackIn, float partialTicks) {
    }

    public ResourceLocation getTextureLocation(LuxtructosaurusEntity entity) {
        return entity.isEnraged() ? TEXTURE_ENRAGED : TEXTURE;
    }

    @Nullable
    protected RenderType getRenderType(LuxtructosaurusEntity mob, boolean normal, boolean translucent, boolean outline) {
        ResourceLocation resourcelocation = this.getTextureLocation(mob);
        if (translucent) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (normal) {
            return this.sepia ? ACRenderTypes.getBookWidget(resourcelocation, true) : RenderType.entityCutoutNoCull(resourcelocation);
        } else {
            return outline ? RenderType.outline(resourcelocation) : null;
        }
    }

    public void render(LuxtructosaurusEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int packedLight) {
        if (this.sepia) {
            ((LuxtructosaurusModel) this.f_115290_).straighten = true;
        }
        super.render(entity, entityYaw, partialTicks, poseStack, source, packedLight);
        if (this.sepia) {
            ((LuxtructosaurusModel) this.f_115290_).straighten = false;
        }
        mouthParticlePositions.put(entity.m_19879_(), ((LuxtructosaurusModel) this.f_115290_).getMouthPosition(Vec3.ZERO));
    }

    public static Vec3 getMouthPositionFor(int entityId) {
        return (Vec3) mouthParticlePositions.get(entityId);
    }

    public boolean shouldRender(LuxtructosaurusEntity entity, Frustum camera, double x, double y, double z) {
        if (super.shouldRender(entity, camera, x, y, z)) {
            return true;
        } else {
            for (PartEntity part : entity.getParts()) {
                if (camera.isVisible(part.m_6921_())) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void setSepiaFlag(boolean sepiaFlag) {
        this.sepia = sepiaFlag;
    }

    protected float getFlipDegrees(LuxtructosaurusEntity luxtructosaurus) {
        return 0.0F;
    }

    class LayerGlow extends RenderLayer<LuxtructosaurusEntity, LuxtructosaurusModel> {

        public LayerGlow() {
            super(LuxtructosaurusRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LuxtructosaurusEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float enragedAlpha = ((float) Math.sin((double) (ageInTicks * 0.2F)) * 0.15F + 0.85F) * entitylivingbaseIn.getEnragedProgress(partialTicks);
            VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(LuxtructosaurusRenderer.TEXTURE_ENRAGED_GLOW));
            ((LuxtructosaurusModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, enragedAlpha);
        }
    }
}