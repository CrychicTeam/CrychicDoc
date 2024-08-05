package net.mehvahdjukaar.dummmmmmy.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.mehvahdjukaar.dummmmmmy.configs.ClientConfigs;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class TargetDummyModel<T extends TargetDummyEntity> extends HumanoidModel<T> {

    public final ModelPart standPlate;

    private float r = 0.0F;

    private float r2 = 0.0F;

    public TargetDummyModel(ModelPart modelPart) {
        super(modelPart);
        this.standPlate = modelPart.getChild("stand");
    }

    public static LayerDefinition createMesh(float size, int textHeight) {
        CubeDeformation deformation = new CubeDeformation(size);
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("stand", CubeListBuilder.create().texOffs(0, 32).addBox(-7.0F, 12.0F, -7.0F, 14.0F, 1.0F, 14.0F, deformation), PartPose.offset(0.0F, 11.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, 1.0F, -2.0F, 4.0F, 8.0F, 4.0F, deformation.extend(0.01F)), PartPose.offset(-2.5F, 2.0F, -0.005F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, 1.0F, -2.0F, 4.0F, 8.0F, 4.0F, deformation.extend(0.01F)), PartPose.offset(2.5F, 2.0F, -0.005F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation.extend(size != 0.0F ? -0.01F : 0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, textHeight);
    }

    public void rotateModelX(ModelPart model, float nrx, float nry, float nrz, float angle) {
        Vec3 oldRot = new Vec3((double) model.x, (double) model.y, (double) model.z);
        Vec3 actualRot = new Vec3((double) nrx, (double) nry, (double) nrz);
        Vec3 newRot = actualRot.add(oldRot.subtract(actualRot).xRot(-angle));
        model.setPos((float) newRot.x(), (float) newRot.y(), (float) newRot.z());
        model.xRot = angle;
    }

    public void rotateModelY(ModelPart model, float nrx, float nry, float nrz, float angle, int mult) {
        Vec3 oldRot = new Vec3((double) model.x, (double) model.y, (double) model.z);
        Vec3 actualRot = new Vec3((double) nrx, (double) nry, (double) nrz);
        Vec3 newRot = actualRot.add(oldRot.subtract(actualRot).xRot(-angle));
        model.setPos((float) newRot.x(), (float) newRot.y(), (float) newRot.z());
        model.yRot = angle * (float) mult;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int overlayIn, float red, float green, float blue, float alpha) {
        int overlay = OverlayTexture.NO_OVERLAY;
        matrixStackIn.pushPose();
        this.standPlate.render(matrixStackIn, bufferIn, packedLightIn, overlay, red, green, blue, alpha);
        this.f_102808_.render(matrixStackIn, bufferIn, packedLightIn, overlay, red, green, blue, alpha);
        this.f_102811_.render(matrixStackIn, bufferIn, packedLightIn, overlay, red, green, blue, alpha);
        this.f_102812_.render(matrixStackIn, bufferIn, packedLightIn, overlay, red, green, blue, alpha);
        this.f_102810_.render(matrixStackIn, bufferIn, packedLightIn, overlay, red, green, blue, alpha);
        this.f_102814_.render(matrixStackIn, bufferIn, packedLightIn, overlay, red, green, blue, alpha);
        this.f_102809_.render(matrixStackIn, bufferIn, packedLightIn, overlay, red, green, blue, alpha);
        matrixStackIn.popPose();
    }

    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        float phase = entity.getShake(partialTick);
        float swing = entity.getAnimationPosition(partialTick);
        float shake = Math.min((float) ((double) swing * (Double) ClientConfigs.ANIMATION_INTENSITY.get()), 40.0F);
        if (shake > 0.0F) {
            this.r = (float) (-((double) Mth.sin(phase) * Math.PI / 100.0 * (double) shake));
            this.r2 = (float) ((double) Mth.sin(phase) * Math.PI / 20.0 * (double) Math.min(shake, 1.0F));
        } else {
            this.r = 0.0F;
            this.r2 = 0.0F;
        }
        this.standPlate.xRot = 0.0F;
        this.standPlate.yRot = (float) (Math.PI / 180.0) * -Mth.rotLerp(partialTick, entity.f_19859_, entity.m_146908_());
        this.standPlate.zRot = 0.0F;
    }

    public void setupAnim(TargetDummyEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float n = 1.5F;
        float yOffsetIn = -1.0F;
        float xangle = this.r / 2.0F;
        this.f_102814_.setPos(0.0F, 12.0F + yOffsetIn, 0.0F);
        this.rotateModelX(this.f_102814_, 0.0F, 24.0F + yOffsetIn, 0.0F, xangle);
        this.f_102813_.setPos(0.0F, 12.0F + yOffsetIn, 0.0F);
        this.rotateModelX(this.f_102813_, 0.01F, 24.0F + yOffsetIn + 0.01F, 0.01F, xangle);
        this.f_102810_.setPos(0.0F, 0.0F + yOffsetIn, 0.0F);
        this.rotateModelX(this.f_102810_, 0.0F, 24.0F + yOffsetIn, 0.0F, xangle);
        this.f_102811_.setPos(-2.5F, 2.0F + yOffsetIn, -0.005F);
        this.rotateModelY(this.f_102811_, 0.0F, 24.0F + yOffsetIn, 0.0F, xangle, -1);
        this.f_102812_.setPos(2.5F, 2.0F + yOffsetIn, -0.005F);
        this.rotateModelY(this.f_102812_, 0.0F, 24.0F + yOffsetIn, 0.0F, xangle, 1);
        this.f_102808_.setPos(0.0F, 0.0F + yOffsetIn, 0.0F);
        this.rotateModelX(this.f_102808_, 0.0F, 24.0F + yOffsetIn, 0.0F, xangle);
        this.f_102809_.copyFrom(this.f_102808_);
        this.f_102808_.xRot = -this.r;
        this.f_102808_.zRot = this.r2;
        this.f_102811_.zRot = (float) (Math.PI / 2);
        this.f_102812_.zRot = (float) (-Math.PI / 2);
        this.f_102811_.xRot = this.r * n;
        this.f_102812_.xRot = this.r * n;
    }
}