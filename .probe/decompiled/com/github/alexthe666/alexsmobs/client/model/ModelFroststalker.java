package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityFroststalker;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelFroststalker extends AdvancedEntityModel<EntityFroststalker> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox icespikesleft;

    private final AdvancedModelBox icespikesright;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox horn;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox armleft;

    private final AdvancedModelBox armright;

    private final AdvancedModelBox legleft;

    private final AdvancedModelBox legright;

    private ModelAnimator animator;

    public ModelFroststalker() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -11.0F, 2.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-3.0F, -4.0F, -10.0F, 6.0F, 7.0F, 16.0F, 0.0F, false);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setRotationPoint(0.0F, -1.0F, 5.0F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(34, 24).addBox(-2.0F, -3.0F, 1.0F, 4.0F, 5.0F, 9.0F, 0.0F, false);
        this.tail1.setTextureOffset(0, 41).addBox(-1.0F, -5.0F, 1.0F, 2.0F, 2.0F, 9.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setRotationPoint(0.0F, -2.0F, 9.0F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(29, 0).addBox(-1.0F, -1.0F, 1.0F, 2.0F, 3.0F, 11.0F, 0.0F, false);
        this.tail2.setTextureOffset(21, 29).addBox(0.0F, -7.0F, 1.0F, 0.0F, 8.0F, 12.0F, 0.0F, false);
        this.icespikesleft = new AdvancedModelBox(this, "icespikesleft");
        this.icespikesleft.setRotationPoint(0.0F, -4.0F, 0.5F);
        this.body.addChild(this.icespikesleft);
        this.setRotationAngle(this.icespikesleft, 0.0F, -0.3927F, 0.0F);
        this.icespikesleft.setTextureOffset(35, 39).addBox(0.0F, -6.0F, -5.5F, 0.0F, 6.0F, 11.0F, 0.0F, false);
        this.icespikesright = new AdvancedModelBox(this, "icespikesright");
        this.icespikesright.setRotationPoint(0.0F, -4.0F, 0.5F);
        this.body.addChild(this.icespikesright);
        this.setRotationAngle(this.icespikesright, 0.0F, 0.3927F, 0.0F);
        this.icespikesright.setTextureOffset(35, 39).addBox(0.0F, -6.0F, -5.5F, 0.0F, 6.0F, 11.0F, 0.0F, true);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setRotationPoint(0.0F, -2.0F, -8.0F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(52, 18).addBox(-1.5F, -4.0F, -3.0F, 3.0F, 6.0F, 5.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -4.0F, -1.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(44, 62).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(5, 29).addBox(-2.0F, -4.0F, -9.0F, 4.0F, 3.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(17, 50).addBox(-1.0F, -6.0F, 0.0F, 2.0F, 6.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(47, 39).addBox(-2.0F, -1.0F, -9.0F, 4.0F, 2.0F, 7.0F, 0.0F, false);
        this.horn = new AdvancedModelBox(this, "horn");
        this.horn.setRotationPoint(0.0F, -3.0F, -3.0F);
        this.head.addChild(this.horn);
        this.setRotationAngle(this.horn, -0.3927F, 0.0F, 0.0F);
        this.horn.setTextureOffset(47, 6).addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 9.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this, "jaw");
        this.jaw.setRotationPoint(0.0F, -1.0F, -2.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(40, 79).addBox(-1.5F, 0.0F, -7.0F, 3.0F, 1.0F, 7.0F, 0.0F, false);
        this.armleft = new AdvancedModelBox(this, "armleft");
        this.armleft.setRotationPoint(3.0F, 2.0F, -7.5F);
        this.body.addChild(this.armleft);
        this.armleft.setTextureOffset(0, 53).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F, false);
        this.armleft.setTextureOffset(0, 24).addBox(-3.0F, 7.0F, -1.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.armright = new AdvancedModelBox(this, "armright");
        this.armright.setRotationPoint(-3.0F, 2.0F, -7.5F);
        this.body.addChild(this.armright);
        this.armright.setTextureOffset(0, 53).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, 0.0F, true);
        this.armright.setTextureOffset(0, 24).addBox(1.0F, 7.0F, -1.5F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        this.legleft = new AdvancedModelBox(this, "legleft");
        this.legleft.setRotationPoint(1.5F, 2.0F, 3.9F);
        this.body.addChild(this.legleft);
        this.legleft.setTextureOffset(0, 0).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 11.0F, 4.0F, 0.0F, false);
        this.legright = new AdvancedModelBox(this, "legright");
        this.legright.setRotationPoint(-1.5F, 2.0F, 3.9F);
        this.body.addChild(this.legright);
        this.legright.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 11.0F, 4.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            this.horn.showModel = false;
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            this.horn.showModel = true;
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.animator.update(entity);
        this.animator.setAnimation(EntityFroststalker.ANIMATION_BITE);
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(40.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-50.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityFroststalker.ANIMATION_SPEAK);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(3);
        this.animator.setAnimation(EntityFroststalker.ANIMATION_SLASH_L);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, Maths.rad(-15.0), Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.neck, Maths.rad(15.0), Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.legright, Maths.rad(15.0), Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.legleft, Maths.rad(15.0), Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.armleft, Maths.rad(-50.0), 0.0F, Maths.rad(-105.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.neck, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.move(this.armleft, 0.0F, 0.0F, -2.0F);
        this.animator.rotate(this.armleft, Maths.rad(-90.0), 0.0F, Maths.rad(15.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityFroststalker.ANIMATION_SLASH_R);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, Maths.rad(-15.0), Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.neck, Maths.rad(15.0), Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.legright, Maths.rad(15.0), Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.legleft, Maths.rad(15.0), Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.armright, Maths.rad(-50.0), 0.0F, Maths.rad(105.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.neck, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.move(this.armright, 0.0F, 0.0F, -2.0F);
        this.animator.rotate(this.armright, Maths.rad(-90.0), 0.0F, Maths.rad(-15.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityFroststalker.ANIMATION_SHOVE);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 4.0F);
        this.animator.move(this.legright, 0.0F, 0.0F, -1.0F);
        this.animator.move(this.legleft, 0.0F, 0.0F, -1.0F);
        this.animator.rotate(this.legright, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.legleft, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(35.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.move(this.body, 0.0F, 0.0F, -7.0F);
        this.animator.rotate(this.legright, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.legleft, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail1, this.tail2, this.icespikesleft, this.icespikesright, this.neck, this.head, this.horn, this.jaw, this.armleft, this.armright, new AdvancedModelBox[] { this.legleft, this.legright });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setupAnim(EntityFroststalker entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.7F;
        float walkDegree = 0.4F;
        float spikeSpeed = 0.9F;
        float spikeDegree = 0.4F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.4F;
        float partialTick = ageInTicks - (float) entityIn.f_19797_;
        float turnAngle = entityIn.prevTurnAngle + (entityIn.getTurnAngle() - entityIn.prevTurnAngle) * partialTick;
        float bipedProgress = entityIn.prevBipedProgress + (entityIn.bipedProgress - entityIn.prevBipedProgress) * partialTick;
        float quadProgress = 5.0F - bipedProgress;
        float tackleProgress = entityIn.prevTackleProgress + (entityIn.tackleProgress - entityIn.prevTackleProgress) * partialTick;
        float spikeProgress = entityIn.prevSpikeShakeProgress + (entityIn.spikeShakeProgress - entityIn.prevSpikeShakeProgress) * partialTick;
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.tail1, this.tail2 };
        this.chainSwing(tailBoxes, idleSpeed, idleDegree * 0.3F, -2.0, ageInTicks, 1.0F);
        this.walk(this.neck, idleSpeed * 0.4F, idleDegree * 0.2F, false, 1.0F, -0.01F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed * 0.4F, idleDegree * 0.2F, true, 1.0F, -0.01F, ageInTicks, 1.0F);
        this.chainSwing(tailBoxes, walkSpeed, walkDegree, -3.0, limbSwing, limbSwingAmount);
        this.walk(this.body, walkSpeed, walkDegree * 0.1F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount * bipedProgress * 0.2F);
        this.walk(this.legleft, walkSpeed, walkDegree * 1.85F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.legright, walkSpeed, walkDegree * 1.85F, true, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.armleft, walkSpeed, walkDegree * 1.85F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount * quadProgress * 0.2F);
        this.walk(this.armright, walkSpeed, walkDegree * 1.85F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount * quadProgress * 0.2F);
        this.bob(this.body, walkSpeed * 0.5F, walkDegree * 4.0F, true, limbSwing, limbSwingAmount * bipedProgress * 0.2F);
        this.progressRotationPrev(this.armright, bipedProgress, Maths.rad(20.0), Maths.rad(10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.armleft, bipedProgress, Maths.rad(20.0), Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, bipedProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, bipedProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.neck, bipedProgress, 0.0F, 0.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.head, bipedProgress, 0.0F, 0.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.tail1, quadProgress, 0.0F, 0.0F, -1.0F, 5.0F);
        this.progressRotationPrev(this.tail1, quadProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail2, quadProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.flap(this.neck, walkSpeed, walkDegree * 0.5F, false, 4.0F, 0.0F, limbSwing, limbSwingAmount * quadProgress * 0.2F);
        this.flap(this.head, walkSpeed, walkDegree * 0.5F, true, 4.0F, 0.0F, limbSwing, limbSwingAmount * quadProgress * 0.2F);
        this.walk(this.armleft, walkSpeed, walkDegree * 0.5F, true, 3.0F, 0.12F, limbSwing, limbSwingAmount * bipedProgress * 0.2F);
        this.walk(this.armright, walkSpeed, walkDegree * 0.5F, true, 3.0F, 0.12F, limbSwing, limbSwingAmount * bipedProgress * 0.2F);
        this.walk(this.neck, walkSpeed, walkDegree * 0.5F, false, 2.0F, -0.1F, limbSwing, limbSwingAmount * bipedProgress * 0.2F);
        this.walk(this.head, walkSpeed, walkDegree * 0.5F, true, 2.0F, -0.1F, limbSwing, limbSwingAmount * bipedProgress * 0.2F);
        this.armleft.rotationPointY -= bipedProgress * 0.5F;
        this.armright.rotationPointY -= bipedProgress * 0.5F;
        float yawAmount = turnAngle / (180.0F / (float) Math.PI) * 0.5F * bipedProgress * 0.2F * limbSwingAmount;
        this.head.rotateAngleY -= yawAmount * 0.5F;
        this.neck.rotateAngleY -= yawAmount;
        this.body.rotateAngleZ += yawAmount;
        this.legleft.rotateAngleZ -= yawAmount;
        this.legright.rotationPointY += 1.5F * yawAmount;
        this.legleft.rotationPointY -= 1.5F * yawAmount;
        this.legright.rotateAngleZ -= yawAmount;
        this.progressRotationPrev(this.body, tackleProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, tackleProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, tackleProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.armright, tackleProgress, Maths.rad(-50.0), 0.0F, Maths.rad(40.0), 5.0F);
        this.progressRotationPrev(this.armleft, tackleProgress, Maths.rad(-50.0), 0.0F, Maths.rad(-40.0), 5.0F);
        this.progressRotationPrev(this.legright, tackleProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legleft, tackleProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail1, tackleProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.jaw, tackleProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.chainSwing(tailBoxes, spikeSpeed, spikeDegree, 0.0, ageInTicks, spikeProgress * 0.2F);
        this.flap(this.body, spikeSpeed, spikeDegree, false, 0.0F, 0.0F, ageInTicks, spikeProgress * 0.2F);
        this.flap(this.neck, spikeSpeed, spikeDegree, false, 1.0F, 0.0F, ageInTicks, spikeProgress * 0.2F);
        this.flap(this.head, spikeSpeed, spikeDegree, true, 2.0F, 0.0F, ageInTicks, spikeProgress * 0.2F);
        this.swing(this.body, spikeSpeed, spikeDegree * 0.5F, false, 1.0F, 0.0F, ageInTicks, spikeProgress * 0.2F);
        this.flap(this.legleft, spikeSpeed, spikeDegree, true, 0.0F, 0.0F, ageInTicks, spikeProgress * 0.2F);
        this.flap(this.legright, spikeSpeed, spikeDegree, true, 0.0F, 0.0F, ageInTicks, spikeProgress * 0.2F);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head, this.neck });
    }
}