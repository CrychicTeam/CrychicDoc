package com.mojang.blaze3d.vertex;

import java.util.function.Consumer;

public class VertexMultiConsumer {

    public static VertexConsumer create() {
        throw new IllegalArgumentException();
    }

    public static VertexConsumer create(VertexConsumer vertexConsumer0) {
        return vertexConsumer0;
    }

    public static VertexConsumer create(VertexConsumer vertexConsumer0, VertexConsumer vertexConsumer1) {
        return new VertexMultiConsumer.Double(vertexConsumer0, vertexConsumer1);
    }

    public static VertexConsumer create(VertexConsumer... vertexConsumer0) {
        return new VertexMultiConsumer.Multiple(vertexConsumer0);
    }

    static class Double implements VertexConsumer {

        private final VertexConsumer first;

        private final VertexConsumer second;

        public Double(VertexConsumer vertexConsumer0, VertexConsumer vertexConsumer1) {
            if (vertexConsumer0 == vertexConsumer1) {
                throw new IllegalArgumentException("Duplicate delegates");
            } else {
                this.first = vertexConsumer0;
                this.second = vertexConsumer1;
            }
        }

        @Override
        public VertexConsumer vertex(double double0, double double1, double double2) {
            this.first.vertex(double0, double1, double2);
            this.second.vertex(double0, double1, double2);
            return this;
        }

        @Override
        public VertexConsumer color(int int0, int int1, int int2, int int3) {
            this.first.color(int0, int1, int2, int3);
            this.second.color(int0, int1, int2, int3);
            return this;
        }

        @Override
        public VertexConsumer uv(float float0, float float1) {
            this.first.uv(float0, float1);
            this.second.uv(float0, float1);
            return this;
        }

        @Override
        public VertexConsumer overlayCoords(int int0, int int1) {
            this.first.overlayCoords(int0, int1);
            this.second.overlayCoords(int0, int1);
            return this;
        }

        @Override
        public VertexConsumer uv2(int int0, int int1) {
            this.first.uv2(int0, int1);
            this.second.uv2(int0, int1);
            return this;
        }

        @Override
        public VertexConsumer normal(float float0, float float1, float float2) {
            this.first.normal(float0, float1, float2);
            this.second.normal(float0, float1, float2);
            return this;
        }

        @Override
        public void vertex(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, int int9, int int10, float float11, float float12, float float13) {
            this.first.vertex(float0, float1, float2, float3, float4, float5, float6, float7, float8, int9, int10, float11, float12, float13);
            this.second.vertex(float0, float1, float2, float3, float4, float5, float6, float7, float8, int9, int10, float11, float12, float13);
        }

        @Override
        public void endVertex() {
            this.first.endVertex();
            this.second.endVertex();
        }

        @Override
        public void defaultColor(int int0, int int1, int int2, int int3) {
            this.first.defaultColor(int0, int1, int2, int3);
            this.second.defaultColor(int0, int1, int2, int3);
        }

        @Override
        public void unsetDefaultColor() {
            this.first.unsetDefaultColor();
            this.second.unsetDefaultColor();
        }
    }

    static class Multiple implements VertexConsumer {

        private final VertexConsumer[] delegates;

        public Multiple(VertexConsumer[] vertexConsumer0) {
            for (int $$1 = 0; $$1 < vertexConsumer0.length; $$1++) {
                for (int $$2 = $$1 + 1; $$2 < vertexConsumer0.length; $$2++) {
                    if (vertexConsumer0[$$1] == vertexConsumer0[$$2]) {
                        throw new IllegalArgumentException("Duplicate delegates");
                    }
                }
            }
            this.delegates = vertexConsumer0;
        }

        private void forEach(Consumer<VertexConsumer> consumerVertexConsumer0) {
            for (VertexConsumer $$1 : this.delegates) {
                consumerVertexConsumer0.accept($$1);
            }
        }

        @Override
        public VertexConsumer vertex(double double0, double double1, double double2) {
            this.forEach(p_167082_ -> p_167082_.vertex(double0, double1, double2));
            return this;
        }

        @Override
        public VertexConsumer color(int int0, int int1, int int2, int int3) {
            this.forEach(p_167163_ -> p_167163_.color(int0, int1, int2, int3));
            return this;
        }

        @Override
        public VertexConsumer uv(float float0, float float1) {
            this.forEach(p_167125_ -> p_167125_.uv(float0, float1));
            return this;
        }

        @Override
        public VertexConsumer overlayCoords(int int0, int int1) {
            this.forEach(p_167167_ -> p_167167_.overlayCoords(int0, int1));
            return this;
        }

        @Override
        public VertexConsumer uv2(int int0, int int1) {
            this.forEach(p_167143_ -> p_167143_.uv2(int0, int1));
            return this;
        }

        @Override
        public VertexConsumer normal(float float0, float float1, float float2) {
            this.forEach(p_167121_ -> p_167121_.normal(float0, float1, float2));
            return this;
        }

        @Override
        public void vertex(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, int int9, int int10, float float11, float float12, float float13) {
            this.forEach(p_167116_ -> p_167116_.vertex(float0, float1, float2, float3, float4, float5, float6, float7, float8, int9, int10, float11, float12, float13));
        }

        @Override
        public void endVertex() {
            this.forEach(VertexConsumer::m_5752_);
        }

        @Override
        public void defaultColor(int int0, int int1, int int2, int int3) {
            this.forEach(p_167139_ -> p_167139_.defaultColor(int0, int1, int2, int3));
        }

        @Override
        public void unsetDefaultColor() {
            this.forEach(VertexConsumer::m_141991_);
        }
    }
}