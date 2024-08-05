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
public class ModelRoadrunnerBoots extends HumanoidModel {

    public ModelRoadrunnerBoots(ModelPart modelPart0) {
        super(modelPart0);
    }

    public static LayerDefinition createArmorLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 1.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition leftleg = partdefinition.getChild("left_leg");
        PartDefinition rightleg = partdefinition.getChild("right_leg");
        rightleg.addOrReplaceChild("featherr", CubeListBuilder.create().texOffs(20, 22).addBox(-3.0F, -7.5F, 0.0F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 9.5F, 0.4F, 0.0F, 0.9773844F, -0.312763F));
        leftleg.addOrReplaceChild("featherl", CubeListBuilder.create().texOffs(20, 22).mirror().addBox(0.0F, -7.4F, 0.0F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 9.5F, -0.4F, 0.0F, -0.9773844F, 0.312763F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}