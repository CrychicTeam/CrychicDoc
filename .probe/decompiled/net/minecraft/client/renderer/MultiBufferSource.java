package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public interface MultiBufferSource {

    static MultiBufferSource.BufferSource immediate(BufferBuilder bufferBuilder0) {
        return immediateWithBuffers(ImmutableMap.of(), bufferBuilder0);
    }

    static MultiBufferSource.BufferSource immediateWithBuffers(Map<RenderType, BufferBuilder> mapRenderTypeBufferBuilder0, BufferBuilder bufferBuilder1) {
        return new MultiBufferSource.BufferSource(bufferBuilder1, mapRenderTypeBufferBuilder0);
    }

    VertexConsumer getBuffer(RenderType var1);

    public static class BufferSource implements MultiBufferSource {

        protected final BufferBuilder builder;

        protected final Map<RenderType, BufferBuilder> fixedBuffers;

        protected Optional<RenderType> lastState = Optional.empty();

        protected final Set<BufferBuilder> startedBuffers = Sets.newHashSet();

        protected BufferSource(BufferBuilder bufferBuilder0, Map<RenderType, BufferBuilder> mapRenderTypeBufferBuilder1) {
            this.builder = bufferBuilder0;
            this.fixedBuffers = mapRenderTypeBufferBuilder1;
        }

        @Override
        public VertexConsumer getBuffer(RenderType renderType0) {
            Optional<RenderType> $$1 = renderType0.asOptional();
            BufferBuilder $$2 = this.getBuilderRaw(renderType0);
            if (!Objects.equals(this.lastState, $$1) || !renderType0.canConsolidateConsecutiveGeometry()) {
                if (this.lastState.isPresent()) {
                    RenderType $$3 = (RenderType) this.lastState.get();
                    if (!this.fixedBuffers.containsKey($$3)) {
                        this.endBatch($$3);
                    }
                }
                if (this.startedBuffers.add($$2)) {
                    $$2.begin(renderType0.mode(), renderType0.format());
                }
                this.lastState = $$1;
            }
            return $$2;
        }

        private BufferBuilder getBuilderRaw(RenderType renderType0) {
            return (BufferBuilder) this.fixedBuffers.getOrDefault(renderType0, this.builder);
        }

        public void endLastBatch() {
            if (this.lastState.isPresent()) {
                RenderType $$0 = (RenderType) this.lastState.get();
                if (!this.fixedBuffers.containsKey($$0)) {
                    this.endBatch($$0);
                }
                this.lastState = Optional.empty();
            }
        }

        public void endBatch() {
            this.lastState.ifPresent(p_109917_ -> {
                VertexConsumer $$1 = this.getBuffer(p_109917_);
                if ($$1 == this.builder) {
                    this.endBatch(p_109917_);
                }
            });
            for (RenderType $$0 : this.fixedBuffers.keySet()) {
                this.endBatch($$0);
            }
        }

        public void endBatch(RenderType renderType0) {
            BufferBuilder $$1 = this.getBuilderRaw(renderType0);
            boolean $$2 = Objects.equals(this.lastState, renderType0.asOptional());
            if ($$2 || $$1 != this.builder) {
                if (this.startedBuffers.remove($$1)) {
                    renderType0.end($$1, RenderSystem.getVertexSorting());
                    if ($$2) {
                        this.lastState = Optional.empty();
                    }
                }
            }
        }
    }
}