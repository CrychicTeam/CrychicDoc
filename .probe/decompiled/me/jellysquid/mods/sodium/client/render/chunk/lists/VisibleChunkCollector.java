package me.jellysquid.mods.sodium.client.render.chunk.lists;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayDeque;
import java.util.EnumMap;
import java.util.Map;
import java.util.Queue;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkUpdateType;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.OcclusionCuller;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import org.embeddedt.embeddium.util.sodium.FlawlessFrames;

public class VisibleChunkCollector implements OcclusionCuller.Visitor {

    private final ObjectArrayList<ChunkRenderList> sortedRenderLists;

    private final EnumMap<ChunkUpdateType, ArrayDeque<RenderSection>> sortedRebuildLists;

    private final int frame;

    private final boolean ignoreQueueSizeLimit;

    public VisibleChunkCollector(int frame) {
        this.frame = frame;
        this.sortedRenderLists = new ObjectArrayList();
        this.sortedRebuildLists = new EnumMap(ChunkUpdateType.class);
        this.ignoreQueueSizeLimit = FlawlessFrames.isActive();
        for (ChunkUpdateType type : ChunkUpdateType.values()) {
            this.sortedRebuildLists.put(type, new ArrayDeque());
        }
    }

    @Override
    public void visit(RenderSection section, boolean visible) {
        RenderRegion region = section.getRegion();
        ChunkRenderList renderList = region.getRenderList();
        if (renderList.getLastVisibleFrame() != this.frame) {
            renderList.reset(this.frame);
            this.sortedRenderLists.add(renderList);
        }
        if (visible && section.getFlags() != 0) {
            renderList.add(section);
        }
        this.addToRebuildLists(section);
    }

    private void addToRebuildLists(RenderSection section) {
        ChunkUpdateType type = section.getPendingUpdate();
        if (type != null && section.getBuildCancellationToken() == null) {
            Queue<RenderSection> queue = (Queue<RenderSection>) this.sortedRebuildLists.get(type);
            if (this.ignoreQueueSizeLimit || queue.size() < type.getMaximumQueueSize()) {
                queue.add(section);
            }
        }
    }

    public SortedRenderLists createRenderLists() {
        return new SortedRenderLists(this.sortedRenderLists);
    }

    public Map<ChunkUpdateType, ArrayDeque<RenderSection>> getRebuildLists() {
        return this.sortedRebuildLists;
    }
}