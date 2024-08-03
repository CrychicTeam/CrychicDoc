package com.github.alexthe666.alexsmobs.client.model.layered;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModelFedora extends HumanoidModel {

    public ModelFedora(ModelPart part) {
        super(part);
    }

    public static LayerDefinition createArmorLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.getChild("head");
        head.addOrReplaceChild("fedora", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -3.55F, -3.0F, 6.0F, 4.0F, 6.0F, deformation), PartPose.offset(0.0F, -8.0F, 0.0F));
        head.addOrReplaceChild("fedora_shade", CubeListBuilder.create().texOffs(0, 32).addBox(-5.0F, -0.5F, -5.0F, 10.0F, 1.0F, 10.0F, deformation), PartPose.offset(0.0F, -8.05F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}