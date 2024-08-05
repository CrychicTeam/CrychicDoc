package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class EnergySwirlLayer {

    public static final ResourceLocation EVASION_TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/evasion.png");

    public static final ResourceLocation CHARGE_TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/charged.png");

    private static RenderType getRenderType(ResourceLocation texture, float f) {
        return RenderType.energySwirl(texture, f * 0.02F % 1.0F, f * 0.01F % 1.0F);
    }

    private static boolean shouldRender(LivingEntity entity, Long shouldRenderFlag) {
        return ClientMagicData.getSyncedSpellData(entity).hasEffect(shouldRenderFlag);
    }

    public static class Geo extends GeoRenderLayer<AbstractSpellCastingMob> {

        private final ResourceLocation TEXTURE;

        private final Long shouldRenderFlag;

        public Geo(GeoEntityRenderer<AbstractSpellCastingMob> entityRendererIn, ResourceLocation texture, Long shouldRenderFlag) {
            super(entityRendererIn);
            this.TEXTURE = texture;
            this.shouldRenderFlag = shouldRenderFlag;
        }

        public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType2, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            if (EnergySwirlLayer.shouldRender(animatable, this.shouldRenderFlag)) {
                float f = (float) animatable.f_19797_ + partialTick;
                RenderType renderType = EnergySwirlLayer.getRenderType(this.TEXTURE, f);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
                poseStack.pushPose();
                bakedModel.getBone("body").ifPresent(rootBone -> rootBone.getChildBones().forEach(bone -> bone.updateScale(1.1F, 1.1F, 1.1F)));
                this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick, packedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
                bakedModel.getBone("body").ifPresent(rootBone -> rootBone.getChildBones().forEach(bone -> bone.updateScale(1.0F, 1.0F, 1.0F)));
                poseStack.popPose();
            }
        }
    }

    public static class Vanilla extends RenderLayer<Player, HumanoidModel<Player>> {

        public static ModelLayerLocation ENERGY_LAYER = new ModelLayerLocation(new ResourceLocation("irons_spellbooks", "energy_layer"), "main");

        private final HumanoidModel<Player> model = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ENERGY_LAYER));

        private final ResourceLocation TEXTURE;

        private final Long shouldRenderFlag;

        public Vanilla(RenderLayerParent pRenderer, ResourceLocation texture, Long shouldRenderFlag) {
            super(pRenderer);
            this.TEXTURE = texture;
            this.shouldRenderFlag = shouldRenderFlag;
        }

        public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, Player pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            if (EnergySwirlLayer.shouldRender(pLivingEntity, this.shouldRenderFlag)) {
                float f = (float) pLivingEntity.f_19797_ + pPartialTicks;
                HumanoidModel<Player> entitymodel = this.model();
                entitymodel.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks);
                ((HumanoidModel) this.m_117386_()).copyPropertiesTo(entitymodel);
                VertexConsumer vertexconsumer = pBuffer.getBuffer(EnergySwirlLayer.getRenderType(this.TEXTURE, f));
                entitymodel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                entitymodel.m_7695_(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0.8F, 0.8F, 0.8F, 1.0F);
            }
        }

        protected HumanoidModel<Player> model() {
            return this.model;
        }

        protected boolean shouldRender(Player entity) {
            return true;
        }
    }
}