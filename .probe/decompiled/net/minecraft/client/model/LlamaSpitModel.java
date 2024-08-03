package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class LlamaSpitModel<T extends Entity> extends HierarchicalModel<T> {

    private static final String MAIN = "main";

    private final ModelPart root;

    public LlamaSpitModel(ModelPart modelPart0) {
        this.root = modelPart0;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        int $$2 = 2;
        $$1.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F).addBox(0.0F, -4.0F, 0.0F, 2.0F, 2.0F, 2.0F).addBox(0.0F, 0.0F, -4.0F, 2.0F, 2.0F, 2.0F).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F).addBox(2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F).addBox(0.0F, 2.0F, 0.0F, 2.0F, 2.0F, 2.0F).addBox(0.0F, 0.0F, 2.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 32);
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}