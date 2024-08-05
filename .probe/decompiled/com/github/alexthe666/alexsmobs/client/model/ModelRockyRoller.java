package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityRockyRoller;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class ModelRockyRoller extends AdvancedEntityModel<EntityRockyRoller> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox HSpikes_r1;

    private final AdvancedModelBox HSpikes_r2;

    private final AdvancedModelBox VSpikes_r1;

    private final AdvancedModelBox VSpikes_r2;

    private final AdvancedModelBox VSpikes_r3;

    private final AdvancedModelBox VSpikes_r4;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox head;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox right_leg;

    public ModelRockyRoller() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -16.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 29).addBox(-9.0F, -8.0F, -10.0F, 18.0F, 16.0F, 20.0F, 0.0F, false);
        this.body.setTextureOffset(64, 85).addBox(0.0F, 1.0F, -13.0F, 15.0F, 0.0F, 28.0F, 0.0F, false);
        this.body.setTextureOffset(64, 85).addBox(-15.0F, 1.0F, -13.0F, 15.0F, 0.0F, 28.0F, 0.0F, true);
        this.HSpikes_r1 = new AdvancedModelBox(this, "HSpikes_r1");
        this.HSpikes_r1.setRotationPoint(-0.5F, -4.0F, 1.0F);
        this.body.addChild(this.HSpikes_r1);
        this.setRotationAngle(this.HSpikes_r1, 0.0F, 0.0F, 0.0873F);
        this.HSpikes_r1.setTextureOffset(0, 0).addBox(-14.5F, 0.0F, -14.0F, 15.0F, 0.0F, 28.0F, 0.0F, true);
        this.HSpikes_r2 = new AdvancedModelBox(this, "HSpikes_r2");
        this.HSpikes_r2.setRotationPoint(0.5F, -4.0F, 1.0F);
        this.body.addChild(this.HSpikes_r2);
        this.setRotationAngle(this.HSpikes_r2, 0.0F, 0.0F, -0.0873F);
        this.HSpikes_r2.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -14.0F, 15.0F, 0.0F, 28.0F, 0.0F, false);
        this.VSpikes_r1 = new AdvancedModelBox(this, "VSpikes_r1");
        this.VSpikes_r1.setRotationPoint(-6.0F, 4.0F, 1.0F);
        this.body.addChild(this.VSpikes_r1);
        this.setRotationAngle(this.VSpikes_r1, 0.0F, 0.0F, -0.1745F);
        this.VSpikes_r1.setTextureOffset(1, 69).addBox(0.0F, -18.0F, -14.0F, 0.0F, 18.0F, 28.0F, 0.0F, false);
        this.VSpikes_r2 = new AdvancedModelBox(this, "VSpikes_r2");
        this.VSpikes_r2.setRotationPoint(6.0F, 4.0F, 1.0F);
        this.body.addChild(this.VSpikes_r2);
        this.setRotationAngle(this.VSpikes_r2, 0.0F, 0.0F, 0.1745F);
        this.VSpikes_r2.setTextureOffset(1, 69).addBox(0.0F, -18.0F, -14.0F, 0.0F, 18.0F, 28.0F, 0.0F, true);
        this.VSpikes_r3 = new AdvancedModelBox(this, "VSpikes_r3");
        this.VSpikes_r3.setRotationPoint(-2.0F, 2.0F, 1.0F);
        this.body.addChild(this.VSpikes_r3);
        this.setRotationAngle(this.VSpikes_r3, 0.0F, 0.0F, -0.0436F);
        this.VSpikes_r3.setTextureOffset(49, 38).addBox(0.0F, -16.0F, -14.0F, 0.0F, 18.0F, 28.0F, 0.0F, true);
        this.VSpikes_r4 = new AdvancedModelBox(this, "VSpikes_r4");
        this.VSpikes_r4.setRotationPoint(2.0F, 2.0F, 1.0F);
        this.body.addChild(this.VSpikes_r4);
        this.setRotationAngle(this.VSpikes_r4, 0.0F, 0.0F, 0.0436F);
        this.VSpikes_r4.setTextureOffset(49, 38).addBox(0.0F, -16.0F, -14.0F, 0.0F, 18.0F, 28.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 7.0F, 10.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, -0.6109F, 0.0F, 0.0F);
        this.tail.setTextureOffset(59, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 12.0F, 0.0F, false);
        this.tail.setTextureOffset(19, 8).addBox(0.0F, -4.0F, 8.0F, 0.0F, 2.0F, 4.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, 6.0F, -10.6F);
        this.body.addChild(this.head);
        this.setRotationAngle(this.head, 0.2618F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 0).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(21, 15).addBox(0.0F, -5.0F, -4.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(4.0F, 8.0F, -7.0F);
        this.body.addChild(this.left_arm);
        this.left_arm.setTextureOffset(0, 29).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);
        this.left_arm.setTextureOffset(18, 0).addBox(0.0F, 3.0F, 2.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-4.0F, 8.0F, -7.0F);
        this.body.addChild(this.right_arm);
        this.right_arm.setTextureOffset(0, 29).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 5.0F, 3.0F, 0.0F, true);
        this.right_arm.setTextureOffset(18, 0).addBox(-2.0F, 3.0F, 2.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(6.0F, 9.0F, 6.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(0, 12).addBox(-3.0F, -1.0F, -3.0F, 5.0F, 8.0F, 5.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-6.0F, 9.0F, 6.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(0, 12).addBox(-2.0F, -1.0F, -3.0F, 5.0F, 8.0F, 5.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.HSpikes_r1, this.HSpikes_r2, this.head, this.VSpikes_r1, this.VSpikes_r2, this.VSpikes_r3, this.VSpikes_r4, this.tail, this.left_arm, this.right_arm, new AdvancedModelBox[] { this.left_leg, this.right_leg });
    }

    public void setupAnim(EntityRockyRoller entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float rollProgress = entity.prevRollProgress + (entity.rollProgress - entity.prevRollProgress) * partialTick;
        float walkProgress = 5.0F - rollProgress;
        float walkSpeed = 1.2F;
        float walkDegree = 0.8F;
        float idleSpeed = 0.15F;
        float idleDegree = 0.3F;
        float rollDegree = 0.2F;
        float timeRolling = (float) entity.rollCounter + partialTick;
        this.progressPositionPrev(this.body, rollProgress, 0.0F, 6.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, walkProgress * limbSwingAmount, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, walkProgress * limbSwingAmount, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, walkProgress * limbSwingAmount, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.tail, walkProgress * limbSwingAmount, 0.0F, -1.0F, -1.0F, 5.0F);
        if (entity.isRolling()) {
            this.body.rotateAngleX = timeRolling * 0.2F * rollProgress * rollDegree;
            entity.clientRoll = this.body.rotateAngleX;
            this.bob(this.body, rollDegree, 10.0F, true, timeRolling, 0.2F * rollProgress);
        } else {
            float rollDeg = (float) Mth.wrapDegrees(Math.toDegrees((double) entity.clientRoll));
            this.body.rotateAngleX = rollProgress * 0.2F * Maths.rad((double) rollDeg);
        }
        this.swing(this.tail, idleSpeed, idleDegree, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.bob(this.head, idleSpeed * 0.5F, idleDegree * 1.5F, false, ageInTicks, 1.0F);
        this.bob(this.left_arm, idleSpeed * 0.25F, idleDegree * 2.0F, true, ageInTicks, 1.0F);
        this.bob(this.right_arm, idleSpeed * 0.25F, idleDegree * 2.0F, true, ageInTicks, 1.0F);
        this.walk(this.right_arm, idleSpeed, idleDegree, true, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.left_arm, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.right_leg, walkSpeed, walkDegree * 1.25F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
        this.walk(this.left_leg, walkSpeed, walkDegree * 1.25F, true, 0.0F, 0.2F, limbSwing, limbSwingAmount);
        this.bob(this.right_leg, walkSpeed, walkDegree * 3.0F, false, limbSwing, limbSwingAmount);
        this.bob(this.left_leg, -walkSpeed, walkDegree * 3.0F, false, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed * 0.8F, walkDegree, true, limbSwing, limbSwingAmount * walkProgress * 0.2F);
        this.walk(this.tail, walkSpeed, walkDegree * 0.35F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.head.rotateAngleY += netHeadYaw / (180.0F / (float) Math.PI);
        this.head.rotateAngleX += headPitch / (180.0F / (float) Math.PI) * 0.5F;
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}