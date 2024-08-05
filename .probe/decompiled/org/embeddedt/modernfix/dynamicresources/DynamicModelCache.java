package org.embeddedt.modernfix.dynamicresources;

import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceLinkedOpenHashMap;
import java.util.concurrent.locks.StampedLock;
import net.minecraft.client.resources.model.BakedModel;

public class DynamicModelCache<K> {

    private final Reference2ReferenceLinkedOpenHashMap<K, BakedModel> cache = new Reference2ReferenceLinkedOpenHashMap();

    private final StampedLock lock = new StampedLock();

    private final Function<K, BakedModel> modelRetriever;

    private final boolean allowNulls;

    public DynamicModelCache(Function<K, BakedModel> modelRetriever, boolean allowNulls) {
        this.modelRetriever = modelRetriever;
        this.allowNulls = allowNulls;
    }

    public void clear() {
        long stamp = this.lock.writeLock();
        try {
            this.cache.clear();
        } finally {
            this.lock.unlock(stamp);
        }
    }

    private boolean needToPopulate(K state) {
        long stamp = this.lock.readLock();
        boolean var4;
        try {
            var4 = !this.cache.containsKey(state);
        } finally {
            this.lock.unlock(stamp);
        }
        return var4;
    }

    private BakedModel getModelFromCache(K state) {
        long stamp = this.lock.readLock();
        BakedModel var4;
        try {
            var4 = (BakedModel) this.cache.get(state);
        } finally {
            this.lock.unlock(stamp);
        }
        return var4;
    }

    private BakedModel cacheModel(K state) {
        BakedModel model = (BakedModel) this.modelRetriever.apply(state);
        long stamp = this.lock.writeLock();
        try {
            this.cache.putAndMoveToFirst(state, model);
            if (this.cache.size() >= 1000) {
                this.cache.removeLast();
            }
        } finally {
            this.lock.unlock(stamp);
        }
        return model;
    }

    public BakedModel get(K key) {
        BakedModel model = this.getModelFromCache(key);
        if (model == null && (!this.allowNulls || this.needToPopulate(key))) {
            model = this.cacheModel(key);
        }
        return model;
    }
}