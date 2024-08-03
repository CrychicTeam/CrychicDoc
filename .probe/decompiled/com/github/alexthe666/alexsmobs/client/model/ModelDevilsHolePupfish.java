package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityDevilsHolePupfish;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelDevilsHolePupfish extends AdvancedEntityModel<EntityDevilsHolePupfish> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox bottom_fin;

    private final AdvancedModelBox dorsal_fin;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox left_fin;

    private final AdvancedModelBox right_fin;

    public ModelDevilsHolePupfish() {
        this.texHeight = 32;
        this.texWidth = 32;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-1.5F, -2.0F, -5.0F, 3.0F, 4.0F, 9.0F, 0.0F, false);
        this.bottom_fin = new AdvancedModelBox(this, "bottom_fin");
        this.bottom_fin.setRotationPoint(0.0F, 2.0F, 1.0F);
        this.body.addChild(this.bottom_fin);
        this.bottom_fin.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        this.dorsal_fin = new AdvancedModelBox(this, "dorsal_fin");
        this.dorsal_fin.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.body.addChild(this.dorsal_fin);
        this.dorsal_fin.setTextureOffset(11, 14).addBox(0.0F, -3.0F, -2.0F, 0.0F, 3.0F, 5.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -1.0F, 4.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 14).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 5.0F, 0.0F, false);
        this.left_fin = new AdvancedModelBox(this, "left_fin");
        this.left_fin.setRotationPoint(1.5F, 1.0F, -2.0F);
        this.body.addChild(this.left_fin);
        this.setRotationAngle(this.left_fin, 0.0F, 0.48F, 0.0F);
        this.left_fin.setTextureOffset(0, 14).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        this.right_fin = new AdvancedModelBox(this, "right_fin");
        this.right_fin.setRotationPoint(-1.5F, 1.0F, -2.0F);
        this.body.addChild(this.right_fin);
        this.setRotationAngle(this.right_fin, 0.0F, -0.48F, 0.0F);
        this.right_fin.setTextureOffset(0, 14).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityDevilsHolePupfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.3F;
        float idleDegree = 0.5F;
        float swimSpeed = 1.0F;
        float swimDegree = 0.5F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float landProgress = entity.prevOnLandProgress + (entity.onLandProgress - entity.prevOnLandProgress) * partialTick;
        float feedingProgress = entity.prevFeedProgress + (entity.feedProgress - entity.prevFeedProgress) * partialTick;
        this.progressRotationPrev(this.dorsal_fin, limbSwingAmount, Maths.rad(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.dorsal_fin, limbSwingAmount, 0.0F, 0.5F, 0.0F, 1.0F);
        this.progressRotationPrev(this.bottom_fin, limbSwingAmount, Maths.rad(10.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.bottom_fin, limbSwingAmount, 0.0F, -0.5F, -0.5F, 1.0F);
        this.progressRotationPrev(this.body, landProgress, 0.0F, 0.0F, Maths.rad(90.0), 5.0F);
        this.bob(this.body, idleSpeed, idleDegree, false, ageInTicks, 1.0F);
        this.swing(this.body, idleSpeed, idleDegree * 0.1F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail, idleSpeed, idleDegree * 0.3F, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail, swimSpeed, swimDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.body, swimSpeed, swimDegree * 0.3F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.left_fin, swimSpeed, swimDegree, false, 3.0F, 0.6F, limbSwing, limbSwingAmount);
        this.swing(this.right_fin, swimSpeed, swimDegree, true, 3.0F, 0.6F, limbSwing, limbSwingAmount);
        this.body.rotateAngleX += headPitch * (float) (Math.PI / 180.0);
        this.body.rotateAngleX = (float) ((double) this.body.rotateAngleX + (double) feedingProgress * Math.cos((double) (ageInTicks * 0.3F)) * 0.2F * Math.PI * 0.1F);
        this.body.rotationPointZ = (float) ((double) this.body.rotationPointZ + (double) feedingProgress * Math.abs(Math.sin((double) (ageInTicks * 0.3F))));
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.dorsal_fin, this.bottom_fin, this.tail, this.left_fin, this.right_fin);
    }

    public void setRotationAngle(AdvancedModelBox modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}