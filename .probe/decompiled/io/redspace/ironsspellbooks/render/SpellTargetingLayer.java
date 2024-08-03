package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MultiTargetEntityCastData;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SpellTargetingLayer {

    public static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/target/heal.png");

    private static Vector3f getColor(String spellId) {
        return SpellRegistry.getSpell(spellId).getTargetingColor();
    }

    public static void renderTargetLayer(PoseStack poseStack, MultiBufferSource bufferSource, LivingEntity entity) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.energySwirl(TEXTURE, 0.0F, 0.0F));
        AABB aabb = entity.m_20191_().move(-entity.m_20185_(), -entity.m_20186_(), -entity.m_20189_());
        float width = (float) aabb.getXsize();
        float height = (float) aabb.getYsize();
        float halfWidth = width * 0.55F;
        float magicYOffset = (float) (1.5 - (double) height);
        Vector3f color = null;
        if (ClientMagicData.getRecasts().hasRecastsActive()) {
            for (RecastInstance recastInstance : ClientMagicData.getRecasts().getActiveRecasts()) {
                if (recastInstance.getCastData() instanceof MultiTargetEntityCastData targetEntityCastData && targetEntityCastData.isTargeted(entity)) {
                    color = getColor(recastInstance.getSpellId());
                    break;
                }
            }
        }
        if (color == null) {
            color = getColor(ClientMagicData.getTargetingData().spellId);
        }
        color.mul(0.4F);
        poseStack.pushPose();
        poseStack.translate(0.0F, magicYOffset, 0.0F);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        for (int i = 0; i < 4; i++) {
            consumer.vertex(poseMatrix, halfWidth, height, halfWidth).color(color.x(), color.y(), color.z(), 1.0F).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, halfWidth, 0.0F, halfWidth).color(color.x(), color.y(), color.z(), 1.0F).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, -halfWidth, 0.0F, halfWidth).color(color.x(), color.y(), color.z(), 1.0F).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, -halfWidth, height, halfWidth).color(color.x(), color.y(), color.z(), 1.0F).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        }
        poseStack.popPose();
    }

    public static boolean shouldRender(LivingEntity entity) {
        if (ClientMagicData.getRecasts().hasRecastsActive()) {
            for (RecastInstance recastInstance : ClientMagicData.getRecasts().getActiveRecasts()) {
                if (recastInstance.getCastData() instanceof MultiTargetEntityCastData targetEntityCastData && targetEntityCastData.isTargeted(entity)) {
                    return true;
                }
            }
        }
        return ClientMagicData.getTargetingData().isTargeted(entity);
    }

    public static class Geo extends GeoRenderLayer<AbstractSpellCastingMob> {

        public Geo(GeoEntityRenderer<AbstractSpellCastingMob> entityRendererIn) {
            super(entityRendererIn);
        }

        public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            if (SpellTargetingLayer.shouldRender(animatable)) {
                poseStack.pushPose();
                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.translate(0.0, -animatable.m_20191_().getYsize() / 2.0, 0.0);
                SpellTargetingLayer.renderTargetLayer(poseStack, bufferSource, animatable);
                poseStack.popPose();
            }
        }
    }

    public static class Vanilla<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

        public Vanilla(RenderLayerParent<T, M> pRenderer) {
            super(pRenderer);
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight, T entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            if (SpellTargetingLayer.shouldRender(entity)) {
                SpellTargetingLayer.renderTargetLayer(poseStack, bufferSource, entity);
            }
        }
    }
}