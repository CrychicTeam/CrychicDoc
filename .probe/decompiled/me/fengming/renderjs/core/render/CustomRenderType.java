package me.fengming.renderjs.core.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class CustomRenderType extends RenderType {

    public static final RenderType BLOCK_LAYER_TOP = RenderType.create("block_layer_top", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, RenderType.CompositeState.builder().setLightmapState(f_110152_).setShaderState(f_173105_).setTextureState(f_110145_).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).createCompositeState(true));

    public CustomRenderType(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }
}