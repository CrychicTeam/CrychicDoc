package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySnowLeopard;
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

public class ModelSnowLeopard extends AdvancedEntityModel<EntitySnowLeopard> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox tail3;

    private final AdvancedModelBox head;

    private final AdvancedModelBox bubble;

    private final AdvancedModelBox whiskersLeft;

    private final AdvancedModelBox whiskersRight;

    private final AdvancedModelBox armLeft;

    private final AdvancedModelBox armRight;

    private final AdvancedModelBox legLeft;

    private final AdvancedModelBox legRight;

    public ModelAnimator animator;

    public ModelSnowLeopard() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, -11.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -9.0F, 8.0F, 9.0F, 18.0F, 0.0F, false);
        this.tail1 = new AdvancedModelBox(this);
        this.tail1.setRotationPoint(0.0F, -1.0F, 9.0F);
        this.body.addChild(this.tail1);
        this.setRotationAngle(this.tail1, -0.7418F, 0.0F, 0.0F);
        this.tail1.setTextureOffset(0, 28).addBox(-1.5F, -2.0F, -2.0F, 3.0F, 3.0F, 13.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this);
        this.tail2.setRotationPoint(0.0F, -0.2F, 11.0F);
        this.tail1.addChild(this.tail2);
        this.setRotationAngle(this.tail2, -1.0472F, 0.0F, 0.0F);
        this.tail2.setTextureOffset(0, 28).addBox(-1.5F, -8.0F, -2.0F, 3.0F, 9.0F, 3.0F, 0.1F, false);
        this.tail3 = new AdvancedModelBox(this);
        this.tail3.setRotationPoint(0.0F, -7.3F, -1.3F);
        this.tail2.addChild(this.tail3);
        this.setRotationAngle(this.tail3, -0.9599F, 0.0F, 0.0F);
        this.tail3.setTextureOffset(20, 28).addBox(-1.5F, -2.0F, -8.0F, 3.0F, 3.0F, 9.0F, 0.2F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -1.0F, -9.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(35, 0).addBox(-3.5F, -4.0F, -4.0F, 7.0F, 5.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(35, 11).addBox(-2.5F, -2.0F, -6.0F, 5.0F, 3.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(36, 28).addBox(1.5F, -6.0F, -3.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        this.head.setTextureOffset(36, 28).addBox(-3.5F, -6.0F, -3.0F, 2.0F, 2.0F, 3.0F, 0.0F, true);
        this.bubble = new AdvancedModelBox(this);
        this.bubble.setRotationPoint(0.0F, -2.0F, -6.0F);
        this.head.addChild(this.bubble);
        this.bubble.setTextureOffset(7, 13).addBox(-2.0F, -2.0F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.whiskersLeft = new AdvancedModelBox(this);
        this.whiskersLeft.setRotationPoint(2.5F, -0.5F, -5.0F);
        this.head.addChild(this.whiskersLeft);
        this.setRotationAngle(this.whiskersLeft, 0.0F, -0.5236F, 0.0F);
        this.whiskersLeft.setTextureOffset(11, 0).addBox(0.0F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);
        this.whiskersRight = new AdvancedModelBox(this);
        this.whiskersRight.setRotationPoint(-2.5F, -0.5F, -5.0F);
        this.head.addChild(this.whiskersRight);
        this.setRotationAngle(this.whiskersRight, 0.0F, 0.5236F, 0.0F);
        this.whiskersRight.setTextureOffset(11, 0).addBox(-3.0F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, true);
        this.armLeft = new AdvancedModelBox(this);
        this.armLeft.setRotationPoint(2.9F, 4.0F, -6.0F);
        this.body.addChild(this.armLeft);
        this.armLeft.setTextureOffset(0, 0).addBox(-1.4F, -2.0F, -2.0F, 3.0F, 9.0F, 4.0F, 0.0F, false);
        this.armRight = new AdvancedModelBox(this);
        this.armRight.setRotationPoint(-2.9F, 4.0F, -6.0F);
        this.body.addChild(this.armRight);
        this.armRight.setTextureOffset(0, 0).addBox(-1.6F, -2.0F, -2.0F, 3.0F, 9.0F, 4.0F, 0.0F, true);
        this.legLeft = new AdvancedModelBox(this);
        this.legLeft.setRotationPoint(2.9F, 4.0F, 8.0F);
        this.body.addChild(this.legLeft);
        this.legLeft.setTextureOffset(29, 41).addBox(-1.4F, -1.0F, -2.0F, 3.0F, 8.0F, 4.0F, 0.0F, false);
        this.legRight = new AdvancedModelBox(this);
        this.legRight.setRotationPoint(-2.9F, 4.0F, 8.0F);
        this.body.addChild(this.legRight);
        this.legRight.setTextureOffset(29, 41).addBox(-1.6F, -1.0F, -2.0F, 3.0F, 8.0F, 4.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = new ModelAnimator();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.bubble, this.whiskersLeft, this.whiskersRight, this.armLeft, this.armRight, this.legLeft, this.legRight, this.tail1, this.tail2, new AdvancedModelBox[] { this.tail3 });
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.update(entity);
        this.animator.setAnimation(EntitySnowLeopard.ANIMATION_ATTACK_R);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-10.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(-10.0), Maths.rad(-10.0));
        this.animator.rotate(this.armRight, Maths.rad(25.0), Maths.rad(-20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(0.0));
        this.animator.rotate(this.armRight, Maths.rad(-90.0), Maths.rad(-30.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntitySnowLeopard.ANIMATION_ATTACK_L);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.body, 0.0F, Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.head, 0.0F, Maths.rad(10.0), Maths.rad(10.0));
        this.animator.rotate(this.armLeft, Maths.rad(25.0), Maths.rad(20.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(0.0));
        this.animator.rotate(this.armLeft, Maths.rad(-90.0), Maths.rad(30.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(EntitySnowLeopard entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.7F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float runProgress = 5.0F * limbSwingAmount;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float stalkProgress = entity.prevSneakProgress + (entity.sneakProgress - entity.prevSneakProgress) * partialTick;
        float tackleProgress = entity.prevTackleProgress + (entity.tackleProgress - entity.prevTackleProgress) * partialTick;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        float sleepProgress = entity.prevSleepProgress + (entity.sleepProgress - entity.prevSleepProgress) * partialTick;
        float sitSleepProgress = Math.max(sitProgress, sleepProgress);
        this.swing(this.tail1, idleSpeed, idleDegree * 2.0F, false, 2.0F, 0.0F, ageInTicks, 1.0F - limbSwingAmount);
        this.swing(this.tail2, idleSpeed, idleDegree * 1.5F, false, 2.0F, 0.0F, ageInTicks, 1.0F - limbSwingAmount);
        this.flap(this.tail3, idleSpeed * 1.2F, idleDegree * 1.5F, false, 2.0F, 0.0F, ageInTicks, 1.0F - limbSwingAmount);
        this.swing(this.tail3, idleSpeed * 1.2F, idleDegree * 1.5F, false, 2.0F, 0.0F, ageInTicks, 1.0F - limbSwingAmount);
        this.walk(this.head, idleSpeed * 0.3F, idleDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed * 0.3F, -idleDegree, false, 0.5F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.armRight, walkSpeed, walkDegree * 1.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.armRight, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.armLeft, walkSpeed, walkDegree * 1.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.armLeft, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.legRight, walkSpeed, walkDegree * 1.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.legRight, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.legLeft, walkSpeed, walkDegree * 1.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.legLeft, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.tail1, this.tail2, this.tail3 };
        this.chainSwing(tailBoxes, walkSpeed, walkDegree * 0.5F, -2.5, limbSwing, limbSwingAmount);
        this.progressRotationPrev(this.tail1, runProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail2, runProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail3, runProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, stalkProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legLeft, stalkProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legRight, stalkProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.armLeft, stalkProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.armRight, stalkProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, stalkProgress, Maths.rad(5.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, stalkProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail1, stalkProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, stalkProgress, 0.0F, -0.5F, 4.0F, 5.0F);
        this.progressPositionPrev(this.legLeft, stalkProgress, 0.0F, 1.6F, -2.0F, 5.0F);
        this.progressPositionPrev(this.legRight, stalkProgress, 0.0F, 1.6F, -2.0F, 5.0F);
        this.progressPositionPrev(this.armLeft, stalkProgress, 0.0F, -0.4F, 0.0F, 5.0F);
        this.progressPositionPrev(this.armRight, stalkProgress, 0.0F, -0.4F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, tackleProgress, Maths.rad(-45.0), 0.0F, 0.0F, 3.0F);
        this.progressRotationPrev(this.head, tackleProgress, Maths.rad(45.0), 0.0F, 0.0F, 3.0F);
        this.progressRotationPrev(this.tail1, tackleProgress, Maths.rad(60.0), 0.0F, 0.0F, 3.0F);
        this.progressRotationPrev(this.armRight, tackleProgress, Maths.rad(-25.0), 0.0F, Maths.rad(45.0), 3.0F);
        this.progressRotationPrev(this.armLeft, tackleProgress, Maths.rad(-25.0), 0.0F, Maths.rad(-45.0), 3.0F);
        this.progressRotationPrev(this.legLeft, tackleProgress, Maths.rad(-15.0), 0.0F, Maths.rad(-25.0), 3.0F);
        this.progressRotationPrev(this.legRight, tackleProgress, Maths.rad(-15.0), 0.0F, Maths.rad(25.0), 3.0F);
        this.progressPositionPrev(this.body, tackleProgress, 0.0F, -5.0F, 0.0F, 3.0F);
        this.progressPositionPrev(this.head, tackleProgress, 0.0F, 2.0F, 0.0F, 3.0F);
        this.progressPositionPrev(this.armLeft, tackleProgress, 1.0F, 2.0F, 0.0F, 3.0F);
        this.progressPositionPrev(this.armRight, tackleProgress, -1.0F, 2.0F, 0.0F, 3.0F);
        this.progressPositionPrev(this.tail1, tackleProgress, 0.0F, 0.0F, -1.0F, 3.0F);
        float tailAngle = entity.m_19879_() % 2 == 0 ? 1.0F : -1.0F;
        this.progressRotationPrev(this.legLeft, sitSleepProgress, Maths.rad(-90.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.legRight, sitSleepProgress, Maths.rad(-90.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.armLeft, sitSleepProgress, Maths.rad(-90.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.armRight, sitSleepProgress, Maths.rad(-90.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sitSleepProgress, 0.0F, 3.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.armRight, sitSleepProgress, 0.0F, 2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.armLeft, sitSleepProgress, 0.0F, 2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legRight, sitSleepProgress, 0.0F, 2.8F, -0.5F, 5.0F);
        this.progressPositionPrev(this.legLeft, sitSleepProgress, 0.0F, 2.8F, -0.5F, 5.0F);
        this.progressRotationPrev(this.tail1, sitProgress, Maths.rad(20.0), Maths.rad((double) (tailAngle * 30.0F)), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail2, sitProgress, Maths.rad(-5.0), Maths.rad((double) (tailAngle * 50.0F)), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail3, sitProgress, Maths.rad(10.0), Maths.rad((double) (tailAngle * 20.0F)), Maths.rad((double) (tailAngle * 20.0F)), 5.0F);
        this.progressRotationPrev(this.tail1, sleepProgress, Maths.rad(20.0), Maths.rad((double) (tailAngle * -60.0F)), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail2, sleepProgress, Maths.rad(10.0), Maths.rad((double) (tailAngle * -70.0F)), Maths.rad((double) (tailAngle * -50.0F)), 5.0F);
        this.progressRotationPrev(this.tail3, sleepProgress, Maths.rad(-30.0), Maths.rad((double) (tailAngle * -50.0F)), Maths.rad((double) (tailAngle * -30.0F)), 5.0F);
        this.progressRotationPrev(this.body, sleepProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.armLeft, sleepProgress, Maths.rad(-10.0), Maths.rad(-30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.armRight, sleepProgress, Maths.rad(-10.0), Maths.rad(30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.legRight, sleepProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legRight, sleepProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail1, sleepProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, sleepProgress, Maths.rad(-10.0), 0.0F, Maths.rad(-10.0), 5.0F);
        this.progressPositionPrev(this.head, sleepProgress, 0.0F, 5.0F, -1.0F, 5.0F);
        if (sleepProgress >= 5.0F) {
            float f = (float) ((double) sleepProgress * Math.max(Math.sin((double) (ageInTicks * 0.05F)), 0.0) * 0.2F);
            this.bubble.showModel = true;
            this.bubble.setScale(f, f, f);
        } else {
            this.bubble.showModel = false;
        }
        if (sleepProgress <= 0.0F) {
            this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.45F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}