package net.raphimc.immediatelyfast.feature.core;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import java.util.Set;
import net.raphimc.immediatelyfast.injection.interfaces.IBufferBuilder;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class BufferBuilderPool {

    private static final int INITIAL_SIZE = 256;

    private static final Set<Pair<BufferBuilder, Long>> POOL = new ReferenceArraySet(256);

    private static long lastCleanup = 0L;

    private BufferBuilderPool() {
    }

    public static BufferBuilder get() {
        RenderSystem.assertOnRenderThread();
        if (lastCleanup < System.currentTimeMillis() - 5000L) {
            lastCleanup = System.currentTimeMillis();
            cleanup();
        }
        for (Pair<BufferBuilder, Long> entry : POOL) {
            BufferBuilder bufferBuilder = (BufferBuilder) entry.getKey();
            if (!bufferBuilder.building() && !((IBufferBuilder) bufferBuilder).immediatelyFast$isReleased()) {
                entry.setValue(System.currentTimeMillis());
                return bufferBuilder;
            }
        }
        BufferBuilder bufferBuilder = new BufferBuilder(256);
        POOL.add(new MutablePair(bufferBuilder, System.currentTimeMillis()));
        return bufferBuilder;
    }

    public static int getAllocatedSize() {
        cleanup();
        return POOL.size();
    }

    private static void cleanup() {
        POOL.removeIf(b -> ((IBufferBuilder) b.getKey()).immediatelyFast$isReleased());
        POOL.removeIf(b -> {
            if ((Long) b.getValue() < System.currentTimeMillis() - 120000L && !((BufferBuilder) b.getKey()).building()) {
                ((IBufferBuilder) b.getKey()).immediatelyFast$release();
                return true;
            } else {
                return false;
            }
        });
    }
}