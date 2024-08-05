package com.simibubi.create.content.logistics.flwdata;

import com.jozufozu.flywheel.api.struct.StructType;
import com.jozufozu.flywheel.backend.gl.buffer.VecBuffer;
import com.jozufozu.flywheel.backend.struct.UnsafeBufferWriter;
import org.lwjgl.system.MemoryUtil;

public class UnsafeFlapWriter extends UnsafeBufferWriter<FlapData> {

    public UnsafeFlapWriter(VecBuffer backingBuffer, StructType<FlapData> vertexType) {
        super(backingBuffer, vertexType);
    }

    protected void writeInternal(FlapData d) {
        long addr = this.writePointer;
        MemoryUtil.memPutFloat(addr, d.x);
        MemoryUtil.memPutFloat(addr + 4L, d.y);
        MemoryUtil.memPutFloat(addr + 8L, d.z);
        MemoryUtil.memPutByte(addr + 12L, (byte) (d.blockLight << 4));
        MemoryUtil.memPutByte(addr + 13L, (byte) (d.skyLight << 4));
        MemoryUtil.memPutFloat(addr + 14L, d.segmentOffsetX);
        MemoryUtil.memPutFloat(addr + 18L, d.segmentOffsetY);
        MemoryUtil.memPutFloat(addr + 22L, d.segmentOffsetZ);
        MemoryUtil.memPutFloat(addr + 26L, d.pivotX);
        MemoryUtil.memPutFloat(addr + 30L, d.pivotY);
        MemoryUtil.memPutFloat(addr + 34L, d.pivotZ);
        MemoryUtil.memPutFloat(addr + 38L, d.horizontalAngle);
        MemoryUtil.memPutFloat(addr + 42L, d.intensity);
        MemoryUtil.memPutFloat(addr + 46L, d.flapScale);
        MemoryUtil.memPutFloat(addr + 50L, d.flapness);
    }
}