package noppes.npcs.shared.client.model.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector4f;

public class CustomRenderStates extends RenderStateShard {

    public static final Vector4f WHITE = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);

    public static VertexFormat POS_COL_TEX_LIGHT_FADE_NORMAL;

    public static VertexFormat POS_COL_TEX_NORMAL;

    public static final VertexFormat POS_TEX_NORMAL = new VertexFormat(ImmutableMap.of("Position", DefaultVertexFormat.ELEMENT_POSITION, "UV0", DefaultVertexFormat.ELEMENT_UV0, "Normal", DefaultVertexFormat.ELEMENT_NORMAL, "Padding", DefaultVertexFormat.ELEMENT_PADDING));

    protected static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("lm_additive_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderStateShard.TransparencyStateShard SUBTRACTIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("lm_subtractive_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    private static final RenderType[] OBJ_RENDER_TYPES = new RenderType[CustomRenderStates.BLEND.values().length * 2];

    public static final RenderType OBJ_OUTLINE_RENDER_TYPE;

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_SHADER;

    public static ShaderInstance posTexNormalShader;

    private static final Function<ResourceLocation, RenderType> ENTITY_CUTOUT;

    public CustomRenderStates(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
        super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
    }

    public static RenderType getObjVBORenderType(int blending, boolean glow) {
        return OBJ_RENDER_TYPES[blending << 1 | (glow ? 1 : 0)];
    }

    public static RenderType entityCutout(ResourceLocation resourceLocation0) {
        return (RenderType) ENTITY_CUTOUT.apply(resourceLocation0);
    }

    public static RenderType getObjRenderType(ResourceLocation texture, int blending, boolean glow) {
        if (POS_COL_TEX_LIGHT_FADE_NORMAL == null) {
            Map<String, VertexFormatElement> vertexFormatValues = new HashMap();
            vertexFormatValues.put("Position", DefaultVertexFormat.ELEMENT_POSITION);
            vertexFormatValues.put("Color", DefaultVertexFormat.ELEMENT_COLOR);
            vertexFormatValues.put("UV0", DefaultVertexFormat.ELEMENT_UV0);
            vertexFormatValues.put("UV1", DefaultVertexFormat.ELEMENT_UV1);
            vertexFormatValues.put("UV2", DefaultVertexFormat.ELEMENT_UV2);
            vertexFormatValues.put("Normal", DefaultVertexFormat.ELEMENT_NORMAL);
            vertexFormatValues.put("Padding", DefaultVertexFormat.ELEMENT_PADDING);
            POS_COL_TEX_LIGHT_FADE_NORMAL = new VertexFormat(ImmutableMap.copyOf(vertexFormatValues));
        }
        RenderStateShard.TransparencyStateShard TransparencyStateShard = f_110139_;
        if (blending == CustomRenderStates.BLEND.ADD.getValue()) {
            TransparencyStateShard = ADDITIVE_TRANSPARENCY;
        } else if (blending == CustomRenderStates.BLEND.SUB.getValue()) {
            TransparencyStateShard = SUBTRACTIVE_TRANSPARENCY;
        }
        RenderType.CompositeState renderTypeState = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(TransparencyStateShard).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true);
        return RenderType.create("lm_obj_translucent_no_cull", POS_COL_TEX_LIGHT_FADE_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, renderTypeState);
    }

    public static RenderType getObjColorOnlyRenderType(ResourceLocation texture, int blending, boolean glow) {
        if (POS_COL_TEX_LIGHT_FADE_NORMAL == null) {
            Map<String, VertexFormatElement> vertexFormatValues = new HashMap();
            vertexFormatValues.put("Position", DefaultVertexFormat.ELEMENT_POSITION);
            vertexFormatValues.put("Color", DefaultVertexFormat.ELEMENT_COLOR);
            vertexFormatValues.put("Normal", DefaultVertexFormat.ELEMENT_NORMAL);
            vertexFormatValues.put("Padding", DefaultVertexFormat.ELEMENT_PADDING);
            POS_COL_TEX_LIGHT_FADE_NORMAL = new VertexFormat(ImmutableMap.copyOf(vertexFormatValues));
        }
        RenderStateShard.TransparencyStateShard TransparencyStateShard = f_110139_;
        if (blending == CustomRenderStates.BLEND.ADD.getValue()) {
            TransparencyStateShard = ADDITIVE_TRANSPARENCY;
        } else if (blending == CustomRenderStates.BLEND.SUB.getValue()) {
            TransparencyStateShard = SUBTRACTIVE_TRANSPARENCY;
        }
        RenderType.CompositeState renderTypeState = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(TransparencyStateShard).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true);
        return RenderType.create("lm_obj_translucent_no_cull", POS_COL_TEX_LIGHT_FADE_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, renderTypeState);
    }

    public static RenderType getObjOutlineRenderType(ResourceLocation texture) {
        if (POS_COL_TEX_LIGHT_FADE_NORMAL == null) {
            Map<String, VertexFormatElement> vertexFormatValues = new HashMap();
            vertexFormatValues.put("Position", DefaultVertexFormat.ELEMENT_POSITION);
            vertexFormatValues.put("Color", DefaultVertexFormat.ELEMENT_COLOR);
            vertexFormatValues.put("UV0", DefaultVertexFormat.ELEMENT_UV0);
            vertexFormatValues.put("UV1", DefaultVertexFormat.ELEMENT_UV1);
            vertexFormatValues.put("UV2", DefaultVertexFormat.ELEMENT_UV2);
            vertexFormatValues.put("Normal", DefaultVertexFormat.ELEMENT_NORMAL);
            vertexFormatValues.put("Padding", DefaultVertexFormat.ELEMENT_PADDING);
            POS_COL_TEX_LIGHT_FADE_NORMAL = new VertexFormat(ImmutableMap.copyOf(vertexFormatValues));
        }
        RenderType.CompositeState renderTypeState = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setCullState(f_110110_).setDepthTestState(f_110111_).setOutputState(f_110124_).createCompositeState(false);
        return RenderType.create("lm_obj_outline_no_cull", POS_COL_TEX_LIGHT_FADE_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, renderTypeState);
    }

    public static RenderType getSpriteRenderType(ResourceLocation texture) {
        if (POS_COL_TEX_NORMAL == null) {
            Map<String, VertexFormatElement> vertexFormatValues = new HashMap();
            vertexFormatValues.put("Position", DefaultVertexFormat.ELEMENT_POSITION);
            vertexFormatValues.put("Color", DefaultVertexFormat.ELEMENT_COLOR);
            vertexFormatValues.put("UV0", DefaultVertexFormat.ELEMENT_UV0);
            vertexFormatValues.put("Normal", DefaultVertexFormat.ELEMENT_NORMAL);
            vertexFormatValues.put("Padding", DefaultVertexFormat.ELEMENT_PADDING);
            POS_COL_TEX_NORMAL = new VertexFormat(ImmutableMap.copyOf(vertexFormatValues));
        }
        RenderType.CompositeState renderTypeState = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).createCompositeState(true);
        return RenderType.create("lm_sprite", POS_COL_TEX_NORMAL, VertexFormat.Mode.QUADS, 256, true, false, renderTypeState);
    }

    static {
        for (CustomRenderStates.BLEND blend : CustomRenderStates.BLEND.values()) {
            for (int glow = 0; glow < 2; glow++) {
                OBJ_RENDER_TYPES[blend.id * 2 + glow] = RenderType.create("lm_obj_" + blend.toString() + (glow == 1 ? "_glow" : ""), POS_TEX_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, RenderType.CompositeState.builder().setTransparencyState(blend == CustomRenderStates.BLEND.ADD ? ADDITIVE_TRANSPARENCY : (blend == CustomRenderStates.BLEND.SUB ? SUBTRACTIVE_TRANSPARENCY : f_110139_)).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(false));
            }
        }
        OBJ_OUTLINE_RENDER_TYPE = RenderType.create("lm_obj_outline_no_cull", POS_TEX_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, RenderType.CompositeState.builder().setDepthTestState(f_110111_).setCullState(f_110110_).setOutputState(f_110124_).createCompositeState(false));
        RENDERTYPE_ENTITY_CUTOUT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172664_);
        posTexNormalShader = null;
        ENTITY_CUTOUT = Util.memoize((Function<ResourceLocation, RenderType>) (p_173202_ -> {
            RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> posTexNormalShader)).setTextureState(new RenderStateShard.TextureStateShard(p_173202_, false, false)).setTransparencyState(f_110134_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true);
            return RenderType.create("nop_entity_cutout", POS_TEX_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, rendertype$compositestate);
        }));
    }

    public static enum BLEND {

        NORMAL(0), ADD(1), SUB(2);

        public final int id;

        private BLEND(int value) {
            this.id = value;
        }

        public int getValue() {
            return this.id;
        }
    }
}