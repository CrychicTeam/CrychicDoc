package me.jellysquid.mods.sodium.mixin.features.render.immediate.buffer_builder.intrinsics;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.render.immediate.model.BakedModelEncoder;
import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ BufferBuilder.class })
public abstract class BufferBuilderMixin extends DefaultedVertexConsumer {

    @Shadow
    private boolean fastFormat;

    @Override
    public void putBulkData(PoseStack.Pose matrices, BakedQuad bakedQuad, float r, float g, float b, int light, int overlay) {
        if (!this.fastFormat) {
            super.m_85987_(matrices, bakedQuad, r, g, b, light, overlay);
            SpriteUtil.markSpriteActive(bakedQuad.getSprite());
        } else if (this.f_85824_) {
            throw new IllegalStateException();
        } else if (bakedQuad.getVertices().length >= 32) {
            VertexBufferWriter writer = VertexBufferWriter.of(this);
            ModelQuadView quad = (ModelQuadView) bakedQuad;
            int color = ColorABGR.pack(r, g, b, 1.0F);
            BakedModelEncoder.writeQuadVertices(writer, matrices, quad, color, light, overlay);
            SpriteUtil.markSpriteActive(quad.getSprite());
        }
    }

    @Override
    public void putBulkData(PoseStack.Pose matrices, BakedQuad bakedQuad, float[] brightnessTable, float r, float g, float b, int[] light, int overlay, boolean colorize) {
        if (!this.fastFormat) {
            super.m_85995_(matrices, bakedQuad, brightnessTable, r, g, b, light, overlay, colorize);
            SpriteUtil.markSpriteActive(bakedQuad.getSprite());
        } else if (this.f_85824_) {
            throw new IllegalStateException();
        } else if (bakedQuad.getVertices().length >= 32) {
            VertexBufferWriter writer = VertexBufferWriter.of(this);
            ModelQuadView quad = (ModelQuadView) bakedQuad;
            BakedModelEncoder.writeQuadVertices(writer, matrices, quad, r, g, b, brightnessTable, colorize, light, overlay);
            SpriteUtil.markSpriteActive(quad.getSprite());
        }
    }
}