package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;

public abstract class RenderType extends RenderStateShard {

    private static final int BYTES_IN_INT = 4;

    private static final int MEGABYTE = 1048576;

    public static final int BIG_BUFFER_SIZE = 2097152;

    public static final int MEDIUM_BUFFER_SIZE = 262144;

    public static final int SMALL_BUFFER_SIZE = 131072;

    public static final int TRANSIENT_BUFFER_SIZE = 256;

    private static final RenderType SOLID = create("solid", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, RenderType.CompositeState.builder().setLightmapState(f_110152_).setShaderState(f_173105_).setTextureState(f_110145_).createCompositeState(true));

    private static final RenderType CUTOUT_MIPPED = create("cutout_mipped", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 131072, true, false, RenderType.CompositeState.builder().setLightmapState(f_110152_).setShaderState(f_173106_).setTextureState(f_110145_).createCompositeState(true));

    private static final RenderType CUTOUT = create("cutout", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 131072, true, false, RenderType.CompositeState.builder().setLightmapState(f_110152_).setShaderState(f_173107_).setTextureState(f_110146_).createCompositeState(true));

    private static final RenderType TRANSLUCENT = create("translucent", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, true, translucentState(f_173108_));

    private static final RenderType TRANSLUCENT_MOVING_BLOCK = create("translucent_moving_block", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 262144, false, true, translucentMovingBlockState());

    private static final RenderType TRANSLUCENT_NO_CRUMBLING = create("translucent_no_crumbling", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 262144, false, true, translucentState(f_173110_));

    private static final Function<ResourceLocation, RenderType> ARMOR_CUTOUT_NO_CULL = Util.memoize((Function<ResourceLocation, RenderType>) (p_286149_ -> {
        RenderType.CompositeState $$1 = RenderType.CompositeState.builder().setShaderState(f_173111_).setTextureState(new RenderStateShard.TextureStateShard(p_286149_, false, false)).setTransparencyState(f_110134_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).setLayeringState(f_110119_).createCompositeState(true);
        return create("armor_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, $$1);
    }));

    private static final Function<ResourceLocation, RenderType> ENTITY_SOLID = Util.memoize((Function<ResourceLocation, RenderType>) (p_286159_ -> {
        RenderType.CompositeState $$1 = RenderType.CompositeState.builder().setShaderState(f_173112_).setTextureState(new RenderStateShard.TextureStateShard(p_286159_, false, false)).setTransparencyState(f_110134_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true);
        return create("entity_solid", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, $$1);
    }));

    private static final Function<ResourceLocation, RenderType> ENTITY_CUTOUT = Util.memoize((Function<ResourceLocation, RenderType>) (p_286173_ -> {
        RenderType.CompositeState $$1 = RenderType.CompositeState.builder().setShaderState(f_173113_).setTextureState(new RenderStateShard.TextureStateShard(p_286173_, false, false)).setTransparencyState(f_110134_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true);
        return create("entity_cutout", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, $$1);
    }));

    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_CUTOUT_NO_CULL = Util.memoize((BiFunction<ResourceLocation, Boolean, RenderType>) ((p_286166_, p_286167_) -> {
        RenderType.CompositeState $$2 = RenderType.CompositeState.builder().setShaderState(f_173114_).setTextureState(new RenderStateShard.TextureStateShard(p_286166_, false, false)).setTransparencyState(f_110134_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(p_286167_);
        return create("entity_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, $$2);
    }));

    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_CUTOUT_NO_CULL_Z_OFFSET = Util.memoize((BiFunction<ResourceLocation, Boolean, RenderType>) ((p_286153_, p_286154_) -> {
        RenderType.CompositeState $$2 = RenderType.CompositeState.builder().setShaderState(f_173063_).setTextureState(new RenderStateShard.TextureStateShard(p_286153_, false, false)).setTransparencyState(f_110134_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).setLayeringState(f_110119_).createCompositeState(p_286154_);
        return create("entity_cutout_no_cull_z_offset", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, $$2);
    }));

    private static final Function<ResourceLocation, RenderType> ITEM_ENTITY_TRANSLUCENT_CULL = Util.memoize((Function<ResourceLocation, RenderType>) (p_286155_ -> {
        RenderType.CompositeState $$1 = RenderType.CompositeState.builder().setShaderState(f_173064_).setTextureState(new RenderStateShard.TextureStateShard(p_286155_, false, false)).setTransparencyState(f_110139_).setOutputState(f_110129_).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(true);
        return create("item_entity_translucent_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, $$1);
    }));

    private static final Function<ResourceLocation, RenderType> ENTITY_TRANSLUCENT_CULL = Util.memoize((Function<ResourceLocation, RenderType>) (p_286165_ -> {
        RenderType.CompositeState $$1 = RenderType.CompositeState.builder().setShaderState(f_173065_).setTextureState(new RenderStateShard.TextureStateShard(p_286165_, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true);
        return create("entity_translucent_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, $$1);
    }));

    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_TRANSLUCENT = Util.memoize((BiFunction<ResourceLocation, Boolean, RenderType>) ((p_286156_, p_286157_) -> {
        RenderType.CompositeState $$2 = RenderType.CompositeState.builder().setShaderState(f_173066_).setTextureState(new RenderStateShard.TextureStateShard(p_286156_, false, false)).setTransparencyState(f_110139_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(p_286157_);
        return create("entity_translucent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, $$2);
    }));

    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_TRANSLUCENT_EMISSIVE = Util.memoize((BiFunction<ResourceLocation, Boolean, RenderType>) ((p_286163_, p_286164_) -> {
        RenderType.CompositeState $$2 = RenderType.CompositeState.builder().setShaderState(f_234323_).setTextureState(new RenderStateShard.TextureStateShard(p_286163_, false, false)).setTransparencyState(f_110139_).setCullState(f_110110_).setWriteMaskState(f_110115_).setOverlayState(f_110154_).createCompositeState(p_286164_);
        return create("entity_translucent_emissive", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, $$2);
    }));

    private static final Function<ResourceLocation, RenderType> ENTITY_SMOOTH_CUTOUT = Util.memoize((Function<ResourceLocation, RenderType>) (p_286169_ -> {
        RenderType.CompositeState $$1 = RenderType.CompositeState.builder().setShaderState(f_173067_).setTextureState(new RenderStateShard.TextureStateShard(p_286169_, false, false)).setCullState(f_110110_).setLightmapState(f_110152_).createCompositeState(true);
        return create("entity_smooth_cutout", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, $$1);
    }));

    private static final BiFunction<ResourceLocation, Boolean, RenderType> BEACON_BEAM = Util.memoize((BiFunction<ResourceLocation, Boolean, RenderType>) ((p_234330_, p_234331_) -> {
        RenderType.CompositeState $$2 = RenderType.CompositeState.builder().setShaderState(f_173068_).setTextureState(new RenderStateShard.TextureStateShard(p_234330_, false, false)).setTransparencyState(p_234331_ ? f_110139_ : f_110134_).setWriteMaskState(p_234331_ ? f_110115_ : f_110114_).createCompositeState(false);
        return create("beacon_beam", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, true, $$2);
    }));

    private static final Function<ResourceLocation, RenderType> ENTITY_DECAL = Util.memoize((Function<ResourceLocation, RenderType>) (p_286171_ -> {
        RenderType.CompositeState $$1 = RenderType.CompositeState.builder().setShaderState(f_173069_).setTextureState(new RenderStateShard.TextureStateShard(p_286171_, false, false)).setDepthTestState(f_110112_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(false);
        return create("entity_decal", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, $$1);
    }));

    private static final Function<ResourceLocation, RenderType> ENTITY_NO_OUTLINE = Util.memoize((Function<ResourceLocation, RenderType>) (p_286160_ -> {
        RenderType.CompositeState $$1 = RenderType.CompositeState.builder().setShaderState(f_173070_).setTextureState(new RenderStateShard.TextureStateShard(p_286160_, false, false)).setTransparencyState(f_110139_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(f_110115_).createCompositeState(false);
        return create("entity_no_outline", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, $$1);
    }));

    private static final Function<ResourceLocation, RenderType> ENTITY_SHADOW = Util.memoize((Function<ResourceLocation, RenderType>) (p_286151_ -> {
        RenderType.CompositeState $$1 = RenderType.CompositeState.builder().setShaderState(f_173071_).setTextureState(new RenderStateShard.TextureStateShard(p_286151_, false, false)).setTransparencyState(f_110139_).setCullState(f_110158_).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(f_110115_).setDepthTestState(f_110113_).setLayeringState(f_110119_).createCompositeState(false);
        return create("entity_shadow", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, $$1);
    }));

    private static final Function<ResourceLocation, RenderType> DRAGON_EXPLOSION_ALPHA = Util.memoize((Function<ResourceLocation, RenderType>) (p_286150_ -> {
        RenderType.CompositeState $$1 = RenderType.CompositeState.builder().setShaderState(f_173072_).setTextureState(new RenderStateShard.TextureStateShard(p_286150_, false, false)).setCullState(f_110110_).createCompositeState(true);
        return create("entity_alpha", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, $$1);
    }));

    private static final Function<ResourceLocation, RenderType> EYES = Util.memoize((Function<ResourceLocation, RenderType>) (p_286170_ -> {
        RenderStateShard.TextureStateShard $$1 = new RenderStateShard.TextureStateShard(p_286170_, false, false);
        return create("eyes", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173073_).setTextureState($$1).setTransparencyState(f_110135_).setWriteMaskState(f_110115_).createCompositeState(false));
    }));

    private static final RenderType LEASH = create("leash", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.TRIANGLE_STRIP, 256, RenderType.CompositeState.builder().setShaderState(f_173075_).setTextureState(f_110147_).setCullState(f_110110_).setLightmapState(f_110152_).createCompositeState(false));

    private static final RenderType WATER_MASK = create("water_mask", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_173076_).setTextureState(f_110147_).setWriteMaskState(f_110116_).createCompositeState(false));

    private static final RenderType ARMOR_GLINT = create("armor_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_173078_).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ENTITY, true, false)).setWriteMaskState(f_110115_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(f_110150_).setLayeringState(f_110119_).createCompositeState(false));

    private static final RenderType ARMOR_ENTITY_GLINT = create("armor_entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_173079_).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ENTITY, true, false)).setWriteMaskState(f_110115_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(f_110151_).setLayeringState(f_110119_).createCompositeState(false));

    private static final RenderType GLINT_TRANSLUCENT = create("glint_translucent", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_173080_).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false)).setWriteMaskState(f_110115_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(f_110150_).setOutputState(f_110129_).createCompositeState(false));

    private static final RenderType GLINT = create("glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_173081_).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false)).setWriteMaskState(f_110115_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(f_110150_).createCompositeState(false));

    private static final RenderType GLINT_DIRECT = create("glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_173082_).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false)).setWriteMaskState(f_110115_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(f_110150_).createCompositeState(false));

    private static final RenderType ENTITY_GLINT = create("entity_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ENTITY, true, false)).setWriteMaskState(f_110115_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setOutputState(f_110129_).setTexturingState(f_110151_).createCompositeState(false));

    private static final RenderType ENTITY_GLINT_DIRECT = create("entity_glint_direct", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_173084_).setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ENTITY, true, false)).setWriteMaskState(f_110115_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(f_110151_).createCompositeState(false));

    private static final Function<ResourceLocation, RenderType> CRUMBLING = Util.memoize((Function<ResourceLocation, RenderType>) (p_286174_ -> {
        RenderStateShard.TextureStateShard $$1 = new RenderStateShard.TextureStateShard(p_286174_, false, false);
        return create("crumbling", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173085_).setTextureState($$1).setTransparencyState(f_110138_).setWriteMaskState(f_110115_).setLayeringState(f_110118_).createCompositeState(false));
    }));

    private static final Function<ResourceLocation, RenderType> TEXT = Util.memoize((Function<ResourceLocation, RenderType>) (p_286161_ -> create("text", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173086_).setTextureState(new RenderStateShard.TextureStateShard(p_286161_, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).createCompositeState(false))));

    private static final RenderType TEXT_BACKGROUND = create("text_background", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_268568_).setTextureState(f_110147_).setTransparencyState(f_110139_).setLightmapState(f_110152_).createCompositeState(false));

    private static final Function<ResourceLocation, RenderType> TEXT_INTENSITY = Util.memoize((Function<ResourceLocation, RenderType>) (p_286172_ -> create("text_intensity", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173087_).setTextureState(new RenderStateShard.TextureStateShard(p_286172_, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).createCompositeState(false))));

    private static final Function<ResourceLocation, RenderType> TEXT_POLYGON_OFFSET = Util.memoize((Function<ResourceLocation, RenderType>) (p_286152_ -> create("text_polygon_offset", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173086_).setTextureState(new RenderStateShard.TextureStateShard(p_286152_, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setLayeringState(f_110118_).createCompositeState(false))));

    private static final Function<ResourceLocation, RenderType> TEXT_INTENSITY_POLYGON_OFFSET = Util.memoize((Function<ResourceLocation, RenderType>) (p_286175_ -> create("text_intensity_polygon_offset", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173087_).setTextureState(new RenderStateShard.TextureStateShard(p_286175_, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setLayeringState(f_110118_).createCompositeState(false))));

    private static final Function<ResourceLocation, RenderType> TEXT_SEE_THROUGH = Util.memoize((Function<ResourceLocation, RenderType>) (p_286158_ -> create("text_see_through", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173088_).setTextureState(new RenderStateShard.TextureStateShard(p_286158_, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setDepthTestState(f_110111_).setWriteMaskState(f_110115_).createCompositeState(false))));

    private static final RenderType TEXT_BACKGROUND_SEE_THROUGH = create("text_background_see_through", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_268491_).setTextureState(f_110147_).setTransparencyState(f_110139_).setLightmapState(f_110152_).setDepthTestState(f_110111_).setWriteMaskState(f_110115_).createCompositeState(false));

    private static final Function<ResourceLocation, RenderType> TEXT_INTENSITY_SEE_THROUGH = Util.memoize((Function<ResourceLocation, RenderType>) (p_286168_ -> create("text_intensity_see_through", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173090_).setTextureState(new RenderStateShard.TextureStateShard(p_286168_, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setDepthTestState(f_110111_).setWriteMaskState(f_110115_).createCompositeState(false))));

    private static final RenderType LIGHTNING = create("lightning", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173091_).setWriteMaskState(f_110114_).setTransparencyState(f_110136_).setOutputState(f_110127_).createCompositeState(false));

    private static final RenderType TRIPWIRE = create("tripwire", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 262144, true, true, tripwireState());

    private static final RenderType END_PORTAL = create("end_portal", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173093_).setTextureState(RenderStateShard.MultiTextureStateShard.builder().add(TheEndPortalRenderer.END_SKY_LOCATION, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false).build()).createCompositeState(false));

    private static final RenderType END_GATEWAY = create("end_gateway", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173094_).setTextureState(RenderStateShard.MultiTextureStateShard.builder().add(TheEndPortalRenderer.END_SKY_LOCATION, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false).build()).createCompositeState(false));

    public static final RenderType.CompositeRenderType LINES = create("lines", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, RenderType.CompositeState.builder().setShaderState(f_173095_).setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty())).setLayeringState(f_110119_).setTransparencyState(f_110139_).setOutputState(f_110129_).setWriteMaskState(f_110114_).setCullState(f_110110_).createCompositeState(false));

    public static final RenderType.CompositeRenderType LINE_STRIP = create("line_strip", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINE_STRIP, 256, RenderType.CompositeState.builder().setShaderState(f_173095_).setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty())).setLayeringState(f_110119_).setTransparencyState(f_110139_).setOutputState(f_110129_).setWriteMaskState(f_110114_).setCullState(f_110110_).createCompositeState(false));

    private static final Function<Double, RenderType.CompositeRenderType> DEBUG_LINE_STRIP = Util.memoize((Function<Double, RenderType.CompositeRenderType>) (p_286162_ -> create("debug_line_strip", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.DEBUG_LINE_STRIP, 256, RenderType.CompositeState.builder().setShaderState(f_173104_).setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(p_286162_))).setTransparencyState(f_110134_).setCullState(f_110110_).createCompositeState(false))));

    private static final RenderType.CompositeRenderType DEBUG_FILLED_BOX = create("debug_filled_box", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP, 131072, false, true, RenderType.CompositeState.builder().setShaderState(f_173104_).setLayeringState(f_110119_).setTransparencyState(f_110139_).createCompositeState(false));

    private static final RenderType.CompositeRenderType DEBUG_QUADS = create("debug_quads", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 131072, false, true, RenderType.CompositeState.builder().setShaderState(f_173104_).setTransparencyState(f_110139_).setCullState(f_110110_).createCompositeState(false));

    private static final RenderType.CompositeRenderType DEBUG_SECTION_QUADS = create("debug_section_quads", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 131072, false, true, RenderType.CompositeState.builder().setShaderState(f_173104_).setLayeringState(f_110119_).setTransparencyState(f_110139_).setCullState(f_110158_).createCompositeState(false));

    private static final RenderType.CompositeRenderType GUI = create("gui", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_285573_).setTransparencyState(f_110139_).setDepthTestState(f_110113_).createCompositeState(false));

    private static final RenderType.CompositeRenderType GUI_OVERLAY = create("gui_overlay", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_285619_).setTransparencyState(f_110139_).setDepthTestState(f_110111_).setWriteMaskState(f_110115_).createCompositeState(false));

    private static final RenderType.CompositeRenderType GUI_TEXT_HIGHLIGHT = create("gui_text_highlight", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_285642_).setTransparencyState(f_110139_).setDepthTestState(f_110111_).setColorLogicState(f_285603_).createCompositeState(false));

    private static final RenderType.CompositeRenderType GUI_GHOST_RECIPE_OVERLAY = create("gui_ghost_recipe_overlay", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_285582_).setTransparencyState(f_110139_).setDepthTestState(f_285579_).setWriteMaskState(f_110115_).createCompositeState(false));

    private static final ImmutableList<RenderType> CHUNK_BUFFER_LAYERS = ImmutableList.of(solid(), cutoutMipped(), cutout(), translucent(), tripwire());

    private final VertexFormat format;

    private final VertexFormat.Mode mode;

    private final int bufferSize;

    private final boolean affectsCrumbling;

    private final boolean sortOnUpload;

    private final Optional<RenderType> asOptional;

    public static RenderType solid() {
        return SOLID;
    }

    public static RenderType cutoutMipped() {
        return CUTOUT_MIPPED;
    }

    public static RenderType cutout() {
        return CUTOUT;
    }

    private static RenderType.CompositeState translucentState(RenderStateShard.ShaderStateShard renderStateShardShaderStateShard0) {
        return RenderType.CompositeState.builder().setLightmapState(f_110152_).setShaderState(renderStateShardShaderStateShard0).setTextureState(f_110145_).setTransparencyState(f_110139_).setOutputState(f_110125_).createCompositeState(true);
    }

    public static RenderType translucent() {
        return TRANSLUCENT;
    }

    private static RenderType.CompositeState translucentMovingBlockState() {
        return RenderType.CompositeState.builder().setLightmapState(f_110152_).setShaderState(f_173109_).setTextureState(f_110145_).setTransparencyState(f_110139_).setOutputState(f_110129_).createCompositeState(true);
    }

    public static RenderType translucentMovingBlock() {
        return TRANSLUCENT_MOVING_BLOCK;
    }

    public static RenderType translucentNoCrumbling() {
        return TRANSLUCENT_NO_CRUMBLING;
    }

    public static RenderType armorCutoutNoCull(ResourceLocation resourceLocation0) {
        return (RenderType) ARMOR_CUTOUT_NO_CULL.apply(resourceLocation0);
    }

    public static RenderType entitySolid(ResourceLocation resourceLocation0) {
        return (RenderType) ENTITY_SOLID.apply(resourceLocation0);
    }

    public static RenderType entityCutout(ResourceLocation resourceLocation0) {
        return (RenderType) ENTITY_CUTOUT.apply(resourceLocation0);
    }

    public static RenderType entityCutoutNoCull(ResourceLocation resourceLocation0, boolean boolean1) {
        return (RenderType) ENTITY_CUTOUT_NO_CULL.apply(resourceLocation0, boolean1);
    }

    public static RenderType entityCutoutNoCull(ResourceLocation resourceLocation0) {
        return entityCutoutNoCull(resourceLocation0, true);
    }

    public static RenderType entityCutoutNoCullZOffset(ResourceLocation resourceLocation0, boolean boolean1) {
        return (RenderType) ENTITY_CUTOUT_NO_CULL_Z_OFFSET.apply(resourceLocation0, boolean1);
    }

    public static RenderType entityCutoutNoCullZOffset(ResourceLocation resourceLocation0) {
        return entityCutoutNoCullZOffset(resourceLocation0, true);
    }

    public static RenderType itemEntityTranslucentCull(ResourceLocation resourceLocation0) {
        return (RenderType) ITEM_ENTITY_TRANSLUCENT_CULL.apply(resourceLocation0);
    }

    public static RenderType entityTranslucentCull(ResourceLocation resourceLocation0) {
        return (RenderType) ENTITY_TRANSLUCENT_CULL.apply(resourceLocation0);
    }

    public static RenderType entityTranslucent(ResourceLocation resourceLocation0, boolean boolean1) {
        return (RenderType) ENTITY_TRANSLUCENT.apply(resourceLocation0, boolean1);
    }

    public static RenderType entityTranslucent(ResourceLocation resourceLocation0) {
        return entityTranslucent(resourceLocation0, true);
    }

    public static RenderType entityTranslucentEmissive(ResourceLocation resourceLocation0, boolean boolean1) {
        return (RenderType) ENTITY_TRANSLUCENT_EMISSIVE.apply(resourceLocation0, boolean1);
    }

    public static RenderType entityTranslucentEmissive(ResourceLocation resourceLocation0) {
        return entityTranslucentEmissive(resourceLocation0, true);
    }

    public static RenderType entitySmoothCutout(ResourceLocation resourceLocation0) {
        return (RenderType) ENTITY_SMOOTH_CUTOUT.apply(resourceLocation0);
    }

    public static RenderType beaconBeam(ResourceLocation resourceLocation0, boolean boolean1) {
        return (RenderType) BEACON_BEAM.apply(resourceLocation0, boolean1);
    }

    public static RenderType entityDecal(ResourceLocation resourceLocation0) {
        return (RenderType) ENTITY_DECAL.apply(resourceLocation0);
    }

    public static RenderType entityNoOutline(ResourceLocation resourceLocation0) {
        return (RenderType) ENTITY_NO_OUTLINE.apply(resourceLocation0);
    }

    public static RenderType entityShadow(ResourceLocation resourceLocation0) {
        return (RenderType) ENTITY_SHADOW.apply(resourceLocation0);
    }

    public static RenderType dragonExplosionAlpha(ResourceLocation resourceLocation0) {
        return (RenderType) DRAGON_EXPLOSION_ALPHA.apply(resourceLocation0);
    }

    public static RenderType eyes(ResourceLocation resourceLocation0) {
        return (RenderType) EYES.apply(resourceLocation0);
    }

    public static RenderType energySwirl(ResourceLocation resourceLocation0, float float1, float float2) {
        return create("energy_swirl", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173074_).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation0, false, false)).setTexturingState(new RenderStateShard.OffsetTexturingStateShard(float1, float2)).setTransparencyState(f_110135_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(false));
    }

    public static RenderType leash() {
        return LEASH;
    }

    public static RenderType waterMask() {
        return WATER_MASK;
    }

    public static RenderType outline(ResourceLocation resourceLocation0) {
        return (RenderType) RenderType.CompositeRenderType.OUTLINE.apply(resourceLocation0, f_110110_);
    }

    public static RenderType armorGlint() {
        return ARMOR_GLINT;
    }

    public static RenderType armorEntityGlint() {
        return ARMOR_ENTITY_GLINT;
    }

    public static RenderType glintTranslucent() {
        return GLINT_TRANSLUCENT;
    }

    public static RenderType glint() {
        return GLINT;
    }

    public static RenderType glintDirect() {
        return GLINT_DIRECT;
    }

    public static RenderType entityGlint() {
        return ENTITY_GLINT;
    }

    public static RenderType entityGlintDirect() {
        return ENTITY_GLINT_DIRECT;
    }

    public static RenderType crumbling(ResourceLocation resourceLocation0) {
        return (RenderType) CRUMBLING.apply(resourceLocation0);
    }

    public static RenderType text(ResourceLocation resourceLocation0) {
        return (RenderType) TEXT.apply(resourceLocation0);
    }

    public static RenderType textBackground() {
        return TEXT_BACKGROUND;
    }

    public static RenderType textIntensity(ResourceLocation resourceLocation0) {
        return (RenderType) TEXT_INTENSITY.apply(resourceLocation0);
    }

    public static RenderType textPolygonOffset(ResourceLocation resourceLocation0) {
        return (RenderType) TEXT_POLYGON_OFFSET.apply(resourceLocation0);
    }

    public static RenderType textIntensityPolygonOffset(ResourceLocation resourceLocation0) {
        return (RenderType) TEXT_INTENSITY_POLYGON_OFFSET.apply(resourceLocation0);
    }

    public static RenderType textSeeThrough(ResourceLocation resourceLocation0) {
        return (RenderType) TEXT_SEE_THROUGH.apply(resourceLocation0);
    }

    public static RenderType textBackgroundSeeThrough() {
        return TEXT_BACKGROUND_SEE_THROUGH;
    }

    public static RenderType textIntensitySeeThrough(ResourceLocation resourceLocation0) {
        return (RenderType) TEXT_INTENSITY_SEE_THROUGH.apply(resourceLocation0);
    }

    public static RenderType lightning() {
        return LIGHTNING;
    }

    private static RenderType.CompositeState tripwireState() {
        return RenderType.CompositeState.builder().setLightmapState(f_110152_).setShaderState(f_173092_).setTextureState(f_110145_).setTransparencyState(f_110139_).setOutputState(f_110127_).createCompositeState(true);
    }

    public static RenderType tripwire() {
        return TRIPWIRE;
    }

    public static RenderType endPortal() {
        return END_PORTAL;
    }

    public static RenderType endGateway() {
        return END_GATEWAY;
    }

    public static RenderType lines() {
        return LINES;
    }

    public static RenderType lineStrip() {
        return LINE_STRIP;
    }

    public static RenderType debugLineStrip(double double0) {
        return (RenderType) DEBUG_LINE_STRIP.apply(double0);
    }

    public static RenderType debugFilledBox() {
        return DEBUG_FILLED_BOX;
    }

    public static RenderType debugQuads() {
        return DEBUG_QUADS;
    }

    public static RenderType debugSectionQuads() {
        return DEBUG_SECTION_QUADS;
    }

    public static RenderType gui() {
        return GUI;
    }

    public static RenderType guiOverlay() {
        return GUI_OVERLAY;
    }

    public static RenderType guiTextHighlight() {
        return GUI_TEXT_HIGHLIGHT;
    }

    public static RenderType guiGhostRecipeOverlay() {
        return GUI_GHOST_RECIPE_OVERLAY;
    }

    public RenderType(String string0, VertexFormat vertexFormat1, VertexFormat.Mode vertexFormatMode2, int int3, boolean boolean4, boolean boolean5, Runnable runnable6, Runnable runnable7) {
        super(string0, runnable6, runnable7);
        this.format = vertexFormat1;
        this.mode = vertexFormatMode2;
        this.bufferSize = int3;
        this.affectsCrumbling = boolean4;
        this.sortOnUpload = boolean5;
        this.asOptional = Optional.of(this);
    }

    static RenderType.CompositeRenderType create(String string0, VertexFormat vertexFormat1, VertexFormat.Mode vertexFormatMode2, int int3, RenderType.CompositeState renderTypeCompositeState4) {
        return create(string0, vertexFormat1, vertexFormatMode2, int3, false, false, renderTypeCompositeState4);
    }

    private static RenderType.CompositeRenderType create(String string0, VertexFormat vertexFormat1, VertexFormat.Mode vertexFormatMode2, int int3, boolean boolean4, boolean boolean5, RenderType.CompositeState renderTypeCompositeState6) {
        return new RenderType.CompositeRenderType(string0, vertexFormat1, vertexFormatMode2, int3, boolean4, boolean5, renderTypeCompositeState6);
    }

    public void end(BufferBuilder bufferBuilder0, VertexSorting vertexSorting1) {
        if (bufferBuilder0.building()) {
            if (this.sortOnUpload) {
                bufferBuilder0.setQuadSorting(vertexSorting1);
            }
            BufferBuilder.RenderedBuffer $$2 = bufferBuilder0.end();
            this.m_110185_();
            BufferUploader.drawWithShader($$2);
            this.m_110188_();
        }
    }

    @Override
    public String toString() {
        return this.f_110133_;
    }

    public static List<RenderType> chunkBufferLayers() {
        return CHUNK_BUFFER_LAYERS;
    }

    public int bufferSize() {
        return this.bufferSize;
    }

    public VertexFormat format() {
        return this.format;
    }

    public VertexFormat.Mode mode() {
        return this.mode;
    }

    public Optional<RenderType> outline() {
        return Optional.empty();
    }

    public boolean isOutline() {
        return false;
    }

    public boolean affectsCrumbling() {
        return this.affectsCrumbling;
    }

    public boolean canConsolidateConsecutiveGeometry() {
        return !this.mode.connectedPrimitives;
    }

    public Optional<RenderType> asOptional() {
        return this.asOptional;
    }

    private static final class CompositeRenderType extends RenderType {

        static final BiFunction<ResourceLocation, RenderStateShard.CullStateShard, RenderType> OUTLINE = Util.memoize((BiFunction<ResourceLocation, RenderStateShard.CullStateShard, RenderType>) ((p_286176_, p_286177_) -> RenderType.create("outline", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(f_173077_).setTextureState(new RenderStateShard.TextureStateShard(p_286176_, false, false)).setCullState(p_286177_).setDepthTestState(f_110111_).setOutputState(f_110124_).createCompositeState(RenderType.OutlineProperty.IS_OUTLINE))));

        private final RenderType.CompositeState state;

        private final Optional<RenderType> outline;

        private final boolean isOutline;

        CompositeRenderType(String string0, VertexFormat vertexFormat1, VertexFormat.Mode vertexFormatMode2, int int3, boolean boolean4, boolean boolean5, RenderType.CompositeState renderTypeCompositeState6) {
            super(string0, vertexFormat1, vertexFormatMode2, int3, boolean4, boolean5, () -> renderTypeCompositeState6.states.forEach(RenderStateShard::m_110185_), () -> renderTypeCompositeState6.states.forEach(RenderStateShard::m_110188_));
            this.state = renderTypeCompositeState6;
            this.outline = renderTypeCompositeState6.outlineProperty == RenderType.OutlineProperty.AFFECTS_OUTLINE ? renderTypeCompositeState6.textureState.cutoutTexture().map(p_173270_ -> (RenderType) OUTLINE.apply(p_173270_, renderTypeCompositeState6.cullState)) : Optional.empty();
            this.isOutline = renderTypeCompositeState6.outlineProperty == RenderType.OutlineProperty.IS_OUTLINE;
        }

        @Override
        public Optional<RenderType> outline() {
            return this.outline;
        }

        @Override
        public boolean isOutline() {
            return this.isOutline;
        }

        protected final RenderType.CompositeState state() {
            return this.state;
        }

        @Override
        public String toString() {
            return "RenderType[" + this.f_110133_ + ":" + this.state + "]";
        }
    }

    protected static final class CompositeState {

        final RenderStateShard.EmptyTextureStateShard textureState;

        private final RenderStateShard.ShaderStateShard shaderState;

        private final RenderStateShard.TransparencyStateShard transparencyState;

        private final RenderStateShard.DepthTestStateShard depthTestState;

        final RenderStateShard.CullStateShard cullState;

        private final RenderStateShard.LightmapStateShard lightmapState;

        private final RenderStateShard.OverlayStateShard overlayState;

        private final RenderStateShard.LayeringStateShard layeringState;

        private final RenderStateShard.OutputStateShard outputState;

        private final RenderStateShard.TexturingStateShard texturingState;

        private final RenderStateShard.WriteMaskStateShard writeMaskState;

        private final RenderStateShard.LineStateShard lineState;

        private final RenderStateShard.ColorLogicStateShard colorLogicState;

        final RenderType.OutlineProperty outlineProperty;

        final ImmutableList<RenderStateShard> states;

        CompositeState(RenderStateShard.EmptyTextureStateShard renderStateShardEmptyTextureStateShard0, RenderStateShard.ShaderStateShard renderStateShardShaderStateShard1, RenderStateShard.TransparencyStateShard renderStateShardTransparencyStateShard2, RenderStateShard.DepthTestStateShard renderStateShardDepthTestStateShard3, RenderStateShard.CullStateShard renderStateShardCullStateShard4, RenderStateShard.LightmapStateShard renderStateShardLightmapStateShard5, RenderStateShard.OverlayStateShard renderStateShardOverlayStateShard6, RenderStateShard.LayeringStateShard renderStateShardLayeringStateShard7, RenderStateShard.OutputStateShard renderStateShardOutputStateShard8, RenderStateShard.TexturingStateShard renderStateShardTexturingStateShard9, RenderStateShard.WriteMaskStateShard renderStateShardWriteMaskStateShard10, RenderStateShard.LineStateShard renderStateShardLineStateShard11, RenderStateShard.ColorLogicStateShard renderStateShardColorLogicStateShard12, RenderType.OutlineProperty renderTypeOutlineProperty13) {
            this.textureState = renderStateShardEmptyTextureStateShard0;
            this.shaderState = renderStateShardShaderStateShard1;
            this.transparencyState = renderStateShardTransparencyStateShard2;
            this.depthTestState = renderStateShardDepthTestStateShard3;
            this.cullState = renderStateShardCullStateShard4;
            this.lightmapState = renderStateShardLightmapStateShard5;
            this.overlayState = renderStateShardOverlayStateShard6;
            this.layeringState = renderStateShardLayeringStateShard7;
            this.outputState = renderStateShardOutputStateShard8;
            this.texturingState = renderStateShardTexturingStateShard9;
            this.writeMaskState = renderStateShardWriteMaskStateShard10;
            this.lineState = renderStateShardLineStateShard11;
            this.colorLogicState = renderStateShardColorLogicStateShard12;
            this.outlineProperty = renderTypeOutlineProperty13;
            this.states = ImmutableList.of(this.textureState, this.shaderState, this.transparencyState, this.depthTestState, this.cullState, this.lightmapState, this.overlayState, this.layeringState, this.outputState, this.texturingState, this.writeMaskState, this.colorLogicState, new RenderStateShard[] { this.lineState });
        }

        public String toString() {
            return "CompositeState[" + this.states + ", outlineProperty=" + this.outlineProperty + "]";
        }

        public static RenderType.CompositeState.CompositeStateBuilder builder() {
            return new RenderType.CompositeState.CompositeStateBuilder();
        }

        public static class CompositeStateBuilder {

            private RenderStateShard.EmptyTextureStateShard textureState = RenderStateShard.NO_TEXTURE;

            private RenderStateShard.ShaderStateShard shaderState = RenderStateShard.NO_SHADER;

            private RenderStateShard.TransparencyStateShard transparencyState = RenderStateShard.NO_TRANSPARENCY;

            private RenderStateShard.DepthTestStateShard depthTestState = RenderStateShard.LEQUAL_DEPTH_TEST;

            private RenderStateShard.CullStateShard cullState = RenderStateShard.CULL;

            private RenderStateShard.LightmapStateShard lightmapState = RenderStateShard.NO_LIGHTMAP;

            private RenderStateShard.OverlayStateShard overlayState = RenderStateShard.NO_OVERLAY;

            private RenderStateShard.LayeringStateShard layeringState = RenderStateShard.NO_LAYERING;

            private RenderStateShard.OutputStateShard outputState = RenderStateShard.MAIN_TARGET;

            private RenderStateShard.TexturingStateShard texturingState = RenderStateShard.DEFAULT_TEXTURING;

            private RenderStateShard.WriteMaskStateShard writeMaskState = RenderStateShard.COLOR_DEPTH_WRITE;

            private RenderStateShard.LineStateShard lineState = RenderStateShard.DEFAULT_LINE;

            private RenderStateShard.ColorLogicStateShard colorLogicState = RenderStateShard.NO_COLOR_LOGIC;

            CompositeStateBuilder() {
            }

            public RenderType.CompositeState.CompositeStateBuilder setTextureState(RenderStateShard.EmptyTextureStateShard renderStateShardEmptyTextureStateShard0) {
                this.textureState = renderStateShardEmptyTextureStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setShaderState(RenderStateShard.ShaderStateShard renderStateShardShaderStateShard0) {
                this.shaderState = renderStateShardShaderStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setTransparencyState(RenderStateShard.TransparencyStateShard renderStateShardTransparencyStateShard0) {
                this.transparencyState = renderStateShardTransparencyStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setDepthTestState(RenderStateShard.DepthTestStateShard renderStateShardDepthTestStateShard0) {
                this.depthTestState = renderStateShardDepthTestStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setCullState(RenderStateShard.CullStateShard renderStateShardCullStateShard0) {
                this.cullState = renderStateShardCullStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setLightmapState(RenderStateShard.LightmapStateShard renderStateShardLightmapStateShard0) {
                this.lightmapState = renderStateShardLightmapStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setOverlayState(RenderStateShard.OverlayStateShard renderStateShardOverlayStateShard0) {
                this.overlayState = renderStateShardOverlayStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setLayeringState(RenderStateShard.LayeringStateShard renderStateShardLayeringStateShard0) {
                this.layeringState = renderStateShardLayeringStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setOutputState(RenderStateShard.OutputStateShard renderStateShardOutputStateShard0) {
                this.outputState = renderStateShardOutputStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setTexturingState(RenderStateShard.TexturingStateShard renderStateShardTexturingStateShard0) {
                this.texturingState = renderStateShardTexturingStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setWriteMaskState(RenderStateShard.WriteMaskStateShard renderStateShardWriteMaskStateShard0) {
                this.writeMaskState = renderStateShardWriteMaskStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setLineState(RenderStateShard.LineStateShard renderStateShardLineStateShard0) {
                this.lineState = renderStateShardLineStateShard0;
                return this;
            }

            public RenderType.CompositeState.CompositeStateBuilder setColorLogicState(RenderStateShard.ColorLogicStateShard renderStateShardColorLogicStateShard0) {
                this.colorLogicState = renderStateShardColorLogicStateShard0;
                return this;
            }

            public RenderType.CompositeState createCompositeState(boolean boolean0) {
                return this.createCompositeState(boolean0 ? RenderType.OutlineProperty.AFFECTS_OUTLINE : RenderType.OutlineProperty.NONE);
            }

            public RenderType.CompositeState createCompositeState(RenderType.OutlineProperty renderTypeOutlineProperty0) {
                return new RenderType.CompositeState(this.textureState, this.shaderState, this.transparencyState, this.depthTestState, this.cullState, this.lightmapState, this.overlayState, this.layeringState, this.outputState, this.texturingState, this.writeMaskState, this.lineState, this.colorLogicState, renderTypeOutlineProperty0);
            }
        }
    }

    static enum OutlineProperty {

        NONE("none"), IS_OUTLINE("is_outline"), AFFECTS_OUTLINE("affects_outline");

        private final String name;

        private OutlineProperty(String p_110702_) {
            this.name = p_110702_;
        }

        public String toString() {
            return this.name;
        }
    }
}