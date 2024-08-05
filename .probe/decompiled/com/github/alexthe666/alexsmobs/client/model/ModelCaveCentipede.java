package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCentipedeBody;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;

public class ModelCaveCentipede<T extends LivingEntity> extends AdvancedEntityModel<T> {

    private final int type;

    private final AdvancedModelBox root;

    private AdvancedModelBox body;

    private AdvancedModelBox leftLegBodyF;

    private AdvancedModelBox leftLeg2BodyF;

    private AdvancedModelBox rightLegBodyF;

    private AdvancedModelBox rightLegBodyF2;

    private AdvancedModelBox leftLegBodyB;

    private AdvancedModelBox leftLeg2BodyB;

    private AdvancedModelBox rightLegBodyB;

    private AdvancedModelBox rightLegBodyB2;

    private AdvancedModelBox tail;

    private AdvancedModelBox leftLegTailF;

    private AdvancedModelBox leftLeg2TailF;

    private AdvancedModelBox rightLegTailF;

    private AdvancedModelBox rightLeg2TailF;

    private AdvancedModelBox leftLegTailB;

    private AdvancedModelBox leftLegTailB2;

    private AdvancedModelBox rightLegTailB;

    private AdvancedModelBox rightLegTailB2;

    private AdvancedModelBox leftTail;

    private AdvancedModelBox leftTailEnd;

    private AdvancedModelBox rightTail;

    private AdvancedModelBox rightTailEnd;

    private AdvancedModelBox head;

    private AdvancedModelBox head2;

    private AdvancedModelBox fangs;

    private AdvancedModelBox antenna_left;

    private AdvancedModelBox antenna_left_r1;

    private AdvancedModelBox antenna_right;

    private AdvancedModelBox antenna_right_r1;

    public ModelCaveCentipede(int type) {
        this.texWidth = 128;
        this.texHeight = 128;
        this.type = type;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 21.0F);
        switch(type) {
            case 0:
                this.head = new AdvancedModelBox(this, "        head");
                this.head.setRotationPoint(0.0F, -7.875F, -20.625F);
                this.root.addChild(this.head);
                this.head.setTextureOffset(0, 62).addBox(-7.0F, -3.125F, -5.375F, 14.0F, 7.0F, 13.0F, 0.0F, false);
                this.head2 = new AdvancedModelBox(this, "        head2");
                this.head2.setRotationPoint(0.0F, -2.125F, -6.375F);
                this.head.addChild(this.head2);
                this.head2.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);
                this.antenna_left = new AdvancedModelBox(this, "        antenna_left");
                this.antenna_left.setRotationPoint(1.2F, -2.125F, -5.775F);
                this.head.addChild(this.antenna_left);
                this.setRotationAngle(this.antenna_left, -0.2618F, 0.48F, -0.2618F);
                this.antenna_left_r1 = new AdvancedModelBox(this, "        antenna_left_r1");
                this.antenna_left_r1.setRotationPoint(0.5F, 0.0F, 0.0F);
                this.antenna_left.addChild(this.antenna_left_r1);
                this.setRotationAngle(this.antenna_left_r1, 0.1309F, 0.0F, 0.0873F);
                this.antenna_left_r1.setTextureOffset(55, 17).addBox(-1.0F, 0.0F, -1.0F, 23.0F, 0.0F, 10.0F, 0.0F, false);
                this.antenna_right = new AdvancedModelBox(this, "        antenna_right");
                this.antenna_right.setRotationPoint(-1.2F, -2.125F, -5.775F);
                this.head.addChild(this.antenna_right);
                this.setRotationAngle(this.antenna_right, -0.2618F, -0.48F, 0.2618F);
                this.antenna_right_r1 = new AdvancedModelBox(this, "        antenna_right_r1");
                this.antenna_right_r1.setRotationPoint(-0.5F, 0.0F, 0.0F);
                this.antenna_right.addChild(this.antenna_right_r1);
                this.setRotationAngle(this.antenna_right_r1, 0.1309F, 0.0F, -0.0873F);
                this.antenna_right_r1.setTextureOffset(55, 17).addBox(-22.0F, 0.0F, -1.0F, 23.0F, 0.0F, 10.0F, 0.0F, true);
                this.fangs = new AdvancedModelBox(this, "        fangs");
                this.fangs.setRotationPoint(0.0F, 1.875F, -6.375F);
                this.head.addChild(this.fangs);
                this.fangs.setTextureOffset(62, 28).addBox(-7.0F, 0.0F, -5.0F, 14.0F, 0.0F, 6.0F, 0.0F, false);
                break;
            case 1:
                this.body = new AdvancedModelBox(this, "        body");
                this.body.setRotationPoint(0.0F, -7.6F, -21.0F);
                this.root.addChild(this.body);
                this.body.setTextureOffset(0, 0).addBox(-8.0F, -5.4F, -8.0F, 16.0F, 10.0F, 16.0F, 0.0F, false);
                this.leftLegBodyF = new AdvancedModelBox(this, "        leftLegBodyF");
                this.leftLegBodyF.setRotationPoint(7.6F, 3.6F, 5.0F);
                this.body.addChild(this.leftLegBodyF);
                this.setRotationAngle(this.leftLegBodyF, 0.0F, 0.0F, -0.5672F);
                this.leftLegBodyF.setTextureOffset(42, 62).addBox(0.0F, -2.0F, -1.0F, 10.0F, 3.0F, 2.0F, 0.0F, false);
                this.leftLeg2BodyF = new AdvancedModelBox(this, "        leftLeg2BodyF");
                this.leftLeg2BodyF.setRotationPoint(9.1F, 0.5F, 0.1F);
                this.leftLegBodyF.addChild(this.leftLeg2BodyF);
                this.setRotationAngle(this.leftLeg2BodyF, 0.0F, 0.0F, 1.4835F);
                this.leftLeg2BodyF.setTextureOffset(0, 53).addBox(-5.0F, -4.0F, -0.1F, 15.0F, 6.0F, 0.0F, 0.0F, false);
                this.rightLegBodyF = new AdvancedModelBox(this, "        rightLegBodyF");
                this.rightLegBodyF.setRotationPoint(-7.6F, 3.6F, 5.0F);
                this.body.addChild(this.rightLegBodyF);
                this.setRotationAngle(this.rightLegBodyF, 0.0F, 0.0F, 0.5672F);
                this.rightLegBodyF.setTextureOffset(42, 62).addBox(-10.0F, -2.0F, -1.0F, 10.0F, 3.0F, 2.0F, 0.0F, true);
                this.rightLegBodyF2 = new AdvancedModelBox(this, "        rightLegBodyF2");
                this.rightLegBodyF2.setRotationPoint(-9.1F, 0.5F, 0.1F);
                this.rightLegBodyF.addChild(this.rightLegBodyF2);
                this.setRotationAngle(this.rightLegBodyF2, 0.0F, 0.0F, -1.4835F);
                this.rightLegBodyF2.setTextureOffset(0, 53).addBox(-10.0F, -4.0F, -0.1F, 15.0F, 6.0F, 0.0F, 0.0F, true);
                this.leftLegBodyB = new AdvancedModelBox(this, "        leftLegBodyB");
                this.leftLegBodyB.setRotationPoint(7.6F, 3.6F, -5.0F);
                this.body.addChild(this.leftLegBodyB);
                this.setRotationAngle(this.leftLegBodyB, 0.0F, 0.0F, -0.5672F);
                this.leftLegBodyB.setTextureOffset(42, 62).addBox(0.0F, -2.0F, -1.0F, 10.0F, 3.0F, 2.0F, 0.0F, false);
                this.leftLeg2BodyB = new AdvancedModelBox(this, "        leftLeg2BodyB");
                this.leftLeg2BodyB.setRotationPoint(9.1F, 0.5F, 0.1F);
                this.leftLegBodyB.addChild(this.leftLeg2BodyB);
                this.setRotationAngle(this.leftLeg2BodyB, 0.0F, 0.0F, 1.4835F);
                this.leftLeg2BodyB.setTextureOffset(0, 53).addBox(-5.0F, -4.0F, -0.1F, 15.0F, 6.0F, 0.0F, 0.0F, false);
                this.rightLegBodyB = new AdvancedModelBox(this, "        rightLegBodyB");
                this.rightLegBodyB.setRotationPoint(-7.6F, 3.6F, -5.0F);
                this.body.addChild(this.rightLegBodyB);
                this.setRotationAngle(this.rightLegBodyB, 0.0F, 0.0F, 0.5672F);
                this.rightLegBodyB.setTextureOffset(42, 62).addBox(-10.0F, -2.0F, -1.0F, 10.0F, 3.0F, 2.0F, 0.0F, true);
                this.rightLegBodyB2 = new AdvancedModelBox(this, "        rightLegBodyB2");
                this.rightLegBodyB2.setRotationPoint(-9.1F, 0.5F, 0.1F);
                this.rightLegBodyB.addChild(this.rightLegBodyB2);
                this.setRotationAngle(this.rightLegBodyB2, 0.0F, 0.0F, -1.4835F);
                this.rightLegBodyB2.setTextureOffset(0, 53).addBox(-10.0F, -4.0F, -0.1F, 15.0F, 6.0F, 0.0F, 0.0F, true);
                break;
            case 2:
                this.tail = new AdvancedModelBox(this, "        tail");
                this.tail.setRotationPoint(0.0F, -7.6F, -21.0F);
                this.root.addChild(this.tail);
                this.tail.setTextureOffset(0, 27).addBox(-7.0F, -4.2F, -8.0F, 14.0F, 9.0F, 16.0F, 0.0F, false);
                this.leftLegTailF = new AdvancedModelBox(this, "        leftLegTailF");
                this.leftLegTailF.setRotationPoint(6.6F, 3.6F, -5.0F);
                this.tail.addChild(this.leftLegTailF);
                this.setRotationAngle(this.leftLegTailF, 0.2269F, -0.1833F, -0.5585F);
                this.leftLegTailF.setTextureOffset(42, 62).addBox(0.0F, -2.0F, -1.0F, 10.0F, 3.0F, 2.0F, 0.0F, false);
                this.leftLeg2TailF = new AdvancedModelBox(this, "        leftLeg2TailF");
                this.leftLeg2TailF.setRotationPoint(9.1F, 0.5F, 0.1F);
                this.leftLegTailF.addChild(this.leftLeg2TailF);
                this.setRotationAngle(this.leftLeg2TailF, 0.0F, 0.0F, 1.4835F);
                this.leftLeg2TailF.setTextureOffset(0, 53).addBox(-5.0F, -4.0F, 0.0F, 15.0F, 6.0F, 0.0F, 0.0F, false);
                this.rightLegTailF = new AdvancedModelBox(this, "        rightLegTailF");
                this.rightLegTailF.setRotationPoint(-6.6F, 3.6F, -5.0F);
                this.tail.addChild(this.rightLegTailF);
                this.setRotationAngle(this.rightLegTailF, 0.2269F, 0.1833F, 0.5585F);
                this.rightLegTailF.setTextureOffset(42, 62).addBox(-10.0F, -2.0F, -1.0F, 10.0F, 3.0F, 2.0F, 0.0F, true);
                this.rightLeg2TailF = new AdvancedModelBox(this, "        rightLeg2TailF");
                this.rightLeg2TailF.setRotationPoint(-9.1F, 0.5F, 0.1F);
                this.rightLegTailF.addChild(this.rightLeg2TailF);
                this.setRotationAngle(this.rightLeg2TailF, 0.0F, 0.0F, -1.4835F);
                this.rightLeg2TailF.setTextureOffset(0, 53).addBox(-10.0F, -4.0F, 0.0F, 15.0F, 6.0F, 0.0F, 0.0F, true);
                this.leftLegTailB = new AdvancedModelBox(this, "        leftLegTailB");
                this.leftLegTailB.setRotationPoint(6.6F, 3.6F, 4.0F);
                this.tail.addChild(this.leftLegTailB);
                this.setRotationAngle(this.leftLegTailB, 0.4977F, -0.6749F, -0.7314F);
                this.leftLegTailB.setTextureOffset(42, 62).addBox(0.0F, -2.0F, -1.0F, 10.0F, 3.0F, 2.0F, 0.0F, false);
                this.leftLegTailB2 = new AdvancedModelBox(this, "        leftLegTailB2");
                this.leftLegTailB2.setRotationPoint(9.1F, 0.5F, 0.1F);
                this.leftLegTailB.addChild(this.leftLegTailB2);
                this.setRotationAngle(this.leftLegTailB2, 0.0F, 0.0F, 1.4835F);
                this.leftLegTailB2.setTextureOffset(0, 53).addBox(-5.0F, -4.0F, 0.0F, 15.0F, 6.0F, 0.0F, 0.0F, false);
                this.rightLegTailB = new AdvancedModelBox(this, "        rightLegTailB");
                this.rightLegTailB.setRotationPoint(-6.6F, 3.6F, 4.0F);
                this.tail.addChild(this.rightLegTailB);
                this.setRotationAngle(this.rightLegTailB, 0.4977F, 0.6749F, 0.7314F);
                this.rightLegTailB.setTextureOffset(42, 62).addBox(-10.0F, -2.0F, -1.0F, 10.0F, 3.0F, 2.0F, 0.0F, true);
                this.rightLegTailB2 = new AdvancedModelBox(this, "        rightLegTailB2");
                this.rightLegTailB2.setRotationPoint(-9.1F, 0.5F, 0.1F);
                this.rightLegTailB.addChild(this.rightLegTailB2);
                this.setRotationAngle(this.rightLegTailB2, 0.0F, 0.0F, -1.4835F);
                this.rightLegTailB2.setTextureOffset(0, 53).addBox(-10.0F, -4.0F, 0.0F, 15.0F, 6.0F, 0.0F, 0.0F, true);
                this.leftTail = new AdvancedModelBox(this, "        leftTail");
                this.leftTail.setRotationPoint(2.5F, -0.1F, 8.0F);
                this.tail.addChild(this.leftTail);
                this.setRotationAngle(this.leftTail, 0.3054F, 0.3927F, 0.0F);
                this.leftTail.setTextureOffset(62, 35).addBox(-0.5F, -1.1F, -1.0F, 2.0F, 3.0F, 12.0F, 0.0F, false);
                this.leftTailEnd = new AdvancedModelBox(this, "        leftTailEnd");
                this.leftTailEnd.setRotationPoint(0.0F, 0.0F, 11.0F);
                this.leftTail.addChild(this.leftTailEnd);
                this.setRotationAngle(this.leftTailEnd, -0.5672F, 0.0F, 0.0F);
                this.leftTailEnd.setTextureOffset(38, 30).addBox(0.5F, -1.1F, -1.0F, 0.0F, 8.0F, 23.0F, 0.0F, false);
                this.rightTail = new AdvancedModelBox(this, "        rightTail");
                this.rightTail.setRotationPoint(-2.5F, -0.1F, 8.0F);
                this.tail.addChild(this.rightTail);
                this.setRotationAngle(this.rightTail, 0.3054F, -0.3927F, 0.0F);
                this.rightTail.setTextureOffset(62, 35).addBox(-1.5F, -1.1F, -1.0F, 2.0F, 3.0F, 12.0F, 0.0F, true);
                this.rightTailEnd = new AdvancedModelBox(this, "        rightTailEnd");
                this.rightTailEnd.setRotationPoint(0.0F, 0.0F, 11.0F);
                this.rightTail.addChild(this.rightTailEnd);
                this.setRotationAngle(this.rightTailEnd, -0.5672F, 0.0F, 0.0F);
                this.rightTailEnd.setTextureOffset(38, 30).addBox(-0.5F, -1.1F, -1.0F, 0.0F, 8.0F, 23.0F, 0.0F, true);
        }
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float walkSpeed = 1.5F;
        float walkDegree = 0.85F;
        float idleSpeed = 0.25F;
        float idleDegree = 0.35F;
        if (entity.deathTime > 0) {
            limbSwing = ageInTicks;
            limbSwingAmount = 1.0F;
        }
        if (this.type == 0) {
            this.swing(this.antenna_left, idleSpeed, idleDegree, true, 1.0F, -0.1F, ageInTicks, 1.0F);
            this.swing(this.antenna_right, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
            this.swing(this.fangs, idleSpeed, idleDegree * 0.1F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.fangs.rotationPointZ = -6.2F;
        } else if (this.type == 1) {
            if (entity instanceof EntityCentipedeBody) {
                float offset = (float) ((double) (((EntityCentipedeBody) entity).getBodyIndex() + 1) * Math.PI * 0.5);
                double walkOffset = (double) offset * Math.PI * 0.5;
                this.swing(this.leftLegBodyF, walkSpeed, walkDegree, true, offset, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.leftLeg2BodyF, walkSpeed, walkDegree * 0.5F, true, offset, 0.1F, limbSwing, limbSwingAmount);
                this.swing(this.leftLegBodyB, walkSpeed, walkDegree, true, offset + 0.5F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.leftLeg2BodyB, walkSpeed, walkDegree * 0.5F, true, offset + 0.5F, 0.1F, limbSwing, limbSwingAmount);
                this.swing(this.rightLegBodyF, walkSpeed, walkDegree, false, offset, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.rightLegBodyF2, walkSpeed, walkDegree * 0.5F, false, offset, 0.1F, limbSwing, limbSwingAmount);
                this.swing(this.rightLegBodyB, walkSpeed, walkDegree, false, offset + 0.5F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.rightLegBodyB2, walkSpeed, walkDegree * 0.5F, false, offset + 0.5F, 0.1F, limbSwing, limbSwingAmount);
                this.body.rotationPointY = this.body.rotationPointY + (float) (Math.sin((double) (limbSwing * walkSpeed) - walkOffset) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
                this.body.rotationPointY = this.body.rotationPointY + (float) (Math.sin((double) ageInTicks * 0.1 - walkOffset) * 0.01);
            }
        } else if (entity instanceof EntityCentipedeBody) {
            float offset = (float) ((double) (((EntityCentipedeBody) entity).getBodyIndex() + 1) * Math.PI * 0.5);
            double walkOffset = (double) offset * Math.PI * 0.5;
            this.swing(this.leftLegTailF, walkSpeed, walkDegree, true, offset, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.leftLeg2TailF, walkSpeed, walkDegree * 0.5F, true, offset, 0.1F, limbSwing, limbSwingAmount);
            this.swing(this.leftLegTailB, walkSpeed, walkDegree, true, offset + 0.5F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.leftLegTailB2, walkSpeed, walkDegree * 0.5F, true, offset + 0.5F, 0.1F, limbSwing, limbSwingAmount);
            this.swing(this.rightLegTailF, walkSpeed, walkDegree, false, offset, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.rightLeg2TailF, walkSpeed, walkDegree * 0.5F, false, offset, 0.1F, limbSwing, limbSwingAmount);
            this.swing(this.rightLegTailB, walkSpeed, walkDegree, false, offset + 0.5F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.rightLegTailB2, walkSpeed, walkDegree * 0.5F, false, offset + 0.5F, 0.1F, limbSwing, limbSwingAmount);
            this.tail.rotationPointY = this.tail.rotationPointY + (float) (Math.sin((double) (limbSwing * walkSpeed) - walkOffset) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
            this.tail.rotationPointY = this.tail.rotationPointY + (float) (Math.sin((double) ageInTicks * 0.1 - walkOffset) * 0.01);
            this.swing(this.leftTail, walkSpeed, walkDegree * 0.2F, true, offset + 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.rightTail, walkSpeed, walkDegree * 0.2F, false, offset + 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.leftTail, idleSpeed, idleDegree, true, offset + 1.5F, -0.5F, ageInTicks, 1.0F);
            this.walk(this.rightTail, idleSpeed, idleDegree, false, offset + 1.5F, 0.5F, ageInTicks, 1.0F);
        }
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return switch(this.type) {
            case 0 ->
                ImmutableList.of(this.root, this.head, this.head2, this.fangs, this.antenna_left, this.antenna_left_r1, this.antenna_right, this.antenna_right_r1);
            case 1 ->
                ImmutableList.of(this.root, this.body, this.leftLegBodyF, this.leftLeg2BodyF, this.rightLegBodyF, this.rightLegBodyF2, this.leftLegBodyB, this.leftLeg2BodyB, this.rightLegBodyB, this.rightLegBodyB2);
            case 2 ->
                ImmutableList.of(this.root, this.tail, this.leftLegTailF, this.leftLeg2TailF, this.rightLegTailF, this.rightLeg2TailF, this.leftLegTailB, this.leftLegTailB2, this.rightLegTailB, this.rightLegTailB2, this.leftTail, this.leftTailEnd, new AdvancedModelBox[] { this.rightTail, this.rightTailEnd });
            default ->
                ImmutableList.of(this.root);
        };
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}