package net.mehvahdjukaar.supplementaries.client.renderers.entities.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class EndermanSkullModel extends SkullModel {

    private final ModelPart hat;

    public EndermanSkullModel(ModelPart modelPart) {
        super(modelPart);
        this.hat = modelPart.getChild("hat");
    }

    @Override
    public void setupAnim(float mouthAnim, float g, float h) {
        super.setupAnim(mouthAnim, g, h);
        this.f_103804_.y = mouthAnim * -6.0F;
        this.hat.y = 0.0F;
        this.hat.yRot = this.f_103804_.yRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.f_103804_.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.hat.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static LayerDefinition createMesh() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, -14.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartPose partPose = PartPose.offset(0.0F, -13.0F, 0.0F);
        partDefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), partPose);
        partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), partPose);
        return LayerDefinition.create(meshDefinition, 64, 32);
    }
}