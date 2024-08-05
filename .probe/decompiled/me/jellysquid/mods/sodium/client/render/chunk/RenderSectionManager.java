package me.jellysquid.mods.sodium.client.render.chunk;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMaps;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gl.arena.GlBufferArena;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import me.jellysquid.mods.sodium.client.render.chunk.compile.executor.ChunkBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.executor.ChunkJobCollector;
import me.jellysquid.mods.sodium.client.render.chunk.compile.executor.ChunkJobResult;
import me.jellysquid.mods.sodium.client.render.chunk.compile.executor.ChunkJobTyped;
import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderSortTask;
import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderTask;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionMeshParts;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import me.jellysquid.mods.sodium.client.render.chunk.lists.SortedRenderLists;
import me.jellysquid.mods.sodium.client.render.chunk.lists.VisibleChunkCollector;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.GraphDirection;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.OcclusionCuller;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegionManager;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkMeshFormats;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import me.jellysquid.mods.sodium.client.render.viewport.CameraTransform;
import me.jellysquid.mods.sodium.client.render.viewport.Viewport;
import me.jellysquid.mods.sodium.client.util.MathUtil;
import me.jellysquid.mods.sodium.client.util.iterator.ByteIterator;
import me.jellysquid.mods.sodium.client.util.task.CancellationToken;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSectionCache;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;
import org.embeddedt.embeddium.api.ChunkMeshEvent;
import org.embeddedt.embeddium.render.chunk.sorting.TranslucentQuadAnalyzer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenderSectionManager {

    private final ChunkBuilder builder;

    private final Thread renderThread = Thread.currentThread();

    private final RenderRegionManager regions;

    private final ClonedChunkSectionCache sectionCache;

    private final Long2ReferenceMap<RenderSection> sectionByPosition = new Long2ReferenceOpenHashMap();

    private final ConcurrentLinkedDeque<ChunkJobResult<ChunkBuildOutput>> buildResults = new ConcurrentLinkedDeque();

    private final ConcurrentLinkedDeque<Runnable> asyncSubmittedTasks = new ConcurrentLinkedDeque();

    private final ChunkRenderer chunkRenderer;

    private final ClientLevel world;

    private final ReferenceSet<RenderSection> sectionsWithGlobalEntities = new ReferenceOpenHashSet();

    private final OcclusionCuller occlusionCuller;

    private final int renderDistance;

    private final ChunkVertexType vertexType;

    @NotNull
    private SortedRenderLists renderLists;

    @NotNull
    private Map<ChunkUpdateType, ArrayDeque<RenderSection>> rebuildLists;

    private int lastUpdatedFrame;

    private boolean needsUpdate;

    @Nullable
    private BlockPos lastCameraPosition;

    private Vec3 cameraPosition = Vec3.ZERO;

    private final boolean translucencySorting;

    private final int translucencyBlockRenderDistance;

    private static final float NEARBY_REBUILD_DISTANCE = Mth.square(16.0F);

    public RenderSectionManager(ClientLevel world, int renderDistance, CommandList commandList) {
        ChunkVertexType vertexType = SodiumClientMod.canUseVanillaVertices() ? ChunkMeshFormats.VANILLA_LIKE : ChunkMeshFormats.COMPACT;
        this.chunkRenderer = new DefaultChunkRenderer(RenderDevice.INSTANCE, vertexType);
        this.vertexType = vertexType;
        this.world = world;
        this.builder = new ChunkBuilder(world, vertexType);
        this.needsUpdate = true;
        this.renderDistance = renderDistance;
        this.regions = new RenderRegionManager(commandList);
        this.sectionCache = new ClonedChunkSectionCache(this.world);
        this.renderLists = SortedRenderLists.empty();
        this.occlusionCuller = new OcclusionCuller(Long2ReferenceMaps.unmodifiable(this.sectionByPosition), this.world);
        this.rebuildLists = new EnumMap(ChunkUpdateType.class);
        for (ChunkUpdateType type : ChunkUpdateType.values()) {
            this.rebuildLists.put(type, new ArrayDeque());
        }
        this.translucencySorting = SodiumClientMod.canApplyTranslucencySorting();
        this.translucencyBlockRenderDistance = Math.min(9216, (renderDistance << 4) * (renderDistance << 4));
    }

    public void runAsyncTasks() {
        Runnable task;
        while ((task = (Runnable) this.asyncSubmittedTasks.poll()) != null) {
            task.run();
        }
    }

    public void update(Camera camera, Viewport viewport, int frame, boolean spectator) {
        this.lastCameraPosition = camera.getBlockPosition();
        this.cameraPosition = camera.getPosition();
        this.createTerrainRenderList(camera, viewport, frame, spectator);
        this.needsUpdate = false;
        this.lastUpdatedFrame = frame;
    }

    private void checkTranslucencyChange() {
        if (this.translucencySorting && this.lastCameraPosition != null) {
            int camSectionX = SectionPos.blockToSectionCoord(this.cameraPosition.x);
            int camSectionY = SectionPos.blockToSectionCoord(this.cameraPosition.y);
            int camSectionZ = SectionPos.blockToSectionCoord(this.cameraPosition.z);
            this.scheduleTranslucencyUpdates(camSectionX, camSectionY, camSectionZ);
        }
    }

    private void scheduleTranslucencyUpdates(int camSectionX, int camSectionY, int camSectionZ) {
        ArrayDeque<RenderSection> sortRebuildList = (ArrayDeque<RenderSection>) this.rebuildLists.get(ChunkUpdateType.SORT);
        ArrayDeque<RenderSection> importantSortRebuildList = (ArrayDeque<RenderSection>) this.rebuildLists.get(ChunkUpdateType.IMPORTANT_SORT);
        boolean allowImportant = allowImportantRebuilds();
        for (ChunkRenderList entry : this.renderLists) {
            RenderRegion region = entry.getRegion();
            ByteIterator sectionIterator = entry.sectionsWithGeometryIterator(false);
            if (sectionIterator != null) {
                while (sectionIterator.hasNext()) {
                    RenderSection section = region.getSection(sectionIterator.nextByteAsInt());
                    if (section != null && section.isBuilt()) {
                        boolean hasTranslucentData = section.containsTranslucentGeometry() && section.getSortState() != null && section.getSortState().requiresDynamicSorting();
                        if (hasTranslucentData) {
                            ChunkUpdateType update = ChunkUpdateType.getPromotionUpdateType(section.getPendingUpdate(), allowImportant && this.shouldPrioritizeRebuild(section) ? ChunkUpdateType.IMPORTANT_SORT : ChunkUpdateType.SORT);
                            if (update != null) {
                                double dx = this.cameraPosition.x - section.lastCameraX;
                                double dy = this.cameraPosition.y - section.lastCameraY;
                                double dz = this.cameraPosition.z - section.lastCameraZ;
                                double camDelta = dx * dx + dy * dy + dz * dz;
                                if (!(camDelta < 1.0)) {
                                    boolean cameraChangedSection = camSectionX != SectionPos.blockToSectionCoord(section.lastCameraX) || camSectionY != SectionPos.blockToSectionCoord(section.lastCameraY) || camSectionZ != SectionPos.blockToSectionCoord(section.lastCameraZ);
                                    if (cameraChangedSection || section.isAlignedWithSectionOnGrid(camSectionX, camSectionY, camSectionZ)) {
                                        section.setPendingUpdate(update);
                                        (update == ChunkUpdateType.IMPORTANT_SORT ? importantSortRebuildList : sortRebuildList).add(section);
                                        section.lastCameraX = this.cameraPosition.x;
                                        section.lastCameraY = this.cameraPosition.y;
                                        section.lastCameraZ = this.cameraPosition.z;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createTerrainRenderList(Camera camera, Viewport viewport, int frame, boolean spectator) {
        this.resetRenderLists();
        float searchDistance = this.getSearchDistance();
        boolean useOcclusionCulling = this.shouldUseOcclusionCulling(camera, spectator);
        VisibleChunkCollector visitor = new VisibleChunkCollector(frame);
        this.occlusionCuller.findVisible(visitor, viewport, searchDistance, useOcclusionCulling, frame);
        this.renderLists = visitor.createRenderLists();
        this.rebuildLists = visitor.getRebuildLists();
        this.checkTranslucencyChange();
    }

    private float getSearchDistance() {
        float distance;
        if (SodiumClientMod.options().performance.useFogOcclusion) {
            distance = this.getEffectiveRenderDistance();
        } else {
            distance = this.getRenderDistance();
        }
        return distance;
    }

    private boolean shouldUseOcclusionCulling(Camera camera, boolean spectator) {
        BlockPos origin = camera.getBlockPosition();
        boolean useOcclusionCulling;
        if (spectator && this.world.m_8055_(origin).m_60804_(this.world, origin)) {
            useOcclusionCulling = false;
        } else {
            useOcclusionCulling = Minecraft.getInstance().smartCull;
        }
        return useOcclusionCulling;
    }

    private void resetRenderLists() {
        this.renderLists = SortedRenderLists.empty();
        for (ArrayDeque<RenderSection> list : this.rebuildLists.values()) {
            list.clear();
        }
    }

    public void onSectionAdded(int x, int y, int z) {
        long key = SectionPos.asLong(x, y, z);
        if (!this.sectionByPosition.containsKey(key)) {
            RenderRegion region = this.regions.createForChunk(x, y, z);
            RenderSection renderSection = new RenderSection(region, x, y, z);
            region.addSection(renderSection);
            this.sectionByPosition.put(key, renderSection);
            ChunkAccess chunk = this.world.m_6325_(x, z);
            LevelChunkSection section = chunk.getSections()[this.world.m_151566_(y)];
            boolean isEmpty = (section == null || section.hasOnlyAir()) && ChunkMeshEvent.post(this.world, SectionPos.of(x, y, z)).isEmpty();
            if (isEmpty) {
                this.updateSectionInfo(renderSection, BuiltSectionInfo.EMPTY);
            } else {
                renderSection.setPendingUpdate(ChunkUpdateType.INITIAL_BUILD);
            }
            this.connectNeighborNodes(renderSection);
            this.needsUpdate = true;
        }
    }

    public void onSectionRemoved(int x, int y, int z) {
        RenderSection section = (RenderSection) this.sectionByPosition.remove(SectionPos.asLong(x, y, z));
        if (section != null) {
            RenderRegion region = section.getRegion();
            if (region != null) {
                region.removeSection(section);
            }
            this.disconnectNeighborNodes(section);
            this.updateSectionInfo(section, null);
            section.delete();
            this.needsUpdate = true;
        }
    }

    public void renderLayer(ChunkRenderMatrices matrices, TerrainRenderPass pass, double x, double y, double z) {
        RenderDevice device = RenderDevice.INSTANCE;
        CommandList commandList = device.createCommandList();
        this.chunkRenderer.render(matrices, commandList, this.renderLists, pass, new CameraTransform(x, y, z));
        commandList.flush();
    }

    public void tickVisibleRenders() {
        for (ChunkRenderList renderList : this.renderLists) {
            RenderRegion region = renderList.getRegion();
            ByteIterator iterator = renderList.sectionsWithSpritesIterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    RenderSection section = region.getSection(iterator.nextByteAsInt());
                    if (section != null) {
                        TextureAtlasSprite[] sprites = section.getAnimatedSprites();
                        if (sprites != null) {
                            for (TextureAtlasSprite sprite : sprites) {
                                SpriteUtil.markSpriteActive(sprite);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isSectionVisible(int x, int y, int z) {
        RenderSection render = this.getRenderSection(x, y, z);
        return render == null ? false : render.getLastVisibleFrame() == this.lastUpdatedFrame;
    }

    public void updateChunks(boolean updateImmediately) {
        this.sectionCache.cleanup();
        this.regions.update();
        ChunkJobCollector blockingRebuilds = new ChunkJobCollector(Integer.MAX_VALUE, this.buildResults::add);
        ChunkJobCollector deferredRebuilds = new ChunkJobCollector(this.builder.getSchedulingBudget(), this.buildResults::add);
        this.submitRebuildTasks(blockingRebuilds, ChunkUpdateType.IMPORTANT_REBUILD);
        this.submitRebuildTasks(blockingRebuilds, ChunkUpdateType.IMPORTANT_SORT);
        this.submitRebuildTasks(updateImmediately ? blockingRebuilds : deferredRebuilds, ChunkUpdateType.REBUILD);
        this.submitRebuildTasks(updateImmediately ? blockingRebuilds : deferredRebuilds, ChunkUpdateType.INITIAL_BUILD);
        ChunkJobCollector deferredSorts = new ChunkJobCollector(Math.max(4, this.builder.getSchedulingBudget() * 4), this.buildResults::add);
        this.submitRebuildTasks(updateImmediately ? blockingRebuilds : deferredSorts, ChunkUpdateType.SORT);
        blockingRebuilds.awaitCompletion(this.builder);
    }

    public void uploadChunks() {
        ArrayList<ChunkBuildOutput> results = this.collectChunkBuildResults();
        if (!results.isEmpty()) {
            this.processChunkBuildResults(results);
            for (ChunkBuildOutput result : results) {
                result.delete();
            }
            this.needsUpdate = true;
        }
    }

    private void processChunkBuildResults(ArrayList<ChunkBuildOutput> results) {
        List<ChunkBuildOutput> filtered = filterChunkBuildResults(results);
        this.regions.uploadMeshes(RenderDevice.INSTANCE.createCommandList(), filtered);
        for (ChunkBuildOutput result : filtered) {
            if (result.info != null) {
                this.updateSectionInfo(result.render, result.info);
                if (this.translucencySorting) {
                    this.updateTranslucencyInfo(result.render, (BuiltSectionMeshParts) result.meshes.get(DefaultTerrainRenderPasses.TRANSLUCENT));
                }
            }
            CancellationToken job = result.render.getBuildCancellationToken();
            if (job != null && result.buildTime >= result.render.getLastSubmittedFrame()) {
                result.render.setBuildCancellationToken(null);
            }
            result.render.setLastBuiltFrame(result.buildTime);
        }
    }

    private void updateTranslucencyInfo(RenderSection render, BuiltSectionMeshParts translucencyMesh) {
        if (translucencyMesh != null) {
            render.setSortState(translucencyMesh.getSortState());
        }
    }

    private void updateSectionInfo(RenderSection render, BuiltSectionInfo info) {
        render.setInfo(info);
        if (info != null && !ArrayUtils.isEmpty(info.globalBlockEntities)) {
            this.sectionsWithGlobalEntities.add(render);
        } else {
            this.sectionsWithGlobalEntities.remove(render);
        }
    }

    private static List<ChunkBuildOutput> filterChunkBuildResults(ArrayList<ChunkBuildOutput> outputs) {
        Reference2ReferenceLinkedOpenHashMap<RenderSection, ChunkBuildOutput> map = new Reference2ReferenceLinkedOpenHashMap();
        for (ChunkBuildOutput output : outputs) {
            if (!output.render.isDisposed() && output.render.getLastBuiltFrame() <= output.buildTime) {
                RenderSection render = output.render;
                ChunkBuildOutput previous = (ChunkBuildOutput) map.get(render);
                if (previous == null || previous.buildTime < output.buildTime) {
                    map.put(render, output);
                }
            }
        }
        return new ArrayList(map.values());
    }

    private ArrayList<ChunkBuildOutput> collectChunkBuildResults() {
        ArrayList<ChunkBuildOutput> results = new ArrayList();
        ChunkJobResult<ChunkBuildOutput> result;
        while ((result = (ChunkJobResult<ChunkBuildOutput>) this.buildResults.poll()) != null) {
            results.add(result.unwrap());
        }
        return results;
    }

    private void submitRebuildTasks(ChunkJobCollector collector, ChunkUpdateType type) {
        ArrayDeque<RenderSection> queue = (ArrayDeque<RenderSection>) this.rebuildLists.get(type);
        while (!queue.isEmpty() && collector.canOffer()) {
            RenderSection section = (RenderSection) queue.remove();
            if (!section.isDisposed() && section.getPendingUpdate() == type) {
                int frame = this.lastUpdatedFrame;
                ChunkBuilderTask<ChunkBuildOutput> task = (ChunkBuilderTask<ChunkBuildOutput>) (type.isSort() ? this.createSortTask(section, frame) : this.createRebuildTask(section, frame));
                if (task == null && type.isSort()) {
                    section.setPendingUpdate(null);
                } else {
                    if (task != null) {
                        ChunkJobTyped<ChunkBuilderTask<ChunkBuildOutput>, ChunkBuildOutput> job = this.builder.scheduleTask(task, type.isImportant(), collector::onJobFinished);
                        collector.addSubmittedJob(job);
                        section.setBuildCancellationToken(job);
                        if (!type.isSort()) {
                            section.setSortState(null);
                        }
                    } else {
                        ChunkJobResult<ChunkBuildOutput> result = ChunkJobResult.successfully(new ChunkBuildOutput(section, BuiltSectionInfo.EMPTY, Collections.emptyMap(), frame));
                        this.buildResults.add(result);
                        section.setBuildCancellationToken(null);
                    }
                    section.setLastSubmittedFrame(frame);
                    section.setPendingUpdate(null);
                }
            }
        }
    }

    @Nullable
    public ChunkBuilderMeshingTask createRebuildTask(RenderSection render, int frame) {
        ChunkRenderContext context = WorldSlice.prepare(this.world, render.getPosition(), this.sectionCache);
        return context == null ? null : new ChunkBuilderMeshingTask(render, context, frame).withCameraPosition(this.cameraPosition);
    }

    public ChunkBuilderSortTask createSortTask(RenderSection render, int frame) {
        Map<TerrainRenderPass, TranslucentQuadAnalyzer.SortState> meshes = new Reference2ReferenceOpenHashMap();
        TranslucentQuadAnalyzer.SortState sortBuffer = render.getSortState();
        if (sortBuffer != null && sortBuffer.requiresDynamicSorting()) {
            meshes.put(DefaultTerrainRenderPasses.TRANSLUCENT, sortBuffer);
            return new ChunkBuilderSortTask(render, (float) this.cameraPosition.x, (float) this.cameraPosition.y, (float) this.cameraPosition.z, frame, meshes);
        } else {
            return null;
        }
    }

    public void markGraphDirty() {
        this.needsUpdate = true;
    }

    public boolean needsUpdate() {
        return this.needsUpdate;
    }

    public ChunkBuilder getBuilder() {
        return this.builder;
    }

    public void destroy() {
        this.builder.shutdown();
        for (ChunkBuildOutput result : this.collectChunkBuildResults()) {
            result.delete();
        }
        this.sectionsWithGlobalEntities.clear();
        this.resetRenderLists();
        try (CommandList commandList = RenderDevice.INSTANCE.createCommandList()) {
            this.regions.delete(commandList);
            this.chunkRenderer.delete(commandList);
        }
    }

    public int getTotalSections() {
        return this.sectionByPosition.size();
    }

    public int getVisibleChunkCount() {
        int sections = 0;
        for (ChunkRenderList renderList : this.renderLists) {
            sections += renderList.getSectionsWithGeometryCount();
        }
        return sections;
    }

    private void scheduleRebuildOffThread(int x, int y, int z, boolean important) {
        this.asyncSubmittedTasks.add((Runnable) () -> this.scheduleRebuild(x, y, z, important));
    }

    public void scheduleRebuild(int x, int y, int z, boolean important) {
        if (Thread.currentThread() != this.renderThread) {
            this.scheduleRebuildOffThread(x, y, z, important);
        } else {
            this.sectionCache.invalidate(x, y, z);
            RenderSection section = (RenderSection) this.sectionByPosition.get(SectionPos.asLong(x, y, z));
            if (section != null) {
                ChunkUpdateType pendingUpdate;
                if (!allowImportantRebuilds() || !important && !this.shouldPrioritizeRebuild(section)) {
                    pendingUpdate = ChunkUpdateType.REBUILD;
                } else {
                    pendingUpdate = ChunkUpdateType.IMPORTANT_REBUILD;
                }
                pendingUpdate = ChunkUpdateType.getPromotionUpdateType(section.getPendingUpdate(), pendingUpdate);
                if (pendingUpdate != null) {
                    section.setPendingUpdate(pendingUpdate);
                    this.needsUpdate = true;
                }
            }
        }
    }

    private boolean shouldPrioritizeRebuild(RenderSection section) {
        return this.lastCameraPosition != null && section.getSquaredDistance(this.lastCameraPosition) < NEARBY_REBUILD_DISTANCE;
    }

    private static boolean allowImportantRebuilds() {
        return !SodiumClientMod.options().performance.alwaysDeferChunkUpdates;
    }

    private float getEffectiveRenderDistance() {
        float[] color = RenderSystem.getShaderFogColor();
        float distance = RenderSystem.getShaderFogEnd();
        float renderDistance = this.getRenderDistance();
        return !Mth.equal(color[3], 1.0F) ? renderDistance : Math.min(renderDistance, distance + 0.5F);
    }

    private float getRenderDistance() {
        return (float) this.renderDistance * 16.0F;
    }

    private void connectNeighborNodes(RenderSection render) {
        for (int direction = 0; direction < 6; direction++) {
            RenderSection adj = this.getRenderSection(render.getChunkX() + GraphDirection.x(direction), render.getChunkY() + GraphDirection.y(direction), render.getChunkZ() + GraphDirection.z(direction));
            if (adj != null) {
                adj.setAdjacentNode(GraphDirection.opposite(direction), render);
                render.setAdjacentNode(direction, adj);
            }
        }
    }

    private void disconnectNeighborNodes(RenderSection render) {
        for (int direction = 0; direction < 6; direction++) {
            RenderSection adj = render.getAdjacent(direction);
            if (adj != null) {
                adj.setAdjacentNode(GraphDirection.opposite(direction), null);
                render.setAdjacentNode(direction, null);
            }
        }
    }

    private RenderSection getRenderSection(int x, int y, int z) {
        return (RenderSection) this.sectionByPosition.get(SectionPos.asLong(x, y, z));
    }

    private Collection<String> getSortingStrings() {
        List<String> list = new ArrayList();
        int[] sectionCounts = new int[TranslucentQuadAnalyzer.Level.VALUES.length];
        for (ChunkRenderList renderList : this.renderLists) {
            RenderRegion region = renderList.getRegion();
            ByteIterator listIter = renderList.sectionsWithGeometryIterator(false);
            if (listIter != null) {
                while (listIter.hasNext()) {
                    RenderSection section = region.getSection(listIter.nextByteAsInt());
                    if (section != null && section.containsTranslucentGeometry()) {
                        TranslucentQuadAnalyzer.SortState data = section.getSortState();
                        TranslucentQuadAnalyzer.Level level = data != null ? data.level() : TranslucentQuadAnalyzer.Level.NONE;
                        sectionCounts[level.ordinal()]++;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Sorting: ");
        TranslucentQuadAnalyzer.Level[] values = TranslucentQuadAnalyzer.Level.VALUES;
        for (int i = 0; i < values.length; i++) {
            TranslucentQuadAnalyzer.Level level = values[i];
            sb.append(level.name());
            sb.append('=');
            sb.append(sectionCounts[level.ordinal()]);
            if (i + 1 < values.length) {
                sb.append(", ");
            }
        }
        list.add(sb.toString());
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity != null) {
            HitResult hitResult = cameraEntity.pick(20.0, 0.0F, false);
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
                RenderSection self = this.getRenderSection(pos.m_123341_() >> 4, pos.m_123342_() >> 4, pos.m_123343_() >> 4);
                if (self != null && self.containsTranslucentGeometry()) {
                    TranslucentQuadAnalyzer.SortState selfData = self.getSortState();
                    TranslucentQuadAnalyzer.Level level = selfData != null ? selfData.level() : TranslucentQuadAnalyzer.Level.NONE;
                    list.add("Targeted Section: " + level.name());
                }
            }
        }
        return list;
    }

    public Collection<String> getDebugStrings() {
        List<String> list = new ArrayList();
        int count = 0;
        int indexCount = 0;
        long deviceUsed = 0L;
        long deviceAllocated = 0L;
        long indexUsed = 0L;
        long indexAllocated = 0L;
        for (RenderRegion region : this.regions.getLoadedRegions()) {
            RenderRegion.DeviceResources resources = region.getResources();
            if (resources != null) {
                GlBufferArena buffer = resources.getGeometryArena();
                deviceUsed += buffer.getDeviceUsedMemoryL();
                deviceAllocated += buffer.getDeviceAllocatedMemoryL();
                GlBufferArena indexBuffer = resources.getIndexArena();
                if (indexBuffer != null) {
                    indexUsed += indexBuffer.getDeviceUsedMemoryL();
                    indexAllocated += indexBuffer.getDeviceAllocatedMemoryL();
                    indexCount++;
                }
                count++;
            }
        }
        list.add(String.format("Geometry Pool: %d/%d MiB (%d buffers)", MathUtil.toMib(deviceUsed), MathUtil.toMib(deviceAllocated), count));
        if (indexUsed > 0L) {
            list.add(String.format("Index Pool: %d/%d MiB (%d buffers)", MathUtil.toMib(indexUsed), MathUtil.toMib(indexAllocated), indexCount));
        }
        list.add(String.format("Transfer Queue: %s", this.regions.getStagingBuffer().toString()));
        list.add(String.format("Chunk Builder: Permits=%02d | Busy=%02d | Total=%02d", this.builder.getScheduledJobCount(), this.builder.getBusyThreadCount(), this.builder.getTotalThreadCount()));
        list.add(String.format("Chunk Queues: U=%02d (P0=%03d | P1=%03d | P2=%03d)", this.buildResults.size(), ((ArrayDeque) this.rebuildLists.get(ChunkUpdateType.IMPORTANT_REBUILD)).size(), ((ArrayDeque) this.rebuildLists.get(ChunkUpdateType.REBUILD)).size(), ((ArrayDeque) this.rebuildLists.get(ChunkUpdateType.INITIAL_BUILD)).size()));
        if (this.translucencySorting) {
            list.addAll(this.getSortingStrings());
        }
        return list;
    }

    @NotNull
    public SortedRenderLists getRenderLists() {
        return this.renderLists;
    }

    public boolean isSectionBuilt(int x, int y, int z) {
        RenderSection section = this.getRenderSection(x, y, z);
        return section != null && section.isBuilt();
    }

    public void onChunkAdded(int x, int z) {
        for (int y = this.world.m_151560_(); y < this.world.m_151561_(); y++) {
            this.onSectionAdded(x, y, z);
        }
    }

    public void onChunkRemoved(int x, int z) {
        for (int y = this.world.m_151560_(); y < this.world.m_151561_(); y++) {
            this.onSectionRemoved(x, y, z);
        }
    }

    public Collection<RenderSection> getSectionsWithGlobalEntities() {
        return ReferenceSets.unmodifiable(this.sectionsWithGlobalEntities);
    }

    public ChunkVertexType getVertexType() {
        return this.vertexType;
    }
}