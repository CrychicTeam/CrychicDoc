package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCatfish;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelCatfishMedium extends AdvancedEntityModel<EntityCatfish> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox dorsal_fin;

    private final AdvancedModelBox left_fin;

    private final AdvancedModelBox right_fin;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail_fin;

    private final AdvancedModelBox head;

    private final AdvancedModelBox left_BigWhisker;

    private final AdvancedModelBox right_BigWhisker;

    private final AdvancedModelBox left_SmallWhisker;

    private final AdvancedModelBox right_SmallWhisker;

    public ModelCatfishMedium() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-7.0F, -6.0F, -10.0F, 14.0F, 11.0F, 18.0F, 0.0F, false);
        this.dorsal_fin = new AdvancedModelBox(this, "dorsal_fin");
        this.dorsal_fin.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.body.addChild(this.dorsal_fin);
        this.dorsal_fin.setTextureOffset(0, 0).addBox(0.0F, -4.0F, -1.0F, 0.0F, 4.0F, 7.0F, 0.0F, false);
        this.left_fin = new AdvancedModelBox(this, "left_fin");
        this.left_fin.setRotationPoint(7.0F, 3.0F, -2.0F);
        this.body.addChild(this.left_fin);
        this.setRotationAngle(this.left_fin, 0.0F, 0.0F, -0.7854F);
        this.left_fin.setTextureOffset(0, 30).addBox(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 5.0F, 0.0F, false);
        this.right_fin = new AdvancedModelBox(this, "right_fin");
        this.right_fin.setRotationPoint(-7.0F, 3.0F, -2.0F);
        this.body.addChild(this.right_fin);
        this.setRotationAngle(this.right_fin, 0.0F, 0.0F, 0.7854F);
        this.right_fin.setTextureOffset(0, 30).addBox(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 5.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, -2.0F, 9.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 30).addBox(-5.0F, -4.0F, -1.0F, 10.0F, 8.0F, 19.0F, 0.0F, false);
        this.tail.setTextureOffset(53, 18).addBox(0.0F, 4.0F, 1.0F, 0.0F, 4.0F, 12.0F, 0.0F, false);
        this.tail.setTextureOffset(8, 0).addBox(0.0F, -6.0F, 4.0F, 0.0F, 2.0F, 4.0F, 0.0F, false);
        this.tail_fin = new AdvancedModelBox(this, "tail_fin");
        this.tail_fin.setRotationPoint(0.0F, -4.0F, 17.0F);
        this.tail.addChild(this.tail_fin);
        this.tail_fin.setTextureOffset(38, 37).addBox(0.0F, -3.0F, -6.0F, 0.0F, 13.0F, 21.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -1.0F, -11.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(47, 0).addBox(-6.0F, -5.0F, -5.0F, 12.0F, 9.0F, 6.0F, 0.0F, false);
        this.left_BigWhisker = new AdvancedModelBox(this, "left_BigWhisker");
        this.left_BigWhisker.setRotationPoint(6.0F, 1.0F, -2.0F);
        this.head.addChild(this.left_BigWhisker);
        this.setRotationAngle(this.left_BigWhisker, 0.0F, -0.5236F, 0.0F);
        this.left_BigWhisker.setTextureOffset(0, 12).addBox(0.0F, 0.0F, 0.0F, 8.0F, 5.0F, 0.0F, 0.0F, false);
        this.right_BigWhisker = new AdvancedModelBox(this, "right_BigWhisker");
        this.right_BigWhisker.setRotationPoint(-6.0F, 1.0F, -2.0F);
        this.head.addChild(this.right_BigWhisker);
        this.setRotationAngle(this.right_BigWhisker, 0.0F, 0.5236F, 0.0F);
        this.right_BigWhisker.setTextureOffset(0, 12).addBox(-8.0F, 0.0F, 0.0F, 8.0F, 5.0F, 0.0F, 0.0F, true);
        this.left_SmallWhisker = new AdvancedModelBox(this, "left_SmallWhisker");
        this.left_SmallWhisker.setRotationPoint(6.0F, 3.0F, -2.0F);
        this.head.addChild(this.left_SmallWhisker);
        this.setRotationAngle(this.left_SmallWhisker, 0.0F, 0.0F, 0.4363F);
        this.left_SmallWhisker.setTextureOffset(6, 30).addBox(0.0F, 0.0F, 0.0F, 5.0F, 3.0F, 0.0F, 0.0F, false);
        this.right_SmallWhisker = new AdvancedModelBox(this, "right_SmallWhisker");
        this.right_SmallWhisker.setRotationPoint(-6.0F, 3.0F, -2.0F);
        this.head.addChild(this.right_SmallWhisker);
        this.setRotationAngle(this.right_SmallWhisker, 0.0F, 0.0F, -0.4363F);
        this.right_SmallWhisker.setTextureOffset(6, 30).addBox(-5.0F, 0.0F, 0.0F, 5.0F, 3.0F, 0.0F, 0.0F, true);
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