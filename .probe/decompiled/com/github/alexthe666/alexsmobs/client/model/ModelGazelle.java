package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityGazelle;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelGazelle extends AdvancedEntityModel<EntityGazelle> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox earL;

    private final AdvancedModelBox earR;

    private final AdvancedModelBox snout;

    private final AdvancedModelBox hornL;

    private final AdvancedModelBox hornR;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox frontlegR;

    private final AdvancedModelBox frontlegL;

    private final AdvancedModelBox backlegL;

    private final AdvancedModelBox backlegR;

    private ModelAnimator animator;

    public ModelGazelle() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, 20.8F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-4.0F, -16.8F, -9.0F, 8.0F, 8.0F, 18.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setPos(0.0F, -14.8F, -8.0F);
        this.body.addChild(this.neck);
        this.setRotationAngle(this.neck, 0.2618F, 0.0F, 0.0F);
        this.neck.setTextureOffset(0, 0).addBox(-2.0F, -7.0F, -2.0F, 4.0F, 9.0F, 4.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -7.0F, 0.0F);
        this.neck.addChild(this.head);
        this.setRotationAngle(this.head, -0.2618F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 27).addBox(-2.5F, -4.0F, -3.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);
        this.earL = new AdvancedModelBox(this, "earL");
        this.earL.setPos(1.5F, -3.3F, 0.5F);
        this.head.addChild(this.earL);
        this.setRotationAngle(this.earL, -0.2618F, -0.5236F, 0.6109F);
        this.earL.setTextureOffset(0, 38).addBox(-0.5F, -3.7F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        this.earR = new AdvancedModelBox(this, "earR");
        this.earR.setPos(-1.5F, -3.3F, 0.5F);
        this.head.addChild(this.earR);
        this.setRotationAngle(this.earR, -0.2618F, 0.5236F, -0.6109F);
        this.earR.setTextureOffset(0, 38).addBox(-1.5F, -3.7F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, true);
        this.snout = new AdvancedModelBox(this, "snout");
        this.snout.setPos(0.0F, -0.5F, -2.9F);
        this.head.addChild(this.snout);
        this.snout.setTextureOffset(34, 27).addBox(-1.5F, -1.5F, -3.1F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        this.hornL = new AdvancedModelBox(this, "hornL");
        this.hornL.setPos(1.3F, -3.4F, -1.9F);
        this.head.addChild(this.hornL);
        this.setRotationAngle(this.hornL, -0.2618F, 0.0F, 0.2618F);
        this.hornL.setTextureOffset(35, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.hornR = new AdvancedModelBox(this, "hornR");
        this.hornR.setPos(-1.3F, -3.4F, -1.9F);
        this.head.addChild(this.hornR);
        this.setRotationAngle(this.hornR, -0.2618F, 0.0F, -0.2618F);
        this.hornR.setTextureOffset(35, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, -13.8F, 9.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.3491F, 0.0F, 0.0F);
        this.tail.setTextureOffset(35, 12).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
        this.frontlegR = new AdvancedModelBox(this, "frontlegR");
        this.frontlegR.setPos(2.5F, -6.8F, -6.5F);
        this.body.addChild(this.frontlegR);
        this.frontlegR.setTextureOffset(34, 34).addBox(-6.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, true);
        this.frontlegL = new AdvancedModelBox(this, "frontlegL");
        this.frontlegL.setPos(2.5F, -6.8F, -6.5F);
        this.body.addChild(this.frontlegL);
        this.frontlegL.setTextureOffset(34, 34).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, false);
        this.backlegL = new AdvancedModelBox(this, "backlegL");
        this.backlegL.setPos(2.5F, -7.8F, 7.5F);
        this.body.addChild(this.backlegL);
        this.backlegL.setTextureOffset(21, 27).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, false);
        this.backlegR = new AdvancedModelBox(this, "backlegR");
        this.backlegR.setPos(-2.5F, -7.8F, 7.5F);
        this.body.addChild(this.backlegR);
        this.backlegR.setTextureOffset(21, 27).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.animator.update(entity);
        this.animator.setAnimation(EntityGazelle.ANIMATION_FLICK_TAIL);
        this.animator.startKeyframe(2);
        this.animator.rotate(this.tail, 0.0F, 0.0F, Maths.rad(50.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.tail, 0.0F, 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.tail, 0.0F, 0.0F, Maths.rad(-50.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.tail, 0.0F, 0.0F, Maths.rad(50.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.tail, 0.0F, 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.tail, 0.0F, 0.0F, Maths.rad(-50.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(2);
        this.animator.setAnimation(EntityGazelle.ANIMATION_FLICK_EARS);
        this.animator.startKeyframe(2);
        this.animator.rotate(this.neck, Maths.rad(25.0), Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.body, 0.0F, Maths.rad(5.0), 0.0F);
        this.animator.rotate(this.earR, 0.0F, Maths.rad(25.0), Maths.rad(40.0));
        this.animator.rotate(this.earL, 0.0F, Maths.rad(-25.0), Maths.rad(-40.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.neck, Maths.rad(25.0), Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.body, 0.0F, Maths.rad(-5.0), 0.0F);
        this.animator.rotate(this.earR, 0.0F, 0.0F, 0.0F);
        this.animator.rotate(this.earL, 0.0F, 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.neck, Maths.rad(25.0), Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.body, 0.0F, Maths.rad(5.0), 0.0F);
        this.animator.rotate(this.earR, 0.0F, Maths.rad(5.0), Maths.rad(-40.0));
        this.animator.rotate(this.earL, 0.0F, Maths.rad(-5.0), Maths.rad(40.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.neck, Maths.rad(25.0), Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, Maths.rad(-10.0));
        this.animator.rotate(this.earR, 0.0F, Maths.rad(25.0), Maths.rad(40.0));
        this.animator.rotate(this.earL, 0.0F, Maths.rad(-25.0), Maths.rad(-40.0));
        this.animator.rotate(this.body, 0.0F, Maths.rad(-5.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.neck, Maths.rad(25.0), Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(5.0), 0.0F, Maths.rad(10.0));
        this.animator.rotate(this.body, 0.0F, Maths.rad(5.0), 0.0F);
        this.animator.rotate(this.earR, 0.0F, 0.0F, 0.0F);
        this.animator.rotate(this.earL, 0.0F, 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.neck, 0.0F, 0.0F, 0.0F);
        this.animator.rotate(this.head, 0.0F, 0.0F, 0.0F);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-5.0), 0.0F);
        this.animator.rotate(this.earR, 0.0F, Maths.rad(5.0), Maths.rad(-40.0));
        this.animator.rotate(this.earL, 0.0F, Maths.rad(-5.0), Maths.rad(40.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(7);
        this.animator.setAnimation(EntityGazelle.ANIMATION_EAT_GRASS);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, Maths.rad(100.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-40.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(120.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-50.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(100.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-40.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(120.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-50.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(100.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-40.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.neck, Maths.rad(120.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-50.0), 0.0F, 0.0F);
        this.eatPose();
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    private void eatPose() {
        this.animator.rotate(this.body, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, 2.0F, 0.0F);
        this.animator.rotate(this.backlegL, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.backlegR, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.frontlegL, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.frontlegR, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.move(this.frontlegL, 0.1F, -3.0F, 0.0F);
        this.animator.move(this.frontlegR, -0.1F, -3.0F, 0.0F);
        this.animator.move(this.backlegL, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.backlegR, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.neck, 0.0F, 1.0F, 0.0F);
    }

    public void setupAnim(EntityGazelle entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        boolean running = entityIn.isRunning();
        float runSpeed = 0.7F;
        float runDegree = 0.7F;
        float walkSpeed = 0.7F;
        float walkDegree = 0.4F;
        float idleSpeed = 0.05F;
        float idleDegree = 0.1F;
        this.faceTarget(netHeadYaw, headPitch, 2.0F, new AdvancedModelBox[] { this.neck, this.head });
        this.walk(this.neck, idleSpeed, idleDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.tail, idleSpeed * 3.0F, idleDegree, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed, -idleDegree, false, 0.5F, 0.0F, ageInTicks, 1.0F);
        if (running) {
            this.walk(this.body, runSpeed, runDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.neck, runSpeed, runDegree * 0.2F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.frontlegR, runSpeed, runDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.frontlegL, runSpeed, runDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.bob(this.frontlegR, runSpeed, runDegree * 2.0F, true, limbSwing, limbSwingAmount);
            this.bob(this.frontlegL, runSpeed, runDegree * 2.0F, true, limbSwing, limbSwingAmount);
            this.walk(this.backlegR, runSpeed, runDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.backlegL, runSpeed, runDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.backlegR, runSpeed, runDegree * 0.2F, true, 0.0F, -0.2F, limbSwing, limbSwingAmount);
            this.flap(this.backlegL, runSpeed, runDegree * 0.2F, false, 0.0F, -0.2F, limbSwing, limbSwingAmount);
            this.bob(this.body, runSpeed, runDegree * 8.0F, false, limbSwing, limbSwingAmount);
        } else {
            this.walk(this.body, walkSpeed, walkDegree * 0.05F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.neck, walkSpeed, walkDegree * 0.5F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.head, walkSpeed, -walkDegree * 0.5F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.frontlegR, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.frontlegL, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.backlegR, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.backlegL, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.75F;
            this.head.setScale(f, f, f);
            this.hornL.setScale(0.4F, 0.4F, 0.4F);
            this.hornR.setScale(0.4F, 0.4F, 0.4F);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
            this.hornL.setScale(1.0F, 1.0F, 1.0F);
            this.hornR.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.neck, this.head, this.earL, this.earR, this.backlegL, this.backlegR, this.frontlegL, this.frontlegR, this.snout, this.hornL, this.hornR, new AdvancedModelBox[] { this.tail });
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}