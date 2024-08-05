package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySeal;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelSeal extends AdvancedEntityModel<EntitySeal> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox tail;

    public final AdvancedModelBox leftLeg;

    public final AdvancedModelBox rightLeg;

    public final AdvancedModelBox leftArm;

    public final AdvancedModelBox rightArm;

    public final AdvancedModelBox head;

    public final AdvancedModelBox leftWhisker;

    public final AdvancedModelBox rightWhisker;

    public ModelSeal() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-6.5F, -6.0F, -9.0F, 13.0F, 9.0F, 18.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 1.0F, 9.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 28).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 6.0F, 14.0F, 0.0F, false);
        this.leftLeg = new AdvancedModelBox(this, "leftLeg");
        this.leftLeg.setRotationPoint(2.0F, -0.2F, 13.4F);
        this.tail.addChild(this.leftLeg);
        this.setRotationAngle(this.leftLeg, 0.0F, 0.3491F, 0.0F);
        this.leftLeg.setTextureOffset(45, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 8.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-2.0F, -0.2F, 13.4F);
        this.tail.addChild(this.rightLeg);
        this.setRotationAngle(this.rightLeg, 0.0F, -0.3491F, 0.0F);
        this.rightLeg.setTextureOffset(45, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 8.0F, 0.0F, true);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(7.5F, 2.5F, -4.0F);
        this.body.addChild(this.leftArm);
        this.leftArm.setTextureOffset(31, 28).addBox(-1.0F, -0.5F, -2.0F, 8.0F, 1.0F, 5.0F, 0.0F, false);
        this.leftArm.setTextureOffset(0, 7).addBox(7.0F, 0.5F, -2.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-7.5F, 2.5F, -4.0F);
        this.body.addChild(this.rightArm);
        this.rightArm.setTextureOffset(31, 28).addBox(-7.0F, -0.5F, -2.0F, 8.0F, 1.0F, 5.0F, 0.0F, true);
        this.rightArm.setTextureOffset(0, 7).addBox(-7.0F, 0.5F, -2.0F, 0.0F, 2.0F, 5.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -1.0F, -5.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(35, 39).addBox(-3.5F, -3.0F, -9.0F, 7.0F, 6.0F, 10.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-2.5F, 0.0F, -12.0F, 5.0F, 3.0F, 3.0F, 0.0F, false);
        this.leftWhisker = new AdvancedModelBox(this, "leftWhisker");
        this.leftWhisker.setRotationPoint(2.5F, 2.0F, -11.0F);
        this.head.addChild(this.leftWhisker);
        this.setRotationAngle(this.leftWhisker, 0.0F, -0.2182F, 0.0F);
        this.leftWhisker.setTextureOffset(0, 7).addBox(0.0F, -2.0F, 0.0F, 2.0F, 3.0F, 0.0F, 0.0F, false);
        this.rightWhisker = new AdvancedModelBox(this, "rightWhisker");
        this.rightWhisker.setRotationPoint(-2.5F, 2.0F, -11.0F);
        this.head.addChild(this.rightWhisker);
        this.setRotationAngle(this.rightWhisker, 0.0F, 0.2182F, 0.0F);
        this.rightWhisker.setTextureOffset(0, 7).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 3.0F, 0.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.leftLeg, this.rightLeg, this.head, this.leftArm, this.rightArm, this.leftWhisker, this.rightWhisker);
    }

    public void setupAnim(EntitySeal entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = this.f_102610_ ? 0.25F : 0.5F;
        float walkDegree = 1.0F;
        float swimSpeed = 0.5F;
        float swimDegree = 0.5F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float baskProgress = entity.prevBaskProgress + (entity.baskProgress - entity.prevBaskProgress) * partialTick;
        float swimAngle = entity.prevSwimAngle + (entity.getSwimAngle() - entity.prevSwimAngle) * partialTick;
        float diggingProgress = entity.prevDigProgress + (entity.digProgress - entity.prevDigProgress) * partialTick;
        float bobbingProgress = entity.prevBobbingProgress + (entity.bobbingProgress - entity.prevBobbingProgress) * partialTick;
        int baskType = entity.isTearsEasterEgg() ? -1 : entity.m_19879_() % 5;
        this.progressRotationPrev(this.body, diggingProgress, Maths.rad(70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, diggingProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, diggingProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftArm, diggingProgress, 0.0F, Maths.rad(30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightArm, diggingProgress, 0.0F, Maths.rad(-30.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, diggingProgress, 0.0F, -12.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.leftArm, diggingProgress, -1.0F, 0.0F, -2.0F, 5.0F);
        this.progressPositionPrev(this.rightArm, diggingProgress, 1.0F, 0.0F, -2.0F, 5.0F);
        this.head.rotationPointZ = this.head.rotationPointZ + (float) (Math.sin((double) (ageInTicks * 0.7F)) * 0.5 * (double) bobbingProgress);
        if (diggingProgress > 0.0F) {
            float amount = diggingProgress * 0.2F;
            this.swing(this.rightArm, 0.6F, 0.85F, true, 1.0F, -0.1F, ageInTicks, amount);
            this.swing(this.leftArm, 0.6F, 0.85F, false, 1.0F, -0.1F, ageInTicks, amount);
            this.walk(this.tail, 0.6F, 0.1F, false, 3.0F, -0.1F, ageInTicks, amount);
            this.bob(this.body, 0.3F, 3.0F, true, ageInTicks, amount);
        }
        if (baskProgress > 0.0F && !entity.isTearsEasterEgg()) {
            this.walk(this.head, 0.05F, 0.2F, true, 1.0F, -0.1F, ageInTicks, 1.0F);
            if (baskType == 0) {
                this.progressRotationPrev(this.body, baskProgress, 0.0F, 0.0F, Maths.rad(70.0), 5.0F);
                this.progressRotationPrev(this.head, baskProgress, 0.0F, Maths.rad(-20.0), Maths.rad(20.0), 5.0F);
                this.progressRotationPrev(this.leftArm, baskProgress, 0.0F, 0.0F, Maths.rad(110.0), 5.0F);
                this.progressRotationPrev(this.rightArm, baskProgress, 0.0F, 0.0F, Maths.rad(-120.0), 5.0F);
                this.progressRotationPrev(this.tail, baskProgress, 0.0F, Maths.rad(15.0), Maths.rad(-20.0), 5.0F);
                this.progressRotationPrev(this.leftLeg, baskProgress, 0.0F, Maths.rad(-15.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.rightLeg, baskProgress, 0.0F, Maths.rad(35.0), Maths.rad(30.0), 5.0F);
                this.progressPositionPrev(this.leftArm, baskProgress, -2.0F, 0.0F, 0.0F, 5.0F);
                this.progressPositionPrev(this.rightArm, baskProgress, 1.0F, 0.0F, 0.0F, 5.0F);
                this.progressPositionPrev(this.head, baskProgress, 0.0F, 0.0F, 1.0F, 5.0F);
                this.progressPositionPrev(this.body, baskProgress, 0.0F, -4.0F, 1.0F, 5.0F);
                this.flap(this.rightArm, 0.05F, 0.2F, true, 3.0F, -0.1F, ageInTicks, 1.0F);
                this.flap(this.leftArm, 0.05F, 0.2F, true, 3.0F, -0.1F, ageInTicks, 1.0F);
            } else if (baskType == 1) {
                this.progressRotationPrev(this.body, baskProgress, 0.0F, 0.0F, Maths.rad(-70.0), 5.0F);
                this.progressRotationPrev(this.head, baskProgress, 0.0F, Maths.rad(20.0), Maths.rad(-20.0), 5.0F);
                this.progressRotationPrev(this.rightArm, baskProgress, 0.0F, 0.0F, Maths.rad(-110.0), 5.0F);
                this.progressRotationPrev(this.leftArm, baskProgress, 0.0F, 0.0F, Maths.rad(120.0), 5.0F);
                this.progressRotationPrev(this.tail, baskProgress, 0.0F, Maths.rad(-15.0), Maths.rad(20.0), 5.0F);
                this.progressRotationPrev(this.rightLeg, baskProgress, 0.0F, Maths.rad(15.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.leftLeg, baskProgress, 0.0F, Maths.rad(-35.0), Maths.rad(-30.0), 5.0F);
                this.progressPositionPrev(this.rightArm, baskProgress, 2.0F, 0.0F, 0.0F, 5.0F);
                this.progressPositionPrev(this.leftArm, baskProgress, -1.0F, 0.0F, 0.0F, 5.0F);
                this.progressPositionPrev(this.head, baskProgress, 0.0F, 0.0F, 1.0F, 5.0F);
                this.progressPositionPrev(this.body, baskProgress, 0.0F, -4.0F, 0.0F, 5.0F);
                this.flap(this.rightArm, 0.05F, 0.2F, false, 3.0F, -0.1F, ageInTicks, 1.0F);
                this.flap(this.leftArm, 0.05F, 0.2F, false, 3.0F, -0.1F, ageInTicks, 1.0F);
            } else if (baskType == 2) {
                this.progressRotationPrev(this.rightArm, baskProgress, 0.0F, 0.0F, Maths.rad(30.0), 5.0F);
                this.progressRotationPrev(this.leftArm, baskProgress, 0.0F, 0.0F, Maths.rad(-40.0), 5.0F);
                this.progressRotationPrev(this.body, baskProgress, 0.0F, 0.0F, Maths.rad(160.0), 5.0F);
                this.progressRotationPrev(this.tail, baskProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
                this.progressRotationPrev(this.head, baskProgress, Maths.rad(-10.0), Maths.rad(20.0), Maths.rad(30.0), 5.0F);
                this.progressPositionPrev(this.body, baskProgress, 0.0F, -4.0F, 0.0F, 5.0F);
                this.progressPositionPrev(this.rightArm, baskProgress, 1.0F, 0.0F, 0.0F, 5.0F);
                this.progressPositionPrev(this.leftArm, baskProgress, -1.0F, 0.0F, 0.0F, 5.0F);
                this.flap(this.rightArm, 0.05F, 0.2F, true, 3.0F, -0.1F, ageInTicks, 1.0F);
                this.flap(this.leftArm, 0.05F, 0.2F, false, 3.0F, -0.1F, ageInTicks, 1.0F);
            } else if (baskType == 3) {
                this.progressRotationPrev(this.body, baskProgress, 0.0F, Maths.rad(20.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.tail, baskProgress, 0.0F, Maths.rad(25.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.head, baskProgress, 0.0F, Maths.rad(-20.0), Maths.rad(25.0), 5.0F);
                this.progressRotationPrev(this.rightArm, baskProgress, 0.0F, Maths.rad(-20.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.leftArm, baskProgress, 0.0F, Maths.rad(30.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.leftLeg, baskProgress, 0.0F, Maths.rad(30.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.rightLeg, baskProgress, 0.0F, Maths.rad(30.0), 0.0F, 5.0F);
                this.progressPositionPrev(this.head, baskProgress, 0.0F, -1.0F, 0.0F, 5.0F);
                this.flap(this.rightArm, 0.05F, 0.2F, true, 3.0F, -0.1F, ageInTicks, 1.0F);
                this.flap(this.leftArm, 0.05F, 0.2F, false, 3.0F, -0.1F, ageInTicks, 1.0F);
            } else if (baskType == 4) {
                this.progressRotationPrev(this.body, baskProgress, 0.0F, Maths.rad(-20.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.tail, baskProgress, 0.0F, Maths.rad(-25.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.head, baskProgress, 0.0F, Maths.rad(20.0), Maths.rad(-25.0), 5.0F);
                this.progressRotationPrev(this.rightArm, baskProgress, 0.0F, Maths.rad(30.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.leftArm, baskProgress, 0.0F, Maths.rad(-20.0), 0.0F, 5.0F);
                this.progressPositionPrev(this.head, baskProgress, 0.0F, -1.0F, 0.0F, 5.0F);
                this.progressRotationPrev(this.leftLeg, baskProgress, 0.0F, Maths.rad(-30.0), 0.0F, 5.0F);
                this.progressRotationPrev(this.rightLeg, baskProgress, 0.0F, Maths.rad(-30.0), 0.0F, 5.0F);
                this.flap(this.rightArm, 0.05F, 0.2F, true, 3.0F, -0.1F, ageInTicks, 1.0F);
                this.flap(this.leftArm, 0.05F, 0.2F, false, 3.0F, -0.1F, ageInTicks, 1.0F);
            }
        }
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[] { this.head, this.body, this.tail };
        if (!entity.m_20069_()) {
            float f1 = walkDegree * 0.3F;
            this.body.rotationPointY = this.body.rotationPointY + 1.4F * Math.min(0.0F, (float) (Math.sin((double) (limbSwing * walkSpeed)) * (double) limbSwingAmount * (double) f1 * 9.0 - (double) (limbSwingAmount * f1) * 9.0));
            this.body.rotationPointZ = this.body.rotationPointZ + (float) (Math.sin((double) (limbSwing * walkSpeed - 1.5F)) * (double) limbSwingAmount * (double) f1 * 9.0 - (double) (limbSwingAmount * f1) * 9.0);
            this.head.rotationPointZ = this.head.rotationPointZ + (float) (Math.sin((double) (limbSwing * walkSpeed - 2.0F)) * (double) limbSwingAmount * (double) f1 * 2.0 - (double) (limbSwingAmount * f1 * 2.0F));
            this.walk(this.body, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.04F, limbSwing, limbSwingAmount);
            this.walk(this.head, walkSpeed, walkDegree * 0.1F, true, 1.0F, 0.04F, limbSwing, limbSwingAmount);
            this.walk(this.tail, walkSpeed, walkDegree * 0.15F, true, 1.0F, 0.06F, limbSwing, limbSwingAmount);
            this.flap(this.rightArm, walkSpeed, walkDegree, true, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.leftArm, walkSpeed, walkDegree, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.rightArm, walkSpeed, walkDegree, false, 2.0F, -0.2F, limbSwing, limbSwingAmount);
            this.swing(this.leftArm, walkSpeed, walkDegree, true, 2.0F, -0.2F, limbSwing, limbSwingAmount);
        } else {
            this.body.rotateAngleX += headPitch * (float) (Math.PI / 180.0);
            this.body.rotationPointY = this.body.rotationPointY + (float) (Math.sin((double) (limbSwing * swimSpeed)) * (double) limbSwingAmount * (double) swimDegree * 9.0 - (double) (limbSwingAmount * swimDegree) * 9.0);
            this.chainWave(bodyParts, swimSpeed, swimDegree, -3.0, limbSwing, limbSwingAmount);
            this.flap(this.rightArm, swimSpeed, swimDegree * 2.5F, true, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.leftArm, swimSpeed, swimDegree * 2.5F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.leftLeg, swimSpeed, swimDegree, false, -4.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.rightLeg, swimSpeed, swimDegree, false, -4.0F, 0.0F, limbSwing, limbSwingAmount);
        }
        if (entity.isTearsEasterEgg() && !entity.m_20069_()) {
            this.swing(this.head, 0.1F, 0.6F, true, 3.0F, 0.0F, ageInTicks, 1.0F);
            this.walk(this.head, 0.1F, 0.1F, true, 2.0F, 0.3F, ageInTicks, 1.0F);
        }
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
        float yawAmount = swimAngle / (180.0F / (float) Math.PI) * 0.5F;
        this.body.rotateAngleZ += yawAmount;
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

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}