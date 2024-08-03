package net.mehvahdjukaar.amendments.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mehvahdjukaar.amendments.integration.CompatObjects;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.moonlight.api.item.IFirstPersonSpecialItemRenderer;
import net.mehvahdjukaar.moonlight.api.item.IThirdPersonAnimationProvider;
import net.mehvahdjukaar.moonlight.api.item.IThirdPersonSpecialItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class CandleHolderRendererExtension implements IThirdPersonAnimationProvider, IThirdPersonSpecialItemRenderer, IFirstPersonSpecialItemRenderer {

    private static final ResourceLocation FLAME = new ResourceLocation("textures/particle/flame.png");

    private static final ResourceLocation FLAME_SOUL = new ResourceLocation("textures/particle/soul_fire_flame.png");

    @Override
    public <T extends LivingEntity> boolean poseRightArm(ItemStack itemStack, HumanoidModel<T> model, T t, HumanoidArm arm) {
        model.rightArm.xRot = (float) (-Math.toRadians(40.0));
        return true;
    }

    @Override
    public <T extends LivingEntity> boolean poseLeftArm(ItemStack itemStack, HumanoidModel<T> model, T t, HumanoidArm arm) {
        model.leftArm.xRot = (float) (-Math.toRadians(40.0));
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
            poseStack.scale(2.0F, 2.0F, 2.0F);
            ItemTransform transform = new ItemTransform(new Vector3f(55.0F, -180.0F, 0.0F), new Vector3f(0.0F, 2.0F, 1.25F).mul(0.0625F), new Vector3f(0.375F, 0.375F, 0.375F));
            transform.apply(left, poseStack);
            renderLanternModel(entity, stack, poseStack, bufferSource, light, left);
            if (!entity.m_20069_()) {
                renderFlame(entity, poseStack, bufferSource, stack);
            }
            poseStack.popPose();
        }
    }

    private static void renderFlame(LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, ItemStack stack) {
        boolean soul = stack.getItem() == CompatObjects.SOUL_CANDLE_HOLDER.get();
        VertexConsumer builder = bufferSource.getBuffer(RenderType.text(soul ? FLAME_SOUL : FLAME));
        int lu = 240;
        int lv = 240;
        float period = 20.0F;
        float t = ((float) entity.f_19797_ + Minecraft.getInstance().getFrameTime()) % period / period;
        float ss = 1.0F - t * t * 0.4F;
        float scale = ss * 2.0F / 16.0F;
        poseStack.translate(0.0F, 0.1875F, 0.0F);
        poseStack.last().pose().setRotationXYZ(0.0F, 0.0F, 0.0F);
        poseStack.scale(-scale, scale, -scale);
        VertexUtil.addQuad(builder, poseStack, -0.5F, -0.5F, 0.5F, 0.5F, lu, lv);
    }

    private static void renderLanternModel(LivingEntity entity, ItemStack itemStack, PoseStack poseStack, MultiBufferSource buffer, int light, boolean left) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        BlockState state = ((BlockItem) itemStack.getItem()).getBlock().defaultBlockState();
        if (!entity.m_20069_()) {
            state = (BlockState) state.m_61124_(CandleBlock.LIT, true);
        }
        BakedModel model = mc.getBlockRenderer().getBlockModel(state);
        itemRenderer.render(itemStack, ItemDisplayContext.NONE, left, poseStack, buffer, light, OverlayTexture.NO_OVERLAY, model);
    }

    @Override
    public boolean renderFirstPersonItem(AbstractClientPlayer player, ItemStack stack, InteractionHand interactionHand, HumanoidArm arm, PoseStack poseStack, float partialTicks, float pitch, float attackAnim, float equipAnim, MultiBufferSource buffer, int light, ItemInHandRenderer renderer) {
        float candleScale = 0.625F;
        boolean left = arm == HumanoidArm.LEFT;
        float f = left ? -1.0F : 1.0F;
        poseStack.pushPose();
        float n = -0.4F * Mth.sin(Mth.sqrt(attackAnim) * (float) Math.PI);
        float m = 0.2F * Mth.sin(Mth.sqrt(attackAnim) * (float) (Math.PI * 2));
        float h = -0.2F * Mth.sin(attackAnim * (float) Math.PI);
        poseStack.translate(f * n, m, h);
        renderer.applyItemArmTransform(poseStack, arm, equipAnim);
        renderer.applyItemArmAttackTransform(poseStack, arm, attackAnim);
        poseStack.translate(0.0, 0.3125, 0.0);
        poseStack.scale(-candleScale, candleScale, -candleScale);
        renderLanternModel(player, stack, poseStack, buffer, light, left);
        if (!player.m_20069_()) {
            poseStack.translate((double) f * 0.03, 0.0, -0.04F);
            renderFlame(player, poseStack, buffer, stack);
        }
        poseStack.popPose();
        return true;
    }
}