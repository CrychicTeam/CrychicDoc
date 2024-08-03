package me.jellysquid.mods.sodium.client.render.chunk.occlusion;

import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import java.util.Objects;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.viewport.CameraTransform;
import me.jellysquid.mods.sodium.client.render.viewport.Viewport;
import me.jellysquid.mods.sodium.client.util.collections.DoubleBufferedQueue;
import me.jellysquid.mods.sodium.client.util.collections.ReadQueue;
import me.jellysquid.mods.sodium.client.util.collections.WriteQueue;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.embeddedt.embeddium.api.render.chunk.RenderSectionDistanceFilter;
import org.embeddedt.embeddium.api.render.chunk.RenderSectionDistanceFilterEvent;
import org.jetbrains.annotations.NotNull;

public class OcclusionCuller {

    private final Long2ReferenceMap<RenderSection> sections;

    private final Level world;

    private final DoubleBufferedQueue<RenderSection> queue = new DoubleBufferedQueue<>();

    private boolean isCameraInUnloadedSection;

    private static final float CHUNK_SECTION_SIZE = 9.125F;

    public OcclusionCuller(Long2ReferenceMap<RenderSection> sections, Level world) {
        this.sections = sections;
        this.world = world;
    }

    public void findVisible(OcclusionCuller.Visitor visitor, Viewport viewport, float searchDistance, boolean useOcclusionCulling, int frame) {
        DoubleBufferedQueue<RenderSection> queues = this.queue;
        queues.reset();
        this.isCameraInUnloadedSection = false;
        this.init(visitor, queues.write(), viewport, searchDistance, useOcclusionCulling, frame);
        if (this.isCameraInUnloadedSection) {
            useOcclusionCulling = false;
        }
        while (queues.flip()) {
            processQueue(visitor, viewport, searchDistance, useOcclusionCulling, frame, queues.read(), queues.write());
        }
    }

    private static void processQueue(OcclusionCuller.Visitor visitor, Viewport viewport, float searchDistance, boolean useOcclusionCulling, int frame, ReadQueue<RenderSection> readQueue, WriteQueue<RenderSection> writeQueue) {
        RenderSection section;
        while ((section = readQueue.dequeue()) != null) {
            boolean visible = isSectionVisible(section, viewport, searchDistance);
            visitor.visit(section, visible);
            if (visible) {
                int connections;
                if (useOcclusionCulling) {
                    connections = VisibilityEncoding.getConnections(section.getVisibilityData(), section.getIncomingDirections());
                } else {
                    connections = 63;
                }
                connections &= getOutwardDirections(viewport.getChunkCoord(), section);
                visitNeighbors(writeQueue, section, connections, frame);
            }
        }
    }

    private static boolean isSectionVisible(RenderSection section, Viewport viewport, float maxDistance) {
        return isWithinRenderDistance(viewport.getTransform(), section, maxDistance) && isWithinFrustum(viewport, section);
    }

    private static void visitNeighbors(WriteQueue<RenderSection> queue, RenderSection section, int outgoing, int frame) {
        outgoing &= section.getAdjacentMask();
        if (outgoing != 0) {
            queue.ensureCapacity(6);
            if (GraphDirectionSet.contains(outgoing, 0)) {
                visitNode(queue, section.adjacentDown, GraphDirectionSet.of(1), frame);
            }
            if (GraphDirectionSet.contains(outgoing, 1)) {
                visitNode(queue, section.adjacentUp, GraphDirectionSet.of(0), frame);
            }
            if (GraphDirectionSet.contains(outgoing, 2)) {
                visitNode(queue, section.adjacentNorth, GraphDirectionSet.of(3), frame);
            }
            if (GraphDirectionSet.contains(outgoing, 3)) {
                visitNode(queue, section.adjacentSouth, GraphDirectionSet.of(2), frame);
            }
            if (GraphDirectionSet.contains(outgoing, 4)) {
                visitNode(queue, section.adjacentWest, GraphDirectionSet.of(5), frame);
            }
            if (GraphDirectionSet.contains(outgoing, 5)) {
                visitNode(queue, section.adjacentEast, GraphDirectionSet.of(4), frame);
            }
        }
    }

    private static void visitNode(WriteQueue<RenderSection> queue, @NotNull RenderSection render, int incoming, int frame) {
        if (render.getLastVisibleFrame() != frame) {
            render.setLastVisibleFrame(frame);
            render.setIncomingDirections(0);
            queue.enqueue(render);
        }
        render.addIncomingDirections(incoming);
    }

    private static int getOutwardDirections(SectionPos origin, RenderSection section) {
        int planes = 0;
        planes |= section.getChunkX() <= origin.m_123341_() ? 16 : 0;
        planes |= section.getChunkX() >= origin.m_123341_() ? 32 : 0;
        planes |= section.getChunkY() <= origin.m_123342_() ? 1 : 0;
        planes |= section.getChunkY() >= origin.m_123342_() ? 2 : 0;
        planes |= section.getChunkZ() <= origin.m_123343_() ? 4 : 0;
        return planes | (section.getChunkZ() >= origin.m_123343_() ? 8 : 0);
    }

    private static boolean isWithinRenderDistance(CameraTransform camera, RenderSection section, float maxDistance) {
        int ox = section.getOriginX() - camera.intX;
        int oy = section.getOriginY() - camera.intY;
        int oz = section.getOriginZ() - camera.intZ;
        float dx = (float) nearestToZero(ox, ox + 16) - camera.fracX;
        float dy = (float) nearestToZero(oy, oy + 16) - camera.fracY;
        float dz = (float) nearestToZero(oz, oz + 16) - camera.fracZ;
        return OcclusionCuller.DistanceFilterHolder.INSTANCE.isWithinDistance(dx, dy, dz, maxDistance);
    }

    private static int nearestToZero(int min, int max) {
        int clamped = 0;
        if (min > 0) {
            clamped = min;
        }
        if (max < 0) {
            clamped = max;
        }
        return clamped;
    }

    public static boolean isWithinFrustum(Viewport viewport, RenderSection section) {
        return viewport.isBoxVisible(section.getCenterX(), section.getCenterY(), section.getCenterZ(), 9.125F);
    }

    private void init(OcclusionCuller.Visitor visitor, WriteQueue<RenderSection> queue, Viewport viewport, float searchDistance, boolean useOcclusionCulling, int frame) {
        SectionPos origin = viewport.getChunkCoord();
        if (origin.m_123342_() < this.world.m_151560_()) {
            this.initOutsideWorldHeight(queue, viewport, searchDistance, frame, this.world.m_151560_(), GraphDirectionSet.of(0));
        } else if (origin.m_123342_() >= this.world.m_151561_()) {
            this.initOutsideWorldHeight(queue, viewport, searchDistance, frame, this.world.m_151561_() - 1, GraphDirectionSet.of(1));
        } else if (this.getRenderSection(origin.m_123341_(), origin.m_123342_(), origin.m_123343_()) == null) {
            this.initOutsideWorldHeight(queue, viewport, searchDistance, frame, origin.m_123342_(), GraphDirectionSet.of(1) | GraphDirectionSet.of(0));
            this.isCameraInUnloadedSection = true;
        } else {
            this.initWithinWorld(visitor, queue, viewport, useOcclusionCulling, frame);
        }
    }

    private void initWithinWorld(OcclusionCuller.Visitor visitor, WriteQueue<RenderSection> queue, Viewport viewport, boolean useOcclusionCulling, int frame) {
        SectionPos origin = viewport.getChunkCoord();
        RenderSection section = this.getRenderSection(origin.m_123341_(), origin.m_123342_(), origin.m_123343_());
        Objects.requireNonNull(section);
        section.setLastVisibleFrame(frame);
        section.setIncomingDirections(0);
        visitor.visit(section, true);
        int outgoing;
        if (useOcclusionCulling) {
            outgoing = VisibilityEncoding.getConnections(section.getVisibilityData());
        } else {
            outgoing = 63;
        }
        visitNeighbors(queue, section, outgoing, frame);
    }

    private void initOutsideWorldHeight(WriteQueue<RenderSection> queue, Viewport viewport, float searchDistance, int frame, int height, int direction) {
        SectionPos origin = viewport.getChunkCoord();
        int radius = Mth.floor(searchDistance / 16.0F);
        this.tryVisitNode(queue, origin.m_123341_(), height, origin.m_123343_(), direction, frame, viewport);
        for (int layer = 1; layer <= radius; layer++) {
            for (int z = -layer; z < layer; z++) {
                int x = Math.abs(z) - layer;
                this.tryVisitNode(queue, origin.m_123341_() + x, height, origin.m_123343_() + z, direction, frame, viewport);
            }
            for (int z = layer; z > -layer; z--) {
                int x = layer - Math.abs(z);
                this.tryVisitNode(queue, origin.m_123341_() + x, height, origin.m_123343_() + z, direction, frame, viewport);
            }
        }
        for (int layer = radius + 1; layer <= 2 * radius; layer++) {
            int l = layer - radius;
            for (int z = -radius; z <= -l; z++) {
                int x = -z - layer;
                this.tryVisitNode(queue, origin.m_123341_() + x, height, origin.m_123343_() + z, direction, frame, viewport);
            }
            for (int z = l; z <= radius; z++) {
                int x = z - layer;
                this.tryVisitNode(queue, origin.m_123341_() + x, height, origin.m_123343_() + z, direction, frame, viewport);
            }
            for (int z = radius; z >= l; z--) {
                int x = layer - z;
                this.tryVisitNode(queue, origin.m_123341_() + x, height, origin.m_123343_() + z, direction, frame, viewport);
            }
            for (int z = -l; z >= -radius; z--) {
                int x = layer + z;
                this.tryVisitNode(queue, origin.m_123341_() + x, height, origin.m_123343_() + z, direction, frame, viewport);
            }
        }
    }

    private void tryVisitNode(WriteQueue<RenderSection> queue, int x, int y, int z, int direction, int frame, Viewport viewport) {
        RenderSection section = this.getRenderSection(x, y, z);
        if (section != null && isWithinFrustum(viewport, section)) {
            visitNode(queue, section, direction, frame);
        }
    }

    private RenderSection getRenderSection(int x, int y, int z) {
        return (RenderSection) this.sections.get(SectionPos.asLong(x, y, z));
    }

    private static class DistanceFilterHolder {

        private static final RenderSectionDistanceFilter INSTANCE;

        static {
            RenderSectionDistanceFilterEvent event = new RenderSectionDistanceFilterEvent();
            RenderSectionDistanceFilterEvent.BUS.post(event);
            INSTANCE = event.getFilter();
        }
    }

    public interface Visitor {

        void visit(RenderSection var1, boolean var2);
    }
}