package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityPotoo;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ModelPotoo extends AdvancedEntityModel<EntityPotoo> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox left_wing;

    private final AdvancedModelBox right_wing;

    private final AdvancedModelBox left_foot;

    private final AdvancedModelBox right_foot;

    private final AdvancedModelBox head;

    private final AdvancedModelBox left_eye;

    private final AdvancedModelBox left_pupil;

    private final AdvancedModelBox right_eye;

    private final AdvancedModelBox right_pupil;

    public ModelPotoo() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-3.5F, -4.0F, -3.0F, 7.0F, 8.0F, 6.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 4.0F, 2.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 26).addBox(-2.5F, 0.0F, -1.0F, 5.0F, 8.0F, 2.0F, 0.0F, false);
        this.left_wing = new AdvancedModelBox(this, "left_wing");
        this.left_wing.setRotationPoint(3.5F, -2.0F, 0.0F);
        this.body.addChild(this.left_wing);
        this.left_wing.setTextureOffset(22, 21).addBox(0.0F, -1.0F, -2.0F, 1.0F, 8.0F, 5.0F, 0.0F, false);
        this.right_wing = new AdvancedModelBox(this, "right_wing");
        this.right_wing.setRotationPoint(-3.5F, -2.0F, 0.0F);
        this.body.addChild(this.right_wing);
        this.right_wing.setTextureOffset(22, 21).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 8.0F, 5.0F, 0.0F, true);
        this.left_foot = new AdvancedModelBox(this, "left_foot");
        this.left_foot.setRotationPoint(2.5F, 3.9F, -2.0F);
        this.body.addChild(this.left_foot);
        this.left_foot.setTextureOffset(21, 0).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);
        this.right_foot = new AdvancedModelBox(this, "right_foot");
        this.right_foot.setRotationPoint(-2.5F, 3.9F, -2.0F);
        this.body.addChild(this.right_foot);
        this.right_foot.setTextureOffset(21, 0).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 2.0F, 3.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -4.0F, 3.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 15).addBox(-3.5F, -4.0F, -6.0F, 7.0F, 4.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(21, 9).addBox(-3.5F, -0.7F, -6.0F, 7.0F, 0.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-0.5F, -1.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.left_eye = new AdvancedModelBox(this, "left_eye");
        this.left_eye.setRotationPoint(4.0F, -2.4F, -4.4F);
        this.head.addChild(this.left_eye);
        this.left_eye.setTextureOffset(30, 16).addBox(-1.0F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        this.left_pupil = new AdvancedModelBox(this, "left_pupil");
        this.left_pupil.setRotationPoint(0.1F, 0.0F, 0.0F);
        this.left_eye.addChild(this.left_pupil);
        this.left_pupil.setTextureOffset(21, 16).addBox(-1.08F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        this.right_eye = new AdvancedModelBox(this, "right_eye");
        this.right_eye.setRotationPoint(-4.0F, -2.4F, -4.4F);
        this.head.addChild(this.right_eye);
        this.right_eye.setTextureOffset(30, 16).addBox(-1.0F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, 0.0F, true);
        this.right_pupil = new AdvancedModelBox(this, "right_pupil");
        this.right_pupil.setRotationPoint(-0.1F, 0.0F, 0.0F);
        this.right_eye.addChild(this.right_pupil);
        this.right_pupil.setTextureOffset(21, 16).addBox(-0.92F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.left_wing, this.right_wing, this.head, this.left_foot, this.left_pupil, this.left_eye, this.right_foot, this.right_pupil, this.right_eye, new AdvancedModelBox[0]);
    }

    public void setupAnim(EntityPotoo entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = Minecraft.getInstance().getFrameTime();
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTick;
        float perchProgress = entity.prevPerchProgress + (entity.perchProgress - entity.prevPerchProgress) * partialTick;
        float mouthProgress = entity.prevMouthProgress + (entity.mouthProgress - entity.prevMouthProgress) * partialTick;
        float flapSpeed = 0.8F;
        float flapDegree = 0.2F;
        float walkSpeed = 1.6F;
        float walkDegree = 0.8F;
        float eyeScale = Mth.clamp((15.0F - entity.getEyeScale(10, partialTick)) / 15.0F, 0.0F, 1.0F);
        this.left_pupil.setScale(0.5F, 1.0F + eyeScale * 2.1F, 1.0F + eyeScale * 2.1F);
        this.left_pupil.rotationPointX += 0.5F;
        this.right_pupil.setScale(0.5F, 1.0F + eyeScale * 2.1F, 1.0F + eyeScale * 2.1F);
        this.right_pupil.rotationPointX -= 0.5F;
        if (entity.isSleeping()) {
            this.right_eye.showModel = false;
            this.right_pupil.showModel = false;
            this.left_eye.showModel = false;
            this.left_pupil.showModel = false;
        } else {
            this.right_eye.showModel = true;
            this.right_pupil.showModel = true;
            this.left_eye.showModel = true;
            this.left_pupil.showModel = true;
        }
        float walkAmount = (5.0F - flyProgress) * 0.2F;
        float walkSwingAmount = limbSwingAmount * walkAmount;
        this.progressRotationPrev(this.body, Math.min(walkSwingAmount, 0.5F), Maths.rad(20.0), 0.0F, 0.0F, 0.5F);
        this.progressRotationPrev(this.left_foot, Math.min(walkSwingAmount, 0.5F), Maths.rad(-20.0), 0.0F, 0.0F, 0.5F);
        this.progressRotationPrev(this.right_foot, Math.min(walkSwingAmount, 0.5F), Maths.rad(-20.0), 0.0F, 0.0F, 0.5F);
        this.progressRotationPrev(this.tail, 5.0F - perchProgress, Maths.rad(85.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, flyProgress * limbSwingAmount, Maths.rad(80.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_wing, flyProgress, Maths.rad(-90.0), Maths.rad(90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_wing, flyProgress, Maths.rad(-90.0), Maths.rad(-90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, flyProgress, Maths.rad(-60.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, mouthProgress, Maths.rad(-70.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, mouthProgress, 0.0F, 0.5F, -0.5F, 5.0F);
        this.flap(this.body, walkSpeed, walkDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, walkSwingAmount);
        if (flyProgress > 0.0F) {
            this.bob(this.body, flapSpeed * 0.5F, flapDegree * 4.0F, true, ageInTicks, 1.0F);
            this.swing(this.right_wing, flapSpeed, flapDegree * 5.0F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.swing(this.left_wing, flapSpeed, flapDegree * 5.0F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        }
        this.left_foot.rotationPointZ = this.left_foot.rotationPointZ + 2.0F * (float) (Math.sin((double) (limbSwing * walkSpeed) - 2.5) * (double) walkSwingAmount * (double) walkDegree - (double) (walkSwingAmount * walkDegree));
        this.right_foot.rotationPointZ = this.right_foot.rotationPointZ + 2.0F * (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) walkSwingAmount * (double) walkDegree - (double) (walkSwingAmount * walkDegree));
        this.left_foot.rotationPointY = this.left_foot.rotationPointY + (float) (Math.sin((double) (limbSwing * walkSpeed) - 2.5) * (double) walkSwingAmount * (double) walkDegree - (double) (walkSwingAmount * walkDegree));
        this.right_foot.rotationPointY = this.right_foot.rotationPointY + (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) walkSwingAmount * (double) walkDegree - (double) (walkSwingAmount * walkDegree));
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.25F;
            this.right_eye.setScale(f, f, f);
            this.left_eye.setScale(f, f, f);
            this.right_eye.setShouldScaleChildren(true);
            this.left_eye.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.right_eye.setScale(1.0F, 1.0F, 1.0F);
            this.left_eye.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }
}