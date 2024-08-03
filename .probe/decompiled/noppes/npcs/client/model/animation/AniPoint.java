package noppes.npcs.client.model.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;

public class AniPoint implements AnimationBase {

    @Override
    public void animatePost(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, HumanoidModel model, int animationStart) {
        model.rightArm.xRot = -1.570796F;
        model.rightArm.yRot = netHeadYaw / (180.0F / (float) Math.PI);
        model.rightArm.zRot = 0.0F;
    }

    @Override
    public void animatePre(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, HumanoidModel model, int animationStart) {
    }
}