package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class LeashKnotModel<T extends Entity> extends HierarchicalModel<T> {

    private static final String KNOT = "knot";

    private final ModelPart root;

    private final ModelPart knot;

    public LeashKnotModel(ModelPart modelPart0) {
        this.root = modelPart0;
        this.knot = modelPart0.getChild("knot");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("knot", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 8.0F, 6.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.knot.yRot = float4 * (float) (Math.PI / 180.0);
        this.knot.xRot = float5 * (float) (Math.PI / 180.0);
    }
}