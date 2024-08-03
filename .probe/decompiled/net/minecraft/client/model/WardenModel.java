package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.client.animation.definitions.WardenAnimation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.warden.Warden;

public class WardenModel<T extends Warden> extends HierarchicalModel<T> {

    private static final float DEFAULT_ARM_X_Y = 13.0F;

    private static final float DEFAULT_ARM_Z = 1.0F;

    private final ModelPart root;

    protected final ModelPart bone;

    protected final ModelPart body;

    protected final ModelPart head;

    protected final ModelPart rightTendril;

    protected final ModelPart leftTendril;

    protected final ModelPart leftLeg;

    protected final ModelPart leftArm;

    protected final ModelPart leftRibcage;

    protected final ModelPart rightArm;

    protected final ModelPart rightLeg;

    protected final ModelPart rightRibcage;

    private final List<ModelPart> tendrilsLayerModelParts;

    private final List<ModelPart> heartLayerModelParts;

    private final List<ModelPart> bioluminescentLayerModelParts;

    private final List<ModelPart> pulsatingSpotsLayerModelParts;

    public WardenModel(ModelPart modelPart0) {
        super(RenderType::m_110458_);
        this.root = modelPart0;
        this.bone = modelPart0.getChild("bone");
        this.body = this.bone.getChild("body");
        this.head = this.body.getChild("head");
        this.rightLeg = this.bone.getChild("right_leg");
        this.leftLeg = this.bone.getChild("left_leg");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightTendril = this.head.getChild("right_tendril");
        this.leftTendril = this.head.getChild("left_tendril");
        this.rightRibcage = this.body.getChild("right_ribcage");
        this.leftRibcage = this.body.getChild("left_ribcage");
        this.tendrilsLayerModelParts = ImmutableList.of(this.leftTendril, this.rightTendril);
        this.heartLayerModelParts = ImmutableList.of(this.body);
        this.bioluminescentLayerModelParts = ImmutableList.of(this.head, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
        this.pulsatingSpotsLayerModelParts = ImmutableList.of(this.body, this.head, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition $$2 = $$1.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition $$3 = $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -13.0F, -4.0F, 18.0F, 21.0F, 11.0F), PartPose.offset(0.0F, -21.0F, 0.0F));
        $$3.addOrReplaceChild("right_ribcage", CubeListBuilder.create().texOffs(90, 11).addBox(-2.0F, -11.0F, -0.1F, 9.0F, 21.0F, 0.0F), PartPose.offset(-7.0F, -2.0F, -4.0F));
        $$3.addOrReplaceChild("left_ribcage", CubeListBuilder.create().texOffs(90, 11).mirror().addBox(-7.0F, -11.0F, -0.1F, 9.0F, 21.0F, 0.0F).mirror(false), PartPose.offset(7.0F, -2.0F, -4.0F));
        PartDefinition $$4 = $$3.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 32).addBox(-8.0F, -16.0F, -5.0F, 16.0F, 16.0F, 10.0F), PartPose.offset(0.0F, -13.0F, 0.0F));
        $$4.addOrReplaceChild("right_tendril", CubeListBuilder.create().texOffs(52, 32).addBox(-16.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F), PartPose.offset(-8.0F, -12.0F, 0.0F));
        $$4.addOrReplaceChild("left_tendril", CubeListBuilder.create().texOffs(58, 0).addBox(0.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F), PartPose.offset(8.0F, -12.0F, 0.0F));
        $$3.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(44, 50).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 28.0F, 8.0F), PartPose.offset(-13.0F, -13.0F, 1.0F));
        $$3.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 58).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 28.0F, 8.0F), PartPose.offset(13.0F, -13.0F, 1.0F));
        $$2.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(76, 48).addBox(-3.1F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F), PartPose.offset(-5.9F, -13.0F, 0.0F));
        $$2.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(76, 76).addBox(-2.9F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F), PartPose.offset(5.9F, -13.0F, 0.0F));
        return LayerDefinition.create($$0, 128, 128);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.root().getAllParts().forEach(ModelPart::m_233569_);
        float $$6 = float3 - (float) t0.f_19797_;
        this.animateHeadLookTarget(float4, float5);
        this.animateWalk(float1, float2);
        this.animateIdlePose(float3);
        this.animateTendrils(t0, float3, $$6);
        this.m_233381_(t0.attackAnimationState, WardenAnimation.WARDEN_ATTACK, float3);
        this.m_233381_(t0.sonicBoomAnimationState, WardenAnimation.WARDEN_SONIC_BOOM, float3);
        this.m_233381_(t0.diggingAnimationState, WardenAnimation.WARDEN_DIG, float3);
        this.m_233381_(t0.emergeAnimationState, WardenAnimation.WARDEN_EMERGE, float3);
        this.m_233381_(t0.roarAnimationState, WardenAnimation.WARDEN_ROAR, float3);
        this.m_233381_(t0.sniffAnimationState, WardenAnimation.WARDEN_SNIFF, float3);
    }

    private void animateHeadLookTarget(float float0, float float1) {
        this.head.xRot = float1 * (float) (Math.PI / 180.0);
        this.head.yRot = float0 * (float) (Math.PI / 180.0);
    }

    private void animateIdlePose(float float0) {
        float $$1 = float0 * 0.1F;
        float $$2 = Mth.cos($$1);
        float $$3 = Mth.sin($$1);
        this.head.zRot += 0.06F * $$2;
        this.head.xRot += 0.06F * $$3;
        this.body.zRot += 0.025F * $$3;
        this.body.xRot += 0.025F * $$2;
    }

    private void animateWalk(float float0, float float1) {
        float $$2 = Math.min(0.5F, 3.0F * float1);
        float $$3 = float0 * 0.8662F;
        float $$4 = Mth.cos($$3);
        float $$5 = Mth.sin($$3);
        float $$6 = Math.min(0.35F, $$2);
        this.head.zRot += 0.3F * $$5 * $$2;
        this.head.xRot = this.head.xRot + 1.2F * Mth.cos($$3 + (float) (Math.PI / 2)) * $$6;
        this.body.zRot = 0.1F * $$5 * $$2;
        this.body.xRot = 1.0F * $$4 * $$6;
        this.leftLeg.xRot = 1.0F * $$4 * $$2;
        this.rightLeg.xRot = 1.0F * Mth.cos($$3 + (float) Math.PI) * $$2;
        this.leftArm.xRot = -(0.8F * $$4 * $$2);
        this.leftArm.zRot = 0.0F;
        this.rightArm.xRot = -(0.8F * $$5 * $$2);
        this.rightArm.zRot = 0.0F;
        this.resetArmPoses();
    }

    private void resetArmPoses() {
        this.leftArm.yRot = 0.0F;
        this.leftArm.z = 1.0F;
        this.leftArm.x = 13.0F;
        this.leftArm.y = -13.0F;
        this.rightArm.yRot = 0.0F;
        this.rightArm.z = 1.0F;
        this.rightArm.x = -13.0F;
        this.rightArm.y = -13.0F;
    }

    private void animateTendrils(T t0, float float1, float float2) {
        float $$3 = t0.getTendrilAnimation(float2) * (float) (Math.cos((double) float1 * 2.25) * Math.PI * 0.1F);
        this.leftTendril.xRot = $$3;
        this.rightTendril.xRot = -$$3;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public List<ModelPart> getTendrilsLayerModelParts() {
        return this.tendrilsLayerModelParts;
    }

    public List<ModelPart> getHeartLayerModelParts() {
        return this.heartLayerModelParts;
    }

    public List<ModelPart> getBioluminescentLayerModelParts() {
        return this.bioluminescentLayerModelParts;
    }

    public List<ModelPart> getPulsatingSpotsLayerModelParts() {
        return this.pulsatingSpotsLayerModelParts;
    }
}