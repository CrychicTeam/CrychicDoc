package net.mehvahdjukaar.amendments.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mehvahdjukaar.amendments.configs.ClientConfigs;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.ThinAirCompat;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
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
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;

public class LanternRendererExtension implements IThirdPersonAnimationProvider, IThirdPersonSpecialItemRenderer, IFirstPersonSpecialItemRenderer {

    @Override
    public <T extends LivingEntity> boolean poseRightArm(ItemStack itemStack, HumanoidModel<T> model, T t, HumanoidArm arm) {
        boolean up = (Boolean) ClientConfigs.LANTERN_HOLDING_UP.get();
        if ((Boolean) ClientConfigs.HOLDING_ANIMATION_FIXED.get()) {
            model.rightArm.xRot = up ? -1.9F : -1.0F;
        } else {
            float v = up ? -1.9F : -1.2F;
            model.rightArm.xRot = Mth.clamp(MthUtils.wrapRad(v + model.head.xRot), -2.4F, -0.5F);
        }
        return true;
    }

    @Override
    public <T extends LivingEntity> boolean poseLeftArm(ItemStack itemStack, HumanoidModel<T> model, T t, HumanoidArm arm) {
        boolean up = (Boolean) ClientConfigs.LANTERN_HOLDING_UP.get();
        if ((Boolean) ClientConfigs.HOLDING_ANIMATION_FIXED.get()) {
            model.leftArm.xRot = up ? -1.9F : 1.0F;
        } else {
            float v = up ? -1.9F : -1.2F;
            model.leftArm.xRot = Mth.clamp(MthUtils.wrapRad(v + model.head.xRot), -2.4F, -0.5F);
        }
        return true;
    }

    @Override
    public <T extends Player, M extends EntityModel<T> & ArmedModel & HeadedModel> void renderThirdPersonItem(M parentModel, LivingEntity entity, ItemStack stack, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            boolean left = humanoidArm == HumanoidArm.LEFT;
            float shoulderOffset = 0.03125F;
            poseStack.translate(-shoulderOffset, 0.0F, 0.0F);
            parentModel.translateToHand(humanoidArm, poseStack);
            poseStack.translate(shoulderOffset + (float) (left ? 1 : -1) / 16.0F, 0.625F, 0.0625F);
            HumanoidModel<?> model = (HumanoidModel<?>) parentModel;
            if (left) {
                poseStack.mulPose(Axis.YP.rotationDegrees(model.leftArm.zRot * (180.0F / (float) Math.PI)));
                poseStack.mulPose(Axis.XP.rotationDegrees(-model.leftArm.xRot * (180.0F / (float) Math.PI)));
            } else {
                poseStack.mulPose(Axis.YP.rotationDegrees(model.rightArm.zRot * (180.0F / (float) Math.PI)));
                poseStack.mulPose(Axis.XP.rotationDegrees(-model.rightArm.xRot * (180.0F / (float) Math.PI)));
            }
            float scale = (float) ((Double) ClientConfigs.LANTERN_HOLDING_SIZE.get()).doubleValue();
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(RotHlpr.Z180);
            poseStack.translate(0.0F, -0.1875F, 0.0F);
            renderLanternModel(entity, stack, poseStack, bufferSource, light, left);
            poseStack.popPose();
        }
    }

    @Override
    public boolean renderFirstPersonItem(AbstractClientPlayer player, ItemStack itemStack, InteractionHand hand, HumanoidArm arm, PoseStack poseStack, float partialTicks, float pitch, float attackAnim, float equipAnim, MultiBufferSource buffer, int light, ItemInHandRenderer renderer) {
        float lanternScale = 1.0F;
        boolean left = arm == HumanoidArm.LEFT;
        float f = left ? -1.0F : 1.0F;
        poseStack.pushPose();
        poseStack.translate(-0.025 * (double) f, 0.125, 0.0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(f * 10.0F));
        renderer.renderPlayerArm(poseStack, buffer, light, equipAnim, attackAnim, arm);
        poseStack.translate(-0.5 - 0.25 * (double) f, 0.15, -0.463);
        poseStack.translate(0.066 * (double) f, -0.033, 0.024);
        Quaternionf rotationDiff;
        if (left) {
            rotationDiff = new Quaternionf(0.2077, 0.6488, -0.4433, 0.5825);
        } else {
            rotationDiff = new Quaternionf(0.2077, -0.6488, 0.4433, 0.5825);
        }
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(rotationDiff);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.scale(lanternScale, lanternScale, lanternScale);
        poseStack.translate(0.5, 0.5, 0.5);
        renderLanternModel(player, itemStack, poseStack, buffer, light, left);
        poseStack.popPose();
        return true;
    }

    private static void renderLanternModel(LivingEntity entity, ItemStack itemStack, PoseStack poseStack, MultiBufferSource buffer, int light, boolean left) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        BlockState state = ((BlockItem) itemStack.getItem()).getBlock().defaultBlockState();
        if (CompatHandler.THIN_AIR) {
            BlockState newState = ThinAirCompat.maybeSetAirQuality(state, entity.m_146892_(), entity.m_9236_());
            if (newState != null) {
                state = newState;
            }
        }
        if (state.m_61138_(LanternBlock.HANGING)) {
            state = (BlockState) state.m_61124_(LanternBlock.HANGING, false);
        }
        BakedModel model = mc.getBlockRenderer().getBlockModel(state);
        itemRenderer.render(itemStack, ItemDisplayContext.NONE, left, poseStack, buffer, light, OverlayTexture.NO_OVERLAY, model);
    }
}