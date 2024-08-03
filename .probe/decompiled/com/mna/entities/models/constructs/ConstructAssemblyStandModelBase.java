package com.mna.entities.models.constructs;

import com.mna.entities.constructs.ConstructAssemblyStand;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ConstructAssemblyStandModelBase extends HumanoidModel<ConstructAssemblyStand> {

    public ConstructAssemblyStandModelBase(ModelPart modelPart0) {
        super(modelPart0);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation0) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(cubeDeformation0, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation0), PartPose.offset(0.0F, 1.0F, 0.0F));
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation0.extend(0.5F)), PartPose.offset(0.0F, 1.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0), PartPose.offset(-1.9F, 11.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0), PartPose.offset(1.9F, 11.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public void setupAnim(ConstructAssemblyStand constructAssemblyStand0, float float1, float float2, float float3, float float4, float float5) {
        this.f_102808_.xRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getHeadPose().getX();
        this.f_102808_.yRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getHeadPose().getY();
        this.f_102808_.zRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getHeadPose().getZ();
        this.f_102810_.xRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getBodyPose().getX();
        this.f_102810_.yRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getBodyPose().getY();
        this.f_102810_.zRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getBodyPose().getZ();
        this.f_102812_.xRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getLeftArmPose().getX();
        this.f_102812_.yRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getLeftArmPose().getY();
        this.f_102812_.zRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getLeftArmPose().getZ();
        this.f_102811_.xRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getRightArmPose().getX();
        this.f_102811_.yRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getRightArmPose().getY();
        this.f_102811_.zRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getRightArmPose().getZ();
        this.f_102814_.xRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getLeftLegPose().getX();
        this.f_102814_.yRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getLeftLegPose().getY();
        this.f_102814_.zRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getLeftLegPose().getZ();
        this.f_102813_.xRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getRightLegPose().getX();
        this.f_102813_.yRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getRightLegPose().getY();
        this.f_102813_.zRot = (float) (Math.PI / 180.0) * constructAssemblyStand0.getRightLegPose().getZ();
        this.f_102809_.copyFrom(this.f_102808_);
    }
}