package net.mehvahdjukaar.supplementaries.client.renderers.entities.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.funny.SkibidiAnimations;
import net.mehvahdjukaar.supplementaries.common.entities.HatStandEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class HatStandModel extends HumanoidModel<HatStandEntity> implements IRootModel {

    private final ModelPart basePlate;

    private final ModelPart neck;

    private final ModelPart neckJoint;

    private final ModelPart root;

    private final ModelPart dummyHead;

    public HatStandModel(ModelPart modelPart) {
        super(modelPart);
        this.basePlate = modelPart.getChild("base_plate");
        this.root = modelPart;
        this.neckJoint = modelPart.getChild("neck_joint");
        this.neck = this.neckJoint.getChild("neck");
        this.dummyHead = this.neckJoint.getChild("dummy_head");
        this.dummyHead.visible = false;
    }

    @Override
    public ModelPart getRoot() {
        return this.root;
    }

    @Override
    public ModelPart getHead() {
        return this.f_102808_;
    }

    public static LayerDefinition createMesh() {
        int f = 20;
        MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, (float) f);
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("base_plate", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, 3.0F, -6.0F, 12.0F, 1.0F, 12.0F), PartPose.offset(0.0F, (float) f, 0.0F));
        partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, (float) f, 0.0F));
        PartDefinition neck = partDefinition.addOrReplaceChild("neck_joint", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        neck.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.ZERO);
        neck.addOrReplaceChild("dummy_head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, -3.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    public static LayerDefinition createArmorMesh() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F);
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    public void setupAnim(HatStandEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        Pose pose = entity.m_20089_();
        if (pose != Pose.STANDING) {
            GenericAnimationStuff.animate(this, entity.skibidiAnimation, SkibidiAnimations.DEFAULT, ageInTicks, 1.24F);
            this.dummyHead.y = this.dummyHead.y + (pose == Pose.SPIN_ATTACK ? 0.0F : 9.0F);
            this.dummyHead.visible = false;
        }
        Vector4f newPivot = new Vector4f(this.dummyHead.x, this.dummyHead.y, this.dummyHead.z, 1.0F);
        PoseStack poseStack = new PoseStack();
        this.translateAndRotate(this.neckJoint, poseStack);
        poseStack.last().pose().transform(newPivot);
        PartPose in = this.f_102808_.getInitialPose();
        this.f_102808_.offsetPos(new Vector3f(newPivot.x - in.x, newPivot.y - in.y, newPivot.z - in.z));
        this.f_102808_.setRotation(this.neckJoint.xRot + this.dummyHead.xRot, this.neckJoint.yRot + this.dummyHead.yRot, this.neckJoint.zRot + this.dummyHead.zRot);
        this.neck.yScale = -this.dummyHead.y / 3.0F;
    }

    public void prepareMobModel(HatStandEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.basePlate.visible = !entity.isNoBasePlate();
        this.basePlate.xRot = 0.0F;
        this.basePlate.yRot = (float) (Math.PI / 180.0) * -Mth.rotLerp(partialTick, entity.f_19859_, entity.m_146908_());
        this.basePlate.zRot = 0.0F;
        float xAngle = (float) (Math.PI / 180.0) * entity.getHeadPose().getX();
        this.f_102808_.yRot = (float) (Math.PI / 180.0) * entity.getHeadPose().getY();
        float zAngle = (float) (Math.PI / 180.0) * entity.getHeadPose().getZ();
        this.f_102809_.copyFrom(this.f_102808_);
        zAngle += entity.swingAnimation.getAngle(partialTick) * (float) (Math.PI / 180.0);
        this.f_102808_.resetPose();
        this.neck.resetPose();
        this.neckJoint.resetPose();
        this.dummyHead.resetPose();
        this.neckJoint.y += 23.0F;
        this.neckJoint.zRot += zAngle;
        this.neckJoint.xRot += xAngle;
    }

    public void translateAndRotate(ModelPart modelPart, PoseStack poseStack) {
        poseStack.translate(modelPart.x, modelPart.y, modelPart.z);
        if (modelPart.xRot != 0.0F || modelPart.yRot != 0.0F || modelPart.zRot != 0.0F) {
            poseStack.mulPose(new Quaternionf().rotationZYX(modelPart.zRot, modelPart.yRot, modelPart.xRot));
        }
        if (modelPart.xScale != 1.0F || modelPart.yScale != 1.0F || modelPart.zScale != 1.0F) {
            poseStack.scale(modelPart.xScale, modelPart.yScale, modelPart.zScale);
        }
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.f_102808_, this.basePlate, this.neckJoint);
    }
}