package team.lodestar.lodestone.systems.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class LodestoneArmorModel extends HumanoidModel<LivingEntity> {

    public EquipmentSlot slot;

    public ModelPart root;

    public ModelPart head;

    public ModelPart body;

    public ModelPart leftArm;

    public ModelPart rightArm;

    public ModelPart leggings;

    public ModelPart leftLegging;

    public ModelPart rightLegging;

    public ModelPart leftFoot;

    public ModelPart rightFoot;

    public LodestoneArmorModel(ModelPart root) {
        super(root);
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leggings = root.getChild("leggings");
        this.leftArm = root.getChild("left_arm");
        this.rightArm = root.getChild("right_arm");
        this.leftLegging = root.getChild("left_legging");
        this.rightLegging = root.getChild("right_legging");
        this.leftFoot = root.getChild("left_foot");
        this.rightFoot = root.getChild("right_foot");
    }

    public static PartDefinition createHumanoidAlias(MeshDefinition mesh) {
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("leggings", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("head", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("left_legging", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("left_foot", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("right_legging", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("right_foot", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", new CubeListBuilder(), PartPose.ZERO);
        return root;
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return this.slot == EquipmentSlot.HEAD ? ImmutableList.of(this.head) : ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        if (this.slot == EquipmentSlot.CHEST) {
            return ImmutableList.of(this.body, this.leftArm, this.rightArm);
        } else if (this.slot == EquipmentSlot.LEGS) {
            return ImmutableList.of(this.leftLegging, this.rightLegging, this.leggings);
        } else {
            return this.slot == EquipmentSlot.FEET ? ImmutableList.of(this.leftFoot, this.rightFoot) : ImmutableList.of();
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.slot == EquipmentSlot.LEGS) {
            this.leggings.copyFrom(this.body);
            this.leftLegging.copyFrom(this.f_102814_);
            this.rightLegging.copyFrom(this.f_102813_);
        }
        super.m_7695_(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void copyFromDefault(HumanoidModel model) {
        this.leggings.copyFrom(model.body);
        this.body.copyFrom(model.body);
        this.head.copyFrom(model.head);
        this.leftArm.copyFrom(model.leftArm);
        this.rightArm.copyFrom(model.rightArm);
        this.leftLegging.copyFrom(this.f_102814_);
        this.rightLegging.copyFrom(this.f_102813_);
        this.leftFoot.copyFrom(this.f_102814_);
        this.rightFoot.copyFrom(this.f_102813_);
    }
}