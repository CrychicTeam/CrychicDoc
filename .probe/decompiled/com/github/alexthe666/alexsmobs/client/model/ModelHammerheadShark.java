package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityHammerheadShark;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelHammerheadShark extends AdvancedEntityModel<EntityHammerheadShark> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox main_body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox head_hammer;

    private final AdvancedModelBox finL;

    private final AdvancedModelBox finR;

    private final AdvancedModelBox topfin;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox tail_finL;

    private final AdvancedModelBox tail_finR;

    private final AdvancedModelBox topfintail;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox tail3;

    private final AdvancedModelBox tailbottomend;

    private final AdvancedModelBox tailtopend;

    public ModelHammerheadShark() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.main_body = new AdvancedModelBox(this, "main_body");
        this.main_body.setPos(0.0F, -6.0F, 0.0F);
        this.root.addChild(this.main_body);
        this.main_body.setTextureOffset(0, 0).addBox(-5.0F, -4.0F, -14.0F, 10.0F, 10.0F, 25.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -1.0F, -14.5F);
        this.main_body.addChild(this.head);
        this.head.setTextureOffset(40, 55).addBox(-4.0F, -1.0F, -6.5F, 8.0F, 7.0F, 7.0F, 0.0F, false);
        this.head_hammer = new AdvancedModelBox(this, "head_hammer");
        this.head_hammer.setPos(0.0F, 1.5F, -7.0F);
        this.head.addChild(this.head_hammer);
        this.head_hammer.setTextureOffset(32, 36).addBox(-11.0F, -1.5F, -3.5F, 22.0F, 3.0F, 7.0F, 0.0F, false);
        this.finL = new AdvancedModelBox(this, "finL");
        this.finL.setPos(6.0F, 6.0F, -6.5F);
        this.main_body.addChild(this.finL);
        this.setRotationAngle(this.finL, 0.0F, -0.2182F, 0.2618F);
        this.finL.setTextureOffset(47, 47).addBox(-1.0F, -1.0F, -1.0F, 14.0F, 1.0F, 6.0F, 0.0F, false);
        this.finR = new AdvancedModelBox(this, "finR");
        this.finR.setPos(-6.0F, 6.0F, -6.5F);
        this.main_body.addChild(this.finR);
        this.setRotationAngle(this.finR, 0.0F, 0.2182F, -0.2618F);
        this.finR.setTextureOffset(47, 47).addBox(-13.0F, -1.0F, -1.0F, 14.0F, 1.0F, 6.0F, 0.0F, true);
        this.topfin = new AdvancedModelBox(this, "topfin");
        this.topfin.setPos(0.0F, -4.0F, -3.5F);
        this.main_body.addChild(this.topfin);
        this.setRotationAngle(this.topfin, -0.2182F, 0.0F, 0.0F);
        this.topfin.setTextureOffset(0, 0).addBox(-1.0F, -13.0F, -2.0F, 2.0F, 14.0F, 7.0F, 0.0F, false);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setPos(0.0F, -0.3F, 11.75F);
        this.main_body.addChild(this.tail1);
        this.tail1.setTextureOffset(0, 36).addBox(-4.0F, -2.5F, -0.75F, 8.0F, 8.0F, 15.0F, 0.0F, false);
        this.tail_finL = new AdvancedModelBox(this, "tail_finL");
        this.tail_finL.setPos(3.0F, 5.3F, 5.75F);
        this.tail1.addChild(this.tail_finL);
        this.setRotationAngle(this.tail_finL, 0.0F, -0.48F, 1.0036F);
        this.tail_finL.setTextureOffset(64, 55).addBox(0.0F, -1.0F, 0.0F, 8.0F, 1.0F, 4.0F, 0.0F, false);
        this.tail_finR = new AdvancedModelBox(this, "tail_finR");
        this.tail_finR.setPos(-3.0F, 5.3F, 5.75F);
        this.tail1.addChild(this.tail_finR);
        this.setRotationAngle(this.tail_finR, 0.0F, 0.48F, -1.0036F);
        this.tail_finR.setTextureOffset(64, 55).addBox(-8.0F, -1.0F, 0.0F, 8.0F, 1.0F, 4.0F, 0.0F, true);
        this.topfintail = new AdvancedModelBox(this, "topfintail");
        this.topfintail.setPos(0.0F, -2.7F, 9.75F);
        this.tail1.addChild(this.topfintail);
        this.setRotationAngle(this.topfintail, -0.2182F, 0.0F, 0.0F);
        this.topfintail.setTextureOffset(0, 36).addBox(-0.5F, -4.0237F, -1.7836F, 1.0F, 5.0F, 4.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setPos(0.0F, 0.3F, 14.75F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(46, 0).addBox(-2.5F, -2.0F, -0.5F, 5.0F, 6.0F, 14.0F, 0.0F, false);
        this.tail3 = new AdvancedModelBox(this, "tail3");
        this.tail3.setPos(0.0F, 0.0F, 12.0F);
        this.tail2.addChild(this.tail3);
        this.tailbottomend = new AdvancedModelBox(this, "tailbottomend");
        this.tailbottomend.setPos(-0.5F, 1.5F, 0.0F);
        this.tail3.addChild(this.tailbottomend);
        this.setRotationAngle(this.tailbottomend, -2.7489F, 0.0F, 0.0F);
        this.tailbottomend.setTextureOffset(17, 60).addBox(0.0F, -11.5F, -2.5F, 1.0F, 14.0F, 6.0F, 0.0F, false);
        this.tailtopend = new AdvancedModelBox(this, "tailtopend");
        this.tailtopend.setPos(0.0F, -0.5F, 0.0F);
        this.tail3.addChild(this.tailtopend);
        this.setRotationAngle(this.tailtopend, -0.829F, 0.0F, 0.0F);
        this.tailtopend.setTextureOffset(0, 60).addBox(-1.0F, -16.5F, -1.5F, 2.0F, 19.0F, 6.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityHammerheadShark entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.main_body, this.tail1, this.tail2, this.tail3 };
        float swimSpeed = 0.4F;
        float swimDegree = 0.5F;
        this.chainSwing(tailBoxes, swimSpeed, swimDegree * 0.9F, -2.0, limbSwing, limbSwingAmount);
        this.bob(this.main_body, swimSpeed * 0.5F, swimDegree * 5.0F, false, limbSwing, limbSwingAmount);
        this.walk(this.topfin, swimSpeed, swimDegree * 0.1F, true, 1.0F, 0.2F, limbSwing, limbSwingAmount);
        this.walk(this.tail_finL, swimSpeed, swimDegree * 0.2F, true, 2.0F, 0.2F, limbSwing, limbSwingAmount);
        this.walk(this.tail_finR, swimSpeed, swimDegree * 0.2F, true, 2.0F, 0.2F, limbSwing, limbSwingAmount);
        this.flap(this.finL, swimSpeed, swimDegree * 0.6F, false, 1.0F, 0.1F, limbSwing, limbSwingAmount);
        this.flap(this.finR, swimSpeed, swimDegree * 0.6F, true, 1.0F, 0.1F, limbSwing, limbSwingAmount);
        this.swing(this.tail_finL, swimSpeed, swimDegree * 0.1F, false, 3.0F, -0.1F, limbSwing, limbSwingAmount);
        this.swing(this.tail_finR, swimSpeed, swimDegree * 0.1F, true, 3.0F, -0.1F, limbSwing, limbSwingAmount);
        this.swing(this.head, swimSpeed, swimDegree * 0.2F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.main_body, this.head, this.head_hammer, this.finL, this.finR, this.tail1, this.tail2, this.tail3, this.tail_finL, this.tail_finR, this.tailbottomend, new AdvancedModelBox[] { this.tailtopend, this.topfintail, this.topfin });
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}