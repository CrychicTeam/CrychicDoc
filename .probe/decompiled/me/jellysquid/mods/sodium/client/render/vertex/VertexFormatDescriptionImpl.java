package me.jellysquid.mods.sodium.client.render.vertex;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import me.jellysquid.mods.sodium.mixin.core.render.VertexFormatAccessor;
import net.caffeinemc.mods.sodium.api.vertex.attributes.CommonVertexAttribute;

public class VertexFormatDescriptionImpl implements net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription {

    @Deprecated
    private final VertexFormat format;

    private final int id;

    private final int stride;

    private final int[] offsets;

    private final boolean isSimple;

    public VertexFormatDescriptionImpl(VertexFormat format, int id) {
        this.format = format;
        this.id = id;
        this.stride = format.getVertexSize();
        this.offsets = getOffsets(format);
        this.isSimple = checkSimple(format);
    }

    private static boolean checkSimple(VertexFormat format) {
        EnumSet<CommonVertexAttribute> attributeSet = EnumSet.noneOf(CommonVertexAttribute.class);
        ImmutableList<VertexFormatElement> elementList = format.getElements();
        for (int elementIndex = 0; elementIndex < elementList.size(); elementIndex++) {
            VertexFormatElement element = (VertexFormatElement) elementList.get(elementIndex);
            CommonVertexAttribute commonType = CommonVertexAttribute.getCommonType(element);
            if (element != DefaultVertexFormat.ELEMENT_PADDING && (commonType == null || !attributeSet.add(commonType))) {
                return false;
            }
        }
        return true;
    }

    public static int[] getOffsets(VertexFormat format) {
        int[] commonElementOffsets = new int[CommonVertexAttribute.COUNT];
        Arrays.fill(commonElementOffsets, -1);
        ImmutableList<VertexFormatElement> elementList = format.getElements();
        IntList elementOffsets = ((VertexFormatAccessor) format).getOffsets();
        for (int elementIndex = 0; elementIndex < elementList.size(); elementIndex++) {
            VertexFormatElement element = (VertexFormatElement) elementList.get(elementIndex);
            CommonVertexAttribute commonType = CommonVertexAttribute.getCommonType(element);
            if (commonType != null) {
                commonElementOffsets[commonType.ordinal()] = elementOffsets.getInt(elementIndex);
            }
        }
        return commonElementOffsets;
    }

    @Override
    public boolean containsElement(CommonVertexAttribute element) {
        return this.offsets[element.ordinal()] != -1;
    }

    @Override
    public int getElementOffset(CommonVertexAttribute element) {
        int offset = this.offsets[element.ordinal()];
        if (offset == -1) {
            throw new NoSuchElementException("Vertex format does not contain element: " + element);
        } else {
            return offset;
        }
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public int stride() {
        return this.stride;
    }

    @Deprecated
    public VertexFormat format() {
        return this.format;
    }

    @Override
    public boolean isSimpleFormat() {
        return this.isSimple;
    }
}