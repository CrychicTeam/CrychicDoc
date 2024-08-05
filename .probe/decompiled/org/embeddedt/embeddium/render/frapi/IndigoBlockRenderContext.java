package org.embeddedt.embeddium.render.frapi;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import me.jellysquid.mods.sodium.client.compat.ccl.SinkingVertexBuilder;
import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import net.fabricmc.fabric.impl.client.indigo.renderer.aocalc.AoCalculator;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderContext;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

public class IndigoBlockRenderContext extends BlockRenderContext implements FRAPIRenderHandler {

    private final SinkingVertexBuilder[] vertexBuilderMap = new SinkingVertexBuilder[RenderType.chunkBufferLayers().size()];

    private me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext currentContext;

    private final BlockOcclusionCache occlusionCache;

    private final LightDataAccess lightDataAccess;

    private int cullChecked;

    private int cullValue;

    private static final MethodHandle FABRIC_RENDER_HANDLE;

    private static final MethodHandle FORGIFIED_RENDER_HANDLE;

    public IndigoBlockRenderContext(BlockOcclusionCache occlusionCache, LightDataAccess lightDataAccess) {
        this.occlusionCache = occlusionCache;
        this.lightDataAccess = lightDataAccess;
    }

    protected AoCalculator createAoCalc(BlockRenderInfo blockInfo) {
        return new AoCalculator(blockInfo) {

            public int light(BlockPos pos, BlockState state) {
                int data = IndigoBlockRenderContext.this.lightDataAccess.get(pos);
                return LightDataAccess.getLightmap(data);
            }

            public float ao(BlockPos pos, BlockState state) {
                return LightDataAccess.unpackAO(IndigoBlockRenderContext.this.lightDataAccess.get(pos));
            }
        };
    }

    public boolean isFaceCulled(@Nullable Direction face) {
        if (face == null) {
            return false;
        } else {
            int fM = 1 << face.ordinal();
            if ((this.cullChecked & fM) != 0) {
                return (this.cullValue & fM) != 0;
            } else {
                me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext ctx = this.currentContext;
                boolean flag = !this.occlusionCache.shouldDrawSide(ctx.state(), ctx.localSlice(), ctx.pos(), face);
                if (flag) {
                    this.cullValue |= fM;
                }
                this.cullChecked |= fM;
                return flag;
            }
        }
    }

    protected VertexConsumer getVertexConsumer(RenderType layer) {
        int id = layer.getChunkLayerId();
        if (id < 0) {
            throw new UnsupportedOperationException("Unsupported render type: " + layer);
        } else {
            SinkingVertexBuilder builder = this.vertexBuilderMap[id];
            if (builder == null) {
                builder = new SinkingVertexBuilder();
                this.vertexBuilderMap[id] = builder;
            }
            return builder;
        }
    }

    @Override
    public void reset() {
        for (SinkingVertexBuilder builder : this.vertexBuilderMap) {
            if (builder != null) {
                builder.reset();
            }
        }
        this.cullChecked = 0;
        this.cullValue = 0;
    }

    private RuntimeException processException(Throwable e) {
        return (RuntimeException) (e instanceof RuntimeException ? (RuntimeException) e : new IllegalStateException("Unexpected throwable", e));
    }

    @Override
    public void renderEmbeddium(me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext ctx, PoseStack mStack, RandomSource random) {
        this.currentContext = ctx;
        mStack.pushPose();
        try {
            if (FABRIC_RENDER_HANDLE != null) {
                FABRIC_RENDER_HANDLE.invokeExact(this, ctx.localSlice(), ctx.model(), ctx.state(), ctx.pos(), mStack, (VertexConsumer) null, true, random, ctx.seed(), OverlayTexture.NO_OVERLAY);
            } else if (FORGIFIED_RENDER_HANDLE != null) {
                FORGIFIED_RENDER_HANDLE.invokeExact(this, ctx.localSlice(), ctx.model(), ctx.state(), ctx.pos(), mStack, (VertexConsumer) null, true, random, ctx.seed(), OverlayTexture.NO_OVERLAY, ctx.modelData(), ctx.renderLayer());
            }
        } catch (Throwable var8) {
            throw this.processException(var8);
        } finally {
            mStack.popPose();
            this.currentContext = null;
        }
    }

    @Override
    public void flush(ChunkBuildBuffers buffers, Vector3fc origin) {
        for (int i = 0; i < this.vertexBuilderMap.length; i++) {
            SinkingVertexBuilder sinkingVertexBuilder = this.vertexBuilderMap[i];
            if (sinkingVertexBuilder != null && !sinkingVertexBuilder.isEmpty()) {
                Material material = DefaultMaterials.forRenderLayer((RenderType) RenderType.chunkBufferLayers().get(i));
                ChunkModelBuilder builder = buffers.get(material);
                sinkingVertexBuilder.flush(builder, material, origin);
            }
        }
    }

    static {
        MethodHandle fabricHandle = null;
        MethodHandle forgeHandle = null;
        ReflectiveOperationException forgeException = null;
        ReflectiveOperationException fabricException = null;
        try {
            fabricHandle = MethodHandles.lookup().findVirtual(BlockRenderContext.class, "render", MethodType.methodType(void.class, BlockAndTintGetter.class, BakedModel.class, BlockState.class, BlockPos.class, PoseStack.class, VertexConsumer.class, boolean.class, RandomSource.class, long.class, int.class));
        } catch (ReflectiveOperationException var6) {
            fabricException = var6;
        }
        try {
            forgeHandle = MethodHandles.lookup().findVirtual(BlockRenderContext.class, "render", MethodType.methodType(void.class, BlockAndTintGetter.class, BakedModel.class, BlockState.class, BlockPos.class, PoseStack.class, VertexConsumer.class, boolean.class, RandomSource.class, long.class, int.class, ModelData.class, RenderType.class));
        } catch (ReflectiveOperationException var5) {
            forgeException = var5;
        }
        if (fabricHandle == null && forgeHandle == null) {
            IllegalStateException ex = new IllegalStateException("Failed to find render method on BlockRenderContext.");
            if (fabricException != null) {
                ex.addSuppressed(fabricException);
            }
            if (forgeException != null) {
                ex.addSuppressed(forgeException);
            }
            throw ex;
        } else {
            FABRIC_RENDER_HANDLE = fabricHandle;
            FORGIFIED_RENDER_HANDLE = forgeHandle;
        }
    }
}