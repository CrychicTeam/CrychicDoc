package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityRoadrunner;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ModelRoadrunner extends AdvancedEntityModel<EntityRoadrunner> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox left_wing;

    private final AdvancedModelBox right_wing;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox beak;

    private final AdvancedModelBox right_spin;

    private final AdvancedModelBox left_spin;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox left_knee;

    private final AdvancedModelBox left_foot;

    private final AdvancedModelBox right_leg;

    private final AdvancedModelBox right_knee;

    private final AdvancedModelBox right_foot;

    public ModelRoadrunner() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(23, 14).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 7.0F, 0.0F, false);
        this.left_wing = new AdvancedModelBox(this, "left_wing");
        this.left_wing.setRotationPoint(2.0F, -1.0F, -3.0F);
        this.body.addChild(this.left_wing);
        this.setRotationAngle(this.left_wing, -0.0873F, 0.1309F, -0.1745F);
        this.left_wing.setTextureOffset(0, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 11.0F, 0.0F, false);
        this.right_wing = new AdvancedModelBox(this, "right_wing");
        this.right_wing.setRotationPoint(-2.0F, -1.0F, -3.0F);
        this.body.addChild(this.right_wing);
        this.setRotationAngle(this.right_wing, -0.0873F, -0.1309F, 0.1745F);
        this.right_wing.setTextureOffset(0, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 11.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -1.6F, 4.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.6545F, 0.0F, 0.0F);
        this.tail.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 13.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setRotationPoint(0.0F, -0.7F, -2.9F);
        this.body.addChild(this.neck);
        this.setRotationAngle(this.neck, 0.6545F, 0.0F, 0.0F);
        this.neck.setTextureOffset(0, 0).addBox(-1.5F, -6.0F, -1.3F, 3.0F, 8.0F, 3.0F, 0.0F, false);
        this.neck.setTextureOffset(0, 14).addBox(0.0F, -8.0F, -1.3F, 0.0F, 4.0F, 5.0F, 0.0F, false);
        this.beak = new AdvancedModelBox(this, "beak");
        this.beak.setRotationPoint(0.0F, -4.5F, -0.8F);
        this.neck.addChild(this.beak);
        this.setRotationAngle(this.beak, -0.3491F, 0.0F, 0.0F);
        this.beak.setTextureOffset(12, 14).addBox(-0.5F, -0.5F, -4.2F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        this.beak.setTextureOffset(47, 22).addBox(-1.0F, -0.1F, -2.1F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        this.right_spin = new AdvancedModelBox(this, "right_spin");
        this.right_spin.setRotationPoint(-1.5F, 4.5F, 1.5F);
        this.body.addChild(this.right_spin);
        this.setRotationAngle(this.right_spin, 0.5236F, 0.0F, 0.0F);
        this.right_spin.setTextureOffset(42, 9).addBox(-1.0F, -2.5F, -2.5F, 2.0F, 5.0F, 5.0F, 0.0F, true);
        this.left_spin = new AdvancedModelBox(this, "left_spin");
        this.left_spin.setRotationPoint(1.5F, 4.5F, 1.5F);
        this.body.addChild(this.left_spin);
        this.setRotationAngle(this.left_spin, 0.5236F, 0.0F, 0.0F);
        this.left_spin.setTextureOffset(42, 9).addBox(-1.0F, -2.5F, -2.5F, 2.0F, 5.0F, 5.0F, 0.0F, false);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(1.5F, 2.0F, 3.0F);
        this.body.addChild(this.left_leg);
        this.setRotationAngle(this.left_leg, 0.5672F, 0.0F, 0.0F);
        this.left_leg.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
        this.left_knee = new AdvancedModelBox(this, "left_knee");
        this.left_knee.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.left_leg.addChild(this.left_knee);
        this.setRotationAngle(this.left_knee, -1.1781F, 0.0F, 0.0F);
        this.left_knee.setTextureOffset(0, 14).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);
        this.left_foot = new AdvancedModelBox(this, "left_foot");
        this.left_foot.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.left_knee.addChild(this.left_foot);
        this.setRotationAngle(this.left_foot, -0.9599F, 0.0F, 0.0F);
        this.left_foot.setTextureOffset(23, 14).addBox(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-1.5F, 2.0F, 3.0F);
        this.body.addChild(this.right_leg);
        this.setRotationAngle(this.right_leg, 0.5672F, 0.0F, 0.0F);
        this.right_leg.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, true);
        this.right_knee = new AdvancedModelBox(this, "right_knee");
        this.right_knee.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.right_leg.addChild(this.right_knee);
        this.setRotationAngle(this.right_knee, -1.1781F, 0.0F, 0.0F);
        this.right_knee.setTextureOffset(0, 14).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, true);
        this.right_foot = new AdvancedModelBox(this, "right_foot");
        this.right_foot.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.right_knee.addChild(this.right_foot);
        this.setRotationAngle(this.right_foot, -0.9599F, 0.0F, 0.0F);
        this.right_foot.setTextureOffset(23, 14).addBox(-1.5F, -2.5F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityRoadrunner entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netneckYaw, float neckPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 0.9F;
        float walkDegree = 0.4F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.4F;
        float runProgress = 5.0F * limbSwingAmount;
        float partialTick = Minecraft.getInstance().getFrameTime();
        boolean spinnyLegs = limbSwingAmount > 0.5F && entityIn.isMeep();
        float biteProgress = entityIn.prevAttackProgress + (entityIn.attackProgress - entityIn.prevAttackProgress) * partialTick;
        this.progressRotationPrev(this.neck, biteProgress, Maths.rad(55.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, runProgress, Maths.rad(-5.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, runProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, runProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, runProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, runProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_wing, runProgress, Maths.rad(-10.0), Maths.rad(-30.0), Maths.rad(40.0), 5.0F);
        this.progressRotationPrev(this.left_wing, runProgress, Maths.rad(-10.0), Maths.rad(30.0), Maths.rad(-40.0), 5.0F);
        this.swing(this.tail, idleSpeed, idleDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.neck, idleSpeed, idleDegree * 0.2F, false, 0.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.right_leg, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_leg, walkSpeed, walkDegree * 1.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_knee, walkSpeed, walkDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_knee, walkSpeed, walkDegree * 0.5F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.left_wing, walkSpeed, walkDegree, true, 2.0F, 0.1F, limbSwing, limbSwingAmount);
        this.flap(this.right_wing, walkSpeed, walkDegree, false, 2.0F, 0.1F, limbSwing, limbSwingAmount);
        this.left_foot.rotateAngleX = -(this.left_leg.rotateAngleX + this.left_knee.rotateAngleX + this.body.rotateAngleX) - (float) (Math.PI / 2);
        this.right_foot.rotateAngleX = -(this.right_leg.rotateAngleX + this.right_knee.rotateAngleX + this.body.rotateAngleX) - (float) (Math.PI / 2);
        this.left_leg.rotationPointY = this.left_leg.rotationPointY + 1.5F * (float) (Math.sin((double) (limbSwing * walkSpeed) + 2.0) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
        this.right_leg.rotationPointY = this.right_leg.rotationPointY + 1.5F * (float) (Math.sin(-((double) (limbSwing * walkSpeed)) - 2.0) * (double) limbSwingAmount * (double) walkDegree - (double) (limbSwingAmount * walkDegree));
        float partialTicks = Minecraft.getInstance().getFrameTime();
        float f = Mth.lerp(partialTicks, entityIn.oFlap, entityIn.wingRotation);
        float f1 = Mth.lerp(partialTicks, entityIn.oFlapSpeed, entityIn.destPos);
        float wingSwing = (Mth.sin(f) + 1.0F) * f1;
        this.flap(this.left_wing, 0.95F, 0.9F, true, 0.0F, 0.2F, wingSwing, wingSwing > 0.0F ? 1.0F : 0.0F);
        this.flap(this.right_wing, 0.95F, 0.9F, false, 0.0F, 0.2F, wingSwing, wingSwing > 0.0F ? 1.0F : 0.0F);
        this.faceTarget(netneckYaw, neckPitch, 1.0F, new AdvancedModelBox[] { this.neck });
        if (spinnyLegs) {
            this.right_spin.showModel = true;
            this.left_spin.showModel = true;
            this.right_leg.showModel = false;
            this.left_leg.showModel = false;
            float wobbleXZ = 1.0F + (1.0F + (float) Math.sin((double) (ageInTicks * 0.6F - 3.0F))) * 0.6F;
            float wobbleY = 1.0F + (1.0F + (float) Math.sin((double) (ageInTicks * 0.6F - 2.0F))) * 0.6F;
            this.right_spin.setScale(1.0F, wobbleY, wobbleXZ);
            this.left_spin.setScale(1.0F, wobbleY, wobbleXZ);
            this.right_spin.rotateAngleX += limbSwingAmount * ageInTicks * 2.0F;
            this.left_spin.rotateAngleX += limbSwingAmount * ageInTicks * 2.0F;
            this.bob(this.body, walkSpeed, walkDegree * 5.0F, true, limbSwing, limbSwingAmount);
        } else {
            this.right_spin.showModel = false;
            this.left_spin.showModel = false;
            this.right_leg.showModel = true;
            this.left_leg.showModel = true;
            this.walk(this.tail, walkSpeed, walkDegree, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.neck, walkSpeed, walkDegree * 0.7F, false, 1.0F, -0.2F, limbSwing, limbSwingAmount);
            this.bob(this.body, walkSpeed * 2.0F, walkDegree * 2.0F, false, limbSwing, limbSwingAmount);
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.75F;
            this.neck.setScale(f, f, f);
            this.neck.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.neck.setScale(1.0F, 1.0F, 1.0F);
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

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.neck, this.beak, this.left_leg, this.right_leg, this.left_wing, this.right_wing, this.left_leg, this.tail, this.right_spin, this.left_spin, new AdvancedModelBox[] { this.beak, this.left_knee, this.right_knee, this.left_foot, this.right_foot });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}