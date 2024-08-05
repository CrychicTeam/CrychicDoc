package com.github.alexthe666.alexsmobs.client.model.layered;

import com.github.alexthe666.alexsmobs.entity.util.Maths;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelFrontierCap extends HumanoidModel {

    public ModelPart tail;

    public ModelPart hat;

    public ModelFrontierCap(ModelPart modelPart0) {
        super(modelPart0);
        this.hat = modelPart0.getChild("head").getChild("frontierhat");
        this.tail = this.hat.getChild("tail");
    }

    public static LayerDefinition createArmorLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.getChild("head");
        PartDefinition front = head.addOrReplaceChild("frontierhat", CubeListBuilder.create().texOffs(32, 32).addBox(-4.0F, -10.5F, -4.0F, 8.0F, 4.0F, 8.0F, deformation), PartPose.offset(0.0F, 0.0F, 0.0F));
        front.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(36, 46).addBox(-1.5F, -0.3F, -1.5F, 3.0F, 13.0F, 3.0F, deformation), PartPose.offsetAndRotation(4.4F, -7.5F, 4.5F, 0.19565141F, -0.039095376F, -0.11728612F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public ModelFrontierCap withAnimations(LivingEntity entity) {
        if (entity != null) {
            float partialTick = Minecraft.getInstance().getFrameTime();
            float limbSwingAmount = entity.walkAnimation.speed(partialTick);
            float limbSwing = entity.walkAnimation.position() + partialTick;
            this.tail.xRot = 0.19565141F + limbSwingAmount * Maths.rad(80.0) + Mth.cos(limbSwing * 0.3F) * 0.2F * limbSwingAmount;
            this.tail.yRot = -0.039095376F + limbSwingAmount * Maths.rad(10.0) - Mth.cos(limbSwing * 0.4F) * 0.3F * limbSwingAmount;
            this.tail.zRot = -0.11728612F + limbSwingAmount * Maths.rad(10.0);
        }
        return this;
    }
}