package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCatfish;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelCatfishSmall extends AdvancedEntityModel<EntityCatfish> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox left_barbel;

    private final AdvancedModelBox right_barbel;

    private final AdvancedModelBox dorsal_fin;

    private final AdvancedModelBox left_fin;

    private final AdvancedModelBox right_fin;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail_fin;

    public ModelCatfishSmall() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-4.0F, -3.0F, -8.0F, 8.0F, 6.0F, 14.0F, 0.0F, false);
        this.left_barbel = new AdvancedModelBox(this, "left_barbel");
        this.left_barbel.setRotationPoint(4.0F, 1.0F, -6.0F);
        this.body.addChild(this.left_barbel);
        this.setRotationAngle(this.left_barbel, 0.0F, -0.6109F, 0.0F);
        this.left_barbel.setTextureOffset(0, 10).addBox(0.0F, 0.0F, 0.0F, 6.0F, 3.0F, 0.0F, 0.0F, false);
        this.right_barbel = new AdvancedModelBox(this, "right_barbel");
        this.right_barbel.setRotationPoint(-4.0F, 1.0F, -6.0F);
        this.body.addChild(this.right_barbel);
        this.setRotationAngle(this.right_barbel, 0.0F, 0.6109F, 0.0F);
        this.right_barbel.setTextureOffset(0, 10).addBox(-6.0F, 0.0F, 0.0F, 6.0F, 3.0F, 0.0F, 0.0F, true);
        this.dorsal_fin = new AdvancedModelBox(this, "dorsal_fin");
        this.dorsal_fin.setRotationPoint(0.0F, -3.0F, -2.0F);
        this.body.addChild(this.dorsal_fin);
        this.setRotationAngle(this.dorsal_fin, -0.1309F, 0.0F, 0.0F);
        this.dorsal_fin.setTextureOffset(0, 21).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 5.0F, 0.0F, false);
        this.left_fin = new AdvancedModelBox(this, "left_fin");
        this.left_fin.setRotationPoint(4.0F, 2.0F, -3.0F);
        this.body.addChild(this.left_fin);
        this.setRotationAngle(this.left_fin, 0.0F, 0.0F, -0.9163F);
        this.left_fin.setTextureOffset(19, 21).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 4.0F, 0.0F, false);
        this.right_fin = new AdvancedModelBox(this, "right_fin");
        this.right_fin.setRotationPoint(-4.0F, 2.0F, -3.0F);
        this.body.addChild(this.right_fin);
        this.setRotationAngle(this.right_fin, 0.0F, 0.0F, 0.9163F);
        this.right_fin.setTextureOffset(19, 21).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 4.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 21).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 5.0F, 10.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 0).addBox(0.0F, -4.0F, 2.0F, 0.0F, 1.0F, 2.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 0).addBox(0.0F, 2.0F, -1.0F, 0.0F, 2.0F, 6.0F, 0.0F, false);
        this.tail_fin = new AdvancedModelBox(this, "tail_fin");
        this.tail_fin.setRotationPoint(0.0F, 2.0F, 9.0F);
        this.tail.addChild(this.tail_fin);
        this.tail_fin.setTextureOffset(19, 27).addBox(0.0F, -7.0F, -2.0F, 0.0F, 9.0F, 10.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityCatfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.2F;
        float idleDegree = 0.25F;
        float swimSpeed = 0.55F;
        float swimDegree = 0.75F;
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.body, this.tail, this.tail_fin };
        this.chainSwing(tailBoxes, swimSpeed, swimDegree * 0.9F, -3.0, limbSwing, limbSwingAmount);
        this.flap(this.left_fin, swimSpeed, swimDegree, false, 4.0F, -0.6F, limbSwing, limbSwingAmount);
        this.flap(this.right_fin, swimSpeed, swimDegree, true, 4.0F, -0.6F, limbSwing, limbSwingAmount);
        this.walk(this.dorsal_fin, idleSpeed, idleDegree * 0.2F, true, 2.0F, 0.1F, ageInTicks, 1.0F);
        this.bob(this.body, idleSpeed, idleDegree, false, ageInTicks, 1.0F);
        this.flap(this.left_fin, idleSpeed, idleDegree, false, 4.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.right_fin, idleSpeed, idleDegree, true, 4.0F, -0.1F, ageInTicks, 1.0F);
        this.swing(this.left_barbel, idleSpeed, idleDegree, false, 2.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.right_barbel, idleSpeed, idleDegree, true, 2.0F, 0.1F, ageInTicks, 1.0F);
        this.chainSwing(tailBoxes, idleSpeed, idleDegree * 0.1F, -2.5, ageInTicks, 1.0F);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.dorsal_fin, this.tail, this.left_fin, this.right_fin, this.left_barbel, this.right_barbel, this.tail_fin);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}