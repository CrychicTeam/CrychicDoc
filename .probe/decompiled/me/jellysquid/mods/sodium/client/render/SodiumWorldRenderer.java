package me.jellysquid.mods.sodium.client.render;

import com.google.common.collect.Iterators;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Consumer;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.model.quad.blender.BlendedColorProvider;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTracker;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTrackerHolder;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import me.jellysquid.mods.sodium.client.render.viewport.Viewport;
import me.jellysquid.mods.sodium.client.util.NativeBuffer;
import me.jellysquid.mods.sodium.client.util.iterator.ByteIterator;
import me.jellysquid.mods.sodium.client.world.WorldRendererExtended;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLLoader;

public class SodiumWorldRenderer {

    private static final boolean ENABLE_BLOCKENTITY_CULLING = FMLLoader.getLoadingModList().getModFileById("valkyrienskies") == null;

    private final Minecraft client;

    private ClientLevel world;

    private int renderDistance;

    private double lastCameraX;

    private double lastCameraY;

    private double lastCameraZ;

    private double lastCameraPitch;

    private double lastCameraYaw;

    private float lastFogDistance;

    private boolean useEntityCulling;

    private Viewport currentViewport;

    private RenderSectionManager renderSectionManager;

    private boolean blockEntityRequestedOutline;

    private static final double MAX_ENTITY_CHECK_VOLUME = 61440.0;

    public static SodiumWorldRenderer instance() {
        SodiumWorldRenderer instance = instanceNullable();
        if (instance == null) {
            throw new IllegalStateException("No renderer attached to active world");
        } else {
            return instance;
        }
    }

    public static SodiumWorldRenderer instanceNullable() {
        LevelRenderer world = Minecraft.getInstance().levelRenderer;
        return world instanceof WorldRendererExtended ? ((WorldRendererExtended) world).sodium$getWorldRenderer() : null;
    }

    public SodiumWorldRenderer(Minecraft client) {
        this.client = client;
    }

    public void setWorld(ClientLevel world) {
        if (this.world != world) {
            if (this.world != null) {
                this.unloadWorld();
            }
            if (world != null) {
                this.loadWorld(world);
            }
        }
    }

    private void loadWorld(ClientLevel world) {
        this.world = world;
        try (CommandList commandList = RenderDevice.INSTANCE.createCommandList()) {
            this.initRenderer(commandList);
        }
    }

    private void unloadWorld() {
        if (this.renderSectionManager != null) {
            this.renderSectionManager.destroy();
            this.renderSectionManager = null;
        }
        this.world = null;
    }

    public int getVisibleChunkCount() {
        return this.renderSectionManager.getVisibleChunkCount();
    }

    public void scheduleTerrainUpdate() {
        if (this.renderSectionManager != null) {
            this.renderSectionManager.markGraphDirty();
        }
    }

    public boolean isTerrainRenderComplete() {
        return this.renderSectionManager.getBuilder().isBuildQueueEmpty();
    }

    public void setupTerrain(Camera camera, Viewport viewport, @Deprecated(forRemoval = true) int frame, boolean spectator, boolean updateChunksImmediately) {
        NativeBuffer.reclaim(false);
        this.processChunkEvents();
        this.useEntityCulling = SodiumClientMod.options().performance.useEntityCulling;
        if (this.client.options.getEffectiveRenderDistance() != this.renderDistance) {
            this.reload();
        }
        ProfilerFiller profiler = this.client.getProfiler();
        profiler.push("camera_setup");
        LocalPlayer player = this.client.player;
        if (player == null) {
            throw new IllegalStateException("Client instance has no active player entity");
        } else {
            Vec3 pos = camera.getPosition();
            float pitch = camera.getXRot();
            float yaw = camera.getYRot();
            float fogDistance = RenderSystem.getShaderFogEnd();
            boolean dirty = pos.x != this.lastCameraX || pos.y != this.lastCameraY || pos.z != this.lastCameraZ || (double) pitch != this.lastCameraPitch || (double) yaw != this.lastCameraYaw || fogDistance != this.lastFogDistance;
            if (dirty) {
                this.renderSectionManager.markGraphDirty();
            }
            this.currentViewport = viewport;
            this.lastCameraX = pos.x;
            this.lastCameraY = pos.y;
            this.lastCameraZ = pos.z;
            this.lastCameraPitch = (double) pitch;
            this.lastCameraYaw = (double) yaw;
            this.lastFogDistance = fogDistance;
            this.renderSectionManager.runAsyncTasks();
            profiler.popPush("chunk_update");
            this.renderSectionManager.updateChunks(updateChunksImmediately);
            profiler.popPush("chunk_upload");
            this.renderSectionManager.uploadChunks();
            if (this.renderSectionManager.needsUpdate()) {
                profiler.popPush("chunk_render_lists");
                this.renderSectionManager.update(camera, viewport, frame, spectator);
            }
            if (updateChunksImmediately) {
                profiler.popPush("chunk_upload_immediately");
                this.renderSectionManager.uploadChunks();
            }
            profiler.popPush("chunk_render_tick");
            this.renderSectionManager.tickVisibleRenders();
            profiler.pop();
            Entity.setViewScale(Mth.clamp((double) this.client.options.getEffectiveRenderDistance() / 8.0, 1.0, 2.5) * this.client.options.entityDistanceScaling().get());
        }
    }

    private void processChunkEvents() {
        ChunkTracker tracker = ChunkTrackerHolder.get(this.world);
        tracker.forEachEvent(this.renderSectionManager::onChunkAdded, this.renderSectionManager::onChunkRemoved);
    }

    public void drawChunkLayer(RenderType renderLayer, PoseStack matrixStack, double x, double y, double z) {
        ChunkRenderMatrices matrices = ChunkRenderMatrices.from(matrixStack);
        if (renderLayer == RenderType.solid()) {
            this.renderSectionManager.renderLayer(matrices, DefaultTerrainRenderPasses.SOLID, x, y, z);
            this.renderSectionManager.renderLayer(matrices, DefaultTerrainRenderPasses.CUTOUT, x, y, z);
        } else if (renderLayer == RenderType.translucent()) {
            this.renderSectionManager.renderLayer(matrices, DefaultTerrainRenderPasses.TRANSLUCENT, x, y, z);
        }
    }

    public void reload() {
        if (this.world != null) {
            try (CommandList commandList = RenderDevice.INSTANCE.createCommandList()) {
                this.initRenderer(commandList);
            }
        }
    }

    private void initRenderer(CommandList commandList) {
        if (this.renderSectionManager != null) {
            this.renderSectionManager.destroy();
            this.renderSectionManager = null;
        }
        this.renderDistance = this.client.options.getEffectiveRenderDistance();
        this.renderSectionManager = new RenderSectionManager(this.world, this.renderDistance, commandList);
        ChunkTracker tracker = ChunkTrackerHolder.get(this.world);
        ChunkTracker.forEachChunk(tracker.getReadyChunks(), this.renderSectionManager::onChunkAdded);
        Window window = Minecraft.getInstance().getWindow();
        if (window != null) {
            window.updateVsync(Minecraft.getInstance().options.enableVsync().get());
        }
        BlendedColorProvider.checkBlendingEnabled();
    }

    public boolean didBlockEntityRequestOutline() {
        return this.blockEntityRequestedOutline;
    }

    public Iterator<BlockEntity> blockEntityIterator() {
        List<Iterator<BlockEntity>> iterators = new ArrayList();
        for (ChunkRenderList renderList : this.renderSectionManager.getRenderLists()) {
            RenderRegion renderRegion = renderList.getRegion();
            ByteIterator renderSectionIterator = renderList.sectionsWithEntitiesIterator();
            if (renderSectionIterator != null) {
                while (renderSectionIterator.hasNext()) {
                    int renderSectionId = renderSectionIterator.nextByteAsInt();
                    RenderSection renderSection = renderRegion.getSection(renderSectionId);
                    BlockEntity[] blockEntities = renderSection.getCulledBlockEntities();
                    if (blockEntities != null) {
                        iterators.add(Iterators.forArray(blockEntities));
                    }
                }
            }
        }
        for (RenderSection renderSection : this.renderSectionManager.getSectionsWithGlobalEntities()) {
            BlockEntity[] blockEntities = renderSection.getGlobalBlockEntities();
            if (blockEntities != null) {
                iterators.add(Iterators.forArray(blockEntities));
            }
        }
        return iterators.isEmpty() ? Collections.emptyIterator() : Iterators.concat(iterators.iterator());
    }

    public void forEachVisibleBlockEntity(Consumer<BlockEntity> consumer) {
        for (ChunkRenderList renderList : this.renderSectionManager.getRenderLists()) {
            RenderRegion renderRegion = renderList.getRegion();
            ByteIterator renderSectionIterator = renderList.sectionsWithEntitiesIterator();
            if (renderSectionIterator != null) {
                while (renderSectionIterator.hasNext()) {
                    int renderSectionId = renderSectionIterator.nextByteAsInt();
                    RenderSection renderSection = renderRegion.getSection(renderSectionId);
                    BlockEntity[] blockEntities = renderSection.getCulledBlockEntities();
                    if (blockEntities != null) {
                        for (BlockEntity blockEntity : blockEntities) {
                            consumer.accept(blockEntity);
                        }
                    }
                }
            }
        }
        for (RenderSection renderSection : this.renderSectionManager.getSectionsWithGlobalEntities()) {
            BlockEntity[] blockEntities = renderSection.getGlobalBlockEntities();
            if (blockEntities != null) {
                for (BlockEntity blockEntity : blockEntities) {
                    consumer.accept(blockEntity);
                }
            }
        }
    }

    public void renderBlockEntities(PoseStack matrices, RenderBuffers bufferBuilders, Long2ObjectMap<SortedSet<BlockDestructionProgress>> blockBreakingProgressions, Camera camera, float tickDelta) {
        MultiBufferSource.BufferSource immediate = bufferBuilders.bufferSource();
        Vec3 cameraPos = camera.getPosition();
        double x = cameraPos.x();
        double y = cameraPos.y();
        double z = cameraPos.z();
        BlockEntityRenderDispatcher blockEntityRenderer = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        this.blockEntityRequestedOutline = false;
        this.renderBlockEntities(matrices, bufferBuilders, blockBreakingProgressions, tickDelta, immediate, x, y, z, blockEntityRenderer);
        this.renderGlobalBlockEntities(matrices, bufferBuilders, blockBreakingProgressions, tickDelta, immediate, x, y, z, blockEntityRenderer);
    }

    private void renderBlockEntities(PoseStack matrices, RenderBuffers bufferBuilders, Long2ObjectMap<SortedSet<BlockDestructionProgress>> blockBreakingProgressions, float tickDelta, MultiBufferSource.BufferSource immediate, double x, double y, double z, BlockEntityRenderDispatcher blockEntityRenderer) {
        for (ChunkRenderList renderList : this.renderSectionManager.getRenderLists()) {
            RenderRegion renderRegion = renderList.getRegion();
            ByteIterator renderSectionIterator = renderList.sectionsWithEntitiesIterator();
            if (renderSectionIterator != null) {
                while (renderSectionIterator.hasNext()) {
                    int renderSectionId = renderSectionIterator.nextByteAsInt();
                    RenderSection renderSection = renderRegion.getSection(renderSectionId);
                    BlockEntity[] blockEntities = renderSection.getCulledBlockEntities();
                    if (blockEntities != null) {
                        for (BlockEntity blockEntity : blockEntities) {
                            if (!ENABLE_BLOCKENTITY_CULLING || this.currentViewport.isBoxVisible(blockEntity.getRenderBoundingBox())) {
                                if (blockEntity.hasCustomOutlineRendering(this.client.player)) {
                                    this.blockEntityRequestedOutline = true;
                                }
                                renderBlockEntity(matrices, bufferBuilders, blockBreakingProgressions, tickDelta, immediate, x, y, z, blockEntityRenderer, blockEntity);
                            }
                        }
                    }
                }
            }
        }
    }

    private void renderGlobalBlockEntities(PoseStack matrices, RenderBuffers bufferBuilders, Long2ObjectMap<SortedSet<BlockDestructionProgress>> blockBreakingProgressions, float tickDelta, MultiBufferSource.BufferSource immediate, double x, double y, double z, BlockEntityRenderDispatcher blockEntityRenderer) {
        for (RenderSection renderSection : this.renderSectionManager.getSectionsWithGlobalEntities()) {
            BlockEntity[] blockEntities = renderSection.getGlobalBlockEntities();
            if (blockEntities != null) {
                for (BlockEntity blockEntity : blockEntities) {
                    if (!ENABLE_BLOCKENTITY_CULLING || this.currentViewport.isBoxVisible(blockEntity.getRenderBoundingBox())) {
                        if (blockEntity.hasCustomOutlineRendering(this.client.player)) {
                            this.blockEntityRequestedOutline = true;
                        }
                        renderBlockEntity(matrices, bufferBuilders, blockBreakingProgressions, tickDelta, immediate, x, y, z, blockEntityRenderer, blockEntity);
                    }
                }
            }
        }
    }

    private static void renderBlockEntity(PoseStack matrices, RenderBuffers bufferBuilders, Long2ObjectMap<SortedSet<BlockDestructionProgress>> blockBreakingProgressions, float tickDelta, MultiBufferSource.BufferSource immediate, double x, double y, double z, BlockEntityRenderDispatcher dispatcher, BlockEntity entity) {
        BlockPos pos = entity.getBlockPos();
        matrices.pushPose();
        matrices.translate((double) pos.m_123341_() - x, (double) pos.m_123342_() - y, (double) pos.m_123343_() - z);
        MultiBufferSource consumer = immediate;
        SortedSet<BlockDestructionProgress> breakingInfo = (SortedSet<BlockDestructionProgress>) blockBreakingProgressions.get(pos.asLong());
        if (breakingInfo != null && !breakingInfo.isEmpty()) {
            int stage = ((BlockDestructionProgress) breakingInfo.last()).getProgress();
            if (stage >= 0) {
                VertexConsumer bufferBuilder = bufferBuilders.crumblingBufferSource().getBuffer((RenderType) ModelBakery.DESTROY_TYPES.get(stage));
                PoseStack.Pose entry = matrices.last();
                VertexConsumer transformer = new SheetedDecalTextureGenerator(bufferBuilder, entry.pose(), entry.normal(), 1.0F);
                consumer = layer -> layer.affectsCrumbling() ? VertexMultiConsumer.create(transformer, immediate.getBuffer(layer)) : immediate.getBuffer(layer);
            }
        }
        try {
            dispatcher.render(entity, tickDelta, matrices, consumer);
        } catch (RuntimeException var20) {
            if (!entity.isRemoved()) {
                throw var20;
            }
            SodiumClientMod.logger().error("Suppressing crash from removed block entity", var20);
        }
        matrices.popPose();
    }

    private static boolean isInfiniteExtentsBox(AABB box) {
        return Double.isInfinite(box.minX) || Double.isInfinite(box.minY) || Double.isInfinite(box.minZ) || Double.isInfinite(box.maxX) || Double.isInfinite(box.maxY) || Double.isInfinite(box.maxZ);
    }

    public boolean isEntityVisible(Entity entity) {
        if (!this.useEntityCulling) {
            return true;
        } else if (!this.client.shouldEntityAppearGlowing(entity) && !entity.shouldShowName()) {
            AABB box = entity.getBoundingBoxForCulling();
            if (isInfiniteExtentsBox(box)) {
                return true;
            } else {
                double entityVolume = (box.maxX - box.minX) * (box.maxY - box.minY) * (box.maxZ - box.minZ);
                return entityVolume > 61440.0 ? true : this.isBoxVisible(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
            }
        } else {
            return true;
        }
    }

    public boolean isBoxVisible(double x1, double y1, double z1, double x2, double y2, double z2) {
        if (!(y2 < (double) this.world.m_141937_() + 0.5) && !(y1 > (double) this.world.m_151558_() - 0.5)) {
            int minX = SectionPos.posToSectionCoord(x1 - 0.5);
            int minY = SectionPos.posToSectionCoord(y1 - 0.5);
            int minZ = SectionPos.posToSectionCoord(z1 - 0.5);
            int maxX = SectionPos.posToSectionCoord(x2 + 0.5);
            int maxY = SectionPos.posToSectionCoord(y2 + 0.5);
            int maxZ = SectionPos.posToSectionCoord(z2 + 0.5);
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    for (int y = minY; y <= maxY; y++) {
                        if (this.renderSectionManager.isSectionVisible(x, y, z)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public String getChunksDebugString() {
        return String.format("C: %d/%d D: %d", this.renderSectionManager.getVisibleChunkCount(), this.renderSectionManager.getTotalSections(), this.renderDistance);
    }

    public void scheduleRebuildForBlockArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean important) {
        this.scheduleRebuildForChunks(minX >> 4, minY >> 4, minZ >> 4, maxX >> 4, maxY >> 4, maxZ >> 4, important);
    }

    public void scheduleRebuildForChunks(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean important) {
        for (int chunkX = minX; chunkX <= maxX; chunkX++) {
            for (int chunkY = minY; chunkY <= maxY; chunkY++) {
                for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
                    this.scheduleRebuildForChunk(chunkX, chunkY, chunkZ, important);
                }
            }
        }
    }

    public void scheduleRebuildForChunk(int x, int y, int z, boolean important) {
        this.renderSectionManager.scheduleRebuild(x, y, z, important);
    }

    public Collection<String> getDebugStrings() {
        return this.renderSectionManager.getDebugStrings();
    }

    public boolean isSectionReady(int x, int y, int z) {
        return this.renderSectionManager.isSectionBuilt(x, y, z);
    }

    @Deprecated
    public void onChunkAdded(int x, int z) {
        ChunkTracker tracker = ChunkTrackerHolder.get(this.world);
        tracker.onChunkStatusAdded(x, z, 1);
    }

    @Deprecated
    public void onChunkLightAdded(int x, int z) {
        ChunkTracker tracker = ChunkTrackerHolder.get(this.world);
        tracker.onChunkStatusAdded(x, z, 2);
    }

    @Deprecated
    public void onChunkRemoved(int x, int z) {
        ChunkTracker tracker = ChunkTrackerHolder.get(this.world);
        tracker.onChunkStatusRemoved(x, z, 3);
    }
}