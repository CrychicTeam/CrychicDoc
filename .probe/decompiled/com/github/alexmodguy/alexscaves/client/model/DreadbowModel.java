package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;

public class DreadbowModel extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox main;

    private final AdvancedModelBox bow;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox tArm;

    private final AdvancedModelBox bArm;

    private final AdvancedModelBox bowstring;

    private final AdvancedModelBox bString;

    private final AdvancedModelBox tString;

    public DreadbowModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.main = new AdvancedModelBox(this);
        this.main.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bow = new AdvancedModelBox(this);
        this.bow.setRotationPoint(0.0F, -16.0F, -6.5F);
        this.main.addChild(this.bow);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bow.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, -0.7854F, 0.0F, 0.0F);
        this.cube_r1.setTextureOffset(16, 0).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);
        this.tArm = new AdvancedModelBox(this);
        this.tArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bow.addChild(this.tArm);
        this.setRotateAngle(this.tArm, -0.7854F, 0.0F, 0.0F);
        this.tArm.setTextureOffset(0, 0).addBox(-0.5F, -18.0F, -8.0F, 1.0F, 18.0F, 14.0F, 0.0F, false);
        this.tArm.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -8.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);
        this.bArm = new AdvancedModelBox(this);
        this.bArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bow.addChild(this.bArm);
        this.setRotateAngle(this.bArm, 0.7854F, 0.0F, 0.0F);
        this.bArm.setTextureOffset(0, 32).addBox(-0.5F, 0.0F, -8.0F, 1.0F, 18.0F, 14.0F, 0.0F, true);
        this.bArm.setTextureOffset(0, -3).addBox(0.0F, -3.0F, -8.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);
        this.bowstring = new AdvancedModelBox(this);
        this.bowstring.setRotationPoint(0.0F, 8.0F, 3.0F);
        this.bString = new AdvancedModelBox(this);
        this.bString.setRotationPoint(0.0F, -0.5F, 0.0F);
        this.bowstring.addChild(this.bString);
        this.bString.setTextureOffset(30, 18).addBox(-0.01F, -0.25F, -0.5F, 0.0F, 14.0F, 1.0F, 0.0F, false);
        this.tString = new AdvancedModelBox(this);
        this.tString.setRotationPoint(0.0F, -0.25F, 0.0F);
        this.bowstring.addChild(this.tString);
        this.tString.setTextureOffset(30, 0).addBox(0.0F, -13.75F, -0.5F, 0.0F, 14.0F, 1.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public void setupAnim(Entity entity, float pullAmount, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.bowstring.rotationPointZ += pullAmount * 9.0F;
        this.tString.rotateAngleX = this.tString.rotateAngleX + (float) Math.toRadians((double) (pullAmount * 25.0F));
        this.bString.rotateAngleX = this.bString.rotateAngleX + (float) Math.toRadians((double) (pullAmount * -25.0F));
        this.tArm.rotateAngleX = this.tArm.rotateAngleX + (float) Math.toRadians((double) (pullAmount * -20.0F));
        this.bArm.rotateAngleX = this.bArm.rotateAngleX + (float) Math.toRadians((double) (pullAmount * 20.0F));
    }

    public void translateToBowString(PoseStack matrixStackIn) {
        this.bowstring.translateAndRotate(matrixStackIn);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.main, this.bowstring, this.bow, this.cube_r1, this.tArm, this.bArm, this.bString, this.tString);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main, this.bowstring);
    }
}