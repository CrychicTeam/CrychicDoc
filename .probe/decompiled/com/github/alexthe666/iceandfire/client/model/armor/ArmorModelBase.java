package com.github.alexthe666.iceandfire.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;

public class ArmorModelBase extends HumanoidModel<LivingEntity> {

    protected static float INNER_MODEL_OFFSET = 0.38F;

    protected static float OUTER_MODEL_OFFSET = 0.45F;

    public ArmorModelBase(ModelPart modelPart0) {
        super(modelPart0);
    }

    @Override
    public void setupAnim(@NotNull LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn instanceof ArmorStand armorStand) {
            this.f_102808_.xRot = (float) (Math.PI / 180.0) * armorStand.getHeadPose().getX();
            this.f_102808_.yRot = (float) (Math.PI / 180.0) * armorStand.getHeadPose().getY();
            this.f_102808_.zRot = (float) (Math.PI / 180.0) * armorStand.getHeadPose().getZ();
            this.f_102810_.xRot = (float) (Math.PI / 180.0) * armorStand.getBodyPose().getX();
            this.f_102810_.yRot = (float) (Math.PI / 180.0) * armorStand.getBodyPose().getY();
            this.f_102810_.zRot = (float) (Math.PI / 180.0) * armorStand.getBodyPose().getZ();
            this.f_102812_.xRot = (float) (Math.PI / 180.0) * armorStand.getLeftArmPose().getX();
            this.f_102812_.yRot = (float) (Math.PI / 180.0) * armorStand.getLeftArmPose().getY();
            this.f_102812_.zRot = (float) (Math.PI / 180.0) * armorStand.getLeftArmPose().getZ();
            this.f_102811_.xRot = (float) (Math.PI / 180.0) * armorStand.getRightArmPose().getX();
            this.f_102811_.yRot = (float) (Math.PI / 180.0) * armorStand.getRightArmPose().getY();
            this.f_102811_.zRot = (float) (Math.PI / 180.0) * armorStand.getRightArmPose().getZ();
            this.f_102814_.xRot = (float) (Math.PI / 180.0) * armorStand.getLeftLegPose().getX();
            this.f_102814_.yRot = (float) (Math.PI / 180.0) * armorStand.getLeftLegPose().getY();
            this.f_102814_.zRot = (float) (Math.PI / 180.0) * armorStand.getLeftLegPose().getZ();
            this.f_102813_.xRot = (float) (Math.PI / 180.0) * armorStand.getRightLegPose().getX();
            this.f_102813_.yRot = (float) (Math.PI / 180.0) * armorStand.getRightLegPose().getY();
            this.f_102813_.zRot = (float) (Math.PI / 180.0) * armorStand.getRightLegPose().getZ();
            this.f_102809_.copyFrom(this.f_102808_);
        } else {
            super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
}