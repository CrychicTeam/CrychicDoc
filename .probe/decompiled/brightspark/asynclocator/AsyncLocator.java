package brightspark.asynclocator;

import brightspark.asynclocator.platform.Services;
import com.mojang.datafixers.util.Pair;
import java.text.NumberFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

public class AsyncLocator {

    private static ExecutorService LOCATING_EXECUTOR_SERVICE = null;

    private AsyncLocator() {
    }

    public static void setupExecutorService() {
        shutdownExecutorService();
        int threads = Services.CONFIG.locatorThreads();
        ALConstants.logInfo("Starting locating executor service with thread pool size of {}", threads);
        LOCATING_EXECUTOR_SERVICE = Executors.newFixedThreadPool(threads, new ThreadFactory() {

            private static final AtomicInteger poolNum = new AtomicInteger(1);

            private final AtomicInteger threadNum = new AtomicInteger(1);

            private final String namePrefix = "asynclocator-" + poolNum.getAndIncrement() + "-thread-";

            public Thread newThread(@NotNull Runnable r) {
                return new Thread(r, this.namePrefix + this.threadNum.getAndIncrement());
            }
        });
    }

    public static void shutdownExecutorService() {
        if (LOCATING_EXECUTOR_SERVICE != null) {
            ALConstants.logInfo("Shutting down locating executor service");
            LOCATING_EXECUTOR_SERVICE.shutdown();
        }
    }

    public static AsyncLocator.LocateTask<BlockPos> locate(ServerLevel level, TagKey<Structure> structureTag, BlockPos pos, int searchRadius, boolean skipKnownStructures) {
        ALConstants.logDebug("Creating locate task for {} in {} around {} within {} chunks", structureTag, level, pos, searchRadius);
        CompletableFuture<BlockPos> completableFuture = new CompletableFuture();
        Future<?> future = LOCATING_EXECUTOR_SERVICE.submit(() -> doLocateLevel(completableFuture, level, structureTag, pos, searchRadius, skipKnownStructures));
        return new AsyncLocator.LocateTask<>(level.getServer(), completableFuture, future);
    }

    public static AsyncLocator.LocateTask<Pair<BlockPos, Holder<Structure>>> locate(ServerLevel level, HolderSet<Structure> structureSet, BlockPos pos, int searchRadius, boolean skipKnownStructures) {
        ALConstants.logDebug("Creating locate task for {} in {} around {} within {} chunks", structureSet, level, pos, searchRadius);
        CompletableFuture<Pair<BlockPos, Holder<Structure>>> completableFuture = new CompletableFuture();
        Future<?> future = LOCATING_EXECUTOR_SERVICE.submit(() -> doLocateChunkGenerator(completableFuture, level, structureSet, pos, searchRadius, skipKnownStructures));
        return new AsyncLocator.LocateTask<>(level.getServer(), completableFuture, future);
    }

    private static void doLocateLevel(CompletableFuture<BlockPos> completableFuture, ServerLevel level, TagKey<Structure> structureTag, BlockPos pos, int searchRadius, boolean skipExistingChunks) {
        ALConstants.logInfo("Trying to locate {} in {} around {} within {} chunks", structureTag, level, pos, searchRadius);
        long start = System.nanoTime();
        BlockPos foundPos = level.findNearestMapStructure(structureTag, pos, searchRadius, skipExistingChunks);
        String time = NumberFormat.getNumberInstance().format(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        if (foundPos == null) {
            ALConstants.logInfo("No {} found (took {}ms)", structureTag, time);
        } else {
            ALConstants.logInfo("Found {} at {} (took {}ms)", structureTag, foundPos, time);
        }
        completableFuture.complete(foundPos);
    }

    private static void doLocateChunkGenerator(CompletableFuture<Pair<BlockPos, Holder<Structure>>> completableFuture, ServerLevel level, HolderSet<Structure> structureSet, BlockPos pos, int searchRadius, boolean skipExistingChunks) {
        ALConstants.logInfo("Trying to locate {} in {} around {} within {} chunks", structureSet, level, pos, searchRadius);
        long start = System.nanoTime();
        Pair<BlockPos, Holder<Structure>> foundPair = level.getChunkSource().getGenerator().findNearestMapStructure(level, structureSet, pos, searchRadius, skipExistingChunks);
        String time = NumberFormat.getNumberInstance().format(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        if (foundPair == null) {
            ALConstants.logInfo("No {} found (took {}ms)", structureSet, time);
        } else {
            ALConstants.logInfo("Found {} at {} (took {}ms)", ((Structure) ((Holder) foundPair.getSecond()).value()).getClass().getSimpleName(), foundPair.getFirst(), time);
        }
        completableFuture.complete(foundPair);
    }

    public static record LocateTask<T>(MinecraftServer server, CompletableFuture<T> completableFuture, Future<?> taskFuture) {

        public AsyncLocator.LocateTask<T> then(Consumer<T> action) {
            this.completableFuture.thenAccept(action);
            return this;
        }

        public AsyncLocator.LocateTask<T> thenOnServerThread(Consumer<T> action) {
            this.completableFuture.thenAccept(pos -> this.server.m_18707_(() -> action.accept(pos)));
            return this;
        }

        public void cancel() {
            this.taskFuture.cancel(true);
            this.completableFuture.cancel(false);
        }
    }
}