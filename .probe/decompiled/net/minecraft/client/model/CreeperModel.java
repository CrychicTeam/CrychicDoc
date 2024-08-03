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

public class CreeperModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;

    private final ModelPart head;

    private final ModelPart rightHindLeg;

    private final ModelPart leftHindLeg;

    private final ModelPart rightFrontLeg;

    private final ModelPart leftFrontLeg;

    private static final int Y_OFFSET = 6;

    public CreeperModel(ModelPart modelPart0) {
        this.root = modelPart0;
        this.head = modelPart0.getChild("head");
        this.leftHindLeg = modelPart0.getChild("right_hind_leg");
        this.rightHindLeg = modelPart0.getChild("left_hind_leg");
        this.leftFrontLeg = modelPart0.getChild("right_front_leg");
        this.rightFrontLeg = modelPart0.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation0) {
        MeshDefinition $$1 = new MeshDefinition();
        PartDefinition $$2 = $$1.getRoot();
        $$2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation0), PartPose.offset(0.0F, 6.0F, 0.0F));
        $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, cubeDeformation0), PartPose.offset(0.0F, 6.0F, 0.0F));
        CubeListBuilder $$3 = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, cubeDeformation0);
        $$2.addOrReplaceChild("right_hind_leg", $$3, PartPose.offset(-2.0F, 18.0F, 4.0F));
        $$2.addOrReplaceChild("left_hind_leg", $$3, PartPose.offset(2.0F, 18.0F, 4.0F));
        $$2.addOrReplaceChild("right_front_leg", $$3, PartPose.offset(-2.0F, 18.0F, -4.0F));
        $$2.addOrReplaceChild("left_front_leg", $$3, PartPose.offset(2.0F, 18.0F, -4.0F));
        return LayerDefinition.create($$1, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
        this.rightHindLeg.xRot = Mth.cos(float1 * 0.6662F) * 1.4F * float2;
        this.leftHindLeg.xRot = Mth.cos(float1 * 0.6662F + (float) Math.PI) * 1.4F * float2;
        this.rightFrontLeg.xRot = Mth.cos(float1 * 0.6662F + (float) Math.PI) * 1.4F * float2;
        this.leftFrontLeg.xRot = Mth.cos(float1 * 0.6662F) * 1.4F * float2;
    }
}