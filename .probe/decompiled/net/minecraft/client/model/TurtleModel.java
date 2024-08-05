package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Turtle;

public class TurtleModel<T extends Turtle> extends QuadrupedModel<T> {

    private static final String EGG_BELLY = "egg_belly";

    private final ModelPart eggBelly;

    public TurtleModel(ModelPart modelPart0) {
        super(modelPart0, true, 120.0F, 0.0F, 9.0F, 6.0F, 120);
        this.eggBelly = modelPart0.getChild("egg_belly");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(3, 0).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 5.0F, 6.0F), PartPose.offset(0.0F, 19.0F, -10.0F));
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(7, 37).addBox("shell", -9.5F, 3.0F, -10.0F, 19.0F, 20.0F, 6.0F).texOffs(31, 1).addBox("belly", -5.5F, 3.0F, -13.0F, 11.0F, 18.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 11.0F, -10.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        $$1.addOrReplaceChild("egg_belly", CubeListBuilder.create().texOffs(70, 33).addBox(-4.5F, 3.0F, -14.0F, 9.0F, 18.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 11.0F, -10.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        int $$2 = 1;
        $$1.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(1, 23).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 10.0F), PartPose.offset(-3.5F, 22.0F, 11.0F));
        $$1.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(1, 12).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 10.0F), PartPose.offset(3.5F, 22.0F, 11.0F));
        $$1.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(27, 30).addBox(-13.0F, 0.0F, -2.0F, 13.0F, 1.0F, 5.0F), PartPose.offset(-5.0F, 21.0F, -4.0F));
        $$1.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(27, 24).addBox(0.0F, 0.0F, -2.0F, 13.0F, 1.0F, 5.0F), PartPose.offset(5.0F, 21.0F, -4.0F));
        return LayerDefinition.create($$0, 128, 64);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.eggBelly));
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        this.f_170852_.xRot = Mth.cos(float1 * 0.6662F * 0.6F) * 0.5F * float2;
        this.f_170853_.xRot = Mth.cos(float1 * 0.6662F * 0.6F + (float) Math.PI) * 0.5F * float2;
        this.f_170854_.zRot = Mth.cos(float1 * 0.6662F * 0.6F + (float) Math.PI) * 0.5F * float2;
        this.f_170855_.zRot = Mth.cos(float1 * 0.6662F * 0.6F) * 0.5F * float2;
        this.f_170854_.xRot = 0.0F;
        this.f_170855_.xRot = 0.0F;
        this.f_170854_.yRot = 0.0F;
        this.f_170855_.yRot = 0.0F;
        this.f_170852_.yRot = 0.0F;
        this.f_170853_.yRot = 0.0F;
        if (!t0.m_20069_() && t0.m_20096_()) {
            float $$6 = t0.isLayingEgg() ? 4.0F : 1.0F;
            float $$7 = t0.isLayingEgg() ? 2.0F : 1.0F;
            float $$8 = 5.0F;
            this.f_170854_.yRot = Mth.cos($$6 * float1 * 5.0F + (float) Math.PI) * 8.0F * float2 * $$7;
            this.f_170854_.zRot = 0.0F;
            this.f_170855_.yRot = Mth.cos($$6 * float1 * 5.0F) * 8.0F * float2 * $$7;
            this.f_170855_.zRot = 0.0F;
            this.f_170852_.yRot = Mth.cos(float1 * 5.0F + (float) Math.PI) * 3.0F * float2;
            this.f_170852_.xRot = 0.0F;
            this.f_170853_.yRot = Mth.cos(float1 * 5.0F) * 3.0F * float2;
            this.f_170853_.xRot = 0.0F;
        }
        this.eggBelly.visible = !this.f_102610_ && t0.hasEgg();
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        boolean $$8 = this.eggBelly.visible;
        if ($$8) {
            poseStack0.pushPose();
            poseStack0.translate(0.0F, -0.08F, 0.0F);
        }
        super.m_7695_(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
        if ($$8) {
            poseStack0.popPose();
        }
    }
}