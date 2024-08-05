package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityTerrapin;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;

public class ModelTerrapin extends AdvancedEntityModel<EntityTerrapin> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox shell;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox left_hand;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox right_hand;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox left_foot;

    private final AdvancedModelBox right_leg;

    private final AdvancedModelBox right_foot;

    public ModelTerrapin() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -2.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 14).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 2.0F, 8.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -1.3F, -5.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 25).addBox(-1.5F, -1.5F, -4.0F, 3.0F, 3.0F, 5.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, 0.5F, 4.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(28, 26).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.shell = new AdvancedModelBox(this, "shell");
        this.shell.setPos(0.0F, 0.0F, 0.0F);
        this.body.addChild(this.shell);
        this.shell.setTextureOffset(0, 0).addBox(-4.5F, -3.0F, -5.0F, 9.0F, 3.0F, 10.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setPos(4.0F, 0.0F, -3.6F);
        this.body.addChild(this.left_arm);
        this.left_arm.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        this.left_hand = new AdvancedModelBox(this, "left_hand");
        this.left_hand.setPos(0.0F, 2.0F, -1.0F);
        this.left_arm.addChild(this.left_hand);
        this.left_hand.setTextureOffset(28, 22).addBox(-1.0F, -0.01F, -2.0F, 3.0F, 0.0F, 3.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setPos(-4.0F, 0.0F, -3.6F);
        this.body.addChild(this.right_arm);
        this.right_arm.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);
        this.right_hand = new AdvancedModelBox(this, "right_hand");
        this.right_hand.setPos(0.0F, 2.0F, -1.0F);
        this.right_arm.addChild(this.right_hand);
        this.right_hand.setTextureOffset(28, 22).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 0.0F, 3.0F, 0.0F, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setPos(4.0F, 0.0F, 4.4F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(17, 25).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        this.left_foot = new AdvancedModelBox(this, "left_foot");
        this.left_foot.setPos(0.0F, 2.0F, 0.0F);
        this.left_leg.addChild(this.left_foot);
        this.left_foot.setTextureOffset(23, 14).addBox(-1.0F, -0.01F, -5.0F, 3.0F, 0.0F, 5.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setPos(-4.0F, 0.0F, 4.4F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(17, 25).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 3.0F, 3.0F, 0.0F, true);
        this.right_foot = new AdvancedModelBox(this, "right_foot");
        this.right_foot.setPos(0.0F, 2.0F, 0.0F);
        this.right_leg.addChild(this.right_foot);
        this.right_foot.setTextureOffset(23, 14).addBox(-2.0F, 0.0F, -4.0F, 3.0F, 0.0F, 5.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.shell, this.head, this.tail, this.left_arm, this.left_foot, this.left_leg, this.left_hand, this.right_arm, this.right_foot, this.right_leg, new AdvancedModelBox[] { this.right_hand });
    }

    public void setupAnim(EntityTerrapin entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float walkSpeed = 0.3F;
        float walkDegree = 0.7F;
        float swimSpeed = 0.5F;
        float swimDegree = 0.8F;
        float idleSpeed = 0.15F;
        float idleDegree = 0.7F;
        float spinProgress = entity.prevSpinProgress + (entity.spinProgress - entity.prevSpinProgress) * partialTick;
        float retreatProgress = Math.max(spinProgress, entity.prevRetreatProgress + (entity.retreatProgress - entity.prevRetreatProgress) * partialTick);
        float swimProgress = (entity.prevSwimProgress + (entity.swimProgress - entity.prevSwimProgress) * partialTick) * (5.0F - retreatProgress) * 0.2F;
        float standUnderwaterProgress = Math.max(0.0F, (1.0F - Math.min(limbSwingAmount * 3.0F, 1.0F)) * swimProgress - retreatProgress);
        float spinDegree = 0.6F;
        this.progressRotationPrev(this.left_arm, swimProgress, Maths.rad(-20.0), 0.0F, Maths.rad(-70.0), 5.0F);
        this.progressRotationPrev(this.left_hand, swimProgress, 0.0F, 0.0F, Maths.rad(70.0), 5.0F);
        this.progressRotationPrev(this.right_arm, swimProgress, Maths.rad(-20.0), 0.0F, Maths.rad(70.0), 5.0F);
        this.progressRotationPrev(this.right_hand, swimProgress, 0.0F, 0.0F, Maths.rad(-70.0), 5.0F);
        this.progressRotationPrev(this.left_leg, swimProgress, Maths.rad(30.0), 0.0F, Maths.rad(-70.0), 5.0F);
        this.progressRotationPrev(this.left_foot, swimProgress, 0.0F, 0.0F, Maths.rad(70.0), 5.0F);
        this.progressRotationPrev(this.right_leg, swimProgress, Maths.rad(30.0), 0.0F, Maths.rad(70.0), 5.0F);
        this.progressRotationPrev(this.right_foot, swimProgress, 0.0F, 0.0F, Maths.rad(-70.0), 5.0F);
        this.progressRotationPrev(this.body, standUnderwaterProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, standUnderwaterProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, standUnderwaterProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, standUnderwaterProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.tail, swimProgress, 0.0F, -0.5F, 1.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, retreatProgress, 0.0F, 0.0F, Maths.rad(-90.0), 5.0F);
        this.progressRotationPrev(this.right_arm, retreatProgress, 0.0F, 0.0F, Maths.rad(90.0), 5.0F);
        this.progressRotationPrev(this.left_leg, retreatProgress, 0.0F, 0.0F, Maths.rad(-90.0), 5.0F);
        this.progressRotationPrev(this.right_leg, retreatProgress, 0.0F, 0.0F, Maths.rad(90.0), 5.0F);
        this.progressPositionPrev(this.body, retreatProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, retreatProgress, 0.0F, 1.0F, 3.0F, 5.0F);
        this.progressPositionPrev(this.tail, retreatProgress, 0.0F, -0.1F, -4.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, retreatProgress, -3.0F, -0.1F, 3.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, retreatProgress, 3.0F, -0.1F, 3.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, retreatProgress, -3.0F, -0.1F, -2.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, retreatProgress, 3.0F, -0.1F, -2.0F, 5.0F);
        this.swing(this.tail, idleSpeed, idleDegree, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        if (!entity.hasRetreated() && !entity.isSpinning()) {
            this.faceTarget(netHeadYaw, headPitch, 1.3F, new AdvancedModelBox[] { this.head });
        }
        if (entity.isSpinning()) {
            float timeSpinning = (float) entity.spinCounter + partialTick;
            this.body.rotateAngleY = timeSpinning * 0.2F * spinProgress * spinDegree;
            entity.clientSpin = this.body.rotateAngleY;
            this.bob(this.body, spinDegree, 1.0F, true, timeSpinning, 0.2F * spinProgress);
        } else {
            float rollDeg = (float) Mth.wrapDegrees(Math.toDegrees((double) entity.clientSpin));
            this.body.rotateAngleY = spinProgress * 0.2F * Maths.rad((double) rollDeg);
            if (swimProgress <= 0.0F) {
                this.flap(this.body, walkSpeed, walkDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.swing(this.body, walkSpeed, walkDegree * 0.5F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.head, walkSpeed, walkDegree * -0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.swing(this.head, walkSpeed, walkDegree * -0.5F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.tail, walkSpeed, walkDegree * -0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.swing(this.tail, walkSpeed, walkDegree * 0.5F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
                this.walk(this.left_arm, walkSpeed, walkDegree, false, -2.5F, -0.1F, limbSwing, limbSwingAmount);
                this.walk(this.right_arm, walkSpeed, walkDegree, true, -2.5F, 0.1F, limbSwing, limbSwingAmount);
                this.walk(this.right_leg, walkSpeed, walkDegree, false, -2.5F, 0.1F, limbSwing, limbSwingAmount);
                this.walk(this.left_leg, walkSpeed, walkDegree, true, -2.5F, -0.1F, limbSwing, limbSwingAmount);
                this.left_hand.rotateAngleX = this.left_hand.rotateAngleX - (this.left_arm.rotateAngleX + this.body.rotateAngleX);
                this.left_hand.rotateAngleZ = this.left_hand.rotateAngleZ - this.body.rotateAngleZ;
                this.right_hand.rotateAngleX = this.right_hand.rotateAngleX - (this.right_arm.rotateAngleX + this.body.rotateAngleX);
                this.right_hand.rotateAngleZ = this.right_hand.rotateAngleZ - this.body.rotateAngleZ;
                this.left_foot.rotateAngleX = this.left_foot.rotateAngleX - (this.left_leg.rotateAngleX + this.body.rotateAngleX);
                this.left_foot.rotateAngleZ = this.left_foot.rotateAngleZ - this.body.rotateAngleZ;
                this.right_foot.rotateAngleX = this.right_foot.rotateAngleX - (this.right_leg.rotateAngleX + this.body.rotateAngleX);
                this.right_foot.rotateAngleZ = this.right_foot.rotateAngleZ - this.body.rotateAngleZ;
                this.left_arm.rotationPointY = this.left_arm.rotationPointY + 1.5F * (float) (Math.sin((double) (limbSwing * walkSpeed) - 2.5) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
                this.right_arm.rotationPointY = this.right_arm.rotationPointY + 1.5F * (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
                this.left_leg.rotationPointY = this.left_leg.rotationPointY + 1.5F * (float) (Math.sin((double) (limbSwing * walkSpeed) - 2.5) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
                this.right_leg.rotationPointY = this.right_leg.rotationPointY + 1.5F * (float) (Math.sin(-((double) (limbSwing * walkSpeed)) + 2.5) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
            } else {
                this.flap(this.tail, walkSpeed, walkDegree * -0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.body, swimSpeed, swimDegree * 0.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.head, swimSpeed, swimDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.left_arm, swimSpeed, swimDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.right_arm, swimSpeed, swimDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.left_leg, swimSpeed, swimDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.right_leg, swimSpeed, swimDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.flap(this.tail, swimSpeed, swimDegree * -0.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
                this.walk(this.left_arm, swimSpeed, swimDegree, false, 3.0F, 0.3F, limbSwing, limbSwingAmount);
                this.walk(this.right_arm, swimSpeed, swimDegree, false, 3.0F, 0.3F, limbSwing, limbSwingAmount);
                this.walk(this.left_leg, swimSpeed, swimDegree, false, 2.0F, 0.3F, limbSwing, limbSwingAmount);
                this.walk(this.right_leg, swimSpeed, swimDegree, false, 2.0F, 0.3F, limbSwing, limbSwingAmount);
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.35F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(0.9F, 0.9F, 0.9F);
        } else {
            this.head.setScale(0.9F, 0.9F, 0.9F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }
}