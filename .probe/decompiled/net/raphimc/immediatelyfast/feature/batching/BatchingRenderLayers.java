package net.raphimc.immediatelyfast.feature.batching;

import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import org.apache.commons.lang3.tuple.Pair;

public class BatchingRenderLayers {

    public static final BiFunction<Integer, BlendFuncDepthFuncState, RenderType> COLORED_TEXTURE = Util.memoize((BiFunction<Integer, BlendFuncDepthFuncState, RenderType>) ((id, blendFuncDepthFunc) -> new BatchingRenderLayers.ImmediatelyFastRenderLayer("texture", VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR, false, () -> {
        blendFuncDepthFunc.saveAndApply();
        RenderSystem.setShaderTexture(0, id);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::m_172820_);
    }, blendFuncDepthFunc::revert)));

    public static <A> Function<A, RenderType> memoizeTemp(Function<A, RenderType> function) {
        return new Function<A, RenderType>() {

            private final Map<A, RenderType> cache = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.SECONDS).build().asMap();

            public RenderType apply(A arg1) {
                return (RenderType) this.cache.computeIfAbsent(arg1, function);
            }
        };
    }

    public static <A, B> BiFunction<A, B, RenderType> memoizeTemp(BiFunction<A, B, RenderType> function) {
        return new BiFunction<A, B, RenderType>() {

            private final Map<Pair<A, B>, RenderType> cache = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.SECONDS).build().asMap();

            public RenderType apply(A arg1, B arg2) {
                return (RenderType) this.cache.computeIfAbsent(Pair.of(arg1, arg2), pair -> (RenderType) function.apply(pair.getLeft(), pair.getRight()));
            }
        };
    }

    private static class ImmediatelyFastRenderLayer extends RenderType {

        private ImmediatelyFastRenderLayer(String name, VertexFormat.Mode drawMode, VertexFormat vertexFormat, boolean translucent, Runnable startAction, Runnable endAction) {
            super("immediatelyfast_" + name, vertexFormat, drawMode, 2048, false, translucent, startAction, endAction);
        }
    }

    public static class WrappedRenderLayer extends RenderType {

        public WrappedRenderLayer(RenderType renderLayer, Runnable additionalStartAction, Runnable additionalEndAction) {
            super(renderLayer.f_110133_, renderLayer.format(), renderLayer.mode(), renderLayer.bufferSize(), renderLayer.affectsCrumbling(), renderLayer.sortOnUpload, () -> {
                renderLayer.m_110185_();
                additionalStartAction.run();
            }, () -> {
                renderLayer.m_110188_();
                additionalEndAction.run();
            });
        }
    }
}