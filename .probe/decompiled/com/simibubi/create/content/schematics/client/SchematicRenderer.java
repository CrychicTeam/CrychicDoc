package com.simibubi.create.content.schematics.client;

import com.jozufozu.flywheel.core.model.ModelUtil;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.jozufozu.flywheel.core.model.ShadeSeparatingVertexConsumer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.simibubi.create.content.schematics.SchematicWorld;
import com.simibubi.create.foundation.render.BlockEntityRenderHelper;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.client.model.data.ModelData;

public class SchematicRenderer {

    private static final ThreadLocal<SchematicRenderer.ThreadLocalObjects> THREAD_LOCAL_OBJECTS = ThreadLocal.withInitial(SchematicRenderer.ThreadLocalObjects::new);

    private final Map<RenderType, SuperByteBuffer> bufferCache = new LinkedHashMap(getLayerCount());

    private boolean active;

    private boolean changed = false;

    protected SchematicWorld schematic;

    private BlockPos anchor;

    public void display(SchematicWorld world) {
        this.anchor = world.anchor;
        this.schematic = world;
        this.active = true;
        this.changed = true;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void update() {
        this.changed = true;
    }

    public void tick() {
        if (this.active) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.player != null && this.changed) {
                this.redraw();
                this.changed = false;
            }
        }
    }

    public void render(PoseStack ms, SuperRenderTypeBuffer buffers) {
        if (this.active) {
            this.bufferCache.forEach((layer, buffer) -> buffer.renderInto(ms, buffers.getBuffer(layer)));
            BlockEntityRenderHelper.renderBlockEntities(this.schematic, this.schematic.getRenderedBlockEntities(), ms, buffers);
        }
    }

    protected void redraw() {
        this.bufferCache.forEach((layerx, sbb) -> sbb.delete());
        this.bufferCache.clear();
        for (RenderType layer : RenderType.chunkBufferLayers()) {
            SuperByteBuffer buffer = this.drawLayer(layer);
            if (!buffer.isEmpty()) {
                this.bufferCache.put(layer, buffer);
            } else {
                buffer.delete();
            }
        }
    }

    protected SuperByteBuffer drawLayer(RenderType layer) {
        BlockRenderDispatcher dispatcher = ModelUtil.VANILLA_RENDERER;
        ModelBlockRenderer renderer = dispatcher.getModelRenderer();
        SchematicRenderer.ThreadLocalObjects objects = (SchematicRenderer.ThreadLocalObjects) THREAD_LOCAL_OBJECTS.get();
        PoseStack poseStack = objects.poseStack;
        RandomSource random = objects.random;
        BlockPos.MutableBlockPos mutableBlockPos = objects.mutableBlockPos;
        SchematicWorld renderWorld = this.schematic;
        renderWorld.renderMode = true;
        BoundingBox bounds = renderWorld.getBounds();
        ShadeSeparatingVertexConsumer shadeSeparatingWrapper = objects.shadeSeparatingWrapper;
        BufferBuilder shadedBuilder = objects.shadedBuilder;
        BufferBuilder unshadedBuilder = objects.unshadedBuilder;
        shadedBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        unshadedBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        shadeSeparatingWrapper.prepare(shadedBuilder, unshadedBuilder);
        ModelBlockRenderer.enableCaching();
        for (BlockPos localPos : BlockPos.betweenClosed(bounds.minX(), bounds.minY(), bounds.minZ(), bounds.maxX(), bounds.maxY(), bounds.maxZ())) {
            BlockPos pos = mutableBlockPos.setWithOffset(localPos, this.anchor);
            BlockState state = renderWorld.getBlockState(pos);
            if (state.m_60799_() == RenderShape.MODEL) {
                BakedModel model = dispatcher.getBlockModel(state);
                BlockEntity blockEntity = renderWorld.getBlockEntity(localPos);
                ModelData modelData = blockEntity != null ? blockEntity.getModelData() : ModelData.EMPTY;
                modelData = model.getModelData(renderWorld, pos, state, modelData);
                long seed = state.m_60726_(pos);
                random.setSeed(seed);
                if (model.getRenderTypes(state, random, modelData).contains(layer)) {
                    poseStack.pushPose();
                    poseStack.translate((float) localPos.m_123341_(), (float) localPos.m_123342_(), (float) localPos.m_123343_());
                    renderer.tesselateBlock(renderWorld, model, state, pos, poseStack, shadeSeparatingWrapper, true, random, seed, OverlayTexture.NO_OVERLAY, modelData, layer);
                    poseStack.popPose();
                }
            }
        }
        ModelBlockRenderer.clearCache();
        shadeSeparatingWrapper.clear();
        ShadeSeparatedBufferedData bufferedData = ModelUtil.endAndCombine(shadedBuilder, unshadedBuilder);
        renderWorld.renderMode = false;
        SuperByteBuffer sbb = new SuperByteBuffer(bufferedData);
        bufferedData.release();
        return sbb;
    }

    private static int getLayerCount() {
        return RenderType.chunkBufferLayers().size();
    }

    private static class ThreadLocalObjects {

        public final PoseStack poseStack = new PoseStack();

        public final RandomSource random = RandomSource.createNewThreadLocalInstance();

        public final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        public final ShadeSeparatingVertexConsumer shadeSeparatingWrapper = new ShadeSeparatingVertexConsumer();

        public final BufferBuilder shadedBuilder = new BufferBuilder(512);

        public final BufferBuilder unshadedBuilder = new BufferBuilder(512);
    }
}