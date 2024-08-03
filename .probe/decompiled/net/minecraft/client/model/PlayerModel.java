package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class PlayerModel<T extends LivingEntity> extends HumanoidModel<T> {

    private static final String EAR = "ear";

    private static final String CLOAK = "cloak";

    private static final String LEFT_SLEEVE = "left_sleeve";

    private static final String RIGHT_SLEEVE = "right_sleeve";

    private static final String LEFT_PANTS = "left_pants";

    private static final String RIGHT_PANTS = "right_pants";

    private final List<ModelPart> parts;

    public final ModelPart leftSleeve;

    public final ModelPart rightSleeve;

    public final ModelPart leftPants;

    public final ModelPart rightPants;

    public final ModelPart jacket;

    private final ModelPart cloak;

    private final ModelPart ear;

    private final boolean slim;

    public PlayerModel(ModelPart modelPart0, boolean boolean1) {
        super(modelPart0, RenderType::m_110473_);
        this.slim = boolean1;
        this.ear = modelPart0.getChild("ear");
        this.cloak = modelPart0.getChild("cloak");
        this.leftSleeve = modelPart0.getChild("left_sleeve");
        this.rightSleeve = modelPart0.getChild("right_sleeve");
        this.leftPants = modelPart0.getChild("left_pants");
        this.rightPants = modelPart0.getChild("right_pants");
        this.jacket = modelPart0.getChild("jacket");
        this.parts = (List<ModelPart>) modelPart0.getAllParts().filter(p_170824_ -> !p_170824_.isEmpty()).collect(ImmutableList.toImmutableList());
    }

    public static MeshDefinition createMesh(CubeDeformation cubeDeformation0, boolean boolean1) {
        MeshDefinition $$2 = HumanoidModel.createMesh(cubeDeformation0, 0.0F);
        PartDefinition $$3 = $$2.getRoot();
        $$3.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, cubeDeformation0), PartPose.ZERO);
        $$3.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, cubeDeformation0, 1.0F, 0.5F), PartPose.offset(0.0F, 0.0F, 0.0F));
        float $$4 = 0.25F;
        if (boolean1) {
            $$3.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, cubeDeformation0), PartPose.offset(5.0F, 2.5F, 0.0F));
            $$3.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, cubeDeformation0), PartPose.offset(-5.0F, 2.5F, 0.0F));
            $$3.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, cubeDeformation0.extend(0.25F)), PartPose.offset(5.0F, 2.5F, 0.0F));
            $$3.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, cubeDeformation0.extend(0.25F)), PartPose.offset(-5.0F, 2.5F, 0.0F));
        } else {
            $$3.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0), PartPose.offset(5.0F, 2.0F, 0.0F));
            $$3.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0.extend(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));
            $$3.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0.extend(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
        }
        $$3.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0), PartPose.offset(1.9F, 12.0F, 0.0F));
        $$3.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0.extend(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        $$3.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0.extend(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        $$3.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, cubeDeformation0.extend(0.25F)), PartPose.ZERO);
        return $$2;
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.leftPants, this.rightPants, this.leftSleeve, this.rightSleeve, this.jacket));
    }

    public void renderEars(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3) {
        this.ear.copyFrom(this.f_102808_);
        this.ear.x = 0.0F;
        this.ear.y = 0.0F;
        this.ear.render(poseStack0, vertexConsumer1, int2, int3);
    }

    public void renderCloak(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3) {
        this.cloak.render(poseStack0, vertexConsumer1, int2, int3);
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        this.leftPants.copyFrom(this.f_102814_);
        this.rightPants.copyFrom(this.f_102813_);
        this.leftSleeve.copyFrom(this.f_102812_);
        this.rightSleeve.copyFrom(this.f_102811_);
        this.jacket.copyFrom(this.f_102810_);
        if (t0.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            if (t0.m_6047_()) {
                this.cloak.z = 1.4F;
                this.cloak.y = 1.85F;
            } else {
                this.cloak.z = 0.0F;
                this.cloak.y = 0.0F;
            }
        } else if (t0.m_6047_()) {
            this.cloak.z = 0.3F;
            this.cloak.y = 0.8F;
        } else {
            this.cloak.z = -1.1F;
            this.cloak.y = -0.85F;
        }
    }

    @Override
    public void setAllVisible(boolean boolean0) {
        super.setAllVisible(boolean0);
        this.leftSleeve.visible = boolean0;
        this.rightSleeve.visible = boolean0;
        this.leftPants.visible = boolean0;
        this.rightPants.visible = boolean0;
        this.jacket.visible = boolean0;
        this.cloak.visible = boolean0;
        this.ear.visible = boolean0;
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm0, PoseStack poseStack1) {
        ModelPart $$2 = this.m_102851_(humanoidArm0);
        if (this.slim) {
            float $$3 = 0.5F * (float) (humanoidArm0 == HumanoidArm.RIGHT ? 1 : -1);
            $$2.x += $$3;
            $$2.translateAndRotate(poseStack1);
            $$2.x -= $$3;
        } else {
            $$2.translateAndRotate(poseStack1);
        }
    }

    public ModelPart getRandomModelPart(RandomSource randomSource0) {
        return (ModelPart) this.parts.get(randomSource0.nextInt(this.parts.size()));
    }
}