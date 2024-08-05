package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityToucan;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelToucan extends AdvancedEntityModel<EntityToucan> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox tail;

    public final AdvancedModelBox left_wing;

    public final AdvancedModelBox left_wingtip;

    public final AdvancedModelBox right_wing;

    public final AdvancedModelBox right_wingtip;

    public final AdvancedModelBox left_leg;

    public final AdvancedModelBox right_leg;

    public final AdvancedModelBox head;

    public final AdvancedModelBox beak;

    public ModelToucan() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -4.3F, 0.6F);
        this.root.addChild(this.body);
        this.setRotationAngle(this.body, -0.3491F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-2.0F, -3.0F, -5.0F, 4.0F, 4.0F, 7.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -2.0F, 2.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.3927F, 0.0F, 0.0F);
        this.tail.setTextureOffset(18, 7).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 5.0F, 0.0F, false);
        this.left_wing = new AdvancedModelBox(this, "left_wing");
        this.left_wing.setRotationPoint(2.0F, -2.0F, -3.0F);
        this.body.addChild(this.left_wing);
        this.setRotationAngle(this.left_wing, 0.1309F, 0.0F, 0.0F);
        this.left_wing.setTextureOffset(11, 16).addBox(0.0F, -1.0F, -1.0F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        this.left_wingtip = new AdvancedModelBox(this, "left_wingtip");
        this.left_wingtip.setRotationPoint(0.3F, 0.1F, 3.0F);
        this.left_wing.addChild(this.left_wingtip);
        this.left_wingtip.setTextureOffset(20, 21).addBox(0.0F, -1.0F, -1.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);
        this.right_wing = new AdvancedModelBox(this, "right_wing");
        this.right_wing.setRotationPoint(-2.0F, -2.0F, -3.0F);
        this.body.addChild(this.right_wing);
        this.setRotationAngle(this.right_wing, 0.1309F, 0.0F, 0.0F);
        this.right_wing.setTextureOffset(11, 16).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 4.0F, 6.0F, 0.0F, true);
        this.right_wingtip = new AdvancedModelBox(this, "right_wingtip");
        this.right_wingtip.setRotationPoint(-0.3F, 0.1F, 3.0F);
        this.right_wing.addChild(this.right_wingtip);
        this.right_wingtip.setTextureOffset(20, 21).addBox(0.0F, -1.0F, -1.0F, 0.0F, 3.0F, 6.0F, 0.0F, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(1.5F, 1.0F, 1.0F);
        this.body.addChild(this.left_leg);
        this.setRotationAngle(this.left_leg, 0.3491F, 0.0F, 0.0F);
        this.left_leg.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-1.5F, 1.0F, 1.0F);
        this.body.addChild(this.right_leg);
        this.setRotationAngle(this.right_leg, 0.3491F, 0.0F, 0.0F);
        this.right_leg.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 3.0F, 2.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -2.0F, -4.0F);
        this.body.addChild(this.head);
        this.setRotationAngle(this.head, 0.48F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 24).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);
        this.beak = new AdvancedModelBox(this, "beak");
        this.beak.setRotationPoint(0.0F, -4.0F, -1.8F);
        this.head.addChild(this.beak);
        this.beak.setTextureOffset(0, 12).addBox(-1.0F, 0.0F, -6.0F, 2.0F, 3.0F, 6.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.left_wing, this.left_wingtip, this.right_wing, this.right_wingtip, this.right_leg, this.left_leg, this.head, this.beak);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.24F;
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

    public void setupAnim(EntityToucan entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float flapSpeed = 1.0F;
        float flapDegree = 0.2F;
        float walkSpeed = 1.2F;
        float walkDegree = 0.78F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTick;
        float runProgress = Math.max(0.0F, limbSwingAmount * 5.0F - flyProgress);
        float biteProgress = entity.prevPeckProgress + (entity.peckProgress - entity.prevPeckProgress) * partialTick;
        this.progressRotationPrev(this.head, biteProgress, Maths.rad(90.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, biteProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, biteProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, biteProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, biteProgress, 0.0F, 1.0F, -2.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, biteProgress, 0.0F, -0.15F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, biteProgress, 0.0F, -0.15F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, runProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, runProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, runProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, runProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, flyProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, flyProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, flyProgress, Maths.rad(55.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, flyProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, flyProgress, Maths.rad(55.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_wing, flyProgress, Maths.rad(-90.0), 0.0F, Maths.rad(90.0), 5.0F);
        this.progressRotationPrev(this.left_wing, flyProgress, Maths.rad(-90.0), 0.0F, Maths.rad(-90.0), 5.0F);
        this.progressPositionPrev(this.right_wing, flyProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.left_wing, flyProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.right_wingtip, flyProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.left_wingtip, flyProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressRotationPrev(this.left_wingtip, flyProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_wingtip, flyProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        if (flyProgress > 0.0F) {
            this.flap(this.right_wing, flapSpeed, flapDegree * 5.0F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.flap(this.left_wing, flapSpeed, flapDegree * 5.0F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.swing(this.right_wingtip, flapSpeed, flapDegree * 0.5F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.swing(this.left_wingtip, flapSpeed, flapDegree * 0.5F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.bob(this.body, flapSpeed * 0.5F, flapDegree * 12.0F, true, ageInTicks, 1.0F);
            this.walk(this.head, flapSpeed, flapDegree * 0.2F, true, 2.0F, -0.1F, ageInTicks, 1.0F);
        } else {
            this.walk(this.head, idleSpeed * 0.7F, idleDegree, false, 1.0F, 0.05F, ageInTicks, 1.0F);
            this.walk(this.tail, idleSpeed * 0.7F, idleDegree, false, -1.0F, 0.05F, ageInTicks, 1.0F);
            this.bob(this.body, walkSpeed * 1.0F, walkDegree * 1.3F, true, limbSwing, limbSwingAmount);
            this.walk(this.right_leg, walkSpeed, walkDegree * 1.85F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.left_leg, walkSpeed, walkDegree * 1.85F, true, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.head, walkSpeed, walkDegree * 0.4F, false, 2.0F, -0.01F, limbSwing, limbSwingAmount);
            this.swing(this.tail, walkSpeed, walkDegree * 0.5F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        }
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}