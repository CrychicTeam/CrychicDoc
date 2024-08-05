package me.jellysquid.mods.sodium.client.buffer;

import com.mojang.blaze3d.vertex.VertexFormatElement;

public interface ExtendedVertexFormat {

    ExtendedVertexFormat.Element[] embeddium$getExtendedElements();

    public static class Element {

        public final VertexFormatElement actual;

        public final int increment;

        public final int byteLength;

        public Element(VertexFormatElement actual, int increment, int byteLength) {
            this.actual = actual;
            this.increment = increment;
            this.byteLength = byteLength;
        }
    }
}