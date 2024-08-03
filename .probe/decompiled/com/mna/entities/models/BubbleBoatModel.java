package com.mna.entities.models;

import com.mna.api.tools.RLoc;
import com.mna.entities.constructs.BubbleBoat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BubbleBoatModel<T extends BubbleBoat> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(RLoc.create("bubbleboatmodel"), "main");

    private final ModelPart bone;

    public BubbleBoatModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(38, 0).addBox(-7.876F, -6.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(38, 0).addBox(7.124F, -6.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(38, 0).addBox(-7.876F, 5.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(38, 0).addBox(7.124F, 5.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(38, 17).addBox(5.124F, -8.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(38, 17).addBox(5.124F, 7.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(34, 49).addBox(6.124F, 6.0F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(0, 51).addBox(6.124F, -6.0F, -7.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 51).addBox(6.124F, -6.0F, 6.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 51).addBox(-6.876F, -6.0F, -7.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 51).addBox(-6.876F, -6.0F, 6.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 49).addBox(-6.876F, 6.0F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(34, 49).addBox(6.124F, -7.0F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(34, 49).addBox(-6.876F, -7.0F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(37, 44).addBox(-5.876F, -7.0F, 6.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(37, 44).addBox(-5.876F, 6.0F, 6.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(37, 44).addBox(-5.876F, -7.0F, -7.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(37, 44).addBox(-5.876F, 6.0F, -7.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(38, 17).addBox(-5.876F, -8.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(38, 17).addBox(-5.876F, 7.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.876F, -8.0F, -6.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(38, 0).addBox(-5.876F, -6.0F, -8.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(38, 0).addBox(-5.876F, -6.0F, 7.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(38, 0).addBox(-5.876F, 5.0F, -8.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(38, 0).addBox(-5.876F, 5.0F, 7.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 53).addBox(5.124F, -5.0F, -8.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 53).addBox(5.124F, -5.0F, 7.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 53).addBox(-5.876F, -5.0F, -8.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 53).addBox(-5.876F, -5.0F, 7.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.876F, 7.0F, -6.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.876F, -8.0F, 5.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.876F, 7.0F, 5.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 53).addBox(-7.876F, -5.0F, -6.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 53).addBox(7.124F, -5.0F, -6.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 53).addBox(-7.876F, -5.0F, 5.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 53).addBox(7.124F, -5.0F, 5.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bone.zRot = entity.getBoatRotation(limbSwing);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bone.render(poseStack, buffer, packedLight, packedOverlay);
    }
}