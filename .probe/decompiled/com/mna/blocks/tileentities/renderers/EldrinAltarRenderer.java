package com.mna.blocks.tileentities.renderers;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.EldrinAltarTile;
import com.mna.gui.GuiTextures;
import com.mna.tools.math.MathUtils;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.ModelUtils;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
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

public class EldrinAltarRenderer implements BlockEntityRenderer<EldrinAltarTile> {

    private final ResourceLocation crystal = RLoc.create("block/eldrin/receiver_crystal");

    private final ItemRenderer itemRenderer;

    private final Minecraft mc;

    private static final Vec3 CrystalOffset = new Vec3(0.5, 1.1, 0.5);

    private static final Vec3 TileBeamOffset = new Vec3(0.0, 0.93, 0.0);

    private final EntityRenderDispatcher renderDispatcher;

    private final Font font;

    private float colorFlash = 255.0F;

    public EldrinAltarRenderer(BlockEntityRendererProvider.Context context) {
        this.mc = Minecraft.getInstance();
        this.itemRenderer = this.mc.getItemRenderer();
        this.renderDispatcher = this.mc.getEntityRenderDispatcher();
        this.font = this.mc.font;
    }

    public void render(EldrinAltarTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack itemstack = tileEntityIn.getDisplayedItem();
        Level world = tileEntityIn.m_58904_();
        BlockPos pos = tileEntityIn.m_58899_();
        BlockState state = tileEntityIn.m_58900_();
        if (!itemstack.isEmpty()) {
            this.renderItem(matrixStackIn, itemstack, combinedLightIn, bufferIn);
        }
        float partialTick = (float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks;
        this.colorFlash = (float) ((int) (255.0 - Math.abs(Math.sin((double) (partialTick / 30.0F))) * 200.0));
        Vec3 center = Vec3.atLowerCornerOf(pos);
        this.renderFloatingCrystals(tileEntityIn, partialTick, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, world, pos, state, center);
        this.renderCollectedItems(tileEntityIn, partialTick, matrixStackIn, bufferIn, combinedLightIn, center);
        this.renderPowerRequirements(tileEntityIn, partialTick, matrixStackIn, bufferIn, combinedLightIn, center);
        this.renderLastRecipe(tileEntityIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderLastRecipe(EldrinAltarTile tileEntityIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.m_7983_() && tileEntityIn.getStage() == EldrinAltarTile.Stage.IDLE && !tileEntityIn.getReCraftOutput().isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult brtr = (BlockHitResult) mc.hitResult;
                if (brtr.getBlockPos().equals(tileEntityIn.m_58899_())) {
                    List<ItemStack> itemstacks = tileEntityIn.getReCraftInput();
                    HashMap<Affinity, Float> eldrin = tileEntityIn.getReCraftEldrin();
                    float scale = 0.15F;
                    float xItemStep = 1.0F;
                    float xEldrinStep = 1.75F;
                    float xItemOffset = (float) (itemstacks.size() - 1) * xItemStep / 2.0F;
                    float xEldrinOffset = (float) (eldrin.size() - 1) * xEldrinStep / 2.0F;
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(0.5, 1.6, 0.5);
                    matrixStackIn.scale(scale, scale, scale);
                    this.renderString(Component.translatable("block.mna.manaweaving_altar.repeat").getString(), matrixStackIn, bufferIn, combinedLightIn);
                    String affStr = "";
                    for (Affinity aff : eldrin.keySet()) {
                        String s = String.format("%.0f", eldrin.get(aff));
                        int size = 8;
                        StringBuilder sb = new StringBuilder(size);
                        for (int i = 0; i < (size - s.length()) / 2; i++) {
                            sb.append(" ");
                        }
                        sb.append(s);
                        while (sb.length() < size) {
                            sb.append(" ");
                        }
                        affStr = affStr + sb.toString();
                    }
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(0.0, -2.4, 0.0);
                    this.renderString(affStr, matrixStackIn, bufferIn, combinedLightIn);
                    matrixStackIn.popPose();
                    matrixStackIn.pushPose();
                    matrixStackIn.mulPose(this.renderDispatcher.cameraOrientation());
                    matrixStackIn.translate(0.0, -0.75, 0.0);
                    this.itemRenderer.renderStatic(tileEntityIn.getReCraftOutput(), ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, mc.level, 0);
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(0.0, -0.75, 0.0);
                    scale = 0.5F;
                    matrixStackIn.scale(scale, scale, scale);
                    matrixStackIn.translate(xItemOffset, 0.0F, 0.0F);
                    for (int i = 0; i < itemstacks.size(); i++) {
                        ItemStack stack = (ItemStack) itemstacks.get(i);
                        this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, mc.level, 0);
                        matrixStackIn.translate(-xItemStep, 0.0F, 0.0F);
                    }
                    matrixStackIn.popPose();
                    matrixStackIn.translate(0.0, -1.35, 0.0);
                    matrixStackIn.scale(scale, scale, scale);
                    matrixStackIn.translate(xEldrinOffset, 0.0F, 0.0F);
                    for (Affinity aff : eldrin.keySet()) {
                        ItemStack stack = (ItemStack) GuiTextures.affinityIcons.get(aff);
                        this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, mc.level, 0);
                        this.renderString(String.format("%.0f", eldrin.get(aff)), matrixStackIn, bufferIn, combinedLightIn);
                        matrixStackIn.translate(-xEldrinStep, 0.0F, 0.0F);
                    }
                    matrixStackIn.popPose();
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
        this.font.drawInBatch(text, hOffset, 0.0F, 553648127, false, matrix4f, buffer, Font.DisplayMode.NORMAL, textColor, packedLight, false);
        this.font.drawInBatch(text, hOffset, 0.0F, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
        stack.popPose();
    }

    private void renderFloatingCrystals(EldrinAltarTile tileEntityIn, float partialTick, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, Level world, BlockPos pos, BlockState state, Vec3 center) {
        Vec3[] offsets = EldrinAltarTile.crystal_offsets;
        double[] yOffsets = new double[offsets.length];
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5, 0.0, 0.5);
        for (int i = 0; i < offsets.length; i++) {
            yOffsets[i] = Math.sin((double) (partialTick + (float) (i * 20)) * Math.PI / 180.0) / 15.0;
            matrixStackIn.pushPose();
            matrixStackIn.translate(offsets[i].x(), 1.0 + offsets[i].y() + yOffsets[i], offsets[i].z());
            ModelUtils.renderModel(bufferIn, world, pos, state, this.crystal, matrixStackIn, combinedLightIn, combinedOverlayIn);
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
        int ticksForEachBeam = 5;
        int stageTicks = tileEntityIn.getStageTicks();
        for (int i = 0; i < tileEntityIn.getActiveCrystals().size(); i++) {
            if (tileEntityIn.getStage() != EldrinAltarTile.Stage.CONSUMING_POWER || tileEntityIn.getStageTicks() >= i * ticksForEachBeam) {
                EldrinAltarTile.ActiveCrystal e = (EldrinAltarTile.ActiveCrystal) tileEntityIn.getActiveCrystals().get(i);
                Vec3 crystalPos = CrystalOffset.add(offsets[e.offsetIndex]).add(0.0, yOffsets[e.offsetIndex], 0.0);
                Vec3 tilePos = Vec3.atCenterOf(e.tilePos).subtract(center).add(TileBeamOffset);
                float minStageTicks = (float) (ticksForEachBeam * i);
                float maxStageTicks = minStageTicks + (float) ticksForEachBeam;
                float beamPct = 1.0F;
                if (tileEntityIn.getStage() == EldrinAltarTile.Stage.CONSUMING_POWER) {
                    if ((float) stageTicks < minStageTicks) {
                        continue;
                    }
                    beamPct = MathUtils.clamp01(((float) stageTicks + partialTicks) / maxStageTicks);
                }
                if (beamPct > 0.0F) {
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(tilePos.x, tilePos.y, tilePos.z);
                    WorldRenderUtils.renderBeam(world, partialTicks, matrixStackIn, bufferIn, combinedLightIn, tilePos, crystalPos, beamPct, e.affinity.getShiftAffinity().getColor(), 0.05F, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
                    matrixStackIn.popPose();
                }
            }
        }
        if (tileEntityIn.getStage() == EldrinAltarTile.Stage.COMPLETING && tileEntityIn.getStageTicks() > 80) {
            float stagePlusPartial = (float) tileEntityIn.getStageTicks() + partialTicks;
            float pct = MathUtils.clamp01((stagePlusPartial - 80.0F) / 20.0F);
            int alpha = (int) (176.0F * pct);
            float scale = 0.01F + 2.74F * pct;
            Vec3 relativeCenterPos = new Vec3(0.5, 1.5, 0.5);
            for (int ix = 0; ix < tileEntityIn.getActiveCrystals().size(); ix++) {
                EldrinAltarTile.ActiveCrystal ex = (EldrinAltarTile.ActiveCrystal) tileEntityIn.getActiveCrystals().get(ix);
                Vec3 crystalPosx = CrystalOffset.add(offsets[ex.offsetIndex]).add(0.0, yOffsets[ex.offsetIndex], 0.0);
                matrixStackIn.pushPose();
                matrixStackIn.translate(crystalPosx.x, crystalPosx.y, crystalPosx.z);
                WorldRenderUtils.renderBeam(world, partialTicks, matrixStackIn, bufferIn, combinedLightIn, crystalPosx, relativeCenterPos, pct, ex.affinity.getShiftAffinity().getColor(), 0.05F, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
                matrixStackIn.popPose();
            }
            int[] innerColor = new int[] { 7, 43, 155 };
            int[] outerColor = new int[] { 20, 40, 150 };
            matrixStackIn.pushPose();
            matrixStackIn.translate(relativeCenterPos.x, relativeCenterPos.y, relativeCenterPos.z);
            WorldRenderUtils.renderRadiant((float) stageTicks, matrixStackIn, bufferIn, innerColor, outerColor, alpha, scale, false);
            matrixStackIn.popPose();
        }
    }

    private void renderCollectedItems(EldrinAltarTile tileEntityIn, float partialTick, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, Vec3 center) {
        if (tileEntityIn.getStage() == EldrinAltarTile.Stage.CONSUMING_REAGENTS) {
            float rotation = (float) tileEntityIn.getStageTicks() + partialTick;
            int maxItems = tileEntityIn.getCollectedItems().size() - tileEntityIn.getStageTicks() / 20;
            float yOffset = 0.53F + 0.43F * MathUtils.clamp01((float) tileEntityIn.getStageTicks() / 10.0F);
            for (int i = 0; i < maxItems; i++) {
                EldrinAltarTile.CollectedItem item = (EldrinAltarTile.CollectedItem) tileEntityIn.getCollectedItems().get(i);
                Vec3 tilePos = Vec3.atCenterOf(item.tilePos).subtract(center).add(0.0, (double) yOffset, 0.0);
                matrixStackIn.pushPose();
                matrixStackIn.translate(tilePos.x, tilePos.y, tilePos.z);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(i % 4 == 0 ? -rotation : rotation));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(i % 2 == 0 ? rotation : -rotation));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(i % 3 == 0 ? -rotation : rotation));
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
                this.itemRenderer.renderStatic(item.stack, ItemDisplayContext.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
                matrixStackIn.popPose();
            }
        }
    }

    private void renderItem(PoseStack matrixStackIn, ItemStack itemstack, int combinedLightIn, MultiBufferSource bufferIn) {
        matrixStackIn.pushPose();
        if (itemstack.getItem() instanceof BlockItem) {
            matrixStackIn.translate(0.5, 1.05, 0.5);
        } else {
            matrixStackIn.translate(0.5, 1.0, 0.5);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        }
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
        matrixStackIn.popPose();
    }

    private void renderPowerRequirements(EldrinAltarTile tileEntityIn, float partialTick, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, Vec3 center) {
        if (tileEntityIn.getStage() == EldrinAltarTile.Stage.CONSUMING_POWER) {
            List<Affinity> missingPower = (List<Affinity>) tileEntityIn.getPowerRequirementStatus().entrySet().stream().filter(e -> !(Boolean) e.getValue()).map(e -> (Affinity) e.getKey()).collect(Collectors.toList());
            double xOffset = 0.5;
            double xPadding = 0.2;
            double hOffset = (double) (missingPower.size() - 1) * xOffset + (double) (missingPower.size() - 1) * xPadding;
            Quaternionf cameraRotation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
            Quaternionf badgeRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 1.5, 0.5);
            matrixStackIn.mulPose(badgeRotation);
            matrixStackIn.translate(-hOffset / 2.0, 0.0, 0.0);
            missingPower.forEach(e -> {
                this.renderAffinityBadge(matrixStackIn, bufferIn, e, combinedLightIn);
                matrixStackIn.translate(xOffset + xPadding, 0.0, 0.0);
            });
            matrixStackIn.popPose();
        }
    }

    private void renderAffinityBadge(PoseStack matrixStackIn, MultiBufferSource bufferIn, Affinity affinity, int packedLightIn) {
        float scaleFactor = 0.5F;
        VertexConsumer vertexBuilder = bufferIn.getBuffer(RenderType.entityTranslucent((ResourceLocation) GuiTextures.affinityBadges.get(affinity)));
        matrixStackIn.pushPose();
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f renderMatrix = matrixstack$entry.pose();
        Matrix3f normalMatrix = matrixstack$entry.normal();
        float nrmV = 1.0F;
        float nrmH = 1.0F;
        this.addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV);
        this.addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV);
        this.addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV);
        this.addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV);
        matrixStackIn.popPose();
    }

    private void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, int packedLight, float x, float y, float u, float v, float nrmH, float nrmV) {
        vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(255, (int) this.colorFlash, (int) this.colorFlash, 230).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, nrmH, nrmV, nrmH).endVertex();
    }
}