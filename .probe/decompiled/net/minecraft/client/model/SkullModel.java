package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SkullModel extends SkullModelBase {

    private final ModelPart root;

    protected final ModelPart head;

    public SkullModel(ModelPart modelPart0) {
        this.root = modelPart0;
        this.head = modelPart0.getChild("head");
    }

    public static MeshDefinition createHeadModel() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return $$0;
    }

    public static LayerDefinition createHumanoidHeadLayer() {
        MeshDefinition $$0 = createHeadModel();
        PartDefinition $$1 = $$0.getRoot();
        $$1.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 64);
    }

    public static LayerDefinition createMobHeadLayer() {
        MeshDefinition $$0 = createHeadModel();
        return LayerDefinition.create($$0, 64, 32);
    }

    @Override
    public void setupAnim(float float0, float float1, float float2) {
        this.head.yRot = float1 * (float) (Math.PI / 180.0);
        this.head.xRot = float2 * (float) (Math.PI / 180.0);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        this.root.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
    }
}