package net.minecraft.world.level.levelgen.feature;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;

public class FeatureCountTracker {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final LoadingCache<ServerLevel, FeatureCountTracker.LevelData> data = CacheBuilder.newBuilder().weakKeys().expireAfterAccess(5L, TimeUnit.MINUTES).build(new CacheLoader<ServerLevel, FeatureCountTracker.LevelData>() {

        public FeatureCountTracker.LevelData load(ServerLevel p_190902_) {
            return new FeatureCountTracker.LevelData(Object2IntMaps.synchronize(new Object2IntOpenHashMap()), new MutableInt(0));
        }
    });

    public static void chunkDecorated(ServerLevel serverLevel0) {
        try {
            ((FeatureCountTracker.LevelData) data.get(serverLevel0)).chunksWithFeatures().increment();
        } catch (Exception var2) {
            LOGGER.error("Failed to increment chunk count", var2);
        }
    }

    public static void featurePlaced(ServerLevel serverLevel0, ConfiguredFeature<?, ?> configuredFeature1, Optional<PlacedFeature> optionalPlacedFeature2) {
        try {
            ((FeatureCountTracker.LevelData) data.get(serverLevel0)).featureData().computeInt(new FeatureCountTracker.FeatureData(configuredFeature1, optionalPlacedFeature2), (p_190891_, p_190892_) -> p_190892_ == null ? 1 : p_190892_ + 1);
        } catch (Exception var4) {
            LOGGER.error("Failed to increment feature count", var4);
        }
    }

    public static void clearCounts() {
        data.invalidateAll();
        LOGGER.debug("Cleared feature counts");
    }

    public static void logCounts() {
        LOGGER.debug("Logging feature counts:");
        data.asMap().forEach((p_190888_, p_190889_) -> {
            String $$2 = p_190888_.m_46472_().location().toString();
            boolean $$3 = p_190888_.getServer().isRunning();
            Registry<PlacedFeature> $$4 = p_190888_.m_9598_().registryOrThrow(Registries.PLACED_FEATURE);
            String $$5 = ($$3 ? "running" : "dead") + " " + $$2;
            Integer $$6 = p_190889_.chunksWithFeatures().getValue();
            LOGGER.debug($$5 + " total_chunks: " + $$6);
            p_190889_.featureData().forEach((p_190897_, p_190898_) -> LOGGER.debug($$5 + " " + String.format(Locale.ROOT, "%10d ", p_190898_) + String.format(Locale.ROOT, "%10f ", (double) p_190898_.intValue() / (double) $$6.intValue()) + p_190897_.topFeature().flatMap($$4::m_7854_).map(ResourceKey::m_135782_) + " " + p_190897_.feature().feature() + " " + p_190897_.feature()));
        });
    }

    static record FeatureData(ConfiguredFeature<?, ?> f_190905_, Optional<PlacedFeature> f_190906_) {

        private final ConfiguredFeature<?, ?> feature;

        private final Optional<PlacedFeature> topFeature;

        FeatureData(ConfiguredFeature<?, ?> f_190905_, Optional<PlacedFeature> f_190906_) {
            this.feature = f_190905_;
            this.topFeature = f_190906_;
        }
    }

    static record LevelData(Object2IntMap<FeatureCountTracker.FeatureData> f_190916_, MutableInt f_190917_) {

        private final Object2IntMap<FeatureCountTracker.FeatureData> featureData;

        private final MutableInt chunksWithFeatures;

        LevelData(Object2IntMap<FeatureCountTracker.FeatureData> f_190916_, MutableInt f_190917_) {
            this.featureData = f_190916_;
            this.chunksWithFeatures = f_190917_;
        }
    }
}