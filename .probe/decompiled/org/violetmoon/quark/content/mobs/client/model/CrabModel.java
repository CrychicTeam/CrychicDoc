package org.violetmoon.quark.content.mobs.client.model;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Set;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.mobs.entity.Crab;

public class CrabModel extends EntityModel<Crab> {

    private float wiggleX = 0.0F;

    private float wiggleY = 0.0F;

    private float crabSize = 0.0F;

    public ModelPart group;

    public ModelPart body;

    public ModelPart rightClaw;

    public ModelPart leftClaw;

    public ModelPart rightLeg1;

    public ModelPart rightLeg2;

    public ModelPart rightLeg3;

    public ModelPart rightLeg4;

    public ModelPart leftLeg1;

    public ModelPart leftLeg2;

    public ModelPart leftLeg3;

    public ModelPart leftLeg4;

    public ModelPart rightEye;

    public ModelPart leftEye;

    private final Set<ModelPart> leftLegs;

    private final Set<ModelPart> rightLegs;

    public CrabModel(ModelPart root) {
        this.group = root.getChild("group");
        this.body = this.group.getChild("body");
        this.rightClaw = this.group.getChild("rightClaw");
        this.leftClaw = this.group.getChild("leftClaw");
        this.rightLeg1 = this.group.getChild("rightLeg1");
        this.rightLeg2 = this.group.getChild("rightLeg2");
        this.rightLeg3 = this.group.getChild("rightLeg3");
        this.rightLeg4 = this.group.getChild("rightLeg4");
        this.leftLeg1 = this.group.getChild("leftLeg1");
        this.leftLeg2 = this.group.getChild("leftLeg2");
        this.leftLeg3 = this.group.getChild("leftLeg3");
        this.leftLeg4 = this.group.getChild("leftLeg4");
        this.rightEye = this.body.getChild("rightEye");
        this.leftEye = this.body.getChild("leftEye");
        this.leftLegs = ImmutableSet.of(this.leftLeg1, this.leftLeg2, this.leftLeg3, this.leftLeg4);
        this.rightLegs = ImmutableSet.of(this.rightLeg1, this.rightLeg2, this.rightLeg3, this.rightLeg4);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition group = root.addOrReplaceChild("group", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition body = group.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.5F, -3.0F, 8.0F, 5.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        group.addOrReplaceChild("leftLeg4", CubeListBuilder.create().mirror().texOffs(0, 19).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(3.0F, 20.0F, -1.0F, 0.0F, 0.43633232F, (float) (Math.PI / 4)));
        group.addOrReplaceChild("leftLeg3", CubeListBuilder.create().mirror().texOffs(0, 19).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(3.0F, 20.0F, 0.0F, 0.0F, 0.21816616F, (float) (Math.PI / 4)));
        body.addOrReplaceChild("rightEye", CubeListBuilder.create().texOffs(0, 11).addBox(-3.0F, -3.5F, -2.85F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (-Math.PI / 8), 0.0F, 0.0F));
        group.addOrReplaceChild("rightLeg4", CubeListBuilder.create().texOffs(0, 19).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-3.0F, 20.0F, -1.0F, 0.0F, -0.43633232F, (float) (-Math.PI / 4)));
        group.addOrReplaceChild("rightClaw", CubeListBuilder.create().texOffs(14, 11).addBox(-3.0F, -2.5F, -6.0F, 3.0F, 5.0F, 6.0F), PartPose.offsetAndRotation(-3.0F, 20.0F, -4.0F, 0.0F, (float) (Math.PI / 8), (float) (-Math.PI / 8)));
        group.addOrReplaceChild("leftLeg1", CubeListBuilder.create().mirror().texOffs(0, 19).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(3.0F, 20.0F, 2.0F, 0.0F, -0.43633232F, (float) (Math.PI / 4)));
        group.addOrReplaceChild("rightLeg2", CubeListBuilder.create().mirror().texOffs(0, 19).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-3.0F, 20.0F, 0.9F, 0.0F, 0.21816616F, (float) (-Math.PI / 4)));
        group.addOrReplaceChild("leftClaw", CubeListBuilder.create().mirror().texOffs(14, 11).addBox(0.0F, -2.5F, -6.0F, 3.0F, 5.0F, 6.0F), PartPose.offsetAndRotation(3.0F, 20.0F, -4.0F, 0.0F, (float) (-Math.PI / 8), (float) (Math.PI / 8)));
        group.addOrReplaceChild("rightLeg1", CubeListBuilder.create().texOffs(0, 19).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-3.0F, 20.0F, 2.0F, 0.0F, 0.43633232F, (float) (-Math.PI / 4)));
        body.addOrReplaceChild("leftEye", CubeListBuilder.create().texOffs(0, 11).addBox(2.0F, -3.5F, -2.85F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float) (-Math.PI / 8), 0.0F, 0.0F));
        group.addOrReplaceChild("leftLeg2", CubeListBuilder.create().mirror().texOffs(0, 19).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(3.0F, 20.0F, 0.9F, 0.0F, -0.21816616F, (float) (Math.PI / 4)));
        group.addOrReplaceChild("rightLeg3", CubeListBuilder.create().texOffs(0, 19).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-3.0F, 20.0F, 0.0F, 0.0F, -0.21816616F, (float) (-Math.PI / 4)));
        return LayerDefinition.create(mesh, 32, 32);
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public void setupAnim(Crab crab, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.rightLeg1.zRot = -0.2618F + (-1.0F + Mth.cos(limbSwing * 0.6662F)) * 0.7F * limbSwingAmount;
        this.rightLeg2.zRot = -0.5236F + (-1.0F + Mth.cos(limbSwing * 0.6662F + (float) Math.PI)) * 0.7F * limbSwingAmount;
        this.rightLeg3.zRot = -0.5236F + (-1.0F + Mth.cos(limbSwing * 0.6662F)) * 0.7F * limbSwingAmount;
        this.rightLeg4.zRot = -0.2618F + (-1.0F + Mth.cos(limbSwing * 0.6662F + (float) Math.PI)) * 0.7F * limbSwingAmount;
        this.leftLeg1.zRot = 0.2618F + (1.0F + Mth.cos(limbSwing * 0.6662F + (float) Math.PI)) * 0.7F * limbSwingAmount;
        this.leftLeg2.zRot = 0.5236F + (1.0F + Mth.cos(limbSwing * 0.6662F)) * 0.7F * limbSwingAmount;
        this.leftLeg3.zRot = 0.5236F + (1.0F + Mth.cos(limbSwing * 0.6662F + (float) Math.PI)) * 0.7F * limbSwingAmount;
        this.leftLeg4.zRot = 0.2618F + (1.0F + Mth.cos(limbSwing * 0.6662F)) * 0.7F * limbSwingAmount;
        this.leftClaw.xRot = 0.0F;
        this.rightClaw.xRot = 0.0F;
        this.wiggleX = 0.0F;
        this.wiggleY = 0.0F;
        this.crabSize = crab.getSizeModifier();
        if (this.f_102610_) {
            this.crabSize /= 2.0F;
        }
        if (crab.isRaving()) {
            float crabRaveBPM = 31.25F;
            float freq = 20.0F / crabRaveBPM;
            float tick = ageInTicks * freq;
            float sin = (float) (Math.sin((double) tick) * 0.5 + 0.5);
            float legRot = sin * 0.8F + 0.6F;
            this.leftLegs.forEach(l -> l.zRot = legRot);
            this.rightLegs.forEach(l -> l.zRot = -legRot);
            float maxHeight = -0.05F;
            float horizontalOff = 0.2F;
            this.wiggleX = (sin - 0.5F) * 2.0F * maxHeight + maxHeight / 2.0F;
            float slowSin = (float) Math.sin((double) (tick / 2.0F));
            this.wiggleY = slowSin * horizontalOff;
            float armRot = sin * 0.5F - 1.2F;
            this.leftClaw.xRot = armRot;
            this.rightClaw.xRot = armRot;
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrix, @NotNull VertexConsumer vb, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrix.pushPose();
        matrix.translate(0.0, 1.5 - (double) this.crabSize * 1.5, 0.0);
        matrix.scale(this.crabSize, this.crabSize, this.crabSize);
        matrix.mulPose(Axis.YP.rotationDegrees(90.0F));
        matrix.translate(this.wiggleX, this.wiggleY, 0.0F);
        this.group.render(matrix, vb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrix.popPose();
    }
}