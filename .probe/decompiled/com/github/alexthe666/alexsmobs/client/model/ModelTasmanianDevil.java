package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityTasmanianDevil;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelTasmanianDevil extends AdvancedEntityModel<EntityTasmanianDevil> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox armLeft;

    private final AdvancedModelBox armRight;

    private final AdvancedModelBox legLeft;

    private final AdvancedModelBox legRight;

    private final AdvancedModelBox head;

    private final AdvancedModelBox earLeft;

    private final AdvancedModelBox earRight;

    private final AdvancedModelBox tail;

    public ModelAnimator animator;

    public ModelTasmanianDevil() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-3.5F, -3.0F, -5.0F, 7.0F, 6.0F, 11.0F, 0.0F, false);
        this.armLeft = new AdvancedModelBox(this);
        this.armLeft.setRotationPoint(2.6F, 3.0F, -3.0F);
        this.body.addChild(this.armLeft);
        this.armLeft.setTextureOffset(26, 18).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        this.armRight = new AdvancedModelBox(this);
        this.armRight.setRotationPoint(-2.6F, 3.0F, -3.0F);
        this.body.addChild(this.armRight);
        this.armRight.setTextureOffset(26, 18).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
        this.legLeft = new AdvancedModelBox(this);
        this.legLeft.setRotationPoint(2.6F, 3.0F, 4.0F);
        this.body.addChild(this.legLeft);
        this.legLeft.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        this.legRight = new AdvancedModelBox(this);
        this.legRight.setRotationPoint(-2.6F, 3.0F, 4.0F);
        this.body.addChild(this.legRight);
        this.legRight.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -2.0F, -6.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 18).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(26, 0).addBox(-2.0F, -2.0F, -6.0F, 4.0F, 4.0F, 3.0F, 0.0F, false);
        this.earLeft = new AdvancedModelBox(this);
        this.earLeft.setRotationPoint(2.0F, -1.0F, 0.0F);
        this.head.addChild(this.earLeft);
        this.setRotationAngle(this.earLeft, 0.2182F, 0.0F, 0.3054F);
        this.earLeft.setTextureOffset(0, 27).addBox(-1.0F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        this.earRight = new AdvancedModelBox(this);
        this.earRight.setRotationPoint(-2.0F, -1.0F, 0.0F);
        this.head.addChild(this.earRight);
        this.setRotationAngle(this.earRight, 0.2182F, 0.0F, -0.3054F);
        this.earRight.setTextureOffset(0, 27).addBox(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 1.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -2.0F, 6.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, -0.5236F, 0.0F, 0.0F);
        this.tail.setTextureOffset(15, 21).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.tail, this.earLeft, this.earRight, this.legLeft, this.legRight, this.armLeft, this.armRight);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityTasmanianDevil.ANIMATION_ATTACK);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, -1.0F, 1.5F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.head, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
        this.animator.endKeyframe();
        this.animator.setAnimation(EntityTasmanianDevil.ANIMATION_HOWL);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.head, Maths.rad(-45.0), Maths.rad(45.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(20);
        this.animator.rotate(this.head, Maths.rad(-45.0), Maths.rad(-45.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(EntityTasmanianDevil entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 1.0F;
        float walkDegree = 0.5F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float baskProgress0 = entity.prevBaskProgress + (entity.baskProgress - entity.prevBaskProgress) * partialTick;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        float baskProgress = Math.max(0.0F, baskProgress0 - sitProgress);
        this.progressRotationPrev(this.tail, limbSwingAmount, Maths.rad(10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.body, sitProgress, Maths.rad(-25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.armRight, sitProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.armLeft, sitProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, sitProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, sitProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legRight, sitProgress, Maths.rad(-40.0), Maths.rad(40.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.legLeft, sitProgress, Maths.rad(-40.0), Maths.rad(-40.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 0.5F, 0.0F, 5.0F);
        this.progressPositionPrev(this.armRight, sitProgress, 0.0F, 1.0F, 1.2F, 5.0F);
        this.progressPositionPrev(this.armLeft, sitProgress, 0.0F, 1.0F, 1.2F, 5.0F);
        this.progressPositionPrev(this.legLeft, sitProgress, 1.0F, -1.5F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legRight, sitProgress, -1.0F, -1.5F, 0.0F, 5.0F);
        this.progressRotationPrev(this.armRight, baskProgress, Maths.rad(-80.0), Maths.rad(40.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.armLeft, baskProgress, Maths.rad(-80.0), Maths.rad(-40.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.legRight, baskProgress, Maths.rad(80.0), Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.legLeft, baskProgress, Maths.rad(80.0), Maths.rad(20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, baskProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, baskProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.earRight, baskProgress, 0.0F, Maths.rad(40.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.earLeft, baskProgress, 0.0F, Maths.rad(-40.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, baskProgress, 0.0F, 3.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, baskProgress, 0.0F, 1.2F, 0.0F, 5.0F);
        this.progressPositionPrev(this.armRight, baskProgress, 0.0F, -1.0F, -1.2F, 5.0F);
        this.progressPositionPrev(this.armLeft, baskProgress, 0.0F, -1.0F, -1.2F, 5.0F);
        this.progressPositionPrev(this.legLeft, baskProgress, 1.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legRight, baskProgress, -1.0F, -1.0F, 0.0F, 5.0F);
        this.walk(this.armRight, walkSpeed, walkDegree * 1.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.armRight, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.armLeft, walkSpeed, walkDegree * 1.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.armLeft, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.legRight, walkSpeed, walkDegree * 1.1F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.legRight, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.legLeft, walkSpeed, walkDegree * 1.1F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.legLeft, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.swing(this.body, walkSpeed, walkDegree * 0.6F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 0.6F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.head, walkSpeed, walkDegree * 0.6F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.body, walkSpeed, walkDegree * 0.05F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.tail, walkSpeed, walkDegree * 0.6F, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.bob(this.head, walkSpeed, walkDegree * 0.6F, false, limbSwing, limbSwingAmount);
        this.swing(this.earRight, walkSpeed, walkDegree * 0.6F, false, -1.0F, 0.3F, limbSwing, limbSwingAmount);
        this.swing(this.earLeft, walkSpeed, walkDegree * 0.6F, true, -1.0F, 0.3F, limbSwing, limbSwingAmount);
        this.swing(this.tail, idleSpeed, idleDegree * 0.9F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
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

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}