package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityWarpedToad;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ModelWarpedToad extends AdvancedEntityModel<EntityWarpedToad> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox tongue;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox sac;

    private final AdvancedModelBox left_gland;

    private final AdvancedModelBox right_gland;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox left_foot_pivot;

    private final AdvancedModelBox left_foot;

    private final AdvancedModelBox right_leg;

    private final AdvancedModelBox right_foot;

    private final AdvancedModelBox right_foot_pivot;

    private final AdvancedModelBox head;

    private final AdvancedModelBox left_eye;

    private final AdvancedModelBox right_eye;

    public ModelWarpedToad() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -8.0F, 1.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 24).addBox(-7.0F, -2.0F, -12.0F, 14.0F, 7.0F, 11.0F, -0.001F, false);
        this.body.setTextureOffset(0, 43).addBox(-7.0F, 0.0F, -12.0F, 14.0F, 0.0F, 11.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-7.0F, -6.0F, -1.0F, 14.0F, 11.0F, 12.0F, -0.001F, false);
        this.tongue = new AdvancedModelBox(this, "tongue");
        this.tongue.setPos(0.0F, -1.0F, -1.0F);
        this.body.addChild(this.tongue);
        this.tongue.setTextureOffset(0, 55).addBox(-4.0F, 0.0F, -10.0F, 8.0F, 0.0F, 10.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setPos(6.4F, 2.0F, -4.0F);
        this.body.addChild(this.left_arm);
        this.left_arm.setTextureOffset(50, 69).addBox(-1.0F, -1.01F, -2.0F, 3.0F, 7.0F, 4.0F, 0.0F, false);
        this.left_arm.setTextureOffset(27, 55).addBox(-4.0F, 6.0F, -3.0F, 6.0F, 0.0F, 5.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setPos(-6.4F, 2.0F, -4.0F);
        this.body.addChild(this.right_arm);
        this.right_arm.setTextureOffset(50, 69).addBox(-2.0F, -1.01F, -2.0F, 3.0F, 7.0F, 4.0F, 0.0F, true);
        this.right_arm.setTextureOffset(27, 55).addBox(-2.0F, 6.0F, -3.0F, 6.0F, 0.0F, 5.0F, 0.0F, true);
        this.sac = new AdvancedModelBox(this, "sac");
        this.sac.setPos(0.0F, 5.0F, -1.0F);
        this.body.addChild(this.sac);
        this.sac.setTextureOffset(42, 13).addBox(-7.0F, -5.0F, -11.0F, 14.0F, 5.0F, 11.0F, -0.1F, false);
        this.left_gland = new AdvancedModelBox(this, "left_gland");
        this.left_gland.setPos(5.0F, -5.0F, 3.1F);
        this.body.addChild(this.left_gland);
        this.left_gland.setTextureOffset(0, 66).addBox(-2.0F, -2.0F, -4.0F, 5.0F, 4.0F, 8.0F, 0.0F, false);
        this.right_gland = new AdvancedModelBox(this, "right_gland");
        this.right_gland.setPos(-5.0F, -5.0F, 3.1F);
        this.body.addChild(this.right_gland);
        this.right_gland.setTextureOffset(0, 66).addBox(-3.0F, -2.0F, -4.0F, 5.0F, 4.0F, 8.0F, 0.0F, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setPos(6.0F, 1.5F, 8.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(40, 50).addBox(-4.0F, -1.0F, -6.0F, 6.0F, 7.0F, 11.0F, 0.0F, false);
        this.left_foot_pivot = new AdvancedModelBox(this, "left_foot_pivot");
        this.left_foot_pivot.setPos(0.0F, 5.5F, -3.0F);
        this.left_leg.addChild(this.left_foot_pivot);
        this.left_foot = new AdvancedModelBox(this, "left_foot");
        this.left_foot_pivot.addChild(this.left_foot);
        this.left_foot.setTextureOffset(64, 50).addBox(-2.0F, 0.0F, -2.0F, 10.0F, 1.0F, 4.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setPos(-6.0F, 1.5F, 8.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(40, 50).addBox(-2.0F, -1.0F, -6.0F, 6.0F, 7.0F, 11.0F, 0.0F, true);
        this.right_foot_pivot = new AdvancedModelBox(this, "right_foot_pivot");
        this.right_foot_pivot.setPos(0.0F, 5.5F, -3.0F);
        this.right_leg.addChild(this.right_foot_pivot);
        this.right_foot = new AdvancedModelBox(this, "right_foot");
        this.right_foot_pivot.addChild(this.right_foot);
        this.right_foot.setTextureOffset(64, 50).addBox(-8.0F, 0.0F, -2.0F, 10.0F, 1.0F, 4.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -2.0F, -3.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(40, 32).addBox(-7.0F, -4.0F, -9.0F, 14.0F, 6.0F, 11.0F, 0.0F, false);
        this.head.setTextureOffset(41, 0).addBox(-7.0F, -1.0F, -9.0F, 14.0F, 0.0F, 11.0F, 0.0F, false);
        this.left_eye = new AdvancedModelBox(this, "left_eye");
        this.left_eye.setPos(7.0F, -4.0F, -6.0F);
        this.head.addChild(this.left_eye);
        this.left_eye.setTextureOffset(27, 69).addBox(-5.0F, -2.0F, 0.0F, 5.0F, 2.0F, 6.0F, 0.0F, false);
        this.left_eye.setTextureOffset(19, 66).addBox(-2.0F, -4.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        this.right_eye = new AdvancedModelBox(this, "right_eye");
        this.right_eye.setPos(-7.0F, -4.0F, -6.0F);
        this.head.addChild(this.right_eye);
        this.right_eye.setTextureOffset(27, 69).addBox(0.0F, -2.0F, 0.0F, 5.0F, 2.0F, 6.0F, 0.0F, true);
        this.right_eye.setTextureOffset(19, 66).addBox(0.0F, -4.0F, 0.0F, 2.0F, 2.0F, 4.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.right_eye, this.left_eye, this.sac, this.right_arm, this.right_leg, this.right_foot, this.right_gland, this.left_arm, this.left_leg, new AdvancedModelBox[] { this.left_foot, this.left_gland, this.tongue, this.right_foot_pivot, this.left_foot_pivot });
    }

    public void setupAnim(EntityWarpedToad entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = Minecraft.getInstance().getFrameTime();
        float attackProgress = entity.prevAttackProgress + (entity.attackProgress - entity.prevAttackProgress) * partialTick;
        float walkSpeed = 1.2F;
        float walkDegree = 0.5F;
        float swimSpeed = 0.4F;
        float swimDegree = 0.9F;
        float blinkProgress = Math.max(attackProgress, entity.prevBlinkProgress + (entity.blinkProgress - entity.prevBlinkProgress) * partialTick);
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        float swimProgress = entity.prevSwimProgress + (entity.swimProgress - entity.prevSwimProgress) * partialTick;
        float fallProgress = entity.prevReboundProgress + (entity.reboundProgress - entity.prevReboundProgress) * partialTick;
        float jumpProgress = Math.max(0.0F, entity.prevJumpProgress + (entity.jumpProgress - entity.prevJumpProgress) * partialTick - fallProgress);
        float glowyBob = 0.75F + (Mth.cos(ageInTicks * 0.2F) + 1.0F) * 0.125F + attackProgress * 0.1F;
        float toungeScale = 1.0F + attackProgress * 0.285F * entity.getTongueLength();
        float toungeScaleCorners = 1.0F - attackProgress * 0.1F;
        this.right_gland.setScale(glowyBob, glowyBob, glowyBob);
        this.left_gland.setScale(glowyBob, glowyBob, glowyBob);
        this.tongue.setScale(toungeScaleCorners, toungeScaleCorners, toungeScale);
        this.progressPositionPrev(this.right_eye, blinkProgress, 0.1F, 1.5F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_eye, blinkProgress, -0.1F, 1.5F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, attackProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tongue, attackProgress, Maths.rad(-5.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, attackProgress, 0.0F, 0.0F, -1.3F, 5.0F);
        this.progressPositionPrev(this.left_arm, sitProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, sitProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, sitProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, sitProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, sitProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, sitProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, sitProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, sitProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, sitProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, swimProgress, Maths.rad(90.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, swimProgress, Maths.rad(90.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, swimProgress, 3.0F, -1.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, swimProgress, -3.0F, -1.0F, 1.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, swimProgress, Maths.rad(-90.0), Maths.rad(-60.0), Maths.rad(30.0), 5.0F);
        this.progressRotationPrev(this.right_arm, swimProgress, Maths.rad(-90.0), Maths.rad(60.0), Maths.rad(-30.0), 5.0F);
        this.progressPositionPrev(this.left_arm, swimProgress, 0.0F, 0.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, swimProgress, 0.0F, 0.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.body, swimProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        float walkAmount = (5.0F - swimProgress) * 0.2F - Math.max(jumpProgress, fallProgress) * 0.2F;
        float walkSwingAmount = limbSwingAmount * walkAmount;
        float swimSwingAmount = limbSwingAmount * 0.2F * swimProgress;
        this.swing(this.right_leg, swimSpeed, swimDegree, false, -2.5F, -0.3F, limbSwing, swimSwingAmount);
        this.swing(this.left_leg, swimSpeed, swimDegree, true, -2.5F, -0.3F, limbSwing, swimSwingAmount);
        this.flap(this.right_foot, swimSpeed, swimDegree, false, -1.0F, -0.3F, limbSwing, swimSwingAmount);
        this.flap(this.left_foot, swimSpeed, swimDegree, true, -1.0F, -0.3F, limbSwing, swimSwingAmount);
        this.flap(this.right_arm, swimSpeed, swimDegree, false, -2.5F, -0.1F, limbSwing, swimSwingAmount);
        this.flap(this.left_arm, swimSpeed, swimDegree, true, -2.5F, -0.1F, limbSwing, swimSwingAmount);
        this.swing(this.right_arm, swimSpeed, swimDegree, false, -2.5F, 0.3F, limbSwing, swimSwingAmount);
        this.swing(this.left_arm, swimSpeed, swimDegree, true, -2.5F, 0.3F, limbSwing, swimSwingAmount);
        this.swing(this.body, swimSpeed, swimDegree * 0.1F, false, 0.0F, 0.0F, limbSwing, swimSwingAmount);
        this.progressRotationPrev(this.left_foot, Math.min(walkSwingAmount, 0.5F), 0.0F, Maths.rad(80.0), 0.0F, 0.5F);
        this.progressRotationPrev(this.right_foot, Math.min(walkSwingAmount, 0.5F), 0.0F, Maths.rad(-80.0), 0.0F, 0.5F);
        this.flap(this.body, walkSpeed, walkDegree * 0.35F, false, 0.0F, 0.0F, limbSwing, walkSwingAmount);
        this.swing(this.body, walkSpeed, walkDegree * 0.35F, false, 1.0F, 0.0F, limbSwing, walkSwingAmount);
        this.walk(this.left_arm, walkSpeed, walkDegree, false, -2.5F, -0.3F, limbSwing, walkSwingAmount);
        this.walk(this.right_arm, walkSpeed, walkDegree, true, -2.5F, 0.3F, limbSwing, walkSwingAmount);
        this.walk(this.right_leg, walkSpeed, walkDegree, false, -2.5F, 0.1F, limbSwing, walkSwingAmount);
        this.walk(this.left_leg, walkSpeed, walkDegree, true, -2.5F, -0.1F, limbSwing, walkSwingAmount);
        this.left_foot_pivot.rotateAngleX = this.left_foot_pivot.rotateAngleX - walkAmount * (this.left_leg.rotateAngleX + this.body.rotateAngleX);
        this.left_foot_pivot.rotateAngleZ = this.left_foot_pivot.rotateAngleZ - walkAmount * this.body.rotateAngleZ;
        this.right_foot_pivot.rotateAngleX = this.right_foot_pivot.rotateAngleX - walkAmount * (this.right_leg.rotateAngleX + this.body.rotateAngleX);
        this.right_foot_pivot.rotateAngleZ = this.right_foot_pivot.rotateAngleZ - walkAmount * this.body.rotateAngleZ;
        this.left_arm.rotationPointZ = this.left_arm.rotationPointZ + 1.5F * (float) (Math.sin((double) (limbSwing * walkSpeed) - 2.5) * (double) walkSwingAmount * (double) walkDegree - (double) (walkSwingAmount * walkDegree));
        this.left_arm.rotationPointY = this.left_arm.rotationPointY + 0.5F * (float) (Math.sin((double) (limbSwing * walkSpeed) - 2.5) * (double) walkSwingAmount * (double) walkDegree - (double) (walkSwingAmount * walkDegree));
        this.right_arm.rotationPointZ = this.right_arm.rotationPointZ + 1.5F * (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) walkSwingAmount * (double) walkDegree - (double) (walkSwingAmount * walkDegree));
        this.right_arm.rotationPointY = this.right_arm.rotationPointY + 0.5F * (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) walkSwingAmount * (double) walkDegree - (double) (walkSwingAmount * walkDegree));
        float leftLegS = (float) (Math.sin((double) (limbSwing * walkSpeed) - 2.5) * (double) walkSwingAmount * (double) walkDegree - (double) (walkSwingAmount * walkDegree));
        float rightLegs = (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) walkSwingAmount * (double) walkDegree - (double) (walkSwingAmount * walkDegree));
        this.left_leg.rotationPointY += 1.5F * leftLegS;
        this.right_leg.rotationPointY += 1.5F * rightLegs;
        this.left_leg.rotationPointZ -= 3.0F * leftLegS;
        this.right_leg.rotationPointZ -= 3.0F * rightLegs;
        this.left_foot_pivot.rotationPointZ -= 1.5F * leftLegS;
        this.right_foot_pivot.rotationPointZ -= 1.5F * rightLegs;
        if (attackProgress > 0.0F) {
            this.tongue.rotateAngleX += headPitch * (float) (Math.PI / 180.0);
        }
        this.progressRotationPrev(this.body, fallProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, fallProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, fallProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, fallProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, fallProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_foot_pivot, fallProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_foot_pivot, fallProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_foot, fallProgress, 0.0F, Maths.rad(70.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_foot, fallProgress, 0.0F, Maths.rad(-70.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.left_foot_pivot, fallProgress, 0.0F, 1.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.right_foot_pivot, fallProgress, 0.0F, 1.0F, -1.0F, 5.0F);
        this.progressRotationPrev(this.body, jumpProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, jumpProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, jumpProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, jumpProgress, Maths.rad(150.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, jumpProgress, Maths.rad(150.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_foot_pivot, fallProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_foot_pivot, fallProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_foot, jumpProgress, Maths.rad(20.0), Maths.rad(70.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_foot, jumpProgress, Maths.rad(20.0), Maths.rad(-70.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, jumpProgress, 0.0F, 1.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, jumpProgress, 0.0F, 1.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, jumpProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, jumpProgress, 0.0F, 1.0F, 0.0F, 5.0F);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.35F, 0.35F, 0.35F);
            matrixStackIn.translate(0.0, 2.75, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}