package com.mna.entities.models;

import com.mna.entities.summon.GreaterAnimus;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.AbstractIllager;

public class GreaterAnimusModel extends HumanoidModel<GreaterAnimus> {

    public GreaterAnimusModel(ModelPart pRoot) {
        super(pRoot);
    }

    public void setupAnim(GreaterAnimus pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.f_102808_.yRot = pNetHeadYaw * (float) (Math.PI / 180.0);
        this.f_102808_.xRot = pHeadPitch * (float) (Math.PI / 180.0);
        if (this.f_102609_) {
            this.f_102811_.xRot = (float) (-Math.PI / 5);
            this.f_102811_.yRot = 0.0F;
            this.f_102811_.zRot = 0.0F;
            this.f_102812_.xRot = (float) (-Math.PI / 5);
            this.f_102812_.yRot = 0.0F;
            this.f_102812_.zRot = 0.0F;
            this.f_102813_.xRot = -1.4137167F;
            this.f_102813_.yRot = (float) (Math.PI / 10);
            this.f_102813_.zRot = 0.07853982F;
            this.f_102814_.xRot = -1.4137167F;
            this.f_102814_.yRot = (float) (-Math.PI / 10);
            this.f_102814_.zRot = -0.07853982F;
        } else {
            this.f_102811_.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 2.0F * pLimbSwingAmount * 0.5F;
            this.f_102811_.yRot = 0.0F;
            this.f_102811_.zRot = 0.0F;
            this.f_102812_.xRot = Mth.cos(pLimbSwing * 0.6662F) * 2.0F * pLimbSwingAmount * 0.5F;
            this.f_102812_.yRot = 0.0F;
            this.f_102812_.zRot = 0.0F;
            this.f_102813_.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount * 0.5F;
            this.f_102813_.yRot = 0.0F;
            this.f_102813_.zRot = 0.0F;
            this.f_102814_.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount * 0.5F;
            this.f_102814_.yRot = 0.0F;
            this.f_102814_.zRot = 0.0F;
        }
        AbstractIllager.IllagerArmPose abstractillager$illagerarmpose = pEntity.getArmPose();
        if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.ATTACKING) {
            if (pEntity.m_21205_().isEmpty()) {
                AnimationUtils.animateZombieArms(this.f_102812_, this.f_102811_, true, this.f_102608_, pAgeInTicks);
            } else {
                AnimationUtils.swingWeaponDown(this.f_102811_, this.f_102812_, pEntity, this.f_102608_, pAgeInTicks);
            }
        } else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.SPELLCASTING) {
            this.f_102811_.z = 0.0F;
            this.f_102811_.x = -5.0F;
            this.f_102812_.z = 0.0F;
            this.f_102812_.x = 5.0F;
            this.f_102811_.xRot = Mth.cos(pAgeInTicks * 0.6662F) * 0.25F;
            this.f_102812_.xRot = Mth.cos(pAgeInTicks * 0.6662F) * 0.25F;
            this.f_102811_.zRot = (float) (Math.PI * 3.0 / 4.0);
            this.f_102812_.zRot = (float) (-Math.PI * 3.0 / 4.0);
            this.f_102811_.yRot = 0.0F;
            this.f_102812_.yRot = 0.0F;
        } else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.BOW_AND_ARROW) {
            this.f_102811_.yRot = -0.1F + this.f_102808_.yRot;
            this.f_102811_.xRot = (float) (-Math.PI / 2) + this.f_102808_.xRot;
            this.f_102812_.xRot = -0.9424779F + this.f_102808_.xRot;
            this.f_102812_.yRot = this.f_102808_.yRot - 0.4F;
            this.f_102812_.zRot = (float) (Math.PI / 2);
        } else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.CROSSBOW_HOLD) {
            AnimationUtils.animateCrossbowHold(this.f_102811_, this.f_102812_, this.f_102808_, true);
        } else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE) {
            AnimationUtils.animateCrossbowCharge(this.f_102811_, this.f_102812_, pEntity, true);
        } else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.CELEBRATING) {
            this.f_102811_.z = 0.0F;
            this.f_102811_.x = -5.0F;
            this.f_102811_.xRot = Mth.cos(pAgeInTicks * 0.6662F) * 0.05F;
            this.f_102811_.zRot = 2.670354F;
            this.f_102811_.yRot = 0.0F;
            this.f_102812_.z = 0.0F;
            this.f_102812_.x = 5.0F;
            this.f_102812_.xRot = Mth.cos(pAgeInTicks * 0.6662F) * 0.05F;
            this.f_102812_.zRot = (float) (-Math.PI * 3.0 / 4.0);
            this.f_102812_.yRot = 0.0F;
        }
    }
}