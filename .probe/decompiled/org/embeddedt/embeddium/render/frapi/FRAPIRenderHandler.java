package org.embeddedt.embeddium.render.frapi;

import com.mojang.blaze3d.vertex.PoseStack;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import net.minecraft.util.RandomSource;
import org.joml.Vector3fc;

public interface FRAPIRenderHandler {

    boolean INDIGO_PRESENT = isIndigoPresent();

    private static boolean isIndigoPresent() {
        boolean indigoPresent = false;
        try {
            Class.forName("net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderContext");
            indigoPresent = true;
        } catch (Throwable var2) {
        }
        return indigoPresent;
    }

    void reset();

    void renderEmbeddium(BlockRenderContext var1, PoseStack var2, RandomSource var3);

    void flush(ChunkBuildBuffers var1, Vector3fc var2);
}