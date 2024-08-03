package com.simibubi.create.foundation.render;

import com.jozufozu.flywheel.core.layout.BufferLayout;
import com.jozufozu.flywheel.core.layout.CommonItems;
import com.jozufozu.flywheel.core.layout.LayoutItem;
import com.jozufozu.flywheel.core.layout.BufferLayout.Builder;

public class AllInstanceFormats {

    public static final BufferLayout ROTATING = kineticInstance().addItems(new LayoutItem[] { CommonItems.NORMAL }).build();

    public static final BufferLayout BELT = kineticInstance().addItems(new LayoutItem[] { CommonItems.QUATERNION, CommonItems.UV, CommonItems.VEC4, CommonItems.NORMALIZED_BYTE }).build();

    public static final BufferLayout ACTOR = BufferLayout.builder().addItems(new LayoutItem[] { CommonItems.VEC3, CommonItems.LIGHT, CommonItems.FLOAT, CommonItems.NORMAL, CommonItems.QUATERNION, CommonItems.NORMAL, CommonItems.FLOAT }).build();

    public static final BufferLayout FLAP = BufferLayout.builder().addItems(new LayoutItem[] { CommonItems.VEC3, CommonItems.LIGHT, CommonItems.VEC3, CommonItems.VEC3, CommonItems.FLOAT, CommonItems.FLOAT, CommonItems.FLOAT, CommonItems.FLOAT }).build();

    private static Builder kineticInstance() {
        return BufferLayout.builder().addItems(new LayoutItem[] { CommonItems.LIGHT, CommonItems.RGBA }).addItems(new LayoutItem[] { CommonItems.VEC3, CommonItems.FLOAT, CommonItems.FLOAT });
    }
}