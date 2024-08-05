package io.redspace.ironsspellbooks.entity.spells.void_tentacle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class VoidTentacleEmissiveLayer extends GeoRenderLayer<VoidTentacle> {

    public static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/void_tentacle/void_tentacle_emissive.png");

    public VoidTentacleEmissiveLayer(GeoEntityRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    public void render(PoseStack poseStack, VoidTentacle animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        renderType = RenderType.eyes(TEXTURE);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
        poseStack.pushPose();
        float f = Mth.sin((float) (((double) ((float) animatable.f_19797_ + partialTick) + (animatable.m_20185_() + animatable.m_20189_()) * 500.0) * 0.15F)) * 0.5F + 0.5F;
        this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick, 15728880, OverlayTexture.NO_OVERLAY, f, f, f, 1.0F);
        poseStack.popPose();
    }
}