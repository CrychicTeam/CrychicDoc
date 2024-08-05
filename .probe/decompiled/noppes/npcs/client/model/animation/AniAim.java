package noppes.npcs.client.model.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;

public class AniAim implements AnimationBase {

    @Override
    public void animatePre(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, HumanoidModel model, int animationStart) {
        model.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
    }

    @Override
    public void animatePost(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, HumanoidModel model, int animationStart) {
        model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
    }
}