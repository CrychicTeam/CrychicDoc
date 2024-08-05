package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityMoose;
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

public class ModelMoose extends AdvancedEntityModel<EntityMoose> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox right_leg;

    private final AdvancedModelBox upper_body;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox left_ear;

    private final AdvancedModelBox right_ear;

    private final AdvancedModelBox beard;

    private final ModelAnimator animator;

    public ModelMoose() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -28.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(37, 80).addBox(-6.0F, -8.0F, -4.0F, 12.0F, 15.0F, 20.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setPos(4.7F, 6.0F, -13.0F);
        this.body.addChild(this.left_arm);
        this.left_arm.setTextureOffset(19, 58).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 21.0F, 4.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setPos(-4.7F, 6.0F, -13.0F);
        this.body.addChild(this.right_arm);
        this.right_arm.setTextureOffset(19, 58).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 21.0F, 4.0F, 0.0F, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setPos(3.7F, 6.0F, 14.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(0, 58).addBox(-2.0F, 1.0F, -3.0F, 4.0F, 21.0F, 5.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setPos(-3.7F, 6.0F, 14.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(0, 58).addBox(-2.0F, 1.0F, -3.0F, 4.0F, 21.0F, 5.0F, 0.0F, true);
        this.upper_body = new AdvancedModelBox(this, "upper_body");
        this.upper_body.setPos(0.0F, -1.0F, -4.0F);
        this.body.addChild(this.upper_body);
        this.upper_body.setTextureOffset(52, 45).addBox(-7.0F, -10.0F, -13.0F, 14.0F, 18.0F, 13.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setPos(0.0F, -6.0F, -14.0F);
        this.upper_body.addChild(this.neck);
        this.neck.setTextureOffset(45, 0).addBox(-4.0F, -3.0F, -6.0F, 8.0F, 9.0F, 7.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, 0.0F, -7.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(51, 18).addBox(-3.0F, -3.0F, -15.0F, 6.0F, 7.0F, 16.0F, 0.0F, false);
        this.head.setTextureOffset(0, 34).addBox(3.0F, -12.0F, -7.0F, 18.0F, 9.0F, 14.0F, 0.0F, false);
        this.head.setTextureOffset(0, 34).addBox(-21.0F, -12.0F, -7.0F, 18.0F, 9.0F, 14.0F, 0.0F, true);
        this.left_ear = new AdvancedModelBox(this, "left_ear");
        this.left_ear.setPos(1.3F, -3.0F, 0.5F);
        this.head.addChild(this.left_ear);
        this.setRotationAngle(this.left_ear, -0.3054F, -0.2618F, 0.3927F);
        this.left_ear.setTextureOffset(11, 0).addBox(-0.3F, -4.0F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        this.right_ear = new AdvancedModelBox(this, "right_ear");
        this.right_ear.setPos(-1.3F, -3.0F, 0.5F);
        this.head.addChild(this.right_ear);
        this.setRotationAngle(this.right_ear, -0.3054F, 0.2618F, -0.3927F);
        this.right_ear.setTextureOffset(11, 0).addBox(-1.7F, -4.0F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, true);
        this.beard = new AdvancedModelBox(this, "beard");
        this.beard.setPos(0.0F, 4.0F, 0.0F);
        this.head.addChild(this.beard);
        this.beard.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -4.0F, 0.0F, 6.0F, 5.0F, 0.0F, false);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.left_ear, this.right_ear, this.head, this.neck, this.body, this.upper_body, this.beard, this.left_leg, this.right_leg, this.left_arm, this.right_arm, new AdvancedModelBox[0]);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityMoose.ANIMATION_EAT_GRASS);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(4.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(70.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(0.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(70.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(0.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(70.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityMoose.ANIMATION_ATTACK);
        this.animator.startKeyframe(8);
        this.eatPose();
        this.animator.rotate(this.neck, Maths.rad(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.neck, Maths.rad(-34.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(4);
    }

    private void eatPose() {
        this.animator.rotate(this.body, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, 2.0F, 0.0F);
        this.animator.rotate(this.left_leg, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_leg, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-10.0), 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.right_arm, Maths.rad(-10.0), 0.0F, Maths.rad(10.0));
        this.animator.move(this.left_arm, 0.1F, -3.0F, 0.0F);
        this.animator.move(this.right_arm, -0.1F, -3.0F, 0.0F);
        this.animator.move(this.left_leg, 0.0F, -0.2F, 0.0F);
        this.animator.move(this.right_leg, 0.0F, -0.2F, 0.0F);
        this.animator.move(this.neck, 0.0F, 1.0F, 0.0F);
    }

    public void setupAnim(EntityMoose entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.7F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float runProgress = 5.0F * limbSwingAmount;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float jostleProgress = entityIn.prevJostleProgress + (entityIn.jostleProgress - entityIn.prevJostleProgress) * partialTick;
        float jostleAngle = entityIn.prevJostleAngle + (entityIn.getJostleAngle() - entityIn.prevJostleAngle) * partialTick;
        this.flap(this.beard, idleSpeed, idleDegree * 4.0F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.left_ear, idleSpeed, idleDegree, false, 1.0F, -0.2F, ageInTicks, 1.0F);
        this.flap(this.right_ear, idleSpeed, idleDegree, true, 1.0F, 0.2F, ageInTicks, 1.0F);
        this.walk(this.neck, idleSpeed, idleDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed, -idleDegree, false, 0.5F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.body, walkSpeed, walkDegree * 0.05F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.neck, walkSpeed, walkDegree * 0.25F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.head, walkSpeed, -walkDegree * 0.25F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_arm, walkSpeed, walkDegree * 1.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.right_arm, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.left_arm, walkSpeed, walkDegree * 1.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.left_arm, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.right_leg, walkSpeed, walkDegree * 1.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.right_leg, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.left_leg, walkSpeed, walkDegree * 1.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.left_leg, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.progressRotationPrev(this.neck, jostleProgress, Maths.rad(7.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, jostleProgress, Maths.rad(80.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.neck, jostleProgress, 0.0F, 0.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.head, jostleProgress, 0.0F, 0.0F, -1.0F, 5.0F);
        if (jostleProgress > 0.0F) {
            float yawAmount = jostleAngle / (180.0F / (float) Math.PI) * 0.5F * jostleProgress * 0.2F;
            this.neck.rotateAngleY += yawAmount;
            this.head.rotateAngleY += yawAmount;
            this.head.rotateAngleZ += yawAmount;
        } else {
            this.faceTarget(netHeadYaw, headPitch, 2.0F, new AdvancedModelBox[] { this.neck, this.head });
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.35F;
            float feet = 1.45F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            this.right_arm.setScale(1.0F, feet, 1.0F);
            this.left_arm.setScale(1.0F, feet, 1.0F);
            this.right_leg.setScale(1.0F, feet, 1.0F);
            this.left_leg.setScale(1.0F, feet, 1.0F);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.35F, 0.35F, 0.35F);
            matrixStackIn.translate(0.0, 2.25, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
            this.right_arm.setScale(1.0F, 1.0F, 1.0F);
            this.left_arm.setScale(1.0F, 1.0F, 1.0F);
            this.right_leg.setScale(1.0F, 1.0F, 1.0F);
            this.left_leg.setScale(1.0F, 1.0F, 1.0F);
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

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}