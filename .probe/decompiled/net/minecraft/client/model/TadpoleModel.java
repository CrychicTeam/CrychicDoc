package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.frog.Tadpole;

public class TadpoleModel<T extends Tadpole> extends AgeableListModel<T> {

    private final ModelPart root;

    private final ModelPart tail;

    public TadpoleModel(ModelPart modelPart0) {
        super(true, 8.0F, 3.35F);
        this.root = modelPart0;
        this.tail = modelPart0.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        float $$2 = 0.0F;
        float $$3 = 22.0F;
        float $$4 = -3.0F;
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 3.0F), PartPose.offset(0.0F, 22.0F, -3.0F));
        $$1.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 7.0F), PartPose.offset(0.0F, 22.0F, 0.0F));
        return LayerDefinition.create($$0, 16, 16);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.root);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.tail);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        float $$6 = t0.m_20069_() ? 1.0F : 1.5F;
        this.tail.yRot = -$$6 * 0.25F * Mth.sin(0.3F * float3);
    }
}