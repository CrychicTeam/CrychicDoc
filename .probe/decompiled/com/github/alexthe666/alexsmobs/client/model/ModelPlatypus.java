package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityPlatypus;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelPlatypus extends AdvancedEntityModel<EntityPlatypus> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox beak;

    private final AdvancedModelBox fedora;

    private final AdvancedModelBox arm_left;

    private final AdvancedModelBox arm_right;

    private final AdvancedModelBox leg_left;

    private final AdvancedModelBox leg_right;

    private final AdvancedModelBox tail;

    public ModelPlatypus() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -2.6F, -0.1F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 2).addBox(-3.5F, -3.4F, -5.9F, 7.0F, 6.0F, 11.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -0.4F, -5.9F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(5, 51).addBox(-3.0F, -2.5F, -4.0F, 6.0F, 5.0F, 4.0F, 0.0F, false);
        this.beak = new AdvancedModelBox(this, "beak");
        this.beak.setPos(0.0F, 2.0F, -5.0F);
        this.head.addChild(this.beak);
        this.beak.setTextureOffset(28, 0).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);
        this.fedora = new AdvancedModelBox(this, "fedora");
        this.fedora.setPos(0.0F, -2.6F, -1.9F);
        this.head.addChild(this.fedora);
        this.fedora.setTextureOffset(23, 20).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, 0.0F, false);
        this.fedora.setTextureOffset(29, 30).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setPos(3.5F, 2.1F, -4.9F);
        this.body.addChild(this.arm_left);
        this.arm_left.setTextureOffset(7, 39).addBox(0.0F, -0.5F, 0.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setPos(-3.5F, 2.1F, -4.9F);
        this.body.addChild(this.arm_right);
        this.arm_right.setTextureOffset(7, 39).addBox(-3.0F, -0.5F, 0.0F, 3.0F, 1.0F, 3.0F, 0.0F, true);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.leg_left.setPos(3.5F, 2.1F, 2.6F);
        this.body.addChild(this.leg_left);
        this.leg_left.setTextureOffset(27, 43).addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setPos(-3.5F, 2.1F, 2.6F);
        this.body.addChild(this.leg_right);
        this.leg_right.setTextureOffset(27, 43).addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 3.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, -0.4F, 5.1F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 24).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 3.0F, 8.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.head, this.beak, this.fedora, this.arm_left, this.arm_right, this.leg_left, this.leg_right);
    }

    public void setupAnim(EntityPlatypus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 1.0F;
        float walkDegree = 1.3F;
        float idleSpeed = 0.3F;
        float idleDegree = 0.2F;
        float swimSpeed = 1.3F;
        float swimDegree = 1.3F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float digProgress = entity.prevDigProgress + (entity.digProgress - entity.prevDigProgress) * partialTick;
        float swimProgress = entity.prevInWaterProgress + (entity.inWaterProgress - entity.prevInWaterProgress) * partialTick;
        this.progressPositionPrev(this.body, swimProgress, 0.0F, -3.5F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, swimProgress, Maths.rad(-5.0), 0.0F, Maths.rad(75.0), 5.0F);
        this.progressRotationPrev(this.arm_right, swimProgress, Maths.rad(-5.0), 0.0F, Maths.rad(-75.0), 5.0F);
        this.progressRotationPrev(this.leg_left, swimProgress, Maths.rad(-5.0), 0.0F, Maths.rad(75.0), 5.0F);
        this.progressRotationPrev(this.leg_right, swimProgress, Maths.rad(-5.0), 0.0F, Maths.rad(-75.0), 5.0F);
        this.progressRotationPrev(this.tail, swimProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, digProgress, 0.0F, -1.5F, 0.0F, 5.0F);
        this.progressPositionPrev(this.arm_right, digProgress, 1.0F, -1.0F, -0.5F, 5.0F);
        this.progressPositionPrev(this.arm_left, digProgress, -1.0F, -1.0F, -0.5F, 5.0F);
        this.progressRotationPrev(this.body, digProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, digProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, digProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.arm_left, digProgress, Maths.rad(-30.0), Maths.rad(10.0), Maths.rad(-65.0), 5.0F);
        this.progressRotationPrev(this.arm_right, digProgress, Maths.rad(-30.0), Maths.rad(-10.0), Maths.rad(65.0), 5.0F);
        if (digProgress > 0.0F) {
            this.swing(this.body, 0.8F, idleDegree * 1.2F, false, 3.0F, 0.0F, ageInTicks, 1.0F);
            this.swing(this.head, 0.8F, idleDegree * 0.7F, false, 3.0F, 0.0F, ageInTicks, 1.0F);
            this.swing(this.arm_right, 0.8F, idleDegree * 2.6F, false, 1.0F, -0.25F, ageInTicks, 1.0F);
            this.swing(this.arm_left, 0.8F, idleDegree * 2.6F, true, 1.0F, -0.25F, ageInTicks, 1.0F);
        } else if (!entity.isSensing() && !entity.isSensingVisual()) {
            this.faceTarget(netHeadYaw, headPitch, 1.2F, new AdvancedModelBox[] { this.head });
        } else {
            this.swing(this.head, swimSpeed, swimDegree * 0.25F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
            this.walk(this.head, swimSpeed, swimDegree * 0.25F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        }
        if (swimProgress > 0.0F) {
            this.bob(this.body, idleSpeed, idleDegree * 2.0F, false, ageInTicks, 1.0F);
            this.walk(this.tail, swimSpeed, swimDegree * 0.1F, false, 3.0F, 0.25F, limbSwing, limbSwingAmount);
            this.swing(this.tail, swimSpeed, swimDegree * 0.5F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.body, swimSpeed, swimDegree * 0.3F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.head, swimSpeed, swimDegree * 0.5F, true, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.arm_right, swimSpeed, swimDegree * 0.9F, false, 1.0F, 0.85F, limbSwing, limbSwingAmount);
            this.flap(this.arm_left, swimSpeed, swimDegree * 0.9F, true, 1.0F, 0.85F, limbSwing, limbSwingAmount);
            this.flap(this.leg_right, swimSpeed, swimDegree * 0.9F, false, 3.0F, 0.85F, limbSwing, limbSwingAmount);
            this.flap(this.leg_left, swimSpeed, swimDegree * 0.9F, true, 3.0F, 0.85F, limbSwing, limbSwingAmount);
            this.walk(this.body, swimSpeed, swimDegree * 0.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        } else {
            this.swing(this.tail, idleSpeed * 0.5F, idleDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.bob(this.body, walkSpeed * 1.75F, walkDegree * 1.0F, false, limbSwing, limbSwingAmount);
            this.swing(this.body, walkSpeed, walkDegree * 0.3F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.head, walkSpeed, walkDegree * 0.2F, true, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.tail, walkSpeed, walkDegree * 0.3F, false, 3.0F, 0.1F, limbSwing, limbSwingAmount);
            this.swing(this.arm_left, walkSpeed, walkDegree * 0.5F, true, 1.0F, 0.15F, limbSwing, limbSwingAmount);
            this.flap(this.arm_left, walkSpeed, walkDegree * 0.5F, true, 0.0F, 0.25F, limbSwing, limbSwingAmount);
            this.swing(this.arm_right, walkSpeed, walkDegree * 0.5F, true, 1.0F, -0.15F, limbSwing, limbSwingAmount);
            this.flap(this.arm_right, walkSpeed, walkDegree * 0.5F, true, 0.0F, -0.25F, limbSwing, limbSwingAmount);
            this.swing(this.leg_left, walkSpeed, walkDegree * 0.5F, false, 1.0F, -0.15F, limbSwing, limbSwingAmount);
            this.flap(this.leg_left, walkSpeed, walkDegree * 0.5F, false, 0.0F, -0.15F, limbSwing, limbSwingAmount);
            this.swing(this.leg_right, walkSpeed, walkDegree * 0.5F, false, 1.0F, 0.15F, limbSwing, limbSwingAmount);
            this.flap(this.leg_right, walkSpeed, walkDegree * 0.5F, false, 0.0F, 0.15F, limbSwing, limbSwingAmount);
        }
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

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}