package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class HumanoidArmorModel<T extends LivingEntity> extends HumanoidModel<T> {

    public HumanoidArmorModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    public static MeshDefinition createBodyLayer(CubeDeformation cubeDeformation0) {
        MeshDefinition $$1 = HumanoidModel.createMesh(cubeDeformation0, 0.0F);
        PartDefinition $$2 = $$1.getRoot();
        $$2.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0.extend(-0.1F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        $$2.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0.extend(-0.1F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        return $$1;
    }
}