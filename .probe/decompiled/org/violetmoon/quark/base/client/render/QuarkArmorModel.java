package org.violetmoon.quark.base.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Consumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;

public class QuarkArmorModel extends HumanoidModel<LivingEntity> {

    protected final EquipmentSlot slot;

    public QuarkArmorModel(ModelPart part, EquipmentSlot slot) {
        super(part);
        this.slot = slot;
    }

    public static LayerDefinition createLayer(int textureWidth, int textureHeight, Consumer<PartDefinition> rootConsumer) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        rootConsumer.accept(root);
        return LayerDefinition.create(mesh, textureWidth, textureHeight);
    }

    @Override
    public void setupAnim(@NotNull LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof ArmorStand entityIn) {
            this.f_102808_.xRot = (float) (Math.PI / 180.0) * entityIn.getHeadPose().getX();
            this.f_102808_.yRot = (float) (Math.PI / 180.0) * entityIn.getHeadPose().getY();
            this.f_102808_.zRot = (float) (Math.PI / 180.0) * entityIn.getHeadPose().getZ();
            this.f_102808_.setPos(0.0F, 1.0F, 0.0F);
            this.f_102810_.xRot = (float) (Math.PI / 180.0) * entityIn.getBodyPose().getX();
            this.f_102810_.yRot = (float) (Math.PI / 180.0) * entityIn.getBodyPose().getY();
            this.f_102810_.zRot = (float) (Math.PI / 180.0) * entityIn.getBodyPose().getZ();
            this.f_102812_.xRot = (float) (Math.PI / 180.0) * entityIn.getLeftArmPose().getX();
            this.f_102812_.yRot = (float) (Math.PI / 180.0) * entityIn.getLeftArmPose().getY();
            this.f_102812_.zRot = (float) (Math.PI / 180.0) * entityIn.getLeftArmPose().getZ();
            this.f_102811_.xRot = (float) (Math.PI / 180.0) * entityIn.getRightArmPose().getX();
            this.f_102811_.yRot = (float) (Math.PI / 180.0) * entityIn.getRightArmPose().getY();
            this.f_102811_.zRot = (float) (Math.PI / 180.0) * entityIn.getRightArmPose().getZ();
            this.f_102814_.xRot = (float) (Math.PI / 180.0) * entityIn.getLeftLegPose().getX();
            this.f_102814_.yRot = (float) (Math.PI / 180.0) * entityIn.getLeftLegPose().getY();
            this.f_102814_.zRot = (float) (Math.PI / 180.0) * entityIn.getLeftLegPose().getZ();
            this.f_102814_.setPos(1.9F, 11.0F, 0.0F);
            this.f_102813_.xRot = (float) (Math.PI / 180.0) * entityIn.getRightLegPose().getX();
            this.f_102813_.yRot = (float) (Math.PI / 180.0) * entityIn.getRightLegPose().getY();
            this.f_102813_.zRot = (float) (Math.PI / 180.0) * entityIn.getRightLegPose().getZ();
            this.f_102813_.setPos(-1.9F, 11.0F, 0.0F);
            this.f_102809_.copyFrom(this.f_102808_);
        } else {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack ms, @NotNull VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
        this.setPartVisibility(this.slot);
        super.m_7695_(ms, buffer, light, overlay, r, g, b, a);
    }

    private void setPartVisibility(EquipmentSlot slot) {
        this.m_8009_(false);
        switch(slot) {
            case HEAD:
                this.f_102808_.visible = true;
                this.f_102809_.visible = true;
                break;
            case CHEST:
                this.f_102810_.visible = true;
                this.f_102811_.visible = true;
                this.f_102812_.visible = true;
                break;
            case LEGS:
                this.f_102810_.visible = true;
                this.f_102813_.visible = true;
                this.f_102814_.visible = true;
                break;
            case FEET:
                this.f_102813_.visible = true;
                this.f_102814_.visible = true;
        }
    }
}