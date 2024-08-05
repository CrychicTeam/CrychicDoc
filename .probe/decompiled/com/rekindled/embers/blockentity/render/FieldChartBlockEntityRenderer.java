package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.EmbersClientEvents;
import com.rekindled.embers.block.FieldChartBlock;
import com.rekindled.embers.blockentity.FieldChartBlockEntity;
import com.rekindled.embers.render.EmbersRenderTypes;
import com.rekindled.embers.util.EmberGenUtil;
import com.rekindled.embers.util.Misc;
import java.awt.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class FieldChartBlockEntityRenderer implements BlockEntityRenderer<FieldChartBlockEntity> {

    public static float baseHeight = 0.375F;

    public static float height = 0.55F;

    public FieldChartBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(FieldChartBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null) {
            VertexConsumer buffer = bufferSource.getBuffer(ConfigManager.RENDER_FALLBACK.get() ? EmbersRenderTypes.FIELD_CHART_FALLBACK : EmbersRenderTypes.FIELD_CHART);
            RenderSystem.enableDepthTest();
            RenderSystem.disableCull();
            BlockState state = blockEntity.m_58904_().getBlockState(blockEntity.m_58899_());
            if (state.m_61138_(FieldChartBlock.INVERTED) && (Boolean) state.m_61143_(FieldChartBlock.INVERTED)) {
                this.renderChart(blockEntity.m_58904_(), blockEntity.m_58899_(), 0.0F, 0.0F, 0.0F, buffer, poseStack.last().pose(), (cx, cz) -> EmberGenUtil.getEmberStability(EmbersClientEvents.seed, cx, cz), new Color(16, 64, 255), new Color(16, 192, 255), new Color(8, 255, 255));
            } else {
                this.renderChart(blockEntity.m_58904_(), blockEntity.m_58899_(), 0.0F, 0.0F, 0.0F, buffer, poseStack.last().pose(), (cx, cz) -> EmberGenUtil.getEmberDensity(EmbersClientEvents.seed, cx, cz), new Color(255, 64, 16), new Color(255, 192, 16), new Color(255, 255, 8));
            }
            RenderSystem.enableCull();
        }
    }

    public void renderChart(Level level, BlockPos pos, float x, float y, float z, VertexConsumer buffer, Matrix4f matrix4f, FieldChartBlockEntityRenderer.IChartSource source, Color color1, Color color2, Color color3) {
        int signal = level.m_277086_(pos);
        float brightness = 1.0F;
        if (signal > 2) {
            brightness = Misc.getLightBrightness(15 - signal, EmbersClientEvents.ticks);
        }
        float red1 = brightness * (float) color1.getRed() / 255.0F;
        float green1 = brightness * (float) color1.getGreen() / 255.0F;
        float blue1 = brightness * (float) color1.getBlue() / 255.0F;
        float[][][] valueCache = new float[10][10][4];
        for (float i = -160.0F; i < 160.0F; i += 32.0F) {
            for (float j = -160.0F; j < 160.0F; j += 32.0F) {
                float[] values = new float[] { source.get(pos.m_123341_() + (int) i / 2, pos.m_123343_() + (int) j / 2), source.get(pos.m_123341_() + (int) i / 2 + 16, pos.m_123343_() + (int) j / 2), source.get(pos.m_123341_() + (int) i / 2 + 16, pos.m_123343_() + (int) j / 2 + 16), source.get(pos.m_123341_() + (int) i / 2, pos.m_123343_() + (int) j / 2 + 16) };
                valueCache[(int) (i / 32.0F) + 5][(int) (j / 32.0F) + 5] = values;
                float alphaul = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i), Math.abs(j)) / 160.0F));
                float alphaur = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i + 32.0F), Math.abs(j)) / 160.0F));
                float alphadr = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i + 32.0F), Math.abs(j + 32.0F)) / 160.0F));
                float alphadl = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i), Math.abs(j + 32.0F)) / 160.0F));
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F), y + baseHeight + values[0] * height, z + 0.5F + 1.25F * (j / 160.0F)).uv(0.0F, 0.0F).color(red1 * alphaul, green1 * alphaul, blue1 * alphaul, 1.0F).endVertex();
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F) + 0.25F, y + baseHeight + values[1] * height, z + 0.5F + 1.25F * (j / 160.0F)).uv(1.0F, 0.0F).color(red1 * alphaur, green1 * alphaur, blue1 * alphaur, 1.0F).endVertex();
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F) + 0.25F, y + baseHeight + values[2] * height, z + 0.5F + 1.25F * (j / 160.0F) + 0.25F).uv(1.0F, 1.0F).color(red1 * alphadr, green1 * alphadr, blue1 * alphadr, 1.0F).endVertex();
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F), y + baseHeight + values[3] * height, z + 0.5F + 1.25F * (j / 160.0F) + 0.25F).uv(0.0F, 1.0F).color(red1 * alphadl, green1 * alphadl, blue1 * alphadl, 1.0F).endVertex();
            }
        }
        float red2 = brightness * (float) color2.getRed() / 255.0F;
        float green2 = brightness * (float) color2.getGreen() / 255.0F;
        float blue2 = brightness * (float) color2.getBlue() / 255.0F;
        for (float i = -160.0F; i < 160.0F; i += 32.0F) {
            for (float j = -160.0F; j < 160.0F; j += 32.0F) {
                float[] values = valueCache[(int) (i / 32.0F) + 5][(int) (j / 32.0F) + 5];
                float amountul = values[0];
                float amountur = values[1];
                float amountdr = values[2];
                float amountdl = values[3];
                float alphaul = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i), Math.abs(j)) / 160.0F) * amountul * amountul) * 0.875F;
                float alphaur = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i + 32.0F), Math.abs(j)) / 160.0F) * amountur * amountur) * 0.875F;
                float alphadr = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i + 32.0F), Math.abs(j + 32.0F)) / 160.0F) * amountdr * amountdr) * 0.875F;
                float alphadl = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i), Math.abs(j + 32.0F)) / 160.0F) * amountdl * amountdl) * 0.875F;
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F), y + baseHeight + amountul * height, z + 0.5F + 1.25F * (j / 160.0F)).uv(0.0F, 0.0F).color(red2 * alphaul, green2 * alphaul, blue2 * alphaul, 1.0F).endVertex();
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F) + 0.25F, y + baseHeight + amountur * height, z + 0.5F + 1.25F * (j / 160.0F)).uv(1.0F, 0.0F).color(red2 * alphaur, green2 * alphaur, blue2 * alphaur, 1.0F).endVertex();
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F) + 0.25F, y + baseHeight + amountdr * height, z + 0.5F + 1.25F * (j / 160.0F) + 0.25F).uv(1.0F, 1.0F).color(red2 * alphadr, green2 * alphadr, blue2 * alphadr, 1.0F).endVertex();
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F), y + baseHeight + amountdl * height, z + 0.5F + 1.25F * (j / 160.0F) + 0.25F).uv(0.0F, 1.0F).color(red2 * alphadl, green2 * alphadl, blue2 * alphadl, 1.0F).endVertex();
            }
        }
        float red3 = brightness * (float) color3.getRed() / 255.0F;
        float green3 = brightness * (float) color3.getGreen() / 255.0F;
        float blue3 = brightness * (float) color3.getBlue() / 255.0F;
        for (float i = -160.0F; i < 160.0F; i += 32.0F) {
            for (float j = -160.0F; j < 160.0F; j += 32.0F) {
                float[] values = valueCache[(int) (i / 32.0F) + 5][(int) (j / 32.0F) + 5];
                float amountul = values[0];
                float amountur = values[1];
                float amountdr = values[2];
                float amountdl = values[3];
                float alphaul = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i), Math.abs(j)) / 160.0F) * amountul * amountul * amountul);
                float alphaur = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i + 32.0F), Math.abs(j)) / 160.0F) * amountur * amountur * amountur);
                float alphadr = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i + 32.0F), Math.abs(j + 32.0F)) / 160.0F) * amountdr * amountdr * amountdr);
                float alphadl = Math.min(1.0F, Math.max(0.0F, 1.0F - Math.max(Math.abs(i), Math.abs(j + 32.0F)) / 160.0F) * amountdl * amountdl * amountdl);
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F), y + baseHeight + amountul * height, z + 0.5F + 1.25F * (j / 160.0F)).uv(0.0F, 0.0F).color(red3 * alphaul, green3 * alphaul, blue3 * alphaul, 1.0F).endVertex();
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F) + 0.25F, y + baseHeight + amountur * height, z + 0.5F + 1.25F * (j / 160.0F)).uv(1.0F, 0.0F).color(red3 * alphaur, green3 * alphaur, blue3 * alphaur, 1.0F).endVertex();
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F) + 0.25F, y + baseHeight + amountdr * height, z + 0.5F + 1.25F * (j / 160.0F) + 0.25F).uv(1.0F, 1.0F).color(red3 * alphadr, green3 * alphadr, blue3 * alphadr, 1.0F).endVertex();
                buffer.vertex(matrix4f, x + 0.5F + 1.25F * (i / 160.0F), y + baseHeight + amountdl * height, z + 0.5F + 1.25F * (j / 160.0F) + 0.25F).uv(0.0F, 1.0F).color(red3 * alphadl, green3 * alphadl, blue3 * alphadl, 1.0F).endVertex();
            }
        }
    }

    interface IChartSource {

        float get(int var1, int var2);
    }
}