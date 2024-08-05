package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;

public class LlamaModel<T extends AbstractChestedHorse> extends EntityModel<T> {

    private final ModelPart head;

    private final ModelPart body;

    private final ModelPart rightHindLeg;

    private final ModelPart leftHindLeg;

    private final ModelPart rightFrontLeg;

    private final ModelPart leftFrontLeg;

    private final ModelPart rightChest;

    private final ModelPart leftChest;

    public LlamaModel(ModelPart modelPart0) {
        this.head = modelPart0.getChild("head");
        this.body = modelPart0.getChild("body");
        this.rightChest = modelPart0.getChild("right_chest");
        this.leftChest = modelPart0.getChild("left_chest");
        this.rightHindLeg = modelPart0.getChild("right_hind_leg");
        this.leftHindLeg = modelPart0.getChild("left_hind_leg");
        this.rightFrontLeg = modelPart0.getChild("right_front_leg");
        this.leftFrontLeg = modelPart0.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation0) {
        MeshDefinition $$1 = new MeshDefinition();
        PartDefinition $$2 = $$1.getRoot();
        $$2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -14.0F, -10.0F, 4.0F, 4.0F, 9.0F, cubeDeformation0).texOffs(0, 14).addBox("neck", -4.0F, -16.0F, -6.0F, 8.0F, 18.0F, 6.0F, cubeDeformation0).texOffs(17, 0).addBox("ear", -4.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, cubeDeformation0).texOffs(17, 0).addBox("ear", 1.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, cubeDeformation0), PartPose.offset(0.0F, 7.0F, -6.0F));
        $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(29, 0).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, cubeDeformation0), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        $$2.addOrReplaceChild("right_chest", CubeListBuilder.create().texOffs(45, 28).addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, cubeDeformation0), PartPose.offsetAndRotation(-8.5F, 3.0F, 3.0F, 0.0F, (float) (Math.PI / 2), 0.0F));
        $$2.addOrReplaceChild("left_chest", CubeListBuilder.create().texOffs(45, 41).addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, cubeDeformation0), PartPose.offsetAndRotation(5.5F, 3.0F, 3.0F, 0.0F, (float) (Math.PI / 2), 0.0F));
        int $$3 = 4;
        int $$4 = 14;
        CubeListBuilder $$5 = CubeListBuilder.create().texOffs(29, 29).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, cubeDeformation0);
        $$2.addOrReplaceChild("right_hind_leg", $$5, PartPose.offset(-3.5F, 10.0F, 6.0F));
        $$2.addOrReplaceChild("left_hind_leg", $$5, PartPose.offset(3.5F, 10.0F, 6.0F));
        $$2.addOrReplaceChild("right_front_leg", $$5, PartPose.offset(-3.5F, 10.0F, -5.0F));
        $$2.addOrReplaceChild("left_front_leg", $$5, PartPose.offset(3.5F, 10.0F, -5.0F));
        return LayerDefinition.create($$1, 128, 64);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        this.rightHindLeg.xRot = Mth.cos(float1 * 0.6662F) * 1.4F * float2;
        this.leftHindLeg.xRot = Mth.cos(float1 * 0.6662F + (float) Math.PI) * 1.4F * float2;
        this.rightFrontLeg.xRot = Mth.cos(float1 * 0.6662F + (float) Math.PI) * 1.4F * float2;
        this.leftFrontLeg.xRot = Mth.cos(float1 * 0.6662F) * 1.4F * float2;
        boolean $$6 = !t0.m_6162_() && t0.hasChest();
        this.rightChest.visible = $$6;
        this.leftChest.visible = $$6;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        if (this.f_102610_) {
            float $$8 = 2.0F;
            poseStack0.pushPose();
            float $$9 = 0.7F;
            poseStack0.scale(0.71428573F, 0.64935064F, 0.7936508F);
            poseStack0.translate(0.0F, 1.3125F, 0.22F);
            this.head.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
            poseStack0.popPose();
            poseStack0.pushPose();
            float $$10 = 1.1F;
            poseStack0.scale(0.625F, 0.45454544F, 0.45454544F);
            poseStack0.translate(0.0F, 2.0625F, 0.0F);
            this.body.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
            poseStack0.popPose();
            poseStack0.pushPose();
            poseStack0.scale(0.45454544F, 0.41322312F, 0.45454544F);
            poseStack0.translate(0.0F, 2.0625F, 0.0F);
            ImmutableList.of(this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg, this.rightChest, this.leftChest).forEach(p_103083_ -> p_103083_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
            poseStack0.popPose();
        } else {
            ImmutableList.of(this.head, this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg, this.rightChest, this.leftChest).forEach(p_103073_ -> p_103073_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
        }
    }
}