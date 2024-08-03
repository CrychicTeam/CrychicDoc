package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Bee;

public class BeeModel<T extends Bee> extends AgeableListModel<T> {

    private static final float BEE_Y_BASE = 19.0F;

    private static final String BONE = "bone";

    private static final String STINGER = "stinger";

    private static final String LEFT_ANTENNA = "left_antenna";

    private static final String RIGHT_ANTENNA = "right_antenna";

    private static final String FRONT_LEGS = "front_legs";

    private static final String MIDDLE_LEGS = "middle_legs";

    private static final String BACK_LEGS = "back_legs";

    private final ModelPart bone;

    private final ModelPart rightWing;

    private final ModelPart leftWing;

    private final ModelPart frontLeg;

    private final ModelPart midLeg;

    private final ModelPart backLeg;

    private final ModelPart stinger;

    private final ModelPart leftAntenna;

    private final ModelPart rightAntenna;

    private float rollAmount;

    public BeeModel(ModelPart modelPart0) {
        super(false, 24.0F, 0.0F);
        this.bone = modelPart0.getChild("bone");
        ModelPart $$1 = this.bone.getChild("body");
        this.stinger = $$1.getChild("stinger");
        this.leftAntenna = $$1.getChild("left_antenna");
        this.rightAntenna = $$1.getChild("right_antenna");
        this.rightWing = this.bone.getChild("right_wing");
        this.leftWing = this.bone.getChild("left_wing");
        this.frontLeg = this.bone.getChild("front_legs");
        this.midLeg = this.bone.getChild("middle_legs");
        this.backLeg = this.bone.getChild("back_legs");
    }

    public static LayerDefinition createBodyLayer() {
        float $$0 = 19.0F;
        MeshDefinition $$1 = new MeshDefinition();
        PartDefinition $$2 = $$1.getRoot();
        PartDefinition $$3 = $$2.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));
        PartDefinition $$4 = $$3.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F), PartPose.ZERO);
        $$4.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(26, 7).addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F), PartPose.ZERO);
        $$4.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F));
        $$4.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F));
        CubeDeformation $$5 = new CubeDeformation(0.001F);
        $$3.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, $$5), PartPose.offsetAndRotation(-1.5F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.0F));
        $$3.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, $$5), PartPose.offsetAndRotation(1.5F, -4.0F, -3.0F, 0.0F, 0.2618F, 0.0F));
        $$3.addOrReplaceChild("front_legs", CubeListBuilder.create().addBox("front_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 1), PartPose.offset(1.5F, 3.0F, -2.0F));
        $$3.addOrReplaceChild("middle_legs", CubeListBuilder.create().addBox("middle_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 3), PartPose.offset(1.5F, 3.0F, 0.0F));
        $$3.addOrReplaceChild("back_legs", CubeListBuilder.create().addBox("back_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 5), PartPose.offset(1.5F, 3.0F, 2.0F));
        return LayerDefinition.create($$1, 64, 64);
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        super.m_6839_(t0, float1, float2, float3);
        this.rollAmount = t0.getRollAmount(float3);
        this.stinger.visible = !t0.hasStung();
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.rightWing.xRot = 0.0F;
        this.leftAntenna.xRot = 0.0F;
        this.rightAntenna.xRot = 0.0F;
        this.bone.xRot = 0.0F;
        boolean $$6 = t0.m_20096_() && t0.m_20184_().lengthSqr() < 1.0E-7;
        if ($$6) {
            this.rightWing.yRot = -0.2618F;
            this.rightWing.zRot = 0.0F;
            this.leftWing.xRot = 0.0F;
            this.leftWing.yRot = 0.2618F;
            this.leftWing.zRot = 0.0F;
            this.frontLeg.xRot = 0.0F;
            this.midLeg.xRot = 0.0F;
            this.backLeg.xRot = 0.0F;
        } else {
            float $$7 = float3 * 120.32113F * (float) (Math.PI / 180.0);
            this.rightWing.yRot = 0.0F;
            this.rightWing.zRot = Mth.cos($$7) * (float) Math.PI * 0.15F;
            this.leftWing.xRot = this.rightWing.xRot;
            this.leftWing.yRot = this.rightWing.yRot;
            this.leftWing.zRot = -this.rightWing.zRot;
            this.frontLeg.xRot = (float) (Math.PI / 4);
            this.midLeg.xRot = (float) (Math.PI / 4);
            this.backLeg.xRot = (float) (Math.PI / 4);
            this.bone.xRot = 0.0F;
            this.bone.yRot = 0.0F;
            this.bone.zRot = 0.0F;
        }
        if (!t0.m_21660_()) {
            this.bone.xRot = 0.0F;
            this.bone.yRot = 0.0F;
            this.bone.zRot = 0.0F;
            if (!$$6) {
                float $$8 = Mth.cos(float3 * 0.18F);
                this.bone.xRot = 0.1F + $$8 * (float) Math.PI * 0.025F;
                this.leftAntenna.xRot = $$8 * (float) Math.PI * 0.03F;
                this.rightAntenna.xRot = $$8 * (float) Math.PI * 0.03F;
                this.frontLeg.xRot = -$$8 * (float) Math.PI * 0.1F + (float) (Math.PI / 8);
                this.backLeg.xRot = -$$8 * (float) Math.PI * 0.05F + (float) (Math.PI / 4);
                this.bone.y = 19.0F - Mth.cos(float3 * 0.18F) * 0.9F;
            }
        }
        if (this.rollAmount > 0.0F) {
            this.bone.xRot = ModelUtils.rotlerpRad(this.bone.xRot, 3.0915928F, this.rollAmount);
        }
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.bone);
    }
}