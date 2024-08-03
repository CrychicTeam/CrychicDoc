package net.minecraft.client.renderer.entity.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.BeeStingerLayer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;

public class PlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRenderer(EntityRendererProvider.Context entityRendererProviderContext0, boolean boolean1) {
        super(entityRendererProviderContext0, new PlayerModel<>(entityRendererProviderContext0.bakeLayer(boolean1 ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), boolean1), 0.5F);
        this.m_115326_(new HumanoidArmorLayer<>(this, new HumanoidArmorModel(entityRendererProviderContext0.bakeLayer(boolean1 ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidArmorModel(entityRendererProviderContext0.bakeLayer(boolean1 ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)), entityRendererProviderContext0.getModelManager()));
        this.m_115326_(new PlayerItemInHandLayer<>(this, entityRendererProviderContext0.getItemInHandRenderer()));
        this.m_115326_(new ArrowLayer<>(entityRendererProviderContext0, this));
        this.m_115326_(new Deadmau5EarsLayer(this));
        this.m_115326_(new CapeLayer(this));
        this.m_115326_(new CustomHeadLayer<>(this, entityRendererProviderContext0.getModelSet(), entityRendererProviderContext0.getItemInHandRenderer()));
        this.m_115326_(new ElytraLayer<>(this, entityRendererProviderContext0.getModelSet()));
        this.m_115326_(new ParrotOnShoulderLayer<>(this, entityRendererProviderContext0.getModelSet()));
        this.m_115326_(new SpinAttackEffectLayer<>(this, entityRendererProviderContext0.getModelSet()));
        this.m_115326_(new BeeStingerLayer<>(this));
    }

    public void render(AbstractClientPlayer abstractClientPlayer0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        this.setModelProperties(abstractClientPlayer0);
        super.render(abstractClientPlayer0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public Vec3 getRenderOffset(AbstractClientPlayer abstractClientPlayer0, float float1) {
        return abstractClientPlayer0.m_6047_() ? new Vec3(0.0, -0.125, 0.0) : super.m_7860_(abstractClientPlayer0, float1);
    }

    private void setModelProperties(AbstractClientPlayer abstractClientPlayer0) {
        PlayerModel<AbstractClientPlayer> $$1 = (PlayerModel<AbstractClientPlayer>) this.m_7200_();
        if (abstractClientPlayer0.isSpectator()) {
            $$1.setAllVisible(false);
            $$1.f_102808_.visible = true;
            $$1.f_102809_.visible = true;
        } else {
            $$1.setAllVisible(true);
            $$1.f_102809_.visible = abstractClientPlayer0.m_36170_(PlayerModelPart.HAT);
            $$1.jacket.visible = abstractClientPlayer0.m_36170_(PlayerModelPart.JACKET);
            $$1.leftPants.visible = abstractClientPlayer0.m_36170_(PlayerModelPart.LEFT_PANTS_LEG);
            $$1.rightPants.visible = abstractClientPlayer0.m_36170_(PlayerModelPart.RIGHT_PANTS_LEG);
            $$1.leftSleeve.visible = abstractClientPlayer0.m_36170_(PlayerModelPart.LEFT_SLEEVE);
            $$1.rightSleeve.visible = abstractClientPlayer0.m_36170_(PlayerModelPart.RIGHT_SLEEVE);
            $$1.f_102817_ = abstractClientPlayer0.m_6047_();
            HumanoidModel.ArmPose $$2 = getArmPose(abstractClientPlayer0, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose $$3 = getArmPose(abstractClientPlayer0, InteractionHand.OFF_HAND);
            if ($$2.isTwoHanded()) {
                $$3 = abstractClientPlayer0.m_21206_().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
            }
            if (abstractClientPlayer0.m_5737_() == HumanoidArm.RIGHT) {
                $$1.f_102816_ = $$2;
                $$1.f_102815_ = $$3;
            } else {
                $$1.f_102816_ = $$3;
                $$1.f_102815_ = $$2;
            }
        }
    }

    private static HumanoidModel.ArmPose getArmPose(AbstractClientPlayer abstractClientPlayer0, InteractionHand interactionHand1) {
        ItemStack $$2 = abstractClientPlayer0.m_21120_(interactionHand1);
        if ($$2.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (abstractClientPlayer0.m_7655_() == interactionHand1 && abstractClientPlayer0.m_21212_() > 0) {
                UseAnim $$3 = $$2.getUseAnimation();
                if ($$3 == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }
                if ($$3 == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }
                if ($$3 == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }
                if ($$3 == UseAnim.CROSSBOW && interactionHand1 == abstractClientPlayer0.m_7655_()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }
                if ($$3 == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }
                if ($$3 == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }
                if ($$3 == UseAnim.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                }
            } else if (!abstractClientPlayer0.f_20911_ && $$2.is(Items.CROSSBOW) && CrossbowItem.isCharged($$2)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }
            return HumanoidModel.ArmPose.ITEM;
        }
    }

    public ResourceLocation getTextureLocation(AbstractClientPlayer abstractClientPlayer0) {
        return abstractClientPlayer0.getSkinTextureLocation();
    }

    protected void scale(AbstractClientPlayer abstractClientPlayer0, PoseStack poseStack1, float float2) {
        float $$3 = 0.9375F;
        poseStack1.scale(0.9375F, 0.9375F, 0.9375F);
    }

    protected void renderNameTag(AbstractClientPlayer abstractClientPlayer0, Component component1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4) {
        double $$5 = this.f_114476_.distanceToSqr(abstractClientPlayer0);
        poseStack2.pushPose();
        if ($$5 < 100.0) {
            Scoreboard $$6 = abstractClientPlayer0.m_36329_();
            Objective $$7 = $$6.getDisplayObjective(2);
            if ($$7 != null) {
                Score $$8 = $$6.getOrCreatePlayerScore(abstractClientPlayer0.m_6302_(), $$7);
                super.m_7649_(abstractClientPlayer0, Component.literal(Integer.toString($$8.getScore())).append(CommonComponents.SPACE).append($$7.getDisplayName()), poseStack2, multiBufferSource3, int4);
                poseStack2.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
            }
        }
        super.m_7649_(abstractClientPlayer0, component1, poseStack2, multiBufferSource3, int4);
        poseStack2.popPose();
    }

    public void renderRightHand(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, AbstractClientPlayer abstractClientPlayer3) {
        this.renderHand(poseStack0, multiBufferSource1, int2, abstractClientPlayer3, ((PlayerModel) this.f_115290_).f_102811_, ((PlayerModel) this.f_115290_).rightSleeve);
    }

    public void renderLeftHand(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, AbstractClientPlayer abstractClientPlayer3) {
        this.renderHand(poseStack0, multiBufferSource1, int2, abstractClientPlayer3, ((PlayerModel) this.f_115290_).f_102812_, ((PlayerModel) this.f_115290_).leftSleeve);
    }

    private void renderHand(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, AbstractClientPlayer abstractClientPlayer3, ModelPart modelPart4, ModelPart modelPart5) {
        PlayerModel<AbstractClientPlayer> $$6 = (PlayerModel<AbstractClientPlayer>) this.m_7200_();
        this.setModelProperties(abstractClientPlayer3);
        $$6.f_102608_ = 0.0F;
        $$6.f_102817_ = false;
        $$6.f_102818_ = 0.0F;
        $$6.setupAnim(abstractClientPlayer3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        modelPart4.xRot = 0.0F;
        modelPart4.render(poseStack0, multiBufferSource1.getBuffer(RenderType.entitySolid(abstractClientPlayer3.getSkinTextureLocation())), int2, OverlayTexture.NO_OVERLAY);
        modelPart5.xRot = 0.0F;
        modelPart5.render(poseStack0, multiBufferSource1.getBuffer(RenderType.entityTranslucent(abstractClientPlayer3.getSkinTextureLocation())), int2, OverlayTexture.NO_OVERLAY);
    }

    protected void setupRotations(AbstractClientPlayer abstractClientPlayer0, PoseStack poseStack1, float float2, float float3, float float4) {
        float $$5 = abstractClientPlayer0.m_20998_(float4);
        if (abstractClientPlayer0.m_21255_()) {
            super.setupRotations(abstractClientPlayer0, poseStack1, float2, float3, float4);
            float $$6 = (float) abstractClientPlayer0.m_21256_() + float4;
            float $$7 = Mth.clamp($$6 * $$6 / 100.0F, 0.0F, 1.0F);
            if (!abstractClientPlayer0.m_21209_()) {
                poseStack1.mulPose(Axis.XP.rotationDegrees($$7 * (-90.0F - abstractClientPlayer0.m_146909_())));
            }
            Vec3 $$8 = abstractClientPlayer0.m_20252_(float4);
            Vec3 $$9 = abstractClientPlayer0.getDeltaMovementLerped(float4);
            double $$10 = $$9.horizontalDistanceSqr();
            double $$11 = $$8.horizontalDistanceSqr();
            if ($$10 > 0.0 && $$11 > 0.0) {
                double $$12 = ($$9.x * $$8.x + $$9.z * $$8.z) / Math.sqrt($$10 * $$11);
                double $$13 = $$9.x * $$8.z - $$9.z * $$8.x;
                poseStack1.mulPose(Axis.YP.rotation((float) (Math.signum($$13) * Math.acos($$12))));
            }
        } else if ($$5 > 0.0F) {
            super.setupRotations(abstractClientPlayer0, poseStack1, float2, float3, float4);
            float $$14 = abstractClientPlayer0.m_20069_() ? -90.0F - abstractClientPlayer0.m_146909_() : -90.0F;
            float $$15 = Mth.lerp($$5, 0.0F, $$14);
            poseStack1.mulPose(Axis.XP.rotationDegrees($$15));
            if (abstractClientPlayer0.m_6067_()) {
                poseStack1.translate(0.0F, -1.0F, 0.3F);
            }
        } else {
            super.setupRotations(abstractClientPlayer0, poseStack1, float2, float3, float4);
        }
    }
}