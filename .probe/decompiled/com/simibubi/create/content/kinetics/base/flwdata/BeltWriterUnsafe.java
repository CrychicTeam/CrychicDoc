package com.simibubi.create.content.kinetics.base.flwdata;

import com.jozufozu.flywheel.api.struct.StructType;
import com.jozufozu.flywheel.backend.gl.buffer.VecBuffer;
import org.lwjgl.system.MemoryUtil;

public class BeltWriterUnsafe extends KineticWriterUnsafe<BeltData> {

    public BeltWriterUnsafe(VecBuffer backingBuffer, StructType<BeltData> vertexType) {
        super(backingBuffer, vertexType);
    }

    protected void writeInternal(BeltData d) {
        super.writeInternal(d);
        long addr = this.writePointer;
        MemoryUtil.memPutFloat(addr + 26L, d.qX);
        MemoryUtil.memPutFloat(addr + 30L, d.qY);
        MemoryUtil.memPutFloat(addr + 34L, d.qZ);
        MemoryUtil.memPutFloat(addr + 38L, d.qW);
        MemoryUtil.memPutFloat(addr + 42L, d.sourceU);
        MemoryUtil.memPutFloat(addr + 46L, d.sourceV);
        MemoryUtil.memPutFloat(addr + 50L, d.minU);
        MemoryUtil.memPutFloat(addr + 54L, d.minV);
        MemoryUtil.memPutFloat(addr + 58L, d.maxU);
        MemoryUtil.memPutFloat(addr + 62L, d.maxV);
        MemoryUtil.memPutByte(addr + 66L, d.scrollMult);
    }
}