package fr.lucreeper74.createmetallurgy.utils;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class CastingItemRenderTypeBuffer implements MultiBufferSource {

    private final MultiBufferSource inner;

    private final int alpha;

    private final int red;

    private final int green;

    private final int blue;

    public CastingItemRenderTypeBuffer(MultiBufferSource inner, int alphaItem, int alphaFluid) {
        this.inner = inner;
        this.alpha = Mth.clamp(alphaItem, 0, 255);
        alphaFluid = Mth.clamp(alphaFluid, 0, 255);
        this.red = 255 - alphaFluid * 79 / 255;
        this.green = 255 - alphaFluid * 159 / 255;
        this.blue = 255 - alphaFluid * 223 / 255;
    }

    @NotNull
    @Override
    public VertexConsumer getBuffer(RenderType type) {
        if (this.alpha < 255) {
            type = RenderType.translucent();
        }
        return new TintedVertexBuilder(this.inner.getBuffer(type), this.red, this.green, this.blue, this.alpha);
    }
}