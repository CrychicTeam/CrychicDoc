package lio.playeranimatorapi.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import lio.playeranimatorapi.data.PlayerParts;
import lio.playeranimatorapi.misc.PlayerModelInterface;
import lio.playeranimatorapi.playeranims.CustomModifierLayer;
import lio.playeranimatorapi.playeranims.PlayerAnimations;
import lio.playeranimatorapi.registry.PlayerEffectsRendererRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LivingEntityRenderer.class })
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow
    protected M model;

    @Inject(method = { "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V" }, at = { @At("TAIL") }, cancellable = true)
    private void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof Player) {
            CustomModifierLayer animationContainer = PlayerAnimations.getModifierLayer((AbstractClientPlayer) entity);
            PlayerModel playerModel = (PlayerModel) this.model;
            if (animationContainer != null && animationContainer.isActive()) {
                PlayerParts parts = animationContainer.data.parts();
                if (!parts.body.isVisible) {
                    ci.cancel();
                }
                playerModel.f_102808_.zigysPlayerAnimatorAPI$setIsVisible(parts.head.isVisible);
                playerModel.f_102810_.zigysPlayerAnimatorAPI$setIsVisible(parts.torso.isVisible);
                playerModel.f_102811_.zigysPlayerAnimatorAPI$setIsVisible(parts.rightArm.isVisible);
                playerModel.f_102812_.zigysPlayerAnimatorAPI$setIsVisible(parts.leftArm.isVisible);
                playerModel.f_102813_.zigysPlayerAnimatorAPI$setIsVisible(parts.rightLeg.isVisible);
                playerModel.f_102814_.zigysPlayerAnimatorAPI$setIsVisible(parts.leftLeg.isVisible);
                playerModel.f_102809_.zigysPlayerAnimatorAPI$setIsVisible(parts.head.isVisible);
                playerModel.jacket.zigysPlayerAnimatorAPI$setIsVisible(parts.torso.isVisible);
                playerModel.rightSleeve.zigysPlayerAnimatorAPI$setIsVisible(parts.rightArm.isVisible);
                playerModel.leftSleeve.zigysPlayerAnimatorAPI$setIsVisible(parts.leftArm.isVisible);
                playerModel.rightPants.zigysPlayerAnimatorAPI$setIsVisible(parts.rightLeg.isVisible);
                playerModel.leftPants.zigysPlayerAnimatorAPI$setIsVisible(parts.leftLeg.isVisible);
            } else {
                playerModel.f_102808_.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.f_102810_.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.f_102811_.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.f_102812_.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.f_102813_.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.f_102814_.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.f_102809_.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.jacket.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.rightSleeve.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.leftSleeve.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.rightPants.zigysPlayerAnimatorAPI$setIsVisible(true);
                playerModel.leftPants.zigysPlayerAnimatorAPI$setIsVisible(true);
            }
            for (EntityRenderer renderer : PlayerEffectsRendererRegistry.getRenderers()) {
                if (renderer instanceof PlayerModelInterface) {
                    ((PlayerModelInterface) renderer).setPlayerModel(playerModel);
                    renderer.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
                }
            }
        }
    }
}