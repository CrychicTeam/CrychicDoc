package net.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import java.util.Optional;

public class OutlineBufferSource implements MultiBufferSource {

    private final MultiBufferSource.BufferSource bufferSource;

    private final MultiBufferSource.BufferSource outlineBufferSource = MultiBufferSource.immediate(new BufferBuilder(256));

    private int teamR = 255;

    private int teamG = 255;

    private int teamB = 255;

    private int teamA = 255;

    public OutlineBufferSource(MultiBufferSource.BufferSource multiBufferSourceBufferSource0) {
        this.bufferSource = multiBufferSourceBufferSource0;
    }

    @Override
    public VertexConsumer getBuffer(RenderType renderType0) {
        if (renderType0.isOutline()) {
            VertexConsumer $$1 = this.outlineBufferSource.getBuffer(renderType0);
            return new OutlineBufferSource.EntityOutlineGenerator($$1, this.teamR, this.teamG, this.teamB, this.teamA);
        } else {
            VertexConsumer $$2 = this.bufferSource.getBuffer(renderType0);
            Optional<RenderType> $$3 = renderType0.outline();
            if ($$3.isPresent()) {
                VertexConsumer $$4 = this.outlineBufferSource.getBuffer((RenderType) $$3.get());
                OutlineBufferSource.EntityOutlineGenerator $$5 = new OutlineBufferSource.EntityOutlineGenerator($$4, this.teamR, this.teamG, this.teamB, this.teamA);
                return VertexMultiConsumer.create($$5, $$2);
            } else {
                return $$2;
            }
        }
    }

    public void setColor(int int0, int int1, int int2, int int3) {
        this.teamR = int0;
        this.teamG = int1;
        this.teamB = int2;
        this.teamA = int3;
    }

    public void endOutlineBatch() {
        this.outlineBufferSource.endBatch();
    }

    static class EntityOutlineGenerator extends DefaultedVertexConsumer {

        private final VertexConsumer delegate;

        private double x;

        private double y;

        private double z;

        private float u;

        private float v;

        EntityOutlineGenerator(VertexConsumer vertexConsumer0, int int1, int int2, int int3, int int4) {
            this.delegate = vertexConsumer0;
            super.defaultColor(int1, int2, int3, int4);
        }

        @Override
        public void defaultColor(int int0, int int1, int int2, int int3) {
        }

        @Override
        public void unsetDefaultColor() {
        }

        @Override
        public VertexConsumer vertex(double double0, double double1, double double2) {
            this.x = double0;
            this.y = double1;
            this.z = double2;
            return this;
        }

        @Override
        public VertexConsumer color(int int0, int int1, int int2, int int3) {
            return this;
        }

        @Override
        public VertexConsumer uv(float float0, float float1) {
            this.u = float0;
            this.v = float1;
            return this;
        }

        @Override
        public VertexConsumer overlayCoords(int int0, int int1) {
            return this;
        }

        @Override
        public VertexConsumer uv2(int int0, int int1) {
            return this;
        }

        @Override
        public VertexConsumer normal(float float0, float float1, float float2) {
            return this;
        }

        @Override
        public void vertex(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, int int9, int int10, float float11, float float12, float float13) {
            this.delegate.vertex((double) float0, (double) float1, (double) float2).color(this.f_85825_, this.f_85826_, this.f_85827_, this.f_85828_).uv(float7, float8).endVertex();
        }

        @Override
        public void endVertex() {
            this.delegate.vertex(this.x, this.y, this.z).color(this.f_85825_, this.f_85826_, this.f_85827_, this.f_85828_).uv(this.u, this.v).endVertex();
        }
    }
}