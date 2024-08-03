package com.github.alexmodguy.alexscaves.client.render.entity.layer;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexmodguy.alexscaves.server.potion.DarknessIncarnateEffect;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ACPotionEffectLayer extends RenderLayer {

    private static final ResourceLocation TEXTURE_BUBBLE = new ResourceLocation("alexscaves:textures/entity/deep_one/bubble.png");

    private static final ResourceLocation TEXTURE_WATER = new ResourceLocation("textures/block/water_still.png");

    public static final ResourceLocation INSIDE_BUBBLE_TEXTURE = new ResourceLocation("alexscaves", "textures/misc/inside_bubble.png");

    public static final ResourceLocation TEXTURE_DARKNESS = new ResourceLocation("alexscaves", "textures/entity/darkness_incarnate.png");

    private RenderLayerParent parent;

    public ACPotionEffectLayer(RenderLayerParent parent) {
        super(parent);
        this.parent = parent;
    }

    public static void renderBubbledFirstPerson(PoseStack poseStack) {
        poseStack.pushPose();
        renderBubbledFluid(Minecraft.getInstance(), poseStack, TEXTURE_BUBBLE, false);
        renderBubbledFluid(Minecraft.getInstance(), poseStack, INSIDE_BUBBLE_TEXTURE, true);
        poseStack.popPose();
    }

    public static void renderBubbledFluid(Minecraft p110726, PoseStack poseStack, ResourceLocation texture, boolean translate) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, texture);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        BlockPos blockpos = BlockPos.containing(p110726.player.m_20185_(), p110726.player.m_20188_(), p110726.player.m_20189_());
        float f = LightTexture.getBrightness(p110726.player.m_9236_().dimensionType(), p110726.player.m_9236_().m_46803_(blockpos));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(f, f, f, translate ? 0.3F : 1.0F);
        Matrix4f matrix4f = poseStack.last().pose();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        if (translate) {
            float f7 = -p110726.player.m_146908_() / 64.0F;
            float f8 = p110726.player.m_146909_() / 64.0F;
            bufferbuilder.m_252986_(matrix4f, -1.0F, -1.0F, -0.5F).uv(4.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.m_252986_(matrix4f, 1.0F, -1.0F, -0.5F).uv(0.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.m_252986_(matrix4f, 1.0F, 1.0F, -0.5F).uv(0.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.m_252986_(matrix4f, -1.0F, 1.0F, -0.5F).uv(4.0F + f7, 0.0F + f8).endVertex();
        } else {
            float min = -0.5F;
            float max = 1.5F;
            bufferbuilder.m_252986_(matrix4f, -1.0F, -1.0F, -0.5F).uv(max, max).endVertex();
            bufferbuilder.m_252986_(matrix4f, 1.0F, -1.0F, -0.5F).uv(min, max).endVertex();
            bufferbuilder.m_252986_(matrix4f, 1.0F, 1.0F, -0.5F).uv(min, min).endVertex();
            bufferbuilder.m_252986_(matrix4f, -1.0F, 1.0F, -0.5F).uv(max, min).endVertex();
        }
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof LivingEntity living) {
            if (living.hasEffect(ACEffectRegistry.IRRADIATED.get()) && AlexsCaves.CLIENT_CONFIG.radiationGlowEffect.get()) {
                PostEffectRegistry.renderEffectForNextTick(ClientProxy.IRRADIATED_SHADER);
                int level = living.getEffect(ACEffectRegistry.IRRADIATED.get()).getAmplifier() + 1;
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(level >= 4 ? ACRenderTypes.getBlueRadiationGlow(this.m_117347_(entity)) : ACRenderTypes.getRadiationGlow(this.m_117347_(entity)));
                float alpha = level >= 4 ? 0.9F : Math.min((float) level * 0.33F, 1.0F);
                poseStack.pushPose();
                this.m_117386_().m_7695_(poseStack, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords((LivingEntity) entity, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
                poseStack.popPose();
            }
            if (living.hasEffect(ACEffectRegistry.BUBBLED.get()) && living.isAlive()) {
                float bodyYaw = Mth.rotLerp(partialTicks, living.yBodyRotO, living.yBodyRot);
                poseStack.pushPose();
                float size = (float) Math.ceil((double) Math.max(living.m_20206_(), living.m_20205_()));
                poseStack.translate(0.0, 1.4 - (double) (size * 0.5F), 0.0);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
                poseStack.scale(1.1F, 1.1F, 1.1F);
                float waterAnimOffset = (float) Math.round((double) ageInTicks * 0.4) % 16.0F;
                renderBubble(living, partialTicks, poseStack, bufferIn.getBuffer(ACRenderTypes.getBubbledCull(TEXTURE_WATER)), size - 0.1F, packedLightIn, size * 0.5F, size * 0.5F * 0.0625F, -0.0625F * waterAnimOffset, true);
                renderBubble(living, partialTicks, poseStack, bufferIn.getBuffer(ACRenderTypes.getBubbledNoCull(TEXTURE_BUBBLE)), size, packedLightIn, 1.0F, 1.0F, 0.0F, false);
                poseStack.popPose();
            }
            if (living.hasEffect(ACEffectRegistry.DARKNESS_INCARNATE.get()) && AlexsCaves.CLIENT_CONFIG.radiationGlowEffect.get() && living.isAlive()) {
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.m_110473_(this.m_117347_(entity)));
                poseStack.pushPose();
                float alpha = DarknessIncarnateEffect.getIntensity(living, partialTicks, 25.0F);
                this.m_117386_().m_7695_(poseStack, ivertexbuilder, 0, LivingEntityRenderer.getOverlayCoords((LivingEntity) entity, 0.0F), 0.0F, 0.0F, 0.0F, alpha);
                poseStack.popPose();
            }
        }
    }

    private static void renderBubble(LivingEntity entity, float partialTicks, PoseStack poseStack, VertexConsumer consumer, float size, int packedLight, float textureScaleXZ, float textureScaleY, float uvOffset, boolean water) {
        Matrix4f cubeAt = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        float cubeStart = size * -0.5F;
        float cubeEnd = size * 0.5F;
        renderCubeFace(entity, cubeAt, matrix3f, consumer, packedLight, cubeStart, cubeEnd, cubeStart, cubeEnd, cubeEnd, cubeEnd, cubeEnd, cubeEnd, textureScaleXZ, textureScaleY, uvOffset, water);
        renderCubeFace(entity, cubeAt, matrix3f, consumer, packedLight, cubeStart, cubeEnd, cubeEnd, cubeStart, cubeStart, cubeStart, cubeStart, cubeStart, textureScaleXZ, textureScaleY, uvOffset, water);
        renderCubeFace(entity, cubeAt, matrix3f, consumer, packedLight, cubeEnd, cubeEnd, cubeEnd, cubeStart, cubeStart, cubeEnd, cubeEnd, cubeStart, textureScaleXZ, textureScaleY, uvOffset, water);
        renderCubeFace(entity, cubeAt, matrix3f, consumer, packedLight, cubeStart, cubeStart, cubeStart, cubeEnd, cubeStart, cubeEnd, cubeEnd, cubeStart, textureScaleXZ, textureScaleY, uvOffset, water);
        renderCubeFace(entity, cubeAt, matrix3f, consumer, packedLight, cubeStart, cubeEnd, cubeStart, cubeStart, cubeStart, cubeStart, cubeEnd, cubeEnd, textureScaleXZ, textureScaleY, uvOffset, water);
        renderCubeFace(entity, cubeAt, matrix3f, consumer, packedLight, cubeStart, cubeEnd, cubeEnd, cubeEnd, cubeEnd, cubeEnd, cubeStart, cubeStart, textureScaleXZ, textureScaleY, uvOffset, water);
    }

    private static void renderCubeFace(LivingEntity entity, Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, int packedLightIn, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float textureScaleXZ, float textureScaleY, float uvOffset, boolean water) {
        int overlayCoords = OverlayTexture.NO_OVERLAY;
        int colorR = 255;
        int colorG = 255;
        int colorB = 255;
        int colorA = water ? 200 : 255;
        if (water) {
            int waterColorAt = ((Biome) entity.m_9236_().m_204166_(entity.m_20183_()).get()).getWaterColor();
            colorR = waterColorAt >> 16 & 0xFF;
            colorG = waterColorAt >> 8 & 0xFF;
            colorB = waterColorAt & 0xFF;
        }
        vertexConsumer.vertex(matrix4f, f1, f3, f5).color(colorR, colorG, colorB, colorA).uv(0.0F, textureScaleY + uvOffset).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, f2, f3, f6).color(colorR, colorG, colorB, colorA).uv(textureScaleXZ, textureScaleY + uvOffset).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, f2, f4, f7).color(colorR, colorG, colorB, colorA).uv(textureScaleXZ, uvOffset).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, f1, f4, f8).color(colorR, colorG, colorB, colorA).uv(0.0F, uvOffset).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
    }
}