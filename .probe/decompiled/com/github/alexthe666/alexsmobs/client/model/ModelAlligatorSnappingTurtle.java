package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityAlligatorSnappingTurtle;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelAlligatorSnappingTurtle extends AdvancedEntityModel<EntityAlligatorSnappingTurtle> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox arm_left;

    private final AdvancedModelBox arm_right;

    private final AdvancedModelBox leg_left;

    private final AdvancedModelBox leg_right;

    private final AdvancedModelBox shell;

    private final AdvancedModelBox spikes_left;

    private final AdvancedModelBox spikes_right;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox head_inside;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox tail;

    public ModelAlligatorSnappingTurtle() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 22).addBox(-7.0F, -6.0F, -8.0F, 14.0F, 6.0F, 16.0F, 0.0F, false);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setPos(6.1F, -1.7F, -6.4F);
        this.body.addChild(this.arm_left);
        this.setRotationAngle(this.arm_left, 0.0F, 0.5672F, 0.0436F);
        this.arm_left.setTextureOffset(47, 45).addBox(-0.5F, -1.5F, -2.0F, 9.0F, 3.0F, 4.0F, 0.0F, false);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setPos(-6.1F, -1.7F, -6.4F);
        this.body.addChild(this.arm_right);
        this.setRotationAngle(this.arm_right, 0.0F, -0.5672F, -0.0436F);
        this.arm_right.setTextureOffset(47, 45).addBox(-8.5F, -1.5F, -2.0F, 9.0F, 3.0F, 4.0F, 0.0F, true);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.body.addChild(this.leg_left);
        this.leg_left.setPos(6.1F, -1.7F, 6.6F);
        this.setRotationAngle(this.leg_left, 0.0F, -0.6109F, 0.0436F);
        this.leg_left.setTextureOffset(45, 22).addBox(-0.5F, -1.5F, -3.0F, 8.0F, 3.0F, 5.0F, 0.0F, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setPos(-6.1F, -1.7F, 6.6F);
        this.body.addChild(this.leg_right);
        this.setRotationAngle(this.leg_right, 0.0F, 0.6109F, -0.0436F);
        this.leg_right.setTextureOffset(45, 22).addBox(-7.5F, -1.5F, -3.0F, 8.0F, 3.0F, 5.0F, 0.0F, true);
        this.shell = new AdvancedModelBox(this, "shell");
        this.shell.setPos(0.0F, -6.0F, 0.0F);
        this.body.addChild(this.shell);
        this.shell.setTextureOffset(0, 0).addBox(-8.0F, -1.0F, -9.0F, 16.0F, 3.0F, 18.0F, 0.0F, false);
        this.spikes_left = new AdvancedModelBox(this, "spikes_left");
        this.spikes_left.setPos(4.0F, -2.0F, 0.0F);
        this.shell.addChild(this.spikes_left);
        this.spikes_left.setTextureOffset(0, 45).addBox(-4.0F, -1.0F, -8.0F, 7.0F, 2.0F, 16.0F, 0.0F, false);
        this.spikes_right = new AdvancedModelBox(this, "spikes_right");
        this.spikes_right.setPos(-4.0F, -2.0F, 0.0F);
        this.shell.addChild(this.spikes_right);
        this.spikes_right.setTextureOffset(0, 45).addBox(-3.0F, -1.0F, -8.0F, 7.0F, 2.0F, 16.0F, 0.0F, true);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setPos(0.0F, -2.0F, -8.0F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(51, 9).addBox(-3.5F, -3.0F, -3.0F, 7.0F, 5.0F, 3.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -0.75F, -3.05F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(51, 0).addBox(-3.0F, -2.25F, -4.95F, 6.0F, 3.0F, 5.0F, 0.0F, false);
        this.head_inside = new AdvancedModelBox(this, "head_inside");
        this.head_inside.setPos(0.0F, 0.0F, 0.0F);
        this.head.addChild(this.head_inside);
        this.head_inside.setTextureOffset(73, 0).addBox(-3.0F, -2.25F, -4.95F, 6.0F, 3.0F, 5.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this, "jaw");
        this.jaw.setPos(0.0F, 1.15F, 0.15F);
        this.head.addChild(this.jaw);
        this.setRotationAngle(this.jaw, -0.2182F, 0.0F, 0.0F);
        this.jaw.setTextureOffset(51, 53).addBox(-2.5F, -0.5F, -5.0F, 5.0F, 2.0F, 5.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, -2.5F, 8.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(31, 45).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 9.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityAlligatorSnappingTurtle entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.05F;
        float idleDegree = 0.25F;
        float walkSpeed = entityIn.m_20069_() ? 0.5F : 1.0F;
        float walkDegree = 0.75F;
        float partialTicks = Minecraft.getInstance().getFrameTime();
        float openProgress = entityIn.prevOpenMouthProgress + (entityIn.openMouthProgress - entityIn.prevOpenMouthProgress) * partialTicks;
        float snapProgress = entityIn.prevAttackProgress + (entityIn.attackProgress - entityIn.prevAttackProgress) * partialTicks;
        this.progressRotationPrev(this.neck, openProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, openProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.jaw, openProgress, Maths.rad(65.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.jaw, openProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.neck, snapProgress, 0.0F, 0.0F, 0.0F, 5.0F);
        this.neck.setScale(1.0F - snapProgress * 0.05F, 1.0F - snapProgress * 0.05F, 1.0F + snapProgress * 0.5F);
        this.head.rotationPointZ -= 1.45F * snapProgress;
        this.progressRotationPrev(this.head, snapProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.jaw, snapProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.swing(this.tail, idleSpeed, idleDegree * 1.15F, false, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.leg_right, walkSpeed, walkDegree, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.leg_left, walkSpeed, walkDegree, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.arm_right, walkSpeed, walkDegree, false, 0.0F, 0.1F, limbSwing, limbSwingAmount);
        this.swing(this.arm_left, walkSpeed, walkDegree, true, 0.0F, 0.1F, limbSwing, limbSwingAmount);
        this.swing(this.tail, walkSpeed * 1.35F, walkDegree * 1.15F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.neck, walkSpeed * 0.75F, walkDegree * 0.15F, false, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.head, walkSpeed * 0.75F, walkDegree * 0.15F, false, -2.0F, 0.0F, limbSwing, limbSwingAmount);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.head_inside.setScale(0.99F, 0.99F, 0.99F);
        if (this.f_102610_) {
            this.head.setScale(1.5F, 1.5F, 1.5F);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.25F, 0.25F, 0.25F);
            matrixStackIn.translate(0.0, 4.5, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        } else {
            this.head.setScale(1.0F, 1.0F, 1.0F);
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
        return ImmutableList.of(this.root, this.body, this.shell, this.spikes_left, this.spikes_right, this.neck, this.jaw, this.head, this.head_inside, this.leg_left, this.leg_right, this.arm_left, new AdvancedModelBox[] { this.arm_right, this.tail });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}