package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityLobster;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;

public class ModelLobster extends AdvancedEntityModel<EntityLobster> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox antenna_left;

    private final AdvancedModelBox antenna_right;

    private final AdvancedModelBox arm_left;

    private final AdvancedModelBox hand_left;

    private final AdvancedModelBox arm_right;

    private final AdvancedModelBox hand_right;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox legs_left;

    private final AdvancedModelBox legs_right;

    public ModelLobster() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -1.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 11).addBox(-2.0F, -1.4F, -7.0F, 4.0F, 2.0F, 7.0F, 0.0F, false);
        this.antenna_left = new AdvancedModelBox(this, "antenna_left");
        this.antenna_left.setPos(1.4F, -0.5F, -7.0F);
        this.body.addChild(this.antenna_left);
        this.setRotationAngle(this.antenna_left, -0.3054F, -0.4363F, 0.0F);
        this.antenna_left.setTextureOffset(18, 18).addBox(0.0F, -0.9F, -5.0F, 0.0F, 1.0F, 5.0F, 0.0F, false);
        this.antenna_right = new AdvancedModelBox(this, "antenna_right");
        this.antenna_right.setPos(-1.4F, -0.5F, -7.0F);
        this.body.addChild(this.antenna_right);
        this.setRotationAngle(this.antenna_right, -0.3054F, 0.4363F, 0.0F);
        this.antenna_right.setTextureOffset(18, 20).addBox(0.0F, -0.9F, -5.0F, 0.0F, 1.0F, 5.0F, 0.0F, true);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setPos(1.7F, -0.4F, -5.0F);
        this.body.addChild(this.arm_left);
        this.setRotationAngle(this.arm_left, 0.0F, 0.6981F, 0.3491F);
        this.arm_left.setTextureOffset(15, 5).addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, 0.0F, false);
        this.hand_left = new AdvancedModelBox(this, "hand_left");
        this.hand_left.setPos(4.0F, 0.4F, 0.4F);
        this.arm_left.addChild(this.hand_left);
        this.setRotationAngle(this.hand_left, 0.0F, 0.6981F, 0.0F);
        this.hand_left.setTextureOffset(0, 21).addBox(0.0F, -1.0F, -1.9F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setPos(-1.7F, -0.4F, -5.0F);
        this.body.addChild(this.arm_right);
        this.setRotationAngle(this.arm_right, 0.0F, -0.6981F, -0.3491F);
        this.arm_right.setTextureOffset(15, 6).addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, 0.0F, true);
        this.hand_right = new AdvancedModelBox(this, "hand_right");
        this.hand_right.setPos(-4.0F, 0.4F, 0.4F);
        this.arm_right.addChild(this.hand_right);
        this.setRotationAngle(this.hand_right, 0.0F, -0.6981F, 0.0F);
        this.hand_right.setTextureOffset(0, 25).addBox(-3.0F, -1.0F, -1.9F, 3.0F, 1.0F, 2.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, -0.4F, 0.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 0).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 8.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setPos(0.0F, 0.0F, 6.0F);
        this.tail.addChild(this.tail2);
        this.tail2.setTextureOffset(15, 0).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 4.0F, 0.0F, false);
        this.legs_left = new AdvancedModelBox(this, "legs_left");
        this.legs_left.setPos(2.0F, 0.1F, -1.45F);
        this.body.addChild(this.legs_left);
        this.setRotationAngle(this.legs_left, 0.0F, 0.0F, 0.3054F);
        this.legs_left.setTextureOffset(16, 11).addBox(0.0F, 0.0F, -3.55F, 3.0F, 0.0F, 5.0F, 0.0F, false);
        this.legs_right = new AdvancedModelBox(this, "legs_right");
        this.legs_right.setPos(-2.0F, 0.1F, -1.45F);
        this.body.addChild(this.legs_right);
        this.setRotationAngle(this.legs_right, 0.0F, 0.0F, -0.3054F);
        this.legs_right.setTextureOffset(25, 11).addBox(-3.0F, 0.0F, -3.55F, 3.0F, 0.0F, 5.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityLobster entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.1F;
        float idleDegree = 0.3F;
        float walkSpeed = 3.0F;
        float walkDegree = 0.6F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float attackProgress = entityIn.prevAttackProgress + (entityIn.attackProgress - entityIn.prevAttackProgress) * partialTick;
        this.progressRotationPrev(this.arm_left, attackProgress, 0.0F, Maths.rad(45.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_right, attackProgress, 0.0F, Maths.rad(-45.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.hand_left, attackProgress, 0.0F, Maths.rad(-30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.hand_right, attackProgress, 0.0F, Maths.rad(30.0), 0.0F, 5.0F);
        this.walk(this.antenna_left, idleSpeed * 1.5F, idleDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.antenna_right, idleSpeed * 1.5F, idleDegree, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.tail, idleSpeed, idleDegree * 0.2F, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.tail2, idleSpeed, idleDegree * 0.15F, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.legs_left, walkSpeed, walkDegree * 0.8F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.legs_left, walkSpeed, walkDegree * 1.0F, false, 1.0F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.legs_right, walkSpeed, walkDegree * 0.8F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.legs_right, walkSpeed, walkDegree * 1.0F, true, 1.0F, 0.1F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed * 0.5F, walkDegree * 4.0F, true, limbSwing, limbSwingAmount);
        this.swing(this.arm_left, walkSpeed, walkDegree * 1.0F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.arm_right, walkSpeed, walkDegree * 1.0F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.antenna_left, this.antenna_right, this.arm_left, this.arm_right, this.hand_left, this.hand_right, this.tail, this.tail2, this.legs_left, this.legs_right, new AdvancedModelBox[0]);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}