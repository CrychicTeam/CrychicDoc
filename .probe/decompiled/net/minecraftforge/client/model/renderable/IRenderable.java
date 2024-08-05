package net.minecraftforge.client.model.renderable;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Unit;

@FunctionalInterface
public interface IRenderable<T> {

    void render(PoseStack var1, MultiBufferSource var2, ITextureRenderTypeLookup var3, int var4, int var5, float var6, T var7);

    default IRenderable<Unit> withContext(T context) {
        return (poseStack, bufferSource, textureRenderTypeLookup, lightmap, overlay, partialTick, unused) -> this.render(poseStack, bufferSource, textureRenderTypeLookup, lightmap, overlay, partialTick, context);
    }
}