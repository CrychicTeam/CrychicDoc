package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class QuadrupedModel<T extends Entity> extends AgeableListModel<T> {

    protected final ModelPart head;

    protected final ModelPart body;

    protected final ModelPart rightHindLeg;

    protected final ModelPart leftHindLeg;

    protected final ModelPart rightFrontLeg;

    protected final ModelPart leftFrontLeg;

    protected QuadrupedModel(ModelPart modelPart0, boolean boolean1, float float2, float float3, float float4, float float5, int int6) {
        super(boolean1, float2, float3, float4, float5, (float) int6);
        this.head = modelPart0.getChild("head");
        this.body = modelPart0.getChild("body");
        this.rightHindLeg = modelPart0.getChild("right_hind_leg");
        this.leftHindLeg = modelPart0.getChild("left_hind_leg");
        this.rightFrontLeg = modelPart0.getChild("right_front_leg");
        this.leftFrontLeg = modelPart0.getChild("left_front_leg");
    }

    public static MeshDefinition createBodyMesh(int int0, CubeDeformation cubeDeformation1) {
        MeshDefinition $$2 = new MeshDefinition();
        PartDefinition $$3 = $$2.getRoot();
        $$3.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, cubeDeformation1), PartPose.offset(0.0F, (float) (18 - int0), -6.0F));
        $$3.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 8).addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F, cubeDeformation1), PartPose.offsetAndRotation(0.0F, (float) (17 - int0), 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        CubeListBuilder $$4 = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, (float) int0, 4.0F, cubeDeformation1);
        $$3.addOrReplaceChild("right_hind_leg", $$4, PartPose.offset(-3.0F, (float) (24 - int0), 7.0F));
        $$3.addOrReplaceChild("left_hind_leg", $$4, PartPose.offset(3.0F, (float) (24 - int0), 7.0F));
        $$3.addOrReplaceChild("right_front_leg", $$4, PartPose.offset(-3.0F, (float) (24 - int0), -5.0F));
        $$3.addOrReplaceChild("left_front_leg", $$4, PartPose.offset(3.0F, (float) (24 - int0), -5.0F));
        return $$2;
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg);
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        this.rightHindLeg.xRot = Mth.cos(float1 * 0.6662F) * 1.4F * float2;
        this.leftHindLeg.xRot = Mth.cos(float1 * 0.6662F + (float) Math.PI) * 1.4F * float2;
        this.rightFrontLeg.xRot = Mth.cos(float1 * 0.6662F + (float) Math.PI) * 1.4F * float2;
        this.leftFrontLeg.xRot = Mth.cos(float1 * 0.6662F) * 1.4F * float2;
    }
}