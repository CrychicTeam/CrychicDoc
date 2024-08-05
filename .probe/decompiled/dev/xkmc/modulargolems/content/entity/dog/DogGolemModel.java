package dev.xkmc.modulargolems.content.entity.dog;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DogGolemModel extends AgeableListModel<DogGolemEntity> implements IGolemModel<DogGolemEntity, DogGolemPartType, DogGolemModel> {

    private final ModelPart root;

    private final ModelPart head;

    private final ModelPart body;

    private final ModelPart rightHindLeg;

    private final ModelPart leftHindLeg;

    private final ModelPart rightFrontLeg;

    private final ModelPart leftFrontLeg;

    private final ModelPart tail;

    private final ModelPart upperBody;

    public DogGolemModel(EntityModelSet set) {
        this(set.bakeLayer(ModelLayers.WOLF));
    }

    public DogGolemModel(ModelPart part) {
        this.root = part;
        this.head = part.getChild("head");
        this.body = part.getChild("body");
        this.upperBody = part.getChild("upper_body");
        this.rightHindLeg = part.getChild("right_hind_leg");
        this.leftHindLeg = part.getChild("left_hind_leg");
        this.rightFrontLeg = part.getChild("right_front_leg");
        this.leftFrontLeg = part.getChild("left_front_leg");
        this.tail = part.getChild("tail");
    }

    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(DogGolemEntity dog, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.xRot = pHeadPitch * (float) (Math.PI / 180.0);
        this.head.yRot = pNetHeadYaw * (float) (Math.PI / 180.0);
        this.tail.xRot = pAgeInTicks;
    }

    public void prepareMobModel(DogGolemEntity dog, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        if (dog.m_21660_()) {
            this.tail.yRot = 0.0F;
        } else {
            this.tail.yRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        }
        if (dog.isInSittingPose()) {
            this.upperBody.setPos(-1.0F, 16.0F, -3.0F);
            this.upperBody.xRot = (float) (Math.PI * 2.0 / 5.0);
            this.upperBody.yRot = 0.0F;
            this.body.setPos(0.0F, 18.0F, 0.0F);
            this.body.xRot = (float) (Math.PI / 4);
            this.tail.setPos(-1.0F, 21.0F, 6.0F);
            this.rightHindLeg.setPos(-2.5F, 22.7F, 2.0F);
            this.rightHindLeg.xRot = (float) (Math.PI * 3.0 / 2.0);
            this.leftHindLeg.setPos(0.5F, 22.7F, 2.0F);
            this.leftHindLeg.xRot = (float) (Math.PI * 3.0 / 2.0);
            this.rightFrontLeg.xRot = 5.811947F;
            this.rightFrontLeg.setPos(-2.49F, 17.0F, -4.0F);
            this.leftFrontLeg.xRot = 5.811947F;
            this.leftFrontLeg.setPos(0.51F, 17.0F, -4.0F);
        } else {
            this.body.setPos(0.0F, 14.0F, 2.0F);
            this.body.xRot = (float) (Math.PI / 2);
            this.upperBody.setPos(-1.0F, 14.0F, -3.0F);
            this.upperBody.xRot = this.body.xRot;
            this.tail.setPos(-1.0F, 12.0F, 8.0F);
            this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
            this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
            this.rightFrontLeg.setPos(-2.5F, 16.0F, -4.0F);
            this.leftFrontLeg.setPos(0.5F, 16.0F, -4.0F);
            this.rightHindLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
            this.leftHindLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount;
            this.rightFrontLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount;
            this.leftFrontLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        }
    }

    public void renderToBufferInternal(DogGolemPartType type, PoseStack stack, VertexConsumer consumer, int i, int j, float f1, float f2, float f3, float f4) {
        if (type == DogGolemPartType.BODY) {
            this.body.render(stack, consumer, i, j, f1, f2, f3, f4);
            this.head.render(stack, consumer, i, j, f1, f2, f3, f4);
            this.upperBody.render(stack, consumer, i, j, f1, f2, f3, f4);
            this.tail.render(stack, consumer, i, j, f1, f2, f3, f4);
        } else if (type == DogGolemPartType.LEGS) {
            this.leftHindLeg.render(stack, consumer, i, j, f1, f2, f3, f4);
            this.rightHindLeg.render(stack, consumer, i, j, f1, f2, f3, f4);
            this.leftFrontLeg.render(stack, consumer, i, j, f1, f2, f3, f4);
            this.rightFrontLeg.render(stack, consumer, i, j, f1, f2, f3, f4);
        }
    }

    @Override
    public ResourceLocation getTextureLocationInternal(ResourceLocation rl) {
        String id = rl.getNamespace();
        String mat = rl.getPath();
        return new ResourceLocation(id, "textures/entity/dog_golem/" + mat + ".png");
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg, this.tail, this.upperBody);
    }
}