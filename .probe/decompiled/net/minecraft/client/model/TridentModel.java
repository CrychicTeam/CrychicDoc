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
import net.minecraft.resources.ResourceLocation;

public class TridentModel extends Model {

    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/trident.png");

    private final ModelPart root;

    public TridentModel(ModelPart modelPart0) {
        super(RenderType::m_110446_);
        this.root = modelPart0;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition $$2 = $$1.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(0, 6).addBox(-0.5F, 2.0F, -0.5F, 1.0F, 25.0F, 1.0F), PartPose.ZERO);
        $$2.addOrReplaceChild("base", CubeListBuilder.create().texOffs(4, 0).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 2.0F, 1.0F), PartPose.ZERO);
        $$2.addOrReplaceChild("left_spike", CubeListBuilder.create().texOffs(4, 3).addBox(-2.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.ZERO);
        $$2.addOrReplaceChild("middle_spike", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.ZERO);
        $$2.addOrReplaceChild("right_spike", CubeListBuilder.create().texOffs(4, 3).mirror().addBox(1.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        this.root.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
    }
}