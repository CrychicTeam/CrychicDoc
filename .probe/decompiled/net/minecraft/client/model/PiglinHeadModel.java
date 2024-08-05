package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class PiglinHeadModel extends SkullModelBase {

    private final ModelPart head;

    private final ModelPart leftEar;

    private final ModelPart rightEar;

    public PiglinHeadModel(ModelPart modelPart0) {
        this.head = modelPart0.getChild("head");
        this.leftEar = this.head.getChild("left_ear");
        this.rightEar = this.head.getChild("right_ear");
    }

    public static MeshDefinition createHeadModel() {
        MeshDefinition $$0 = new MeshDefinition();
        PiglinModel.addHead(CubeDeformation.NONE, $$0);
        return $$0;
    }

    @Override
    public void setupAnim(float float0, float float1, float float2) {
        this.head.yRot = float1 * (float) (Math.PI / 180.0);
        this.head.xRot = float2 * (float) (Math.PI / 180.0);
        float $$3 = 1.2F;
        this.leftEar.zRot = (float) (-(Math.cos((double) (float0 * (float) Math.PI * 0.2F * 1.2F)) + 2.5)) * 0.2F;
        this.rightEar.zRot = (float) (Math.cos((double) (float0 * (float) Math.PI * 0.2F)) + 2.5) * 0.2F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        this.head.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
    }
}