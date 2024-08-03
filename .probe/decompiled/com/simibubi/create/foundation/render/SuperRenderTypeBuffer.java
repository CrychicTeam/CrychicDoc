package com.simibubi.create.foundation.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.SortedMap;
import net.minecraft.Util;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.ModelBakery;

public class SuperRenderTypeBuffer implements MultiBufferSource {

    private static final SuperRenderTypeBuffer INSTANCE = new SuperRenderTypeBuffer();

    private SuperRenderTypeBuffer.SuperRenderTypeBufferPhase earlyBuffer = new SuperRenderTypeBuffer.SuperRenderTypeBufferPhase();

    private SuperRenderTypeBuffer.SuperRenderTypeBufferPhase defaultBuffer = new SuperRenderTypeBuffer.SuperRenderTypeBufferPhase();

    private SuperRenderTypeBuffer.SuperRenderTypeBufferPhase lateBuffer = new SuperRenderTypeBuffer.SuperRenderTypeBufferPhase();

    public static SuperRenderTypeBuffer getInstance() {
        return INSTANCE;
    }

    public VertexConsumer getEarlyBuffer(RenderType type) {
        return this.earlyBuffer.bufferSource.getBuffer(type);
    }

    @Override
    public VertexConsumer getBuffer(RenderType type) {
        return this.defaultBuffer.bufferSource.getBuffer(type);
    }

    public VertexConsumer getLateBuffer(RenderType type) {
        return this.lateBuffer.bufferSource.getBuffer(type);
    }

    public void draw() {
        this.earlyBuffer.bufferSource.endBatch();
        this.defaultBuffer.bufferSource.endBatch();
        this.lateBuffer.bufferSource.endBatch();
    }

    public void draw(RenderType type) {
        this.earlyBuffer.bufferSource.endBatch(type);
        this.defaultBuffer.bufferSource.endBatch(type);
        this.lateBuffer.bufferSource.endBatch(type);
    }

    private static class SuperRenderTypeBufferPhase {

        private final ChunkBufferBuilderPack fixedBufferPack = new ChunkBufferBuilderPack();

        private final SortedMap<RenderType, BufferBuilder> fixedBuffers = Util.make(new Object2ObjectLinkedOpenHashMap(), map -> {
            map.put(Sheets.solidBlockSheet(), this.fixedBufferPack.builder(RenderType.solid()));
            map.put(Sheets.cutoutBlockSheet(), this.fixedBufferPack.builder(RenderType.cutout()));
            map.put(Sheets.bannerSheet(), this.fixedBufferPack.builder(RenderType.cutoutMipped()));
            map.put(Sheets.translucentCullBlockSheet(), this.fixedBufferPack.builder(RenderType.translucent()));
            put(map, Sheets.shieldSheet());
            put(map, Sheets.bedSheet());
            put(map, Sheets.shulkerBoxSheet());
            put(map, Sheets.signSheet());
            put(map, Sheets.chestSheet());
            put(map, RenderType.translucentNoCrumbling());
            put(map, RenderType.armorGlint());
            put(map, RenderType.armorEntityGlint());
            put(map, RenderType.glint());
            put(map, RenderType.glintDirect());
            put(map, RenderType.glintTranslucent());
            put(map, RenderType.entityGlint());
            put(map, RenderType.entityGlintDirect());
            put(map, RenderType.waterMask());
            put(map, RenderTypes.getOutlineSolid());
            ModelBakery.DESTROY_TYPES.forEach(p_173062_ -> put(map, p_173062_));
        });

        private final MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediateWithBuffers(this.fixedBuffers, new BufferBuilder(256));

        private static void put(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> map, RenderType type) {
            map.put(type, new BufferBuilder(type.bufferSize()));
        }
    }
}