package net.mehvahdjukaar.supplementaries.client.renderers.entities.funny;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class PickleModel<T extends LivingEntity> extends PlayerModel<T> {

    public PickleModel(ModelPart modelPart) {
        super(modelPart, false);
    }

    public static LayerDefinition createMesh() {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 2).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 14.0F, 8.0F, false), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(2, 18).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, false), PartPose.offset(5.0F, 2.5F, 0.0F));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(16, 18).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, false), PartPose.offset(-5.0F, 2.5F, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 24).addBox(3.85F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, true), PartPose.offset(-1.9F, 12.0F, 0.0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 24).addBox(-5.85F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, false), PartPose.offset(1.9F, 12.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void translateToHand(HumanoidArm handSide, PoseStack matrixStack) {
        matrixStack.translate(0.0, 0.5, 0.0);
        ModelPart arm = this.m_102851_(handSide);
        float f = 1.0F * (float) (handSide == HumanoidArm.RIGHT ? 1 : -1);
        arm.x += f;
        arm.y--;
        arm.z++;
        arm.translateAndRotate(matrixStack);
        arm.z--;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        matrixStack.translate(0.0, this.f_102609_ ? -0.5 : 0.5, 0.0);
        super.m_7695_(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
    }

    @Override
    public void setupAnim(T player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (this.f_102818_ > 0.0F && player.isVisuallySwimming()) {
            this.f_102810_.yRot = this.m_102835_(limbSwing, this.f_102810_.yRot, -0.10471976F);
        } else {
            float f1 = (float) player.getFallFlyingTicks();
            if ((double) f1 > 0.01) {
                f1 += Minecraft.getInstance().getFrameTime();
                float inclination = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
                this.f_102812_.xRot = inclination * (float) Math.PI;
                this.f_102811_.xRot = inclination * (float) Math.PI;
            }
        }
    }

    public static class PickleArmor<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T, M, A> {

        public PickleArmor(RenderLayerParent<T, M> renderer, A modelChest, ModelManager manager) {
            super(renderer, modelChest, modelChest, manager);
        }

        @Override
        public void setPartVisibility(A modelIn, EquipmentSlot slotIn) {
            modelIn.setAllVisible(false);
            boolean head = slotIn == EquipmentSlot.HEAD;
            modelIn.hat.visible = head;
            modelIn.head.visible = head;
            modelIn.head.copyFrom(modelIn.body);
            modelIn.head.y = 13.0F;
            modelIn.hat.copyFrom(modelIn.head);
        }

        @Override
        public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!entity.m_6047_()) {
                super.render(matrixStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }

    public static class PickleElytra<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {

        public PickleElytra(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
            super(renderer, modelSet);
        }

        @Override
        public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            matrixStack.translate(0.0, 0.625, 0.09375);
            matrixStack.scale(0.625F, 0.625F, 0.625F);
            super.render(matrixStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }

        public boolean shouldRender(ItemStack stack, T entity) {
            return !entity.m_6047_() && stack.getItem() == Items.ELYTRA;
        }
    }
}