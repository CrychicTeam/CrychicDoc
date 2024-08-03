package yesman.epicfight.client.renderer;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EpicFightRenderTypes extends RenderType {

    private static final Map<RenderType, RenderType> renderTypeCache = Maps.newHashMap();

    private static final Function<ResourceLocation, RenderType> ENTITY_INDICATOR = Util.memoize((Function<ResourceLocation, RenderType>) (textureLocation -> {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(f_173102_).setTextureState(new RenderStateShard.TextureStateShard(textureLocation, false, false)).setTransparencyState(f_110134_).setLightmapState(f_110153_).setOverlayState(f_110155_).createCompositeState(true);
        return m_173215_("epicfight:entity_indicator", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, true, false, state);
    }));

    private static final RenderType DEBUG_COLLIDER = m_173215_("epicfight:debug_collider", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINE_STRIP, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173104_).setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty())).setLayeringState(f_110119_).setTransparencyState(f_110139_).setOutputState(f_110129_).setWriteMaskState(f_110114_).setCullState(f_110110_).createCompositeState(false));

    private static final RenderType DEBUG_QUADS = m_173215_("epicfight:debug_quad", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173104_).setLayeringState(f_110119_).setTransparencyState(f_110134_).setWriteMaskState(f_110114_).setCullState(f_110110_).createCompositeState(false));

    public static RenderType triangles(RenderType renderType) {
        return (RenderType) renderTypeCache.computeIfAbsent(renderType, key -> {
            RenderType trianglesRenderType = null;
            if (renderType instanceof RenderType.CompositeRenderType compositeRenderType) {
                trianglesRenderType = new RenderType.CompositeRenderType(renderType.f_110133_, renderType.format, VertexFormat.Mode.TRIANGLES, renderType.bufferSize(), renderType.affectsCrumbling(), renderType.sortOnUpload, compositeRenderType.state);
            }
            return trianglesRenderType;
        });
    }

    private EpicFightRenderTypes(String name, VertexFormat format, VertexFormat.Mode drawingMode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setup, Runnable clean) {
        super(name, format, drawingMode, bufferSize, affectsCrumbling, sortOnUpload, setup, clean);
    }

    private static VertexConsumer getTriangleBuffer(MultiBufferSource bufferSource, RenderType renderType) {
        RenderType triangleRenderType = triangles(renderType);
        if (bufferSource instanceof MultiBufferSource.BufferSource cast) {
            if (cast.fixedBuffers.containsKey(renderType)) {
                cast.fixedBuffers.computeIfAbsent(triangleRenderType, key -> new BufferBuilder(renderType.bufferSize()));
                return cast.getBuffer(triangleRenderType);
            } else {
                return cast.getBuffer(triangleRenderType);
            }
        } else {
            return bufferSource.getBuffer(triangleRenderType);
        }
    }

    public static VertexConsumer getArmorFoilBufferTriangles(MultiBufferSource bufferSource, RenderType renderType, boolean isEntity, boolean hasEffect) {
        return hasEffect ? VertexMultiConsumer.create(getTriangleBuffer(bufferSource, isEntity ? RenderType.armorGlint() : RenderType.armorEntityGlint()), getTriangleBuffer(bufferSource, renderType)) : getTriangleBuffer(bufferSource, renderType);
    }

    public static RenderType entityIndicator(ResourceLocation locationIn) {
        return (RenderType) ENTITY_INDICATOR.apply(locationIn);
    }

    public static RenderType debugCollider() {
        return DEBUG_COLLIDER;
    }

    public static RenderType debugQuads() {
        return DEBUG_QUADS;
    }
}