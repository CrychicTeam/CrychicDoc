package software.bernie.geckolib.cache;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public final class AnimatableIdCache extends SavedData {

    private static final String DATA_KEY = "geckolib_id_cache";

    private long lastId;

    private AnimatableIdCache() {
        this(new CompoundTag());
    }

    private AnimatableIdCache(CompoundTag tag) {
        this.lastId = tag.getLong("last_id");
    }

    public static long getFreeId(ServerLevel level) {
        return getCache(level).getNextId();
    }

    private long getNextId() {
        this.m_77762_();
        return ++this.lastId;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putLong("last_id", this.lastId);
        return tag;
    }

    private static AnimatableIdCache getCache(ServerLevel level) {
        DimensionDataStorage storage = level.getServer().overworld().getDataStorage();
        AnimatableIdCache cache = storage.computeIfAbsent(AnimatableIdCache::new, AnimatableIdCache::new, "geckolib_id_cache");
        if (cache.lastId == 0L) {
            AnimatableIdCache legacyCache = storage.get(AnimatableIdCache::fromLegacy, "geckolib_ids");
            if (legacyCache != null) {
                cache.lastId = legacyCache.lastId;
            }
        }
        return cache;
    }

    private static AnimatableIdCache fromLegacy(CompoundTag tag) {
        AnimatableIdCache legacyCache = new AnimatableIdCache();
        for (String key : tag.getAllKeys()) {
            if (tag.contains(key, 99)) {
                legacyCache.lastId = Math.max(legacyCache.lastId, (long) tag.getInt(key));
            }
        }
        return legacyCache;
    }
}