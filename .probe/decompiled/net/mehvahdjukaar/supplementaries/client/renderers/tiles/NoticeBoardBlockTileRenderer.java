package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Locale;
import net.mehvahdjukaar.moonlight.api.client.util.LOD;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.TextUtil;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.mehvahdjukaar.supplementaries.client.TextUtils;
import net.mehvahdjukaar.supplementaries.common.block.tiles.NoticeBoardBlockTile;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.ServerBoundRequestMapDataPacket;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class NoticeBoardBlockTileRenderer implements BlockEntityRenderer<NoticeBoardBlockTile> {

    private final ItemRenderer itemRenderer;

    private final MapRenderer mapRenderer;

    private final Font font;

    private final Camera camera;

    private static final float PAPER_X_MARGIN = 0.1875F;

    private static final float PAPER_Y_MARGIN = 0.125F;

    public NoticeBoardBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        Minecraft minecraft = Minecraft.getInstance();
        this.itemRenderer = minecraft.getItemRenderer();
        this.mapRenderer = minecraft.gameRenderer.getMapRenderer();
        this.font = context.getFont();
        this.camera = minecraft.gameRenderer.getMainCamera();
    }

    public int getFrontLight(Level world, BlockPos pos, Direction dir) {
        return LevelRenderer.getLightColor(world, pos.relative(dir));
    }

    public void render(NoticeBoardBlockTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int overlay) {
        if (!tile.shouldSkipTileRenderer()) {
            Level level = tile.m_58904_();
            if (level == null) {
                return;
            }
            ItemStack stack = tile.getDisplayedItem();
            if (stack.isEmpty()) {
                return;
            }
            Direction dir = tile.getDirection();
            float yaw = -dir.toYRot();
            Vec3 cameraPos = this.camera.getPosition();
            BlockPos pos = tile.m_58899_();
            if (LOD.isOutOfFocus(cameraPos, pos, yaw, 0.0F, dir, 0.0F)) {
                return;
            }
            int frontLight = this.getFrontLight(level, pos, dir);
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(RotHlpr.rot((int) yaw));
            poseStack.translate(0.0, 0.0, 0.5);
            renderNoticeBoardContent(this.mapRenderer, this.font, this.itemRenderer, tile, poseStack, buffer, frontLight, overlay, stack, dir, new LOD(cameraPos, pos));
            poseStack.popPose();
        }
    }

    public static void renderNoticeBoardContent(MapRenderer mapRenderer, Font font, ItemRenderer itemRenderer, NoticeBoardBlockTile tile, PoseStack poseStack, MultiBufferSource buffer, int frontLight, int overlay, ItemStack stack, Direction dir, LOD lod) {
        if (tile.isGlowing()) {
            frontLight = 15728880;
        }
        if (stack.getItem() instanceof ComplexItem) {
            MapItemSavedData mapData = MapItem.getSavedData(stack, tile.m_58904_());
            if (mapData != null) {
                poseStack.pushPose();
                poseStack.translate(0.0, 0.0, 0.008);
                poseStack.scale(0.0078125F, -0.0078125F, -0.0078125F);
                poseStack.translate(-64.0, -64.0, 0.0);
                Integer integer = MapItem.getMapId(stack);
                mapRenderer.render(poseStack, buffer, integer, mapData, true, frontLight);
                poseStack.popPose();
            } else {
                Player player = Minecraft.getInstance().player;
                ModNetwork.CHANNEL.sendToServer(new ServerBoundRequestMapDataPacket(tile.m_58899_(), player.m_20148_()));
            }
        } else {
            String page = tile.getText();
            if (page != null && !page.equals("")) {
                if (lod.isNearMed()) {
                    poseStack.pushPose();
                    poseStack.translate(0.0, 0.5, 0.008);
                    if (MiscUtils.FESTIVITY.isAprilsFool()) {
                        float d0 = ColorUtils.getShading(dir.step());
                        TextUtils.renderBeeMovie(poseStack, buffer, frontLight, font, d0);
                        poseStack.popPose();
                    } else {
                        String bookName = tile.m_8020_(0).getHoverName().getString().toLowerCase(Locale.ROOT);
                        if (bookName.equals("credits")) {
                            float d0 = ColorUtils.getShading(dir.step());
                            TextUtils.renderCredits(poseStack, buffer, frontLight, font, d0);
                            poseStack.popPose();
                        } else {
                            TextUtil.RenderProperties textProperties = tile.getTextHolder().computeRenderProperties(frontLight, dir.step(), lod::isVeryNear);
                            if (tile.needsVisualUpdate()) {
                                updateAndCacheLines(font, tile, page, textProperties);
                            }
                            List<FormattedCharSequence> rendererLines = tile.getCachedLines();
                            float scale = tile.getFontScale();
                            poseStack.scale(scale, -scale, scale);
                            int numberOfLines = rendererLines.size();
                            boolean centered = (Boolean) ClientConfigs.Blocks.NOTICE_BOARD_CENTERED_TEXT.get();
                            boolean missingno = bookName.equals("missingno");
                            for (int lin = 0; lin < numberOfLines; lin++) {
                                FormattedCharSequence str = (FormattedCharSequence) rendererLines.get(lin);
                                float dx = centered ? (float) (-font.width(str)) / 2.0F + 0.5F : -0.3125F / scale;
                                float dy = (1.0F / scale - (float) (8 * numberOfLines)) / 2.0F + 0.5F;
                                Matrix4f pose = poseStack.last().pose();
                                if (missingno) {
                                    font.drawInBatch("§ka", dx, dy + (float) (8 * lin), textProperties.textColor(), false, pose, buffer, Font.DisplayMode.NORMAL, 0, frontLight);
                                } else if (textProperties.outline()) {
                                    font.drawInBatch8xOutline(str, dx, dy + (float) (8 * lin), textProperties.textColor(), textProperties.darkenedColor(), pose, buffer, textProperties.light());
                                } else {
                                    font.drawInBatch(str, dx, dy + (float) (8 * lin), textProperties.darkenedColor(), false, pose, buffer, Font.DisplayMode.NORMAL, 0, textProperties.light());
                                }
                            }
                            poseStack.popPose();
                        }
                    }
                }
            } else {
                Material pattern = tile.getCachedPattern();
                if (pattern != null) {
                    VertexConsumer builder = pattern.buffer(buffer, RenderType::m_110482_);
                    int i = tile.getDyeColor().getTextColor();
                    float scale = 0.5F;
                    int b = (int) (scale * (float) FastColor.ARGB32.blue(i));
                    int g = (int) (scale * (float) FastColor.ARGB32.green(i));
                    int r = (int) (scale * (float) FastColor.ARGB32.red(i));
                    int lu = frontLight & 65535;
                    int lv = frontLight >> 16 & 65535;
                    poseStack.translate(0.0F, 0.0F, 0.008F);
                    VertexUtil.addQuad(builder, poseStack, -0.4375F, -0.4375F, 0.4375F, 0.4375F, 0.15625F, 0.0625F, 0.59375F, 0.9375F, r, g, b, 255, lu, lv);
                } else if (!tile.isNormalItem()) {
                    BakedModel model = itemRenderer.getModel(stack, tile.m_58904_(), null, 0);
                    poseStack.translate(0.0, 0.0, 0.015675);
                    poseStack.scale(-0.5F, 0.5F, -0.5F);
                    itemRenderer.render(stack, ItemDisplayContext.FIXED, true, poseStack, buffer, frontLight, overlay, model);
                }
            }
        }
    }

    private static void updateAndCacheLines(Font font, NoticeBoardBlockTile tile, String page, TextUtil.RenderProperties textProperties) {
        float paperWidth = 0.625F;
        float paperHeight = 0.75F;
        MutableComponent var8;
        if (TextUtil.parseText(page) instanceof MutableComponent mc) {
            var8 = mc.setStyle(textProperties.style());
        } else {
            var8 = Component.literal(page).setStyle(textProperties.style());
        }
        Pair<List<FormattedCharSequence>, Float> p = TextUtil.fitLinesToBox(font, var8, paperWidth, paperHeight);
        tile.setFontScale((Float) p.getSecond());
        tile.setCachedPageLines((List<FormattedCharSequence>) p.getFirst());
    }
}