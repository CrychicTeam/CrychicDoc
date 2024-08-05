package me.jellysquid.mods.sodium.client.render.vertex;

import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public class VertexFormatRegistryImpl implements net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatRegistry {

    private final Map<VertexFormat, VertexFormatDescriptionImpl> descriptions = new Reference2ReferenceOpenHashMap();

    private final StampedLock lock = new StampedLock();

    @Override
    public net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription get(VertexFormat format) {
        net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription desc = this.findExisting(format);
        if (desc == null) {
            desc = this.create(format);
        }
        return desc;
    }

    private net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription findExisting(VertexFormat format) {
        long stamp = this.lock.readLock();
        net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription var4;
        try {
            var4 = (net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription) this.descriptions.get(format);
        } finally {
            this.lock.unlockRead(stamp);
        }
        return var4;
    }

    private net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription create(VertexFormat format) {
        long stamp = this.lock.writeLock();
        int id = this.descriptions.size();
        VertexFormatDescriptionImpl desc = new VertexFormatDescriptionImpl(format, id);
        try {
            this.descriptions.put(format, desc);
        } finally {
            this.lock.unlockWrite(stamp);
        }
        return desc;
    }
}