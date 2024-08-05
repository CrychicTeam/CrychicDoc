package net.minecraft.client.model;

import net.minecraft.client.animation.definitions.SnifferAnimation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.sniffer.Sniffer;

public class SnifferModel<T extends Sniffer> extends AgeableHierarchicalModel<T> {

    private static final float WALK_ANIMATION_SPEED_MAX = 9.0F;

    private static final float WALK_ANIMATION_SCALE_FACTOR = 100.0F;

    private final ModelPart root;

    private final ModelPart head;

    public SnifferModel(ModelPart modelPart0) {
        super(0.5F, 24.0F);
        this.root = modelPart0.getChild("root");
        this.head = this.root.getChild("bone").getChild("body").getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot().addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));
        PartDefinition $$2 = $$1.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition $$3 = $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(62, 68).addBox(-12.5F, -14.0F, -20.0F, 25.0F, 29.0F, 40.0F, new CubeDeformation(0.0F)).texOffs(62, 0).addBox(-12.5F, -14.0F, -20.0F, 25.0F, 24.0F, 40.0F, new CubeDeformation(0.5F)).texOffs(87, 68).addBox(-12.5F, 12.0F, -20.0F, 25.0F, 0.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        $$2.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(32, 87).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, 10.0F, -15.0F));
        $$2.addOrReplaceChild("right_mid_leg", CubeListBuilder.create().texOffs(32, 105).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, 10.0F, 0.0F));
        $$2.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(32, 123).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, 10.0F, 15.0F));
        $$2.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 87).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, 10.0F, -15.0F));
        $$2.addOrReplaceChild("left_mid_leg", CubeListBuilder.create().texOffs(0, 105).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, 10.0F, 0.0F));
        $$2.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 123).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, 10.0F, 15.0F));
        PartDefinition $$4 = $$3.addOrReplaceChild("head", CubeListBuilder.create().texOffs(8, 15).addBox(-6.5F, -7.5F, -11.5F, 13.0F, 18.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(8, 4).addBox(-6.5F, 7.5F, -11.5F, 13.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.5F, -19.48F));
        $$4.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(2, 0).addBox(0.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(6.51F, -7.5F, -4.51F));
        $$4.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.51F, -7.5F, -4.51F));
        $$4.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(10, 45).addBox(-6.5F, -2.0F, -9.0F, 13.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, -11.5F));
        $$4.addOrReplaceChild("lower_beak", CubeListBuilder.create().texOffs(10, 57).addBox(-6.5F, -7.0F, -8.0F, 13.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, -12.5F));
        return LayerDefinition.create($$0, 192, 192);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.root().getAllParts().forEach(ModelPart::m_233569_);
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        if (t0.isSearching()) {
            this.m_267799_(SnifferAnimation.SNIFFER_SNIFF_SEARCH, float1, float2, 9.0F, 100.0F);
        } else {
            this.m_267799_(SnifferAnimation.SNIFFER_WALK, float1, float2, 9.0F, 100.0F);
        }
        this.m_233381_(t0.diggingAnimationState, SnifferAnimation.SNIFFER_DIG, float3);
        this.m_233381_(t0.sniffingAnimationState, SnifferAnimation.SNIFFER_LONGSNIFF, float3);
        this.m_233381_(t0.risingAnimationState, SnifferAnimation.SNIFFER_STAND_UP, float3);
        this.m_233381_(t0.feelingHappyAnimationState, SnifferAnimation.SNIFFER_HAPPY, float3);
        this.m_233381_(t0.scentingAnimationState, SnifferAnimation.SNIFFER_SNIFFSNIFF, float3);
        if (this.f_102610_) {
            this.m_288214_(SnifferAnimation.BABY_TRANSFORM);
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}