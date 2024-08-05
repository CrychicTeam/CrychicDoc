package org.violetmoon.quark.content.tweaks.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.tweaks.entity.DyedItemFrame;

public class DyedItemFrameRenderer extends EntityRenderer<DyedItemFrame> {

    private static final ModelResourceLocation FRAME_LOCATION = new ModelResourceLocation("quark", "extra/dyed_item_frame", "inventory");

    private static final ModelResourceLocation MAP_FRAME_LOCATION = new ModelResourceLocation("quark", "extra/dyed_item_frame_map", "inventory");

    public static final int GLOW_FRAME_BRIGHTNESS = 5;

    public static final int BRIGHT_MAP_LIGHT_ADJUSTMENT = 30;

    private final ItemRenderer itemRenderer;

    private final BlockRenderDispatcher blockRenderer;

    public DyedItemFrameRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.itemRenderer = entityRendererProviderContext0.getItemRenderer();
        this.blockRenderer = entityRendererProviderContext0.getBlockRenderDispatcher();
    }

    protected int getBlockLightLevel(DyedItemFrame dyedItemFrame0, BlockPos blockPos1) {
        return dyedItemFrame0.isGlow() ? Math.max(5, super.getBlockLightLevel(dyedItemFrame0, blockPos1)) : super.getBlockLightLevel(dyedItemFrame0, blockPos1);
    }

    public void render(@NotNull DyedItemFrame dyedItemFrame, float float0, float float1, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int int2) {
        super.render(dyedItemFrame, float0, float1, poseStack, multiBufferSource, int2);
        poseStack.pushPose();
        Direction direction = dyedItemFrame.m_6350_();
        Vec3 vec3 = this.getRenderOffset(dyedItemFrame, float1);
        poseStack.translate(-vec3.x(), -vec3.y(), -vec3.z());
        poseStack.translate((double) direction.getStepX() * 0.46875, (double) direction.getStepY() * 0.46875, (double) direction.getStepZ() * 0.46875);
        poseStack.mulPose(Axis.XP.rotationDegrees(dyedItemFrame.m_146909_()));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - dyedItemFrame.m_146908_()));
        boolean flag = dyedItemFrame.m_20145_();
        ItemStack itemstack = dyedItemFrame.m_31822_();
        if (!flag) {
            ModelManager modelmanager = this.blockRenderer.getBlockModelShaper().getModelManager();
            boolean map = dyedItemFrame.m_31822_().getItem() instanceof MapItem;
            ModelResourceLocation modelresourcelocation = map ? MAP_FRAME_LOCATION : FRAME_LOCATION;
            poseStack.pushPose();
            poseStack.translate(-0.5, -0.5, -0.5);
            int color = dyedItemFrame.getColor();
            float r = (float) (color >> 16 & 0xFF) / 255.0F;
            float g = (float) (color >> 8 & 0xFF) / 255.0F;
            float b = (float) (color & 0xFF) / 255.0F;
            this.blockRenderer.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(Sheets.solidBlockSheet()), (BlockState) null, modelmanager.getModel(modelresourcelocation), r, g, b, int2, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
        if (!itemstack.isEmpty()) {
            MapItemSavedData mapitemsaveddata = MapItem.getSavedData(itemstack, dyedItemFrame.m_9236_());
            if (flag) {
                poseStack.translate(0.0, 0.0, 0.5);
            } else {
                poseStack.translate(0.0, 0.0, 0.4375);
            }
            int j = mapitemsaveddata != null ? dyedItemFrame.m_31823_() % 4 * 2 : dyedItemFrame.m_31823_();
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) j * 360.0F / 8.0F));
            if (mapitemsaveddata != null) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
                poseStack.translate(-64.0, -64.0, 0.0);
                poseStack.translate(0.0, 0.0, -1.0);
                if (mapitemsaveddata != null) {
                    int i = this.getLightVal(dyedItemFrame, 15728850, int2);
                    Minecraft.getInstance().gameRenderer.getMapRenderer().render(poseStack, multiBufferSource, dyedItemFrame.m_218868_().getAsInt(), mapitemsaveddata, true, i);
                }
            } else {
                int k = this.getLightVal(dyedItemFrame, 15728880, int2);
                poseStack.scale(0.5F, 0.5F, 0.5F);
                this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, k, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, Minecraft.getInstance().level, dyedItemFrame.m_19879_());
            }
        }
        poseStack.popPose();
    }

    private int getLightVal(DyedItemFrame dyedItemFrame0, int int1, int int2) {
        return dyedItemFrame0.isGlow() ? int1 : int2;
    }

    public Vec3 getRenderOffset(DyedItemFrame dyedItemFrame0, float float1) {
        return new Vec3((double) ((float) dyedItemFrame0.m_6350_().getStepX() * 0.3F), -0.25, (double) ((float) dyedItemFrame0.m_6350_().getStepZ() * 0.3F));
    }

    public ResourceLocation getTextureLocation(DyedItemFrame dyedItemFrame0) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    protected boolean shouldShowName(DyedItemFrame dyedItemFrame0) {
        if (Minecraft.renderNames() && !dyedItemFrame0.m_31822_().isEmpty() && dyedItemFrame0.m_31822_().hasCustomHoverName() && this.f_114476_.crosshairPickEntity == dyedItemFrame0) {
            double d0 = this.f_114476_.distanceToSqr(dyedItemFrame0);
            float f = dyedItemFrame0.m_20163_() ? 32.0F : 64.0F;
            return d0 < (double) (f * f);
        } else {
            return false;
        }
    }

    protected void renderNameTag(DyedItemFrame dyedItemFrame0, Component component1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4) {
        super.renderNameTag(dyedItemFrame0, dyedItemFrame0.m_31822_().getHoverName(), poseStack2, multiBufferSource3, int4);
    }
}