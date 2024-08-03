package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityMurmur;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelMurmurBody extends AdvancedEntityModel<EntityMurmur> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox arms;

    public ModelMurmurBody() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-7.0F, -13.0F, -5.0F, 14.0F, 14.0F, 10.0F, 0.0F, false);
        this.body.setTextureOffset(72, 20).addBox(-7.0F, 1.0F, -5.0F, 14.0F, 13.0F, 10.0F, 0.0F, false);
        this.arms = new AdvancedModelBox(this, "arms");
        this.arms.setRotationPoint(0.0F, -8.5F, -1.0F);
        this.body.addChild(this.arms);
        this.arms.rotateAngleX = 0.4363F;
        this.arms.setTextureOffset(0, 25).addBox(-9.0F, -2.5F, -8.0F, 18.0F, 5.0F, 10.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.arms);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setupAnim(EntityMurmur entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 0.9F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        this.body.rotationPointY = (float) ((double) this.body.rotationPointY - Math.abs(Math.sin((double) (0.9F * limbSwing)) * (double) limbSwingAmount * 4.0));
        this.walk(this.arms, walkSpeed * 2.0F, walkDegree * 0.3F, false, -1.0F, 0.15F, limbSwing, limbSwingAmount);
        this.swing(this.arms, walkSpeed, walkDegree * 0.3F, false, -3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.progressRotationPrev(this.body, limbSwingAmount, Maths.rad(15.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.body, limbSwingAmount, 0.0F, 2.0F, 4.0F, 1.0F);
        this.walk(this.arms, idleSpeed, idleDegree, false, -1.0F, 0.15F, ageInTicks, 1.0F);
    }
}