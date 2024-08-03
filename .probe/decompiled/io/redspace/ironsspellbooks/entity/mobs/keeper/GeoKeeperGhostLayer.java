package io.redspace.ironsspellbooks.entity.mobs.keeper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class GeoKeeperGhostLayer extends GeoRenderLayer<AbstractSpellCastingMob> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/keeper/keeper_ghost.png");

    public GeoKeeperGhostLayer(GeoEntityRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType2, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        int hurtTime = animatable.f_20916_;
        if (hurtTime > 0) {
            float alpha = (float) hurtTime / (float) animatable.f_20917_;
            float f = (float) animatable.f_19797_ + partialTick;
            RenderType renderType = RenderType.energySwirl(TEXTURE, f * 0.02F % 1.0F, f * 0.01F % 1.0F);
            VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
            poseStack.pushPose();
            bakedModel.getBone("body").ifPresent(rootBone -> rootBone.getChildBones().forEach(bone -> {
                if (bone.getName().equals("head")) {
                    bone.updateScale(0.75F, 0.75F, 0.75F);
                } else {
                    bone.updateScale(0.95F, 0.99F, 0.95F);
                }
            }));
            this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick, 15728880, OverlayTexture.NO_OVERLAY, 0.15F * alpha, 0.02F * alpha, 0.0F * alpha, 1.0F);
            bakedModel.getBone("body").ifPresent(rootBone -> rootBone.getChildBones().forEach(bone -> bone.updateScale(1.0F, 1.0F, 1.0F)));
            poseStack.popPose();
        }
    }
}