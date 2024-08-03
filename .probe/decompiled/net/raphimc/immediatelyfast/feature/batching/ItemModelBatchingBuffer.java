package net.raphimc.immediatelyfast.feature.batching;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ReferenceObjectPair;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import java.util.Set;
import net.minecraft.client.renderer.RenderType;

public class ItemModelBatchingBuffer extends BatchingBuffer {

    private final Object2ObjectMap<ReferenceObjectPair<RenderType, LightingState>, RenderType> lightingRenderLayers = new Object2ObjectOpenHashMap();

    private final Reference2ObjectMap<RenderType, ReferenceSet<RenderType>> renderLayerMap = new Reference2ObjectOpenHashMap();

    public ItemModelBatchingBuffer() {
        super(BatchingBuffers.createLayerBuffers(RenderType.armorGlint(), RenderType.armorEntityGlint(), RenderType.glint(), RenderType.glintDirect(), RenderType.glintTranslucent(), RenderType.entityGlint(), RenderType.entityGlintDirect()));
    }

    @Override
    public VertexConsumer getBuffer(RenderType layer) {
        if (this.f_109905_.containsKey(layer)) {
            return super.m_6299_(layer);
        } else {
            LightingState lightingState = LightingState.current();
            RenderType newLayer = (RenderType) this.lightingRenderLayers.computeIfAbsent(new ReferenceObjectImmutablePair(layer, lightingState), key -> new BatchingRenderLayers.WrappedRenderLayer(layer, lightingState::saveAndApply, lightingState::revert));
            ((ReferenceSet) this.renderLayerMap.computeIfAbsent(layer, key -> new ReferenceOpenHashSet())).add(newLayer);
            return super.m_6299_(newLayer);
        }
    }

    @Override
    public void endBatch(RenderType layer) {
        Set<RenderType> renderLayers = (Set<RenderType>) this.renderLayerMap.remove(layer);
        if (renderLayers != null) {
            for (RenderType renderLayer : renderLayers) {
                super.endBatch(renderLayer);
            }
        } else {
            super.endBatch(layer);
        }
    }

    @Override
    public void endBatch() {
        RenderSystem.enableBlend();
        super.m_109911_();
        RenderSystem.disableBlend();
        this.lightingRenderLayers.clear();
        this.renderLayerMap.clear();
    }

    @Override
    public void close() {
        super.close();
        this.lightingRenderLayers.clear();
        this.renderLayerMap.clear();
    }
}