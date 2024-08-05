package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCosmicCod;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class ModelCosmicCod extends AdvancedEntityModel<EntityCosmicCod> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox left_fin;

    private final AdvancedModelBox right_fin;

    private final AdvancedModelBox mouth;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail_tip;

    public ModelCosmicCod() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -2.0F, -2.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 3.0F, 8.0F, 0.0F, false);
        this.body.setTextureOffset(15, 15).addBox(0.0F, -3.0F, -2.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);
        this.left_fin = new AdvancedModelBox(this, "left_fin");
        this.left_fin.setRotationPoint(1.0F, 2.0F, 0.0F);
        this.body.addChild(this.left_fin);
        this.setRotationAngle(this.left_fin, 0.0F, 0.0F, -0.829F);
        this.left_fin.setTextureOffset(0, 12).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        this.right_fin = new AdvancedModelBox(this, "right_fin");
        this.right_fin.setRotationPoint(-1.0F, 2.0F, 0.0F);
        this.body.addChild(this.right_fin);
        this.setRotationAngle(this.right_fin, 0.0F, 0.0F, 0.829F);
        this.right_fin.setTextureOffset(0, 12).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, true);
        this.mouth = new AdvancedModelBox(this, "mouth");
        this.mouth.setRotationPoint(0.0F, 1.0F, -5.0F);
        this.body.addChild(this.mouth);
        this.mouth.setTextureOffset(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(15, 6).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);
        this.tail_tip = new AdvancedModelBox(this, "tail_tip");
        this.tail_tip.setRotationPoint(0.0F, 0.0F, 6.0F);
        this.tail.addChild(this.tail_tip);
        this.tail_tip.setTextureOffset(0, 12).addBox(0.0F, -3.0F, -1.0F, 0.0F, 5.0F, 7.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setupAnim(EntityCosmicCod entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.14F;
        float idleDegree = 0.25F;
        float swimSpeed = 0.8F;
        float swimDegree = 0.5F;
        float pitch = Maths.rad((double) Mth.rotLerp(ageInTicks - (float) entity.f_19797_, entity.prevFishPitch, entity.getFishPitch()));
        this.swing(this.tail, idleSpeed, idleDegree, true, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.left_fin, idleSpeed, idleDegree, true, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.right_fin, idleSpeed, idleDegree, false, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.bob(this.body, idleSpeed, idleDegree * 4.0F, false, ageInTicks, 1.0F);
        this.swing(this.tail, swimSpeed, swimDegree, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail_tip, swimSpeed, swimDegree * 0.5F, true, 2.5F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.left_fin, swimSpeed, swimDegree, true, 1.0F, 0.3F, limbSwing, limbSwingAmount);
        this.flap(this.right_fin, swimSpeed, swimDegree, false, 1.0F, 0.3F, limbSwing, limbSwingAmount);
        this.root.rotateAngleX += pitch;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.right_fin, this.left_fin, this.mouth, this.tail, this.tail_tip);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}