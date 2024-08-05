package dev.xkmc.modulargolems.content.client.armor;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class GolemEquipmentModels {

    public static final List<ModelLayerLocation> LIST = new ArrayList();

    public static final ModelLayerLocation HELMET_LAYER = new ModelLayerLocation(new ResourceLocation("modulargolems", "golem_helmet"), "main");

    public static final ModelLayerLocation CHESTPLATE_LAYER = new ModelLayerLocation(new ResourceLocation("modulargolems", "golem_chestplate"), "main");

    public static final ModelLayerLocation SHINGUARD_LAYER = new ModelLayerLocation(new ResourceLocation("modulargolems", "golem_shinguard"), "main");

    public static final ModelLayerLocation METALGOLEM = new ModelLayerLocation(new ResourceLocation("modulargolems", "metalgolem"), "model");

    public static MeshDefinition buildGolemBaseLayers() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition partdefinition = mesh.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F).texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -7.0F, -2.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F).texOffs(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -7.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 14.0F, 6.0F), PartPose.offset(0.0F, -7.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 14.0F, 6.0F), PartPose.offset(0.0F, -7.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F), PartPose.offset(-4.0F, 11.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(60, 0).mirror().addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F), PartPose.offset(5.0F, 11.0F, 0.0F));
        PartDefinition root1 = mesh.getRoot().getChild("right_arm");
        root1.addOrReplaceChild("right_forearm", CubeListBuilder.create().texOffs(60, 35).addBox(-12.99F, 0.0F, -6.0F, 4.0F, 16.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 11.5F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition root2 = mesh.getRoot().getChild("left_arm");
        root2.addOrReplaceChild("left_forearm", CubeListBuilder.create().texOffs(60, 72).addBox(8.99F, 0.0F, -6.0F, 4.0F, 16.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 11.5F, 3.0F, 0.0F, 0.0F, 0.0F));
        return mesh;
    }

    public static LayerDefinition createHelmetLayer() {
        MeshDefinition mesh = buildGolemBaseLayers();
        PartDefinition root = mesh.getRoot().getChild("head");
        root.addOrReplaceChild("helmet3", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, -44.0F, -8.0F, 9.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(76, 39).addBox(-4.5F, -37.0F, -6.25F, 10.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 31.0F, 2.0F));
        root.addOrReplaceChild("helmet17", CubeListBuilder.create().texOffs(48, 105).addBox(-5.5F, 28.0F, 0.0F, 9.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, -43.0F, -1.5F, 0.0F, 0.8727F, 0.0F));
        root.addOrReplaceChild("helmet16", CubeListBuilder.create().texOffs(108, 52).addBox(-3.5F, 28.0F, 0.0F, 9.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -43.0F, -1.5F, 0.0F, -0.8727F, 0.0F));
        root.addOrReplaceChild("helmet15", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, -36.0F, -5.25F, 0.4363F, 0.0F, 0.0F));
        root.addOrReplaceChild("helmet8", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 0.0F, -2.0F, 0.0F, 0.3491F, 0.0F));
        root.addOrReplaceChild("helmet9", CubeListBuilder.create().texOffs(50, 0).addBox(2.0F, 28.0F, -2.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -40.5F, -3.5F, 0.0F, 0.0F, 0.2182F));
        root.addOrReplaceChild("helmet10", CubeListBuilder.create().texOffs(70, 39).addBox(13.0F, 24.5F, 0.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.8669F, -41.8411F, -3.5F, 0.0F, 0.1309F, 0.5672F));
        root.addOrReplaceChild("helmet11", CubeListBuilder.create().texOffs(0, 0).addBox(-17.0F, -4.5F, -5.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.384F, -0.549F, 0.0F, 0.0F, 0.0873F, 0.7418F));
        root.addOrReplaceChild("helmet4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, -0.3491F, 0.0F));
        root.addOrReplaceChild("helmet6", CubeListBuilder.create().texOffs(105, 14).addBox(-8.0F, 28.0F, -2.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -40.5F, -3.5F, 0.0F, 0.0F, -0.2182F));
        root.addOrReplaceChild("helmet5", CubeListBuilder.create().texOffs(73, 18).addBox(-16.0F, 24.5F, 0.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.8669F, -41.8411F, -3.5F, 0.0F, -0.1309F, -0.5672F));
        root.addOrReplaceChild("helmet7", CubeListBuilder.create().texOffs(0, 5).addBox(14.0F, -4.5F, -5.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.384F, -0.549F, 0.0F, 0.0F, -0.0873F, -0.7418F));
        root.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(58, 12).addBox(-1.0F, -49.0F, -6.0F, 1.0F, 14.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 31.0F, 2.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createChestplateLayer() {
        MeshDefinition mesh = buildGolemBaseLayers();
        PartDefinition root = mesh.getRoot().getChild("body");
        root.addOrReplaceChild("main_body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.5F, 4.5F, -6.6F, 19.0F, 13.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(19, 110).addBox(5.5F, 3.5F, -7.75F, 2.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(105, 28).addBox(-7.5F, 3.5F, -7.75F, 2.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));
        PartDefinition root_ra1 = mesh.getRoot().getChild("right_arm");
        PartDefinition root_rfa = mesh.getRoot().getChild("right_arm").getChild("right_forearm");
        root_ra1.addOrReplaceChild("main_rightarm1", CubeListBuilder.create().texOffs(0, 83).addBox(-14.5F, -33.5F, -3.5F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 31.0F, 0.0F));
        root_rfa.addOrReplaceChild("main_rightforearm", CubeListBuilder.create().texOffs(89, 104).addBox(-13.0F, -29.5F, -6.5F, 4.01F, 7.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 111).addBox(-17.0F, -32.75F, -3.5F, 5.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 31.0F, 0.0F));
        root_ra1.addOrReplaceChild("main_rightarm2", CubeListBuilder.create().texOffs(90, 65).addBox(27.75F, 8.75F, -5.0F, 7.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(76, 52).addBox(27.0F, 4.75F, -5.5F, 7.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(65, 67).addBox(26.25F, 2.75F, -6.5F, 7.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.75F, -31.75F, 1.0F, 0.0F, 0.0F, 1.3526F));
        PartDefinition root_la1 = mesh.getRoot().getChild("left_arm");
        PartDefinition root_lfa = mesh.getRoot().getChild("left_arm").getChild("left_forearm");
        root_la1.addOrReplaceChild("main_leftarm1", CubeListBuilder.create().texOffs(86, 18).addBox(8.75F, -33.5F, -3.5F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 31.0F, 0.0F));
        root_la1.addOrReplaceChild("main_leftarm2", CubeListBuilder.create().texOffs(96, 82).addBox(-34.5F, 8.75F, -5.0F, 7.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(47, 81).addBox(-33.75F, 4.75F, -5.5F, 7.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(0, 69).addBox(-33.0F, 2.75F, -6.5F, 7.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.75F, -31.75F, 1.0F, 0.0F, 0.0F, -1.3526F));
        root_lfa.addOrReplaceChild("main_leftforearm", CubeListBuilder.create().texOffs(105, 0).addBox(8.98F, -29.5F, -6.5F, 4.01F, 7.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(68, 111).addBox(11.75F, -32.75F, -3.5F, 5.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 31.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createShinGuard() {
        MeshDefinition mesh = buildGolemBaseLayers();
        PartDefinition root1 = mesh.getRoot().getChild("body");
        root1.addOrReplaceChild("main_shinguard", CubeListBuilder.create().texOffs(70, 81).addBox(-5.0F, 19.0F, -3.5F, 10.0F, 2.0F, 7.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -7.0F, 0.0F));
        PartDefinition root2 = mesh.getRoot().getChild("right_leg");
        root2.addOrReplaceChild("shinguard1", CubeListBuilder.create().texOffs(48, 94).addBox(0.0F, -9.0F, -3.5F, 7.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 11.0F, 0.0F));
        PartDefinition root3 = mesh.getRoot().getChild("left_leg");
        root3.addOrReplaceChild("shinguard2", CubeListBuilder.create().texOffs(0, 100).addBox(-9.0F, -9.0F, -3.5F, 7.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 11.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createGolemLayer() {
        MeshDefinition mesh = buildGolemBaseLayers();
        return LayerDefinition.create(mesh, 128, 128);
    }

    public static void registerArmorLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        LIST.add(HELMET_LAYER);
        event.registerLayerDefinition(HELMET_LAYER, GolemEquipmentModels::createHelmetLayer);
        LIST.add(CHESTPLATE_LAYER);
        event.registerLayerDefinition(CHESTPLATE_LAYER, GolemEquipmentModels::createChestplateLayer);
        LIST.add(SHINGUARD_LAYER);
        event.registerLayerDefinition(SHINGUARD_LAYER, GolemEquipmentModels::createShinGuard);
        event.registerLayerDefinition(METALGOLEM, GolemEquipmentModels::createGolemLayer);
    }
}