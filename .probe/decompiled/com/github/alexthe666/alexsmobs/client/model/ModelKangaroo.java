package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityKangaroo;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ModelKangaroo extends AdvancedEntityModel<EntityKangaroo> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox pouch;

    public final AdvancedModelBox tail1;

    public final AdvancedModelBox tail2;

    public final AdvancedModelBox leg_left;

    public final AdvancedModelBox knee_left;

    public final AdvancedModelBox foot_left;

    public final AdvancedModelBox leg_right;

    public final AdvancedModelBox knee_right;

    public final AdvancedModelBox foot_right;

    public final AdvancedModelBox chest;

    public final AdvancedModelBox arm_left;

    public final AdvancedModelBox arm_right;

    public final AdvancedModelBox neck;

    public final AdvancedModelBox head;

    public final AdvancedModelBox ear_left;

    public final AdvancedModelBox ear_right;

    public final AdvancedModelBox snout;

    public static boolean renderOnlyHead = false;

    private ModelAnimator animator;

    public ModelKangaroo() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -15.0F, 4.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-5.0F, -6.0F, -6.0F, 10.0F, 11.0F, 13.0F, 0.0F, false);
        this.pouch = new AdvancedModelBox(this, "pouch");
        this.pouch.setPos(0.0F, 2.7F, -2.2F);
        this.body.addChild(this.pouch);
        this.pouch.setTextureOffset(64, 6).addBox(-3.5F, -2.5F, -4.0F, 7.0F, 5.0F, 8.0F, -0.1F, false);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setPos(0.0F, -5.0F, 7.0F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(0, 25).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 6.0F, 15.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setPos(0.0F, 4.0F, 15.0F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(26, 32).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 4.0F, 15.0F, 0.0F, false);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.leg_left.setPos(4.25F, 0.75F, -0.5F);
        this.body.addChild(this.leg_left);
        this.leg_left.setTextureOffset(48, 28).addBox(-1.25F, -3.75F, -3.5F, 3.0F, 7.0F, 8.0F, 0.0F, false);
        this.knee_left = new AdvancedModelBox(this, "knee_left");
        this.knee_left.setPos(0.25F, 3.25F, -3.5F);
        this.leg_left.addChild(this.knee_left);
        this.knee_left.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 9.0F, 3.0F, 0.0F, false);
        this.foot_left = new AdvancedModelBox(this, "foot_left");
        this.foot_left.setPos(0.0F, 9.0F, 1.0F);
        this.knee_left.addChild(this.foot_left);
        this.foot_left.setTextureOffset(35, 13).addBox(-1.5F, 0.0F, -10.0F, 3.0F, 2.0F, 12.0F, 0.0F, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setPos(-4.25F, 0.75F, -0.5F);
        this.body.addChild(this.leg_right);
        this.leg_right.setTextureOffset(48, 28).addBox(-1.75F, -3.75F, -3.5F, 3.0F, 7.0F, 8.0F, 0.0F, true);
        this.knee_right = new AdvancedModelBox(this, "knee_right");
        this.knee_right.setPos(-0.25F, 3.25F, -3.5F);
        this.leg_right.addChild(this.knee_right);
        this.knee_right.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 9.0F, 3.0F, 0.0F, true);
        this.foot_right = new AdvancedModelBox(this, "foot_right");
        this.foot_right.setPos(0.0F, 9.0F, 1.0F);
        this.knee_right.addChild(this.foot_right);
        this.foot_right.setTextureOffset(35, 13).addBox(-1.5F, 0.0F, -10.0F, 3.0F, 2.0F, 12.0F, 0.0F, true);
        this.chest = new AdvancedModelBox(this, "chest");
        this.chest.setPos(0.0F, -6.0F, -6.0F);
        this.body.addChild(this.chest);
        this.chest.setTextureOffset(0, 47).addBox(-4.0F, 0.0F, -9.0F, 8.0F, 9.0F, 9.0F, 0.0F, false);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setPos(4.0F, 6.0F, -6.0F);
        this.chest.addChild(this.arm_left);
        this.arm_left.setTextureOffset(71, 49).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 12.0F, 3.0F, 0.0F, false);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setPos(-4.0F, 6.0F, -6.0F);
        this.chest.addChild(this.arm_right);
        this.arm_right.setTextureOffset(71, 49).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 12.0F, 3.0F, 0.0F, true);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setPos(0.0F, 2.0F, -8.0F);
        this.chest.addChild(this.neck);
        this.neck.setTextureOffset(35, 52).addBox(-2.0F, -6.0F, -3.0F, 4.0F, 11.0F, 5.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -6.0F, -0.5F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(34, 0).addBox(-2.5F, -4.0F, -3.5F, 5.0F, 4.0F, 6.0F, 0.0F, false);
        this.ear_left = new AdvancedModelBox(this, "ear_left");
        this.ear_left.setPos(0.4F, -4.0F, 1.5F);
        this.head.addChild(this.ear_left);
        this.setRotationAngle(this.ear_left, -0.1745F, -0.3491F, 0.4363F);
        this.ear_left.setTextureOffset(0, 47).addBox(0.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, 0.0F, false);
        this.ear_right = new AdvancedModelBox(this, "ear_right");
        this.ear_right.setPos(-0.4F, -4.0F, 1.5F);
        this.head.addChild(this.ear_right);
        this.setRotationAngle(this.ear_right, -0.1745F, 0.3491F, -0.4363F);
        this.ear_right.setTextureOffset(0, 47).addBox(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, 0.0F, true);
        this.setRotationAngle(this.chest, 0.1745F, 0.0F, 0.0F);
        this.setRotationAngle(this.tail1, -0.1745F, 0.0F, 0.0F);
        this.snout = new AdvancedModelBox(this, "snout");
        this.snout.setPos(0.0F, -1.5F, -3.5F);
        this.head.addChild(this.snout);
        this.snout.setTextureOffset(0, 25).addBox(-1.5F, -1.5F, -4.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityKangaroo.ANIMATION_EAT_GRASS);
        this.animator.startKeyframe(5);
        this.animator.move(this.neck, 0.0F, 3.0F, -2.0F);
        this.animator.rotate(this.neck, Maths.rad(100.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.neck, 0.0F, 3.0F, -2.0F);
        this.animator.rotate(this.neck, Maths.rad(70.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.neck, 0.0F, 3.0F, -2.0F);
        this.animator.rotate(this.neck, Maths.rad(100.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityKangaroo.ANIMATION_KICK);
        this.animator.startKeyframe(5);
        this.animator.move(this.head, 0.0F, 1.0F, -1.0F);
        this.animator.rotate(this.body, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, -4.0F, -20.0F);
        this.animator.move(this.chest, 0.0F, 2.0F, 2.0F);
        this.animator.move(this.knee_right, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.knee_left, 0.0F, -1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail2, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.knee_left, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.knee_right, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.foot_left, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.foot_right, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, 0.0F, 0.0F, Maths.rad(-15.0));
        this.animator.rotate(this.arm_left, 0.0F, 0.0F, Maths.rad(15.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
        this.animator.setAnimation(EntityKangaroo.ANIMATION_PUNCH_R);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.chest, Maths.rad(-10.0), Maths.rad(-30.0), 0.0F);
        this.animator.rotate(this.neck, 0.0F, Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.chest, Maths.rad(10.0), Maths.rad(-10.0), 0.0F);
        this.animator.rotate(this.neck, 0.0F, Maths.rad(-40.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-125.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityKangaroo.ANIMATION_PUNCH_L);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.chest, Maths.rad(-10.0), Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.neck, 0.0F, Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.chest, Maths.rad(10.0), Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.neck, 0.0F, Maths.rad(-40.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(30.0), 0.0F);
        this.animator.rotate(this.arm_left, Maths.rad(-125.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(EntityKangaroo entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float partialTick = Minecraft.getInstance().getFrameTime();
        float jumpRotation = Mth.sin(entity.getJumpCompletion(partialTick) * (float) Math.PI);
        float walkSpeed = 1.0F;
        float walkDegree = 0.5F;
        float idleSpeed = 0.05F;
        float idleDegree = 0.1F;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        float pouchOpenProgress = entity.prevPouchProgress + (entity.pouchProgress - entity.prevPouchProgress) * partialTick;
        float moveProgress = entity.prevTotalMovingProgress + (entity.totalMovingProgress - entity.prevTotalMovingProgress) * partialTick;
        float stillProgress = Math.max(0.0F, entity.prevStandProgress + (entity.standProgress - entity.prevStandProgress) * partialTick - moveProgress);
        if (entity.getVisualFlag() == 1) {
            this.progressRotationPrev(this.arm_left, 1.0F, Maths.rad(-65.0), 0.0F, Maths.rad(-45.0), 1.0F);
            this.progressRotationPrev(this.arm_right, 1.0F, Maths.rad(-65.0), 0.0F, Maths.rad(45.0), 1.0F);
        }
        this.progressRotationPrev(this.knee_left, sitProgress, Maths.rad(65.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.knee_right, sitProgress, Maths.rad(65.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.foot_left, sitProgress, Maths.rad(-65.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.foot_right, sitProgress, Maths.rad(-65.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, sitProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_right, sitProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.foot_left, sitProgress, 0.0F, -1.0F, 0.7F, 5.0F);
        this.progressPositionPrev(this.foot_right, sitProgress, 0.0F, -1.0F, 0.7F, 5.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 7.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.arm_right, sitProgress, 0.0F, -4.5F, 2.0F, 5.0F);
        this.progressPositionPrev(this.arm_left, sitProgress, 0.0F, -4.5F, 2.0F, 5.0F);
        this.progressRotationPrev(this.body, stillProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, stillProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, stillProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.chest, stillProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, stillProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_right, stillProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, stillProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail1, stillProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail2, stillProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.tail1, stillProgress, 0.0F, 0.0F, -2.0F, 5.0F);
        this.progressPositionPrev(this.pouch, pouchOpenProgress, 0.0F, 3.0F, 0.0F, 5.0F);
        this.walk(this.arm_left, idleSpeed, idleDegree * 1.1F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.arm_right, idleSpeed, idleDegree * 1.1F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.chest, idleSpeed, idleDegree * 0.4F, true, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.neck, idleSpeed, idleDegree * 0.4F, true, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.tail1, idleSpeed, idleDegree * 1.1F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.tail2, idleSpeed, idleDegree * 1.1F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.ear_right, idleSpeed, idleDegree * -1.5F, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.ear_left, idleSpeed, idleDegree * 1.5F, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.body.rotationPointY -= jumpRotation * 4.0F;
        this.knee_left.rotationPointY -= jumpRotation * 2.0F;
        this.knee_right.rotationPointY -= jumpRotation * 2.0F;
        this.leg_right.rotationPointY -= jumpRotation * 2.0F;
        this.leg_left.rotationPointY -= jumpRotation * 2.0F;
        this.leg_right.rotationPointZ += jumpRotation * 2.0F;
        this.leg_left.rotationPointZ += jumpRotation * 2.0F;
        this.head.rotationPointY += jumpRotation * 1.0F;
        this.leg_left.rotateAngleX += jumpRotation * 50.0F * (float) (Math.PI / 180.0);
        this.leg_right.rotateAngleX += jumpRotation * 50.0F * (float) (Math.PI / 180.0);
        this.foot_left.rotateAngleX += jumpRotation * 25.0F * (float) (Math.PI / 180.0);
        this.foot_right.rotateAngleX += jumpRotation * 25.0F * (float) (Math.PI / 180.0);
        this.knee_left.rotateAngleX += jumpRotation * -25.0F * (float) (Math.PI / 180.0);
        this.knee_right.rotateAngleX += jumpRotation * -25.0F * (float) (Math.PI / 180.0);
        this.neck.rotateAngleX += jumpRotation * 15.0F * (float) (Math.PI / 180.0);
        this.head.rotateAngleX += jumpRotation * -10.0F * (float) (Math.PI / 180.0);
        this.body.rotateAngleX += jumpRotation * 10.0F * (float) (Math.PI / 180.0);
        this.arm_left.rotateAngleX += jumpRotation * 20.0F * (float) (Math.PI / 180.0);
        this.arm_right.rotateAngleX += jumpRotation * 20.0F * (float) (Math.PI / 180.0);
        this.chest.rotateAngleX += jumpRotation * -5.0F * (float) (Math.PI / 180.0);
        this.foot_left.rotateAngleX = this.foot_left.rotateAngleX + Math.max(0.0F, jumpRotation - 0.5F) * 25.0F * (float) (Math.PI / 180.0);
        this.foot_right.rotateAngleX = this.foot_right.rotateAngleX + Math.max(0.0F, jumpRotation - 0.5F) * 25.0F * (float) (Math.PI / 180.0);
        ItemStack helmet = entity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack hand = entity.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!helmet.isEmpty()) {
            this.ear_left.rotateAngleZ++;
            this.ear_right.rotateAngleZ += (float) (-Math.PI * 5.0 / 12.0);
        }
        if (!hand.isEmpty()) {
            if (entity.m_21526_()) {
                this.arm_left.rotateAngleX -= 0.43633232F;
            } else {
                this.arm_right.rotateAngleX -= 0.43633232F;
            }
        }
        this.head.rotateAngleY += netHeadYaw * 0.35F * (float) (Math.PI / 180.0);
        this.head.rotateAngleX += headPitch * 0.65F * (float) (Math.PI / 180.0);
        this.neck.rotateAngleY += netHeadYaw * 0.15F * (float) (Math.PI / 180.0);
        if (entity.m_6162_() && entity.m_20159_() && entity.m_20202_() instanceof EntityKangaroo) {
            this.head.rotateAngleX -= 0.87266463F;
            this.neck.rotateAngleX += (float) (Math.PI * 2.0 / 3.0);
            this.progressPositionPrev(this.head, 1.0F, 0.0F, 0.0F, -2.0F, 1.0F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.65F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            if (renderOnlyHead) {
                this.neck.setPos(0.0F, 0.0F, 0.0F);
                this.neck.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            } else {
                this.neck.setPos(0.0F, 2.0F, -8.0F);
                this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            }
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.arm_left, this.arm_right, this.neck, this.head, this.ear_left, this.ear_right, this.snout, this.leg_left, this.leg_right, this.knee_left, new AdvancedModelBox[] { this.knee_right, this.foot_left, this.foot_right, this.pouch, this.tail1, this.tail2, this.chest });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}