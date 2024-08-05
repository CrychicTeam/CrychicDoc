package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.client.util.LOD;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.TextUtil;
import net.mehvahdjukaar.supplementaries.client.TextUtils;
import net.mehvahdjukaar.supplementaries.common.block.TextHolder;
import net.mehvahdjukaar.supplementaries.common.block.tiles.DoormatBlockTile;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Style;

public class DoormatBlockTileRenderer implements BlockEntityRenderer<DoormatBlockTile> {

    public static final int LINE_SEPARATION = 15;

    private final Font font;

    private final Camera camera;

    public DoormatBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
        this.camera = Minecraft.getInstance().gameRenderer.getMainCamera();
    }

    @Override
    public int getViewDistance() {
        return 48;
    }

    public void render(DoormatBlockTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        LOD lod = new LOD(this.camera, tile.m_58899_());
        if (lod.isNear()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.0, 0.5);
            poseStack.mulPose(RotHlpr.rot(tile.getDirection()));
            poseStack.mulPose(RotHlpr.X90);
            poseStack.translate(0.0, -0.19791667F, -0.0675);
            poseStack.scale(0.010416667F, 0.010416667F, -0.010416667F);
            TextHolder textHolder = tile.getTextHolder();
            TextUtil.RenderProperties prop = TextUtil.renderProperties(textHolder.getColor(), textHolder.hasGlowingText(), ClientConfigs.getSignColorMult(), combinedLightIn, textHolder.hasAntiqueInk() ? Style.EMPTY.withFont(ModTextures.ANTIQUABLE_FONT) : Style.EMPTY, Direction.UP.step(), lod::isVeryNear);
            TextUtils.renderTextHolderLines(textHolder, 15, this.font, poseStack, bufferIn, prop);
            poseStack.popPose();
        }
    }
}