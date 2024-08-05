package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityManedWolf;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;

public class ModelManedWolf extends AdvancedEntityModel<EntityManedWolf> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head_pivot;

    private final AdvancedModelBox head;

    private final AdvancedModelBox left_ear_pivot;

    private final AdvancedModelBox right_ear_pivot;

    private final AdvancedModelBox left_ear;

    private final AdvancedModelBox right_ear;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox right_leg;

    public ModelManedWolf() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -17.0F, 2.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-3.0F, -4.0F, -10.0F, 6.0F, 8.0F, 17.0F, 0.0F, false);
        this.body.setTextureOffset(0, 26).addBox(-3.5F, -4.4F, -10.1F, 7.0F, 5.0F, 8.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setRotationPoint(0.0F, -1.0F, -9.0F);
        this.body.addChild(this.neck);
        this.setRotationAngle(this.neck, -0.6545F, 0.0F, 0.0F);
        this.neck.setTextureOffset(30, 0).addBox(-2.5F, -3.0F, -6.0F, 5.0F, 5.0F, 7.0F, 0.0F, false);
        this.neck.setTextureOffset(42, 39).addBox(-1.5F, -6.0F, -5.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);
        this.head_pivot = new AdvancedModelBox(this, "head_pivot");
        this.head_pivot.setRotationPoint(0.0F, -0.8F, -6.0F);
        this.neck.addChild(this.head_pivot);
        this.setRotationAngle(this.head_pivot, 0.6545F, 0.0F, 0.0F);
        this.head = new AdvancedModelBox(this, "head");
        this.head_pivot.addChild(this.head);
        this.head.setTextureOffset(0, 40).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 5.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(44, 22).addBox(-1.5F, 0.0F, -8.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);
        this.left_ear_pivot = new AdvancedModelBox(this, "left_ear_pivot");
        this.left_ear_pivot.setRotationPoint(2.0F, -2.0F, -1.6F);
        this.head.addChild(this.left_ear_pivot);
        this.right_ear_pivot = new AdvancedModelBox(this, "right_ear_pivot");
        this.right_ear_pivot.setRotationPoint(-2.0F, -2.0F, -1.6F);
        this.head.addChild(this.right_ear_pivot);
        this.left_ear = new AdvancedModelBox(this, "left_ear");
        this.left_ear_pivot.addChild(this.left_ear);
        this.setRotationAngle(this.left_ear, -0.0479F, -0.2129F, 0.2233F);
        this.left_ear.setTextureOffset(47, 13).addBox(-1.0F, -5.0F, -0.4F, 3.0F, 6.0F, 1.0F, 0.0F, false);
        this.right_ear = new AdvancedModelBox(this, "right_ear");
        this.right_ear_pivot.addChild(this.right_ear);
        this.setRotationAngle(this.right_ear, -0.0479F, 0.2129F, -0.2233F);
        this.right_ear.setTextureOffset(47, 13).addBox(-2.0F, -5.0F, -0.4F, 3.0F, 6.0F, 1.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -2.0F, 7.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(31, 26).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 14.0F, 4.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(1.8F, 5.0F, -8.0F);
        this.body.addChild(this.left_arm);
        this.left_arm.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 13.0F, 2.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-1.8F, 5.0F, -8.0F);
        this.body.addChild(this.right_arm);
        this.right_arm.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 13.0F, 2.0F, 0.0F, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(2.0F, 3.0F, 5.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(23, 42).addBox(-0.8F, -2.0F, -0.9F, 2.0F, 16.0F, 3.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-2.0F, 3.0F, 5.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(23, 42).addBox(-1.2F, -2.0F, -0.9F, 2.0F, 16.0F, 3.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head_pivot, this.head, this.neck, this.tail, this.left_ear, this.right_ear, this.left_arm, this.right_arm, this.left_leg, this.right_leg, new AdvancedModelBox[] { this.right_ear_pivot, this.left_ear_pivot });
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.35F;
            float feet = 0.8F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            this.left_arm.setScale(1.0F, feet, 1.0F);
            this.right_arm.setScale(1.0F, feet, 1.0F);
            this.left_leg.setScale(1.0F, feet, 1.0F);
            this.right_leg.setScale(1.0F, feet, 1.0F);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.65F, 0.65F, 0.65F);
            matrixStackIn.translate(0.0, 1.0, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
            this.left_arm.setScale(1.0F, 1.0F, 1.0F);
            this.right_arm.setScale(1.0F, 1.0F, 1.0F);
            this.left_leg.setScale(1.0F, 1.0F, 1.0F);
            this.right_leg.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setupAnim(EntityManedWolf entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 0.5F;
        float walkDegree = 0.8F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.2F;
        float shakeSpeed = 0.9F;
        float shakeDegree = 0.4F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float runProgress = 10.0F * Math.min(limbSwingAmount, 0.5F);
        float danceProgress = entity.prevDanceProgress + (entity.danceProgress - entity.prevDanceProgress) * partialTick;
        float shakeProgress = entity.prevShakeProgress + (entity.shakeProgress - entity.prevShakeProgress) * partialTick;
        float earPitch = entity.prevEarPitch + (entity.getEarPitch() - entity.prevEarPitch) * partialTick;
        float earYaw = entity.prevEarYaw + (entity.getEarYaw() - entity.prevEarYaw) * partialTick;
        this.left_ear_pivot.rotateAngleX += earPitch * (float) (Math.PI / 180.0);
        this.left_ear_pivot.rotateAngleY += earYaw * (float) (Math.PI / 180.0);
        this.right_ear_pivot.rotateAngleX += earPitch * (float) (Math.PI / 180.0);
        this.right_ear_pivot.rotateAngleY -= earYaw * (float) (Math.PI / 180.0);
        this.head.rotateAngleY += netHeadYaw * 0.5F * (float) (Math.PI / 180.0);
        this.progressRotationPrev(this.tail, runProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, runProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, runProgress, Maths.rad(-40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, danceProgress, Maths.rad(-40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, danceProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, danceProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, danceProgress, Maths.rad(-30.0), 0.0F, Maths.rad(-15.0), 5.0F);
        this.progressRotationPrev(this.right_leg, danceProgress, Maths.rad(-30.0), 0.0F, Maths.rad(15.0), 5.0F);
        this.progressRotationPrev(this.head, danceProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, danceProgress, Maths.rad(20.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, danceProgress, 0.0F, 6.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, danceProgress, 0.0F, -2.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, danceProgress, 0.0F, -2.0F, 1.0F, 5.0F);
        this.progressRotationPrev(this.neck, shakeProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, shakeProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, shakeProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.swing(this.body, 0.25F, 0.5F, false, 0.0F, 0.0F, ageInTicks, danceProgress * 0.2F);
        this.walk(this.body, 0.5F, 0.3F, false, 0.0F, 0.0F, ageInTicks, danceProgress * 0.2F);
        this.walk(this.left_leg, 0.5F, -0.3F, false, 0.0F, 0.0F, ageInTicks, danceProgress * 0.2F);
        this.walk(this.right_leg, 0.5F, -0.3F, false, 0.0F, 0.0F, ageInTicks, danceProgress * 0.2F);
        this.walk(this.left_arm, 0.5F, -0.6F, false, 0.0F, 0.0F, ageInTicks, danceProgress * 0.2F);
        this.walk(this.right_arm, 0.5F, -0.6F, false, 0.0F, 0.0F, ageInTicks, danceProgress * 0.2F);
        this.walk(this.neck, 0.5F, 0.3F, false, 0.0F, 0.0F, ageInTicks, danceProgress * 0.2F);
        this.flap(this.neck, 0.25F, 0.9F, false, 0.0F, 0.0F, ageInTicks, danceProgress * 0.2F);
        this.swing(this.head, 0.25F, 0.9F, true, 0.0F, 0.0F, ageInTicks, danceProgress * 0.2F);
        this.flap(this.tail, 0.25F, 0.9F, true, 1.0F, 0.0F, ageInTicks, danceProgress * 0.2F);
        float f1 = 0.2F * danceProgress * (float) (-Math.abs(Math.sin((double) (ageInTicks * 0.5F * 0.5F)) * 8.0));
        float f2 = 0.2F * danceProgress * 6.0F * Mth.cos(ageInTicks * 0.5F + 0.0F) * 0.3F * danceProgress * 0.2F + 0.0F * danceProgress * 0.2F;
        this.body.rotationPointY += f1;
        this.body.rotationPointZ -= f2;
        this.right_leg.rotationPointY += 0.25F * f2;
        this.left_leg.rotationPointY += 0.25F * f2;
        this.right_leg.rotateAngleX = (float) ((double) this.right_leg.rotateAngleX - Math.toRadians((double) (f1 * 5.0F)));
        this.left_leg.rotateAngleX = (float) ((double) this.left_leg.rotateAngleX - Math.toRadians((double) (f1 * 5.0F)));
        this.right_arm.rotationPointY += f2;
        this.left_arm.rotationPointY += f2;
        this.right_arm.rotationPointZ -= -f2 + 0.25F * f1;
        this.left_arm.rotationPointZ -= -f2 + 0.25F * f1;
        this.flap(this.tail, idleSpeed, idleDegree * 0.2F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed, idleDegree * 0.2F, true, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.neck, idleSpeed, idleDegree * 0.2F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.right_leg, walkSpeed, walkDegree * 1.1F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_leg, walkSpeed, walkDegree * 1.1F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree, true, limbSwing, limbSwingAmount);
        this.walk(this.right_arm, walkSpeed, walkDegree * -1.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.right_arm, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.bob(this.left_arm, -walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.left_arm, walkSpeed, walkDegree * -1.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.neck, walkSpeed, walkDegree * 0.2F, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.head, walkSpeed, walkDegree * 0.2F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        if (danceProgress <= 0.0F) {
            this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.neck, this.head });
        }
        this.flap(this.body, shakeSpeed, shakeDegree, false, 0.0F, 0.0F, ageInTicks, shakeProgress * 0.2F);
        this.flap(this.neck, shakeSpeed, shakeDegree, false, 1.0F, 0.0F, ageInTicks, shakeProgress * 0.2F);
        this.flap(this.head, shakeSpeed, shakeDegree, true, 2.0F, 0.0F, ageInTicks, shakeProgress * 0.2F);
        this.flap(this.tail, shakeSpeed, shakeDegree, true, 2.0F, 0.0F, ageInTicks, shakeProgress * 0.2F);
        this.swing(this.body, shakeSpeed, shakeDegree * 0.5F, false, 1.0F, 0.0F, ageInTicks, shakeProgress * 0.2F);
        this.flap(this.left_leg, shakeSpeed, shakeDegree, true, 0.0F, 0.0F, ageInTicks, shakeProgress * 0.2F);
        this.flap(this.right_leg, shakeSpeed, shakeDegree, true, 0.0F, 0.0F, ageInTicks, shakeProgress * 0.2F);
        this.flap(this.left_arm, shakeSpeed, shakeDegree, true, 0.0F, 0.0F, ageInTicks, shakeProgress * 0.2F);
        this.flap(this.right_arm, shakeSpeed, shakeDegree, true, 0.0F, 0.0F, ageInTicks, shakeProgress * 0.2F);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}