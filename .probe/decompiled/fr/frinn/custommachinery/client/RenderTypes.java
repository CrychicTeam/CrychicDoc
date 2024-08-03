package fr.frinn.custommachinery.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.OptionalDouble;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class RenderTypes extends RenderType {

    public static final RenderType PHANTOM = m_173215_("phantom", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, RenderType.CompositeState.builder().setShaderState(f_173108_).setLightmapState(f_110152_).setTextureState(f_110145_).setTransparencyState(f_110139_).createCompositeState(true));

    public static final RenderType NOPE = m_173215_("nope", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, RenderType.CompositeState.builder().setShaderState(f_173108_).setLightmapState(f_110152_).setTextureState(f_110145_).setDepthTestState(f_110113_).createCompositeState(true));

    public static final RenderType THICK_LINES = m_173215_("thick_lines", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173095_).setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(10.0))).setLayeringState(f_110119_).setTransparencyState(f_110139_).setOutputState(f_110129_).setWriteMaskState(f_110114_).setCullState(f_110110_).createCompositeState(false));

    public RenderTypes(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }
}