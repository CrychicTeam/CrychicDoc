package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class TropicalFishModelB<T extends Entity> extends ColorableHierarchicalModel<T> {

    private final ModelPart root;

    private final ModelPart tail;

    public TropicalFishModelB(ModelPart modelPart0) {
        this.root = modelPart0;
        this.tail = modelPart0.getChild("tail");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation0) {
        MeshDefinition $$1 = new MeshDefinition();
        PartDefinition $$2 = $$1.getRoot();
        int $$3 = 19;
        $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F, cubeDeformation0), PartPose.offset(0.0F, 19.0F, 0.0F));
        $$2.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(21, 16).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 5.0F, cubeDeformation0), PartPose.offset(0.0F, 19.0F, 3.0F));
        $$2.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(2, 16).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, cubeDeformation0), PartPose.offsetAndRotation(-1.0F, 20.0F, 0.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        $$2.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(2, 12).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, cubeDeformation0), PartPose.offsetAndRotation(1.0F, 20.0F, 0.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        $$2.addOrReplaceChild("top_fin", CubeListBuilder.create().texOffs(20, 11).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 6.0F, cubeDeformation0), PartPose.offset(0.0F, 16.0F, -3.0F));
        $$2.addOrReplaceChild("bottom_fin", CubeListBuilder.create().texOffs(20, 21).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 6.0F, cubeDeformation0), PartPose.offset(0.0F, 22.0F, -3.0F));
        return LayerDefinition.create($$1, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        float $$6 = 1.0F;
        if (!t0.isInWater()) {
            $$6 = 1.5F;
        }
        this.tail.yRot = -$$6 * 0.45F * Mth.sin(0.6F * float3);
    }
}