package net.mehvahdjukaar.moonlight.core.mixins;

import net.mehvahdjukaar.moonlight.api.item.IThirdPersonAnimationProvider;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ HumanoidModel.class })
public abstract class ThirdPersonRendererMixin<T extends LivingEntity> extends AgeableListModel<T> {

    @Unique
    private boolean moonlight$isTwoHanded = false;

    @Inject(method = { "poseRightArm" }, at = { @At("HEAD") }, cancellable = true, require = 0)
    public void poseRightArm(T entity, CallbackInfo ci) {
        if (this.moonlight$isTwoHanded) {
            ci.cancel();
        }
        HumanoidArm handSide = entity.getMainArm();
        ItemStack stack = entity.getItemInHand(handSide == HumanoidArm.RIGHT ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
        IThirdPersonAnimationProvider provider = IThirdPersonAnimationProvider.get(stack.getItem());
        if (provider != null && provider.poseRightArm(stack, (HumanoidModel<T>) this, entity, handSide)) {
            if (provider.isTwoHanded()) {
                this.moonlight$isTwoHanded = true;
            }
            ci.cancel();
        }
    }

    @Inject(method = { "poseLeftArm" }, at = { @At("HEAD") }, cancellable = true, require = 0)
    public void poseLeftArm(T entity, CallbackInfo ci) {
        if (this.moonlight$isTwoHanded) {
            ci.cancel();
        }
        HumanoidArm handSide = entity.getMainArm();
        ItemStack stack = entity.getItemInHand(handSide == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        IThirdPersonAnimationProvider provider = IThirdPersonAnimationProvider.get(stack.getItem());
        if (provider != null && provider.poseLeftArm(stack, (HumanoidModel<T>) this, entity, handSide)) {
            if (provider.isTwoHanded()) {
                this.moonlight$isTwoHanded = true;
            }
            ci.cancel();
        }
    }

    @Inject(method = { "setupAnim*" }, at = { @At("RETURN") }, require = 0)
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        this.moonlight$isTwoHanded = false;
    }
}