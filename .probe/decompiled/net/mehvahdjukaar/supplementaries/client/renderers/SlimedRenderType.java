package net.mehvahdjukaar.supplementaries.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class SlimedRenderType extends RenderType {

    protected static final RenderStateShard.TexturingStateShard TEXTURING_STATE_SHARD = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> animateTexture(1.2F, 4L), RenderSystem::resetTextureMatrix);

    protected static final RenderStateShard.TextureStateShard TEXTURE_SHARD = new RenderStateShard.TextureStateShard(ModTextures.SLIME_ENTITY_OVERLAY, true, false);

    public SlimedRenderType(String s, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean b, boolean b1, Runnable runnable, Runnable aSuper) {
        super(s, vertexFormat, mode, i, b, b1, runnable, aSuper);
    }

    private static void animateTexture(float in, long time) {
        long i = Util.getMillis() * time;
        float f = (float) (i % 80000L) / 80000.0F;
        float f1 = 0.5F + Mth.sin((float) ((double) ((float) (i % 30000L) / 30000.0F) * Math.PI)) * 0.5F;
        Matrix4f matrix4f = new Matrix4f().translation(0.0F, f, 0.0F);
        matrix4f.rotate(Axis.ZP.rotationDegrees(30.0F));
        matrix4f.mul(new Matrix4f().scale(0.5F, 0.5F, 0.5F));
        RenderSystem.setTextureMatrix(matrix4f);
    }
}