package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityAnteater;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelAnteater extends AdvancedEntityModel<EntityAnteater> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox head;

    public final AdvancedModelBox left_ear;

    public final AdvancedModelBox right_ear;

    public final AdvancedModelBox snout;

    public final AdvancedModelBox tongue1;

    public final AdvancedModelBox tongue2;

    public final AdvancedModelBox left_leg;

    public final AdvancedModelBox right_leg;

    public final AdvancedModelBox left_arm;

    public final AdvancedModelBox left_claws;

    public final AdvancedModelBox right_arm;

    public final AdvancedModelBox right_claws;

    public final AdvancedModelBox tail;

    public ModelAnimator animator;

    public ModelAnteater() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -13.0F, 4.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-4.0F, -5.0F, -14.0F, 8.0F, 10.0F, 21.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -4.0F, -15.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(38, 0).addBox(-2.0F, -1.0F, -7.0F, 4.0F, 5.0F, 8.0F, 0.0F, false);
        this.left_ear = new AdvancedModelBox(this, "left_ear");
        this.left_ear.setRotationPoint(2.0F, 0.0F, -4.0F);
        this.head.addChild(this.left_ear);
        this.left_ear.setTextureOffset(11, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, 0.0F, false);
        this.right_ear = new AdvancedModelBox(this, "right_ear");
        this.right_ear.setRotationPoint(-2.0F, 0.0F, -4.0F);
        this.head.addChild(this.right_ear);
        this.right_ear.setTextureOffset(11, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, 0.0F, true);
        this.snout = new AdvancedModelBox(this, "snout");
        this.snout.setRotationPoint(0.0F, 1.5F, -6.5F);
        this.head.addChild(this.snout);
        this.setRotationAngle(this.snout, 0.2618F, 0.0F, 0.0F);
        this.snout.setTextureOffset(28, 32).addBox(-1.0F, -1.0F, -10.0F, 2.0F, 3.0F, 10.0F, 0.0F, false);
        this.tongue1 = new AdvancedModelBox(this, "tongue1");
        this.tongue1.setRotationPoint(0.0F, 1.0F, -10.0F);
        this.snout.addChild(this.tongue1);
        this.tongue1.setTextureOffset(43, 32).addBox(-0.5F, 0.0F, -6.0F, 1.0F, 0.0F, 6.0F, 0.0F, false);
        this.tongue2 = new AdvancedModelBox(this, "tongue2");
        this.tongue2.setRotationPoint(0.0F, 0.0F, -6.0F);
        this.tongue1.addChild(this.tongue2);
        this.tongue2.setTextureOffset(38, 14).addBox(-0.5F, 0.0F, -6.0F, 1.0F, 0.0F, 6.0F, 0.0F, false);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(2.5F, 4.0F, 5.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(0, 32).addBox(-1.5F, 1.0F, -2.0F, 3.0F, 8.0F, 4.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-2.5F, 4.0F, 5.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(0, 32).addBox(-1.5F, 1.0F, -2.0F, 3.0F, 8.0F, 4.0F, 0.0F, true);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(3.1F, 4.0F, -11.9F);
        this.body.addChild(this.left_arm);
        this.left_arm.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -2.0F, 3.0F, 10.0F, 4.0F, 0.0F, false);
        this.left_claws = new AdvancedModelBox(this, "left_claws");
        this.left_claws.setRotationPoint(-1.0F, 9.0F, 2.0F);
        this.left_arm.addChild(this.left_claws);
        this.left_claws.setTextureOffset(13, 13).addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.left_claws.setTextureOffset(0, 15).addBox(-1.0F, -2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-3.1F, 4.0F, -11.9F);
        this.body.addChild(this.right_arm);
        this.right_arm.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 10.0F, 4.0F, 0.0F, true);
        this.right_claws = new AdvancedModelBox(this, "right_claws");
        this.right_claws.setRotationPoint(1.0F, 9.0F, 2.0F);
        this.right_arm.addChild(this.right_claws);
        this.right_claws.setTextureOffset(13, 13).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, true);
        this.right_claws.setTextureOffset(0, 15).addBox(1.0F, -2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -5.0F, 7.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 32).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 11.0F, 19.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void animate(EntityAnteater entity, float f, float f1, float f2, float f3, float f4) {
        float partialTick = f2 - (float) entity.f_19797_;
        float standProgress = (entity.prevStandProgress + (entity.standProgress - entity.prevStandProgress) * partialTick) * 0.2F;
        float inverStandProgress = 1.0F - standProgress;
        this.animator.update(entity);
        this.animator.setAnimation(EntityAnteater.ANIMATION_SLASH_L);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, Maths.rad(-15.0), Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.tail, Maths.rad(25.0), Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(15.0), Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.right_leg, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_leg, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-50.0), 0.0F, Maths.rad(-45.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.move(this.left_arm, 0.0F, 2.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-10.0) + Maths.rad(-70.0) * inverStandProgress, 0.0F, Maths.rad(65.0) * standProgress);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityAnteater.ANIMATION_SLASH_R);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, Maths.rad(-15.0), Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.tail, Maths.rad(25.0), Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(15.0), Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.right_leg, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_leg, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-50.0), 0.0F, Maths.rad(45.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.move(this.right_arm, 0.0F, 2.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(-10.0) + Maths.rad(-70.0) * inverStandProgress, 0.0F, Maths.rad(-65.0) * standProgress);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(EntityAnteater entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float tongueSpeed = 0.7F;
        float tongueDegree = 0.35F;
        float walkSpeed = 0.5F;
        float walkDegree = 1.0F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.2F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float standProgress = entity.prevStandProgress + (entity.standProgress - entity.prevStandProgress) * partialTick;
        float feedProgress = entity.prevTongueProgress + (entity.tongueProgress - entity.prevTongueProgress) * partialTick;
        float leaningProgress = entity.prevLeaningProgress + (entity.leaningProgress - entity.prevLeaningProgress) * partialTick;
        this.progressRotationPrev(this.body, standProgress, Maths.rad(-80.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, standProgress, Maths.rad(80.0), Maths.rad(10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, standProgress, Maths.rad(80.0), Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, standProgress, Maths.rad(80.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, standProgress, Maths.rad(45.0), Maths.rad(-70.0), Maths.rad(-120.0), 5.0F);
        this.progressRotationPrev(this.right_arm, standProgress, Maths.rad(45.0), Maths.rad(70.0), Maths.rad(120.0), 5.0F);
        this.progressRotationPrev(this.head, standProgress, Maths.rad(80.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, standProgress, 0.0F, 1.0F, -2.0F, 5.0F);
        this.progressPositionPrev(this.body, standProgress, 0.0F, -1.0F, -2.0F, 5.0F);
        this.progressPositionPrev(this.tail, standProgress, 0.0F, 2.0F, -3.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, standProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, standProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, standProgress, 1.0F, -1.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, standProgress, -1.0F, -1.0F, 2.0F, 5.0F);
        if (entity.m_6162_() && entity.m_20159_()) {
            this.progressRotationPrev(this.left_arm, 1.0F, 0.0F, Maths.rad(-90.0), Maths.rad(-60.0), 1.0F);
            this.progressRotationPrev(this.right_arm, 1.0F, 0.0F, Maths.rad(90.0), Maths.rad(60.0), 1.0F);
            this.progressRotationPrev(this.left_leg, 1.0F, Maths.rad(20.0), 0.0F, Maths.rad(-60.0), 1.0F);
            this.progressRotationPrev(this.right_leg, 1.0F, Maths.rad(20.0), 0.0F, Maths.rad(60.0), 1.0F);
            this.progressRotationPrev(this.tail, 1.0F, Maths.rad(-20.0), 0.0F, 0.0F, 1.0F);
        }
        this.progressRotationPrev(this.head, leaningProgress, Maths.rad(50.0), 0.0F, 0.0F, 20.0F);
        double tongueM = Math.min(Math.sin((double) (ageInTicks * 0.15F)), 0.0);
        float toungeF = 12.0F + 12.0F * (float) tongueM * feedProgress * 0.2F;
        float toungeMinus = (float) (-tongueM) * feedProgress * 0.2F;
        this.walk(this.tongue1, tongueSpeed * 2.0F, tongueDegree, false, 0.0F, 0.0F, ageInTicks, toungeMinus);
        this.walk(this.tongue2, tongueSpeed * 2.0F, tongueDegree, false, 0.0F, 0.0F, ageInTicks, toungeMinus);
        this.tongue1.rotationPointZ += toungeF;
        this.walk(this.tail, idleSpeed, idleDegree, true, 2.0F, 0.2F, ageInTicks, 1.0F);
        this.walk(this.right_arm, idleSpeed, idleDegree, true, 2.0F, 0.2F, ageInTicks, standProgress * 0.2F);
        this.walk(this.left_arm, idleSpeed, idleDegree, true, 2.0F, 0.2F, ageInTicks, standProgress * 0.2F);
        this.walk(this.right_leg, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_leg, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_arm, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_arm, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 0.2F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree * 2.0F, true, limbSwing, limbSwingAmount);
        this.bob(this.head, walkSpeed, walkDegree, true, limbSwing, limbSwingAmount);
        if (standProgress <= 0.0F) {
            this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
        }
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.head, this.left_ear, this.right_ear, this.left_arm, this.right_arm, this.left_leg, this.right_leg, this.left_claws, this.right_claws, new AdvancedModelBox[] { this.snout, this.tongue1, this.tongue2 });
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.35F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            this.head.setScale(1.0F, 1.0F, 1.0F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}