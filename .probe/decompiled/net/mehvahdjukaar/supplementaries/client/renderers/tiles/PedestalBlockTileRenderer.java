package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.supplementaries.client.GlobeManager;
import net.mehvahdjukaar.supplementaries.client.renderers.CapturedMobCache;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PedestalBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PedestalBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class PedestalBlockTileRenderer implements BlockEntityRenderer<PedestalBlockTile> {

    private final ItemRenderer itemRenderer;

    private final EntityRenderDispatcher entityRenderer;

    public PedestalBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        Minecraft minecraft = Minecraft.getInstance();
        this.itemRenderer = minecraft.getItemRenderer();
        this.entityRenderer = minecraft.getEntityRenderDispatcher();
    }

    protected boolean canRenderName(ItemStack item, PedestalBlockTile tile, PedestalBlockTile.DisplayType type) {
        if (Minecraft.renderNames() && item.hasCustomHoverName() && !type.isGlobe()) {
            double d0 = this.entityRenderer.distanceToSqr((double) tile.m_58899_().m_123341_() + 0.5, (double) tile.m_58899_().m_123342_() + 0.5, (double) tile.m_58899_().m_123343_() + 0.5);
            return d0 < 256.0;
        } else {
            return false;
        }
    }

    public static void renderName(Component name, float h, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn) {
        Minecraft mc = Minecraft.getInstance();
        int s = "Dinnerbone".equals(name.getString()) ? -1 : 1;
        poseStack.scale((float) s, (float) s, 1.0F);
        int i = 0;
        poseStack.pushPose();
        poseStack.translate(0.0F, h, 0.0F);
        poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        poseStack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = poseStack.last().pose();
        float f1 = mc.options.getBackgroundOpacity(0.25F);
        int j = (int) (f1 * 255.0F) << 24;
        float f2 = (float) (-mc.font.width(name)) / 2.0F;
        mc.font.drawInBatch(name, f2, (float) i, -1, false, matrix4f, bufferIn, Font.DisplayMode.NORMAL, j, combinedLightIn);
        poseStack.popPose();
    }

    public void render(PedestalBlockTile tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tile.m_7983_()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 1.125, 0.5);
            PedestalBlockTile.DisplayType displayType = tile.getDisplayType();
            ItemStack stack = tile.getDisplayedItem();
            if (this.canRenderName(stack, tile, displayType)) {
                renderName(tile.m_8020_(0).getHoverName(), 0.875F, matrixStackIn, bufferIn, combinedLightIn);
            }
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 0.25, 0.0);
            if (tile.m_58900_().m_61143_(PedestalBlock.AXIS) == Direction.Axis.X) {
                matrixStackIn.mulPose(RotHlpr.Y90);
            }
            ItemDisplayContext transform = ItemDisplayContext.FIXED;
            if ((Boolean) ClientConfigs.Blocks.PEDESTAL_SPECIAL.get()) {
                switch(displayType) {
                    case SWORD:
                        matrixStackIn.translate(0.0, -0.03125, 0.0);
                        matrixStackIn.scale(1.5F, 1.5F, 1.5F);
                        matrixStackIn.mulPose(RotHlpr.Z135);
                        break;
                    case TRIDENT:
                        matrixStackIn.translate(0.0, 0.03125, 0.0);
                        matrixStackIn.scale(1.5F, 1.5F, 1.5F);
                        matrixStackIn.mulPose(RotHlpr.ZN45);
                        break;
                    case CRYSTAL:
                        this.entityRenderer.render(CapturedMobCache.getEndCrystal(tile.m_58904_()), 0.0, 0.0, 0.0, 0.0F, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
                        matrixStackIn.popPose();
                        return;
                    default:
                        if ((Boolean) ClientConfigs.Blocks.PEDESTAL_SPIN.get()) {
                            matrixStackIn.translate(0.0F, 0.375F, 0.0F);
                            matrixStackIn.scale(1.5F, 1.5F, 1.5F);
                            int scale = (int) ((Double) ClientConfigs.Blocks.PEDESTAL_SPEED.get() * 360.0);
                            long time = tile.m_58904_().getGameTime();
                            float angle = ((float) Math.floorMod(time, (long) scale) + partialTicks) / (float) scale;
                            Quaternionf rotation = Axis.YP.rotation((float) ((double) angle * Math.PI * 10.0));
                            matrixStackIn.mulPose(rotation);
                        }
                        if (displayType.isGlobe()) {
                            if (GlobeBlockTileRenderer.INSTANCE != null) {
                                boolean sepia = tile.getDisplayType() == PedestalBlockTile.DisplayType.SEPIA_GLOBE;
                                Pair<GlobeManager.Model, ResourceLocation> pair = stack.hasCustomHoverName() ? GlobeManager.Type.getModelAndTexture(stack.getHoverName().getString()) : Pair.of(GlobeManager.Model.GLOBE, null);
                                GlobeBlockTileRenderer.INSTANCE.renderGlobe(pair, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, sepia, tile.m_58904_());
                            }
                            matrixStackIn.popPose();
                            return;
                        }
                }
            }
            if (MiscUtils.FESTIVITY.isAprilsFool()) {
                stack = new ItemStack(Items.DIRT);
            }
            this.itemRenderer.renderStatic(stack, transform, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, tile.m_58904_(), 0);
            matrixStackIn.popPose();
        }
    }
}