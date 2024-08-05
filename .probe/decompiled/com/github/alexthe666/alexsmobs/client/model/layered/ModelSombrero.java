package com.github.alexthe666.alexsmobs.client.model.layered;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelSombrero extends HumanoidModel {

    public ModelPart sombrero;

    public ModelSombrero(ModelPart modelPart0) {
        super(modelPart0);
    }

    public static LayerDefinition createArmorLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.getChild("head");
        head.addOrReplaceChild("sombrero", CubeListBuilder.create().texOffs(0, 64).addBox(-4.0F, -11.0F, -4.0F, 8.0F, 6.0F, 8.0F, deformation), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("sombrero2", CubeListBuilder.create().texOffs(22, 73).addBox(-11.0F, -8.0F, -11.0F, 22.0F, 3.0F, 22.0F, deformation), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public static LayerDefinition createArmorLayerAprilFools(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.getChild("head");
        head.addOrReplaceChild("sombrero", CubeListBuilder.create().texOffs(0, 64).addBox(-4.0F, 7.0F, -4.0F, 8.0F, 6.0F, 8.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) Math.PI, 0.0F, (float) (Math.PI / 10)));
        head.addOrReplaceChild("sombrero2", CubeListBuilder.create().texOffs(22, 73).addBox(-11.0F, 10.0F, -11.0F, 22.0F, 3.0F, 22.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) Math.PI, 0.0F, (float) (Math.PI / 10)));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}