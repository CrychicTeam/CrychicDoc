package org.violetmoon.quark.content.mobs.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.mobs.entity.Shiba;

public class ShibaModel extends EntityModel<Shiba> {

    private final ModelPart main;

    private final ModelPart head;

    private final ModelPart rEar;

    private final ModelPart lEar;

    private final ModelPart tongue;

    private final ModelPart torso;

    private final ModelPart tail;

    private final ModelPart rFrontLeg;

    private final ModelPart lFrontLeg;

    private final ModelPart rBackLeg;

    private final ModelPart lBackLeg;

    private boolean sleeping = false;

    public ShibaModel(ModelPart root) {
        this.main = root.getChild("main");
        this.head = this.main.getChild("head");
        this.rEar = this.head.getChild("rEar");
        this.lEar = this.head.getChild("lEar");
        this.tongue = this.head.getChild("tongue");
        this.torso = this.main.getChild("torso");
        this.tail = this.torso.getChild("tail");
        this.rFrontLeg = this.main.getChild("rFrontLeg");
        this.lFrontLeg = this.main.getChild("lFrontLeg");
        this.rBackLeg = this.main.getChild("rBackLeg");
        this.lBackLeg = this.main.getChild("lBackLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(16, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 11.0F, 6.0F).texOffs(44, 0).addBox(-1.5F, -6.0F, -8.0F, 3.0F, 3.0F, 4.0F), PartPose.offset(0.0F, 15.0F, -5.0F));
        head.addOrReplaceChild("rEar", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 2.0F, 3.0F), PartPose.offset(3.0F, -12.0F, 2.0F));
        head.addOrReplaceChild("lEar", CubeListBuilder.create().mirror().texOffs(0, 0).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 2.0F, 3.0F), PartPose.offset(-3.0F, -12.0F, 2.0F));
        head.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(36, 34).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 0.0F, 3.0F), PartPose.offset(0.0F, -4.0F, -8.0F));
        PartDefinition torso = main.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(36, 10).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 14.0F, 8.0F), PartPose.offset(0.0F, 13.0F, -7.0F));
        torso.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 14.0F, 4.0F));
        main.addOrReplaceChild("rFrontLeg", CubeListBuilder.create().texOffs(0, 21).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 8.0F, 3.0F), PartPose.offset(3.0F, 16.0F, -5.0F));
        main.addOrReplaceChild("lFrontLeg", CubeListBuilder.create().mirror().texOffs(0, 21).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 8.0F, 3.0F), PartPose.offset(-3.0F, 16.0F, -5.0F));
        main.addOrReplaceChild("rBackLeg", CubeListBuilder.create().texOffs(12, 18).addBox(-2.0F, -1.0F, -2.0F, 3.0F, 10.0F, 4.0F), PartPose.offset(3.0F, 15.0F, 4.0F));
        main.addOrReplaceChild("lBackLeg", CubeListBuilder.create().mirror().texOffs(12, 18).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 10.0F, 4.0F), PartPose.offset(-3.0F, 15.0F, 4.0F));
        return LayerDefinition.create(mesh, 80, 48);
    }

    public void transformToHead(PoseStack matrix) {
        this.head.translateAndRotate(matrix);
    }

    public void prepareMobModel(@NotNull Shiba shiba, float limbSwing, float limbSwingAmount, float partialTickTime) {
        BlockState state = shiba.m_146900_();
        this.sleeping = state.m_204336_(BlockTags.BEDS);
        this.setRotationAngle(this.rFrontLeg, Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount, 0.0F, 0.0F);
        this.setRotationAngle(this.lFrontLeg, Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount, 0.0F, 0.0F);
        this.setRotationAngle(this.rBackLeg, Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount, 0.0F, 0.0F);
        this.setRotationAngle(this.lBackLeg, Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount, 0.0F, 0.0F);
    }

    public void setupAnim(Shiba shiba, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        this.main.setPos(0.0F, 0.0F, 0.0F);
        this.lBackLeg.setPos(-3.0F, 15.0F, 4.0F);
        this.rBackLeg.setPos(3.0F, 15.0F, 4.0F);
        this.setRotationAngle(this.main, 0.0F, 0.0F, 0.0F);
        this.setRotationAngle(this.torso, 1.5708F, 0.0F, 0.0F);
        this.setRotationAngle(this.head, Mth.cos(ageInTicks * 0.6F) * 0.01F, yaw * (float) (Math.PI / 180.0), Mth.sin(ageInTicks * 0.06F) * 0.06F);
        this.setRotationAngle(this.tail, Mth.cos(ageInTicks * 0.1F) * 0.1F, Mth.sin(ageInTicks * 0.15F) * 0.12F, Mth.cos(ageInTicks * 0.3F) * 0.2F);
        this.setRotationAngle(this.lEar, 0.0F, Mth.cos(ageInTicks * 0.08F) * 0.05F - 0.05F, 0.0F);
        this.setRotationAngle(this.rEar, 0.0F, Mth.sin(ageInTicks * 0.07F) * 0.05F + 0.05F, 0.0F);
        boolean tongueOut = false;
        BlockState state = shiba.m_146900_();
        boolean sleep = state.m_204336_(BlockTags.BEDS);
        if (shiba.m_21825_()) {
            tongueOut = true;
            if (sleep) {
                this.main.setPos(16.0F, 18.0F, 0.0F);
                this.setRotationAngle(this.main, 0.0F, 0.0F, 1.5708F);
                this.setRotationAngle(this.lBackLeg, Mth.cos(ageInTicks * 0.2F) * 0.1F, 0.0F, Mth.sin(ageInTicks * 0.18F) * 0.12F);
                this.setRotationAngle(this.rBackLeg, Mth.sin(ageInTicks * 0.22F) * 0.08F, 0.0F, Mth.cos(ageInTicks * 0.16F) * 0.11F);
                this.setRotationAngle(this.rFrontLeg, Mth.cos(ageInTicks * 0.19F) * 0.1F, 0.0F, Mth.sin(ageInTicks * 0.21F) * 0.12F);
                this.setRotationAngle(this.lFrontLeg, Mth.sin(ageInTicks * 0.18F) * 0.08F, 0.0F, Mth.cos(ageInTicks * 0.2F) * 0.11F);
            } else {
                this.setRotationAngle(this.torso, 1.0F, 0.0F, 0.0F);
                this.lBackLeg.setPos(-3.0F, 19.0F, 2.0F);
                this.rBackLeg.setPos(3.0F, 19.0F, 2.0F);
                this.setRotationAngle(this.lBackLeg, -1.0F, -0.5F, 0.0F);
                this.setRotationAngle(this.rBackLeg, -1.0F, -0.5F, 0.0F);
                this.setRotationAngle(this.lFrontLeg, -0.5F, 0.5F, 0.0F);
                this.setRotationAngle(this.rFrontLeg, -0.5F, 0.5F, 0.0F);
            }
        }
        if (tongueOut && shiba.getMouthItem().isEmpty()) {
            this.tongue.setPos(0.0F, -4.0F, -6.75F + Mth.cos(ageInTicks * 0.19F) * 0.25F);
            this.setRotationAngle(this.tongue, Mth.cos(ageInTicks * 0.19F) * 0.1F + 0.2F, 0.0F, 0.0F);
        } else {
            this.tongue.setPos(0.0F, -4.0F, -5.0F);
            this.setRotationAngle(this.tongue, 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        if (this.sleeping) {
            matrixStack.translate(0.0, 0.12, 0.0);
        }
        this.main.translateAndRotate(matrixStack);
        matrixStack.pushPose();
        if (this.f_102610_) {
            matrixStack.translate(0.0F, 0.3125F, 0.0F);
        }
        this.head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
        matrixStack.pushPose();
        if (this.f_102610_) {
            matrixStack.translate(0.0F, 0.75F, 0.0F);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
        }
        this.torso.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rFrontLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rBackLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        this.lFrontLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        this.lBackLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.popPose();
        matrixStack.popPose();
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}