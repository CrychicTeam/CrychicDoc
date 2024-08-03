package com.mna.blocks.tileentities.renderers;

import com.mna.ManaAndArtifice;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.api.tools.RLoc;
import com.mna.blocks.manaweaving.ManaweavingAltarBlock;
import com.mna.blocks.tileentities.ManaweavingAltarTile;
import com.mna.tools.math.MathUtils;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ManaweavingAltarRenderer implements BlockEntityRenderer<ManaweavingAltarTile> {

    private static final ResourceLocation SPARKLE_TEXTURE = RLoc.create("textures/particle/sparkle.png");

    private static final ResourceLocation CRYSTAL_A = RLoc.create("block/manaweaving_altar_crystal_a");

    private static final ResourceLocation CRYSTAL_B = RLoc.create("block/manaweaving_altar_crystal_b");

    private static final ResourceLocation CRYSTAL_C = RLoc.create("block/manaweaving_altar_crystal_c");

    private static final RenderType RENDER_TYPE = RenderType.entityTranslucentCull(SPARKLE_TEXTURE);

    private final Minecraft mc = Minecraft.getInstance();

    private final ItemRenderer itemRenderer = this.mc.getItemRenderer();

    private final EntityRenderDispatcher renderDispatcher = this.mc.getEntityRenderDispatcher();

    private final Font font;

    public ManaweavingAltarRenderer(BlockEntityRendererProvider.Context context) {
        this.font = this.mc.font;
    }

    public void render(ManaweavingAltarTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float yFloatA = (float) Math.sin((double) (((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks) / 40.0F)) * 0.01F;
        float yFloatB = (float) Math.sin((double) (((float) (ManaAndArtifice.instance.proxy.getGameTicks() + 40L) + partialTicks) / 40.0F)) * 0.01F;
        float yFloatC = (float) Math.sin((double) (((float) (ManaAndArtifice.instance.proxy.getGameTicks() + 80L) + partialTicks) / 40.0F)) * 0.01F;
        this.renderModelExtras(tileEntityIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, yFloatA, yFloatB, yFloatC);
        this.renderStacks(tileEntityIn, matrixStackIn, bufferIn, combinedLightIn, partialTicks);
        this.renderPatterns(tileEntityIn, matrixStackIn, bufferIn, yFloatA, yFloatB, yFloatC);
        this.renderLastRecipe(tileEntityIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderModelExtras(ManaweavingAltarTile tileEntityIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, float yFloatA, float yFloatB, float yFloatC) {
        Level world = tileEntityIn.m_58904_();
        BlockPos pos = tileEntityIn.m_58899_();
        BlockState state = tileEntityIn.m_58900_();
        matrixStackIn.pushPose();
        switch((Direction) state.m_61143_(ManaweavingAltarBlock.FACING)) {
            case EAST:
                matrixStackIn.translate(1.0F, 0.0F, 0.0F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-90.0F));
                break;
            case SOUTH:
                matrixStackIn.translate(1.0F, 0.0F, 1.0F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
                break;
            case WEST:
                matrixStackIn.translate(0.0F, 0.0F, 1.0F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
        }
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0F, yFloatA, 0.0F);
        ModelUtils.renderModel(bufferIn, world, pos, state, CRYSTAL_A, matrixStackIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0F, yFloatB, 0.0F);
        ModelUtils.renderModel(bufferIn, world, pos, state, CRYSTAL_B, matrixStackIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0F, yFloatC, 0.0F);
        ModelUtils.renderModel(bufferIn, world, pos, state, CRYSTAL_C, matrixStackIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }

    private void renderLastRecipe(ManaweavingAltarTile tileEntityIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.m_7983_() && tileEntityIn.getAddedPatterns().size() == 0 && !tileEntityIn.getReCraftOutput().isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult brtr = (BlockHitResult) mc.hitResult;
                if (brtr.getBlockPos().equals(tileEntityIn.m_58899_())) {
                    List<ItemStack> itemstacks = tileEntityIn.getReCraftInput();
                    float scale = 0.15F;
                    float xStep = 1.0F;
                    float xOffset = (float) (itemstacks.size() - 1) * xStep / 2.0F;
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(0.5, 1.3, 0.5);
                    matrixStackIn.scale(scale, scale, scale);
                    this.renderString(Component.translatable("block.mna.manaweaving_altar.repeat").getString(), matrixStackIn, bufferIn, combinedLightIn);
                    matrixStackIn.pushPose();
                    matrixStackIn.mulPose(this.renderDispatcher.cameraOrientation());
                    matrixStackIn.translate(0.0, -0.75, 0.0);
                    this.itemRenderer.renderStatic(tileEntityIn.getReCraftOutput(), ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, mc.level, 0);
                    matrixStackIn.translate(0.0, -0.75, 0.0);
                    scale = 0.5F;
                    matrixStackIn.scale(scale, scale, scale);
                    matrixStackIn.translate(xOffset, 0.0F, 0.0F);
                    for (int i = 0; i < itemstacks.size(); i++) {
                        ItemStack stack = (ItemStack) itemstacks.get(i);
                        this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, mc.level, 0);
                        matrixStackIn.translate(-xStep, 0.0F, 0.0F);
                    }
                    matrixStackIn.popPose();
                    matrixStackIn.translate(0.0, -1.95, 0.0);
                    this.renderString(Component.translatable("block.mna.manaweaving_altar.repeat_mana_cost", tileEntityIn.getReCraftManaCost()).getString(), matrixStackIn, bufferIn, combinedLightIn);
                    matrixStackIn.popPose();
                }
            }
        }
    }

    protected void renderString(String text, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.mulPose(this.renderDispatcher.cameraOrientation());
        stack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = stack.last().pose();
        float opacity = 0.25F;
        int textColor = (int) (opacity * 255.0F) << 24;
        float hOffset = (float) (-this.font.width(text) / 2);
        this.font.drawInBatch(text, hOffset, 0.0F, 553648127, false, matrix4f, buffer, Font.DisplayMode.NORMAL, textColor, packedLight);
        this.font.drawInBatch(text, hOffset, 0.0F, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
        stack.popPose();
    }

    private void renderStacks(ManaweavingAltarTile tileEntityIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, float partialTicks) {
        ItemStack[] itemstacks = tileEntityIn.getDisplayedItems();
        for (int i = 0; i < itemstacks.length; i++) {
            Random r = new Random((long) i * 1234L);
            if (!itemstacks[i].isEmpty()) {
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.5, 1.3, 0.5);
                Vec3 randomPos = new Vec3(r.nextGaussian() * Math.sin((double) ((float) i / 4.5F)), r.nextGaussian() * Math.sin((double) ((float) i / 4.5F)), r.nextGaussian() * Math.sin((double) ((float) i / 4.5F))).normalize();
                randomPos = randomPos.scale(0.3F);
                if (!tileEntityIn.isCrafting()) {
                    matrixStackIn.translate(randomPos.x(), randomPos.y(), randomPos.z());
                } else {
                    float ringAngle = (float) i * (360.0F / (float) itemstacks.length);
                    float craftPct = (float) tileEntityIn.getCraftTicks() / (float) tileEntityIn.getMaxCraftTicks();
                    Vec3 ringPos = new Vec3(Math.cos((double) ringAngle), 0.0, Math.sin((double) ringAngle)).scale((double) (1.0F - craftPct));
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks));
                    if (tileEntityIn.getCraftTicks() < tileEntityIn.getMaxCraftTicks() / 5) {
                        float pct = (float) tileEntityIn.getCraftTicks() / ((float) tileEntityIn.getMaxCraftTicks() / 5.0F);
                        Vec3 lerpPos = MathUtils.lerpVector3d(randomPos, ringPos, pct);
                        matrixStackIn.translate(lerpPos.x(), lerpPos.y(), lerpPos.z());
                    } else {
                        matrixStackIn.translate(ringPos.x(), ringPos.y(), ringPos.z());
                    }
                }
                matrixStackIn.mulPose(Axis.XP.rotationDegrees((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks));
                matrixStackIn.mulPose(Axis.YP.rotationDegrees((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks));
                matrixStackIn.scale(0.25F, 0.25F, 0.25F);
                this.itemRenderer.renderStatic(itemstacks[i], ItemDisplayContext.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
                matrixStackIn.popPose();
            }
        }
    }

    private void renderPatterns(ManaweavingAltarTile tileEntityIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, float yFloatA, float yFloatB, float yFloatC) {
        int count = 7;
        float radius = 0.375F;
        float rotation = 0.0F;
        float rotationBase = 0.0F;
        float[] yOffsets = new float[] { 0.8375F, 0.91F, 0.9525F };
        switch((Direction) tileEntityIn.m_58900_().m_61143_(ManaweavingAltarBlock.FACING)) {
            case EAST:
                rotation = 90.0F;
                break;
            case SOUTH:
                rotation = 180.0F;
                rotationBase = 180.0F;
                break;
            case WEST:
                rotation = -90.0F;
                break;
            case NORTH:
            default:
                rotationBase = 180.0F;
        }
        for (IManaweavePattern pattern : tileEntityIn.getAddedPatterns()) {
            float yOffset = count == 1 || count == 7 ? yOffsets[0] + yFloatA : (count != 2 && count != 6 ? yOffsets[2] + yFloatC : yOffsets[1] + yFloatB);
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5F, 0.0F, 0.5F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
            matrixStackIn.translate(-0.5F, 0.0F, -0.5F);
            Vector3f vec = new Vector3f(0.0F, yOffset, radius);
            vec.rotate(Axis.YP.rotationDegrees(rotationBase + (float) (45 * count)));
            matrixStackIn.translate(vec.x() + 0.5F, vec.y(), vec.z() + 0.5F);
            this.renderPattern(pattern, matrixStackIn, bufferIn, rotation);
            matrixStackIn.popPose();
            if (--count == 4) {
                count--;
            }
        }
    }

    private void renderPattern(IManaweavePattern pattern, PoseStack matrixStackIn, MultiBufferSource bufferIn, float rotation) {
        byte[][] points = pattern.get();
        float pointSize = 0.5F;
        float baseScale = 0.03F;
        float offsetX = (float) points.length / 2.0F * pointSize;
        float offsetY = (float) points[0].length / 2.0F * pointSize;
        Quaternionf cameraRotation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0, 0.2, 0.0);
        matrixStackIn.scale(baseScale, baseScale, baseScale);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-rotation));
        matrixStackIn.mulPose(cameraRotation);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f renderMatrix = matrixstack$entry.pose();
        Matrix3f normalMatrix = matrixstack$entry.normal();
        VertexConsumer vertexBuilder = bufferIn.getBuffer(RENDER_TYPE);
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j] == 1) {
                    float originX = offsetX - (float) j * pointSize;
                    float originY = offsetY - (float) i * pointSize;
                    addVertex(vertexBuilder, renderMatrix, normalMatrix, 0.0F + originX, 0.0F + originY, 0.0F, 1.0F);
                    addVertex(vertexBuilder, renderMatrix, normalMatrix, 1.0F + originX, 0.0F + originY, 1.0F, 1.0F);
                    addVertex(vertexBuilder, renderMatrix, normalMatrix, 1.0F + originX, 1.0F + originY, 1.0F, 0.0F);
                    addVertex(vertexBuilder, renderMatrix, normalMatrix, 0.0F + originX, 1.0F + originY, 0.0F, 0.0F);
                }
            }
        }
        matrixStackIn.popPose();
    }

    private static void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, float x, float y, float u, float v) {
        vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(109, 227, 220, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728640).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
    }
}