package com.simibubi.create.content.kinetics.base.flwdata;

import com.jozufozu.flywheel.api.struct.StructType;
import com.jozufozu.flywheel.backend.gl.buffer.VecBuffer;
import com.jozufozu.flywheel.core.materials.BasicWriterUnsafe;
import org.lwjgl.system.MemoryUtil;

public abstract class KineticWriterUnsafe<D extends KineticData> extends BasicWriterUnsafe<D> {

    public KineticWriterUnsafe(VecBuffer backingBuffer, StructType<D> vertexType) {
        super(backingBuffer, vertexType);
    }

    protected void writeInternal(D d) {
        super.writeInternal(d);
        long addr = this.writePointer;
        MemoryUtil.memPutFloat(addr + 6L, d.x);
        MemoryUtil.memPutFloat(addr + 10L, d.y);
        MemoryUtil.memPutFloat(addr + 14L, d.z);
        MemoryUtil.memPutFloat(addr + 18L, d.rotationalSpeed);
        MemoryUtil.memPutFloat(addr + 22L, d.rotationOffset);
    }
}