package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityTarantulaHawk;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelTarantulaHawk extends AdvancedEntityModel<EntityTarantulaHawk> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox wing_left;

    private final AdvancedModelBox wing_right;

    private final AdvancedModelBox legback_left;

    private final AdvancedModelBox legback_right;

    private final AdvancedModelBox legmid_left;

    private final AdvancedModelBox legmid_right;

    private final AdvancedModelBox legfront_left;

    private final AdvancedModelBox legfront_right;

    private final AdvancedModelBox head;

    private final AdvancedModelBox fang_left;

    private final AdvancedModelBox fang_right;

    private final AdvancedModelBox antenna_left;

    private final AdvancedModelBox antenna_right;

    private final AdvancedModelBox abdomen;

    private final AdvancedModelBox stinger;

    public ModelTarantulaHawk() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -15.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(33, 54).addBox(-3.0F, -3.0F, -5.0F, 6.0F, 6.0F, 10.0F, 0.0F, false);
        this.wing_left = new AdvancedModelBox(this, "wing_left");
        this.wing_left.setPos(1.0F, -3.0F, -3.0F);
        this.body.addChild(this.wing_left);
        this.setRotationAngle(this.wing_left, 0.0F, 0.0F, -0.1309F);
        this.wing_left.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -1.0F, 20.0F, 0.0F, 21.0F, 0.0F, false);
        this.wing_right = new AdvancedModelBox(this, "wing_right");
        this.wing_right.setPos(-1.0F, -3.0F, -3.0F);
        this.body.addChild(this.wing_right);
        this.setRotationAngle(this.wing_right, 0.0F, 0.0F, 0.1309F);
        this.wing_right.setTextureOffset(0, 0).addBox(-20.0F, 0.0F, -1.0F, 20.0F, 0.0F, 21.0F, 0.0F, true);
        this.legback_left = new AdvancedModelBox(this, "legback_left");
        this.legback_left.setPos(2.0F, 3.0F, 3.0F);
        this.body.addChild(this.legback_left);
        this.setRotationAngle(this.legback_left, 0.0F, -0.3054F, 0.0F);
        this.legback_left.setTextureOffset(0, 41).addBox(0.0F, -3.0F, 0.0F, 21.0F, 15.0F, 0.0F, 0.0F, false);
        this.legback_right = new AdvancedModelBox(this, "legback_right");
        this.legback_right.setPos(-2.0F, 3.0F, 3.0F);
        this.body.addChild(this.legback_right);
        this.setRotationAngle(this.legback_right, 0.0F, 0.3054F, 0.0F);
        this.legback_right.setTextureOffset(0, 41).addBox(-21.0F, -3.0F, 0.0F, 21.0F, 15.0F, 0.0F, 0.0F, true);
        this.legmid_left = new AdvancedModelBox(this, "legmid_left");
        this.legmid_left.setPos(2.0F, 3.0F, 0.0F);
        this.body.addChild(this.legmid_left);
        this.legmid_left.setTextureOffset(43, 38).addBox(0.0F, -3.0F, 0.0F, 19.0F, 15.0F, 0.0F, 0.0F, false);
        this.legmid_right = new AdvancedModelBox(this, "legmid_right");
        this.legmid_right.setPos(-2.0F, 3.0F, 0.0F);
        this.body.addChild(this.legmid_right);
        this.legmid_right.setTextureOffset(43, 38).addBox(-19.0F, -3.0F, 0.0F, 19.0F, 15.0F, 0.0F, 0.0F, true);
        this.legfront_left = new AdvancedModelBox(this, "legfront_left");
        this.legfront_left.setPos(2.0F, 3.0F, -3.0F);
        this.body.addChild(this.legfront_left);
        this.setRotationAngle(this.legfront_left, 0.0F, 0.2618F, 0.0F);
        this.legfront_left.setTextureOffset(41, 22).addBox(0.0F, -3.0F, 0.0F, 19.0F, 15.0F, 0.0F, 0.0F, false);
        this.legfront_right = new AdvancedModelBox(this, "legfront_right");
        this.legfront_right.setPos(-2.0F, 3.0F, -3.0F);
        this.body.addChild(this.legfront_right);
        this.setRotationAngle(this.legfront_right, 0.0F, -0.2618F, 0.0F);
        this.legfront_right.setTextureOffset(41, 22).addBox(-19.0F, -3.0F, 0.0F, 19.0F, 15.0F, 0.0F, 0.0F, true);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, 0.0F, -5.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 57).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 7.0F, 4.0F, 0.0F, false);
        this.fang_left = new AdvancedModelBox(this, "fang_left");
        this.fang_left.setPos(1.0F, 4.5F, -3.3F);
        this.head.addChild(this.fang_left);
        this.fang_left.setTextureOffset(0, 22).addBox(-1.0F, -1.0F, -1.0F, 3.0F, 4.0F, 1.0F, 0.0F, false);
        this.fang_right = new AdvancedModelBox(this, "fang_right");
        this.fang_right.setPos(-1.0F, 4.5F, -3.3F);
        this.head.addChild(this.fang_right);
        this.fang_right.setTextureOffset(0, 22).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 4.0F, 1.0F, 0.0F, true);
        this.antenna_left = new AdvancedModelBox(this, "antenna_left");
        this.antenna_left.setPos(1.0F, -2.0F, -4.0F);
        this.head.addChild(this.antenna_left);
        this.setRotationAngle(this.antenna_left, 0.0F, -0.3927F, -0.3491F);
        this.antenna_left.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -8.0F, 0.0F, 11.0F, 8.0F, 0.0F, false);
        this.antenna_right = new AdvancedModelBox(this, "antenna_right");
        this.antenna_right.setPos(-1.0F, -2.0F, -4.0F);
        this.head.addChild(this.antenna_right);
        this.setRotationAngle(this.antenna_right, 0.0F, 0.3927F, 0.3491F);
        this.antenna_right.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -8.0F, 0.0F, 11.0F, 8.0F, 0.0F, true);
        this.abdomen = new AdvancedModelBox(this, "abdomen");
        this.abdomen.setPos(0.0F, -2.0F, 5.0F);
        this.body.addChild(this.abdomen);
        this.abdomen.setTextureOffset(0, 22).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 6.0F, 12.0F, 0.0F, false);
        this.stinger = new AdvancedModelBox(this, "stinger");
        this.stinger.setPos(0.0F, 3.0F, 12.0F);
        this.abdomen.addChild(this.stinger);
        this.stinger.setTextureOffset(9, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.abdomen, this.head, this.antenna_left, this.antenna_right, this.legback_left, this.legback_right, this.legfront_left, this.legfront_right, this.legmid_left, this.legmid_right, new AdvancedModelBox[] { this.wing_left, this.wing_right, this.stinger, this.fang_left, this.fang_right });
    }

    public void setupAnim(EntityTarantulaHawk entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.25F;
        float idleDegree = 0.25F;
        float walkSpeed = entity.isDragging() ? 2.0F : 0.8F;
        float walkDegree = 0.4F;
        float flySpeed = 0.25F;
        float flyDegree = 0.6F;
        float digSpeed = 0.85F;
        float digDegree = 0.6F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTick;
        float dragProgress = entity.prevDragProgress + (entity.dragProgress - entity.prevDragProgress) * partialTick;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        float digProgress = entity.prevDigProgress + (entity.digProgress - entity.prevDigProgress) * partialTick;
        float stingProgress = entity.prevAttackProgress + (entity.attackProgress - entity.prevAttackProgress) * partialTick;
        float walkProgress = 5.0F - flyProgress;
        float stingFlyProgress = stingProgress * flyProgress * 0.2F;
        float stingGroundProgress = stingProgress * walkProgress * 0.2F;
        float flyAngle = entity.prevFlyAngle + (entity.getFlyAngle() - entity.prevFlyAngle) * partialTick;
        this.flap(this.antenna_left, idleSpeed, idleDegree * 1.0F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.antenna_right, idleSpeed, idleDegree * 1.0F, true, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.antenna_right, idleSpeed, idleDegree * 2.0F, true, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.antenna_left, idleSpeed, idleDegree * 2.0F, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.fang_right, idleSpeed, idleDegree * -0.5F, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.fang_left, idleSpeed, idleDegree * 0.5F, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.abdomen, idleSpeed, idleDegree * 0.4F, true, 0.0F, 0.1F, ageInTicks, 1.0F);
        this.progressPositionPrev(this.body, flyProgress, 0.0F, -3.0F, -2.0F, 5.0F);
        this.progressPositionPrev(this.legfront_right, flyProgress, 0.0F, -1.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.legfront_left, flyProgress, 0.0F, -1.0F, 2.0F, 5.0F);
        this.progressRotationPrev(this.legfront_left, flyProgress, Maths.rad(35.0), Maths.rad(-20.0), Maths.rad(30.0), 5.0F);
        this.progressRotationPrev(this.legfront_right, flyProgress, Maths.rad(35.0), Maths.rad(20.0), Maths.rad(-30.0), 5.0F);
        this.progressRotationPrev(this.legmid_left, flyProgress, Maths.rad(35.0), Maths.rad(-35.0), Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.legmid_right, flyProgress, Maths.rad(35.0), Maths.rad(35.0), Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.legback_left, flyProgress, Maths.rad(35.0), Maths.rad(-35.0), Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.legback_right, flyProgress, Maths.rad(35.0), Maths.rad(35.0), Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.wing_left, flyProgress, 0.0F, Maths.rad(35.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.wing_right, flyProgress, 0.0F, Maths.rad(-35.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.head, flyProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.wing_left, walkProgress, Maths.rad(20.0), Maths.rad(-20.0), Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.wing_right, walkProgress, Maths.rad(20.0), Maths.rad(20.0), Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.wing_right, walkProgress * limbSwingAmount, Maths.rad(20.0), Maths.rad(15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.wing_left, walkProgress * limbSwingAmount, Maths.rad(20.0), Maths.rad(-15.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.head, dragProgress, Maths.rad(-70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.fang_right, dragProgress, 0.0F, 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.fang_left, dragProgress, 0.0F, 0.0F, Maths.rad(-20.0), 5.0F);
        this.progressPositionPrev(this.head, dragProgress, 0.0F, 3.0F, -1.0F, 5.0F);
        this.progressPositionPrev(this.fang_right, dragProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.fang_left, dragProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 7.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legfront_right, sitProgress, 0.0F, Maths.rad(-25.0), Maths.rad(27.0), 5.0F);
        this.progressRotationPrev(this.legfront_left, sitProgress, 0.0F, Maths.rad(25.0), Maths.rad(-27.0), 5.0F);
        this.progressRotationPrev(this.legmid_right, sitProgress, 0.0F, 0.0F, Maths.rad(21.0), 5.0F);
        this.progressRotationPrev(this.legmid_left, sitProgress, 0.0F, 0.0F, Maths.rad(-21.0), 5.0F);
        this.progressRotationPrev(this.legback_right, sitProgress, 0.0F, Maths.rad(25.0), Maths.rad(27.0), 5.0F);
        this.progressRotationPrev(this.legback_left, sitProgress, 0.0F, Maths.rad(-25.0), Maths.rad(-27.0), 5.0F);
        this.progressRotationPrev(this.head, sitProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.abdomen, stingGroundProgress, Maths.rad(-70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.stinger, stingGroundProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, stingGroundProgress, Maths.rad(-40.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, stingGroundProgress, 0.0F, -2.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.abdomen, stingGroundProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.stinger, stingGroundProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legfront_right, stingGroundProgress, Maths.rad(40.0), 0.0F, Maths.rad(-40.0), 5.0F);
        this.progressRotationPrev(this.legfront_left, stingGroundProgress, Maths.rad(40.0), 0.0F, Maths.rad(40.0), 5.0F);
        this.progressRotationPrev(this.legmid_right, stingGroundProgress, Maths.rad(40.0), 0.0F, Maths.rad(-10.0), 5.0F);
        this.progressRotationPrev(this.legmid_left, stingGroundProgress, Maths.rad(40.0), 0.0F, Maths.rad(10.0), 5.0F);
        this.progressRotationPrev(this.legback_left, stingGroundProgress, Maths.rad(40.0), 0.0F, Maths.rad(-10.0), 5.0F);
        this.progressRotationPrev(this.legback_right, stingGroundProgress, Maths.rad(40.0), 0.0F, Maths.rad(10.0), 5.0F);
        this.progressRotationPrev(this.body, stingFlyProgress, Maths.rad(-70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.abdomen, stingFlyProgress, Maths.rad(-50.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.stinger, stingFlyProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, stingFlyProgress, 0.0F, -5.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.abdomen, stingFlyProgress, 0.0F, 0.0F, 2.0F, 5.0F);
        this.progressPositionPrev(this.stinger, 5.0F - stingProgress, 0.0F, 0.0F, -3.0F, 5.0F);
        this.stinger.setScale(1.0F, 1.0F, 1.0F + stingProgress * 0.15F);
        this.progressRotationPrev(this.body, digProgress, Maths.rad(40.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, digProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legfront_right, digProgress, Maths.rad(-50.0), 0.0F, Maths.rad(20.0), 5.0F);
        this.progressRotationPrev(this.legfront_left, digProgress, Maths.rad(-50.0), 0.0F, Maths.rad(-20.0), 5.0F);
        this.progressRotationPrev(this.legmid_right, digProgress, Maths.rad(-10.0), 0.0F, Maths.rad(-10.0), 5.0F);
        this.progressRotationPrev(this.legmid_left, digProgress, Maths.rad(-10.0), 0.0F, Maths.rad(10.0), 5.0F);
        this.progressRotationPrev(this.legback_left, digProgress, Maths.rad(-30.0), 0.0F, Maths.rad(30.0), 5.0F);
        this.progressRotationPrev(this.legback_right, digProgress, Maths.rad(-30.0), 0.0F, Maths.rad(-30.0), 5.0F);
        this.swing(this.legfront_left, digSpeed, digDegree * 1.0F, false, 1.0F, -0.5F, ageInTicks, digProgress * 0.2F);
        this.swing(this.legfront_right, digSpeed, digDegree * 1.0F, false, 1.0F, 0.5F, ageInTicks, digProgress * 0.2F);
        this.swing(this.head, digSpeed, digDegree * 1.0F, false, 0.0F, 0.0F, ageInTicks, digProgress * 0.2F);
        if (flyProgress > 0.0F) {
            this.bob(this.body, flySpeed, flyDegree * 5.0F, false, ageInTicks, 1.0F);
            this.flap(this.legfront_left, flySpeed, flyDegree * 0.5F, true, 1.0F, 0.1F, ageInTicks, 1.0F);
            this.flap(this.legfront_right, flySpeed, flyDegree * 0.5F, false, 1.0F, 0.1F, ageInTicks, 1.0F);
            this.flap(this.legmid_left, flySpeed, flyDegree * 0.5F, true, 2.0F, 0.1F, ageInTicks, 1.0F);
            this.flap(this.legmid_right, flySpeed, flyDegree * 0.5F, false, 2.0F, 0.1F, ageInTicks, 1.0F);
            this.flap(this.legback_left, flySpeed, flyDegree * 0.5F, true, 2.0F, 0.1F, ageInTicks, 1.0F);
            this.flap(this.legback_right, flySpeed, flyDegree * 0.5F, false, 2.0F, 0.1F, ageInTicks, 1.0F);
            this.walk(this.abdomen, flySpeed, flyDegree * 0.35F, false, 0.0F, -0.1F, ageInTicks, 1.0F);
            this.walk(this.head, flySpeed, flyDegree * 0.15F, true, 0.0F, -0.1F, ageInTicks, 1.0F);
            this.flap(this.wing_left, flySpeed * 7.0F, flyDegree, true, 0.0F, 0.1F, ageInTicks, 1.0F);
            this.flap(this.wing_right, flySpeed * 7.0F, flyDegree, false, 0.0F, 0.1F, ageInTicks, 1.0F);
        } else {
            this.swing(this.legback_right, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.flap(this.legback_right, walkSpeed, walkDegree * 0.8F, false, -1.5F, 0.4F, limbSwing, limbSwingAmount);
            this.swing(this.legfront_right, walkSpeed, walkDegree, false, 0.0F, -0.3F, limbSwing, limbSwingAmount);
            this.flap(this.legfront_right, walkSpeed, walkDegree * 0.8F, false, -1.5F, 0.4F, limbSwing, limbSwingAmount);
            this.swing(this.legmid_left, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.legmid_left, walkSpeed, walkDegree * 0.8F, false, -1.5F, -0.4F, limbSwing, limbSwingAmount);
            this.bob(this.body, walkSpeed * 2.0F, walkDegree * -3.0F, false, limbSwing, limbSwingAmount);
            float offsetleft = 2.0F;
            this.swing(this.legback_left, walkSpeed, -walkDegree * 1.2F, false, offsetleft, -0.2F, limbSwing, limbSwingAmount);
            this.flap(this.legback_left, walkSpeed, walkDegree * 0.8F, false, offsetleft - 1.5F, -0.4F, limbSwing, limbSwingAmount);
            this.swing(this.legfront_left, walkSpeed, -walkDegree, false, offsetleft, 0.3F, limbSwing, limbSwingAmount);
            this.flap(this.legfront_left, walkSpeed, walkDegree * 0.8F, false, offsetleft + 1.5F, -0.4F, limbSwing, limbSwingAmount);
            this.swing(this.legmid_right, walkSpeed, -walkDegree, false, offsetleft, 0.0F, limbSwing, limbSwingAmount);
            this.flap(this.legmid_right, walkSpeed, walkDegree * 0.8F, false, offsetleft - 1.5F, 0.4F, limbSwing, limbSwingAmount);
            this.swing(this.abdomen, walkSpeed, walkDegree * 0.4F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.head, walkSpeed, walkDegree * 0.4F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        }
        float f = Maths.rad((double) flyAngle);
        this.body.rotateAngleZ += f;
        if (dragProgress == 0.0F) {
            this.faceTarget(netHeadYaw, headPitch, 1.2F, new AdvancedModelBox[] { this.head });
        }
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