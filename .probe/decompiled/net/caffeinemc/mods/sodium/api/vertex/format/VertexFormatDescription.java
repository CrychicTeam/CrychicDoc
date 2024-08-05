package net.caffeinemc.mods.sodium.api.vertex.format;

import net.caffeinemc.mods.sodium.api.vertex.attributes.CommonVertexAttribute;

public interface VertexFormatDescription {

    boolean containsElement(CommonVertexAttribute var1);

    int getElementOffset(CommonVertexAttribute var1);

    int id();

    int stride();

    boolean isSimpleFormat();
}