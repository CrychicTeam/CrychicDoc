package dev.xkmc.modulargolems.content.client.pose;

import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemModel;
import net.minecraft.util.Mth;

public class MetalGolemPose {

    public static final MetalGolemPose DEFAULT = new MetalGolemPose();

    public void attackModel(MetalGolemEntity entity, MetalGolemModel model, float atkTick) {
        model.rightArm.xRot = -2.0F + 1.5F * Mth.triangleWave(atkTick, 10.0F);
        model.leftArm.xRot = -2.0F + 1.5F * Mth.triangleWave(atkTick, 10.0F);
        model.rightForeArm.xRot = 0.0F;
        model.leftForeArm.xRot = 0.0F;
    }

    public void aggressive(MetalGolemEntity entity, MetalGolemModel model, float walkTick, float speed, float pTick) {
        this.walking(entity, model, walkTick, speed, pTick);
    }

    public void walking(MetalGolemEntity entity, MetalGolemModel model, float walkTick, float speed, float pTick) {
        model.rightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(walkTick, 13.0F)) * speed;
        model.leftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(walkTick, 13.0F)) * speed;
        model.rightForeArm.xRot = 0.0F;
        model.leftForeArm.xRot = 0.0F;
    }
}