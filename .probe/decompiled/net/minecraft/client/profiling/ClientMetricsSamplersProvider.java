package net.minecraft.client.profiling;

import com.mojang.blaze3d.systems.TimerQuery;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.util.profiling.ProfileCollector;
import net.minecraft.util.profiling.metrics.MetricCategory;
import net.minecraft.util.profiling.metrics.MetricSampler;
import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
import net.minecraft.util.profiling.metrics.profiling.ProfilerSamplerAdapter;
import net.minecraft.util.profiling.metrics.profiling.ServerMetricsSamplersProvider;

public class ClientMetricsSamplersProvider implements MetricsSamplerProvider {

    private final LevelRenderer levelRenderer;

    private final Set<MetricSampler> samplers = new ObjectOpenHashSet();

    private final ProfilerSamplerAdapter samplerFactory = new ProfilerSamplerAdapter();

    public ClientMetricsSamplersProvider(LongSupplier longSupplier0, LevelRenderer levelRenderer1) {
        this.levelRenderer = levelRenderer1;
        this.samplers.add(ServerMetricsSamplersProvider.tickTimeSampler(longSupplier0));
        this.registerStaticSamplers();
    }

    private void registerStaticSamplers() {
        this.samplers.addAll(ServerMetricsSamplersProvider.runtimeIndependentSamplers());
        this.samplers.add(MetricSampler.create("totalChunks", MetricCategory.CHUNK_RENDERING, this.levelRenderer, LevelRenderer::m_173016_));
        this.samplers.add(MetricSampler.create("renderedChunks", MetricCategory.CHUNK_RENDERING, this.levelRenderer, LevelRenderer::m_109821_));
        this.samplers.add(MetricSampler.create("lastViewDistance", MetricCategory.CHUNK_RENDERING, this.levelRenderer, LevelRenderer::m_173017_));
        ChunkRenderDispatcher $$0 = this.levelRenderer.getChunkRenderDispatcher();
        this.samplers.add(MetricSampler.create("toUpload", MetricCategory.CHUNK_RENDERING_DISPATCHING, $$0, ChunkRenderDispatcher::m_173713_));
        this.samplers.add(MetricSampler.create("freeBufferCount", MetricCategory.CHUNK_RENDERING_DISPATCHING, $$0, ChunkRenderDispatcher::m_173714_));
        this.samplers.add(MetricSampler.create("toBatchCount", MetricCategory.CHUNK_RENDERING_DISPATCHING, $$0, ChunkRenderDispatcher::m_173712_));
        if (TimerQuery.getInstance().isPresent()) {
            this.samplers.add(MetricSampler.create("gpuUtilization", MetricCategory.GPU, Minecraft.getInstance(), Minecraft::m_231464_));
        }
    }

    @Override
    public Set<MetricSampler> samplers(Supplier<ProfileCollector> supplierProfileCollector0) {
        this.samplers.addAll(this.samplerFactory.newSamplersFoundInProfiler(supplierProfileCollector0));
        return this.samplers;
    }
}