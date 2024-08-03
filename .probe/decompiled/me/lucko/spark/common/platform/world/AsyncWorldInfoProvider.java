package me.lucko.spark.common.platform.world;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.logging.Level;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.SparkPlugin;

public class AsyncWorldInfoProvider {

    private static final int TIMEOUT_SECONDS = 5;

    private final SparkPlatform platform;

    private final WorldInfoProvider provider;

    public AsyncWorldInfoProvider(SparkPlatform platform, WorldInfoProvider provider) {
        this.platform = platform;
        this.provider = provider == WorldInfoProvider.NO_OP ? null : provider;
    }

    private <T> CompletableFuture<T> async(Function<WorldInfoProvider, T> function) {
        if (this.provider == null) {
            return null;
        } else if (this.provider.mustCallSync()) {
            SparkPlugin plugin = this.platform.getPlugin();
            return CompletableFuture.supplyAsync(() -> function.apply(this.provider), plugin::executeSync);
        } else {
            return CompletableFuture.completedFuture(function.apply(this.provider));
        }
    }

    private <T> T get(CompletableFuture<T> future) {
        if (future == null) {
            return null;
        } else {
            try {
                return (T) future.get(5L, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException var3) {
                throw new RuntimeException(var3);
            } catch (TimeoutException var4) {
                this.platform.getPlugin().log(Level.WARNING, "Timed out waiting for world statistics");
                return null;
            }
        }
    }

    public CompletableFuture<WorldInfoProvider.CountsResult> pollCounts() {
        return this.async(WorldInfoProvider::pollCounts);
    }

    public CompletableFuture<WorldInfoProvider.ChunksResult<? extends ChunkInfo<?>>> pollChunks() {
        return this.async(WorldInfoProvider::pollChunks);
    }

    public WorldInfoProvider.CountsResult getCounts() {
        return this.get(this.pollCounts());
    }

    public WorldInfoProvider.ChunksResult<? extends ChunkInfo<?>> getChunks() {
        return this.get(this.pollChunks());
    }
}