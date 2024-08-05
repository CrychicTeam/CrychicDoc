package net.raphimc.immediatelyfast.feature.batching;

import com.mojang.blaze3d.vertex.BufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.raphimc.immediatelyfast.ImmediatelyFast;

public class BatchingBuffers {

    public static MultiBufferSource FILL_CONSUMER = null;

    public static MultiBufferSource TEXTURE_CONSUMER = null;

    public static MultiBufferSource TEXT_CONSUMER = null;

    public static MultiBufferSource ITEM_MODEL_CONSUMER = null;

    public static MultiBufferSource ITEM_OVERLAY_CONSUMER = null;

    private static final BatchingBuffer HUD_BATCH = new BatchingBuffer();

    private static final BatchingBuffer DEBUG_HUD_BATCH = new GuiOverlayFirstBatchingBuffer();

    private static final BatchingBuffer ITEM_MODEL_BATCH = new ItemModelBatchingBuffer();

    private static final BatchingBuffer ITEM_OVERLAY_BATCH = new BatchingBuffer();

    private static MultiBufferSource PREV_FILL_CONSUMER = null;

    private static MultiBufferSource PREV_TEXT_CONSUMER = null;

    private static MultiBufferSource PREV_TEXTURE_CONSUMER = null;

    public static void beginHudBatching() {
        beginHudBatching(HUD_BATCH);
    }

    public static void beginDebugHudBatching() {
        beginHudBatching(DEBUG_HUD_BATCH);
    }

    public static void beginHudBatching(BatchingBuffer batch) {
        if (batch.hasActiveLayers()) {
            ImmediatelyFast.LOGGER.warn("HUD batching was already active! endHudBatching() was not called before beginHudBatching(). This will cause rendering issues.");
            batch.close();
        }
        FILL_CONSUMER = batch;
        TEXTURE_CONSUMER = batch;
        TEXT_CONSUMER = batch;
        beginItemModelBatching();
        beginItemOverlayBatching();
    }

    public static void endHudBatching() {
        endHudBatching(HUD_BATCH);
    }

    public static void endDebugHudBatching() {
        endHudBatching(DEBUG_HUD_BATCH);
    }

    public static void endHudBatching(BatchingBuffer batch) {
        FILL_CONSUMER = null;
        TEXTURE_CONSUMER = null;
        TEXT_CONSUMER = null;
        RenderSystemState renderSystemState = RenderSystemState.current();
        batch.m_109911_();
        endItemModelBatching();
        endItemOverlayBatching();
        renderSystemState.apply();
    }

    public static boolean isHudBatching() {
        return TEXT_CONSUMER != null || TEXTURE_CONSUMER != null || FILL_CONSUMER != null || ITEM_MODEL_CONSUMER != null || ITEM_OVERLAY_CONSUMER != null;
    }

    public static boolean hasDataToDraw() {
        return HUD_BATCH.hasActiveLayers() || DEBUG_HUD_BATCH.hasActiveLayers() || ITEM_MODEL_BATCH.hasActiveLayers() || ITEM_OVERLAY_BATCH.hasActiveLayers();
    }

    public static void forceDrawBuffers() {
        RenderSystemState renderSystemState = RenderSystemState.current();
        HUD_BATCH.m_109911_();
        DEBUG_HUD_BATCH.m_109911_();
        ITEM_MODEL_BATCH.m_109911_();
        ITEM_OVERLAY_BATCH.m_109911_();
        renderSystemState.apply();
    }

    private static void beginItemModelBatching() {
        if (ITEM_MODEL_BATCH.hasActiveLayers()) {
            ImmediatelyFast.LOGGER.warn("Item model batching was already active! endItemModelBatching() was not called before beginItemModelBatching(). This will cause rendering issues.");
            ITEM_MODEL_BATCH.close();
        }
        ITEM_MODEL_CONSUMER = ITEM_MODEL_BATCH;
    }

    private static void endItemModelBatching() {
        ITEM_MODEL_CONSUMER = null;
        ITEM_MODEL_BATCH.m_109911_();
    }

    private static void beginItemOverlayBatching() {
        if (ITEM_OVERLAY_BATCH.hasActiveLayers()) {
            ImmediatelyFast.LOGGER.warn("Item overlay batching was already active! endItemOverlayBatching() was not called before beginItemOverlayBatching(). This will cause rendering issues.");
            ITEM_OVERLAY_BATCH.close();
        }
        ITEM_OVERLAY_CONSUMER = ITEM_OVERLAY_BATCH;
    }

    private static void endItemOverlayBatching() {
        ITEM_OVERLAY_CONSUMER = null;
        ITEM_OVERLAY_BATCH.m_109911_();
    }

    public static void beginItemOverlayRendering() {
        if (ITEM_OVERLAY_CONSUMER != null) {
            PREV_FILL_CONSUMER = FILL_CONSUMER;
            PREV_TEXT_CONSUMER = TEXT_CONSUMER;
            PREV_TEXTURE_CONSUMER = TEXTURE_CONSUMER;
            FILL_CONSUMER = ITEM_OVERLAY_CONSUMER;
            TEXT_CONSUMER = ITEM_OVERLAY_CONSUMER;
            TEXTURE_CONSUMER = ITEM_OVERLAY_CONSUMER;
        }
    }

    public static void endItemOverlayRendering() {
        if (ITEM_OVERLAY_CONSUMER != null) {
            FILL_CONSUMER = PREV_FILL_CONSUMER;
            TEXT_CONSUMER = PREV_TEXT_CONSUMER;
            TEXTURE_CONSUMER = PREV_TEXTURE_CONSUMER;
        }
    }

    public static Map<RenderType, BufferBuilder> createLayerBuffers(RenderType... layers) {
        Object2ObjectMap<RenderType, BufferBuilder> layerBuffers = new Object2ObjectLinkedOpenHashMap(layers.length);
        for (RenderType layer : layers) {
            layerBuffers.put(layer, new BufferBuilder(layer.bufferSize()));
        }
        return layerBuffers;
    }
}