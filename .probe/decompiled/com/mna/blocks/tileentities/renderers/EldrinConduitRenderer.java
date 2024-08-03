package com.mna.blocks.tileentities.renderers;

import com.mna.ManaAndArtifice;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.EldrinConduitTile;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EldrinConduitRenderer implements BlockEntityRenderer<EldrinConduitTile> {

    private final ResourceLocation inner = RLoc.create("block/eldrin/supplier_lower_interior");

    private final ResourceLocation frame = RLoc.create("block/eldrin/supplier_lower_exterior");

    private final ResourceLocation crystal = RLoc.create("block/eldrin/supplier_upper");

    private static final Random RANDOM = new Random(31100L);

    public EldrinConduitRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(EldrinConduitTile tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tileEntityIn.isLesser()) {
            Level world = tileEntityIn.m_58904_();
            BlockPos pos = tileEntityIn.m_58899_();
            BlockState state = tileEntityIn.m_58900_();
            float partialTick = (float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks;
            float radians = (float) ((double) partialTick * Math.PI / 180.0);
            matrixStack.pushPose();
            matrixStack.translate(0.5F, 0.75F, 0.5F);
            matrixStack.mulPose(Axis.YP.rotation(radians));
            float scale = 0.6F;
            matrixStack.scale(scale, scale, scale);
            float colorMod = 0.15F;
            this.renderModelWithRandomColor(matrixStack, bufferIn.getBuffer(RenderType.endPortal()), world, pos, state, colorMod, combinedLightIn, combinedOverlayIn);
            ModelUtils.renderModel(bufferIn, world, pos, state, this.frame, matrixStack, combinedLightIn, combinedOverlayIn);
            matrixStack.mulPose(Axis.YP.rotation(-radians * 2.0F));
            ModelUtils.renderModel(bufferIn, world, pos, state, this.crystal, matrixStack, combinedLightIn, combinedOverlayIn);
            matrixStack.popPose();
        }
    }

    private void renderModelWithRandomColor(PoseStack stack, VertexConsumer builder, Level world, BlockPos pos, BlockState state, float colorMod, int light, int overlay) {
        float r = (RANDOM.nextFloat() * 0.5F + 0.1F) * colorMod;
        float g = (RANDOM.nextFloat() * 0.5F + 0.4F) * colorMod;
        float b = (RANDOM.nextFloat() * 0.5F + 0.5F) * colorMod;
        ModelUtils.renderModel(builder, world, pos, state, this.inner, stack, new float[] { 1.0F, r, g, b }, light, overlay);
    }
}