package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.api.client.util.LOD;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.TextUtil;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SignPostBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SignPostBlockTileRenderer implements BlockEntityRenderer<SignPostBlockTile> {

    public static final Map<WoodType, BakedModel> MODELS = new IdentityHashMap();

    private static ModelBlockRenderer renderer;

    private final Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

    private final Font font;

    public SignPostBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
        ModelManager manager = Minecraft.getInstance().getModelManager();
        MODELS.clear();
        for (Entry<WoodType, ResourceLocation> e : ((Map) ClientRegistry.SIGN_POST_MODELS.get()).entrySet()) {
            MODELS.put((WoodType) e.getKey(), ClientHelper.getModel(manager, (ResourceLocation) e.getValue()));
        }
        renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
    }

    @Override
    public int getViewDistance() {
        return 32;
    }

    public void render(SignPostBlockTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        this.renderSignsText(tile, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderSignsText(SignPostBlockTile tile, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockPos pos = tile.m_58899_();
        Vec3 cameraPos = this.camera.getPosition();
        LOD lod = new LOD(cameraPos, pos);
        if (lod.isNear()) {
            SignPostBlockTile.Sign signUp = tile.getSignUp();
            SignPostBlockTile.Sign signDown = tile.getSignDown();
            boolean up = signUp.active();
            boolean down = signDown.active();
            if (up || down) {
                float relAngle = LOD.getRelativeAngle(cameraPos, pos);
                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                if (up && LOD.isOutOfFocus(relAngle, signUp.yaw() + 90.0F, 2.0F)) {
                    Vector3f v = new Vector3f(1.0F, 0.0F, 0.0F);
                    v.rotateY(signUp.yaw() * (float) (Math.PI / 180.0));
                    TextUtil.RenderProperties textProperties = tile.getTextHolder(0).computeRenderProperties(combinedLightIn, v, lod::isVeryNear);
                    this.renderSignText(tile, poseStack, bufferIn, signUp, textProperties, 0);
                }
                if (down && LOD.isOutOfFocus(relAngle, signDown.yaw() + 90.0F, 2.0F)) {
                    Vector3f normalVector = new Vector3f(1.0F, 0.0F, 0.0F);
                    normalVector.rotateY(signUp.yaw() * (float) (Math.PI / 180.0));
                    TextUtil.RenderProperties textProperties = tile.getTextHolder(1).computeRenderProperties(combinedLightIn, normalVector, lod::isVeryNear);
                    poseStack.translate(0.0, -0.5, 0.0);
                    this.renderSignText(tile, poseStack, bufferIn, signDown, textProperties, 1);
                }
                poseStack.popPose();
            }
        }
    }

    private void renderSignText(SignPostBlockTile tile, PoseStack matrixStackIn, MultiBufferSource bufferIn, SignPostBlockTile.Sign sign, TextUtil.RenderProperties textProperties, int line) {
        matrixStackIn.pushPose();
        boolean left = sign.left();
        int o = left ? 1 : -1;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(sign.yaw() - 90.0F));
        if (tile.isSlim()) {
            matrixStackIn.translate(0.0F, 0.0F, -0.0625F);
        }
        matrixStackIn.translate(-0.03125 * (double) o, 0.28125, 0.1925);
        matrixStackIn.scale(0.010416667F, -0.010416667F, 0.010416667F);
        TextUtil.renderLine(tile.getTextHolder(line).getRenderMessages(0, this.font), this.font, -4.0F, matrixStackIn, bufferIn, textProperties);
        matrixStackIn.popPose();
    }

    public static void renderSigns(PoseStack poseStack, VertexConsumer builder, int combinedLightIn, int combinedOverlayIn, SignPostBlockTile.Sign signUp, SignPostBlockTile.Sign signDown, boolean slim) {
        boolean up = signUp.active();
        boolean down = signDown.active();
        if (up || down) {
            poseStack.pushPose();
            if (down) {
                renderSign(poseStack, builder, combinedLightIn, combinedOverlayIn, signDown, slim);
            }
            if (up) {
                poseStack.translate(0.0, 0.5, 0.0);
                renderSign(poseStack, builder, combinedLightIn, combinedOverlayIn, signUp, slim);
            }
            poseStack.popPose();
        }
    }

    public static void renderSign(PoseStack posestack, VertexConsumer builder, int light, int overlay, SignPostBlockTile.Sign sign, boolean slim) {
        posestack.pushPose();
        boolean left = sign.left();
        posestack.translate(0.5, 0.5, 0.5);
        posestack.mulPose(Axis.YP.rotationDegrees(sign.yaw() - 90.0F));
        if (slim) {
            posestack.translate(0.0F, 0.0F, -0.0625F);
        }
        if (!left) {
            posestack.mulPose(RotHlpr.YN180);
            posestack.translate(0.0, 0.0, -0.3125);
        }
        posestack.translate(-0.5, -0.5, -0.25);
        renderer.renderModel(posestack.last(), builder, null, (BakedModel) MODELS.get(sign.woodType()), 1.0F, 1.0F, 1.0F, light, overlay);
        posestack.popPose();
    }
}