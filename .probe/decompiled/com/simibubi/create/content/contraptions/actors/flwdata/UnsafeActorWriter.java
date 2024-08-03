package com.simibubi.create.content.contraptions.actors.flwdata;

import com.jozufozu.flywheel.api.struct.StructType;
import com.jozufozu.flywheel.backend.gl.buffer.VecBuffer;
import com.jozufozu.flywheel.backend.struct.UnsafeBufferWriter;
import org.lwjgl.system.MemoryUtil;

public class UnsafeActorWriter extends UnsafeBufferWriter<ActorData> {

    public UnsafeActorWriter(VecBuffer backingBuffer, StructType<ActorData> vertexType) {
        super(backingBuffer, vertexType);
    }

    protected void writeInternal(ActorData d) {
        long addr = this.writePointer;
        MemoryUtil.memPutFloat(addr, d.x);
        MemoryUtil.memPutFloat(addr + 4L, d.y);
        MemoryUtil.memPutFloat(addr + 8L, d.z);
        MemoryUtil.memPutByte(addr + 12L, d.blockLight);
        MemoryUtil.memPutByte(addr + 13L, d.skyLight);
        MemoryUtil.memPutFloat(addr + 14L, d.rotationOffset);
        MemoryUtil.memPutByte(addr + 18L, d.rotationAxisX);
        MemoryUtil.memPutByte(addr + 19L, d.rotationAxisY);
        MemoryUtil.memPutByte(addr + 20L, d.rotationAxisZ);
        MemoryUtil.memPutFloat(addr + 21L, d.qX);
        MemoryUtil.memPutFloat(addr + 25L, d.qY);
        MemoryUtil.memPutFloat(addr + 29L, d.qZ);
        MemoryUtil.memPutFloat(addr + 33L, d.qW);
        MemoryUtil.memPutByte(addr + 37L, d.rotationCenterX);
        MemoryUtil.memPutByte(addr + 38L, d.rotationCenterY);
        MemoryUtil.memPutByte(addr + 39L, d.rotationCenterZ);
        MemoryUtil.memPutFloat(addr + 40L, d.speed);
    }
}