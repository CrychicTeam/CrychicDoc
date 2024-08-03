package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityMantisShrimp;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelMantisShrimp extends AdvancedEntityModel<EntityMantisShrimp> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox tail;

    public final AdvancedModelBox legs_front;

    public final AdvancedModelBox legs_back;

    public final AdvancedModelBox head;

    public final AdvancedModelBox flapper_left;

    public final AdvancedModelBox flapper_right;

    public final AdvancedModelBox eye_left;

    public final AdvancedModelBox eye_right;

    public final AdvancedModelBox arm_left;

    public final AdvancedModelBox fist_left;

    public final AdvancedModelBox arm_right;

    public final AdvancedModelBox fist_right;

    public final AdvancedModelBox whisker_left;

    public final AdvancedModelBox whisker_right;

    public ModelMantisShrimp() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -14.0F, -5.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-6.0F, -2.0F, 0.0F, 12.0F, 10.0F, 25.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, 0.0F, 21.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, -0.2182F, 0.0F, 0.0F);
        this.tail.setTextureOffset(50, 0).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 9.0F, 9.0F, 0.0F, false);
        this.legs_front = new AdvancedModelBox(this, "legs_front");
        this.legs_front.setPos(0.0F, 5.0F, -7.0F);
        this.body.addChild(this.legs_front);
        this.legs_front.setTextureOffset(0, 61).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 9.0F, 8.0F, 0.0F, false);
        this.legs_back = new AdvancedModelBox(this, "legs_back");
        this.legs_back.setPos(0.0F, 8.0F, 1.0F);
        this.body.addChild(this.legs_back);
        this.legs_back.setTextureOffset(0, 36).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 6.0F, 18.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, 4.0F, 2.0F);
        this.body.addChild(this.head);
        this.setRotationAngle(this.head, 0.3491F, 0.0F, 0.0F);
        this.head.setTextureOffset(49, 53).addBox(-6.0F, -14.0F, -8.0F, 12.0F, 14.0F, 8.0F, 0.1F, false);
        this.flapper_left = new AdvancedModelBox(this, "flapper_left");
        this.flapper_left.setPos(4.0F, -14.0F, -8.0F);
        this.head.addChild(this.flapper_left);
        this.setRotationAngle(this.flapper_left, -0.48F, 0.2182F, 0.0F);
        this.flapper_left.setTextureOffset(50, 19).addBox(0.0F, 0.0F, 0.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);
        this.flapper_right = new AdvancedModelBox(this, "flapper_right");
        this.flapper_right.setPos(-4.0F, -14.0F, -8.0F);
        this.head.addChild(this.flapper_right);
        this.setRotationAngle(this.flapper_right, -0.48F, -0.2182F, 0.0F);
        this.flapper_right.setTextureOffset(50, 19).addBox(-14.0F, 0.0F, 0.0F, 14.0F, 5.0F, 0.0F, 0.0F, true);
        this.eye_left = new AdvancedModelBox(this, "eye_left");
        this.eye_left.setPos(3.0F, -14.0F, -4.0F);
        this.head.addChild(this.eye_left);
        this.eye_left.setTextureOffset(0, 15).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        this.eye_right = new AdvancedModelBox(this, "eye_right");
        this.eye_right.setPos(-3.0F, -14.0F, -4.0F);
        this.head.addChild(this.eye_right);
        this.eye_right.setTextureOffset(0, 15).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, true);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setPos(4.0F, -1.0F, -8.0F);
        this.head.addChild(this.arm_left);
        this.arm_left.setTextureOffset(0, 36).addBox(-2.5F, -9.0F, -2.0F, 5.0F, 10.0F, 3.0F, 0.0F, false);
        this.fist_left = new AdvancedModelBox(this, "fist_left");
        this.fist_left.setPos(-1.0F, -7.0F, -1.0F);
        this.arm_left.addChild(this.fist_left);
        this.fist_left.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -4.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setPos(-4.0F, -1.0F, -8.0F);
        this.head.addChild(this.arm_right);
        this.arm_right.setTextureOffset(0, 36).addBox(-2.5F, -9.0F, -2.0F, 5.0F, 10.0F, 3.0F, 0.0F, true);
        this.fist_right = new AdvancedModelBox(this, "fist_right");
        this.fist_right.setPos(1.0F, -7.0F, -1.0F);
        this.arm_right.addChild(this.fist_right);
        this.fist_right.setTextureOffset(0, 0).addBox(-3.0F, -1.0F, -4.0F, 4.0F, 10.0F, 4.0F, 0.0F, true);
        this.whisker_left = new AdvancedModelBox(this, "whisker_left");
        this.whisker_left.setPos(1.0F, -14.0F, -8.0F);
        this.head.addChild(this.whisker_left);
        this.setRotationAngle(this.whisker_left, 0.0F, -0.3927F, 0.0F);
        this.whisker_left.setTextureOffset(39, 39).addBox(0.0F, 0.0F, -13.0F, 8.0F, 0.0F, 13.0F, 0.0F, false);
        this.whisker_right = new AdvancedModelBox(this, "whisker_right");
        this.whisker_right.setPos(-1.0F, -14.0F, -8.0F);
        this.head.addChild(this.whisker_right);
        this.setRotationAngle(this.whisker_right, 0.0F, 0.3927F, 0.0F);
        this.whisker_right.setTextureOffset(39, 39).addBox(-8.0F, 0.0F, -13.0F, 8.0F, 0.0F, 13.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.eye_left, this.eye_right, this.fist_left, this.fist_right, this.arm_left, this.arm_right, this.whisker_left, this.whisker_right, this.flapper_left, new AdvancedModelBox[] { this.flapper_right, this.tail, this.legs_back, this.legs_front });
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            this.eye_left.setScale(1.15F, 1.15F, 1.15F);
            this.eye_right.setScale(1.15F, 1.15F, 1.15F);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        } else {
            this.eye_left.setScale(1.0F, 1.0F, 1.0F);
            this.eye_right.setScale(1.0F, 1.0F, 1.0F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setupAnim(EntityMantisShrimp entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.1F;
        float idleDegree = 0.3F;
        float walkSpeed = 0.9F;
        float walkDegree = 0.6F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float swimProgress = Math.min(limbSwingAmount, 0.25F) * 4.0F * (entity.prevInWaterProgress + (entity.inWaterProgress - entity.prevInWaterProgress) * partialTick);
        float punchProgress = entity.prevPunchProgress + (entity.punchProgress - entity.prevPunchProgress) * partialTick;
        float leftEyePitch = entity.prevLeftPitch + (entity.getEyePitch(true) - entity.prevLeftPitch) * partialTick;
        float rightEyePitch = entity.prevRightPitch + (entity.getEyePitch(false) - entity.prevRightPitch) * partialTick;
        float leftEyeYaw = entity.prevLeftYaw + (entity.getEyeYaw(true) - entity.prevLeftYaw) * partialTick;
        float rightEyeYaw = entity.prevRightYaw + (entity.getEyeYaw(false) - entity.prevRightYaw) * partialTick;
        this.eye_left.rotateAngleX += leftEyePitch * (float) (Math.PI / 180.0);
        this.eye_left.rotateAngleY += leftEyeYaw * (float) (Math.PI / 180.0);
        this.eye_right.rotateAngleX += rightEyePitch * (float) (Math.PI / 180.0);
        this.eye_right.rotateAngleY += rightEyeYaw * (float) (Math.PI / 180.0);
        this.head.rotateAngleY += netHeadYaw * 0.5F * (float) (Math.PI / 180.0);
        this.head.rotateAngleX += headPitch * 0.8F * (float) (Math.PI / 180.0);
        this.walk(this.whisker_left, idleSpeed * 1.5F, idleDegree, false, 0.0F, -0.25F, ageInTicks, 1.0F);
        this.walk(this.whisker_right, idleSpeed * 1.5F, idleDegree, true, 0.0F, 0.25F, ageInTicks, 1.0F);
        this.swing(this.whisker_left, idleSpeed * 1.0F, idleDegree * 0.75F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.whisker_right, idleSpeed * 1.0F, idleDegree * 0.75F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.flapper_left, idleSpeed * 1.0F, idleDegree * 0.75F, false, 2.0F, -0.3F, ageInTicks, 1.0F);
        this.swing(this.flapper_right, idleSpeed * 1.0F, idleDegree * 0.75F, true, 2.0F, -0.3F, ageInTicks, 1.0F);
        this.swing(this.arm_left, idleSpeed * 1.0F, idleDegree * 0.5F, false, 1.0F, -0.2F, ageInTicks, 1.0F);
        this.swing(this.arm_right, idleSpeed * 1.0F, idleDegree * 0.5F, true, 1.0F, -0.2F, ageInTicks, 1.0F);
        this.walk(this.legs_front, walkSpeed, walkDegree * 1.0F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.legs_front, walkSpeed * 0.5F, walkDegree * 4.0F, true, limbSwing, limbSwingAmount);
        this.walk(this.legs_back, walkSpeed, walkDegree * 0.2F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.legs_back, walkSpeed * 0.5F, walkDegree * 4.0F, true, limbSwing, limbSwingAmount);
        this.progressRotationPrev(this.head, Math.max(0.0F, swimProgress - punchProgress * 2.5F), Maths.rad(45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.whisker_left, swimProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.whisker_right, swimProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, swimProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_right, swimProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, swimProgress, 0.0F, -6.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.arm_left, swimProgress, 0.0F, -3.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.arm_right, swimProgress, 0.0F, -3.0F, 0.0F, 5.0F);
        if (swimProgress > 0.0F) {
            this.bob(this.body, walkSpeed * 0.5F, walkDegree * 4.0F, true, limbSwing, limbSwingAmount);
            this.walk(this.body, walkSpeed * 1.0F, walkDegree * 0.2F, true, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.tail, walkSpeed * 1.0F, walkDegree * 0.5F, true, 3.0F, -0.2F, limbSwing, limbSwingAmount);
            this.walk(this.head, walkSpeed * 1.0F, walkDegree * 0.1F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        }
        this.progressPositionPrev(this.arm_right, punchProgress, 1.0F, -7.0F, 0.0F, 2.0F);
        this.progressPositionPrev(this.arm_left, punchProgress, -1.0F, -7.0F, 0.0F, 2.0F);
        this.progressPositionPrev(this.fist_right, punchProgress, 0.0F, -2.0F, -2.0F, 2.0F);
        this.progressPositionPrev(this.fist_left, punchProgress, 0.0F, -2.0F, -2.0F, 2.0F);
        this.progressRotationPrev(this.arm_right, punchProgress, Maths.rad(70.0), 0.0F, 0.0F, 2.0F);
        this.progressRotationPrev(this.fist_right, punchProgress, Maths.rad(-240.0), 0.0F, Maths.rad(10.0), 2.0F);
        this.progressRotationPrev(this.arm_left, punchProgress, Maths.rad(70.0), 0.0F, 0.0F, 2.0F);
        this.progressRotationPrev(this.fist_left, punchProgress, Maths.rad(-240.0), 0.0F, Maths.rad(-10.0), 2.0F);
        this.progressRotationPrev(this.flapper_right, punchProgress, 0.0F, Maths.rad(50.0), 0.0F, 2.0F);
        this.progressRotationPrev(this.flapper_left, punchProgress, 0.0F, Maths.rad(-50.0), 0.0F, 2.0F);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}