package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.animal.allay.Allay;

public class AllayModel extends HierarchicalModel<Allay> implements ArmedModel {

    private final ModelPart root;

    private final ModelPart head;

    private final ModelPart body;

    private final ModelPart right_arm;

    private final ModelPart left_arm;

    private final ModelPart right_wing;

    private final ModelPart left_wing;

    private static final float FLYING_ANIMATION_X_ROT = (float) (Math.PI / 4);

    private static final float MAX_HAND_HOLDING_ITEM_X_ROT_RAD = -1.134464F;

    private static final float MIN_HAND_HOLDING_ITEM_X_ROT_RAD = (float) (-Math.PI / 3);

    public AllayModel(ModelPart modelPart0) {
        super(RenderType::m_110473_);
        this.root = modelPart0.getChild("root");
        this.head = this.root.getChild("head");
        this.body = this.root.getChild("body");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.right_wing = this.body.getChild("right_wing");
        this.left_wing = this.body.getChild("left_wing");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition $$2 = $$1.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 23.5F, 0.0F));
        $$2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.99F, 0.0F));
        PartDefinition $$3 = $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, -4.0F, 0.0F));
        $$3.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(23, 0).addBox(-0.75F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(-1.75F, 0.5F, 0.0F));
        $$3.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(23, 6).addBox(-0.25F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(1.75F, 0.5F, 0.0F));
        $$3.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 0.6F));
        $$3.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.6F));
        return LayerDefinition.create($$0, 32, 32);
    }

    public void setupAnim(Allay allay0, float float1, float float2, float float3, float float4, float float5) {
        this.root().getAllParts().forEach(ModelPart::m_233569_);
        float $$6 = float3 * 20.0F * (float) (Math.PI / 180.0) + float1;
        float $$7 = Mth.cos($$6) * (float) Math.PI * 0.15F + float2;
        float $$8 = float3 - (float) allay0.f_19797_;
        float $$9 = float3 * 9.0F * (float) (Math.PI / 180.0);
        float $$10 = Math.min(float2 / 0.3F, 1.0F);
        float $$11 = 1.0F - $$10;
        float $$12 = allay0.getHoldingItemAnimationProgress($$8);
        if (allay0.isDancing()) {
            float $$13 = float3 * 8.0F * (float) (Math.PI / 180.0) + float2;
            float $$14 = Mth.cos($$13) * 16.0F * (float) (Math.PI / 180.0);
            float $$15 = allay0.getSpinningProgress($$8);
            float $$16 = Mth.cos($$13) * 14.0F * (float) (Math.PI / 180.0);
            float $$17 = Mth.cos($$13) * 30.0F * (float) (Math.PI / 180.0);
            this.root.yRot = allay0.isSpinning() ? (float) (Math.PI * 4) * $$15 : this.root.yRot;
            this.root.zRot = $$14 * (1.0F - $$15);
            this.head.yRot = $$17 * (1.0F - $$15);
            this.head.zRot = $$16 * (1.0F - $$15);
        } else {
            this.head.xRot = float5 * (float) (Math.PI / 180.0);
            this.head.yRot = float4 * (float) (Math.PI / 180.0);
        }
        this.right_wing.xRot = 0.43633232F * (1.0F - $$10);
        this.right_wing.yRot = (float) (-Math.PI / 4) + $$7;
        this.left_wing.xRot = 0.43633232F * (1.0F - $$10);
        this.left_wing.yRot = (float) (Math.PI / 4) - $$7;
        this.body.xRot = $$10 * (float) (Math.PI / 4);
        float $$18 = $$12 * Mth.lerp($$10, (float) (-Math.PI / 3), -1.134464F);
        this.root.y = this.root.y + (float) Math.cos((double) $$9) * 0.25F * $$11;
        this.right_arm.xRot = $$18;
        this.left_arm.xRot = $$18;
        float $$19 = $$11 * (1.0F - $$12);
        float $$20 = 0.43633232F - Mth.cos($$9 + (float) (Math.PI * 3.0 / 2.0)) * (float) Math.PI * 0.075F * $$19;
        this.left_arm.zRot = -$$20;
        this.right_arm.zRot = $$20;
        this.right_arm.yRot = 0.27925268F * $$12;
        this.left_arm.yRot = -0.27925268F * $$12;
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm0, PoseStack poseStack1) {
        float $$2 = 1.0F;
        float $$3 = 3.0F;
        this.root.translateAndRotate(poseStack1);
        this.body.translateAndRotate(poseStack1);
        poseStack1.translate(0.0F, 0.0625F, 0.1875F);
        poseStack1.mulPose(Axis.XP.rotation(this.right_arm.xRot));
        poseStack1.scale(0.7F, 0.7F, 0.7F);
        poseStack1.translate(0.0625F, 0.0F, 0.0F);
    }
}