package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityBlueJay;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelBlueJay extends AdvancedEntityModel<EntityBlueJay> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox leftLeg;

    private final AdvancedModelBox rightLeg;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox leftWing;

    private final AdvancedModelBox rightWing;

    private final AdvancedModelBox head;

    private final AdvancedModelBox crest;

    public ModelBlueJay() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -3.2F, 0.0F);
        this.root.addChild(this.body);
        this.setRotateAngle(this.body, -0.1309F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 4.0F, 7.0F, 0.0F, false);
        this.leftLeg = new AdvancedModelBox(this, "leftLeg");
        this.leftLeg.setRotationPoint(1.5F, 0.0F, 1.0F);
        this.body.addChild(this.leftLeg);
        this.setRotateAngle(this.leftLeg, 0.1309F, 0.0F, 0.0F);
        this.leftLeg.setTextureOffset(26, 10).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-1.5F, 0.0F, 1.0F);
        this.body.addChild(this.rightLeg);
        this.setRotateAngle(this.rightLeg, 0.1309F, 0.0F, 0.0F);
        this.rightLeg.setTextureOffset(26, 10).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 3.0F, 2.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -3.0F, 3.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 22).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 7.0F, 0.0F, false);
        this.leftWing = new AdvancedModelBox(this, "leftWing");
        this.leftWing.setRotationPoint(2.0F, -3.0F, -2.0F);
        this.body.addChild(this.leftWing);
        this.leftWing.setTextureOffset(15, 14).addBox(0.0F, -1.0F, -1.0F, 1.0F, 3.0F, 8.0F, 0.0F, false);
        this.rightWing = new AdvancedModelBox(this, "rightWing");
        this.rightWing.setRotationPoint(-2.0F, -3.0F, -2.0F);
        this.body.addChild(this.rightWing);
        this.rightWing.setTextureOffset(15, 14).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 3.0F, 8.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -3.0F, -4.0F);
        this.body.addChild(this.head);
        this.setRotateAngle(this.head, 0.2182F, 0.0F, 0.0F);
        this.head.setTextureOffset(23, 0).addBox(-2.5F, -3.0F, -3.0F, 5.0F, 4.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(26, 16).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        this.crest = new AdvancedModelBox(this, "crest");
        this.crest.setRotationPoint(0.0F, -2.0F, -1.0F);
        this.head.addChild(this.crest);
        this.setRotateAngle(this.crest, 0.3491F, 0.0F, 0.0F);
        this.crest.setTextureOffset(0, 12).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 3.0F, 6.0F, -0.01F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setupAnim(EntityBlueJay entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float flapSpeed = 0.6F;
        float flapDegree = 0.2F;
        float walkSpeed = 0.95F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTick;
        float flapAmount = flyProgress * 0.2F * (entity.prevFlapAmount + (entity.flapAmount - entity.prevFlapAmount) * partialTick);
        float crestAmount = entity.prevCrestAmount + (entity.crestAmount - entity.prevCrestAmount) * partialTick;
        float biteProgress = entity.prevAttackProgress + (entity.attackProgress - entity.prevAttackProgress) * partialTick;
        float birdPitch = entity.prevBirdPitch + (entity.birdPitch - entity.prevBirdPitch) * partialTick;
        this.progressRotationPrev(this.rightWing, flyProgress, Maths.rad(-20.0), 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.leftWing, flyProgress, Maths.rad(-20.0), 0.0F, Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.body, flyProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftLeg, flyProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightLeg, flyProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, flyProgress, 0.0F, 1.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.body, flyProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.rightWing, flyProgress, 0.0F, 1.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.leftWing, flyProgress, 0.0F, 1.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.rightLeg, flyProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leftLeg, flyProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightWing, flapAmount, Maths.rad(-70.0), 0.0F, Maths.rad(70.0), 1.0F);
        this.progressRotationPrev(this.leftWing, flapAmount, Maths.rad(-70.0), 0.0F, Maths.rad(-70.0), 1.0F);
        this.progressRotationPrev(this.crest, crestAmount, Maths.rad(20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, biteProgress, Maths.rad(60.0), 0.0F, 0.0F, 5.0F);
        this.leftWing.setScale(1.0F + flyProgress * 0.1F, 1.0F + flyProgress * 0.1F, 1.0F + flyProgress * 0.1F);
        this.rightWing.setScale(1.0F + flyProgress * 0.1F, 1.0F + flyProgress * 0.1F, 1.0F + flyProgress * 0.1F);
        this.flap(this.leftWing, flapSpeed, flapDegree * 5.0F, true, -1.0F, 0.0F, ageInTicks, flapAmount);
        this.flap(this.rightWing, flapSpeed, flapDegree * 5.0F, false, -1.0F, 0.0F, ageInTicks, flapAmount);
        this.swing(this.leftWing, flapSpeed, flapDegree * 2.0F, false, 0.0F, 0.0F, ageInTicks, flapAmount);
        this.swing(this.rightWing, flapSpeed, flapDegree * 2.0F, false, 0.0F, 0.0F, ageInTicks, flapAmount);
        this.walk(this.leftWing, flapSpeed, flapDegree * 2.0F, false, 1.0F, 0.0F, ageInTicks, flapAmount);
        this.walk(this.rightWing, flapSpeed, flapDegree * 2.0F, false, 1.0F, 0.0F, ageInTicks, flapAmount);
        this.walk(this.tail, flapSpeed, flapDegree * 0.3F, false, -3.0F, -0.1F, ageInTicks, flapAmount);
        this.bob(this.body, flapSpeed, flapDegree * 10.0F, false, ageInTicks, flapAmount);
        this.bob(this.head, flapSpeed, flapDegree * -6.0F, false, ageInTicks, flapAmount);
        if (flyProgress <= 0.0F) {
            this.bob(this.body, walkSpeed * 1.0F, walkDegree * 1.3F, true, limbSwing, limbSwingAmount);
            this.walk(this.rightLeg, walkSpeed, walkDegree * 1.85F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.leftLeg, walkSpeed, walkDegree * 1.85F, true, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.head, walkSpeed, walkDegree * 0.2F, false, 2.0F, -0.01F, limbSwing, limbSwingAmount);
            this.walk(this.tail, walkSpeed, walkDegree * 0.5F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        }
        this.walk(this.tail, idleSpeed, idleDegree, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.crest, idleSpeed, idleDegree, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.bob(this.head, idleSpeed, idleDegree * 1.5F, true, ageInTicks, 1.0F);
        this.faceTarget(netHeadYaw, headPitch, 1.3F, new AdvancedModelBox[] { this.head });
        this.body.rotateAngleX += birdPitch * flyProgress * 0.2F * (float) (Math.PI / 180.0);
        if (entity.getFeedTime() > 0) {
            this.flap(this.head, 0.4F, 0.4F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        }
        if (entity.getSingTime() > 0) {
            this.flap(this.head, 0.4F, 0.4F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
            this.walk(this.crest, 0.4F, 0.3F, false, 1.0F, 0.1F, ageInTicks, 1.0F);
            this.swing(this.head, 0.4F, 0.4F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
            this.head.rotationPointZ = this.head.rotationPointZ + (float) Math.sin((double) ageInTicks * -0.4 - 1.0);
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
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            this.head.setScale(1.0F, 1.0F, 1.0F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.crest, this.head, this.tail, this.leftLeg, this.rightLeg, this.leftWing, this.rightWing);
    }
}