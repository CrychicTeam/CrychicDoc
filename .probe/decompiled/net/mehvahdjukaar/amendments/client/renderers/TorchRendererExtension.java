package net.mehvahdjukaar.amendments.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mehvahdjukaar.amendments.configs.ClientConfigs;
import net.mehvahdjukaar.moonlight.api.item.IFirstPersonSpecialItemRenderer;
import net.mehvahdjukaar.moonlight.api.item.IThirdPersonAnimationProvider;
import net.mehvahdjukaar.moonlight.api.item.IThirdPersonSpecialItemRenderer;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class TorchRendererExtension implements IThirdPersonAnimationProvider, IThirdPersonSpecialItemRenderer, IFirstPersonSpecialItemRenderer {

    @Override
    public <T extends LivingEntity> boolean poseRightArm(ItemStack itemStack, HumanoidModel<T> model, T t, HumanoidArm arm) {
        if ((Boolean) ClientConfigs.HOLDING_ANIMATION_FIXED.get()) {
            model.rightArm.xRot = -1.3F;
        } else {
            model.rightArm.xRot = Mth.clamp(MthUtils.wrapRad(-1.4F + model.head.xRot), -2.4F, -0.2F);
        }
        return true;
    }

    @Override
    public <T extends LivingEntity> boolean poseLeftArm(ItemStack itemStack, HumanoidModel<T> model, T t, HumanoidArm arm) {
        if ((Boolean) ClientConfigs.HOLDING_ANIMATION_FIXED.get()) {
            model.leftArm.xRot = -1.3F;
        } else {
            model.leftArm.xRot = Mth.clamp(MthUtils.wrapRad(-1.4F + model.head.xRot), -2.4F, -0.2F);
        }
        return true;
    }

    @Override
    public <T extends Player, M extends EntityModel<T> & ArmedModel & HeadedModel> void renderThirdPersonItem(M parentModel, LivingEntity entity, ItemStack stack, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            parentModel.translateToHand(humanoidArm, poseStack);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            boolean left = humanoidArm == HumanoidArm.LEFT;
            poseStack.translate((float) (left ? -1 : 1) / 16.0F, 0.125F, -0.625F);
            poseStack.scale(1.0F, 1.0F, 1.0F);
            poseStack.translate(0.0F, 0.1875F, 0.125F);
            renderTorchModel(entity, stack, poseStack, bufferSource, light, left);
            poseStack.popPose();
        }
    }

    private static void renderTorchModel(LivingEntity entity, ItemStack itemStack, PoseStack poseStack, MultiBufferSource buffer, int light, boolean left) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        BlockState state = ((BlockItem) itemStack.getItem()).getBlock().defaultBlockState();
        BakedModel model = mc.getBlockRenderer().getBlockModel(state);
        itemRenderer.render(itemStack, ItemDisplayContext.NONE, left, poseStack, buffer, light, OverlayTexture.NO_OVERLAY, model);
    }

    @Override
    public boolean renderFirstPersonItem(AbstractClientPlayer player, ItemStack stack, InteractionHand hand, HumanoidArm arm, PoseStack poseStack, float partialTicks, float pitch, float attackAnim, float equipAnim, MultiBufferSource buffer, int light, ItemInHandRenderer renderer) {
        boolean left = arm == HumanoidArm.LEFT;
        float f = left ? -1.0F : 1.0F;
        poseStack.pushPose();
        float n = -0.4F * Mth.sin(Mth.sqrt(attackAnim) * (float) Math.PI);
        float m = 0.2F * Mth.sin(Mth.sqrt(attackAnim) * (float) (Math.PI * 2));
        float h = -0.2F * Mth.sin(attackAnim * (float) Math.PI);
        poseStack.translate(f * n, m, h);
        renderer.applyItemArmTransform(poseStack, arm, equipAnim);
        renderer.applyItemArmAttackTransform(poseStack, arm, attackAnim);
        ItemTransform transform = new ItemTransform(new Vector3f(0.0F, -90.0F, 25.0F), new Vector3f(0.0F, 2.0F, 1.25F).mul(0.0625F), new Vector3f(0.68F, 0.68F, 0.68F));
        transform.apply(left, poseStack);
        poseStack.translate((double) f * 0.5 / 16.0, 0.103125, -0.0625);
        renderTorchModel(player, stack, poseStack, buffer, light, left);
        poseStack.popPose();
        return true;
    }
}