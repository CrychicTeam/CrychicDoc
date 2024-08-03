package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;

public class ModelCockatriceChick extends ModelDragonBase<EntityCockatrice> {

    public AdvancedModelBox lowerBody;

    public AdvancedModelBox leftThigh;

    public AdvancedModelBox rightThigh;

    public AdvancedModelBox upperBody;

    public AdvancedModelBox tail1;

    public AdvancedModelBox neck;

    public AdvancedModelBox leftUpperArm;

    public AdvancedModelBox RightUpperArm;

    public AdvancedModelBox neck2;

    public AdvancedModelBox head;

    public AdvancedModelBox upperJaw;

    public AdvancedModelBox lowerJaw;

    public AdvancedModelBox leftUpperArmWing;

    public AdvancedModelBox RightUpperArmWing;

    public AdvancedModelBox tail2;

    public AdvancedModelBox tail3;

    public AdvancedModelBox rightToeClaw2;

    public AdvancedModelBox leftLeg;

    public AdvancedModelBox leftFoot;

    public AdvancedModelBox rightLeg;

    public AdvancedModelBox rightFoot;

    private final ModelAnimator animator;

    public ModelCockatriceChick() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.RightUpperArm = new AdvancedModelBox(this, 0, 20);
        this.RightUpperArm.mirror = true;
        this.RightUpperArm.setPos(-2.5F, 1.3F, -3.3F);
        this.RightUpperArm.addBox(-2.0F, 0.0F, -1.0F, 2.0F, 5.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.RightUpperArm, 0.0F, -0.2268928F, 0.87266463F);
        this.leftUpperArmWing = new AdvancedModelBox(this, 22, 10);
        this.leftUpperArmWing.setPos(1.4F, 2.7F, 1.1F);
        this.leftUpperArmWing.addBox(-0.5F, 0.0F, -2.5F, 1.0F, 4.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.leftUpperArmWing, 1.5184364F, -0.0F, 0.0F);
        this.tail1 = new AdvancedModelBox(this, 91, 0);
        this.tail1.setPos(0.0F, -0.3F, 6.5F);
        this.tail1.addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.tail1, 0.18203785F, -0.0F, 0.0F);
        this.rightThigh = new AdvancedModelBox(this, 14, 35);
        this.rightThigh.setPos(-3.0F, 15.2F, 3.0F);
        this.rightThigh.addBox(-3.0F, -2.5F, -2.0F, 3.0F, 6.0F, 5.0F, 0.0F);
        this.lowerBody = new AdvancedModelBox(this, 65, 32);
        this.lowerBody.setPos(0.0F, 9.0F, -0.5F);
        this.lowerBody.addBox(-4.5F, -1.0F, 0.0F, 9.0F, 9.0F, 9.0F, 0.0F);
        this.setRotateAngle(this.lowerBody, -0.31869712F, 0.0F, 0.0F);
        this.head = new AdvancedModelBox(this, 0, 0);
        this.head.setPos(0.0F, 0.8F, -6.03F);
        this.head.addBox(-2.5F, -4.0F, -5.0F, 5.0F, 5.0F, 6.0F, 0.0F);
        this.setRotateAngle(this.head, 1.1383038F, 0.0F, 0.0F);
        this.rightLeg = new AdvancedModelBox(this, 2, 25);
        this.rightLeg.setPos(-1.2F, 0.9F, 0.1F);
        this.rightLeg.addBox(-1.0F, 0.4F, -6.7F, 2.0F, 2.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.rightLeg, (float) (Math.PI * 5.0 / 12.0), -0.0F, 0.0F);
        this.neck2 = new AdvancedModelBox(this, 0, 47);
        this.neck2.setPos(0.0F, 1.0F, -2.6F);
        this.neck2.addBox(-2.02F, -2.0F, -8.0F, 4.0F, 4.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.neck2, -0.31869712F, -0.0F, 0.0F);
        this.tail3 = new AdvancedModelBox(this, 49, 16);
        this.tail3.setPos(0.0F, 0.3F, 5.2F);
        this.tail3.addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 12.0F, 0.0F);
        this.setRotateAngle(this.tail3, -0.05253441F, -0.0F, 0.0F);
        this.upperJaw = new AdvancedModelBox(this, 28, 1);
        this.upperJaw.setPos(0.0F, 0.0F, -3.93F);
        this.upperJaw.addBox(-1.5F, -2.4F, -4.0F, 3.0F, 2.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.upperJaw, -0.0017453292F, -0.0F, 0.0F);
        this.leftFoot = new AdvancedModelBox(this, 0, 36);
        this.leftFoot.setPos(0.0F, 0.9F, -5.7F);
        this.leftFoot.addBox(-1.5F, 0.0F, -3.1F, 3.0F, 2.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.leftFoot, (float) (-Math.PI * 5.0 / 12.0), -0.0F, 0.0F);
        this.rightToeClaw2 = new AdvancedModelBox(this, 0, 40);
        this.rightToeClaw2.setPos(0.0F, 0.2F, -2.5F);
        this.rightToeClaw2.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F);
        this.setRotateAngle(this.rightToeClaw2, -1.7627826F, -0.0F, 0.0F);
        this.leftUpperArm = new AdvancedModelBox(this, 0, 20);
        this.leftUpperArm.mirror = true;
        this.leftUpperArm.setPos(2.5F, 1.3F, -3.3F);
        this.leftUpperArm.addBox(0.0F, 0.0F, -1.0F, 2.0F, 5.0F, 3.0F, 0.0F);
        this.setRotateAngle(this.leftUpperArm, 0.0F, 0.2268928F, -0.87266463F);
        this.upperBody = new AdvancedModelBox(this, 67, 5);
        this.upperBody.setPos(0.0F, 1.1F, -2.0F);
        this.upperBody.addBox(-3.5F, -2.0F, -5.0F, 7.0F, 8.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.upperBody, 0.091106184F, -0.0F, 0.0F);
        this.rightFoot = new AdvancedModelBox(this, 0, 36);
        this.rightFoot.setPos(0.0F, 0.9F, -5.7F);
        this.rightFoot.addBox(-1.5F, 0.0F, -3.0F, 3.0F, 2.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.rightFoot, (float) (-Math.PI * 5.0 / 12.0), -0.0F, 0.0F);
        this.leftThigh = new AdvancedModelBox(this, 14, 35);
        this.leftThigh.mirror = true;
        this.leftThigh.setPos(3.0F, 15.2F, 3.0F);
        this.leftThigh.addBox(0.0F, -2.5F, -2.0F, 3.0F, 6.0F, 5.0F, 0.0F);
        this.tail2 = new AdvancedModelBox(this, 90, 13);
        this.tail2.setPos(0.0F, 0.1F, 4.7F);
        this.tail2.addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.tail2, 0.18203785F, 0.0F, 0.0F);
        this.neck = new AdvancedModelBox(this, 29, 51);
        this.neck.setPos(0.0F, 1.8F, -1.5F);
        this.neck.addBox(-2.5F, -2.0F, -6.5F, 5.0F, 5.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.neck, -0.4553564F, -0.0F, 0.0F);
        this.RightUpperArmWing = new AdvancedModelBox(this, 22, 10);
        this.RightUpperArmWing.mirror = true;
        this.RightUpperArmWing.setPos(-1.0F, 2.7F, 1.1F);
        this.RightUpperArmWing.addBox(-0.5F, 0.0F, -2.5F, 1.0F, 4.0F, 7.0F, 0.0F);
        this.setRotateAngle(this.RightUpperArmWing, 1.5184364F, -0.0F, 0.0F);
        this.lowerJaw = new AdvancedModelBox(this, 50, 0);
        this.lowerJaw.setPos(0.0F, -0.1F, -3.53F);
        this.lowerJaw.addBox(-1.0F, -0.5F, -4.3F, 2.0F, 1.0F, 4.0F, 0.0F);
        this.setRotateAngle(this.lowerJaw, -0.091106184F, 0.0F, 0.0F);
        this.leftLeg = new AdvancedModelBox(this, 2, 25);
        this.leftLeg.mirror = true;
        this.leftLeg.setPos(1.2F, 0.9F, 0.1F);
        this.leftLeg.addBox(-1.0F, 0.4F, -6.7F, 2.0F, 2.0F, 8.0F, 0.0F);
        this.setRotateAngle(this.leftLeg, (float) (Math.PI * 5.0 / 12.0), -0.0F, 0.0F);
        this.upperBody.addChild(this.RightUpperArm);
        this.leftUpperArm.addChild(this.leftUpperArmWing);
        this.lowerBody.addChild(this.tail1);
        this.neck2.addChild(this.head);
        this.rightThigh.addChild(this.rightLeg);
        this.neck.addChild(this.neck2);
        this.tail2.addChild(this.tail3);
        this.head.addChild(this.upperJaw);
        this.leftLeg.addChild(this.leftFoot);
        this.tail2.addChild(this.rightToeClaw2);
        this.upperBody.addChild(this.leftUpperArm);
        this.lowerBody.addChild(this.upperBody);
        this.rightLeg.addChild(this.rightFoot);
        this.tail1.addChild(this.tail2);
        this.upperBody.addChild(this.neck);
        this.RightUpperArm.addChild(this.RightUpperArmWing);
        this.head.addChild(this.lowerJaw);
        this.leftThigh.addChild(this.leftLeg);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.lowerBody, this.leftThigh, this.rightThigh);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.lowerBody, this.leftThigh, this.rightThigh, this.upperBody, this.tail1, this.neck, this.leftUpperArm, this.RightUpperArm, this.neck2, this.head, this.upperJaw, this.lowerJaw, new AdvancedModelBox[] { this.leftUpperArmWing, this.RightUpperArmWing, this.tail2, this.tail3, this.rightToeClaw2, this.leftLeg, this.leftFoot, this.rightLeg, this.rightFoot });
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityCockatrice.ANIMATION_EAT)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.neck2, 7.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.head, 45.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.upperBody, 15.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.neck, 31.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.neck2, 7.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.head, 45.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.lowerJaw, 15.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.upperBody, 5.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.neck, -10.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.neck2, 1.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.head, 45.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityCockatrice.ANIMATION_JUMPAT)) {
            this.animator.startKeyframe(10);
            this.rotate(this.animator, this.lowerBody, 18.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.upperBody, 5.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.neck, -23.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.neck2, 18.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.head, -27.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.leftUpperArm, 0.0F, 30.0F, -50.0F);
            this.rotate(this.animator, this.RightUpperArm, 0.0F, -30.0F, 50.0F);
            this.animator.move(this.lowerBody, 0.0F, 1.7F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.jumpPos();
            this.animator.endKeyframe();
            this.animator.startKeyframe(2);
            this.jumpPos();
            this.rotate(this.animator, this.leftUpperArm, 0.0F, 30.0F, -75.0F);
            this.rotate(this.animator, this.RightUpperArm, 0.0F, -30.0F, 75.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(2);
            this.jumpPos();
            this.rotate(this.animator, this.leftUpperArm, 0.0F, 30.0F, -19.0F);
            this.rotate(this.animator, this.RightUpperArm, 0.0F, -30.0F, 19.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(2);
            this.jumpPos();
            this.rotate(this.animator, this.leftUpperArm, 0.0F, 30.0F, -75.0F);
            this.rotate(this.animator, this.RightUpperArm, 0.0F, -30.0F, 75.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(2);
            this.jumpPos();
            this.rotate(this.animator, this.leftUpperArm, 0.0F, 30.0F, -19.0F);
            this.rotate(this.animator, this.RightUpperArm, 0.0F, -30.0F, 19.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(2);
            this.jumpPos();
            this.rotate(this.animator, this.leftUpperArm, 0.0F, 30.0F, -75.0F);
            this.rotate(this.animator, this.RightUpperArm, 0.0F, -30.0F, 75.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityCockatrice.ANIMATION_WATTLESHAKE)) {
            this.animator.startKeyframe(3);
            this.rotate(this.animator, this.neck, 0.0F, 0.0F, -23.0F);
            this.rotate(this.animator, this.neck2, 0.0F, 0.0F, -13.0F);
            this.rotate(this.animator, this.head, 5.0F, 0.0F, -15.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(2);
            this.rotate(this.animator, this.neck, 0.0F, 0.0F, -23.0F);
            this.rotate(this.animator, this.neck2, 0.0F, 0.0F, -13.0F);
            this.rotate(this.animator, this.head, 5.0F, 0.0F, -15.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(3);
            this.rotate(this.animator, this.neck, 0.0F, 0.0F, 23.0F);
            this.rotate(this.animator, this.neck2, 0.0F, 0.0F, 13.0F);
            this.rotate(this.animator, this.head, 5.0F, 0.0F, 15.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(2);
            this.rotate(this.animator, this.neck, 0.0F, 0.0F, 23.0F);
            this.rotate(this.animator, this.neck2, 0.0F, 0.0F, 13.0F);
            this.rotate(this.animator, this.head, 5.0F, 0.0F, 15.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(3);
            this.rotate(this.animator, this.neck, 0.0F, 0.0F, -23.0F);
            this.rotate(this.animator, this.neck2, 0.0F, 0.0F, -13.0F);
            this.rotate(this.animator, this.head, 5.0F, 0.0F, -15.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(2);
            this.rotate(this.animator, this.neck, 0.0F, 0.0F, -23.0F);
            this.rotate(this.animator, this.neck2, 0.0F, 0.0F, -13.0F);
            this.rotate(this.animator, this.head, 5.0F, 0.0F, -15.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityCockatrice.ANIMATION_BITE)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.neck, -47.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.neck2, 17.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.head, 46.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.lowerJaw, 10.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.neck, 26.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.neck2, -18.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.head, 2.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.lowerJaw, 33.0F, 0.0F, 0.0F);
            this.rotate(this.animator, this.upperJaw, -20.0F, 0.0F, 0.0F);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityCockatrice.ANIMATION_SPEAK)) {
            this.animator.startKeyframe(5);
            this.rotate(this.animator, this.lowerJaw, 25.0F, 0.0F, 0.0F);
            this.animator.resetKeyframe(5);
        }
    }

    private void jumpPos() {
        this.rotate(this.animator, this.lowerBody, -29.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.upperBody, 10.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.neck, 7.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.neck2, 32.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.head, 36.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.lowerJaw, 28.0F, 0.0F, 0.0F);
        this.rotate(this.animator, this.tail1, -18.0F, 0.0F, 0.0F);
        this.animator.move(this.lowerBody, 0.0F, -1.9F, 2.5F);
        this.rotate(this.animator, this.rightThigh, -74.0F, 0.0F, 10.0F);
        this.rotate(this.animator, this.rightLeg, 0.0F, 39.0F, 0.0F);
        this.rotate(this.animator, this.rightFoot, 50.0F, -10.0F, 0.0F);
        this.rotate(this.animator, this.leftThigh, -74.0F, 0.0F, -10.0F);
        this.rotate(this.animator, this.leftLeg, 0.0F, -39.0F, 0.0F);
        this.rotate(this.animator, this.leftFoot, 50.0F, 10.0F, 0.0F);
    }

    public void setupAnim(EntityCockatrice entity, float f, float f1, float f2, float f3, float f4) {
        this.animate(entity, f, f1, f2, f3, f4);
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 0.5F;
        float degree_idle = 0.5F;
        AdvancedModelBox[] NECK = new AdvancedModelBox[] { this.neck, this.neck2, this.head };
        AdvancedModelBox[] TAIL = new AdvancedModelBox[] { this.tail1, this.tail2, this.tail3 };
        this.chainWave(NECK, speed_idle, degree_idle * 0.1F, 4.0, f2, 1.0F);
        this.chainSwing(TAIL, speed_idle, degree_idle * 0.5F, 0.0, f2, 1.0F);
        this.walk(this.lowerBody, speed_idle, degree_idle * 0.1F, false, 0.0F, 0.1F, f2, 1.0F);
        this.walk(this.upperBody, speed_idle, degree_idle * 0.05F, true, 1.0F, 0.0F, f2, 1.0F);
        this.walk(this.leftUpperArm, speed_idle, degree_idle * 0.2F, false, 1.0F, 0.1F, f2, 1.0F);
        this.walk(this.RightUpperArm, speed_idle, degree_idle * 0.2F, false, 1.0F, 0.1F, f2, 1.0F);
        this.flap(this.leftUpperArm, speed_idle, degree_idle * 0.2F, true, 2.0F, -0.3F, f2, 1.0F);
        this.flap(this.RightUpperArm, speed_idle, degree_idle * 0.2F, false, 2.0F, -0.3F, f2, 1.0F);
        this.faceTarget(f3, f4, 2.0F, new AdvancedModelBox[] { this.head });
        this.faceTarget(f3, f4, 2.0F, new AdvancedModelBox[] { this.neck });
        this.chainWave(NECK, speed_walk, degree_walk * 0.5F, 4.0, f, f1);
        this.walk(this.lowerBody, speed_walk, degree_walk * 0.1F, false, 0.0F, 0.0F, f, f1);
        this.walk(this.upperBody, speed_walk, degree_walk * 0.05F, true, 1.0F, 0.0F, f, f1);
        this.walk(this.leftThigh, speed_walk, degree_walk, true, 1.0F, 0.1F, f, f1);
        this.walk(this.rightThigh, speed_walk, degree_walk, false, 1.0F, 0.1F, f, f1);
        this.walk(this.leftLeg, speed_walk, degree_walk, true, 1.0F, 0.1F, f, f1);
        this.walk(this.rightLeg, speed_walk, degree_walk, false, 1.0F, 0.1F, f, f1);
        this.walk(this.leftFoot, speed_walk, degree_walk * -1.75F, true, 1.0F, -0.1F, f, f1);
        this.walk(this.rightFoot, speed_walk, degree_walk * -1.75F, false, 1.0F, -0.1F, f, f1);
        this.progressRotation(this.neck, entity.stareProgress, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.progressRotation(this.neck2, entity.stareProgress, (float) Math.toRadians(-18.0), 0.0F, 0.0F);
        this.progressRotation(this.head, entity.stareProgress, (float) Math.toRadians(18.0), 0.0F, 0.0F);
        this.progressRotation(this.rightThigh, entity.sitProgress, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.progressRotation(this.leftThigh, entity.sitProgress, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.progressRotation(this.rightLeg, entity.sitProgress, (float) Math.toRadians(13.0), 0.0F, 0.0F);
        this.progressRotation(this.leftLeg, entity.sitProgress, (float) Math.toRadians(13.0), 0.0F, 0.0F);
        this.progressRotation(this.rightFoot, entity.sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressRotation(this.leftFoot, entity.sitProgress, 0.0F, 0.0F, 0.0F);
        this.progressPosition(this.rightThigh, entity.sitProgress, -3.0F, 19.0F, 3.0F);
        this.progressPosition(this.leftThigh, entity.sitProgress, 3.0F, 19.0F, 3.0F);
        this.progressPosition(this.lowerBody, entity.sitProgress, 0.0F, 12.9F, -2.5F);
    }

    @Override
    public void renderStatue(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.m_7695_(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}