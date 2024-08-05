package net.mehvahdjukaar.moonlight.core.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.Internal;

public class MLRenderTypes extends RenderType {

    @Internal
    public static Supplier<ShaderInstance> textColorShader = GameRenderer::m_172749_;

    public static final Function<ResourceLocation, RenderType> COLOR_TEXT = Util.memoize((Function<ResourceLocation, RenderType>) (p -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(textColorShader)).setTextureState(new RenderStateShard.TextureStateShard(p, false, true)).setTransparencyState(f_110139_).setLightmapState(f_110152_).createCompositeState(false);
        return m_173215_("moonlight_text_color_mipped", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, compositeState);
    }));

    public static final Function<ResourceLocation, RenderType> TEXT_MIP = Util.memoize((Function<ResourceLocation, RenderType>) (p -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(f_173086_).setTextureState(new RenderStateShard.TextureStateShard(p, false, true)).setTransparencyState(f_110139_).setLightmapState(f_110152_).createCompositeState(false);
        return m_173215_("moonlight_text_mipped", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, compositeState);
    }));

    public static final Function<ResourceLocation, RenderType> ENTITY_SOLID_MIP = Util.memoize((Function<ResourceLocation, RenderType>) (resourceLocation -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(f_173112_).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, true)).setTransparencyState(f_110134_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true);
        return m_173215_("moonlight_entity_solid_mipped", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, compositeState);
    }));

    public static final Function<ResourceLocation, RenderType> ENTITY_CUTOUT_MIP = Util.memoize((Function<ResourceLocation, RenderType>) (resourceLocation -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(f_173113_).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, true)).setTransparencyState(f_110134_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true);
        return RenderType.create("moonlight_entity_cutout_mipped", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, compositeState);
    }));

    public MLRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }
}