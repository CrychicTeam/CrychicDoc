package top.theillusivec4.curios.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public interface ICurioRenderer {

    <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack var1, SlotContext var2, PoseStack var3, RenderLayerParent<T, M> var4, MultiBufferSource var5, int var6, float var7, float var8, float var9, float var10, float var11, float var12);

    static void translateIfSneaking(PoseStack matrixStack, LivingEntity livingEntity) {
        if (livingEntity.m_6047_()) {
            matrixStack.translate(0.0F, 0.1875F, 0.0F);
        }
    }

    static void rotateIfSneaking(PoseStack matrixStack, LivingEntity livingEntity) {
        if (livingEntity.m_6047_() && Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity) instanceof LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer) {
            EntityModel<LivingEntity> model = livingRenderer.getModel();
            if (model instanceof HumanoidModel) {
                matrixStack.mulPose(Axis.XP.rotation(((HumanoidModel) model).body.xRot));
            }
        }
    }

    static void followHeadRotations(LivingEntity livingEntity, ModelPart... renderers) {
        if (Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity) instanceof LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer) {
            EntityModel<LivingEntity> model = livingRenderer.getModel();
            if (model instanceof HumanoidModel) {
                for (ModelPart renderer : renderers) {
                    renderer.copyFrom(((HumanoidModel) model).head);
                }
            }
        }
    }

    @SafeVarargs
    static void followBodyRotations(LivingEntity livingEntity, HumanoidModel<LivingEntity>... models) {
        if (Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity) instanceof LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer) {
            EntityModel<LivingEntity> entityModel = livingRenderer.getModel();
            if (entityModel instanceof HumanoidModel) {
                for (HumanoidModel<LivingEntity> model : models) {
                    HumanoidModel<LivingEntity> bipedModel = (HumanoidModel<LivingEntity>) entityModel;
                    bipedModel.copyPropertiesTo(model);
                }
            }
        }
    }
}