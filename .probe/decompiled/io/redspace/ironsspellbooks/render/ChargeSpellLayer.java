package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceRenderer;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowRenderer;
import io.redspace.ironsspellbooks.entity.spells.poison_arrow.PoisonArrowRenderer;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

public class ChargeSpellLayer {

    public static HumanoidArm getArmFromUseHand(LivingEntity livingEntity) {
        return livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? livingEntity.getMainArm() : livingEntity.getMainArm().getOpposite();
    }

    public static class Geo extends GeoRenderLayer<AbstractSpellCastingMob> {

        public Geo(GeoEntityRenderer<AbstractSpellCastingMob> entityRenderer) {
            super(entityRenderer);
        }

        public void render(PoseStack poseStack, AbstractSpellCastingMob entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            SyncedSpellData syncedSpellData = ClientMagicData.getSyncedSpellData(entity);
            String spellId = syncedSpellData.getCastingSpellId();
            GeoBone bone = (GeoBone) bakedModel.getBone("bipedHandRight").get();
            poseStack.pushPose();
            RenderUtils.translateToPivotPoint(poseStack, bone);
            RenderUtils.rotateMatrixAroundBone(poseStack, (CoreGeoBone) bakedModel.getBone("right_arm").get());
            RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
            HumanoidArm arm = ChargeSpellLayer.getArmFromUseHand(entity);
            boolean flag = arm == HumanoidArm.LEFT;
            if (spellId.equals(SpellRegistry.LIGHTNING_LANCE_SPELL.get().getSpellId())) {
                poseStack.translate(-((double) ((float) (flag ? -1 : 1) / 32.0F) - 0.125), 0.5, 0.0);
                poseStack.translate(0.0F, -bone.getPivotY() / 16.0F, 0.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                LightningLanceRenderer.renderModel(poseStack, bufferSource, entity.f_19797_);
            } else if (spellId.equals(SpellRegistry.MAGIC_ARROW_SPELL.get().getSpellId())) {
                poseStack.translate(0.0F, -bone.getPivotY() / 16.0F, 0.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.translate((double) (-((float) (flag ? -1 : 1) / 32.0F)), 0.5, -0.55);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                MagicArrowRenderer.renderModel(poseStack, bufferSource);
            } else if (spellId.equals(SpellRegistry.POISON_ARROW_SPELL.get().getSpellId())) {
                poseStack.translate(0.0F, -bone.getPivotY() / 16.0F, 0.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.translate((double) (-((float) (flag ? -1 : 1) / 32.0F)), 0.5, -0.55);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                PoisonArrowRenderer.renderModel(poseStack, bufferSource, packedLight);
            }
            poseStack.popPose();
        }
    }

    public static class Vanilla<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

        public Vanilla(RenderLayerParent<T, M> pRenderer) {
            super(pRenderer);
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight, T entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            SyncedSpellData syncedSpellData = ClientMagicData.getSyncedSpellData(entity);
            if (syncedSpellData.isCasting()) {
                String spellId = syncedSpellData.getCastingSpellId();
                poseStack.pushPose();
                HumanoidArm arm = ChargeSpellLayer.getArmFromUseHand(entity);
                ((HumanoidModel) this.m_117386_()).translateToHand(arm, poseStack);
                boolean flag = arm == HumanoidArm.LEFT;
                if (spellId.equals(SpellRegistry.LIGHTNING_LANCE_SPELL.get().getSpellId())) {
                    poseStack.translate((double) ((float) (flag ? -1 : 1) / 32.0F) - 0.125, 0.5, 0.0);
                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    float castCompletion = Utils.smoothstep(0.35F, 1.0F, ClientMagicData.getCastCompletionPercent());
                    poseStack.scale(castCompletion, castCompletion, castCompletion);
                    LightningLanceRenderer.renderModel(poseStack, bufferSource, entity.f_19797_);
                } else if (spellId.equals(SpellRegistry.MAGIC_ARROW_SPELL.get().getSpellId())) {
                    poseStack.translate((double) ((float) (flag ? -1 : 1) / 32.0F), 0.5, 0.0);
                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    float castCompletion = Utils.smoothstep(0.65F, 1.0F, ClientMagicData.getCastCompletionPercent());
                    poseStack.scale(castCompletion, castCompletion, castCompletion);
                    MagicArrowRenderer.renderModel(poseStack, bufferSource);
                } else if (spellId.equals(SpellRegistry.POISON_ARROW_SPELL.get().getSpellId())) {
                    poseStack.translate((float) (flag ? -1 : 1) / 32.0F, 1.0F, 0.0F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    float castCompletion = Utils.smoothstep(0.65F, 1.0F, ClientMagicData.getCastCompletionPercent());
                    poseStack.scale(castCompletion, castCompletion, castCompletion);
                    PoisonArrowRenderer.renderModel(poseStack, bufferSource, pPackedLight);
                }
                poseStack.popPose();
            }
        }
    }
}