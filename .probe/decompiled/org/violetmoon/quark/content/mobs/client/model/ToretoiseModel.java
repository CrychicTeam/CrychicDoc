package org.violetmoon.quark.content.mobs.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.function.BiConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.content.mobs.entity.Toretoise;

public class ToretoiseModel extends EntityModel<Toretoise> {

    private Toretoise entity;

    private float animFrames;

    public ModelPart body;

    public ModelPart head;

    public ModelPart rightFrontLeg;

    public ModelPart leftFrontLeg;

    public ModelPart rightBackLeg;

    public ModelPart leftBackLeg;

    public ModelPart mouth;

    public ModelPart CoalOre1;

    public ModelPart CoalOre2;

    public ModelPart CoalOre3;

    public ModelPart CoalOre4;

    public ModelPart IronOre1;

    public ModelPart IronOre2;

    public ModelPart IronOre3;

    public ModelPart LapisOre1;

    public ModelPart LapisOre2;

    public ModelPart LapisOre3;

    public ModelPart LapisOre4;

    public ModelPart RedstoneOre1;

    public ModelPart RedstoneOre2;

    public ModelPart RedstoneOre3;

    public ModelPart RedstoneOre4;

    public ModelPart RedstoneOre5;

    public ToretoiseModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.rightFrontLeg = root.getChild("rightFrontLeg");
        this.leftFrontLeg = root.getChild("leftFrontLeg");
        this.rightBackLeg = root.getChild("rightBackLeg");
        this.leftBackLeg = root.getChild("leftBackLeg");
        this.mouth = this.head.getChild("mouth");
        this.CoalOre1 = this.body.getChild("CoalOre1");
        this.CoalOre2 = this.body.getChild("CoalOre2");
        this.CoalOre3 = this.body.getChild("CoalOre3");
        this.CoalOre4 = this.body.getChild("CoalOre4");
        this.IronOre1 = this.body.getChild("IronOre1");
        this.IronOre2 = this.body.getChild("IronOre2");
        this.IronOre3 = this.body.getChild("IronOre3");
        this.LapisOre1 = this.body.getChild("LapisOre1");
        this.LapisOre2 = this.body.getChild("LapisOre2");
        this.LapisOre3 = this.body.getChild("LapisOre3");
        this.LapisOre4 = this.body.getChild("LapisOre4");
        this.RedstoneOre1 = this.body.getChild("RedstoneOre1");
        this.RedstoneOre2 = this.body.getChild("RedstoneOre2");
        this.RedstoneOre3 = this.body.getChild("RedstoneOre3");
        this.RedstoneOre4 = this.body.getChild("RedstoneOre4");
        this.RedstoneOre5 = this.body.getChild("RedstoneOre5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, 0.0F, -13.0F, 22.0F, 12.0F, 26.0F), PartPose.offset(0.0F, 8.0F, 0.0F));
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 38).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 5.0F, 8.0F), PartPose.offset(0.0F, 16.0F, -13.0F));
        head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(66, 38).addBox(-4.5F, -2.5F, -8.0F, 9.0F, 4.0F, 8.0F), PartPose.offset(0.0F, 1.0F, -1.0F));
        root.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().mirror().texOffs(34, 38).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.offsetAndRotation(10.0F, 16.0F, -12.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        root.addOrReplaceChild("rightBackLeg", CubeListBuilder.create().texOffs(34, 38).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.offsetAndRotation(-10.0F, 16.0F, 12.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(34, 38).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.offsetAndRotation(-10.0F, 16.0F, -12.0F, 0.0F, (float) (Math.PI / 4), 0.0F));
        root.addOrReplaceChild("leftBackLeg", CubeListBuilder.create().mirror().texOffs(34, 38).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.offsetAndRotation(10.0F, 16.0F, 12.0F, 0.0F, (float) (-Math.PI / 4), 0.0F));
        body.addOrReplaceChild("CoalOre1", CubeListBuilder.create().texOffs(36, 56).addBox(0.0F, -7.0F, -6.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("CoalOre2", CubeListBuilder.create().texOffs(42, 56).addBox(7.0F, -2.0F, -10.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("CoalOre3", CubeListBuilder.create().texOffs(66, 50).addBox(-2.0F, -7.0F, -4.0F, 7.0F, 7.0F, 7.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("CoalOre4", CubeListBuilder.create().texOffs(60, 64).addBox(-15.0F, 0.0F, 1.0F, 4.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("IronOre1", CubeListBuilder.create().texOffs(36, 89).addBox(1.0F, -3.0F, 1.0F, 8.0F, 3.0F, 8.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("IronOre2", CubeListBuilder.create().texOffs(32, 81).addBox(-7.0F, -2.0F, -11.0F, 6.0F, 2.0F, 6.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("IronOre3", CubeListBuilder.create().texOffs(30, 76).addBox(-9.0F, -1.0F, 6.0F, 4.0F, 1.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("LapisOre1", CubeListBuilder.create().texOffs(0, 51).addBox(-5.0F, -8.0F, 0.0F, 8.0F, 8.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("LapisOre2", CubeListBuilder.create().texOffs(0, 53).addBox(-1.0F, -8.0F, -4.0F, 0.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("LapisOre3", CubeListBuilder.create().texOffs(18, 51).addBox(-10.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("LapisOre4", CubeListBuilder.create().texOffs(18, 53).addBox(-6.0F, -8.0F, 4.0F, 0.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("RedstoneOre1", CubeListBuilder.create().texOffs(0, 83).addBox(-8.0F, -12.0F, -6.0F, 5.0F, 12.0F, 5.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("RedstoneOre2", CubeListBuilder.create().texOffs(0, 74).addBox(6.0F, -6.0F, -1.0F, 3.0F, 6.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("RedstoneOre3", CubeListBuilder.create().texOffs(12, 76).addBox(-7.0F, -4.0F, 2.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("RedstoneOre4", CubeListBuilder.create().texOffs(20, 87).addBox(1.0F, -9.0F, -9.0F, 4.0F, 9.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("RedstoneOre5", CubeListBuilder.create().texOffs(15, 77).addBox(-1.0F, -5.0F, 5.0F, 5.0F, 5.0F, 5.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    public void setupAnim(@NotNull Toretoise entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.entity = entity;
        this.animFrames = limbSwing;
    }

    @Override
    public void renderToBuffer(PoseStack matrix, @NotNull VertexConsumer vb, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrix.pushPose();
        int bufferTime = 10;
        if (this.entity.angeryTicks > 0 && this.entity.angeryTicks < 20 - bufferTime) {
            double angeryTime = (double) (((float) this.entity.angeryTicks - QuarkClient.ticker.partialTicks) / (float) (20 - bufferTime)) * Math.PI;
            angeryTime = Math.sin(angeryTime) * -20.0;
            matrix.translate(0.0, 1.0, 1.0);
            matrix.mulPose(Axis.XP.rotationDegrees((float) angeryTime));
            matrix.translate(0.0F, -1.0F, -1.0F);
        }
        float animSpeed = 30.0F;
        float animPause = 12.0F;
        float actualFrames = this.animFrames * 10.0F;
        float doubleAnimSpeed = animSpeed * 2.0F;
        float animBuff = animSpeed - animPause;
        float scale = 0.02F;
        float bodyTrans = (float) (Math.sin((double) (actualFrames / doubleAnimSpeed) * Math.PI) + 1.0) * scale;
        float rideMultiplier = 0.0F;
        if (this.entity.rideTime > 0) {
            rideMultiplier = Math.min(30.0F, (float) (this.entity.rideTime - 1) + QuarkClient.ticker.partialTicks) / 30.0F;
        }
        bodyTrans *= 1.0F - rideMultiplier;
        matrix.translate(0.0F, bodyTrans, 0.0F);
        matrix.mulPose(Axis.ZP.rotation((bodyTrans - scale) * 0.5F));
        this.body.render(matrix, vb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrix.pushPose();
        matrix.translate(0.0, (double) bodyTrans, (double) rideMultiplier * 0.3);
        this.head.xRot = bodyTrans * 2.0F;
        this.head.render(matrix, vb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrix.popPose();
        float finalRideMultiplier = rideMultiplier;
        BiConsumer<ModelPart, Float> draw = (renderer, frames) -> {
            float time = Math.min(animBuff, frames % doubleAnimSpeed);
            float trans = (float) (Math.sin((double) (time / animBuff) * Math.PI) + 1.0) / -2.0F * 0.12F + 0.06F;
            float rotTime = frames % doubleAnimSpeed;
            float rot = ((float) Math.sin((double) (rotTime / doubleAnimSpeed) * Math.PI) + 1.0F) * -0.25F;
            trans *= 1.0F - finalRideMultiplier;
            rot *= 1.0F - finalRideMultiplier;
            trans += finalRideMultiplier * -0.2F;
            matrix.pushPose();
            ModelPart.Cube box = renderer.getRandomCube(this.entity.m_217043_());
            double spread = -0.1125 * (double) finalRideMultiplier;
            double x = (double) (renderer.x + box.minX);
            double z = (double) (renderer.z + box.minZ);
            x *= spread / Math.abs(x);
            z *= spread / Math.abs(z);
            matrix.translate(x, 0.0, z);
            matrix.translate(0.0F, trans, 0.0F);
            float yRot = renderer.yRot;
            renderer.xRot = rot;
            renderer.yRot *= 1.0F - finalRideMultiplier;
            renderer.render(matrix, vb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            renderer.yRot = yRot;
            matrix.popPose();
        };
        draw.accept(this.leftFrontLeg, actualFrames);
        draw.accept(this.rightFrontLeg, actualFrames + animSpeed);
        draw.accept(this.leftBackLeg, actualFrames + animSpeed * 0.5F);
        draw.accept(this.rightBackLeg, actualFrames + animSpeed * 1.5F);
        matrix.popPose();
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}