package net.raphimc.immediatelyfast.injection.mixins.core;

import com.mojang.blaze3d.platform.MemoryTracker;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.nio.ByteBuffer;
import net.raphimc.immediatelyfast.injection.interfaces.IBufferBuilder;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ BufferBuilder.class })
public abstract class MixinBufferBuilder implements IBufferBuilder {

    @Shadow
    private ByteBuffer buffer;

    @Override
    public boolean immediatelyFast$isReleased() {
        return this.buffer == null;
    }

    @Override
    public void immediatelyFast$release() {
        if (!this.immediatelyFast$isReleased()) {
            MemoryTracker.ALLOCATOR.free(MemoryUtil.memAddress0(this.buffer));
            this.buffer = null;
        }
    }
}