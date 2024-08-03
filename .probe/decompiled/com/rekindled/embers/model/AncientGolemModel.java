package com.rekindled.embers.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class AncientGolemModel<T extends Entity> extends HierarchicalModel<T> {

    ModelPart root;

    ModelPart legL;

    ModelPart legR;

    ModelPart body1;

    ModelPart body2;

    ModelPart armR;

    ModelPart fistR;

    ModelPart armL;

    ModelPart fistL;

    ModelPart head;

    public AncientGolemModel(ModelPart root) {
        this.root = root;
        this.legL = root.getChild("legL");
        this.legR = root.getChild("legR");
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.armR = root.getChild("armR");
        this.fistR = root.getChild("fistR");
        this.armL = root.getChild("armL");
        this.fistL = root.getChild("fistL");
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("legL", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F), PartPose.offset(2.0F, 14.0F, 0.0F));
        partdefinition.addOrReplaceChild("legR", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F), PartPose.offset(-2.0F, 14.0F, 0.0F));
        partdefinition.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 10.0F, 0.0F));
        partdefinition.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(16, 0).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 8.0F, 6.0F), PartPose.offset(0.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("armR", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(-4.5F, 2.013333F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 8)));
        partdefinition.addOrReplaceChild("fistR", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 12.0F, -2.5F, 5.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(-4.5F, 2.013333F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 8)));
        partdefinition.addOrReplaceChild("armL", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(4.5F, 2.013333F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 8)));
        partdefinition.addOrReplaceChild("fistL", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, 12.0F, -2.5F, 5.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(4.5F, 2.013333F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 8)));
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 32).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 2.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * (float) (Math.PI / 180.0);
        this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        this.legR.xRot = (float) Math.toRadians((double) (180.0F * (float) Math.sin((double) limbSwing * 0.5) * limbSwingAmount) * 0.5);
        this.legL.xRot = -((float) Math.toRadians((double) (180.0F * (float) Math.sin((double) limbSwing * 0.5) * limbSwingAmount) * 0.5));
        this.armL.xRot = (float) Math.toRadians((double) (180.0F * (float) Math.sin((double) limbSwing * 0.5) * limbSwingAmount) * 0.5);
        this.armR.xRot = -((float) Math.toRadians((double) (180.0F * (float) Math.sin((double) limbSwing * 0.5) * limbSwingAmount) * 0.5));
        this.fistL.xRot = (float) Math.toRadians((double) (180.0F * (float) Math.sin((double) limbSwing * 0.5) * limbSwingAmount) * 0.5);
        this.fistR.xRot = -((float) Math.toRadians((double) (180.0F * (float) Math.sin((double) limbSwing * 0.5) * limbSwingAmount) * 0.5));
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}