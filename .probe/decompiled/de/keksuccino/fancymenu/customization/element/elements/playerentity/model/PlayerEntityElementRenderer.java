package de.keksuccino.fancymenu.customization.element.elements.playerentity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.keksuccino.fancymenu.customization.element.elements.playerentity.model.layers.PlayerEntityCapeLayer;
import de.keksuccino.fancymenu.customization.element.elements.playerentity.model.layers.PlayerEntityRenderLayer;
import de.keksuccino.fancymenu.customization.element.elements.playerentity.model.layers.PlayerEntityShoulderParrotLayer;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class PlayerEntityElementRenderer extends PlayerRenderer {

    public static final EntityModelSet ENTITY_MODEL_SET = Minecraft.getInstance().getEntityModels();

    public static final EntityRendererProvider.Context RENDER_CONTEXT = new EntityRendererProvider.Context(Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer(), Minecraft.getInstance().getBlockRenderer(), Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer(), Minecraft.getInstance().getResourceManager(), ENTITY_MODEL_SET, Minecraft.getInstance().font);

    public final PlayerEntityProperties properties;

    public final PlayerEntityModel playerModel;

    public PlayerEntityElementRenderer(boolean slim) {
        super(RENDER_CONTEXT, slim);
        this.properties = new PlayerEntityProperties(slim);
        this.playerModel = new PlayerEntityModel(RENDER_CONTEXT.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim, this.properties);
        this.m_115326_(new PlayerEntityShoulderParrotLayer(this, RENDER_CONTEXT.getModelSet(), this.properties));
        this.m_115326_(new PlayerEntityCapeLayer(this, this.properties));
    }

    public void renderPlayerEntityItem(double d11, double d12, double d13, float f11, float f12, PoseStack matrix, MultiBufferSource bufferSource, int i11) {
        try {
            Vec3 vec3 = this.getRenderOffset(null, f12);
            double d2 = d11 + vec3.x();
            double d3 = d12 + vec3.y();
            double d0 = d13 + vec3.z();
            matrix.pushPose();
            matrix.translate(d2, d3, d0);
            this.render(f11, f12, matrix, bufferSource, i11);
            matrix.translate(-vec3.x(), -vec3.y(), -vec3.z());
            matrix.popPose();
        } catch (Exception var19) {
            CrashReport crashreport = CrashReport.forThrowable(var19, "FancyMenu: Rendering player entity item");
            CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
            crashreportcategory1.setDetail("Rotation", f11);
            crashreportcategory1.setDetail("Delta", f12);
            throw new ReportedException(crashreport);
        }
    }

    protected void render(float f11, float f12, PoseStack matrix, MultiBufferSource bufferSource, int i11) {
        this.setModelProperties();
        this.innerRender(f11, f12, matrix, bufferSource, i11);
    }

    protected void innerRender(float f11, float f12, PoseStack matrix, MultiBufferSource bufferSource, int i11) {
        matrix.pushPose();
        boolean shouldSit = this.properties.shouldSit;
        this.playerModel.f_102609_ = shouldSit;
        this.playerModel.f_102610_ = this.properties.isBaby;
        float f = Mth.rotLerp(f12, this.properties.yBodyRotO, this.properties.yBodyRot);
        float f1 = Mth.rotLerp(f12, this.properties.yHeadRotO, this.properties.yHeadRot);
        float f2 = f1 - f;
        float f6 = Mth.lerp(f12, this.properties.xRotO, this.properties.xRot);
        if (this.properties.hasPose(Pose.SLEEPING)) {
            Direction direction = this.properties.getBedOrientation();
            if (direction != null) {
                float f4 = this.properties.getEyeHeight(Pose.STANDING) - 0.1F;
                matrix.translate((float) (-direction.getStepX()) * f4, 0.0F, (float) (-direction.getStepZ()) * f4);
            }
        }
        float f7 = f12;
        this.setupRotations(matrix, f12, f, f12);
        matrix.scale(-1.0F, -1.0F, 1.0F);
        this.scale(matrix, f12);
        matrix.translate(0.0F, -1.501F, 0.0F);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit) {
            f8 = Mth.lerp(f12, this.properties.animationSpeedOld, this.properties.animationSpeed);
            f5 = this.properties.animationPosition - this.properties.animationSpeed * (1.0F - f12);
            if (this.properties.isBaby) {
                f5 *= 3.0F;
            }
            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }
        this.playerModel.setupAnimWithoutEntity(f5, f8, f12, f2, f6);
        boolean visible = !this.properties.invisible;
        boolean flag1 = false;
        boolean glowing = this.properties.glowing;
        RenderType rendertype = this.getRenderType(null, visible, flag1, glowing);
        if (rendertype != null) {
            VertexConsumer vertexconsumer = bufferSource.getBuffer(rendertype);
            int i = OverlayTexture.pack(OverlayTexture.u(this.m_6931_(null, f12)), OverlayTexture.v(false));
            this.playerModel.m_7695_(matrix, vertexconsumer, i11, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
        }
        if (!this.properties.isSpectator()) {
            for (RenderLayer<?, ?> renderlayer : this.f_115291_) {
                if (renderlayer instanceof PlayerEntityRenderLayer pl) {
                    pl.m_6494_(matrix, bufferSource, i11, null, f5, f8, f12, f7, f2, f6);
                }
            }
        }
        matrix.popPose();
        if (this.properties.showDisplayName) {
            this.renderNameTag(null, this.properties.displayName, matrix, bufferSource, i11);
        }
    }

    protected void scale(PoseStack matrix, float f11) {
        float f = 0.9375F;
        matrix.scale(0.9375F, 0.9375F, 0.9375F);
    }

    private void setModelProperties() {
        PlayerEntityModel playermodel = this.playerModel;
        if (this.properties.isSpectator()) {
            playermodel.m_8009_(false);
            playermodel.f_102808_.visible = true;
            playermodel.f_102809_.visible = true;
        } else {
            playermodel.m_8009_(true);
            playermodel.f_102809_.visible = this.properties.isModelPartShown(PlayerModelPart.HAT);
            playermodel.f_103378_.visible = this.properties.isModelPartShown(PlayerModelPart.JACKET);
            playermodel.f_103376_.visible = this.properties.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
            playermodel.f_103377_.visible = this.properties.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
            playermodel.f_103374_.visible = this.properties.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            playermodel.f_103375_.visible = this.properties.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
            playermodel.f_102817_ = this.properties.isCrouching();
            HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(this.properties, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(this.properties, InteractionHand.OFF_HAND);
            if (humanoidmodel$armpose.isTwoHanded()) {
                humanoidmodel$armpose1 = this.properties.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
            }
            if (this.properties.getMainArm() == HumanoidArm.RIGHT) {
                playermodel.f_102816_ = humanoidmodel$armpose;
                playermodel.f_102815_ = humanoidmodel$armpose1;
            } else {
                playermodel.f_102816_ = humanoidmodel$armpose1;
                playermodel.f_102815_ = humanoidmodel$armpose;
            }
        }
    }

    private static HumanoidModel.ArmPose getArmPose(PlayerEntityProperties props, InteractionHand interactionHand) {
        ItemStack itemstack = props.getItemInHand(interactionHand);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (props.getUsedItemHand() == interactionHand && props.getUseItemRemainingTicks() > 0L) {
                UseAnim useanim = itemstack.getUseAnimation();
                if (useanim == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }
                if (useanim == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }
                if (useanim == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }
                if (useanim == UseAnim.CROSSBOW && interactionHand == props.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }
                if (useanim == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }
                if (useanim == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }
            } else if (itemstack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemstack)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }
            return HumanoidModel.ArmPose.ITEM;
        }
    }

    @Override
    public Vec3 getRenderOffset(@Nullable AbstractClientPlayer entity, float f11) {
        return this.properties.isCrouching() ? new Vec3(0.0, -0.125, 0.0) : Vec3.ZERO;
    }

    @Nullable
    protected RenderType getRenderType(@Nullable AbstractClientPlayer entity, boolean visible, boolean isVisibleToPlayer, boolean glowing) {
        ResourceLocation resourcelocation = this.getTextureLocation(entity);
        if (isVisibleToPlayer) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (visible) {
            return this.playerModel.m_103119_(resourcelocation);
        } else {
            return glowing ? RenderType.outline(resourcelocation) : null;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(@Nullable AbstractClientPlayer entity) {
        return this.properties.getSkinTextureLocation();
    }

    @Override
    protected void renderNameTag(@Nullable AbstractClientPlayer entity, Component content, PoseStack matrix, MultiBufferSource bufferSource, int int0) {
        boolean flag = !this.properties.isCrouching();
        float f = this.properties.getDimensions().height + 0.5F;
        int i = 0;
        matrix.pushPose();
        matrix.translate(0.0F, f, 0.0F);
        matrix.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = matrix.last().pose();
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int) (f1 * 255.0F) << 24;
        Font font = this.m_114481_();
        float f2 = (float) (-font.width(content) / 2);
        font.drawInBatch(content, f2, (float) i, 553648127, false, matrix4f, bufferSource, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, int0);
        if (flag) {
            font.drawInBatch(content, f2, (float) i, -1, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, int0);
        }
        matrix.popPose();
    }

    protected void setupRotations(PoseStack matrix, float f11, float f12, float f13) {
        if (!this.properties.hasPose(Pose.SLEEPING)) {
            matrix.mulPose(Axis.YP.rotationDegrees(180.0F - f12));
        }
    }
}