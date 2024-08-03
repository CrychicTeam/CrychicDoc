package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityLaviathan;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ModelLaviathan extends AdvancedEntityModel<EntityLaviathan> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox rightArm;

    private final AdvancedModelBox leftLeg;

    private final AdvancedModelBox rightLeg;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox neck2;

    private final AdvancedModelBox head;

    private final AdvancedModelBox bottom_jaw;

    private final AdvancedModelBox top_jaw;

    private final AdvancedModelBox shell;

    private final AdvancedModelBox vent1;

    private final AdvancedModelBox vent2;

    private final AdvancedModelBox vent3;

    private final AdvancedModelBox vent4;

    public ModelLaviathan() {
        this.texHeight = 256;
        this.texWidth = 256;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-20.0F, -27.0F, -31.0F, 40.0F, 27.0F, 64.0F, 0.0F, false);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(20.0F, -9.0F, -28.0F);
        this.body.addChild(this.leftArm);
        this.leftArm.setTextureOffset(150, 151).addBox(0.0F, -2.0F, -3.0F, 21.0F, 4.0F, 13.0F, 0.0F, false);
        this.leftArm.setTextureOffset(0, 49).addBox(13.0F, -1.9F, 1.0F, 10.0F, 0.0F, 14.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-20.0F, -9.0F, -28.0F);
        this.body.addChild(this.rightArm);
        this.rightArm.setTextureOffset(150, 151).addBox(-21.0F, -2.0F, -3.0F, 21.0F, 4.0F, 13.0F, 0.0F, true);
        this.rightArm.setTextureOffset(0, 49).addBox(-23.0F, -1.9F, 1.0F, 10.0F, 0.0F, 14.0F, 0.0F, true);
        this.leftLeg = new AdvancedModelBox(this, "leftLeg");
        this.leftLeg.setRotationPoint(20.0F, -8.0F, 21.0F);
        this.body.addChild(this.leftLeg);
        this.leftLeg.setTextureOffset(65, 151).addBox(0.0F, -2.0F, -5.0F, 25.0F, 4.0F, 17.0F, 0.0F, false);
        this.leftLeg.setTextureOffset(0, 30).addBox(23.0F, -1.9F, -4.0F, 4.0F, 0.0F, 18.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-20.0F, -8.0F, 21.0F);
        this.body.addChild(this.rightLeg);
        this.rightLeg.setTextureOffset(65, 151).addBox(-25.0F, -2.0F, -5.0F, 25.0F, 4.0F, 17.0F, 0.0F, true);
        this.rightLeg.setTextureOffset(0, 30).addBox(-27.0F, -1.9F, -4.0F, 4.0F, 0.0F, 18.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -20.0F, 33.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(92, 92).addBox(-7.0F, -4.0F, 0.0F, 14.0F, 9.0F, 49.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setRotationPoint(0.0F, -19.0F, -32.0F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(0, 138).addBox(-7.0F, -5.0F, -35.0F, 14.0F, 11.0F, 36.0F, 0.0F, false);
        this.neck.setTextureOffset(0, 0).addBox(-1.0F, -7.0F, -34.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.neck.setTextureOffset(0, 0).addBox(-1.0F, -7.0F, -25.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.neck.setTextureOffset(0, 0).addBox(-1.0F, -7.0F, -16.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.neck.setTextureOffset(0, 0).addBox(-1.0F, -7.0F, -7.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.neck2 = new AdvancedModelBox(this, "neck2");
        this.neck2.setRotationPoint(0.0F, 0.0F, -35.0F);
        this.neck.addChild(this.neck2);
        this.neck2.setTextureOffset(145, 0).addBox(-5.0F, -4.0F, -39.0F, 10.0F, 9.0F, 39.0F, 0.0F, false);
        this.neck2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -35.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.neck2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -26.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.neck2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -17.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.neck2.setTextureOffset(0, 0).addBox(-1.0F, -6.0F, -8.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -1.0F, -40.0F);
        this.neck2.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-6.0F, -5.0F, -16.0F, 12.0F, 12.0F, 17.0F, 0.0F, false);
        this.head.setTextureOffset(0, 49).addBox(1.0F, -10.0F, -14.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);
        this.head.setTextureOffset(0, 41).addBox(-3.0F, -7.0F, -8.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(27, 30).addBox(0.0F, -8.0F, -4.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        this.bottom_jaw = new AdvancedModelBox(this, "bottom_jaw");
        this.bottom_jaw.setRotationPoint(0.0F, 2.0F, -16.0F);
        this.head.addChild(this.bottom_jaw);
        this.bottom_jaw.setTextureOffset(27, 30).addBox(-4.0F, 0.0F, -9.0F, 8.0F, 3.0F, 9.0F, 0.0F, false);
        this.bottom_jaw.setTextureOffset(0, 92).addBox(-3.5F, -2.0F, -9.0F, 7.0F, 2.0F, 9.0F, 0.0F, false);
        this.top_jaw = new AdvancedModelBox(this, "top_jaw");
        this.top_jaw.setRotationPoint(0.0F, 2.0F, -16.0F);
        this.head.addChild(this.top_jaw);
        this.top_jaw.setTextureOffset(103, 92).addBox(-3.0F, -4.0F, -9.0F, 6.0F, 4.0F, 9.0F, 0.0F, false);
        this.top_jaw.setTextureOffset(0, 104).addBox(-3.0F, 0.0F, -9.0F, 6.0F, 2.0F, 9.0F, 0.0F, false);
        this.top_jaw.setTextureOffset(0, 30).addBox(-2.0F, -4.0F, -11.0F, 4.0F, 8.0F, 2.0F, 0.0F, false);
        this.shell = new AdvancedModelBox(this, "shell");
        this.shell.setRotationPoint(0.0F, -27.0F, 1.0F);
        this.body.addChild(this.shell);
        this.shell.setTextureOffset(0, 92).addBox(-16.0F, -7.0F, -19.0F, 32.0F, 7.0F, 38.0F, 0.0F, false);
        this.vent1 = new AdvancedModelBox(this, "vent1");
        this.vent1.setRotationPoint(10.5F, -27.0F, -26.5F);
        this.body.addChild(this.vent1);
        this.vent1.setTextureOffset(224, 65).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, 0.0F, false);
        this.vent2 = new AdvancedModelBox(this, "vent2");
        this.vent2.setRotationPoint(7.0F, -27.0F, 27.0F);
        this.body.addChild(this.vent2);
        this.vent2.setTextureOffset(216, 104).addBox(-2.0F, -7.0F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, false);
        this.vent3 = new AdvancedModelBox(this, "vent3");
        this.vent3.setRotationPoint(-6.5F, -27.0F, 24.5F);
        this.body.addChild(this.vent3);
        this.vent3.setTextureOffset(182, 103).addBox(-2.5F, -14.0F, -2.5F, 5.0F, 14.0F, 5.0F, 0.0F, false);
        this.vent4 = new AdvancedModelBox(this, "vent4");
        this.vent4.setRotationPoint(-6.0F, -27.0F, -23.0F);
        this.body.addChild(this.vent4);
        this.vent4.setTextureOffset(226, 124).addBox(-3.0F, -13.0F, -3.0F, 6.0F, 13.0F, 6.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void translateRotate(PoseStack stack) {
        this.head.translateRotate(stack);
        this.neck2.translateRotate(stack);
        this.neck.translateRotate(stack);
        this.body.translateRotate(stack);
        this.root.translateRotate(stack);
    }

    public void setupAnim(EntityLaviathan entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = Minecraft.getInstance().getFrameTime();
        float hh1 = entity.prevHeadHeight;
        float hh2 = entity.getHeadHeight();
        float rawHeadHeight = (hh1 + (hh2 - hh1) * partialTick) / 3.0F;
        float clampedNeckRot = Mth.clamp(-rawHeadHeight, -1.0F, 1.0F);
        float headStillProgress = 1.0F - Math.abs(clampedNeckRot);
        float swimProgress = entity.prevSwimProgress + (entity.swimProgress - entity.prevSwimProgress) * partialTick;
        float onLandProgress = Math.max(0.0F, 5.0F - swimProgress);
        float biteProgress = entity.prevBiteProgress + (entity.biteProgress - entity.prevBiteProgress) * partialTick;
        this.neck.rotateAngleX += clampedNeckRot;
        this.neck.rotationPointZ = this.neck.rotationPointZ + Math.abs(clampedNeckRot) * 2.0F;
        this.neck2.rotateAngleX -= clampedNeckRot * 0.4F;
        this.neck2.rotationPointZ = this.neck2.rotationPointZ + Math.abs(clampedNeckRot) * 2.0F;
        this.head.rotateAngleX -= clampedNeckRot * 0.6F;
        this.head.rotationPointZ = this.head.rotationPointZ + Math.abs(clampedNeckRot) * 2.0F;
        this.neck.rotationPointY = this.neck.rotationPointY - Mth.clamp(Math.abs(entity.getHeadYaw(partialTick)) / 50.0F, 0.0F, 1.0F);
        this.neck.rotateAngleY = (float) ((double) this.neck.rotateAngleY + Math.toRadians((double) (entity.getHeadYaw(partialTick) * 0.65F)));
        this.neck2.rotateAngleY = (float) ((double) this.neck2.rotateAngleY + Math.toRadians((double) (entity.getHeadYaw(partialTick) * 0.6F)));
        this.head.rotateAngleY = (float) ((double) this.head.rotateAngleY + Math.toRadians((double) (entity.getHeadYaw(partialTick) * 0.45F)));
        this.progressRotationPrev(this.rightLeg, onLandProgress, 0.0F, 0.0F, Maths.rad(-15.0), 5.0F);
        this.progressRotationPrev(this.leftLeg, onLandProgress, 0.0F, 0.0F, Maths.rad(15.0), 5.0F);
        this.progressRotationPrev(this.rightArm, onLandProgress, 0.0F, 0.0F, Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.leftArm, onLandProgress, 0.0F, 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.head, biteProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.top_jaw, biteProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.bottom_jaw, biteProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, biteProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.bottom_jaw, biteProgress, 0.0F, 2.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.neck, biteProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        float idleSpeed = 0.04F;
        float idleDegree = 0.3F;
        float walkSpeed = 0.9F;
        if (entity.swimProgress >= 5.0F) {
            walkSpeed = 0.3F;
        }
        float walkDegree = 0.5F + swimProgress * 0.05F;
        AdvancedModelBox[] neckBoxes = new AdvancedModelBox[] { this.neck, this.neck2, this.head };
        this.chainWave(neckBoxes, idleSpeed, idleDegree * 0.2F, 9.0, ageInTicks, 1.0F);
        this.walk(this.tail, idleSpeed, idleDegree * 0.4F, false, 1.3F, -0.2F, ageInTicks, 1.0F);
        this.walk(this.bottom_jaw, idleSpeed * 2.0F, idleDegree * 0.4F, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.leftLeg, walkSpeed, walkDegree, true, 2.0F, 0.2F, limbSwing, limbSwingAmount * onLandProgress * 0.2F);
        this.swing(this.rightLeg, walkSpeed, walkDegree, false, 2.0F, 0.2F, limbSwing, limbSwingAmount * onLandProgress * 0.2F);
        this.swing(this.leftArm, walkSpeed, walkDegree, false, 2.0F, -0.25F, limbSwing, limbSwingAmount * onLandProgress * 0.2F);
        this.swing(this.rightArm, walkSpeed, walkDegree, true, 2.0F, -0.25F, limbSwing, limbSwingAmount * onLandProgress * 0.2F);
        this.bob(this.body, -walkSpeed * 0.5F, walkDegree * 3.0F, true, limbSwing, limbSwingAmount);
        this.chainSwing(neckBoxes, walkSpeed, walkDegree * 0.3F, -21.0, limbSwing, limbSwingAmount * swimProgress * 0.2F * headStillProgress);
        this.swing(this.tail, walkSpeed, walkDegree * 0.5F, false, -3.0F, 0.0F, limbSwing, limbSwingAmount * swimProgress * 0.2F);
        this.flap(this.leftLeg, walkSpeed, walkDegree, true, 2.0F, 0.2F, limbSwing, limbSwingAmount * swimProgress * 0.2F);
        this.flap(this.rightLeg, walkSpeed, walkDegree, false, 2.0F, 0.2F, limbSwing, limbSwingAmount * swimProgress * 0.2F);
        this.flap(this.leftArm, walkSpeed, walkDegree, false, 2.0F, -0.25F, limbSwing, limbSwingAmount * swimProgress * 0.2F);
        this.flap(this.rightArm, walkSpeed, walkDegree, true, 2.0F, -0.25F, limbSwing, limbSwingAmount * swimProgress * 0.2F);
        this.flap(this.leftLeg, idleSpeed, idleDegree, false, 1.0F, 0.2F, ageInTicks, swimProgress * 0.2F);
        this.flap(this.rightLeg, idleSpeed, idleDegree, true, 1.0F, 0.2F, ageInTicks, swimProgress * 0.2F);
        this.flap(this.leftArm, idleSpeed, idleDegree, false, 1.0F, 0.2F, ageInTicks, swimProgress * 0.2F);
        this.flap(this.rightArm, idleSpeed, idleDegree, true, 1.0F, 0.2F, ageInTicks, swimProgress * 0.2F);
        this.tail.rotationPointZ -= limbSwingAmount * swimProgress * 0.2F;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg, this.tail, this.neck, this.neck2, this.head, this.bottom_jaw, this.top_jaw, new AdvancedModelBox[] { this.shell, this.vent1, this.vent2, this.vent3, this.vent4 });
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.45F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }
}