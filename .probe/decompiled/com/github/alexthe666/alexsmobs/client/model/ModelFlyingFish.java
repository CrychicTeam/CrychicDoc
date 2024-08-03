package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityFlyingFish;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelFlyingFish extends AdvancedEntityModel<EntityFlyingFish> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox left_pectoralFin;

    private final AdvancedModelBox right_pectoralFin;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail_fin;

    private final AdvancedModelBox left_pelvicFin;

    private final AdvancedModelBox right_pelvicFin;

    public ModelFlyingFish() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -2.0F, -2.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(11, 16).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 4.0F, 8.0F, 0.0F, false);
        this.body.setTextureOffset(10, 6).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        this.left_pectoralFin = new AdvancedModelBox(this, "left_pectoralFin");
        this.left_pectoralFin.setRotationPoint(1.5F, 0.0F, -1.0F);
        this.body.addChild(this.left_pectoralFin);
        this.setRotationAngle(this.left_pectoralFin, -0.7503F, -1.3169F, -0.8498F);
        this.left_pectoralFin.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -2.0F, 13.0F, 0.0F, 5.0F, 0.0F, false);
        this.right_pectoralFin = new AdvancedModelBox(this, "right_pectoralFin");
        this.right_pectoralFin.setRotationPoint(-1.5F, 0.0F, -1.0F);
        this.body.addChild(this.right_pectoralFin);
        this.setRotationAngle(this.right_pectoralFin, -0.7503F, 1.3169F, 0.8498F);
        this.right_pectoralFin.setTextureOffset(0, 0).addBox(-13.0F, 0.0F, -2.0F, 13.0F, 0.0F, 5.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -1.0F, 4.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(26, 6).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 3.0F, 8.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 0).addBox(0.0F, -2.0F, 2.0F, 0.0F, 1.0F, 2.0F, 0.0F, false);
        this.tail_fin = new AdvancedModelBox(this, "tail_fin");
        this.tail_fin.setRotationPoint(0.0F, 1.0F, 6.0F);
        this.tail.addChild(this.tail_fin);
        this.tail_fin.setTextureOffset(0, 6).addBox(0.0F, -5.0F, -1.0F, 0.0F, 8.0F, 9.0F, 0.0F, false);
        this.left_pelvicFin = new AdvancedModelBox(this, "left_pelvicFin");
        this.left_pelvicFin.setRotationPoint(1.0F, 2.0F, 1.0F);
        this.tail.addChild(this.left_pelvicFin);
        this.setRotationAngle(this.left_pelvicFin, 0.0F, 0.0F, -0.5672F);
        this.left_pelvicFin.setTextureOffset(0, 6).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 0.0F, false);
        this.right_pelvicFin = new AdvancedModelBox(this, "right_pelvicFin");
        this.right_pelvicFin.setRotationPoint(-1.0F, 2.0F, 1.0F);
        this.tail.addChild(this.right_pelvicFin);
        this.setRotationAngle(this.right_pelvicFin, 0.0F, 0.0F, 0.5672F);
        this.right_pelvicFin.setTextureOffset(0, 6).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityFlyingFish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.2F;
        float idleDegree = 0.3F;
        float swimSpeed = 0.55F;
        float swimDegree = 0.5F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTick;
        float landProgress = entity.prevOnLandProgress + (entity.onLandProgress - entity.prevOnLandProgress) * partialTick;
        float swimProgress = Math.max(0.0F, 5.0F - flyProgress) * 0.2F;
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.body, this.tail, this.tail_fin };
        this.progressRotationPrev(this.left_pectoralFin, flyProgress, Maths.rad(45.0), Maths.rad(80.0), Maths.rad(45.0), 5.0F);
        this.progressRotationPrev(this.right_pectoralFin, flyProgress, Maths.rad(45.0), Maths.rad(-80.0), Maths.rad(-45.0), 5.0F);
        this.progressRotationPrev(this.left_pelvicFin, flyProgress, 0.0F, 0.0F, Maths.rad(-35.0), 5.0F);
        this.progressRotationPrev(this.right_pelvicFin, flyProgress, 0.0F, 0.0F, Maths.rad(35.0), 5.0F);
        this.progressPositionPrev(this.left_pectoralFin, flyProgress, 0.0F, -1.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.right_pectoralFin, flyProgress, 0.0F, -1.0F, 1.0F, 5.0F);
        this.progressRotationPrev(this.body, landProgress, 0.0F, 0.0F, Maths.rad(-90.0), 5.0F);
        this.progressRotation(this.left_pectoralFin, landProgress, 0.0F, 0.0F, Maths.rad(70.0), 5.0F);
        this.progressRotation(this.right_pectoralFin, landProgress, 0.0F, 0.0F, Maths.rad(-80.0), 5.0F);
        this.bob(this.body, idleSpeed, idleDegree, false, ageInTicks, 1.0F);
        this.chainSwing(tailBoxes, idleSpeed, idleDegree * 0.1F, -2.5, ageInTicks, 1.0F);
        this.flap(this.left_pelvicFin, idleSpeed, idleDegree, false, 3.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.right_pelvicFin, idleSpeed, idleDegree, true, 3.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.left_pectoralFin, idleSpeed, idleDegree * 0.25F, true, -1.0F, -0.12F, ageInTicks, 1.0F);
        this.flap(this.right_pectoralFin, idleSpeed, idleDegree * 0.25F, false, -1.0F, -0.12F, ageInTicks, 1.0F);
        this.chainSwing(tailBoxes, swimSpeed, swimDegree * 0.9F, -2.0, limbSwing, limbSwingAmount * swimProgress);
        this.chainSwing(tailBoxes, swimSpeed, swimDegree * 0.3F, -1.0, limbSwing, limbSwingAmount * flyProgress * 0.2F);
        this.flap(this.left_pectoralFin, swimSpeed * 2.0F, swimDegree * 0.2F, true, 1.0F, 0.1F, ageInTicks, flyProgress * 0.2F);
        this.flap(this.right_pectoralFin, swimSpeed * 2.0F, swimDegree * 0.2F, false, 1.0F, 0.1F, ageInTicks, flyProgress * 0.2F);
        this.flap(this.left_pelvicFin, swimSpeed * 2.0F, swimDegree * 0.2F, true, 1.0F, 0.3F, ageInTicks, flyProgress * 0.2F);
        this.flap(this.right_pelvicFin, swimSpeed * 2.0F, swimDegree * 0.2F, false, 1.0F, 0.3F, ageInTicks, flyProgress * 0.2F);
        this.body.rotateAngleX += headPitch * (float) (Math.PI / 180.0);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.tail_fin, this.left_pectoralFin, this.left_pelvicFin, this.right_pectoralFin, this.right_pelvicFin);
    }

    public void setRotationAngle(AdvancedModelBox modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}