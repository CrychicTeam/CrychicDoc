package de.keksuccino.fancymenu.customization.element.elements.playerentity.model;

import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinPlayerModel;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;

public class PlayerEntityModel extends PlayerModel {

    public final PlayerEntityProperties properties;

    public PlayerEntityModel(ModelPart modelPart, boolean slim, PlayerEntityProperties properties) {
        super(modelPart, slim);
        this.properties = properties;
    }

    public void setupAnimWithoutEntity(float animationSpeed, float animationSpeedOld, float someFloatThatsAlways1, float headRotY, float headRotX) {
        this.setupAnimRaw(animationSpeed, animationSpeedOld, someFloatThatsAlways1, headRotY, headRotX);
        this.f_103376_.copyFrom(this.f_102814_);
        this.f_103377_.copyFrom(this.f_102813_);
        this.f_103374_.copyFrom(this.f_102812_);
        this.f_103375_.copyFrom(this.f_102811_);
        this.f_103378_.copyFrom(this.f_102810_);
        if (this.properties.isCrouching()) {
            ((IMixinPlayerModel) this).getCloakFancyMenu().z = 1.4F;
            ((IMixinPlayerModel) this).getCloakFancyMenu().y = 1.85F;
        } else {
            ((IMixinPlayerModel) this).getCloakFancyMenu().z = 0.0F;
            ((IMixinPlayerModel) this).getCloakFancyMenu().y = 0.0F;
        }
    }

    protected void setupAnimRaw(float animationSpeed, float animationSpeedOld, float someFloatThatsAlways1, float headRotY, float headRotX) {
        this.f_102810_.yRot = 0.0F;
        this.f_102811_.z = 0.0F;
        this.f_102811_.x = -5.0F;
        this.f_102812_.z = 0.0F;
        this.f_102812_.x = 5.0F;
        float f = 1.0F;
        this.f_102808_.yRot = headRotY * (float) (Math.PI / 180.0);
        this.f_102808_.xRot = headRotX * (float) (Math.PI / 180.0);
        this.f_102808_.zRot = this.properties.headZRot * (float) (Math.PI / 180.0);
        this.f_102812_.xRot = this.properties.leftArmXRot * (float) (Math.PI / 180.0);
        this.f_102812_.yRot = this.properties.leftArmYRot * (float) (Math.PI / 180.0);
        this.f_102812_.zRot = this.properties.leftArmZRot * (float) (Math.PI / 180.0);
        this.f_102811_.xRot = this.properties.rightArmXRot * (float) (Math.PI / 180.0);
        this.f_102811_.yRot = this.properties.rightArmYRot * (float) (Math.PI / 180.0);
        this.f_102811_.zRot = this.properties.rightArmZRot * (float) (Math.PI / 180.0);
        this.f_102814_.xRot = this.properties.leftLegXRot * (float) (Math.PI / 180.0);
        this.f_102814_.yRot = this.properties.leftLegYRot * (float) (Math.PI / 180.0);
        this.f_102814_.zRot = this.properties.leftLegZRot * (float) (Math.PI / 180.0);
        this.f_102813_.xRot = this.properties.rightLegXRot * (float) (Math.PI / 180.0);
        this.f_102813_.yRot = this.properties.rightLegYRot * (float) (Math.PI / 180.0);
        this.f_102813_.zRot = this.properties.rightLegZRot * (float) (Math.PI / 180.0);
        if (this.f_102817_) {
            this.f_102810_.xRot = 0.5F;
            this.f_102811_.xRot += 0.4F;
            this.f_102812_.xRot += 0.4F;
            this.f_102813_.z = 4.0F;
            this.f_102814_.z = 4.0F;
            this.f_102813_.y = 12.2F;
            this.f_102814_.y = 12.2F;
            this.f_102808_.y = 4.2F;
            this.f_102810_.y = 3.2F;
            this.f_102812_.y = 5.2F;
            this.f_102811_.y = 5.2F;
        } else {
            this.f_102810_.xRot = 0.0F;
            this.f_102813_.z = 0.1F;
            this.f_102814_.z = 0.1F;
            this.f_102813_.y = 12.0F;
            this.f_102814_.y = 12.0F;
            this.f_102808_.y = 0.0F;
            this.f_102810_.y = 0.0F;
            this.f_102812_.y = 2.0F;
            this.f_102811_.y = 2.0F;
        }
        if (this.f_102816_ != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.f_102811_, someFloatThatsAlways1, 1.0F);
        }
        if (this.f_102815_ != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.f_102812_, someFloatThatsAlways1, -1.0F);
        }
        this.f_102809_.copyFrom(this.f_102808_);
    }
}