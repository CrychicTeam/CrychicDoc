package icyllis.modernui.mc;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class TooltipRenderType extends RenderType {

    private static ShaderInstance sShaderTooltip;

    static final RenderStateShard.ShaderStateShard RENDERTYPE_MODERN_TOOLTIP = new RenderStateShard.ShaderStateShard(TooltipRenderType::getShaderTooltip);

    private static final ImmutableList<RenderStateShard> STATES = ImmutableList.of(RENDERTYPE_MODERN_TOOLTIP, f_110147_, f_110139_, f_110113_, f_110158_, f_110152_, f_110155_, f_110117_, f_110123_, f_110148_, f_110114_, f_110130_, new RenderStateShard[0]);

    static final RenderType TOOLTIP = new TooltipRenderType("modern_tooltip", 1536, () -> STATES.forEach(RenderStateShard::m_110185_), () -> STATES.forEach(RenderStateShard::m_110188_));

    private TooltipRenderType(String name, int bufferSize, Runnable setupState, Runnable clearState) {
        super(name, DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, bufferSize, false, false, setupState, clearState);
    }

    public static RenderType tooltip() {
        return TOOLTIP;
    }

    public static ShaderInstance getShaderTooltip() {
        return sShaderTooltip;
    }

    public static void setShaderTooltip(ShaderInstance shaderTooltip) {
        sShaderTooltip = shaderTooltip;
    }
}