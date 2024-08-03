package org.violetmoon.quark.content.mobs.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import org.violetmoon.quark.content.mobs.entity.Foxhound;

public class FoxhoundModel extends AgeableListModel<Foxhound> {

    public final ModelPart head;

    public final ModelPart rightFrontLeg;

    public final ModelPart leftFrontLeg;

    public final ModelPart rightBackLeg;

    public final ModelPart leftBackLeg;

    public final ModelPart body;

    public final ModelPart snout;

    public final ModelPart rightEar;

    public final ModelPart leftEar;

    public final ModelPart tail;

    public final ModelPart fluff;

    public FoxhoundModel(ModelPart root) {
        super(false, 5.0F, 2.5F);
        this.head = root.getChild("head");
        this.rightFrontLeg = root.getChild("rightFrontLeg");
        this.leftFrontLeg = root.getChild("leftFrontLeg");
        this.rightBackLeg = root.getChild("rightBackLeg");
        this.leftBackLeg = root.getChild("leftBackLeg");
        this.body = root.getChild("body");
        this.snout = this.head.getChild("snout");
        this.rightEar = this.head.getChild("rightEar");
        this.leftEar = this.head.getChild("leftEar");
        this.tail = this.body.getChild("tail");
        this.fluff = this.body.getChild("fluff");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        float zOff = 5.5F;
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -3.0F, -6.0F, 8.0F, 6.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 14.5F, -5.5F, 0.0F, 1.0F, 0.0F));
        head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(0, 47).addBox(-4.0F, -5.0F, -5.0F, 2.0F, 2.0F, 3.0F), PartPose.ZERO);
        head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(10, 47).addBox(2.0F, -5.0F, -5.0F, 2.0F, 2.0F, 3.0F), PartPose.ZERO);
        head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(29, 18).addBox(-2.0F, 1.0F, -10.0F, 4.0F, 2.0F, 4.0F), PartPose.ZERO);
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 2).addBox(-4.0F, -12.0F, 0.0F, 8.0F, 12.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 17.0F, 6.5F, (float) (Math.PI / 2), 0.0F, 0.0F));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(36, 16).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 5.0F, 10.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 1.5F, (float) (-Math.PI * 5.0 / 12.0), 0.0F, 0.0F));
        body.addOrReplaceChild("fluff", CubeListBuilder.create().texOffs(28, 0).addBox(-5.0F, 0.0F, -4.0F, 10.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, -13.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftBackLeg", CubeListBuilder.create().texOffs(36, 32).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offsetAndRotation(3.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightBackLeg", CubeListBuilder.create().texOffs(24, 32).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offsetAndRotation(-3.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offsetAndRotation(-2.0F, 12.0F, -3.5F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(12, 32).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offsetAndRotation(2.0F, 12.0F, -3.5F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public void prepareMobModel(Foxhound hound, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if (hound.getRemainingPersistentAngerTime() > 0) {
            this.tail.xRot = -0.65449846F;
        } else {
            this.tail.xRot = (float) (-Math.PI * 5.0 / 12.0) + Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        }
        this.head.yRot = hound.m_30448_(partialTickTime) - hound.m_30432_(partialTickTime, 0.0F);
        this.head.xRot = 0.0F;
        this.body.yRot = hound.m_30432_(partialTickTime, -0.16F);
        this.tail.yRot = hound.m_30432_(partialTickTime, -0.2F);
        if (hound.m_5803_()) {
            this.head.setPos(1.0F, 20.5F, -5.5F);
            this.setAngle(this.head, 0.0F, (float) (Math.PI / 4), -0.04363323F);
            this.body.setPos(0.0F, 20.0F, 6.5F);
            this.setAngle(this.body, (float) (Math.PI / 2), 0.0F, (float) (Math.PI / 2));
            this.tail.setPos(0.0F, -1.0F, 1.0F);
            this.setAngle(this.tail, 2.5497515F, -0.22759093F, 0.0F);
            this.rightFrontLeg.setPos(0.0F, 18.0F, -3.5F);
            this.leftFrontLeg.setPos(2.0F, 21.0F, -4.5F);
            this.rightBackLeg.setPos(0.0F, 22.0F, 5.5F);
            this.leftBackLeg.setPos(3.0F, 20.0F, 4.5F);
            this.setAngle(this.rightFrontLeg, 0.21816616F, 0.43633232F, (float) (Math.PI * 5.0 / 12.0));
            this.setAngle(this.leftFrontLeg, 0.0F, 0.0F, (float) (Math.PI * 4.0 / 9.0));
            this.setAngle(this.rightBackLeg, (float) (-Math.PI / 3), -0.08726646F, 1.4835298F);
            this.setAngle(this.leftBackLeg, (float) (-Math.PI / 4), 0.0F, 1.2217305F);
        } else if (hound.m_21825_()) {
            this.head.setPos(0.0F, 12.0F, -3.5F);
            this.body.setPos(0.0F, 23.0F, 1.5F);
            this.setAngle(this.body, (float) (Math.PI / 4), this.body.yRot, 0.0F);
            this.tail.setPos(0.0F, 0.0F, 2.0F);
            this.setAngle(this.tail, -1.1F, -0.0F, 0.0F);
            this.rightFrontLeg.setPos(-2.0F, 12.0F, -4.25F);
            this.leftFrontLeg.setPos(2.0F, 12.0F, -4.25F);
            this.rightBackLeg.setPos(-3.0F, 21.0F, 4.5F);
            this.leftBackLeg.setPos(3.0F, 21.0F, 4.5F);
            this.setAngle(this.rightFrontLeg, 0.0F, 0.0F, 0.0F);
            this.setAngle(this.leftFrontLeg, 0.0F, 0.0F, 0.0F);
            this.setAngle(this.rightBackLeg, (float) (-Math.PI * 5.0 / 12.0), (float) (Math.PI / 8), 0.0F);
            this.setAngle(this.leftBackLeg, (float) (-Math.PI * 5.0 / 12.0), (float) (-Math.PI / 8), 0.0F);
        } else {
            this.head.setPos(0.0F, 14.5F, -5.5F);
            this.body.setPos(0.0F, 17.0F, 6.5F);
            this.setAngle(this.body, (float) (Math.PI / 2), this.body.yRot, 0.0F);
            this.tail.setPos(0.0F, 0.0F, 1.5F);
            this.rightFrontLeg.setPos(-2.0F, 12.0F, -3.5F);
            this.leftFrontLeg.setPos(2.0F, 12.0F, -3.5F);
            this.rightBackLeg.setPos(-3.0F, 12.0F, 4.0F);
            this.leftBackLeg.setPos(3.0F, 12.0F, 4.0F);
            this.setAngle(this.rightFrontLeg, Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount, 0.0F, 0.0F);
            this.setAngle(this.leftFrontLeg, Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount, 0.0F, 0.0F);
            this.setAngle(this.rightBackLeg, Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount, 0.0F, 0.0F);
            this.setAngle(this.leftBackLeg, Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount, 0.0F, 0.0F);
        }
    }

    public void setupAnim(Foxhound entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.m_5803_()) {
            this.head.yRot += netHeadYaw * (float) (Math.PI / 180.0);
            this.head.xRot += headPitch * (float) (Math.PI / 180.0);
        } else {
            this.head.yRot = this.head.yRot + Mth.cos((float) entity.f_19797_ / 30.0F) / 20.0F;
        }
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightBackLeg, this.leftBackLeg, this.rightFrontLeg, this.leftFrontLeg);
    }

    public void setAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}