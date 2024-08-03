package me.jellysquid.mods.sodium.mixin.features.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.common.ColorVertex;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.server.level.progress.StoringChunkProgressListener;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ LevelLoadingScreen.class })
public class LevelLoadingScreenMixin {

    @Mutable
    @Shadow
    @Final
    private static Object2IntMap<ChunkStatus> COLORS;

    @Unique
    private static Reference2IntOpenHashMap<ChunkStatus> STATUS_TO_COLOR_FAST;

    @Unique
    private static final int NULL_STATUS_COLOR = ColorABGR.pack(0, 0, 0, 255);

    @Unique
    private static final int DEFAULT_STATUS_COLOR = ColorARGB.pack(0, 17, 255, 255);

    @Overwrite
    public static void renderChunks(GuiGraphics drawContext, StoringChunkProgressListener tracker, int mapX, int mapY, int mapScale, int mapPadding) {
        if (STATUS_TO_COLOR_FAST == null) {
            STATUS_TO_COLOR_FAST = new Reference2IntOpenHashMap(COLORS.size());
            STATUS_TO_COLOR_FAST.put(null, NULL_STATUS_COLOR);
            COLORS.object2IntEntrySet().forEach(entry -> STATUS_TO_COLOR_FAST.put((ChunkStatus) entry.getKey(), ColorARGB.toABGR(entry.getIntValue(), 255)));
        }
        RenderSystem.setShader(GameRenderer::m_172811_);
        Matrix4f matrix = drawContext.pose().last().pose();
        Tesselator tessellator = Tesselator.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        VertexBufferWriter writer = VertexBufferWriter.of(bufferBuilder);
        int centerSize = tracker.getFullDiameter();
        int size = tracker.getDiameter();
        int tileSize = mapScale + mapPadding;
        if (mapPadding != 0) {
            int mapRenderCenterSize = centerSize * tileSize - mapPadding;
            int radius = mapRenderCenterSize / 2 + 1;
            addRect(writer, matrix, mapX - radius, mapY - radius, mapX - radius + 1, mapY + radius, DEFAULT_STATUS_COLOR);
            addRect(writer, matrix, mapX + radius - 1, mapY - radius, mapX + radius, mapY + radius, DEFAULT_STATUS_COLOR);
            addRect(writer, matrix, mapX - radius, mapY - radius, mapX + radius, mapY - radius + 1, DEFAULT_STATUS_COLOR);
            addRect(writer, matrix, mapX - radius, mapY + radius - 1, mapX + radius, mapY + radius, DEFAULT_STATUS_COLOR);
        }
        int mapRenderSize = size * tileSize - mapPadding;
        int mapStartX = mapX - mapRenderSize / 2;
        int mapStartY = mapY - mapRenderSize / 2;
        ChunkStatus prevStatus = null;
        int prevColor = NULL_STATUS_COLOR;
        for (int x = 0; x < size; x++) {
            int tileX = mapStartX + x * tileSize;
            for (int z = 0; z < size; z++) {
                int tileY = mapStartY + z * tileSize;
                ChunkStatus status = tracker.getStatus(x, z);
                int color;
                if (prevStatus == status) {
                    color = prevColor;
                } else {
                    color = STATUS_TO_COLOR_FAST.getInt(status);
                    prevStatus = status;
                    prevColor = color;
                }
                addRect(writer, matrix, tileX, tileY, tileX + mapScale, tileY + mapScale, color);
            }
        }
        tessellator.end();
        RenderSystem.disableBlend();
    }

    @Unique
    private static void addRect(VertexBufferWriter writer, Matrix4f matrix, int x1, int y1, int x2, int y2, int color) {
        MemoryStack stack = MemoryStack.stackPush();
        try {
            long buffer = stack.nmalloc(64);
            ColorVertex.put(buffer, matrix, (float) x1, (float) y2, 0.0F, color);
            long ptr = buffer + 16L;
            ColorVertex.put(ptr, matrix, (float) x2, (float) y2, 0.0F, color);
            ptr += 16L;
            ColorVertex.put(ptr, matrix, (float) x2, (float) y1, 0.0F, color);
            ptr += 16L;
            ColorVertex.put(ptr, matrix, (float) x1, (float) y1, 0.0F, color);
            ptr += 16L;
            writer.push(stack, buffer, 4, ColorVertex.FORMAT);
        } catch (Throwable var13) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var12) {
                    var13.addSuppressed(var12);
                }
            }
            throw var13;
        }
        if (stack != null) {
            stack.close();
        }
    }
}