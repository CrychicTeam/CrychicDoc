package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCatfish;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelCatfishLarge extends AdvancedEntityModel<EntityCatfish> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox left_fin;

    private final AdvancedModelBox right_fin;

    private final AdvancedModelBox dorsal_fin;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail_fin;

    private final AdvancedModelBox head;

    private final AdvancedModelBox left_BigWhisker;

    private final AdvancedModelBox right_BigWhisker;

    private final AdvancedModelBox left_SmallWhisker;

    private final AdvancedModelBox right_SmallWhisker;

    public ModelCatfishLarge() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-10.0F, -7.0F, -14.0F, 20.0F, 15.0F, 26.0F, 0.0F, false);
        this.left_fin = new AdvancedModelBox(this, "left_fin");
        this.left_fin.setRotationPoint(10.0F, 5.0F, -5.0F);
        this.body.addChild(this.left_fin);
        this.setRotationAngle(this.left_fin, 0.0F, 0.0F, 0.6981F);
        this.left_fin.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -2.0F, 5.0F, 0.0F, 8.0F, 0.0F, false);
        this.right_fin = new AdvancedModelBox(this, "right_fin");
        this.right_fin.setRotationPoint(-10.0F, 5.0F, -5.0F);
        this.body.addChild(this.right_fin);
        this.setRotationAngle(this.right_fin, 0.0F, 0.0F, -0.6981F);
        this.right_fin.setTextureOffset(0, 0).addBox(-5.0F, 0.0F, -2.0F, 5.0F, 0.0F, 8.0F, 0.0F, true);
        this.dorsal_fin = new AdvancedModelBox(this, "dorsal_fin");
        this.dorsal_fin.setRotationPoint(0.0F, -7.0F, -5.0F);
        this.body.addChild(this.dorsal_fin);
        this.setRotationAngle(this.dorsal_fin, -0.0873F, 0.0F, 0.0F);
        this.dorsal_fin.setTextureOffset(0, 0).addBox(0.0F, -7.0F, 0.0F, 0.0F, 7.0F, 8.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -2.0F, 13.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 41).addBox(-6.0F, -5.0F, -1.0F, 12.0F, 11.0F, 20.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 56).addBox(0.0F, 6.0F, -1.0F, 0.0F, 4.0F, 16.0F, 0.0F, false);
        this.tail.setTextureOffset(16, 18).addBox(0.0F, -7.0F, 4.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);
        this.tail_fin = new AdvancedModelBox(this, "tail_fin");
        this.tail_fin.setRotationPoint(0.0F, 0.0F, 17.0F);
        this.tail.addChild(this.tail_fin);
        this.tail_fin.setTextureOffset(44, 18).addBox(0.0F, -10.0F, -5.0F, 0.0F, 17.0F, 23.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -5.0F, -15.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(56, 64).addBox(-9.5F, -2.0F, -7.0F, 19.0F, 13.0F, 8.0F, 0.0F, false);
        this.left_BigWhisker = new AdvancedModelBox(this, "left_BigWhisker");
        this.left_BigWhisker.setRotationPoint(9.5F, 6.0F, -5.0F);
        this.head.addChild(this.left_BigWhisker);
        this.setRotationAngle(this.left_BigWhisker, 0.0F, -0.4363F, 0.2618F);
        this.left_BigWhisker.setTextureOffset(66, 0).addBox(0.0F, 0.0F, 0.0F, 15.0F, 8.0F, 0.0F, 0.0F, false);
        this.right_BigWhisker = new AdvancedModelBox(this, "right_BigWhisker");
        this.right_BigWhisker.setRotationPoint(-9.5F, 6.0F, -5.0F);
        this.head.addChild(this.right_BigWhisker);
        this.setRotationAngle(this.right_BigWhisker, 0.0F, 0.4363F, -0.2618F);
        this.right_BigWhisker.setTextureOffset(66, 0).addBox(-15.0F, 0.0F, 0.0F, 15.0F, 8.0F, 0.0F, 0.0F, true);
        this.left_SmallWhisker = new AdvancedModelBox(this, "left_SmallWhisker");
        this.left_SmallWhisker.setRotationPoint(9.5F, 8.0F, -6.0F);
        this.head.addChild(this.left_SmallWhisker);
        this.setRotationAngle(this.left_SmallWhisker, 0.0F, 0.0436F, 0.3054F);
        this.left_SmallWhisker.setTextureOffset(0, 15).addBox(0.0F, 0.0F, 0.0F, 7.0F, 6.0F, 0.0F, 0.0F, false);
        this.right_SmallWhisker = new AdvancedModelBox(this, "right_SmallWhisker");
        this.right_SmallWhisker.setRotationPoint(-9.5F, 8.0F, -6.0F);
        this.head.addChild(this.right_SmallWhisker);
        this.setRotationAngle(this.right_SmallWhisker, 0.0F, -0.0436F, -0.3054F);
        this.right_SmallWhisker.setTextureOffset(0, 15).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 6.0F, 0.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityCatfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.2F;
        float idleDegree = 0.25F;
        float swimSpeed = 0.55F;
        float swimDegree = 0.75F;
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.body, this.tail, this.tail_fin };
        this.chainSwing(tailBoxes, swimSpeed, swimDegree * 0.9F, -2.5, limbSwing, limbSwingAmount);
        this.swing(this.head, swimSpeed, swimDegree * 0.2F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.left_fin, swimSpeed, swimDegree, false, 4.0F, -0.6F, limbSwing, limbSwingAmount);
        this.flap(this.right_fin, swimSpeed, swimDegree, true, 4.0F, -0.6F, limbSwing, limbSwingAmount);
        this.bob(this.body, idleSpeed, idleDegree, false, ageInTicks, 1.0F);
        this.flap(this.left_fin, idleSpeed, idleDegree, false, 4.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.right_fin, idleSpeed, idleDegree, true, 4.0F, -0.1F, ageInTicks, 1.0F);
        this.swing(this.left_BigWhisker, idleSpeed, idleDegree, false, 2.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.right_BigWhisker, idleSpeed, idleDegree, true, 2.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.left_SmallWhisker, idleSpeed, idleDegree, false, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.right_SmallWhisker, idleSpeed, idleDegree, true, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.chainSwing(tailBoxes, idleSpeed, idleDegree * 0.1F, -2.5, ageInTicks, 1.0F);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.head, this.body, this.dorsal_fin, this.tail, this.left_fin, this.right_fin, this.left_BigWhisker, this.right_BigWhisker, this.left_SmallWhisker, this.right_SmallWhisker, this.tail_fin, new AdvancedModelBox[0]);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}