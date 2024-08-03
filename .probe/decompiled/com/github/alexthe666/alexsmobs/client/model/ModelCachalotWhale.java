package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCachalotWhale;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;

public class ModelCachalotWhale extends AdvancedEntityModel<EntityCachalotWhale> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox top_fin;

    public final AdvancedModelBox arm_left;

    public final AdvancedModelBox arm_right;

    public final AdvancedModelBox tail1;

    public final AdvancedModelBox tail2;

    public final AdvancedModelBox tail3;

    public final AdvancedModelBox head;

    public final AdvancedModelBox jaw;

    public final AdvancedModelBox teeth;

    public ModelCachalotWhale() {
        this.texWidth = 512;
        this.texHeight = 512;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -30.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-21.0F, -30.0F, -60.0F, 42.0F, 60.0F, 112.0F, 0.0F, false);
        this.top_fin = new AdvancedModelBox(this, "top_fin");
        this.top_fin.setPos(0.0F, -34.0F, 42.0F);
        this.body.addChild(this.top_fin);
        this.top_fin.setTextureOffset(0, 0).addBox(-3.0F, -4.0F, -10.0F, 6.0F, 8.0F, 20.0F, 0.0F, false);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setPos(21.0F, 26.0F, -38.0F);
        this.body.addChild(this.arm_left);
        this.arm_left.setTextureOffset(304, 220).addBox(0.0F, -2.0F, -3.0F, 36.0F, 4.0F, 21.0F, 0.0F, false);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setPos(-21.0F, 26.0F, -38.0F);
        this.body.addChild(this.arm_right);
        this.arm_right.setTextureOffset(304, 220).addBox(-36.0F, -2.0F, -3.0F, 36.0F, 4.0F, 21.0F, 0.0F, true);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setPos(0.0F, -1.0F, 52.0F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(163, 227).addBox(-15.0F, -22.0F, 0.0F, 30.0F, 45.0F, 80.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setPos(0.0F, -1.0F, 80.0F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(197, 0).addBox(-9.0F, -14.0F, 0.0F, 18.0F, 28.0F, 65.0F, 0.0F, false);
        this.tail3 = new AdvancedModelBox(this, "tail3");
        this.tail3.setPos(0.0F, 2.0F, 56.0F);
        this.tail2.addChild(this.tail3);
        this.tail3.setTextureOffset(158, 173).addBox(-33.0F, -5.0F, -5.0F, 66.0F, 9.0F, 37.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -2.0F, -60.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 173).addBox(-18.0F, -28.0F, -85.0F, 36.0F, 48.0F, 85.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this, "jaw");
        this.jaw.setPos(0.0F, 20.0F, 0.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(293, 23).addBox(-7.0F, 0.0F, -71.0F, 14.0F, 9.0F, 71.0F, 0.0F, false);
        this.teeth = new AdvancedModelBox(this, "teeth");
        this.teeth.setPos(0.0F, 0.0F, -7.0F);
        this.jaw.addChild(this.teeth);
        this.teeth.setTextureOffset(32, 370).addBox(-4.0F, -4.0F, -59.0F, 8.0F, 4.0F, 60.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.head, this.body, this.tail1, this.tail2, this.tail3, this.top_fin, this.jaw, this.teeth, this.arm_left, this.arm_right);
    }

    public void setupAnim(EntityCachalotWhale entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float renderYaw = (float) entity.getMovementOffsets(0, partialTicks)[0];
        float properPitch = entity.f_19860_ + (entity.m_146909_() - entity.f_19860_) * partialTicks;
        float chargeProgress = entity.prevChargingProgress + (entity.chargeProgress - entity.prevChargingProgress) * partialTicks;
        float sleepProgress = entity.prevSleepProgress + (entity.sleepProgress - entity.prevSleepProgress) * partialTicks;
        float beachedProgress = entity.prevBeachedProgress + (entity.beachedProgress - entity.prevBeachedProgress) * partialTicks;
        float grabProgress = entity.prevGrabProgress + (entity.grabProgress - entity.prevGrabProgress) * partialTicks;
        float f = Mth.clamp((float) entity.getMovementOffsets(7, partialTicks)[0] - renderYaw, -50.0F, 50.0F);
        this.tail1.rotateAngleY = this.tail1.rotateAngleY + Mth.clamp((float) entity.getMovementOffsets(15, partialTicks)[0] - renderYaw, -50.0F, 50.0F) * (float) (Math.PI / 180.0);
        this.tail2.rotateAngleY = this.tail2.rotateAngleY + Mth.clamp((float) entity.getMovementOffsets(17, partialTicks)[0] - renderYaw, -50.0F, 50.0F) * (float) (Math.PI / 180.0);
        this.body.rotateAngleX = this.body.rotateAngleX + Math.min(properPitch, sleepProgress * -9.0F) * (float) (Math.PI / 180.0);
        this.body.rotateAngleZ += f * (float) (Math.PI / 180.0);
        this.head.rotateAngleY = (float) ((double) this.head.rotateAngleY + Math.sin((double) (((float) entity.grabTime + partialTicks) * 0.3F)) * 0.1F * (double) grabProgress);
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.tail1, this.tail2, this.tail3 };
        float swimSpeed = 0.2F;
        float swimDegree = 0.4F;
        float beachedSpeed = 0.05F;
        float beachedIdle = 0.4F;
        this.progressRotationPrev(this.jaw, Math.max(chargeProgress, grabProgress * 0.8F), Maths.rad(30.0), 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.jaw, beachedProgress, Maths.rad(20.0), Maths.rad(5.0), 0.0F, 10.0F);
        this.progressRotationPrev(this.body, beachedProgress, 0.0F, 0.0F, Maths.rad(80.0), 10.0F);
        this.progressRotationPrev(this.tail1, beachedProgress, Maths.rad(-30.0), Maths.rad(10.0), 0.0F, 10.0F);
        this.progressRotationPrev(this.tail2, beachedProgress, Maths.rad(-30.0), Maths.rad(-30.0), Maths.rad(-30.0), 10.0F);
        this.progressRotationPrev(this.tail3, beachedProgress, 0.0F, Maths.rad(-10.0), Maths.rad(-60.0), 10.0F);
        this.progressRotationPrev(this.head, beachedProgress, 0.0F, Maths.rad(-10.0), 0.0F, 10.0F);
        this.progressRotationPrev(this.arm_right, beachedProgress, 0.0F, 0.0F, Maths.rad(-110.0), 10.0F);
        this.progressRotationPrev(this.arm_left, beachedProgress, 0.0F, 0.0F, Maths.rad(110.0), 10.0F);
        this.progressPositionPrev(this.tail1, beachedProgress, -2.0F, -1.0F, -10.0F, 10.0F);
        this.progressPositionPrev(this.tail2, beachedProgress, 0.0F, -1.0F, -4.0F, 10.0F);
        this.progressPositionPrev(this.tail3, beachedProgress, 0.0F, 2.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.body, beachedProgress, 0.0F, 5.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.head, beachedProgress, 0.0F, 0.0F, 3.0F, 10.0F);
        if (beachedProgress > 0.0F) {
            this.swing(this.arm_left, beachedSpeed, beachedIdle * 0.2F, true, 1.0F, 0.0F, ageInTicks, 1.0F);
            this.flap(this.arm_right, beachedSpeed, beachedIdle * 0.2F, true, 3.0F, 0.06F, ageInTicks, 1.0F);
            this.walk(this.jaw, beachedSpeed, beachedIdle * 0.2F, true, 2.0F, 0.06F, ageInTicks, 1.0F);
            this.walk(this.tail1, beachedSpeed, beachedIdle * 0.2F, false, 4.0F, 0.06F, ageInTicks, 1.0F);
            this.walk(this.tail2, beachedSpeed, beachedIdle * 0.2F, false, 4.0F, 0.06F, ageInTicks, 1.0F);
        } else {
            this.walk(this.jaw, swimSpeed * 0.4F, swimDegree * 0.15F, true, 1.0F, -0.01F, ageInTicks, 1.0F);
            this.flap(this.arm_left, swimSpeed * 0.4F, swimDegree * 0.5F, true, 2.5F, -0.4F, ageInTicks, 1.0F);
            this.flap(this.arm_right, swimSpeed * 0.4F, swimDegree * 0.5F, false, 2.5F, -0.4F, ageInTicks, 1.0F);
            this.swing(this.arm_left, swimSpeed, swimDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.arm_right, swimSpeed, swimDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.arm_left, swimSpeed, swimDegree * 1.4F, true, 2.5F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.arm_right, swimSpeed, swimDegree * 1.4F, false, 2.5F, 0.0F, limbSwing, limbSwingAmount);
            this.bob(this.body, swimSpeed, swimDegree * 20.0F, false, limbSwing, limbSwingAmount);
            this.chainWave(tailBoxes, swimSpeed, swimDegree * 0.8F, -2.0, limbSwing, limbSwingAmount);
            this.walk(this.head, swimSpeed, swimDegree * 0.1F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
            this.tail1.rotationPointZ -= 4.0F * limbSwingAmount;
            this.tail2.rotationPointZ -= 2.0F * limbSwingAmount;
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.25F;
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

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}