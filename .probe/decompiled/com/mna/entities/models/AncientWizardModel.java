package com.mna.entities.models;

import com.mna.api.tools.RLoc;
import com.mna.entities.rituals.AncientCouncil;
import com.mna.tools.math.Vector3;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class AncientWizardModel<T extends AncientCouncil> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(RLoc.create("ancientwizardmodel"), "main");

    private final ModelPart main;

    private final ModelPart right_arm;

    private final ModelPart right_elbow;

    private final ModelPart left_arm;

    private final ModelPart left_elbow;

    private final ModelPart head;

    private static ModelPositionData neutral;

    private static ModelPositionData imbue_stage_1;

    private static ModelPositionData imbue_stage_2;

    private static ModelPositionData imbue_stage_3;

    private static HashMap<String, ModelPositionData> animationMap;

    public AncientWizardModel(ModelPart root) {
        this.main = root.getChild("main");
        this.right_arm = this.main.getChild("right_arm");
        this.left_arm = this.main.getChild("left_arm");
        this.head = this.main.getChild("head");
        this.right_elbow = this.right_arm.getChild("right_elbow");
        this.left_elbow = this.left_arm.getChild("left_elbow");
        this.initPoses();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(48, 22).addBox(-4.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(48, 22).addBox(0.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 33).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition right_arm = main.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 0).addBox(-4.0F, -0.025F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -24.0F, 0.0F, -0.6981F, 0.0F, 0.0F));
        right_arm.addOrReplaceChild("right_elbow", CubeListBuilder.create().texOffs(48, 11).addBox(-2.0F, -1.025F, -1.95F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.95F, 6.0F, 0.0F, 0.0F, 0.0F, -1.0472F));
        PartDefinition left_arm = main.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, -0.025F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -24.0F, 0.0F, -0.6981F, 0.0F, 0.0F));
        left_arm.addOrReplaceChild("left_elbow", CubeListBuilder.create().texOffs(48, 11).addBox(-2.075F, -1.025F, -1.95F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.05F, 6.0F, 0.0F, 0.0F, 0.0F, 1.0472F));
        main.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -12.0F, -2.0F, 1.0F, 24.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(4.0F, -12.0F, -2.0F, 1.0F, 24.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(30, 0).addBox(-4.0F, -12.0F, 2.0F, 8.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 0).addBox(-5.05F, -12.0F, -2.45F, 4.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(22, 0).addBox(-5.05F, 0.0F, -2.45F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(31, 13).addBox(-5.05F, 4.0F, -2.45F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(22, 0).mirror().addBox(1.95F, 0.0F, -2.45F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(31, 13).mirror().addBox(2.95F, 4.0F, -2.45F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(30, 0).mirror().addBox(0.95F, -12.0F, -2.45F, 4.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -12.0F, 0.0F));
        main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(40, 52).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 49).addBox(-4.0F, -6.95F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));
        main.addOrReplaceChild("gem", CubeListBuilder.create().texOffs(15, 8).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 12).addBox(-0.5F, -0.5F, -0.55F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -21.95F, -1.675F, 0.0F, 0.0F, 0.7854F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    private void initPoses() {
        animationMap = new HashMap();
        neutral = new ModelPositionData();
        neutral.addPositionDegrees(this.head);
        neutral.addPositionDegrees(this.right_arm);
        neutral.addPositionDegrees(this.right_elbow);
        neutral.addPositionDegrees(this.left_arm);
        neutral.addPositionDegrees(this.left_elbow);
        animationMap.put("neutral", neutral);
        imbue_stage_1 = new ModelPositionData();
        imbue_stage_1.addPositionDegrees(this.head, new Vector3(-30.0, 0.0, 0.0));
        imbue_stage_1.addPositionDegrees(this.right_arm, new Vector3(40.0, -45.0, 0.0));
        imbue_stage_1.addPositionDegrees(this.right_elbow, new Vector3(0.0, 0.0, 17.5));
        imbue_stage_1.addPositionDegrees(this.left_arm, new Vector3(40.0, 45.0, 0.0));
        imbue_stage_1.addPositionDegrees(this.left_elbow, new Vector3(0.0, 0.0, -17.5));
        animationMap.put("imbue_stage_1", imbue_stage_1);
        imbue_stage_2 = new ModelPositionData();
        imbue_stage_2.addPositionDegrees(this.head, new Vector3(30.0, 0.0, 0.0));
        imbue_stage_2.addPositionDegrees(this.right_arm, new Vector3(135.0, 5.0, 44.0));
        imbue_stage_2.addPositionDegrees(this.right_elbow, new Vector3(0.0, 0.0, 35.0));
        imbue_stage_2.addPositionDegrees(this.left_arm, new Vector3(125.0, -5.0, -44.0));
        imbue_stage_2.addPositionDegrees(this.left_elbow, new Vector3(0.0, 0.0, -35.0));
        animationMap.put("imbue_stage_2", imbue_stage_2);
        imbue_stage_3 = new ModelPositionData();
        imbue_stage_3.addPositionDegrees(this.head, new Vector3(30.0, 0.0, 0.0));
        imbue_stage_3.addPositionDegrees(this.right_arm, new Vector3(95.0, 47.0, 80.0));
        imbue_stage_3.addPositionDegrees(this.right_elbow, new Vector3(0.0, 0.0, 15.0));
        imbue_stage_3.addPositionDegrees(this.left_arm, new Vector3(95.0, -47.0, -80.0));
        imbue_stage_3.addPositionDegrees(this.left_elbow, new Vector3(0.0, 0.0, -15.0));
        animationMap.put("imbue_stage_3", imbue_stage_3);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        int lastChange = entity.getLastAnimChangeTimer();
        if (lastChange == 0) {
            animationMap.forEach((s, d) -> d.startLerp());
        } else {
            if (lastChange <= 20) {
                float neutral_return_timer = (float) lastChange / 20.0F;
                neutral.lerpRotations(neutral_return_timer);
            }
            float anim_pct = entity.getAnimationPct(0.0F);
            String current_anim = entity.getCurrentAnimation();
            if (animationMap.containsKey(current_anim)) {
                ((ModelPositionData) animationMap.get(current_anim)).lerpRotations(anim_pct);
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.main.render(poseStack, buffer, packedLight, packedOverlay);
    }
}