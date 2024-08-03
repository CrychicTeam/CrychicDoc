package org.embeddedt.embeddium.compat.immersive;

import blusunrize.immersiveengineering.api.utils.ResettableLazy;
import blusunrize.immersiveengineering.api.wires.Connection;
import blusunrize.immersiveengineering.api.wires.ConnectionPoint;
import blusunrize.immersiveengineering.api.wires.GlobalWireNetwork;
import blusunrize.immersiveengineering.api.wires.Connection.CatenaryData;
import blusunrize.immersiveengineering.api.wires.WireCollisionData.ConnectionSegments;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.phys.Vec3;
import org.embeddedt.embeddium.api.ChunkMeshEvent;

public class ImmersiveConnectionRenderer implements ResourceManagerReloadListener {

    private static final LoadingCache<ImmersiveConnectionRenderer.SegmentsKey, List<ImmersiveConnectionRenderer.RenderedSegment>> SEGMENT_CACHE = CacheBuilder.newBuilder().expireAfterAccess(120L, TimeUnit.SECONDS).build(CacheLoader.from(ImmersiveConnectionRenderer::renderSegmentForCache));

    private static final ResettableLazy<TextureAtlasSprite> WIRE_TEXTURE = new ResettableLazy(() -> Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(rl("block/wire")));

    private static final Material MATERIAL = DefaultMaterials.SOLID;

    public static ResourceLocation rl(String path) {
        return new ResourceLocation("immersiveengineering", path);
    }

    static void meshAppendEvent(ChunkMeshEvent event) {
        if (ImmersiveEmptyChunkChecker.hasWires(event.getSectionOrigin())) {
            event.addMeshAppender(ctx -> renderConnectionsInSection(ctx.sodiumBuildBuffers(), ctx.blockRenderView(), ctx.sectionOrigin()));
        }
    }

    @Override
    public void onResourceManagerReload(@Nonnull ResourceManager pResourceManager) {
        WIRE_TEXTURE.reset();
        resetCache();
    }

    public static void resetCache() {
        SEGMENT_CACHE.invalidateAll();
    }

    public static void renderConnectionsInSection(ChunkBuildBuffers buffers, BlockAndTintGetter region, SectionPos section) {
        GlobalWireNetwork globalNet = GlobalWireNetwork.getNetwork(Minecraft.getInstance().level);
        List<ConnectionSegments> connectionParts = globalNet.getCollisionData().getWiresIn(section);
        if (connectionParts != null && !connectionParts.isEmpty()) {
            RenderType renderType = RenderType.solid();
            ChunkModelBuilder builder = buffers.get(DefaultMaterials.forRenderLayer(renderType));
            int originX = section.minBlockX();
            int originY = section.minBlockY();
            int originZ = section.minBlockZ();
            for (ConnectionSegments connection : connectionParts) {
                ConnectionPoint connectionOrigin = connection.connection().getEndA();
                renderSegments(builder, connection, region, connectionOrigin.getX() - originX, connectionOrigin.getY() - originY, connectionOrigin.getZ() - originZ);
            }
        }
    }

    public static void renderSegments(ChunkModelBuilder out, ConnectionSegments toRender, BlockAndTintGetter level, int offX, int offY, int offZ) {
        Connection connection = toRender.connection();
        int colorRGB = connection.type.getColour(connection);
        int colorBGR = ColorARGB.toABGR(colorRGB, 255);
        double radius = connection.type.getRenderDiameter() / 2.0;
        List<ImmersiveConnectionRenderer.RenderedSegment> segments = (List<ImmersiveConnectionRenderer.RenderedSegment>) SEGMENT_CACHE.getUnchecked(new ImmersiveConnectionRenderer.SegmentsKey(radius, colorBGR, connection.getCatenaryData(), toRender.firstPointToRender(), toRender.lastPointToRender()));
        int lastLight = 0;
        for (int startPoint = 0; startPoint < segments.size(); startPoint++) {
            ImmersiveConnectionRenderer.RenderedSegment renderedSegment = (ImmersiveConnectionRenderer.RenderedSegment) segments.get(startPoint);
            if (startPoint == 0) {
                lastLight = getLight(connection, renderedSegment.offsetStart, level);
            }
            int nextLight = getLight(connection, renderedSegment.offsetEnd, level);
            renderedSegment.render(lastLight, nextLight, offX, offY, offZ, out);
            lastLight = nextLight;
        }
    }

    private static List<ImmersiveConnectionRenderer.RenderedSegment> renderSegmentForCache(ImmersiveConnectionRenderer.SegmentsKey key) {
        List<ImmersiveConnectionRenderer.RenderedSegment> segments = new ArrayList(key.endIndex() - key.beginIndex());
        for (int i = key.beginIndex(); i < key.endIndex(); i++) {
            segments.add(renderSegmentForCache(key, i));
        }
        return segments;
    }

    private static ImmersiveConnectionRenderer.RenderedSegment renderSegmentForCache(ImmersiveConnectionRenderer.SegmentsKey key, int startIndex) {
        CatenaryData catenaryData = key.catenaryShape();
        Vec3 start = key.catenaryShape().getRenderPoint(startIndex);
        Vec3 end = key.catenaryShape().getRenderPoint(startIndex + 1);
        Vec3 horNormal;
        if (key.catenaryShape().isVertical()) {
            horNormal = new Vec3(1.0, 0.0, 0.0);
        } else {
            horNormal = new Vec3(-catenaryData.delta().z, 0.0, catenaryData.delta().x).normalize();
        }
        Vec3 verticalNormal = start.subtract(end).cross(horNormal).normalize();
        Vec3 horRadius = horNormal.scale(key.radius());
        Vec3 verticalRadius = verticalNormal.scale(-key.radius());
        return new ImmersiveConnectionRenderer.RenderedSegment(renderQuad(start, end, horRadius, key.color()), renderQuad(start, end, verticalRadius, key.color()), flooredVec3(start.x, start.y, start.z), flooredVec3(end.x, end.y, end.z));
    }

    private static Vec3i flooredVec3(double x, double y, double z) {
        return new Vec3i(Mth.floor(x), Mth.floor(y), Mth.floor(z));
    }

    private static int getLight(Connection connection, Vec3i point, BlockAndTintGetter level) {
        return LevelRenderer.getLightColor(level, connection.getEndA().position().offset(point));
    }

    private static ImmersiveConnectionRenderer.Quad renderQuad(Vec3 start, Vec3 end, Vec3 radius, int color) {
        TextureAtlasSprite texture = (TextureAtlasSprite) WIRE_TEXTURE.get();
        return new ImmersiveConnectionRenderer.Quad(vertex(start.add(radius), (double) texture.getU0(), (double) texture.getV0(), color, true), vertex(end.add(radius), (double) texture.getU1(), (double) texture.getV0(), color, false), vertex(end.subtract(radius), (double) texture.getU1(), (double) texture.getV1(), color, false), vertex(start.subtract(radius), (double) texture.getU0(), (double) texture.getV1(), color, true));
    }

    private static ImmersiveConnectionRenderer.Vertex vertex(Vec3 point, double u, double v, int color, boolean lightForStart) {
        return new ImmersiveConnectionRenderer.Vertex((float) point.x, (float) point.y, (float) point.z, (float) u, (float) v, color, lightForStart);
    }

    private static record Quad(ImmersiveConnectionRenderer.Vertex v0, ImmersiveConnectionRenderer.Vertex v1, ImmersiveConnectionRenderer.Vertex v2, ImmersiveConnectionRenderer.Vertex v3) {

        void write(ChunkModelBuilder out, int offX, int offY, int offZ, int lightStart, int lightEnd) {
            ChunkMeshBufferBuilder vertexSink = out.getVertexBuffer(ModelQuadFacing.UNASSIGNED);
            int quadStart = vertexSink.count();
            this.v0.write(vertexSink, offX, offY, offZ, lightStart, lightEnd);
            this.v1.write(vertexSink, offX, offY, offZ, lightStart, lightEnd);
            this.v2.write(vertexSink, offX, offY, offZ, lightStart, lightEnd);
            this.v3.write(vertexSink, offX, offY, offZ, lightStart, lightEnd);
            this.v0.write(vertexSink, offX, offY, offZ, lightStart, lightEnd);
            this.v3.write(vertexSink, offX, offY, offZ, lightStart, lightEnd);
            this.v2.write(vertexSink, offX, offY, offZ, lightStart, lightEnd);
            this.v1.write(vertexSink, offX, offY, offZ, lightStart, lightEnd);
        }
    }

    private static record RenderedSegment(ImmersiveConnectionRenderer.Quad quadA, ImmersiveConnectionRenderer.Quad quadB, Vec3i offsetStart, Vec3i offsetEnd) {

        public void render(int lightStart, int lightEnd, int offX, int offY, int offZ, ChunkModelBuilder out) {
            this.quadA.write(out, offX, offY, offZ, lightStart, lightEnd);
            this.quadB.write(out, offX, offY, offZ, lightStart, lightEnd);
        }
    }

    private static record SegmentsKey(double radius, int color, CatenaryData catenaryShape, int beginIndex, int endIndex) {
    }

    private static record Vertex(float posX, float posY, float posZ, float texU, float texV, int color, boolean lightForStart) {

        static final ThreadLocal<ChunkVertexEncoder.Vertex[]> vertexHolder = ThreadLocal.withInitial(() -> new ChunkVertexEncoder.Vertex[1]);

        void write(ChunkMeshBufferBuilder vertexSink, int offX, int offY, int offZ, int lightStart, int lightEnd) {
            ChunkVertexEncoder.Vertex sodiumVertex = new ChunkVertexEncoder.Vertex();
            sodiumVertex.x = (float) offX + this.posX;
            sodiumVertex.y = (float) offY + this.posY;
            sodiumVertex.z = (float) offZ + this.posZ;
            sodiumVertex.color = this.color;
            sodiumVertex.u = this.texU;
            sodiumVertex.v = this.texV;
            sodiumVertex.light = this.lightForStart ? lightStart : lightEnd;
            ChunkVertexEncoder.Vertex[] array = (ChunkVertexEncoder.Vertex[]) vertexHolder.get();
            array[0] = sodiumVertex;
            vertexSink.push(array, ImmersiveConnectionRenderer.MATERIAL);
        }
    }
}