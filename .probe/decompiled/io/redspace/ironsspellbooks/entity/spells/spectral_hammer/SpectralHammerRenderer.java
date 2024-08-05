package io.redspace.ironsspellbooks.entity.spells.spectral_hammer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class SpectralHammerRenderer extends GeoLivingEntityRenderer<SpectralHammer> {

    public SpectralHammerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SpectralHammerModel());
        this.f_114477_ = 0.3F;
    }

    public ResourceLocation getTextureLocation(SpectralHammer animatable) {
        return SpectralHammerModel.textureResource;
    }

    public void preRender(PoseStack poseStack, SpectralHammer animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(2.0F, 2.0F, 2.0F);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public RenderType getRenderType(SpectralHammer animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        Vec2 vec2 = getEnergySwirlOffset(animatable, partialTick);
        return RenderType.energySwirl(texture, vec2.x, vec2.y);
    }

    private static float shittyNoise(float f) {
        return (float) (Math.sin((double) (f / 4.0F)) + 2.0 * Math.sin((double) (f / 3.0F)) + 3.0 * Math.sin((double) (f / 2.0F)) + 4.0 * Math.sin((double) f)) * 0.25F;
    }

    public static Vec2 getEnergySwirlOffset(SpectralHammer entity, float partialTicks, int offset) {
        float f = ((float) entity.f_19797_ + partialTicks) * 0.02F;
        return new Vec2(shittyNoise(1.2F * f + (float) offset), shittyNoise(f + 456.0F + (float) offset));
    }

    public static Vec2 getEnergySwirlOffset(SpectralHammer entity, float partialTicks) {
        return getEnergySwirlOffset(entity, partialTicks, 0);
    }
}