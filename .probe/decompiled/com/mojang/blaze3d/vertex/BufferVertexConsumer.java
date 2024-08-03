package com.mojang.blaze3d.vertex;

import net.minecraft.util.Mth;

public interface BufferVertexConsumer extends VertexConsumer {

    VertexFormatElement currentElement();

    void nextElement();

    void putByte(int var1, byte var2);

    void putShort(int var1, short var2);

    void putFloat(int var1, float var2);

    @Override
    default VertexConsumer vertex(double double0, double double1, double double2) {
        if (this.currentElement().getUsage() != VertexFormatElement.Usage.POSITION) {
            return this;
        } else if (this.currentElement().getType() == VertexFormatElement.Type.FLOAT && this.currentElement().getCount() == 3) {
            this.putFloat(0, (float) double0);
            this.putFloat(4, (float) double1);
            this.putFloat(8, (float) double2);
            this.nextElement();
            return this;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    default VertexConsumer color(int int0, int int1, int int2, int int3) {
        VertexFormatElement $$4 = this.currentElement();
        if ($$4.getUsage() != VertexFormatElement.Usage.COLOR) {
            return this;
        } else if ($$4.getType() == VertexFormatElement.Type.UBYTE && $$4.getCount() == 4) {
            this.putByte(0, (byte) int0);
            this.putByte(1, (byte) int1);
            this.putByte(2, (byte) int2);
            this.putByte(3, (byte) int3);
            this.nextElement();
            return this;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    default VertexConsumer uv(float float0, float float1) {
        VertexFormatElement $$2 = this.currentElement();
        if ($$2.getUsage() == VertexFormatElement.Usage.UV && $$2.getIndex() == 0) {
            if ($$2.getType() == VertexFormatElement.Type.FLOAT && $$2.getCount() == 2) {
                this.putFloat(0, float0);
                this.putFloat(4, float1);
                this.nextElement();
                return this;
            } else {
                throw new IllegalStateException();
            }
        } else {
            return this;
        }
    }

    @Override
    default VertexConsumer overlayCoords(int int0, int int1) {
        return this.uvShort((short) int0, (short) int1, 1);
    }

    @Override
    default VertexConsumer uv2(int int0, int int1) {
        return this.uvShort((short) int0, (short) int1, 2);
    }

    default VertexConsumer uvShort(short short0, short short1, int int2) {
        VertexFormatElement $$3 = this.currentElement();
        if ($$3.getUsage() != VertexFormatElement.Usage.UV || $$3.getIndex() != int2) {
            return this;
        } else if ($$3.getType() == VertexFormatElement.Type.SHORT && $$3.getCount() == 2) {
            this.putShort(0, short0);
            this.putShort(2, short1);
            this.nextElement();
            return this;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    default VertexConsumer normal(float float0, float float1, float float2) {
        VertexFormatElement $$3 = this.currentElement();
        if ($$3.getUsage() != VertexFormatElement.Usage.NORMAL) {
            return this;
        } else if ($$3.getType() == VertexFormatElement.Type.BYTE && $$3.getCount() == 3) {
            this.putByte(0, normalIntValue(float0));
            this.putByte(1, normalIntValue(float1));
            this.putByte(2, normalIntValue(float2));
            this.nextElement();
            return this;
        } else {
            throw new IllegalStateException();
        }
    }

    static byte normalIntValue(float float0) {
        return (byte) ((int) (Mth.clamp(float0, -1.0F, 1.0F) * 127.0F) & 0xFF);
    }
}