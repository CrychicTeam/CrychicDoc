package me.jellysquid.mods.sodium.mixin.modcompat.fabric_renderer_indigo;

import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.ItemRenderContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.embeddedt.embeddium.render.frapi.SpriteFinderCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { ItemRenderContext.class }, remap = false)
public class ItemRenderContextMixin {

    @Inject(method = { "renderQuad" }, at = { @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/impl/client/indigo/renderer/mesh/MutableQuadViewImpl;material()Lnet/fabricmc/fabric/impl/client/indigo/renderer/material/RenderMaterialImpl;") }, require = 0)
    private void captureTexture(@Coerce MutableQuadView quad, CallbackInfo ci) {
        float midU = 0.0F;
        float midV = 0.0F;
        for (int i = 0; i < 4; i++) {
            midU += quad.u(i);
            midV += quad.v(i);
        }
        TextureAtlasSprite sprite = SpriteFinderCache.forBlockAtlas().findNearestSprite(midU / 4.0F, midV / 4.0F);
        if (sprite != null) {
            SpriteUtil.markSpriteActive(sprite);
        }
    }
}