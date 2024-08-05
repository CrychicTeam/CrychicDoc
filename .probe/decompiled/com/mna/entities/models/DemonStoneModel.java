package com.mna.entities.models;

import com.mna.api.tools.RLoc;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class DemonStoneModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(RLoc.create("demonstonemodel"), "main");

    private final ModelPart base;

    public DemonStoneModel(ModelPart root) {
        this.base = root.getChild("base");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -16.0F, -6.0F, 12.0F, 16.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition first_pivot = base.addOrReplaceChild("first_pivot", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -12.1216F, -5.8895F, 10.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 1.0F, -0.2182F, 0.0F, 0.0F));
        PartDefinition second_pivot = first_pivot.addOrReplaceChild("second_pivot", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -13.1158F, -7.6818F, 8.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.6761F, 2.1737F, -0.48F, 0.0F, 0.0F));
        second_pivot.addOrReplaceChild("third_pivot", CubeListBuilder.create().texOffs(5, 8).addBox(-2.0F, -11.1158F, -7.6818F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.8618F, 0.8628F, -0.48F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.base.render(poseStack, buffer, packedLight, packedOverlay);
    }
}