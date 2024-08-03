package me.jellysquid.mods.sodium.client.gl.attribute;

import java.util.EnumMap;

public class GlVertexFormat<T extends Enum<T>> {

    private final Class<T> attributeEnum;

    private final EnumMap<T, GlVertexAttribute> attributesKeyed;

    private final int stride;

    public GlVertexFormat(Class<T> attributeEnum, EnumMap<T, GlVertexAttribute> attributesKeyed, int stride) {
        this.attributeEnum = attributeEnum;
        this.attributesKeyed = attributesKeyed;
        this.stride = stride;
    }

    public static <T extends Enum<T>> GlVertexFormat.Builder<T> builder(Class<T> type, int stride) {
        return new GlVertexFormat.Builder<>(type, stride);
    }

    public GlVertexAttribute getAttribute(T name) {
        GlVertexAttribute attr = (GlVertexAttribute) this.attributesKeyed.get(name);
        if (attr == null) {
            throw new NullPointerException("No attribute exists for " + name.toString());
        } else {
            return attr;
        }
    }

    public int getStride() {
        return this.stride;
    }

    public String toString() {
        return String.format("GlVertexFormat<%s>{attributes=%d,stride=%d}", this.attributeEnum.getName(), this.attributesKeyed.size(), this.stride);
    }

    public static class Builder<T extends Enum<T>> {

        private final EnumMap<T, GlVertexAttribute> attributes;

        private final Class<T> type;

        private final int stride;

        public Builder(Class<T> type, int stride) {
            this.type = type;
            this.attributes = new EnumMap(type);
            this.stride = stride;
        }

        public GlVertexFormat.Builder<T> addElement(T type, int pointer, GlVertexAttributeFormat format, int count, boolean normalized, boolean intType) {
            return this.addElement(type, new GlVertexAttribute(format, count, normalized, pointer, this.stride, intType));
        }

        private GlVertexFormat.Builder<T> addElement(T type, GlVertexAttribute attribute) {
            if (attribute.getPointer() >= this.stride) {
                throw new IllegalArgumentException("Element starts outside vertex format");
            } else if (attribute.getPointer() + attribute.getSize() > this.stride) {
                throw new IllegalArgumentException("Element extends outside vertex format");
            } else if (this.attributes.put(type, attribute) != null) {
                throw new IllegalStateException("Generic attribute " + type.name() + " already defined in vertex format");
            } else {
                return this;
            }
        }

        public GlVertexFormat<T> build() {
            int size = 0;
            for (T key : (Enum[]) this.type.getEnumConstants()) {
                GlVertexAttribute attribute = (GlVertexAttribute) this.attributes.get(key);
                if (attribute == null) {
                    throw new NullPointerException("Generic attribute not assigned to enumeration " + key.name());
                }
                size = Math.max(size, attribute.getPointer() + attribute.getSize());
            }
            if (this.stride < size) {
                throw new IllegalArgumentException("Stride is too small");
            } else {
                return new GlVertexFormat<>(this.type, this.attributes, this.stride);
            }
        }
    }
}