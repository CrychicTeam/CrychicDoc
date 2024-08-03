package com.github.alexthe666.alexsmobs.client.model.layered;

import com.github.alexthe666.alexsmobs.entity.util.FlyingFishBootsUtil;
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
import net.minecraft.world.entity.Pose;

public class ModelFlyingFishBoots extends HumanoidModel {

    private final ModelPart rightFish;

    private final ModelPart leftFish;

    private final ModelPart rightWingOuter;

    private final ModelPart leftWingOuter;

    private final ModelPart rightWingInner;

    private final ModelPart leftWingInner;

    public ModelFlyingFishBoots(ModelPart root) {
        super(root);
        this.rightFish = root.getChild("right_leg").getChild("RBoot");
        this.leftFish = root.getChild("left_leg").getChild("LBoot");
        this.rightWingOuter = this.rightFish.getChild("RwingR");
        this.leftWingOuter = this.leftFish.getChild("LwingL");
        this.rightWingInner = this.rightFish.getChild("RwingL");
        this.leftWingInner = this.leftFish.getChild("LwingR");
    }

    public static LayerDefinition createArmorLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 1.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition leftleg = partdefinition.getChild("left_leg");
        PartDefinition rightleg = partdefinition.getChild("right_leg");
        PartDefinition RBoot = rightleg.addOrReplaceChild("RBoot", CubeListBuilder.create().texOffs(18, 12).mirror().addBox(-1.9F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false).texOffs(0, 25).mirror().addBox(0.0F, -2.0F, 2.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(0, 0).mirror().addBox(-2.5F, 0.0F, -5.0F, 5.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.1F, 10.0F, 0.0F));
        RBoot.addOrReplaceChild("RwingR", CubeListBuilder.create().texOffs(9, 47).mirror().addBox(0.0F, -3.0F, 0.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, 1.0F, -3.0F, 0.0F, -0.5672F, 0.0F));
        RBoot.addOrReplaceChild("RwingL", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(0.0F, -3.0F, 0.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.5F, 1.0F, -3.0F, 0.0F, 0.5672F, 0.0F));
        PartDefinition LBoot = leftleg.addOrReplaceChild("LBoot", CubeListBuilder.create().texOffs(18, 12).addBox(-2.1F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.3F)).texOffs(0, 25).addBox(0.0F, -2.0F, 2.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-2.5F, 0.0F, -5.0F, 5.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 10.0F, 0.0F));
        LBoot.addOrReplaceChild("LwingL", CubeListBuilder.create().texOffs(9, 47).addBox(0.0F, -3.0F, 0.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 1.0F, -3.0F, 0.0F, 0.5672F, 0.0F));
        LBoot.addOrReplaceChild("LwingR", CubeListBuilder.create().texOffs(0, 42).addBox(0.0F, -3.0F, 0.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 1.0F, -3.0F, 0.0F, -0.5672F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public ModelFlyingFishBoots withAnimations(LivingEntity entity) {
        if (entity != null) {
            float partialTick = Minecraft.getInstance().getFrameTime();
            float ageInTicks = (float) entity.f_19797_ + partialTick;
            float fly = Mth.cos(ageInTicks * 0.2F) * 0.1F;
            float fly2 = fly * 0.35F;
            boolean flying = FlyingFishBootsUtil.getBoostTicks(entity) > 0;
            if (flying) {
                fly = (1.0F + Mth.sin(ageInTicks * 1.2F)) * 0.8F;
                fly2 = fly;
            }
            this.rightWingOuter.yRot = -0.5672F - fly;
            this.leftWingOuter.yRot = 0.5672F + fly;
            this.rightWingInner.yRot = 0.5672F + fly2;
            this.leftWingInner.yRot = -0.5672F - fly2;
            if (flying || entity.m_20089_() == Pose.SWIMMING) {
                this.leftFish.xRot = Maths.rad(-45.0);
                this.rightFish.xRot = Maths.rad(-45.0);
                this.rightFish.y = 11.0F;
                this.leftFish.y = 11.0F;
                this.rightFish.z = -1.5F;
                this.leftFish.z = -1.5F;
            } else if (entity.m_20089_() == Pose.CROUCHING) {
                this.leftFish.xRot = 0.0F;
                this.rightFish.xRot = 0.0F;
                this.rightFish.y = 8.0F;
                this.leftFish.y = 8.0F;
                this.rightFish.z = 0.0F;
                this.leftFish.z = 0.0F;
            } else {
                this.leftFish.xRot = 0.0F;
                this.rightFish.xRot = 0.0F;
                this.rightFish.y = 10.0F;
                this.leftFish.y = 10.0F;
                this.rightFish.z = 0.0F;
                this.leftFish.z = 0.0F;
            }
        }
        return this;
    }
}