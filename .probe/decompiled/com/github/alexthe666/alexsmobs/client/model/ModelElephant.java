package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityElephant;
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

public class ModelElephant extends AdvancedEntityModel<EntityElephant> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox cabin;

    public final AdvancedModelBox left_chest;

    public final AdvancedModelBox right_chest;

    public final AdvancedModelBox tail;

    public final AdvancedModelBox left_arm;

    public final AdvancedModelBox right_arm;

    public final AdvancedModelBox left_leg;

    public final AdvancedModelBox right_leg;

    public final AdvancedModelBox head;

    public final AdvancedModelBox left_tusk;

    public final AdvancedModelBox right_tusk;

    public final AdvancedModelBox left_megatusk;

    public final AdvancedModelBox right_megatusk;

    public final AdvancedModelBox left_ear;

    public final AdvancedModelBox right_ear;

    public final AdvancedModelBox trunk1;

    public final AdvancedModelBox trunk2;

    private final ModelAnimator animator;

    public ModelElephant(float f) {
        this.texWidth = 256;
        this.texHeight = 256;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -41.0F, 1.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-14.0F, -15.0F, -24.0F, 28.0F, 29.0F, 47.0F, f, false);
        this.cabin = new AdvancedModelBox(this, "cabin");
        this.cabin.setRotationPoint(0.0F, -14.0F, -11.5F);
        this.body.addChild(this.cabin);
        this.cabin.setTextureOffset(0, 165).addBox(-13.0F, -29.0F, -11.5F, 26.0F, 28.0F, 23.0F, f, false);
        this.cabin.setTextureOffset(109, 176).addBox(-16.0F, -29.1F, -13.5F, 32.0F, 7.0F, 27.0F, f, false);
        this.left_chest = new AdvancedModelBox(this, "left_chest");
        this.left_chest.setRotationPoint(14.0F, -8.0F, 10.0F);
        this.body.addChild(this.left_chest);
        this.left_chest.setTextureOffset(57, 125).addBox(0.0F, -2.0F, -10.0F, 9.0F, 13.0F, 19.0F, f, false);
        this.right_chest = new AdvancedModelBox(this, "right_chest");
        this.right_chest.setRotationPoint(-14.0F, -8.0F, 10.0F);
        this.body.addChild(this.right_chest);
        this.right_chest.setTextureOffset(57, 125).addBox(-9.0F, -2.0F, -10.0F, 9.0F, 13.0F, 19.0F, f, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -5.0F, 23.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, 0.1745F, 0.0F, 0.0F);
        this.tail.setTextureOffset(42, 114).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 20.0F, 0.0F, f, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(8.1F, 9.5F, -18.2F);
        this.body.addChild(this.left_arm);
        this.left_arm.setTextureOffset(0, 0).addBox(-5.5F, 4.5F, -5.5F, 11.0F, 27.0F, 11.0F, f, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-8.1F, 9.5F, -18.2F);
        this.body.addChild(this.right_arm);
        this.right_arm.setTextureOffset(0, 0).addBox(-5.5F, 4.5F, -5.5F, 11.0F, 27.0F, 11.0F, f, true);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(8.2F, 11.5F, 17.2F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(71, 77).addBox(-5.5F, 2.5F, -5.5F, 11.0F, 27.0F, 11.0F, f, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-8.2F, 11.5F, 17.2F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(71, 77).addBox(-5.5F, 2.5F, -5.5F, 11.0F, 27.0F, 11.0F, f, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -2.0F, -25.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 77).addBox(-10.0F, -12.0F, -14.0F, 20.0F, 21.0F, 15.0F, f, false);
        this.left_tusk = new AdvancedModelBox(this, "left_tusk");
        this.left_tusk.setRotationPoint(6.5F, 8.0F, -11.5F);
        this.head.addChild(this.left_tusk);
        this.setRotationAngle(this.left_tusk, -0.4887F, -0.1571F, -0.2967F);
        this.left_tusk.setTextureOffset(104, 25).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, f, false);
        this.right_tusk = new AdvancedModelBox(this, "right_tusk");
        this.right_tusk.setRotationPoint(-6.5F, 8.0F, -11.5F);
        this.head.addChild(this.right_tusk);
        this.setRotationAngle(this.right_tusk, -0.4887F, 0.1571F, 0.2967F);
        this.right_tusk.setTextureOffset(104, 25).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, f, true);
        this.left_megatusk = new AdvancedModelBox(this, "left_megatusk");
        this.left_megatusk.setRotationPoint(6.5F, 8.0F, -11.5F);
        this.head.addChild(this.left_megatusk);
        this.setRotationAngle(this.left_megatusk, -0.2618F, 0.0524F, -0.2269F);
        this.left_megatusk.setTextureOffset(0, 114).addBox(-1.5F, 0.0F, -2.5F, 4.0F, 28.0F, 4.0F, f, false);
        this.left_megatusk.setTextureOffset(104, 25).addBox(-1.5F, 24.0F, -19.5F, 4.0F, 4.0F, 17.0F, f, false);
        this.right_megatusk = new AdvancedModelBox(this, "right_megatusk");
        this.right_megatusk.setRotationPoint(-6.5F, 8.0F, -11.5F);
        this.head.addChild(this.right_megatusk);
        this.setRotationAngle(this.right_megatusk, -0.2618F, -0.0524F, 0.2269F);
        this.right_megatusk.setTextureOffset(0, 114).addBox(-2.5F, 0.0F, -2.5F, 4.0F, 28.0F, 4.0F, f, true);
        this.right_megatusk.setTextureOffset(104, 25).addBox(-2.5F, 24.0F, -19.5F, 4.0F, 4.0F, 17.0F, f, true);
        this.left_ear = new AdvancedModelBox(this, "left_ear");
        this.left_ear.setRotationPoint(9.0F, -3.0F, -3.0F);
        this.head.addChild(this.left_ear);
        this.setRotationAngle(this.left_ear, 0.0F, -0.6981F, 0.0F);
        this.left_ear.setTextureOffset(104, 0).addBox(0.0F, -4.0F, -1.0F, 20.0F, 22.0F, 2.0F, f, false);
        this.right_ear = new AdvancedModelBox(this, "right_ear");
        this.right_ear.setRotationPoint(-9.0F, -3.0F, -3.0F);
        this.head.addChild(this.right_ear);
        this.setRotationAngle(this.right_ear, 0.0F, 0.6981F, 0.0F);
        this.right_ear.setTextureOffset(104, 0).addBox(-20.0F, -4.0F, -1.0F, 20.0F, 22.0F, 2.0F, f, true);
        this.trunk1 = new AdvancedModelBox(this, "trunk1");
        this.trunk1.setRotationPoint(0.0F, 3.0F, -16.0F);
        this.head.addChild(this.trunk1);
        this.trunk1.setTextureOffset(108, 108).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 24.0F, 8.0F, f, false);
        this.trunk2 = new AdvancedModelBox(this, "trunk2");
        this.trunk2.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.trunk1.addChild(this.trunk2);
        this.trunk2.setTextureOffset(17, 114).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 18.0F, 6.0F, f, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityElephant.ANIMATION_TRUMPET_0);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_ear, 0.0F, Maths.rad(25.0), 0.0F);
        this.animator.rotate(this.right_ear, 0.0F, Maths.rad(-25.0), 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-65.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(-35.0), 0.0F, 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_ear, 0.0F, Maths.rad(45.0), 0.0F);
        this.animator.rotate(this.right_ear, 0.0F, Maths.rad(-45.0), 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-75.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(-55.0), 0.0F, 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 1.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(3);
        this.animator.resetKeyframe(7);
        this.animator.setAnimation(EntityElephant.ANIMATION_TRUMPET_1);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, Maths.rad(-25.0));
        this.animator.rotate(this.left_ear, 0.0F, Maths.rad(25.0), 0.0F);
        this.animator.rotate(this.right_ear, 0.0F, Maths.rad(-25.0), 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-75.0), Maths.rad(25.0), 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(-35.0), Maths.rad(10.0), 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, Maths.rad(25.0));
        this.animator.rotate(this.left_ear, 0.0F, Maths.rad(25.0), 0.0F);
        this.animator.rotate(this.right_ear, 0.0F, Maths.rad(-25.0), 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-75.0), Maths.rad(-25.0), 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(-35.0), Maths.rad(-10.0), 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, Maths.rad(-25.0));
        this.animator.rotate(this.left_ear, 0.0F, Maths.rad(25.0), 0.0F);
        this.animator.rotate(this.right_ear, 0.0F, Maths.rad(-25.0), 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-75.0), Maths.rad(25.0), 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(-35.0), Maths.rad(10.0), 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, Maths.rad(25.0));
        this.animator.rotate(this.left_ear, 0.0F, Maths.rad(25.0), 0.0F);
        this.animator.rotate(this.right_ear, 0.0F, Maths.rad(-25.0), 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-75.0), Maths.rad(-25.0), 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(-35.0), Maths.rad(-10.0), 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(EntityElephant.ANIMATION_CHARGE_PREPARE);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_ear, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.left_ear, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(-15.0), 0.0F, Maths.rad(-15.0));
        this.animator.rotate(this.right_arm, Maths.rad(-15.0), 0.0F, Maths.rad(15.0));
        this.animator.rotate(this.left_leg, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_leg, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.move(this.right_arm, 0.0F, -9.0F, 0.0F);
        this.animator.move(this.left_arm, 0.0F, -9.0F, 0.0F);
        this.animator.move(this.left_leg, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.right_leg, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.head, 0.0F, 2.0F, 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 0.0F);
        this.animator.move(this.body, 0.0F, 6.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(10);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityElephant.ANIMATION_STOMP);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.body, Maths.rad(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_leg, Maths.rad(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_leg, Maths.rad(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(35.0), Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(35.0), Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, -6.0F, 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 0.0F);
        this.animator.move(this.left_leg, 0.0F, -1.0F, 0.0F);
        this.animator.move(this.right_leg, 0.0F, -1.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(7);
        this.animator.resetKeyframe(3);
        this.animator.setAnimation(EntityElephant.ANIMATION_FLING);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.head, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_ear, 0.0F, Maths.rad(25.0), 0.0F);
        this.animator.rotate(this.right_ear, 0.0F, Maths.rad(-25.0), 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, 3.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.head, 0.0F, -2.0F, -1.0F);
        this.animator.rotate(this.head, Maths.rad(-45.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_ear, 0.0F, Maths.rad(25.0), 0.0F);
        this.animator.rotate(this.right_ear, 0.0F, Maths.rad(-25.0), 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-55.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(-55.0), 0.0F, 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.resetKeyframe(8);
        this.animator.setAnimation(EntityElephant.ANIMATION_EAT);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(-45.0), 0.0F, 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(8);
        this.animator.rotate(this.head, Maths.rad(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-45.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(120.0), 0.0F, 0.0F);
        this.animator.move(this.trunk1, 0.0F, 0.0F, -1.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 1.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(9);
        this.animator.setAnimation(EntityElephant.ANIMATION_BREAKLEAVES);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, Maths.rad(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_ear, 0.0F, Maths.rad(5.0), 0.0F);
        this.animator.rotate(this.right_ear, 0.0F, Maths.rad(-5.0), 0.0F);
        this.animator.rotate(this.trunk1, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.trunk2, Maths.rad(-60.0), 0.0F, 0.0F);
        this.animator.move(this.trunk2, 0.0F, -2.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(5);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
            float f2 = 0.75F;
            this.head.rotationPointY = -10.0F;
            this.head.setScale(f, f, f);
            this.tail.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            this.trunk1.setScale(f2, f2, f2);
            this.trunk1.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.35F, 0.35F, 0.35F);
            matrixStackIn.translate(0.0, 2.8, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
            this.tail.setScale(1.0F, 1.0F, 1.0F);
            this.trunk1.setScale(1.0F, 1.0F, 1.0F);
        } else {
            this.head.rotationPointY = -2.0F;
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setupAnim(EntityElephant entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.7F;
        float walkDegree = 0.4F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.2F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float sitProgress = entityIn.prevSitProgress + (entityIn.sitProgress - entityIn.prevSitProgress) * partialTick;
        float standProgress = entityIn.prevStandProgress + (entityIn.standProgress - entityIn.prevStandProgress) * partialTick;
        this.progressRotationPrev(this.body, standProgress, Maths.rad(-60.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, standProgress, Maths.rad(60.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, standProgress, Maths.rad(60.0), Maths.rad(10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, standProgress, Maths.rad(60.0), Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, standProgress, Maths.rad(60.0), Maths.rad(-15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, standProgress, Maths.rad(60.0), Maths.rad(15.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, standProgress, 0.0F, -9.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, standProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, standProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressRotationPrev(this.tail, limbSwingAmount, Maths.rad(20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.right_arm, sitProgress, Maths.rad(-90.0), Maths.rad(10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, sitProgress, Maths.rad(-90.0), Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, sitProgress, Maths.rad(90.0), Maths.rad(5.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, sitProgress, Maths.rad(90.0), Maths.rad(-5.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 14.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.right_arm, sitProgress, 0.0F, 5.0F, 5.0F, 5.0F);
        this.progressPositionPrev(this.left_arm, sitProgress, 0.0F, 5.0F, 5.0F, 5.0F);
        this.progressPositionPrev(this.right_leg, sitProgress, 0.0F, 5.0F, -3.0F, 5.0F);
        this.progressPositionPrev(this.left_leg, sitProgress, 0.0F, 5.0F, -3.0F, 5.0F);
        this.progressPositionPrev(this.head, sitProgress, 0.0F, -3.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.trunk1, sitProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.trunk2, sitProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.trunk1, sitProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.trunk2, sitProgress, Maths.rad(60.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, sitProgress, Maths.rad(50.0), 0.0F, 0.0F, 5.0F);
        this.swing(this.right_ear, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.swing(this.left_ear, idleSpeed, idleDegree, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed * 0.7F, idleDegree * 0.1F, false, -1.0F, 0.05F, ageInTicks, 1.0F);
        this.walk(this.trunk1, idleSpeed * 0.4F, idleDegree * 0.7F, false, 0.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.trunk1, idleSpeed * 0.4F, idleDegree * 0.7F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.trunk2, idleSpeed * 0.4F, idleDegree * 0.35F, false, 0.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.tail, idleSpeed, idleDegree * 0.7F, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.right_arm, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_arm, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_leg, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_leg, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.head, walkSpeed, walkDegree * 0.1F, true, 0.0F, 0.2F, limbSwing, limbSwingAmount);
        this.walk(this.trunk1, walkSpeed, walkDegree * 0.1F, true, 0.0F, -0.1F, limbSwing, limbSwingAmount);
        this.walk(this.trunk2, walkSpeed, walkDegree * 0.1F, true, 0.0F, -0.3F, limbSwing, limbSwingAmount);
        this.swing(this.right_ear, walkSpeed, walkDegree * 0.34F, false, 1.0F, -0.01F, limbSwing, limbSwingAmount);
        this.swing(this.left_ear, walkSpeed, walkDegree * 0.34F, true, 1.0F, -0.01F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed * 2.0F, walkDegree * 2.0F, false, limbSwing, limbSwingAmount);
        this.faceTarget(netHeadYaw, headPitch, 2.0F, new AdvancedModelBox[] { this.head });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.head, this.trunk1, this.trunk2, this.tail, this.left_ear, this.right_ear, this.left_leg, this.right_leg, this.left_arm, new AdvancedModelBox[] { this.right_arm, this.cabin, this.left_chest, this.right_chest, this.left_tusk, this.right_tusk, this.left_megatusk, this.right_megatusk });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}