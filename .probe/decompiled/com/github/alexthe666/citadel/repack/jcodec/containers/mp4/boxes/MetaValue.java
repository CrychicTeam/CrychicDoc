package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MetaValue {

    public static final int TYPE_STRING_UTF16 = 2;

    public static final int TYPE_STRING_UTF8 = 1;

    public static final int TYPE_FLOAT_64 = 24;

    public static final int TYPE_FLOAT_32 = 23;

    public static final int TYPE_INT_32 = 67;

    public static final int TYPE_INT_16 = 66;

    public static final int TYPE_INT_8 = 65;

    public static final int TYPE_INT_V = 22;

    public static final int TYPE_UINT_V = 21;

    public static final int TYPE_JPEG = 13;

    public static final int TYPE_PNG = 13;

    public static final int TYPE_BMP = 27;

    private int type;

    private int locale;

    private byte[] data;

    private MetaValue(int type, int locale, byte[] data) {
        this.type = type;
        this.locale = locale;
        this.data = data;
    }

    public static MetaValue createInt(int value) {
        return new MetaValue(21, 0, fromInt(value));
    }

    public static MetaValue createFloat(float value) {
        return new MetaValue(23, 0, fromFloat(value));
    }

    public static MetaValue createString(String value) {
        return new MetaValue(1, 0, Platform.getBytesForCharset(value, "UTF-8"));
    }

    public static MetaValue createOther(int type, byte[] data) {
        return new MetaValue(type, 0, data);
    }

    public static MetaValue createOtherWithLocale(int type, int locale, byte[] data) {
        return new MetaValue(type, locale, data);
    }

    public int getInt() {
        if (this.type == 21 || this.type == 22) {
            switch(this.data.length) {
                case 1:
                    return this.data[0];
                case 2:
                    return this.toInt16(this.data);
                case 3:
                    return this.toInt24(this.data);
                case 4:
                    return this.toInt32(this.data);
            }
        }
        if (this.type == 65) {
            return this.data[0];
        } else if (this.type == 66) {
            return this.toInt16(this.data);
        } else {
            return this.type == 67 ? this.toInt32(this.data) : 0;
        }
    }

    public double getFloat() {
        if (this.type == 23) {
            return (double) this.toFloat(this.data);
        } else {
            return this.type == 24 ? this.toDouble(this.data) : 0.0;
        }
    }

    public String getString() {
        if (this.type == 1) {
            return Platform.stringFromCharset(this.data, "UTF-8");
        } else {
            return this.type == 2 ? Platform.stringFromCharset(this.data, "UTF-16BE") : null;
        }
    }

    public boolean isInt() {
        return this.type == 21 || this.type == 22 || this.type == 65 || this.type == 66 || this.type == 67;
    }

    public boolean isString() {
        return this.type == 1 || this.type == 2;
    }

    public boolean isFloat() {
        return this.type == 23 || this.type == 24;
    }

    public String toString() {
        if (this.isInt()) {
            return String.valueOf(this.getInt());
        } else if (this.isFloat()) {
            return String.valueOf(this.getFloat());
        } else {
            return this.isString() ? String.valueOf(this.getString()) : "BLOB";
        }
    }

    public int getType() {
        return this.type;
    }

    public int getLocale() {
        return this.locale;
    }

    public byte[] getData() {
        return this.data;
    }

    private static byte[] fromFloat(float floatValue) {
        byte[] bytes = new byte[4];
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putFloat(floatValue);
        return bytes;
    }

    private static byte[] fromInt(int value) {
        byte[] bytes = new byte[4];
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(value);
        return bytes;
    }

    private int toInt16(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getShort();
    }

    private int toInt24(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.BIG_ENDIAN);
        return (bb.getShort() & 65535) << 8 | bb.get() & 0xFF;
    }

    private int toInt32(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getInt();
    }

    private float toFloat(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getFloat();
    }

    private double toDouble(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getDouble();
    }

    public boolean isBlob() {
        return !this.isFloat() && !this.isInt() && !this.isString();
    }
}