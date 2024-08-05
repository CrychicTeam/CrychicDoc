package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.scores.Team;
import org.slf4j.Logger;

public abstract class LivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final float EYE_BED_OFFSET = 0.1F;

    protected M model;

    protected final List<RenderLayer<T, M>> layers = Lists.newArrayList();

    public LivingEntityRenderer(EntityRendererProvider.Context entityRendererProviderContext0, M m1, float float2) {
        super(entityRendererProviderContext0);
        this.model = m1;
        this.f_114477_ = float2;
    }

    protected final boolean addLayer(RenderLayer<T, M> renderLayerTM0) {
        return this.layers.add(renderLayerTM0);
    }

    @Override
    public M getModel() {
        return this.model;
    }

    public void render(T t0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        this.model.attackTime = this.getAttackAnim(t0, float2);
        this.model.riding = t0.m_20159_();
        this.model.young = t0.isBaby();
        float $$6 = Mth.rotLerp(float2, t0.yBodyRotO, t0.yBodyRot);
        float $$7 = Mth.rotLerp(float2, t0.yHeadRotO, t0.yHeadRot);
        float $$8 = $$7 - $$6;
        if (t0.m_20159_() && t0.m_20202_() instanceof LivingEntity) {
            LivingEntity $$9 = (LivingEntity) t0.m_20202_();
            $$6 = Mth.rotLerp(float2, $$9.yBodyRotO, $$9.yBodyRot);
            $$8 = $$7 - $$6;
            float $$10 = Mth.wrapDegrees($$8);
            if ($$10 < -85.0F) {
                $$10 = -85.0F;
            }
            if ($$10 >= 85.0F) {
                $$10 = 85.0F;
            }
            $$6 = $$7 - $$10;
            if ($$10 * $$10 > 2500.0F) {
                $$6 += $$10 * 0.2F;
            }
            $$8 = $$7 - $$6;
        }
        float $$11 = Mth.lerp(float2, t0.f_19860_, t0.m_146909_());
        if (isEntityUpsideDown(t0)) {
            $$11 *= -1.0F;
            $$8 *= -1.0F;
        }
        if (t0.m_217003_(Pose.SLEEPING)) {
            Direction $$12 = t0.getBedOrientation();
            if ($$12 != null) {
                float $$13 = t0.m_20236_(Pose.STANDING) - 0.1F;
                poseStack3.translate((float) (-$$12.getStepX()) * $$13, 0.0F, (float) (-$$12.getStepZ()) * $$13);
            }
        }
        float $$14 = this.getBob(t0, float2);
        this.setupRotations(t0, poseStack3, $$14, $$6, float2);
        poseStack3.scale(-1.0F, -1.0F, 1.0F);
        this.scale(t0, poseStack3, float2);
        poseStack3.translate(0.0F, -1.501F, 0.0F);
        float $$15 = 0.0F;
        float $$16 = 0.0F;
        if (!t0.m_20159_() && t0.isAlive()) {
            $$15 = t0.walkAnimation.speed(float2);
            $$16 = t0.walkAnimation.position(float2);
            if (t0.isBaby()) {
                $$16 *= 3.0F;
            }
            if ($$15 > 1.0F) {
                $$15 = 1.0F;
            }
        }
        this.model.prepareMobModel(t0, $$16, $$15, float2);
        this.model.setupAnim(t0, $$16, $$15, $$14, $$8, $$11);
        Minecraft $$17 = Minecraft.getInstance();
        boolean $$18 = this.isBodyVisible(t0);
        boolean $$19 = !$$18 && !t0.m_20177_($$17.player);
        boolean $$20 = $$17.shouldEntityAppearGlowing(t0);
        RenderType $$21 = this.getRenderType(t0, $$18, $$19, $$20);
        if ($$21 != null) {
            VertexConsumer $$22 = multiBufferSource4.getBuffer($$21);
            int $$23 = getOverlayCoords(t0, this.getWhiteOverlayProgress(t0, float2));
            this.model.m_7695_(poseStack3, $$22, int5, $$23, 1.0F, 1.0F, 1.0F, $$19 ? 0.15F : 1.0F);
        }
        if (!t0.m_5833_()) {
            for (RenderLayer<T, M> $$24 : this.layers) {
                $$24.render(poseStack3, multiBufferSource4, int5, t0, $$16, $$15, float2, $$14, $$8, $$11);
            }
        }
        poseStack3.popPose();
        super.render(t0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    @Nullable
    protected RenderType getRenderType(T t0, boolean boolean1, boolean boolean2, boolean boolean3) {
        ResourceLocation $$4 = this.m_5478_(t0);
        if (boolean2) {
            return RenderType.itemEntityTranslucentCull($$4);
        } else if (boolean1) {
            return this.model.m_103119_($$4);
        } else {
            return boolean3 ? RenderType.outline($$4) : null;
        }
    }

    public static int getOverlayCoords(LivingEntity livingEntity0, float float1) {
        return OverlayTexture.pack(OverlayTexture.u(float1), OverlayTexture.v(livingEntity0.hurtTime > 0 || livingEntity0.deathTime > 0));
    }

    protected boolean isBodyVisible(T t0) {
        return !t0.m_20145_();
    }

    private static float sleepDirectionToRotation(Direction direction0) {
        switch(direction0) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    protected boolean isShaking(T t0) {
        return t0.m_146890_();
    }

    protected void setupRotations(T t0, PoseStack poseStack1, float float2, float float3, float float4) {
        if (this.isShaking(t0)) {
            float3 += (float) (Math.cos((double) t0.f_19797_ * 3.25) * Math.PI * 0.4F);
        }
        if (!t0.m_217003_(Pose.SLEEPING)) {
            poseStack1.mulPose(Axis.YP.rotationDegrees(180.0F - float3));
        }
        if (t0.deathTime > 0) {
            float $$5 = ((float) t0.deathTime + float4 - 1.0F) / 20.0F * 1.6F;
            $$5 = Mth.sqrt($$5);
            if ($$5 > 1.0F) {
                $$5 = 1.0F;
            }
            poseStack1.mulPose(Axis.ZP.rotationDegrees($$5 * this.getFlipDegrees(t0)));
        } else if (t0.isAutoSpinAttack()) {
            poseStack1.mulPose(Axis.XP.rotationDegrees(-90.0F - t0.m_146909_()));
            poseStack1.mulPose(Axis.YP.rotationDegrees(((float) t0.f_19797_ + float4) * -75.0F));
        } else if (t0.m_217003_(Pose.SLEEPING)) {
            Direction $$6 = t0.getBedOrientation();
            float $$7 = $$6 != null ? sleepDirectionToRotation($$6) : float3;
            poseStack1.mulPose(Axis.YP.rotationDegrees($$7));
            poseStack1.mulPose(Axis.ZP.rotationDegrees(this.getFlipDegrees(t0)));
            poseStack1.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (isEntityUpsideDown(t0)) {
            poseStack1.translate(0.0F, t0.m_20206_() + 0.1F, 0.0F);
            poseStack1.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    protected float getAttackAnim(T t0, float float1) {
        return t0.getAttackAnim(float1);
    }

    protected float getBob(T t0, float float1) {
        return (float) t0.f_19797_ + float1;
    }

    protected float getFlipDegrees(T t0) {
        return 90.0F;
    }

    protected float getWhiteOverlayProgress(T t0, float float1) {
        return 0.0F;
    }

    protected void scale(T t0, PoseStack poseStack1, float float2) {
    }

    protected boolean shouldShowName(T t0) {
        double $$1 = this.f_114476_.distanceToSqr(t0);
        float $$2 = t0.m_20163_() ? 32.0F : 64.0F;
        if ($$1 >= (double) ($$2 * $$2)) {
            return false;
        } else {
            Minecraft $$3 = Minecraft.getInstance();
            LocalPlayer $$4 = $$3.player;
            boolean $$5 = !t0.m_20177_($$4);
            if (t0 != $$4) {
                Team $$6 = t0.m_5647_();
                Team $$7 = $$4.m_5647_();
                if ($$6 != null) {
                    Team.Visibility $$8 = $$6.getNameTagVisibility();
                    switch($$8) {
                        case ALWAYS:
                            return $$5;
                        case NEVER:
                            return false;
                        case HIDE_FOR_OTHER_TEAMS:
                            return $$7 == null ? $$5 : $$6.isAlliedTo($$7) && ($$6.canSeeFriendlyInvisibles() || $$5);
                        case HIDE_FOR_OWN_TEAM:
                            return $$7 == null ? $$5 : !$$6.isAlliedTo($$7) && $$5;
                        default:
                            return true;
                    }
                }
            }
            return Minecraft.renderNames() && t0 != $$3.getCameraEntity() && $$5 && !t0.m_20160_();
        }
    }

    public static boolean isEntityUpsideDown(LivingEntity livingEntity0) {
        if (livingEntity0 instanceof Player || livingEntity0.m_8077_()) {
            String $$1 = ChatFormatting.stripFormatting(livingEntity0.m_7755_().getString());
            if ("Dinnerbone".equals($$1) || "Grumm".equals($$1)) {
                return !(livingEntity0 instanceof Player) || ((Player) livingEntity0).isModelPartShown(PlayerModelPart.CAPE);
            }
        }
        return false;
    }
}