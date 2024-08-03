package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FixedBookModel extends Model {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(RLoc.create("sentry_book"), "main");

    private final ModelPart root;

    private final ModelPart leftLid;

    private final ModelPart rightLid;

    private final ModelPart leftPages;

    private final ModelPart rightPages;

    private final ModelPart flipPage1;

    private final ModelPart flipPage2;

    public FixedBookModel(ModelPart modelPart0) {
        super(RenderType::m_110446_);
        this.root = modelPart0;
        this.leftLid = modelPart0.getChild("left_lid");
        this.rightLid = modelPart0.getChild("right_lid");
        this.leftPages = modelPart0.getChild("left_pages");
        this.rightPages = modelPart0.getChild("right_pages");
        this.flipPage1 = modelPart0.getChild("flip_page1");
        this.flipPage2 = modelPart0.getChild("flip_page2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("left_lid", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -5.0F, -0.005F, 6.0F, 10.0F, 0.005F), PartPose.offset(0.0F, 0.0F, -1.0F));
        partdefinition.addOrReplaceChild("right_lid", CubeListBuilder.create().texOffs(16, 0).addBox(0.0F, -5.0F, -0.005F, 6.0F, 10.0F, 0.005F), PartPose.offset(0.0F, 0.0F, 1.0F));
        partdefinition.addOrReplaceChild("seam", CubeListBuilder.create().texOffs(12, 0).addBox(-1.005F, -5.005F, -0.005F, 2.005F, 10.005F, 0.005F), PartPose.rotation(0.0F, (float) (Math.PI / 2), 0.0F));
        partdefinition.addOrReplaceChild("left_pages", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, -4.0F, -0.99F, 5.0F, 8.0F, 1.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("right_pages", CubeListBuilder.create().texOffs(12, 10).addBox(0.0F, -4.0F, -0.01F, 5.0F, 8.0F, 1.0F), PartPose.ZERO);
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(24, 10).addBox(0.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.005F);
        partdefinition.addOrReplaceChild("flip_page1", cubelistbuilder, PartPose.ZERO);
        partdefinition.addOrReplaceChild("flip_page2", cubelistbuilder, PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        this.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
    }

    public void render(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        this.root.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
    }

    public void setupAnim(float float0, float float1, float float2, float float3) {
        float f = (Mth.sin(float0 * 0.02F) * 0.1F + 1.25F) * float3;
        this.leftLid.yRot = (float) Math.PI + f;
        this.rightLid.yRot = -f;
        this.leftPages.yRot = f;
        this.rightPages.yRot = -f;
        this.flipPage1.yRot = f - f * 2.0F * float1;
        this.flipPage2.yRot = f - f * 2.0F * float2;
        this.leftPages.x = Mth.sin(f);
        this.rightPages.x = Mth.sin(f);
        this.flipPage1.x = Mth.sin(f);
        this.flipPage2.x = Mth.sin(f);
    }
}