package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySoulVulture;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelSoulVulture extends AdvancedEntityModel<EntitySoulVulture> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leftWing;

    private final AdvancedModelBox rightWing;

    private final AdvancedModelBox heart;

    private final AdvancedModelBox leftLeg;

    private final AdvancedModelBox leftFoot;

    private final AdvancedModelBox rightLeg;

    private final AdvancedModelBox rightFoot;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    public ModelSoulVulture() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -5.0F, 4.0F);
        this.root.addChild(this.body);
        this.setRotationAngle(this.body, -0.3054F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 15).addBox(-3.0F, -6.0F, -9.0F, 6.0F, 6.0F, 10.0F, 0.0F, false);
        this.body.setTextureOffset(26, 25).addBox(-3.5F, -6.5F, -9.2F, 7.0F, 6.0F, 7.0F, 0.0F, false);
        this.leftWing = new AdvancedModelBox(this, "leftWing");
        this.leftWing.setRotationPoint(3.0F, -2.0F, -7.0F);
        this.body.addChild(this.leftWing);
        this.setRotationAngle(this.leftWing, 1.1345F, -1.3265F, 0.3491F);
        this.leftWing.setTextureOffset(36, 15).addBox(-1.0F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, 0.0F, false);
        this.leftWing.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, 0.0F, 24.0F, 0.0F, 14.0F, 0.0F, false);
        this.rightWing = new AdvancedModelBox(this, "rightWing");
        this.rightWing.setRotationPoint(-3.0F, -2.0F, -7.0F);
        this.body.addChild(this.rightWing);
        this.setRotationAngle(this.rightWing, 1.1345F, 1.3265F, -0.3491F);
        this.rightWing.setTextureOffset(36, 15).addBox(-6.0F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, 0.0F, true);
        this.rightWing.setTextureOffset(0, 0).addBox(-23.0F, 0.0F, 0.0F, 24.0F, 0.0F, 14.0F, 0.0F, true);
        this.heart = new AdvancedModelBox(this, "heart");
        this.heart.setRotationPoint(0.0F, 0.0F, -6.0F);
        this.body.addChild(this.heart);
        this.heart.setTextureOffset(0, 15).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.leftLeg = new AdvancedModelBox(this, "leftWing");
        this.leftLeg.setRotationPoint(3.0F, 0.0F, 0.0F);
        this.body.addChild(this.leftLeg);
        this.setRotationAngle(this.leftLeg, 0.7418F, 0.0F, 0.0F);
        this.leftLeg.setTextureOffset(0, 6).addBox(-1.0F, -1.0F, -4.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        this.leftFoot = new AdvancedModelBox(this, "leftFoot");
        this.leftFoot.setRotationPoint(0.0F, 3.0F, -4.0F);
        this.leftLeg.addChild(this.leftFoot);
        this.setRotationAngle(this.leftFoot, -0.4363F, 0.0F, 0.0F);
        this.leftFoot.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-3.0F, 0.0F, 0.0F);
        this.body.addChild(this.rightLeg);
        this.setRotationAngle(this.rightLeg, 0.7418F, 0.0F, 0.0F);
        this.rightLeg.setTextureOffset(0, 6).addBox(0.0F, -1.0F, -4.0F, 1.0F, 4.0F, 4.0F, 0.0F, true);
        this.rightFoot = new AdvancedModelBox(this, "rightFoot");
        this.rightFoot.setRotationPoint(0.0F, 3.0F, -4.0F);
        this.rightLeg.addChild(this.rightFoot);
        this.setRotationAngle(this.rightFoot, -0.4363F, 0.0F, 0.0F);
        this.rightFoot.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 2.0F, 3.0F, 0.0F, true);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setRotationPoint(0.0F, -2.5F, -10.0F);
        this.body.addChild(this.neck);
        this.setRotationAngle(this.neck, 0.48F, 0.0F, 0.0F);
        this.neck.setTextureOffset(17, 39).addBox(-1.5F, -1.5F, -7.0F, 3.0F, 3.0F, 8.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -2.5F, -6.0F);
        this.neck.addChild(this.head);
        this.setRotationAngle(this.head, -0.1745F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 32).addBox(-2.5F, -3.0F, -5.0F, 5.0F, 4.0F, 7.0F, 0.0F, false);
        this.head.setTextureOffset(23, 15).addBox(-1.5F, -2.0F, -11.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(32, 39).addBox(-1.5F, 1.0F, -11.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.neck, this.heart, this.head, this.rightFoot, this.leftFoot, this.rightWing, this.leftWing, this.leftLeg, this.rightLeg);
    }

    public void setupAnim(EntitySoulVulture entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float flapSpeed = 0.4F;
        float flapDegree = 0.2F;
        float walkSpeed = 0.7F;
        float walkDegree = 0.4F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTick;
        float tackleProgress = entity.prevTackleProgress + (entity.tackleProgress - entity.prevTackleProgress) * partialTick;
        this.progressRotationPrev(this.body, flyProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, flyProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, flyProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftLeg, flyProgress, Maths.rad(55.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightLeg, flyProgress, Maths.rad(55.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightWing, flyProgress, Maths.rad(-70.0), Maths.rad(-90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftWing, flyProgress, Maths.rad(-70.0), Maths.rad(90.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.rightWing, flyProgress, 0.0F, -2.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.leftWing, flyProgress, 0.0F, -2.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.body, flyProgress, 0.0F, 2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leftLeg, flyProgress, 0.0F, -3.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.rightLeg, flyProgress, 0.0F, -3.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.head, flyProgress, 0.0F, 3.0F, -2.0F, 5.0F);
        this.progressRotationPrev(this.body, tackleProgress, -Maths.rad(55.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, tackleProgress, Maths.rad(-35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, tackleProgress, Maths.rad(90.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightLeg, tackleProgress, Maths.rad(-100.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftLeg, tackleProgress, Maths.rad(-100.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leftLeg, tackleProgress, 0.0F, 3.0F, -2.0F, 5.0F);
        this.progressPositionPrev(this.rightLeg, tackleProgress, 0.0F, 3.0F, -2.0F, 5.0F);
        if (flyProgress > 0.0F) {
            this.walk(this.rightLeg, walkSpeed, walkDegree * 0.4F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.leftLeg, walkSpeed, walkDegree * 0.4F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.bob(this.body, flapSpeed * 0.5F, flapDegree * 8.0F, true, ageInTicks, 1.0F);
            this.walk(this.neck, flapSpeed, flapDegree * 0.5F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.walk(this.head, flapSpeed, flapDegree * 1.0F, true, 0.0F, -0.1F, ageInTicks, 1.0F);
            this.flap(this.rightWing, flapSpeed, flapDegree * 5.0F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.flap(this.leftWing, flapSpeed, flapDegree * 5.0F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        } else {
            this.walk(this.body, walkSpeed, walkDegree * 0.5F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.neck, walkSpeed, walkDegree * 0.4F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.rightWing, walkSpeed, walkDegree * 0.4F, true, 1.0F, 0.2F, limbSwing, limbSwingAmount);
            this.swing(this.leftWing, walkSpeed, walkDegree * 0.4F, false, 1.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.rightLeg, walkSpeed, walkDegree * 1.85F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.leftLeg, walkSpeed, walkDegree * 1.85F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        }
        this.walk(this.heart, idleSpeed, idleDegree, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.bob(this.heart, idleSpeed, idleDegree * 4.0F, false, ageInTicks, 1.0F);
        this.walk(this.neck, idleSpeed, idleDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed, idleDegree, false, -1.0F, 0.2F, ageInTicks, 1.0F);
        this.faceTarget(netHeadYaw, headPitch, 2.0F, new AdvancedModelBox[] { this.neck, this.head });
        float bloatScale = 1.0F + Math.min(1.0F, (float) entity.getSoulLevel() * 0.5F);
        this.heart.setScale(bloatScale, bloatScale, bloatScale);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(matrixStack, buffer, packedLight, packedOverlay);
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