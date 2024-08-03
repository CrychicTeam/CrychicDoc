package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeData;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.ShaderUniformHandler;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

public class LodestoneRenderTypeRegistry extends RenderStateShard {

    public static final Runnable TRANSPARENT_FUNCTION = () -> RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

    public static final Runnable ADDITIVE_FUNCTION = () -> RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

    public static final RenderStateShard.EmptyTextureStateShard NO_TEXTURE = RenderStateShard.NO_TEXTURE;

    public static final RenderStateShard.LightmapStateShard LIGHTMAP = RenderStateShard.LIGHTMAP;

    public static final RenderStateShard.LightmapStateShard NO_LIGHTMAP = RenderStateShard.NO_LIGHTMAP;

    public static final RenderStateShard.CullStateShard CULL = RenderStateShard.CULL;

    public static final RenderStateShard.CullStateShard NO_CULL = RenderStateShard.NO_CULL;

    public static final HashMap<Pair<Object, LodestoneRenderType>, LodestoneRenderType> COPIES = new HashMap();

    public static final Function<RenderTypeData, LodestoneRenderType> GENERIC = data -> createGenericRenderType(data.name, data.format, data.mode, data.shader, data.transparency, data.texture, data.cull);

    private static Consumer<LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder> MODIFIER;

    public static final LodestoneRenderType ADDITIVE_PARTICLE = createGenericRenderType("additive_particle", DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.PARTICLE).setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY).setTextureState(TextureAtlas.LOCATION_PARTICLES).setCullState(NO_CULL));

    public static final LodestoneRenderType ADDITIVE_BLOCK_PARTICLE = createGenericRenderType("additive_block_particle", DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.PARTICLE).setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY).setTextureState(TextureAtlas.LOCATION_BLOCKS).setCullState(NO_CULL));

    public static final LodestoneRenderType ADDITIVE_BLOCK = createGenericRenderType("additive_block", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE).setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY).setTextureState(TextureAtlas.LOCATION_BLOCKS));

    public static final LodestoneRenderType ADDITIVE_SOLID = createGenericRenderType("additive_block", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(f_173099_).setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY).setTextureState(NO_TEXTURE));

    public static final LodestoneRenderType TRANSPARENT_PARTICLE = createGenericRenderType("transparent_particle", DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.PARTICLE).setTransparencyState(StateShards.NORMAL_TRANSPARENCY).setTextureState(TextureAtlas.LOCATION_PARTICLES).setCullState(NO_CULL));

    public static final LodestoneRenderType TRANSPARENT_BLOCK_PARTICLE = createGenericRenderType("transparent_block_particle", DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.PARTICLE).setTransparencyState(StateShards.NORMAL_TRANSPARENCY).setTextureState(TextureAtlas.LOCATION_BLOCKS).setCullState(NO_CULL));

    public static final LodestoneRenderType TRANSPARENT_BLOCK = createGenericRenderType("transparent_block", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE).setTransparencyState(StateShards.NORMAL_TRANSPARENCY).setTextureState(TextureAtlas.LOCATION_BLOCKS));

    public static final LodestoneRenderType TRANSPARENT_SOLID = createGenericRenderType("transparent_block", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(f_173099_).setTransparencyState(StateShards.NORMAL_TRANSPARENCY).setTextureState(NO_TEXTURE));

    public static final LodestoneRenderType LUMITRANSPARENT_PARTICLE = copyWithUniformChanges("lodestone:lumitransparent_particle", TRANSPARENT_PARTICLE, ShaderUniformHandler.LUMITRANSPARENT);

    public static final LodestoneRenderType LUMITRANSPARENT_BLOCK_PARTICLE = copyWithUniformChanges("lodestone:lumitransparent_block_particle", TRANSPARENT_BLOCK_PARTICLE, ShaderUniformHandler.LUMITRANSPARENT);

    public static final LodestoneRenderType LUMITRANSPARENT_BLOCK = copyWithUniformChanges("lodestone:lumitransparent_block", TRANSPARENT_BLOCK, ShaderUniformHandler.LUMITRANSPARENT);

    public static final LodestoneRenderType LUMITRANSPARENT_SOLID = copyWithUniformChanges("lodestone:lumitransparent_solid", TRANSPARENT_SOLID, ShaderUniformHandler.LUMITRANSPARENT);

    public static final RenderTypeProvider TEXTURE = new RenderTypeProvider(token -> createGenericRenderType("texture", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE).setTransparencyState(StateShards.f_110134_).setLightmapState(LIGHTMAP).setCullState(CULL).setTextureState(token.get())));

    public static final RenderTypeProvider TRANSPARENT_TEXTURE = new RenderTypeProvider(token -> createGenericRenderType("transparent_texture", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE).setTransparencyState(StateShards.NORMAL_TRANSPARENCY).setLightmapState(LIGHTMAP).setCullState(CULL).setTextureState(token.get())));

    public static final RenderTypeProvider TRANSPARENT_TEXTURE_TRIANGLE = new RenderTypeProvider(token -> createGenericRenderType("transparent_texture_triangles", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.TRIANGLE_TEXTURE).setTransparencyState(StateShards.NORMAL_TRANSPARENCY).setLightmapState(LIGHTMAP).setCullState(CULL).setTextureState(token.get())));

    public static final RenderTypeProvider TRANSPARENT_SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider(token -> createGenericRenderType("transparent_scrolling_texture_triangles", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.SCROLLING_TRIANGLE_TEXTURE).setTransparencyState(StateShards.NORMAL_TRANSPARENCY).setLightmapState(LIGHTMAP).setCullState(CULL).setTextureState(token.get())));

    public static final RenderTypeProvider TRANSPARENT_TEXT = new RenderTypeProvider(token -> createGenericRenderType("transparent_text", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.LODESTONE_TEXT.getShard()).setTransparencyState(StateShards.f_110139_).setTextureState(token.get())));

    public static final RenderTypeProvider ADDITIVE_TEXTURE = new RenderTypeProvider(token -> createGenericRenderType("additive_texture", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE).setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY).setLightmapState(LIGHTMAP).setCullState(CULL).setTextureState(token.get())));

    public static final RenderTypeProvider ADDITIVE_TEXTURE_TRIANGLE = new RenderTypeProvider(token -> createGenericRenderType("additive_texture_triangle", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.TRIANGLE_TEXTURE).setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY).setLightmapState(LIGHTMAP).setCullState(CULL).setTextureState(token.get())));

    public static final RenderTypeProvider ADDITIVE_SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider(token -> createGenericRenderType("additive_scrolling_texture_triangle", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.SCROLLING_TRIANGLE_TEXTURE).setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY).setLightmapState(LIGHTMAP).setCullState(CULL).setTextureState(token.get())));

    public static final RenderTypeProvider ADDITIVE_TEXT = new RenderTypeProvider(token -> createGenericRenderType("additive_text", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, builder().setShaderState(LodestoneShaderRegistry.LODESTONE_TEXT.getShard()).setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY).setTextureState(token.get())));

    public LodestoneRenderTypeRegistry(String string0, Runnable runnable1, Runnable runnable2) {
        super(string0, runnable1, runnable2);
    }

    @Deprecated
    public static LodestoneRenderType createGenericRenderType(String name, VertexFormat format, RenderStateShard.ShaderStateShard shader, RenderStateShard.TransparencyStateShard transparency, ResourceLocation texture) {
        return createGenericRenderType(name, format, VertexFormat.Mode.QUADS, shader, transparency, new RenderStateShard.TextureStateShard(texture, false, false), CULL);
    }

    public static LodestoneRenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, RenderStateShard.ShaderStateShard shader, RenderStateShard.TransparencyStateShard transparency, RenderStateShard.EmptyTextureStateShard texture, RenderStateShard.CullStateShard cull) {
        return createGenericRenderType(name, format, mode, builder().setShaderState(shader).setTransparencyState(transparency).setTextureState(texture).setLightmapState(LIGHTMAP).setCullState(cull));
    }

    public static LodestoneRenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder builder) {
        return createGenericRenderType(name, format, mode, builder, null);
    }

    public static LodestoneRenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder builder, ShaderUniformHandler handler) {
        if (MODIFIER != null) {
            MODIFIER.accept(builder);
        }
        LodestoneRenderType type = LodestoneRenderType.createRenderType(name, format, builder.mode != null ? builder.mode : mode, 256, false, true, builder.m_110691_(true));
        RenderHandler.addRenderType(type);
        if (handler != null) {
            applyUniformChanges(type, handler);
        }
        MODIFIER = null;
        return type;
    }

    public static LodestoneRenderType copyWithUniformChanges(LodestoneRenderType type, ShaderUniformHandler handler) {
        return applyUniformChanges(copy(type), handler);
    }

    public static LodestoneRenderType copyWithUniformChanges(String newName, LodestoneRenderType type, ShaderUniformHandler handler) {
        return applyUniformChanges(copy(newName, type), handler);
    }

    public static LodestoneRenderType applyUniformChanges(LodestoneRenderType type, ShaderUniformHandler handler) {
        RenderHandler.UNIFORM_HANDLERS.put(type, handler);
        return type;
    }

    public static LodestoneRenderType copy(LodestoneRenderType type) {
        return (LodestoneRenderType) GENERIC.apply(new RenderTypeData(type));
    }

    public static LodestoneRenderType copy(String newName, LodestoneRenderType type) {
        return (LodestoneRenderType) GENERIC.apply(new RenderTypeData(newName, type));
    }

    public static LodestoneRenderType copyAndStore(Object index, LodestoneRenderType type) {
        return (LodestoneRenderType) COPIES.computeIfAbsent(Pair.of(index, type), p -> (LodestoneRenderType) GENERIC.apply(new RenderTypeData(type)));
    }

    public static LodestoneRenderType copyAndStore(Object index, LodestoneRenderType type, ShaderUniformHandler handler) {
        return applyUniformChanges(copyAndStore(index, type), handler);
    }

    public static void addRenderTypeModifier(Consumer<LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder> modifier) {
        MODIFIER = modifier;
    }

    public static LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder builder() {
        return new LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder().setLightmapState(LIGHTMAP);
    }

    public static class LodestoneCompositeStateBuilder extends RenderType.CompositeState.CompositeStateBuilder {

        protected VertexFormat.Mode mode;

        LodestoneCompositeStateBuilder() {
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder replaceVertexFormat(VertexFormat.Mode mode) {
            this.mode = mode;
            return this;
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setTextureState(ResourceLocation texture) {
            return this.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false));
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setShaderState(ShaderHolder shaderHolder) {
            return this.setShaderState(shaderHolder.getShard());
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setTextureState(RenderStateShard.EmptyTextureStateShard pTextureState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setTextureState(pTextureState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setShaderState(RenderStateShard.ShaderStateShard pShaderState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setShaderState(pShaderState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setTransparencyState(RenderStateShard.TransparencyStateShard pTransparencyState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setTransparencyState(pTransparencyState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setDepthTestState(RenderStateShard.DepthTestStateShard pDepthTestState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setDepthTestState(pDepthTestState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setCullState(RenderStateShard.CullStateShard pCullState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setCullState(pCullState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setLightmapState(RenderStateShard.LightmapStateShard pLightmapState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setLightmapState(pLightmapState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setOverlayState(RenderStateShard.OverlayStateShard pOverlayState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setOverlayState(pOverlayState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setLayeringState(RenderStateShard.LayeringStateShard pLayerState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setLayeringState(pLayerState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setOutputState(RenderStateShard.OutputStateShard pOutputState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setOutputState(pOutputState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setTexturingState(RenderStateShard.TexturingStateShard pTexturingState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setTexturingState(pTexturingState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setWriteMaskState(RenderStateShard.WriteMaskStateShard pWriteMaskState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setWriteMaskState(pWriteMaskState);
        }

        public LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder setLineState(RenderStateShard.LineStateShard pLineState) {
            return (LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder) super.setLineState(pLineState);
        }
    }
}