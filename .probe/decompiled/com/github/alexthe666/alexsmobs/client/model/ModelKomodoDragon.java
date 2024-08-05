package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityKomodoDragon;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelKomodoDragon extends AdvancedEntityModel<EntityKomodoDragon> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox tongue;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox left_shoulder;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox right_shoulder;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox left_hip;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox right_hip;

    private final AdvancedModelBox right_leg;

    public ModelKomodoDragon(float scale) {
        this.texHeight = 128;
        this.texWidth = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -11.0F, -1.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-5.0F, -4.0F, -11.0F, 10.0F, 9.0F, 23.0F, scale, false);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setRotationPoint(0.0F, -1.0F, -10.0F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(0, 60).addBox(-3.5F, -3.0F, -7.0F, 7.0F, 7.0F, 6.0F, scale, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -1.0F, -7.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(44, 0).addBox(-3.5F, -2.0F, -10.0F, 7.0F, 5.0F, 10.0F, scale, false);
        this.tongue = new AdvancedModelBox(this, "tongue");
        this.tongue.setRotationPoint(0.0F, 1.0F, -10.0F);
        this.head.addChild(this.tongue);
        this.tongue.setTextureOffset(60, 26).addBox(-1.5F, 0.0F, -7.0F, 3.0F, 0.0F, 7.0F, scale, false);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setRotationPoint(0.0F, -1.0F, 11.0F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(0, 33).addBox(-3.0F, -2.0F, 1.0F, 6.0F, 6.0F, 20.0F, scale, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setRotationPoint(0.0F, 1.0F, 20.0F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(35, 42).addBox(-1.5F, -2.0F, 1.0F, 3.0F, 4.0F, 18.0F, scale, false);
        this.left_shoulder = new AdvancedModelBox(this, "left_shoulder");
        this.left_shoulder.setRotationPoint(1.5F, 2.0F, -5.5F);
        this.body.addChild(this.left_shoulder);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(4.5F, 0.0F, -0.5F);
        this.left_shoulder.addChild(this.left_arm);
        this.left_arm.setTextureOffset(0, 33).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale, false);
        this.left_arm.setTextureOffset(57, 34).addBox(-4.0F, 8.99F, -5.0F, 8.0F, 0.0F, 7.0F, scale, false);
        this.right_shoulder = new AdvancedModelBox(this, "right_shoulder");
        this.right_shoulder.setRotationPoint(-1.5F, 2.0F, -5.5F);
        this.body.addChild(this.right_shoulder);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-4.5F, 0.0F, -0.5F);
        this.right_shoulder.addChild(this.right_arm);
        this.right_arm.setTextureOffset(0, 33).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale, true);
        this.right_arm.setTextureOffset(57, 34).addBox(-4.0F, 8.99F, -5.0F, 8.0F, 0.0F, 7.0F, scale, true);
        this.left_hip = new AdvancedModelBox(this, "left_hip");
        this.left_hip.setRotationPoint(1.5F, 2.0F, 7.5F);
        this.body.addChild(this.left_hip);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(2.5F, 0.0F, 0.5F);
        this.left_hip.addChild(this.left_leg);
        this.left_leg.setTextureOffset(0, 0).addBox(-1.0F, -3.0F, -2.0F, 4.0F, 12.0F, 5.0F, scale, false);
        this.left_leg.setTextureOffset(33, 33).addBox(-3.0F, 8.99F, -5.0F, 8.0F, 0.0F, 7.0F, scale, false);
        this.right_hip = new AdvancedModelBox(this, "right_hip");
        this.right_hip.setRotationPoint(-1.5F, 2.0F, 7.5F);
        this.body.addChild(this.right_hip);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-2.5F, 0.0F, 0.5F);
        this.right_hip.addChild(this.right_leg);
        this.right_leg.setTextureOffset(0, 0).addBox(-3.0F, -3.0F, -2.0F, 4.0F, 12.0F, 5.0F, scale, true);
        this.right_leg.setTextureOffset(33, 33).addBox(-5.0F, 8.99F, -5.0F, 8.0F, 0.0F, 7.0F, scale, true);
        this.setRotationAngle(this.tail1, Maths.rad(-23.0), 0.0F, 0.0F);
        this.setRotationAngle(this.tail2, Maths.rad(23.0), 0.0F, 0.0F);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityKomodoDragon entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.7F;
        float idleDegree = 0.7F;
        float walkSpeed = 0.5F;
        float walkDegree = 0.7F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float sitProgress = entityIn.prevSitProgress + (entityIn.sitProgress - entityIn.prevSitProgress) * partialTick;
        float jostleProgress = entityIn.prevJostleProgress + (entityIn.jostleProgress - entityIn.prevJostleProgress) * partialTick;
        float jostleAngle = entityIn.prevJostleAngle + (entityIn.getJostleAngle() - entityIn.prevJostleAngle) * partialTick;
        float toungeF = (float) Math.min(Math.sin((double) (ageInTicks * 0.3F)), 0.0) * 9.0F;
        float toungeMinus = (float) Math.max(Math.sin((double) (ageInTicks * 0.3F)), 0.0);
        this.progressRotationPrev(this.tail1, limbSwingAmount, Maths.rad(13.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail2, limbSwingAmount, Maths.rad(-13.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, sitProgress, 0.0F, 3.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, sitProgress, 0.0F, 3.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, sitProgress, 0.0F, 1.5F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, sitProgress, 0.0F, 1.5F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, sitProgress, Maths.rad(-25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, sitProgress, Maths.rad(8.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail1, sitProgress, Maths.rad(35.0), Maths.rad(15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, sitProgress, Maths.rad(25.0), Maths.rad(-25.0), Maths.rad(70.0), 5.0F);
        this.progressRotationPrev(this.left_leg, sitProgress, Maths.rad(25.0), Maths.rad(25.0), Maths.rad(-70.0), 5.0F);
        this.progressRotationPrev(this.right_arm, sitProgress, Maths.rad(25.0), Maths.rad(-15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, sitProgress, Maths.rad(25.0), Maths.rad(15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.body, jostleProgress, Maths.rad(-55.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, jostleProgress, 0.0F, -6.0F, -3.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, jostleProgress, Maths.rad(55.0), -Maths.rad(15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, jostleProgress, Maths.rad(55.0), Maths.rad(15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail1, jostleProgress, Maths.rad(60.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, jostleProgress, Maths.rad(2.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, jostleProgress, Maths.rad(14.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, jostleProgress, 0.0F, 0.0F, -2.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, jostleProgress, 0.0F, 0.0F, -2.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, jostleProgress, Maths.rad(-10.0), Maths.rad(90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, jostleProgress, Maths.rad(-10.0), Maths.rad(-90.0), 0.0F, 5.0F);
        this.flap(this.body, walkSpeed, walkDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.body, walkSpeed, walkDegree * 0.5F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.neck, walkSpeed, walkDegree * -0.25F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.neck, walkSpeed, walkDegree * -0.25F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.head, walkSpeed, walkDegree * -0.25F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.head, walkSpeed, walkDegree * -0.25F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.tail1, walkSpeed, walkDegree * -0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail1, walkSpeed, walkDegree * 0.5F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail2, walkSpeed, walkDegree * 0.5F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_arm, walkSpeed, walkDegree * 1.2F, false, -2.5F, -0.25F, limbSwing, limbSwingAmount);
        this.walk(this.right_arm, walkSpeed, walkDegree * 1.2F, true, -2.5F, 0.25F, limbSwing, limbSwingAmount);
        this.walk(this.right_leg, walkSpeed, walkDegree * 1.2F, false, -2.5F, 0.25F, limbSwing, limbSwingAmount);
        this.walk(this.left_leg, walkSpeed, walkDegree * 1.2F, true, -2.5F, -0.25F, limbSwing, limbSwingAmount);
        this.flap(this.left_arm, walkSpeed, walkDegree, false, -2.5F, -0.25F, limbSwing, limbSwingAmount);
        this.flap(this.right_arm, walkSpeed, walkDegree, false, -2.5F, 0.25F, limbSwing, limbSwingAmount);
        this.flap(this.right_leg, walkSpeed, walkDegree, false, -2.5F, 0.25F, limbSwing, limbSwingAmount);
        this.flap(this.left_leg, walkSpeed, walkDegree, false, -2.5F, -0.25F, limbSwing, limbSwingAmount);
        this.left_arm.rotationPointY = this.left_arm.rotationPointY + 1.5F * (float) (Math.sin((double) (limbSwing * walkSpeed) - 2.5) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
        this.right_arm.rotationPointY = this.right_arm.rotationPointY + 1.5F * (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
        this.left_leg.rotationPointY = this.left_leg.rotationPointY + 1.5F * (float) (Math.sin((double) (limbSwing * walkSpeed) - 2.5) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
        this.right_leg.rotationPointY = this.right_leg.rotationPointY + 1.5F * (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
        this.walk(this.tongue, idleSpeed * 2.0F, idleDegree, false, 0.0F, 0.0F, ageInTicks, toungeMinus);
        this.walk(this.neck, idleSpeed * 0.1F, idleDegree * 0.1F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed * 0.1F, idleDegree * 0.1F, false, 4.0F, 0.0F, ageInTicks, 1.0F);
        this.tongue.rotationPointZ -= toungeF;
        this.progressPositionPrev(this.neck, jostleProgress, 0.0F, 0.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.head, jostleProgress, 0.0F, 0.0F, 1.0F, 5.0F);
        if (jostleProgress > 0.0F) {
            float jostleScale = 2.5F;
            float yawAmount = jostleAngle / (180.0F / (float) Math.PI) * jostleProgress * 0.2F * jostleScale;
            float inv = (10.0F - Math.abs(jostleAngle)) / (180.0F / (float) Math.PI) * jostleProgress * 0.2F * jostleScale * 0.5F;
            this.right_shoulder.rotateAngleX += yawAmount;
            this.left_shoulder.rotateAngleX += yawAmount;
            this.neck.rotateAngleY += yawAmount;
            this.head.rotateAngleY += yawAmount;
            this.neck.rotateAngleX -= inv;
            this.head.rotateAngleX -= inv;
        } else {
            this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.neck, this.head });
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.75F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.35F, 0.35F, 0.35F);
            matrixStackIn.translate(0.0, 2.75, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
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
        return ImmutableList.of(this.root, this.body, this.tail2, this.tail1, this.left_leg, this.right_leg, this.left_shoulder, this.right_shoulder, this.left_arm, this.right_arm, this.left_hip, this.right_hip, new AdvancedModelBox[] { this.neck, this.head, this.tongue });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}