package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class ShieldModel extends Model {

    private static final String PLATE = "plate";

    private static final String HANDLE = "handle";

    private static final int SHIELD_WIDTH = 10;

    private static final int SHIELD_HEIGHT = 20;

    private final ModelPart root;

    private final ModelPart plate;

    private final ModelPart handle;

    public ShieldModel(ModelPart modelPart0) {
        super(RenderType::m_110446_);
        this.root = modelPart0;
        this.plate = modelPart0.getChild("plate");
        this.handle = modelPart0.getChild("handle");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -11.0F, -2.0F, 12.0F, 22.0F, 1.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 64);
    }

    public ModelPart plate() {
        return this.plate;
    }

    public ModelPart handle() {
        return this.handle;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        this.root.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
    }
}