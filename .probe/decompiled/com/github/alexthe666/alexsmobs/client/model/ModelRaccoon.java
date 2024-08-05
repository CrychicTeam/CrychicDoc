package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityRaccoon;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class ModelRaccoon extends AdvancedEntityModel<EntityRaccoon> {

    public AdvancedModelBox root;

    public AdvancedModelBox body;

    public AdvancedModelBox tail;

    public AdvancedModelBox arm_left;

    public AdvancedModelBox arm_right;

    public AdvancedModelBox leg_left;

    public AdvancedModelBox leg_right;

    public AdvancedModelBox head;

    public AdvancedModelBox ear_left;

    public AdvancedModelBox ear_right;

    public AdvancedModelBox snout;

    public ModelAnimator animator;

    public ModelRaccoon() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -11.0F, 0.5F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-5.5F, -4.0F, -7.5F, 11.0F, 8.0F, 15.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.5F, -1.0F, 7.5F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 24).addBox(-3.0F, -2.0F, 0.0F, 5.0F, 5.0F, 19.0F, 0.0F, false);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setPos(3.0F, 4.0F, -5.5F);
        this.body.addChild(this.arm_left);
        this.arm_left.setTextureOffset(0, 24).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setPos(-3.0F, 4.0F, -5.5F);
        this.body.addChild(this.arm_right);
        this.arm_right.setTextureOffset(0, 24).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.leg_left.setPos(3.0F, 4.0F, 6.5F);
        this.body.addChild(this.leg_left);
        this.leg_left.setTextureOffset(9, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setPos(-3.0F, 4.0F, 6.5F);
        this.body.addChild(this.leg_right);
        this.leg_right.setTextureOffset(9, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, 0.5F, -8.5F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(30, 30).addBox(-4.5F, -4.0F, -4.0F, 9.0F, 7.0F, 5.0F, 0.0F, false);
        this.ear_left = new AdvancedModelBox(this, "ear_left");
        this.ear_left.setPos(3.5F, -4.0F, -2.0F);
        this.head.addChild(this.ear_left);
        this.ear_left.setTextureOffset(9, 24).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        this.ear_right = new AdvancedModelBox(this, "ear_right");
        this.ear_right.setPos(-3.5F, -4.0F, -2.0F);
        this.head.addChild(this.ear_right);
        this.ear_right.setTextureOffset(9, 24).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        this.snout = new AdvancedModelBox(this, "snout");
        this.snout.setPos(0.0F, 1.5F, -5.0F);
        this.head.addChild(this.snout);
        this.snout.setTextureOffset(0, 0).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 3.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.leg_left, this.leg_right, this.arm_left, this.arm_right, this.tail, this.head, this.ear_left, this.ear_right, this.snout);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityRaccoon.ANIMATION_ATTACK);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-120.0), 0.0F, Maths.rad(30.0));
        this.animator.rotate(this.arm_left, Maths.rad(-40.0), 0.0F, Maths.rad(-20.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-40.0), 0.0F, Maths.rad(20.0));
        this.animator.rotate(this.arm_left, Maths.rad(-120.0), 0.0F, Maths.rad(-30.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-120.0), 0.0F, Maths.rad(30.0));
        this.animator.rotate(this.arm_left, Maths.rad(-40.0), 0.0F, Maths.rad(-20.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
    }

    public void setupAnim(EntityRaccoon entityRaccoon, float limbSwing, float limbSwingAmount, float ageInTicks, float v3, float v4) {
        this.animate(entityRaccoon, limbSwing, limbSwingAmount, ageInTicks, v3, v4);
        float partialTicks = Minecraft.getInstance().getFrameTime();
        float normalProgress = 5.0F;
        float walkSpeed = 1.0F;
        float walkDegree = 0.8F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.2F;
        float runProgress = 5.0F * limbSwingAmount;
        float begProgress = entityRaccoon.prevBegProgress + (entityRaccoon.begProgress - entityRaccoon.prevBegProgress) * partialTicks;
        float standProgress0 = entityRaccoon.prevStandProgress + (entityRaccoon.standProgress - entityRaccoon.prevStandProgress) * partialTicks;
        float sitProgress = entityRaccoon.prevSitProgress + (entityRaccoon.sitProgress - entityRaccoon.prevSitProgress) * partialTicks;
        float standProgress = Math.max(Math.max(begProgress, standProgress0) - sitProgress, 0.0F);
        float washProgress = entityRaccoon.prevWashProgress + (entityRaccoon.washProgress - entityRaccoon.prevWashProgress) * partialTicks;
        this.progressRotationPrev(this.body, standProgress, Maths.rad(-70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, standProgress, Maths.rad(70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_right, standProgress, Maths.rad(70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, standProgress, Maths.rad(70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, standProgress, Maths.rad(70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, standProgress, Maths.rad(70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, sitProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, sitProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, sitProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, sitProgress, Maths.rad(-75.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_right, sitProgress, Maths.rad(-75.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, sitProgress, Maths.rad(-80.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, sitProgress, Maths.rad(-80.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 4.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leg_left, sitProgress, 1.5F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leg_right, sitProgress, -1.5F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.arm_left, sitProgress, 0.0F, 2.5F, 1.0F, 5.0F);
        this.progressPositionPrev(this.arm_right, sitProgress, 0.0F, 2.5F, 1.0F, 5.0F);
        this.progressPositionPrev(this.head, standProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, standProgress, 0.0F, -3.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leg_left, standProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leg_right, standProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.arm_left, standProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.arm_right, standProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, standProgress, Maths.rad(80.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, normalProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, normalProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, normalProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_right, normalProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, normalProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, normalProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, normalProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.arm_left, normalProgress, 0.0F, -1.9F, 0.0F, 5.0F);
        this.progressPositionPrev(this.arm_right, normalProgress, 0.0F, -1.9F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leg_left, normalProgress, 0.0F, 0.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.leg_right, normalProgress, 0.0F, 0.0F, -1.0F, 5.0F);
        this.progressRotationPrev(this.tail, 5.0F - runProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, runProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.ear_left, Math.max(runProgress, begProgress), Maths.rad(-20.0), 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.ear_right, Math.max(runProgress, begProgress), Maths.rad(-20.0), 0.0F, Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.arm_right, begProgress, Maths.rad(-25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, begProgress, Maths.rad(-25.0), 0.0F, 0.0F, 5.0F);
        if (begProgress > 0.0F) {
            this.walk(this.head, 0.7F, 0.2F, false, 2.0F, -0.2F, ageInTicks, begProgress * 0.2F);
            this.walk(this.arm_right, 0.7F, 1.2F, false, 0.0F, -1.0F, ageInTicks, begProgress * 0.2F);
            this.flap(this.arm_right, 0.7F, 0.25F, false, -1.0F, 0.2F, ageInTicks, begProgress * 0.2F);
            this.walk(this.arm_left, 0.7F, 1.2F, false, 0.0F, -1.0F, ageInTicks, begProgress * 0.2F);
            this.flap(this.arm_left, 0.7F, 0.25F, true, -1.0F, 0.2F, ageInTicks, begProgress * 0.2F);
        }
        this.progressRotationPrev(this.body, washProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, washProgress, Maths.rad(-90.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_right, washProgress, Maths.rad(-90.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, washProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, washProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, washProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, washProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, washProgress, 0.0F, -3.0F, 0.4F, 5.0F);
        this.progressPositionPrev(this.body, washProgress, 0.0F, 1.5F, -10.0F, 5.0F);
        this.progressPositionPrev(this.arm_left, washProgress, 0.0F, 1.0F, -1.4F, 5.0F);
        this.progressPositionPrev(this.arm_right, washProgress, 0.0F, 1.0F, -1.4F, 5.0F);
        if (washProgress > 0.0F) {
            this.arm_left.rotationPointY = this.arm_left.rotationPointY - (float) (-Math.abs(Math.sin((double) (ageInTicks * 0.5F)) * (double) washProgress * 0.2 * 1.0));
            this.arm_left.rotationPointZ = this.arm_left.rotationPointZ - (float) (-Math.abs(Math.sin((double) (ageInTicks * 0.25F)) * (double) washProgress * 0.2 * 3.0));
            this.arm_right.rotationPointY = this.arm_right.rotationPointY - (float) (-Math.abs(Math.sin((double) (ageInTicks * 0.5F)) * (double) washProgress * 0.2 * 1.0));
            this.arm_right.rotationPointZ = this.arm_right.rotationPointZ - (float) (-Math.abs(Math.cos((double) (ageInTicks * 0.25F)) * (double) washProgress * 0.2 * 3.0));
            this.swing(this.arm_right, 0.5F, 0.25F, false, 2.0F, -0.1F, ageInTicks, washProgress * 0.2F);
            this.swing(this.arm_left, 0.5F, 0.25F, true, 2.0F, -0.1F, ageInTicks, washProgress * 0.2F);
            float bodyFlap = (float) (Math.sin((double) (ageInTicks * 0.5F)) * (double) washProgress * 0.2 * 0.15F);
            this.body.rotateAngleZ += bodyFlap;
            this.tail.rotateAngleY += bodyFlap;
            this.head.rotateAngleZ -= bodyFlap;
            this.leg_left.rotateAngleZ -= bodyFlap;
            this.leg_right.rotateAngleZ -= bodyFlap;
        } else {
            this.faceTarget(v3, v4, 1.3F, new AdvancedModelBox[] { this.head });
        }
        if (standProgress <= 0.0F) {
            this.walk(this.arm_right, walkSpeed, walkDegree * 1.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.arm_left, walkSpeed, walkDegree * 1.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        }
        this.swing(this.tail, idleSpeed, idleDegree, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.body, walkSpeed, walkDegree * 0.3F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 1.0F, false, 4.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leg_right, walkSpeed, walkDegree * 1.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leg_left, walkSpeed, walkDegree * 1.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
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
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public Vec3 getRidingPosition(Vec3 offsetIn) {
        PoseStack armStack = new PoseStack();
        armStack.pushPose();
        this.root.translateAndRotate(armStack);
        this.body.translateAndRotate(armStack);
        Vector4f armOffsetVec = new Vector4f((float) offsetIn.x, (float) offsetIn.y, (float) offsetIn.z, 1.0F);
        armOffsetVec.mul(armStack.last().pose());
        Vec3 vec3 = new Vec3((double) armOffsetVec.x(), (double) armOffsetVec.y(), (double) armOffsetVec.z());
        armStack.popPose();
        return vec3;
    }
}