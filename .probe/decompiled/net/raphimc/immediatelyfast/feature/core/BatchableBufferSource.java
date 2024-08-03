package net.raphimc.immediatelyfast.feature.core;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.compat.IrisCompat;

public class BatchableBufferSource extends MultiBufferSource.BufferSource implements AutoCloseable {

    private static final BufferBuilder FALLBACK_BUFFER = new BufferBuilder(0);

    protected final Reference2ObjectMap<RenderType, ReferenceSet<BufferBuilder>> fallbackBuffers = new Reference2ObjectLinkedOpenHashMap();

    protected final ReferenceSet<RenderType> activeLayers = new ReferenceLinkedOpenHashSet();

    protected boolean drawFallbackLayersFirst = false;

    public BatchableBufferSource() {
        this(ImmutableMap.of());
    }

    public BatchableBufferSource(Map<RenderType, BufferBuilder> layerBuffers) {
        this(FALLBACK_BUFFER, layerBuffers);
    }

    public BatchableBufferSource(BufferBuilder fallbackBuffer, Map<RenderType, BufferBuilder> layerBuffers) {
        super(fallbackBuffer, layerBuffers);
    }

    @Override
    public VertexConsumer getBuffer(RenderType layer) {
        Optional<RenderType> newLayer = layer.asOptional();
        if (!this.drawFallbackLayersFirst && !this.f_109906_.equals(newLayer) && this.f_109906_.isPresent() && !this.f_109905_.containsKey(this.f_109906_.get())) {
            this.drawFallbackLayersFirst = true;
        }
        this.f_109906_ = newLayer;
        BufferBuilder bufferBuilder = this.getOrCreateBufferBuilder(layer);
        if (bufferBuilder.building() && !layer.canConsolidateConsecutiveGeometry()) {
            throw new IllegalStateException("Tried to write shared vertices into the same buffer");
        } else {
            if (!bufferBuilder.building()) {
                if (IrisCompat.IRIS_LOADED && !IrisCompat.isRenderingLevel.getAsBoolean()) {
                    IrisCompat.iris$beginWithoutExtending.accept(bufferBuilder, layer.mode(), layer.format());
                } else {
                    bufferBuilder.begin(layer.mode(), layer.format());
                }
                this.activeLayers.add(layer);
            } else if ((ImmediatelyFast.config.debug_only_use_last_usage_for_batch_ordering || layer.f_110133_.contains("immediatelyfast:renderlast")) && this.activeLayers.contains(layer)) {
                this.activeLayers.remove(layer);
                this.activeLayers.add(layer);
            }
            return bufferBuilder;
        }
    }

    @Override
    public void endLastBatch() {
        this.f_109906_ = Optional.empty();
        this.drawFallbackLayersFirst = false;
        int sortedLayersLength = 0;
        RenderType[] sortedLayers = new RenderType[this.activeLayers.size()];
        ObjectIterator i = this.activeLayers.iterator();
        while (i.hasNext()) {
            RenderType layer = (RenderType) i.next();
            if (!this.f_109905_.containsKey(layer)) {
                sortedLayers[sortedLayersLength++] = layer;
            }
        }
        if (sortedLayersLength != 0) {
            Arrays.sort(sortedLayers, (l1, l2) -> Integer.compare(this.getLayerOrder(l1), this.getLayerOrder(l2)));
            for (int ix = 0; ix < sortedLayersLength; ix++) {
                this.endBatch(sortedLayers[ix]);
            }
        }
    }

    @Override
    public void endBatch() {
        if (this.activeLayers.isEmpty()) {
            this.close();
        } else {
            this.endLastBatch();
            for (RenderType layer : this.f_109905_.keySet()) {
                this.endBatch(layer);
            }
        }
    }

    @Override
    public void endBatch(RenderType layer) {
        if (this.drawFallbackLayersFirst) {
            this.endLastBatch();
        }
        if (IrisCompat.IRIS_LOADED && !IrisCompat.isRenderingLevel.getAsBoolean()) {
            IrisCompat.renderWithExtendedVertexFormat.accept(false);
        }
        this.activeLayers.remove(layer);
        for (BufferBuilder bufferBuilder : this.getBufferBuilder(layer)) {
            if (bufferBuilder != null) {
                layer.end(bufferBuilder, RenderSystem.getVertexSorting());
            }
        }
        this.fallbackBuffers.remove(layer);
        if (IrisCompat.IRIS_LOADED && !IrisCompat.isRenderingLevel.getAsBoolean()) {
            IrisCompat.renderWithExtendedVertexFormat.accept(true);
        }
    }

    public void close() {
        this.f_109906_ = Optional.empty();
        this.drawFallbackLayersFirst = false;
        ObjectIterator var1 = this.activeLayers.iterator();
        while (var1.hasNext()) {
            RenderType layer = (RenderType) var1.next();
            for (BufferBuilder bufferBuilder : this.getBufferBuilder(layer)) {
                bufferBuilder.end().release();
            }
        }
        this.activeLayers.clear();
        this.fallbackBuffers.clear();
    }

    public boolean hasActiveLayers() {
        return !this.activeLayers.isEmpty();
    }

    protected BufferBuilder getOrCreateBufferBuilder(RenderType layer) {
        if (!layer.canConsolidateConsecutiveGeometry()) {
            return this.addNewFallbackBuffer(layer);
        } else if (this.f_109905_.containsKey(layer)) {
            return (BufferBuilder) this.f_109905_.get(layer);
        } else {
            return this.fallbackBuffers.containsKey(layer) ? (BufferBuilder) ((ReferenceSet) this.fallbackBuffers.get(layer)).iterator().next() : this.addNewFallbackBuffer(layer);
        }
    }

    protected Set<BufferBuilder> getBufferBuilder(RenderType layer) {
        if (this.fallbackBuffers.containsKey(layer)) {
            return (Set<BufferBuilder>) this.fallbackBuffers.get(layer);
        } else {
            return this.f_109905_.containsKey(layer) ? Collections.singleton((BufferBuilder) this.f_109905_.get(layer)) : Collections.emptySet();
        }
    }

    protected BufferBuilder addNewFallbackBuffer(RenderType layer) {
        BufferBuilder bufferBuilder = BufferBuilderPool.get();
        ((ReferenceSet) this.fallbackBuffers.computeIfAbsent(layer, k -> new ReferenceLinkedOpenHashSet())).add(bufferBuilder);
        return bufferBuilder;
    }

    protected int getLayerOrder(RenderType layer) {
        if (layer == null) {
            return Integer.MAX_VALUE;
        } else {
            if (layer instanceof RenderType.CompositeRenderType multiPhase) {
                ResourceLocation textureId = (ResourceLocation) multiPhase.state().textureState.cutoutTexture().orElse(null);
                if (textureId != null && textureId.toString().startsWith("minecraft:textures/entity/horse/")) {
                    String horseTexturePath = textureId.toString().substring("minecraft:textures/entity/horse/".length());
                    if (horseTexturePath.startsWith("horse_markings")) {
                        return 2;
                    }
                    if (horseTexturePath.startsWith("armor/")) {
                        return 3;
                    }
                    return 1;
                }
            }
            return !layer.sortOnUpload ? Integer.MIN_VALUE : 2147483646;
        }
    }
}