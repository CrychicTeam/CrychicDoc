package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCrocodile;
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

public class ModelCrocodile extends AdvancedEntityModel<EntityCrocodile> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox left_foot;

    private final AdvancedModelBox right_leg;

    private final AdvancedModelBox right_foot;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox left_hand;

    private final AdvancedModelBox right_arm;

    private final AdvancedModelBox right_hand;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox tail3;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox crown;

    private final AdvancedModelBox left_upperteeth;

    private final AdvancedModelBox right_upperteeth;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox left_lowerteeth;

    private final AdvancedModelBox right_lowerteeth;

    private final ModelAnimator animator;

    public ModelCrocodile() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -9.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-8.0F, -7.0F, -13.0F, 16.0F, 12.0F, 27.0F, 0.0F, false);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(8.0F, 3.0F, 10.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, -5.0F, 5.0F, 8.0F, 8.0F, 0.0F, false);
        this.left_foot = new AdvancedModelBox(this, "left_foot");
        this.left_foot.setRotationPoint(2.0F, 6.0F, -3.0F);
        this.left_leg.addChild(this.left_foot);
        this.left_foot.setTextureOffset(45, 42).addBox(-2.0F, -0.01F, -5.0F, 5.0F, 0.0F, 6.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-8.0F, 3.0F, 10.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(0, 0).addBox(-3.0F, -2.0F, -5.0F, 5.0F, 8.0F, 8.0F, 0.0F, true);
        this.right_foot = new AdvancedModelBox(this, "right_foot");
        this.right_foot.setRotationPoint(-2.0F, 6.0F, -3.0F);
        this.right_leg.addChild(this.right_foot);
        this.right_foot.setTextureOffset(45, 42).addBox(-3.0F, -0.01F, -5.0F, 5.0F, 0.0F, 6.0F, 0.0F, true);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(9.0F, 1.0F, -9.0F);
        this.body.addChild(this.left_arm);
        this.left_arm.setTextureOffset(0, 40).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
        this.left_hand = new AdvancedModelBox(this, "left_hand");
        this.left_hand.setRotationPoint(0.0F, 8.0F, 1.0F);
        this.left_arm.addChild(this.left_hand);
        this.left_hand.setTextureOffset(0, 17).addBox(-2.0F, -0.01F, -7.0F, 6.0F, 0.0F, 7.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-9.0F, 1.0F, -9.0F);
        this.body.addChild(this.right_arm);
        this.right_arm.setTextureOffset(0, 40).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, true);
        this.right_hand = new AdvancedModelBox(this, "right_hand");
        this.right_hand.setRotationPoint(0.0F, 8.0F, 1.0F);
        this.right_arm.addChild(this.right_hand);
        this.right_hand.setTextureOffset(0, 17).addBox(-4.0F, -0.01F, -7.0F, 6.0F, 0.0F, 7.0F, 0.0F, true);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setRotationPoint(0.0F, 0.0F, 16.0F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(0, 40).addBox(-5.0F, -5.0F, -2.0F, 10.0F, 10.0F, 24.0F, 0.0F, false);
        this.tail1.setTextureOffset(45, 51).addBox(-5.0F, -7.0F, -2.0F, 10.0F, 2.0F, 24.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setRotationPoint(0.0F, 1.0F, 24.0F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(62, 15).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 7.0F, 25.0F, 0.0F, false);
        this.tail2.setTextureOffset(43, 78).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 2.0F, 20.0F, 0.0F, false);
        this.tail3 = new AdvancedModelBox(this, "tail3");
        this.tail3.setRotationPoint(0.0F, 0.0F, 18.0F);
        this.tail2.addChild(this.tail3);
        this.tail3.setTextureOffset(0, 75).addBox(0.0F, -6.0F, 0.0F, 0.0F, 10.0F, 21.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setRotationPoint(0.0F, 0.0F, -15.0F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(80, 89).addBox(-6.0F, -5.0F, -10.0F, 12.0F, 10.0F, 12.0F, 0.0F, false);
        this.neck.setTextureOffset(60, 0).addBox(-4.0F, -6.0F, -10.0F, 8.0F, 1.0F, 12.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, 1.0F, -11.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(72, 78).addBox(-5.0F, -4.0F, -5.0F, 10.0F, 4.0F, 6.0F, 0.0F, false);
        this.head.setTextureOffset(60, 14).addBox(-4.0F, -5.0F, -5.0F, 8.0F, 1.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(22, 78).addBox(-3.0F, -4.0F, -17.0F, 6.0F, 4.0F, 12.0F, 0.0F, false);
        this.crown = new AdvancedModelBox(this, "crown");
        this.crown.setRotationPoint(0.0F, -5.0F, -2.0F);
        this.head.addChild(this.crown);
        this.crown.setTextureOffset(49, 54).addBox(-1.5F, -5.0F, -2.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);
        this.left_upperteeth = new AdvancedModelBox(this, "left_upperteeth");
        this.left_upperteeth.setRotationPoint(0.0F, 0.0F, -17.0F);
        this.head.addChild(this.left_upperteeth);
        this.setRotationAngle(this.left_upperteeth, 0.0F, 0.0F, -0.0873F);
        this.left_upperteeth.setTextureOffset(104, 23).addBox(0.0F, 0.0F, -0.025F, 3.0F, 2.0F, 11.0F, 0.0F, false);
        this.right_upperteeth = new AdvancedModelBox(this, "right_upperteeth");
        this.right_upperteeth.setRotationPoint(0.0F, 0.0F, -17.0F);
        this.head.addChild(this.right_upperteeth);
        this.setRotationAngle(this.right_upperteeth, 0.0F, 0.0F, 0.0873F);
        this.right_upperteeth.setTextureOffset(104, 23).addBox(-3.0F, 0.0F, -0.025F, 3.0F, 2.0F, 11.0F, 0.0F, true);
        this.jaw = new AdvancedModelBox(this, "jaw");
        this.jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(100, 7).addBox(-5.5F, -2.0F, -6.0F, 11.0F, 5.0F, 7.0F, 0.001F, false);
        this.jaw.setTextureOffset(90, 48).addBox(-3.0F, 0.0F, -17.0F, 6.0F, 3.0F, 11.0F, 0.0F, false);
        this.left_lowerteeth = new AdvancedModelBox(this, "left_lowerteeth");
        this.left_lowerteeth.setRotationPoint(0.0F, 0.0F, -17.0F);
        this.jaw.addChild(this.left_lowerteeth);
        this.setRotationAngle(this.left_lowerteeth, 0.0F, 0.0F, 0.0873F);
        this.left_lowerteeth.setTextureOffset(105, 67).addBox(0.0F, -2.0F, -0.025F, 3.0F, 2.0F, 11.0F, 0.0F, false);
        this.right_lowerteeth = new AdvancedModelBox(this, "right_lowerteeth");
        this.right_lowerteeth.setRotationPoint(0.0F, 0.0F, -17.0F);
        this.jaw.addChild(this.right_lowerteeth);
        this.setRotationAngle(this.right_lowerteeth, 0.0F, 0.0F, -0.0873F);
        this.right_lowerteeth.setTextureOffset(105, 67).addBox(-3.0F, -2.0F, -0.025F, 3.0F, 2.0F, 11.0F, 0.0F, true);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityCrocodile.ANIMATION_LUNGE);
        this.animator.startKeyframe(2);
        this.animator.move(this.body, 0.0F, 0.0F, 2.0F);
        this.animator.rotate(this.head, Maths.rad(-55.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(60.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(1);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, -14.0F);
        this.animator.rotate(this.head, Maths.rad(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_arm, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_arm, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.right_leg, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.left_leg, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 3.0F);
        this.animator.rotate(this.head, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(EntityCrocodile.ANIMATION_DEATHROLL);
        this.animator.startKeyframe(30);
        int rolls = 3;
        this.animator.rotate(this.body, 0.0F, 0.0F, Maths.rad((double) (-360 * rolls)));
        this.animator.endKeyframe();
    }

    public void setupAnim(EntityCrocodile entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        boolean swimAnimate = entityIn.m_20069_();
        float walkSpeed = 0.7F;
        float walkDegree = 0.7F;
        float swimSpeed = 1.0F;
        float swimDegree = 0.2F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float swimProgress = entityIn.prevSwimProgress + (entityIn.swimProgress - entityIn.prevSwimProgress) * partialTick;
        float baskProgress = entityIn.prevBaskingProgress + (entityIn.baskingProgress - entityIn.prevBaskingProgress) * partialTick;
        float grabProgress = entityIn.prevGrabProgress + (entityIn.grabProgress - entityIn.prevGrabProgress) * partialTick;
        if (!swimAnimate && grabProgress <= 0.0F) {
            this.faceTarget(netHeadYaw, headPitch, 2.0F, new AdvancedModelBox[] { this.neck, this.head });
        }
        this.progressRotationPrev(this.jaw, grabProgress, Maths.rad(30.0), 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.head, grabProgress, Maths.rad(-10.0), 0.0F, 0.0F, 10.0F);
        if (entityIn.baskingType == 0) {
            this.progressRotationPrev(this.body, baskProgress, 0.0F, Maths.rad(-7.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.tail1, baskProgress, 0.0F, Maths.rad(30.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.tail2, baskProgress, 0.0F, Maths.rad(20.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.tail3, baskProgress, 0.0F, Maths.rad(30.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.neck, baskProgress, 0.0F, Maths.rad(-10.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.head, baskProgress, Maths.rad(-60.0), Maths.rad(-10.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.jaw, baskProgress, Maths.rad(60.0), 0.0F, 0.0F, 10.0F);
        } else if (entityIn.baskingType == 1) {
            this.progressRotationPrev(this.body, baskProgress, 0.0F, Maths.rad(7.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.tail1, baskProgress, 0.0F, Maths.rad(-30.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.tail2, baskProgress, 0.0F, Maths.rad(-20.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.tail3, baskProgress, 0.0F, Maths.rad(-30.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.neck, baskProgress, 0.0F, Maths.rad(10.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.head, baskProgress, Maths.rad(-60.0), Maths.rad(10.0), 0.0F, 10.0F);
            this.progressRotationPrev(this.jaw, baskProgress, Maths.rad(60.0), 0.0F, 0.0F, 10.0F);
        }
        this.progressPositionPrev(this.body, baskProgress, 0.0F, 3.0F, -3.0F, 10.0F);
        this.progressPositionPrev(this.tail1, baskProgress, 0.0F, 0.0F, -3.0F, 10.0F);
        this.progressPositionPrev(this.right_leg, baskProgress, 0.0F, -3.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.left_leg, baskProgress, 0.0F, -3.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.right_arm, baskProgress, 0.0F, -3.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.left_arm, baskProgress, 0.0F, -3.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.right_arm, swimProgress, 0.0F, 2.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.left_arm, baskProgress, 0.0F, 0.0F, Maths.rad(-30.0), 10.0F);
        this.progressRotationPrev(this.left_hand, baskProgress, 0.0F, 0.0F, Maths.rad(30.0), 10.0F);
        this.progressRotationPrev(this.right_arm, baskProgress, 0.0F, 0.0F, Maths.rad(30.0), 10.0F);
        this.progressRotationPrev(this.right_hand, baskProgress, 0.0F, 0.0F, Maths.rad(-30.0), 10.0F);
        this.progressRotationPrev(this.left_leg, baskProgress, 0.0F, 0.0F, Maths.rad(-30.0), 10.0F);
        this.progressRotationPrev(this.left_foot, baskProgress, 0.0F, 0.0F, Maths.rad(30.0), 10.0F);
        this.progressRotationPrev(this.right_leg, baskProgress, 0.0F, 0.0F, Maths.rad(30.0), 10.0F);
        this.progressRotationPrev(this.right_foot, baskProgress, 0.0F, 0.0F, Maths.rad(-30.0), 10.0F);
        this.progressRotationPrev(this.right_arm, swimProgress, Maths.rad(75.0), 0.0F, Maths.rad(90.0), 10.0F);
        this.progressPositionPrev(this.left_arm, swimProgress, 0.0F, 2.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.left_arm, swimProgress, Maths.rad(75.0), 0.0F, Maths.rad(-90.0), 10.0F);
        this.progressPositionPrev(this.right_leg, swimProgress, 0.0F, 2.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.right_leg, swimProgress, Maths.rad(75.0), 0.0F, Maths.rad(90.0), 10.0F);
        this.progressPositionPrev(this.left_leg, swimProgress, 0.0F, 2.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.left_leg, swimProgress, Maths.rad(75.0), 0.0F, Maths.rad(-90.0), 10.0F);
        this.progressPositionPrev(this.left_foot, swimProgress, -2.0F, 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.left_foot, swimProgress, Maths.rad(75.0), 0.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.right_foot, swimProgress, 2.0F, 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.right_foot, swimProgress, Maths.rad(75.0), 0.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.left_hand, swimProgress, -1.0F, 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.left_hand, swimProgress, Maths.rad(75.0), 0.0F, 0.0F, 10.0F);
        this.progressPositionPrev(this.right_hand, swimProgress, 1.0F, 0.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.right_hand, swimProgress, Maths.rad(75.0), 0.0F, 0.0F, 10.0F);
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.tail1, this.tail2, this.tail3 };
        if (swimAnimate) {
            this.walk(this.right_arm, swimSpeed, swimDegree, false, 0.0F, -0.25F, limbSwing, limbSwingAmount);
            this.walk(this.left_arm, swimSpeed, swimDegree, false, 0.0F, -0.25F, limbSwing, limbSwingAmount);
            this.walk(this.right_leg, swimSpeed, swimDegree, true, 0.0F, 0.25F, limbSwing, limbSwingAmount);
            this.walk(this.left_leg, swimSpeed, swimDegree, true, 0.0F, 0.25F, limbSwing, limbSwingAmount);
            this.swing(this.body, swimSpeed, swimDegree * 0.7F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.neck, swimSpeed, swimDegree * 0.5F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.head, swimSpeed, swimDegree * 0.3F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
            this.chainSwing(tailBoxes, swimSpeed, swimDegree * 2.0F, -2.5, limbSwing, limbSwingAmount);
        } else {
            this.walk(this.right_arm, walkSpeed, walkDegree, false, 0.0F, 0.25F, limbSwing, limbSwingAmount);
            this.walk(this.left_arm, walkSpeed, walkDegree, true, 0.0F, -0.25F, limbSwing, limbSwingAmount);
            this.walk(this.right_leg, walkSpeed, walkDegree, true, 0.0F, 0.25F, limbSwing, limbSwingAmount);
            this.walk(this.left_leg, walkSpeed, walkDegree, false, 0.0F, -0.25F, limbSwing, limbSwingAmount);
            this.swing(this.body, walkSpeed, walkDegree * 0.1F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.neck, walkSpeed, walkDegree * 0.1F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
            this.chainSwing(tailBoxes, walkSpeed, walkDegree * 0.3F, -2.5, limbSwing, limbSwingAmount);
        }
        if (baskProgress > 0.0F) {
            this.walk(this.head, 0.1F, 0.1F, false, 1.0F, 0.1F, ageInTicks, 1.0F);
            this.jaw.rotateAngleX = -this.head.rotateAngleX;
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.neck, this.head, this.jaw, this.left_arm, this.right_arm, this.left_leg, this.right_leg, this.tail1, this.tail2, this.tail3, new AdvancedModelBox[] { this.crown, this.left_foot, this.right_foot, this.left_hand, this.right_hand, this.left_upperteeth, this.right_upperteeth, this.left_lowerteeth, this.right_lowerteeth });
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.15F, 0.15F, 0.15F);
            matrixStackIn.translate(0.0, 8.5, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}