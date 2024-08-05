package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.Sheep;

public class SheepFurModel<T extends Sheep> extends QuadrupedModel<T> {

    private float headXRot;

    public SheepFurModel(ModelPart modelPart0) {
        super(modelPart0, false, 8.0F, 4.0F, 2.0F, 2.0F, 24);
    }

    public static LayerDefinition createFurLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 6.0F, -8.0F));
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 8).addBox(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F, new CubeDeformation(1.75F)), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        CubeListBuilder $$2 = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F));
        $$1.addOrReplaceChild("right_hind_leg", $$2, PartPose.offset(-3.0F, 12.0F, 7.0F));
        $$1.addOrReplaceChild("left_hind_leg", $$2, PartPose.offset(3.0F, 12.0F, 7.0F));
        $$1.addOrReplaceChild("right_front_leg", $$2, PartPose.offset(-3.0F, 12.0F, -5.0F));
        $$1.addOrReplaceChild("left_front_leg", $$2, PartPose.offset(3.0F, 12.0F, -5.0F));
        return LayerDefinition.create($$0, 64, 32);
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        super.m_6839_(t0, float1, float2, float3);
        this.f_103492_.y = 6.0F + t0.getHeadEatPositionScale(float3) * 9.0F;
        this.headXRot = t0.getHeadEatAngleScale(float3);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        this.f_103492_.xRot = this.headXRot;
    }
}