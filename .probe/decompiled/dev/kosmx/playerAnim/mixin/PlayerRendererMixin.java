package dev.kosmx.playerAnim.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.kosmx.playerAnim.impl.IPlayerModel;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PlayerRenderer.class })
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = { "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V") })
    private void hideBonesInFirstPerson(AbstractClientPlayer entity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
        if (FirstPersonMode.isFirstPersonPass()) {
            AnimationApplier animationApplier = ((IAnimatedPlayer) entity).playerAnimator_getAnimation();
            FirstPersonConfiguration config = animationApplier.getFirstPersonConfiguration();
            if (entity == Minecraft.getInstance().getCameraEntity()) {
                this.setAllPartsVisible(false);
                boolean showRightArm = config.isShowRightArm();
                boolean showLeftArm = config.isShowLeftArm();
                ((PlayerModel) this.f_115290_).f_102811_.visible = showRightArm;
                ((PlayerModel) this.f_115290_).rightSleeve.visible = showRightArm;
                ((PlayerModel) this.f_115290_).f_102812_.visible = showLeftArm;
                ((PlayerModel) this.f_115290_).leftSleeve.visible = showLeftArm;
            }
        }
    }

    @Unique
    private void setAllPartsVisible(boolean visible) {
        ((PlayerModel) this.f_115290_).f_102808_.visible = visible;
        ((PlayerModel) this.f_115290_).f_102810_.visible = visible;
        ((PlayerModel) this.f_115290_).f_102814_.visible = visible;
        ((PlayerModel) this.f_115290_).f_102813_.visible = visible;
        ((PlayerModel) this.f_115290_).f_102811_.visible = visible;
        ((PlayerModel) this.f_115290_).f_102812_.visible = visible;
        ((PlayerModel) this.f_115290_).f_102809_.visible = visible;
        ((PlayerModel) this.f_115290_).leftSleeve.visible = visible;
        ((PlayerModel) this.f_115290_).rightSleeve.visible = visible;
        ((PlayerModel) this.f_115290_).leftPants.visible = visible;
        ((PlayerModel) this.f_115290_).rightPants.visible = visible;
        ((PlayerModel) this.f_115290_).jacket.visible = visible;
    }

    @Inject(method = { "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V" }, at = { @At("RETURN") })
    private void applyBodyTransforms(AbstractClientPlayer abstractClientPlayerEntity, PoseStack matrixStack, float f, float bodyYaw, float tickDelta, CallbackInfo ci) {
        AnimationApplier animationPlayer = ((IAnimatedPlayer) abstractClientPlayerEntity).playerAnimator_getAnimation();
        animationPlayer.setTickDelta(tickDelta);
        if (animationPlayer.isActive()) {
            Vec3f vec3d = animationPlayer.get3DTransform("body", TransformType.POSITION, Vec3f.ZERO);
            matrixStack.translate((double) vec3d.getX().floatValue(), (double) vec3d.getY().floatValue() + 0.7, (double) vec3d.getZ().floatValue());
            Vec3f vec3f = animationPlayer.get3DTransform("body", TransformType.ROTATION, Vec3f.ZERO);
            matrixStack.mulPose(Axis.ZP.rotation(vec3f.getZ()));
            matrixStack.mulPose(Axis.YP.rotation(vec3f.getY()));
            matrixStack.mulPose(Axis.XP.rotation(vec3f.getX()));
            matrixStack.translate(0.0, -0.7, 0.0);
        }
    }

    @Inject(method = { "renderHand" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/model/PlayerModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V") })
    private void notifyModelOfFirstPerson(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, ModelPart modelPart, ModelPart modelPart2, CallbackInfo ci) {
        if (this.m_7200_() instanceof IPlayerModel playerModel && !((IAnimatedPlayer) abstractClientPlayer).playerAnimator_getAnimation().getFirstPersonMode().isEnabled()) {
            playerModel.playerAnimator_prepForFirstPersonRender();
        }
    }
}