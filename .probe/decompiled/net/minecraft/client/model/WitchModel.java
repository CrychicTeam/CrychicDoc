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

public class WitchModel<T extends Entity> extends VillagerModel<T> {

    private boolean holdingItem;

    public WitchModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = VillagerModel.createBodyModel();
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition $$2 = $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO);
        PartDefinition $$3 = $$2.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 64).addBox(0.0F, 0.0F, 0.0F, 10.0F, 2.0F, 10.0F), PartPose.offset(-5.0F, -10.03125F, -5.0F));
        PartDefinition $$4 = $$3.addOrReplaceChild("hat2", CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 7.0F, 4.0F, 7.0F), PartPose.offsetAndRotation(1.75F, -4.0F, 2.0F, -0.05235988F, 0.0F, 0.02617994F));
        PartDefinition $$5 = $$4.addOrReplaceChild("hat3", CubeListBuilder.create().texOffs(0, 87).addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F), PartPose.offsetAndRotation(1.75F, -4.0F, 2.0F, -0.10471976F, 0.0F, 0.05235988F));
        $$5.addOrReplaceChild("hat4", CubeListBuilder.create().texOffs(0, 95).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(1.75F, -2.0F, 2.0F, (float) (-Math.PI / 15), 0.0F, 0.10471976F));
        PartDefinition $$6 = $$2.getChild("nose");
        $$6.addOrReplaceChild("mole", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 3.0F, -6.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, -2.0F, 0.0F));
        return LayerDefinition.create($$0, 64, 128);
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        this.f_104044_.setPos(0.0F, -2.0F, 0.0F);
        float $$6 = 0.01F * (float) (t0.getId() % 10);
        this.f_104044_.xRot = Mth.sin((float) t0.tickCount * $$6) * 4.5F * (float) (Math.PI / 180.0);
        this.f_104044_.yRot = 0.0F;
        this.f_104044_.zRot = Mth.cos((float) t0.tickCount * $$6) * 2.5F * (float) (Math.PI / 180.0);
        if (this.holdingItem) {
            this.f_104044_.setPos(0.0F, 1.0F, -1.5F);
            this.f_104044_.xRot = -0.9F;
        }
    }

    public ModelPart getNose() {
        return this.f_104044_;
    }

    public void setHoldingItem(boolean boolean0) {
        this.holdingItem = boolean0;
    }
}