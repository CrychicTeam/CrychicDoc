package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityBaldEagle;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelBaldEagle extends AdvancedEntityModel<EntityBaldEagle> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox legL;

    private final AdvancedModelBox footL;

    private final AdvancedModelBox legR;

    private final AdvancedModelBox footR;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox headPivot;

    private final AdvancedModelBox head;

    private final AdvancedModelBox topHead;

    private final AdvancedModelBox beak1;

    private final AdvancedModelBox beak2;

    private final AdvancedModelBox hood_tie;

    private final AdvancedModelBox hood;

    private final AdvancedModelBox wingR;

    private final AdvancedModelBox feathersR;

    private final AdvancedModelBox tipR;

    private final AdvancedModelBox wingL;

    private final AdvancedModelBox feathersL;

    private final AdvancedModelBox tipL;

    public ModelBaldEagle() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -9.3F, -2.0F);
        this.root.addChild(this.body);
        this.setRotationAngle(this.body, 0.8727F, 0.0F, 0.0F);
        this.body.setTextureOffset(1, 12).addBox(-2.0F, 0.0F, -1.8F, 4.0F, 8.0F, 5.0F, 0.0F, false);
        this.legL = new AdvancedModelBox(this, "legL");
        this.legL.setPos(-1.4F, 5.5F, -1.15F);
        this.body.addChild(this.legL);
        this.setRotationAngle(this.legL, -0.8727F, 0.1745F, 0.0F);
        this.legL.setTextureOffset(0, 26).addBox(-0.5F, 0.2F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        this.footL = new AdvancedModelBox(this, "footL");
        this.footL.setPos(0.0F, 4.2F, 0.5F);
        this.legL.addChild(this.footL);
        this.setRotationAngle(this.footL, 0.0F, 0.1745F, -0.1745F);
        this.footL.setTextureOffset(5, 25).addBox(-1.5F, 0.0F, -1.9F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        this.legR = new AdvancedModelBox(this, "legR");
        this.legR.setPos(1.4F, 5.5F, -1.15F);
        this.body.addChild(this.legR);
        this.setRotationAngle(this.legR, -0.8727F, -0.1745F, 0.0F);
        this.legR.setTextureOffset(0, 26).addBox(-0.5F, 0.2F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        this.footR = new AdvancedModelBox(this, "footR");
        this.footR.setPos(0.0F, 4.2F, 0.5F);
        this.legR.addChild(this.footR);
        this.setRotationAngle(this.footR, 0.0F, -0.1745F, 0.1745F);
        this.footR.setTextureOffset(5, 25).addBox(-1.5F, 0.0F, -1.9F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, 8.07F, 1.36F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.392F, 0.0F, 0.0F);
        this.tail.setTextureOffset(24, 1).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 7.0F, 1.0F, 0.0F, false);
        this.headPivot = new AdvancedModelBox(this, "headPivot");
        this.headPivot.setPos(0.0F, -0.51F, 0.64F);
        this.body.addChild(this.headPivot);
        this.setRotationAngle(this.headPivot, -0.576F, 0.0F, 0.0F);
        this.head = new AdvancedModelBox(this, "head");
        this.headPivot.addChild(this.head);
        this.head.setTextureOffset(1, 3).addBox(-1.5F, -4.4F, -1.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        this.topHead = new AdvancedModelBox(this, "topHead");
        this.topHead.setPos(0.0F, -4.9F, -0.5F);
        this.head.addChild(this.topHead);
        this.topHead.setTextureOffset(10, 0).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 1.0F, 4.0F, 0.0F, false);
        this.beak1 = new AdvancedModelBox(this, "beak1");
        this.beak1.setPos(0.0F, -3.5F, -0.7F);
        this.head.addChild(this.beak1);
        this.setRotationAngle(this.beak1, -0.1745F, 0.0F, 0.0F);
        this.beak1.setTextureOffset(21, 9).addBox(-0.5F, -1.0F, -1.94F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.beak2 = new AdvancedModelBox(this, "beak2");
        this.beak2.setPos(0.0F, -1.0F, -2.54F);
        this.beak1.addChild(this.beak2);
        this.beak2.setTextureOffset(16, 9).addBox(-0.5F, 0.01F, -0.4F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.hood_tie = new AdvancedModelBox(this, "hood_tie");
        this.hood_tie.setPos(0.0F, -3.65F, 1.85F);
        this.head.addChild(this.hood_tie);
        this.setRotationAngle(this.hood_tie, -0.2215F, 0.0F, 0.0F);
        this.hood_tie.setTextureOffset(40, -4).addBox(0.0F, -4.0F, -2.0F, 0.0F, 5.0F, 4.0F, 0.0F, false);
        this.hood = new AdvancedModelBox(this, "hood");
        this.hood.setPos(0.0F, -4.9F, -0.5F);
        this.head.addChild(this.hood);
        this.hood.setTextureOffset(36, 7).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 4.0F, 4.0F, 0.0F, false);
        this.hood.setScale(1.1F, 1.1F, 1.1F);
        this.wingR = new AdvancedModelBox(this, "wingR");
        this.wingR.setPos(-1.9F, 0.0F, 2.0F);
        this.body.addChild(this.wingR);
        this.setRotationAngle(this.wingR, 0.576F, 0.1571F, 0.1396F);
        this.wingR.setTextureOffset(20, 14).addBox(-0.5F, -1.0F, -4.5F, 1.0F, 12.0F, 5.0F, 0.0F, true);
        this.feathersR = new AdvancedModelBox(this, "feathersR");
        this.feathersR.setPos(-0.5F, 0.8F, 1.6F);
        this.wingR.addChild(this.feathersR);
        this.feathersR.setTextureOffset(52, 10).addBox(0.5F, -1.5F, -3.1F, 0.0F, 16.0F, 2.0F, 0.0F, true);
        this.tipR = new AdvancedModelBox(this, "tipR");
        this.tipR.setPos(-0.51F, 11.8F, 0.5F);
        this.wingR.addChild(this.tipR);
        this.setRotationAngle(this.tipR, 0.0F, 0.0F, 0.0873F);
        this.tipR.setTextureOffset(36, 10).addBox(0.5F, -6.0F, -6.0F, 0.0F, 13.0F, 8.0F, 0.0F, true);
        this.wingL = new AdvancedModelBox(this, "wingL");
        this.wingL.setPos(1.9F, 0.0F, 2.0F);
        this.body.addChild(this.wingL);
        this.setRotationAngle(this.wingL, 0.576F, -0.1571F, -0.1396F);
        this.wingL.setTextureOffset(20, 14).addBox(-0.5F, -1.0F, -4.5F, 1.0F, 12.0F, 5.0F, 0.0F, false);
        this.feathersL = new AdvancedModelBox(this, "feathersL");
        this.feathersL.setPos(0.5F, 0.8F, 1.6F);
        this.wingL.addChild(this.feathersL);
        this.feathersL.setTextureOffset(52, 10).addBox(-0.5F, -1.5F, -3.1F, 0.0F, 16.0F, 2.0F, 0.0F, false);
        this.tipL = new AdvancedModelBox(this, "tipL");
        this.tipL.setPos(0.51F, 11.8F, 0.5F);
        this.wingL.addChild(this.tipL);
        this.setRotationAngle(this.tipL, 0.0F, 0.0F, -0.0873F);
        this.tipL.setTextureOffset(36, 10).addBox(-0.5F, -6.0F, -6.0F, 0.0F, 13.0F, 8.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityBaldEagle entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float flapSpeed = 0.4F;
        float flapDegree = 0.2F;
        float walkSpeed = 0.5F;
        float walkDegree = 0.5F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTicks;
        float perchProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTicks;
        float tackleProgress = Math.min(entity.prevTackleProgress + (entity.tackleProgress - entity.prevTackleProgress) * partialTicks, flyProgress);
        float swoopProgress = Math.min(entity.prevSwoopProgress + (entity.swoopProgress - entity.prevSwoopProgress) * partialTicks, flyProgress);
        float flyFeetProgress = Math.max(0.0F, flyProgress - tackleProgress);
        float biteProgress = entity.prevAttackProgress + (entity.attackProgress - entity.prevAttackProgress) * partialTicks;
        float flapAmount = (entity.prevFlapAmount + (entity.flapAmount - entity.prevFlapAmount) * partialTicks) * flyProgress * 0.2F * (5.0F - swoopProgress) * 0.2F;
        this.progressRotationPrev(this.body, flyProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, flyProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legL, flyFeetProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legR, flyFeetProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.footL, flyFeetProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.footR, flyFeetProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legL, flyFeetProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legR, flyFeetProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.wingR, flyProgress, Maths.rad(-120.0), Maths.rad(90.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.wingR, flyProgress, -1.0F, 5.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.wingL, flyProgress, Maths.rad(-120.0), Maths.rad(-90.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.wingL, flyProgress, 1.0F, 5.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, flyProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, flyProgress, 0.0F, 0.0F, -1.5F, 5.0F);
        this.progressPositionPrev(this.body, flyProgress, 0.0F, 4.0F, -1.5F, 5.0F);
        this.progressRotationPrev(this.body, perchProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legL, perchProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legR, perchProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, perchProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, perchProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, tackleProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, tackleProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legL, tackleProgress, Maths.rad(-60.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legR, tackleProgress, Maths.rad(-60.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legL, tackleProgress, 0.0F, -1.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.legR, tackleProgress, 0.0F, -1.0F, 1.0F, 5.0F);
        this.progressRotationPrev(this.wingL, swoopProgress, Maths.rad(60.0), Maths.rad(50.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.wingL, swoopProgress, -2.0F, -2.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.wingR, swoopProgress, Maths.rad(60.0), Maths.rad(-50.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.wingR, swoopProgress, 2.0F, -2.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, swoopProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, biteProgress, Maths.rad(70.0), 0.0F, 0.0F, 2.5F);
        if (flyProgress > 0.0F) {
            this.bob(this.body, flapSpeed * 0.5F, flapDegree * 4.0F, true, ageInTicks, 1.0F);
            this.swing(this.wingL, flapSpeed, flapDegree * 3.0F, true, 0.0F, 0.0F, ageInTicks, flapAmount);
            this.swing(this.wingR, flapSpeed, flapDegree * 3.0F, false, 0.0F, 0.0F, ageInTicks, flapAmount);
            this.bob(this.body, flapSpeed * 0.5F, flapDegree * 4.0F, true, ageInTicks, flapAmount);
        } else {
            float walk = Math.min(limbSwingAmount, 1.0F);
            this.progressRotationPrev(this.body, walk, Maths.rad(15.0), 0.0F, 0.0F, 1.0F);
            this.progressRotationPrev(this.legL, walk, Maths.rad(-15.0), 0.0F, 0.0F, 1.0F);
            this.progressRotationPrev(this.legR, walk, Maths.rad(-15.0), 0.0F, 0.0F, 1.0F);
            this.progressRotationPrev(this.wingL, walk, Maths.rad(-15.0), 0.0F, 0.0F, 1.0F);
            this.progressRotationPrev(this.wingR, walk, Maths.rad(-15.0), 0.0F, 0.0F, 1.0F);
            this.progressPositionPrev(this.body, walk, 0.0F, 1.0F, 0.0F, 1.0F);
            this.bob(this.body, walkSpeed, walkDegree * 1.3F, true, limbSwing, limbSwingAmount);
            this.walk(this.legL, walkSpeed, walkDegree * 1.85F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.legR, walkSpeed, walkDegree * 1.85F, true, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.footL, walkSpeed, walkDegree * 0.4F, true, 2.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.footR, walkSpeed, walkDegree * 0.4F, false, 2.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.head, walkSpeed, walkDegree * 0.3F, false, 1.0F, -0.2F, limbSwing, limbSwingAmount);
            this.flap(this.tail, walkSpeed, walkDegree * 0.2F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.body, walkSpeed, walkDegree * 0.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        }
        this.walk(this.head, idleSpeed * 0.7F, idleDegree, false, -1.0F, 0.05F, ageInTicks, 1.0F);
        this.walk(this.tail, idleSpeed * 0.7F, idleDegree, false, 1.0F, 0.05F, ageInTicks, 1.0F);
        if (!entity.m_20160_()) {
            this.head.rotateAngleY += netHeadYaw / (180.0F / (float) Math.PI);
            this.head.rotateAngleZ += headPitch / (180.0F / (float) Math.PI);
        }
        float birdPitch = entity.prevBirdPitch + (entity.birdPitch - entity.prevBirdPitch) * partialTicks;
        this.body.rotateAngleX += birdPitch * flyProgress * 0.2F * (float) (Math.PI / 180.0);
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
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            this.head.setScale(1.0F, 1.0F, 1.0F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.head, this.headPivot, this.wingL, this.wingR, this.beak1, this.beak2, this.hood, this.tipL, this.tipR, new AdvancedModelBox[] { this.hood_tie, this.feathersL, this.feathersR, this.legL, this.legR, this.footL, this.footR, this.topHead });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}