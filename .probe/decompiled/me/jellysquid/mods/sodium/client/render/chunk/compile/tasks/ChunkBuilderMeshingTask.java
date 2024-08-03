package me.jellysquid.mods.sodium.client.render.chunk.compile.tasks;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.Map;
import java.util.Objects;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBufferSorter;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderCache;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionMeshParts;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.util.task.CancellationToken;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import org.embeddedt.embeddium.api.ChunkDataBuiltEvent;
import org.embeddedt.embeddium.chunk.MeshAppenderRenderer;
import org.embeddedt.embeddium.model.ModelDataSnapshotter;
import org.embeddedt.embeddium.model.UnwrappableBakedModel;

public class ChunkBuilderMeshingTask extends ChunkBuilderTask<ChunkBuildOutput> {

    private final RandomSource random = new SingleThreadedRandomSource(42L);

    private final RenderSection render;

    private final ChunkRenderContext renderContext;

    private final int buildTime;

    private final Map<BlockPos, ModelData> modelDataMap;

    private Vec3 camera = Vec3.ZERO;

    public ChunkBuilderMeshingTask(RenderSection render, ChunkRenderContext renderContext, int time) {
        this.render = render;
        this.renderContext = renderContext;
        this.buildTime = time;
        this.modelDataMap = ModelDataSnapshotter.getModelDataForSection(Minecraft.getInstance().level, this.renderContext.getOrigin());
    }

    public ChunkBuilderMeshingTask withCameraPosition(Vec3 camera) {
        this.camera = camera;
        return this;
    }

    public ChunkBuildOutput execute(ChunkBuildContext buildContext, CancellationToken cancellationToken) {
        BuiltSectionInfo.Builder renderData = new BuiltSectionInfo.Builder();
        VisGraph occluder = new VisGraph();
        ChunkBuildBuffers buffers = buildContext.buffers;
        buffers.init(renderData, this.render.getSectionIndex());
        BlockRenderCache cache = buildContext.cache;
        cache.init(this.renderContext);
        WorldSlice slice = cache.getWorldSlice();
        int minX = this.render.getOriginX();
        int minY = this.render.getOriginY();
        int minZ = this.render.getOriginZ();
        int maxX = minX + 16;
        int maxY = minY + 16;
        int maxZ = minZ + 16;
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(minX, minY, minZ);
        BlockPos.MutableBlockPos modelOffset = new BlockPos.MutableBlockPos();
        BlockRenderContext context = new BlockRenderContext(slice);
        try {
            for (int y = minY; y < maxY; y++) {
                if (cancellationToken.isCancelled()) {
                    return null;
                }
                for (int z = minZ; z < maxZ; z++) {
                    for (int x = minX; x < maxX; x++) {
                        BlockState blockState = slice.getBlockState(x, y, z);
                        if (!blockState.m_60795_() || blockState.m_155947_()) {
                            blockPos.set(x, y, z);
                            modelOffset.set(x & 15, y & 15, z & 15);
                            if (blockState.m_60799_() == RenderShape.MODEL) {
                                BakedModel model = cache.getBlockModels().getBlockModel(blockState);
                                ModelData modelData = model.getModelData(context.localSlice(), blockPos, blockState, (ModelData) this.modelDataMap.getOrDefault(blockPos, ModelData.EMPTY));
                                long seed = blockState.m_60726_(blockPos);
                                this.random.setSeed(seed);
                                model = UnwrappableBakedModel.unwrapIfPossible(model, this.random);
                                this.random.setSeed(seed);
                                for (RenderType layer : model.getRenderTypes(blockState, this.random, modelData)) {
                                    context.update(blockPos, modelOffset, blockState, model, seed, modelData, layer);
                                    cache.getBlockRenderer().renderModel(context, buffers);
                                }
                            }
                            FluidState fluidState = blockState.m_60819_();
                            if (!fluidState.isEmpty()) {
                                cache.getFluidRenderer().render(slice, fluidState, blockPos, modelOffset, buffers);
                            }
                            if (blockState.m_155947_()) {
                                BlockEntity entity = slice.getBlockEntity(blockPos);
                                if (entity != null) {
                                    BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(entity);
                                    if (renderer != null) {
                                        renderData.addBlockEntity(entity, !renderer.shouldRenderOffScreen(entity));
                                    }
                                }
                            }
                            if (blockState.m_60804_(slice, blockPos)) {
                                occluder.setOpaque(blockPos);
                            }
                        }
                    }
                }
            }
            MeshAppenderRenderer.renderMeshAppenders(this.renderContext.getMeshAppenders(), context.localSlice(), this.renderContext.getOrigin(), buffers);
        } catch (ReportedException var27) {
            throw this.fillCrashInfo(var27.getReport(), slice, blockPos);
        } catch (Throwable var28) {
            throw this.fillCrashInfo(CrashReport.forThrowable(var28, "Encountered exception while building chunk meshes"), slice, blockPos);
        }
        Map<TerrainRenderPass, BuiltSectionMeshParts> meshes = new Reference2ReferenceOpenHashMap();
        for (TerrainRenderPass pass : DefaultTerrainRenderPasses.ALL) {
            BuiltSectionMeshParts mesh = buffers.createMesh(pass);
            if (mesh != null) {
                if (pass.isSorted()) {
                    Objects.requireNonNull(mesh.getIndexData());
                    ChunkBufferSorter.sort(mesh.getIndexData(), mesh.getSortState(), (float) this.camera.x - (float) minX, (float) this.camera.y - (float) minY, (float) this.camera.z - (float) minZ);
                }
                meshes.put(pass, mesh);
                renderData.addRenderPass(pass);
            }
        }
        renderData.setOcclusionData(occluder.resolve());
        ChunkDataBuiltEvent.BUS.post(new ChunkDataBuiltEvent(renderData));
        return new ChunkBuildOutput(this.render, renderData.build(), meshes, this.buildTime);
    }

    private ReportedException fillCrashInfo(CrashReport report, WorldSlice slice, BlockPos pos) {
        CrashReportCategory crashReportSection = report.addCategory("Block being rendered", 1);
        BlockState state = null;
        try {
            state = slice.getBlockState(pos);
        } catch (Exception var7) {
        }
        CrashReportCategory.populateBlockDetails(crashReportSection, slice, pos, state);
        crashReportSection.setDetail("Chunk section", this.render);
        if (this.renderContext != null) {
            crashReportSection.setDetail("Render context volume", this.renderContext.getVolume());
        }
        return new ReportedException(report);
    }
}