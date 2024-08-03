package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySeagull;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelSeagull extends AdvancedEntityModel<EntitySeagull> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox tail;

    public final AdvancedModelBox head;

    public final AdvancedModelBox beak;

    public final AdvancedModelBox left_wing;

    public final AdvancedModelBox left_wingtip;

    public final AdvancedModelBox left_wingtip_r1;

    public final AdvancedModelBox right_wing;

    public final AdvancedModelBox right_wingtip;

    public final AdvancedModelBox right_wingtip_r1;

    public final AdvancedModelBox left_leg;

    public final AdvancedModelBox right_leg;

    public ModelSeagull() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -6.6F, -0.5F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-2.0F, -2.5F, -4.5F, 4.0F, 5.0F, 9.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, -1.5F, 4.5F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, -0.3927F, 0.0F, 0.0F);
        this.tail.setTextureOffset(18, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 5.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -0.5F, -4.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(16, 26).addBox(-1.5F, -6.0F, -1.5F, 3.0F, 8.0F, 3.0F, 0.0F, false);
        this.beak = new AdvancedModelBox(this, "beak");
        this.beak.setPos(0.0F, -4.0F, -1.5F);
        this.head.addChild(this.beak);
        this.beak.setTextureOffset(11, 15).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        this.left_wing = new AdvancedModelBox(this, "left_wing");
        this.left_wing.setPos(2.0F, -1.5F, -2.5F);
        this.body.addChild(this.left_wing);
        this.left_wing.setTextureOffset(0, 15).addBox(0.0F, -1.0F, -1.0F, 1.0F, 4.0F, 8.0F, 0.0F, false);
        this.left_wingtip = new AdvancedModelBox(this, "left_wingtip");
        this.left_wingtip.setPos(1.5F, 0.0F, 8.0F);
        this.left_wing.addChild(this.left_wingtip);
        this.left_wingtip_r1 = new AdvancedModelBox(this, "left_wingtip_r1");
        this.left_wingtip_r1.setPos(-1.0F, 1.0F, -3.0F);
        this.left_wingtip.addChild(this.left_wingtip_r1);
        this.setRotationAngle(this.left_wingtip_r1, 0.2182F, 0.0F, 0.0F);
        this.left_wingtip_r1.setTextureOffset(19, 15).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 7.0F, 0.0F, false);
        this.right_wing = new AdvancedModelBox(this, "right_wing");
        this.right_wing.setPos(-2.0F, -1.5F, -2.5F);
        this.body.addChild(this.right_wing);
        this.right_wing.setTextureOffset(0, 15).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 4.0F, 8.0F, 0.0F, true);
        this.right_wingtip = new AdvancedModelBox(this, "right_wingtip");
        this.right_wingtip.setPos(-1.5F, 0.0F, 8.0F);
        this.right_wing.addChild(this.right_wingtip);
        this.right_wingtip_r1 = new AdvancedModelBox(this, "right_wingtip_r1");
        this.right_wingtip_r1.setPos(1.0F, 1.0F, -3.0F);
        this.right_wingtip.addChild(this.right_wingtip_r1);
        this.setRotationAngle(this.right_wingtip_r1, 0.2182F, 0.0F, 0.0F);
        this.right_wingtip_r1.setTextureOffset(19, 15).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 7.0F, 0.0F, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setPos(1.0F, 2.5F, 2.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(27, 8).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setPos(-1.0F, 2.5F, 2.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(27, 8).addBox(-2.0F, 0.0F, -3.0F, 3.0F, 4.0F, 3.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.left_wing, this.left_wingtip, this.left_wingtip_r1, this.right_wing, this.right_wingtip, this.right_wingtip_r1, this.right_leg, this.left_leg, this.head, new AdvancedModelBox[] { this.beak });
    }

    public void setupAnim(EntitySeagull entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float flapSpeed = 0.6F;
        float flapDegree = 0.2F;
        float walkSpeed = 0.8F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTick;
        float groundProgress = 5.0F - flyProgress;
        float flapAmount = (entity.prevFlapAmount + (entity.flapAmount - entity.prevFlapAmount) * partialTick) * flyProgress * 0.2F;
        float biteProgress = entity.prevAttackProgress + (entity.attackProgress - entity.prevAttackProgress) * partialTick;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 4.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, sitProgress, 0.0F, -4.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, sitProgress, 0.0F, -4.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, biteProgress, Maths.rad(60.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, flyProgress, 0.0F, 1.0F, -1.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, flyProgress, Maths.rad(85.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, flyProgress, Maths.rad(85.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_wing, flyProgress, Maths.rad(-90.0), 0.0F, Maths.rad(90.0), 5.0F);
        this.progressRotationPrev(this.left_wing, flyProgress, Maths.rad(-90.0), 0.0F, Maths.rad(-90.0), 5.0F);
        this.progressPositionPrev(this.right_wing, flyProgress, -1.0F, 0.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.left_wing, flyProgress, 1.0F, 0.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.right_wingtip, flyProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.left_wingtip, flyProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressRotationPrev(this.left_wingtip, flyProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_wingtip, flyProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, flyProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        if (flyProgress > 0.0F) {
            this.flap(this.left_wing, flapSpeed, flapDegree * 5.0F, true, 0.0F, 0.0F, ageInTicks, flapAmount);
            this.flap(this.right_wing, flapSpeed, flapDegree * 5.0F, false, 0.0F, 0.0F, ageInTicks, flapAmount);
            this.bob(this.body, flapSpeed * 0.5F, flapDegree * 10.0F, true, ageInTicks, flapAmount);
            this.walk(this.head, flapSpeed, flapDegree * 0.4F, true, 2.0F, -0.1F, ageInTicks, 1.0F);
            this.walk(this.tail, flapSpeed, flapDegree * 0.6F, true, 3.0F, 0.1F, ageInTicks, 1.0F);
            this.walk(this.right_leg, flapSpeed, flapDegree * 0.5F, false, 0.0F, -0.2F, ageInTicks, 1.0F);
            this.walk(this.left_leg, flapSpeed, flapDegree * 0.5F, true, 0.0F, 0.2F, ageInTicks, 1.0F);
        } else {
            this.bob(this.body, walkSpeed * 1.0F, walkDegree * 1.3F, true, limbSwing, limbSwingAmount);
            this.walk(this.right_leg, walkSpeed, walkDegree * 1.85F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.left_leg, walkSpeed, walkDegree * 1.85F, true, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.head, walkSpeed, walkDegree * 0.4F, false, 2.0F, -0.01F, limbSwing, limbSwingAmount);
            this.flap(this.tail, walkSpeed, walkDegree * 0.5F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        }
        this.swing(this.tail, idleSpeed, idleDegree, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.bob(this.head, idleSpeed * 0.5F, idleDegree * 1.5F, true, ageInTicks, 1.0F);
        this.head.rotateAngleY = (float) ((double) this.head.rotateAngleY + Math.toRadians((double) entity.getFlightLookYaw()) * (double) flyProgress * 0.2F);
        this.head.rotateAngleY += netHeadYaw / (180.0F / (float) Math.PI) * groundProgress * 0.2F;
        this.head.rotateAngleX += headPitch / (180.0F / (float) Math.PI) * groundProgress * 0.2F;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.45F;
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

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}