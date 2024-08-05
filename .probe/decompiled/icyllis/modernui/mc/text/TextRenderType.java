package icyllis.modernui.mc.text;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.opengl.GLCaps;
import icyllis.arc3d.opengl.GLCore;
import icyllis.arc3d.opengl.GLDevice;
import icyllis.arc3d.opengl.GLSampler;
import icyllis.modernui.ModernUI;
import icyllis.modernui.core.Core;
import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.text.mixin.AccessRenderBuffers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;

public class TextRenderType extends RenderType {

    public static final int MODE_NORMAL = 0;

    public static final int MODE_SDF_FILL = 1;

    public static final int MODE_SDF_STROKE = 2;

    public static final int MODE_SEE_THROUGH = 3;

    public static final int MODE_UNIFORM_SCALE = 4;

    private static volatile ShaderInstance sShaderNormal;

    private static volatile ShaderInstance sCurrentShaderSDFFill;

    private static volatile ShaderInstance sCurrentShaderSDFStroke;

    private static volatile ShaderInstance sShaderSDFFill;

    private static volatile ShaderInstance sShaderSDFStroke;

    @Nullable
    private static volatile ShaderInstance sShaderSDFFillSmart;

    @Nullable
    private static volatile ShaderInstance sShaderSDFStrokeSmart;

    private static boolean sSmartShadersLoaded = false;

    static final RenderStateShard.ShaderStateShard RENDERTYPE_MODERN_TEXT_NORMAL = new RenderStateShard.ShaderStateShard(TextRenderType::getShaderNormal);

    static final RenderStateShard.ShaderStateShard RENDERTYPE_MODERN_TEXT_SDF_FILL = new RenderStateShard.ShaderStateShard(TextRenderType::getShaderSDFFill);

    static final RenderStateShard.ShaderStateShard RENDERTYPE_MODERN_TEXT_SDF_STROKE = new RenderStateShard.ShaderStateShard(TextRenderType::getShaderSDFStroke);

    private static final ImmutableList<RenderStateShard> NORMAL_STATES = ImmutableList.of(RENDERTYPE_MODERN_TEXT_NORMAL, f_110139_, f_110113_, f_110158_, f_110152_, f_110155_, f_110117_, f_110123_, f_110148_, f_110114_, f_110130_);

    private static final ImmutableList<RenderStateShard> SDF_FILL_STATES = ImmutableList.of(RENDERTYPE_MODERN_TEXT_SDF_FILL, f_110139_, f_110113_, f_110158_, f_110152_, f_110155_, f_110118_, f_110123_, f_110148_, f_110114_, f_110130_);

    private static final ImmutableList<RenderStateShard> SDF_STROKE_STATES = ImmutableList.of(RENDERTYPE_MODERN_TEXT_SDF_STROKE, f_110139_, f_110113_, f_110158_, f_110152_, f_110155_, f_110118_, f_110123_, f_110148_, f_110114_, f_110130_);

    private static final ImmutableList<RenderStateShard> SEE_THROUGH_STATES = ImmutableList.of(f_173088_, f_110139_, f_110111_, f_110158_, f_110152_, f_110155_, f_110117_, f_110123_, f_110148_, f_110115_, f_110130_);

    private static final ImmutableList<RenderStateShard> POLYGON_OFFSET_STATES = ImmutableList.of(f_173086_, f_110139_, f_110113_, f_110158_, f_110152_, f_110155_, f_110118_, f_110123_, f_110148_, f_110114_, f_110130_);

    private static final Int2ObjectMap<TextRenderType> sNormalTypes = new Int2ObjectOpenHashMap();

    private static final Int2ObjectMap<TextRenderType> sSDFFillTypes = new Int2ObjectOpenHashMap();

    private static final Int2ObjectMap<TextRenderType> sSDFStrokeTypes = new Int2ObjectOpenHashMap();

    private static final Int2ObjectMap<TextRenderType> sSeeThroughTypes = new Int2ObjectOpenHashMap();

    private static final Int2ObjectMap<TextRenderType> sPolygonOffsetTypes = new Int2ObjectOpenHashMap();

    private static TextRenderType sFirstSDFFillType;

    private static final BufferBuilder sFirstSDFFillBuffer = new BufferBuilder(131072);

    private static TextRenderType sFirstSDFStrokeType;

    private static final BufferBuilder sFirstSDFStrokeBuffer = new BufferBuilder(131072);

    @SharedPtr
    private static GLSampler sLinearFontSampler;

    private TextRenderType(String name, int bufferSize, Runnable setupState, Runnable clearState) {
        super(name, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, bufferSize, false, true, setupState, clearState);
    }

    @Nonnull
    public static TextRenderType getOrCreate(int texture, int mode) {
        return switch(mode) {
            case 1 ->
                (TextRenderType) sSDFFillTypes.computeIfAbsent(texture, TextRenderType::makeSDFFillType);
            case 2 ->
                (TextRenderType) sSDFStrokeTypes.computeIfAbsent(texture, TextRenderType::makeSDFStrokeType);
            case 3 ->
                (TextRenderType) sSeeThroughTypes.computeIfAbsent(texture, TextRenderType::makeSeeThroughType);
            default ->
                (TextRenderType) sNormalTypes.computeIfAbsent(texture, TextRenderType::makeNormalType);
        };
    }

    @Nonnull
    public static TextRenderType getOrCreate(int texture, Font.DisplayMode mode) {
        return switch(mode) {
            case SEE_THROUGH ->
                (TextRenderType) sSeeThroughTypes.computeIfAbsent(texture, TextRenderType::makeSeeThroughType);
            case POLYGON_OFFSET ->
                (TextRenderType) sPolygonOffsetTypes.computeIfAbsent(texture, TextRenderType::makePolygonOffsetType);
            default ->
                (TextRenderType) sNormalTypes.computeIfAbsent(texture, TextRenderType::makeNormalType);
        };
    }

    @Nonnull
    private static TextRenderType makeNormalType(int texture) {
        return new TextRenderType("modern_text_normal", 256, () -> {
            NORMAL_STATES.forEach(RenderStateShard::m_110185_);
            RenderSystem.setShaderTexture(0, texture);
        }, () -> NORMAL_STATES.forEach(RenderStateShard::m_110188_));
    }

    private static void ensureLinearFontSampler() {
        if (sLinearFontSampler == null) {
            GLDevice device = (GLDevice) Core.requireDirectContext().getDevice();
            sLinearFontSampler = device.getResourceProvider().findOrCreateCompatibleSampler(1188369);
            Objects.requireNonNull(sLinearFontSampler, "Failed to create sampler object");
        }
    }

    @Nonnull
    private static TextRenderType makeSDFFillType(int texture) {
        ensureLinearFontSampler();
        TextRenderType renderType = new TextRenderType("modern_text_sdf_fill", 256, () -> {
            SDF_FILL_STATES.forEach(RenderStateShard::m_110185_);
            RenderSystem.setShaderTexture(0, texture);
            if (!TextLayoutEngine.sCurrentInWorldRendering || TextLayoutEngine.sUseTextShadersInWorld) {
                GLCore.glBindSampler(0, sLinearFontSampler.getHandle());
            }
        }, () -> {
            SDF_FILL_STATES.forEach(RenderStateShard::m_110188_);
            if (!TextLayoutEngine.sCurrentInWorldRendering || TextLayoutEngine.sUseTextShadersInWorld) {
                GLCore.glBindSampler(0, 0);
            }
        });
        if (sFirstSDFFillType == null) {
            assert sSDFFillTypes.isEmpty();
            sFirstSDFFillType = renderType;
            if (TextLayoutEngine.sUseTextShadersInWorld) {
                try {
                    ((AccessRenderBuffers) Minecraft.getInstance().renderBuffers()).getFixedBuffers().put(renderType, sFirstSDFFillBuffer);
                } catch (Exception var3) {
                    ModernUI.LOGGER.warn(ModernUI.MARKER, "Failed to add SDF fill to fixed buffers", var3);
                }
            }
        }
        return renderType;
    }

    @Nonnull
    private static TextRenderType makeSDFStrokeType(int texture) {
        ensureLinearFontSampler();
        TextRenderType renderType = new TextRenderType("modern_text_sdf_stroke", 256, () -> {
            SDF_STROKE_STATES.forEach(RenderStateShard::m_110185_);
            RenderSystem.setShaderTexture(0, texture);
            if (!TextLayoutEngine.sCurrentInWorldRendering || TextLayoutEngine.sUseTextShadersInWorld) {
                GLCore.glBindSampler(0, sLinearFontSampler.getHandle());
            }
        }, () -> {
            SDF_STROKE_STATES.forEach(RenderStateShard::m_110188_);
            if (!TextLayoutEngine.sCurrentInWorldRendering || TextLayoutEngine.sUseTextShadersInWorld) {
                GLCore.glBindSampler(0, 0);
            }
        });
        if (sFirstSDFStrokeType == null) {
            assert sSDFStrokeTypes.isEmpty();
            sFirstSDFStrokeType = renderType;
            if (TextLayoutEngine.sUseTextShadersInWorld) {
                try {
                    ((AccessRenderBuffers) Minecraft.getInstance().renderBuffers()).getFixedBuffers().put(renderType, sFirstSDFStrokeBuffer);
                } catch (Exception var3) {
                    ModernUI.LOGGER.warn(ModernUI.MARKER, "Failed to add SDF stroke to fixed buffers", var3);
                }
            }
        }
        return renderType;
    }

    @Nonnull
    private static TextRenderType makeSeeThroughType(int texture) {
        return new TextRenderType("modern_text_see_through", 256, () -> {
            SEE_THROUGH_STATES.forEach(RenderStateShard::m_110185_);
            RenderSystem.setShaderTexture(0, texture);
        }, () -> SEE_THROUGH_STATES.forEach(RenderStateShard::m_110188_));
    }

    @Nonnull
    private static TextRenderType makePolygonOffsetType(int texture) {
        return new TextRenderType("modern_text_polygon_offset", 256, () -> {
            POLYGON_OFFSET_STATES.forEach(RenderStateShard::m_110185_);
            RenderSystem.setShaderTexture(0, texture);
        }, () -> POLYGON_OFFSET_STATES.forEach(RenderStateShard::m_110188_));
    }

    @Nullable
    public static TextRenderType getFirstSDFFillType() {
        return sFirstSDFFillType;
    }

    @Nullable
    public static TextRenderType getFirstSDFStrokeType() {
        return sFirstSDFStrokeType;
    }

    public static void clear(boolean cleanup) {
        if (sFirstSDFFillType != null) {
            assert !sSDFFillTypes.isEmpty();
            AccessRenderBuffers access = (AccessRenderBuffers) Minecraft.getInstance().renderBuffers();
            access.getFixedBuffers().remove(sFirstSDFFillType, sFirstSDFFillBuffer);
            sFirstSDFFillType = null;
        }
        if (sFirstSDFStrokeType != null) {
            assert !sSDFStrokeTypes.isEmpty();
            AccessRenderBuffers access = (AccessRenderBuffers) Minecraft.getInstance().renderBuffers();
            access.getFixedBuffers().remove(sFirstSDFStrokeType, sFirstSDFStrokeBuffer);
            sFirstSDFStrokeType = null;
        }
        sNormalTypes.clear();
        sSDFFillTypes.clear();
        sSDFStrokeTypes.clear();
        sSeeThroughTypes.clear();
        sFirstSDFFillBuffer.clear();
        sFirstSDFStrokeBuffer.clear();
        if (cleanup) {
            sLinearFontSampler = RefCnt.move(sLinearFontSampler);
        }
    }

    public static ShaderInstance getShaderNormal() {
        return TextLayoutEngine.sCurrentInWorldRendering && !TextLayoutEngine.sUseTextShadersInWorld ? GameRenderer.getRendertypeTextShader() : sShaderNormal;
    }

    public static ShaderInstance getShaderSDFFill() {
        return TextLayoutEngine.sCurrentInWorldRendering && !TextLayoutEngine.sUseTextShadersInWorld ? GameRenderer.getRendertypeTextShader() : sCurrentShaderSDFFill;
    }

    public static ShaderInstance getShaderSDFStroke() {
        return sCurrentShaderSDFStroke;
    }

    public static synchronized void toggleSDFShaders(boolean smart) {
        if (smart) {
            if (!sSmartShadersLoaded) {
                sSmartShadersLoaded = true;
                if (((GLCaps) Core.requireDirectContext().getCaps()).getGLSLVersion() >= 400) {
                    ResourceProvider provider = obtainResourceProvider();
                    try {
                        sShaderSDFFillSmart = MuiModApi.get().makeShaderInstance(provider, ModernUIMod.location("rendertype_modern_text_sdf_fill_400"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
                        sShaderSDFStrokeSmart = MuiModApi.get().makeShaderInstance(provider, ModernUIMod.location("rendertype_modern_text_sdf_stroke_400"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
                        ModernUI.LOGGER.info(ModernUI.MARKER, "Loaded smart SDF text shaders");
                    } catch (IOException var3) {
                        ModernUI.LOGGER.error(ModernUI.MARKER, "Failed to load smart SDF text shaders", var3);
                    }
                } else {
                    ModernUI.LOGGER.info(ModernUI.MARKER, "No GLSL 400, smart SDF text shaders disabled");
                }
            }
            if (sShaderSDFStrokeSmart != null) {
                sCurrentShaderSDFFill = sShaderSDFFillSmart;
                sCurrentShaderSDFStroke = sShaderSDFStrokeSmart;
                return;
            }
        }
        sCurrentShaderSDFFill = sShaderSDFFill;
        sCurrentShaderSDFStroke = sShaderSDFStroke;
    }

    public static synchronized void preloadShaders() {
        if (sShaderNormal == null) {
            ResourceProvider provider = obtainResourceProvider();
            try {
                sShaderNormal = MuiModApi.get().makeShaderInstance(provider, ModernUIMod.location("rendertype_modern_text_normal"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
                sShaderSDFFill = MuiModApi.get().makeShaderInstance(provider, ModernUIMod.location("rendertype_modern_text_sdf_fill"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
                sShaderSDFStroke = MuiModApi.get().makeShaderInstance(provider, ModernUIMod.location("rendertype_modern_text_sdf_stroke"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
            } catch (IOException var2) {
                throw new IllegalStateException("Bad text shaders", var2);
            }
            toggleSDFShaders(false);
            ModernUI.LOGGER.info(ModernUI.MARKER, "Preloaded modern text shaders");
        }
    }

    @Nonnull
    private static ResourceProvider obtainResourceProvider() {
        VanillaPackResources source = Minecraft.getInstance().getVanillaPackResources();
        ResourceProvider fallback = source.asProvider();
        return location -> {
            InputStream stream = TextRenderType.class.getResourceAsStream("/assets/" + location.getNamespace() + "/" + location.getPath());
            return stream == null ? fallback.getResource(location) : Optional.of(new Resource(source, () -> stream));
        };
    }
}