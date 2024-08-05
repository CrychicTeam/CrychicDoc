package com.simibubi.create.content.kinetics.base.flwdata;

import com.jozufozu.flywheel.api.struct.StructType;
import com.jozufozu.flywheel.backend.gl.buffer.VecBuffer;
import org.lwjgl.system.MemoryUtil;

public class RotatingWriterUnsafe extends KineticWriterUnsafe<RotatingData> {

    public RotatingWriterUnsafe(VecBuffer backingBuffer, StructType<RotatingData> vertexType) {
        super(backingBuffer, vertexType);
    }

    protected void writeInternal(RotatingData d) {
        super.writeInternal(d);
        long addr = this.writePointer;
        MemoryUtil.memPutByte(addr + 26L, d.rotationAxisX);
        MemoryUtil.memPutByte(addr + 27L, d.rotationAxisY);
        MemoryUtil.memPutByte(addr + 28L, d.rotationAxisZ);
    }
}