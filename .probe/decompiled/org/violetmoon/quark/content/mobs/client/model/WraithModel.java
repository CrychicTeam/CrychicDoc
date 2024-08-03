package org.violetmoon.quark.content.mobs.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Random;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.mobs.entity.Wraith;

public class WraithModel extends EntityModel<Wraith> {

    public final ModelPart main;

    public final ModelPart body;

    public final ModelPart arms;

    private double offset;

    private float alphaMult;

    public WraithModel(ModelPart root) {
        super(RenderType::m_110473_);
        this.main = root.getChild("main");
        this.body = this.main.getChild("body");
        this.arms = this.main.getChild("arms");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        main.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(36, 6).addBox(-8.5F, 1.0F, -2.0F, 3.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 55).addBox(-5.5F, 12.0F, 0.0F, 11.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(47, 3).addBox(-8.5F, 11.0F, -2.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.25F)).texOffs(36, 6).mirror().addBox(5.5F, 1.0F, -2.0F, 3.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(47, 3).mirror().addBox(5.5F, 11.0F, -2.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(0.0F, -17.0F, -1.0F));
        main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -10.0F, -4.0F, 11.0F, 26.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -18.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public void setupAnim(Wraith entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        Random rng = new Random((long) entity.m_19879_());
        int offset1 = rng.nextInt(10000000);
        int offset2 = rng.nextInt(6000000);
        int offset3 = rng.nextInt(8000000);
        float time = ageInTicks + (float) offset1;
        float time2 = ageInTicks + (float) offset2;
        float time3 = ageInTicks + (float) offset3;
        this.main.xRot = (float) Math.sin((double) (time / 16.0F)) * 0.1F - 0.3F;
        this.main.yRot = (float) Math.sin((double) (time2 / 20.0F)) * 0.12F;
        this.main.zRot = (float) Math.sin((double) (time3 / 12.0F)) * 0.07F;
        this.arms.xRot = (float) Math.sin((double) (time2 / 22.0F)) * 0.15F;
        this.offset = Math.sin((double) (time / 16.0F)) * 0.1 - 0.25;
        this.alphaMult = 0.8F + (float) Math.sin((double) (time2 / 20.0F)) * 0.2F;
    }

    @Override
    public void renderToBuffer(PoseStack matrix, @NotNull VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        alpha *= this.alphaMult;
        matrix.pushPose();
        matrix.translate(0.0, this.offset, -0.1);
        this.main.render(matrix, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrix.popPose();
    }
}