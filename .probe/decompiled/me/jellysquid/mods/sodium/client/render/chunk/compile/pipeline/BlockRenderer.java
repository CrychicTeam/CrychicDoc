package me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.List;
import me.jellysquid.mods.sodium.client.compat.ccl.SinkingVertexBuilder;
import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
import me.jellysquid.mods.sodium.client.model.color.ColorProviderRegistry;
import me.jellysquid.mods.sodium.client.model.light.LightMode;
import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.light.LightPipelineProvider;
import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
import me.jellysquid.mods.sodium.client.model.quad.BakedQuadView;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadOrientation;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import me.jellysquid.mods.sodium.client.util.DirectionUtil;
import me.jellysquid.mods.sodium.client.util.ModelQuadUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.phys.Vec3;
import org.embeddedt.embeddium.api.BlockRendererRegistry;
import org.embeddedt.embeddium.api.model.EmbeddiumBakedModelExtension;
import org.embeddedt.embeddium.render.chunk.ChunkColorWriter;
import org.embeddedt.embeddium.render.frapi.FRAPIModelUtils;
import org.embeddedt.embeddium.render.frapi.FRAPIRenderHandler;
import org.embeddedt.embeddium.render.frapi.IndigoBlockRenderContext;

public class BlockRenderer {

    private static final PoseStack EMPTY_STACK = new PoseStack();

    private final RandomSource random = new SingleThreadedRandomSource(42L);

    private final ColorProviderRegistry colorProviderRegistry;

    private final BlockOcclusionCache occlusionCache;

    private final QuadLightData quadLightData = new QuadLightData();

    private final LightPipelineProvider lighters;

    private final ChunkVertexEncoder.Vertex[] vertices = ChunkVertexEncoder.Vertex.uninitializedQuad();

    private final boolean useAmbientOcclusion;

    private final int[] quadColors = new int[4];

    private boolean useReorienting;

    private final List<BlockRendererRegistry.Renderer> customRenderers = new ObjectArrayList();

    private final SinkingVertexBuilder sinkingVertexBuilder = new SinkingVertexBuilder();

    private final FRAPIRenderHandler fabricModelRenderingHandler;

    private final ChunkColorWriter colorEncoder = ChunkColorWriter.get();

    public BlockRenderer(ColorProviderRegistry colorRegistry, LightPipelineProvider lighters) {
        this.colorProviderRegistry = colorRegistry;
        this.lighters = lighters;
        this.occlusionCache = new BlockOcclusionCache();
        this.useAmbientOcclusion = Minecraft.useAmbientOcclusion();
        this.fabricModelRenderingHandler = FRAPIRenderHandler.INDIGO_PRESENT ? new IndigoBlockRenderContext(this.occlusionCache, lighters.getLightData()) : null;
    }

    public void renderModel(BlockRenderContext ctx, ChunkBuildBuffers buffers) {
        Material material = DefaultMaterials.forRenderLayer(ctx.renderLayer());
        ChunkModelBuilder meshBuilder = buffers.get(material);
        ColorProvider<BlockState> colorizer = this.colorProviderRegistry.getColorProvider(ctx.state().m_60734_());
        LightMode mode = this.getLightingMode(ctx.state(), ctx.model(), ctx.localSlice(), ctx.pos(), ctx.renderLayer());
        LightPipeline lighter = this.lighters.getLighter(mode);
        Vec3 renderOffset;
        if (ctx.state().m_271730_()) {
            renderOffset = ctx.state().m_60824_(ctx.localSlice(), ctx.pos());
        } else {
            renderOffset = Vec3.ZERO;
        }
        this.customRenderers.clear();
        BlockRendererRegistry.instance().fillCustomRenderers(this.customRenderers, ctx);
        if (!this.customRenderers.isEmpty()) {
            for (BlockRendererRegistry.Renderer customRenderer : this.customRenderers) {
                this.sinkingVertexBuilder.reset();
                BlockRendererRegistry.RenderResult result = customRenderer.renderBlock(ctx, this.random, this.sinkingVertexBuilder);
                this.sinkingVertexBuilder.flush(meshBuilder, material, ctx.origin());
                if (result == BlockRendererRegistry.RenderResult.OVERRIDE) {
                    return;
                }
            }
        }
        if (FRAPIModelUtils.isFRAPIModel(ctx.model())) {
            this.fabricModelRenderingHandler.reset();
            this.fabricModelRenderingHandler.renderEmbeddium(ctx, ctx.stack(), this.random);
            this.fabricModelRenderingHandler.flush(buffers, ctx.origin());
        } else {
            for (Direction face : DirectionUtil.ALL_DIRECTIONS) {
                List<BakedQuad> quads = this.getGeometry(ctx, face);
                if (!quads.isEmpty() && this.isFaceVisible(ctx, face)) {
                    this.renderQuadList(ctx, material, lighter, colorizer, renderOffset, meshBuilder, quads, face);
                }
            }
            List<BakedQuad> all = this.getGeometry(ctx, null);
            if (!all.isEmpty()) {
                this.renderQuadList(ctx, material, lighter, colorizer, renderOffset, meshBuilder, all, null);
            }
        }
    }

    private List<BakedQuad> getGeometry(BlockRenderContext ctx, Direction face) {
        RandomSource random = this.random;
        random.setSeed(ctx.seed());
        return ctx.model().getQuads(ctx.state(), face, random, ctx.modelData(), ctx.renderLayer());
    }

    private boolean isFaceVisible(BlockRenderContext ctx, Direction face) {
        return this.occlusionCache.shouldDrawSide(ctx.state(), ctx.localSlice(), ctx.pos(), face);
    }

    private void renderQuadList(BlockRenderContext ctx, Material material, LightPipeline lighter, ColorProvider<BlockState> colorizer, Vec3 offset, ChunkModelBuilder builder, List<BakedQuad> quads, Direction cullFace) {
        this.useReorienting = true;
        int i = 0;
        for (int quadsSize = quads.size(); i < quadsSize; i++) {
            if (!((BakedQuad) quads.get(i)).hasAmbientOcclusion()) {
                this.useReorienting = false;
                break;
            }
        }
        i = 0;
        for (int quadsSizex = quads.size(); i < quadsSizex; i++) {
            BakedQuadView quad = (BakedQuadView) quads.get(i);
            QuadLightData lightData = this.getVertexLight(ctx, quad.hasAmbientOcclusion() ? lighter : this.lighters.getLighter(LightMode.FLAT), cullFace, quad);
            int[] vertexColors = this.getVertexColors(ctx, colorizer, quad);
            this.writeGeometry(ctx, builder, offset, material, quad, vertexColors, lightData);
            TextureAtlasSprite sprite = quad.getSprite();
            if (sprite != null) {
                builder.addSprite(sprite);
            }
        }
    }

    private QuadLightData getVertexLight(BlockRenderContext ctx, LightPipeline lighter, Direction cullFace, BakedQuadView quad) {
        QuadLightData light = this.quadLightData;
        lighter.calculate(quad, ctx.pos(), light, cullFace, quad.getLightFace(), quad.hasShade());
        return light;
    }

    private int[] getVertexColors(BlockRenderContext ctx, ColorProvider<BlockState> colorProvider, BakedQuadView quad) {
        int[] vertexColors = this.quadColors;
        if (colorProvider != null && quad.hasColor()) {
            colorProvider.getColors(ctx.world(), ctx.pos(), ctx.state(), quad, vertexColors);
            for (int i = 0; i < vertexColors.length; i++) {
                vertexColors[i] |= -16777216;
            }
        } else {
            Arrays.fill(vertexColors, -1);
        }
        return vertexColors;
    }

    private void writeGeometry(BlockRenderContext ctx, ChunkModelBuilder builder, Vec3 offset, Material material, BakedQuadView quad, int[] colors, QuadLightData light) {
        ModelQuadOrientation orientation = this.useReorienting ? ModelQuadOrientation.orientByBrightness(light.br, light.lm) : ModelQuadOrientation.NORMAL;
        ChunkVertexEncoder.Vertex[] vertices = this.vertices;
        ModelQuadFacing normalFace = quad.getNormalFace();
        for (int dstIndex = 0; dstIndex < 4; dstIndex++) {
            int srcIndex = orientation.getVertexIndex(dstIndex);
            ChunkVertexEncoder.Vertex out = vertices[dstIndex];
            out.x = ctx.origin().x() + quad.getX(srcIndex) + (float) offset.x();
            out.y = ctx.origin().y() + quad.getY(srcIndex) + (float) offset.y();
            out.z = ctx.origin().z() + quad.getZ(srcIndex) + (float) offset.z();
            out.color = this.colorEncoder.writeColor(ModelQuadUtil.mixARGBColors(colors[srcIndex], quad.getColor(srcIndex)), light.br[srcIndex]);
            out.u = quad.getTexU(srcIndex);
            out.v = quad.getTexV(srcIndex);
            out.light = ModelQuadUtil.mergeBakedLight(quad.getLight(srcIndex), light.lm[srcIndex]);
        }
        ChunkMeshBufferBuilder vertexBuffer = builder.getVertexBuffer(normalFace);
        vertexBuffer.push(vertices, material);
    }

    private LightMode getLightingMode(BlockState state, BakedModel model, BlockAndTintGetter world, BlockPos pos, RenderType renderLayer) {
        return !this.useAmbientOcclusion || !model.useAmbientOcclusion(state, renderLayer) || !((EmbeddiumBakedModelExtension) model).useAmbientOcclusionWithLightEmission(state, renderLayer) && state.getLightEmission(world, pos) != 0 ? LightMode.FLAT : LightMode.SMOOTH;
    }
}