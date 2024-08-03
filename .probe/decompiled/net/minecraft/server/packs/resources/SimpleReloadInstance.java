package net.minecraft.server.packs.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.Util;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.InactiveProfiler;

public class SimpleReloadInstance<S> implements ReloadInstance {

    private static final int PREPARATION_PROGRESS_WEIGHT = 2;

    private static final int EXTRA_RELOAD_PROGRESS_WEIGHT = 2;

    private static final int LISTENER_PROGRESS_WEIGHT = 1;

    protected final CompletableFuture<Unit> allPreparations = new CompletableFuture();

    protected CompletableFuture<List<S>> allDone;

    final Set<PreparableReloadListener> preparingListeners;

    private final int listenerCount;

    private int startedReloads;

    private int finishedReloads;

    private final AtomicInteger startedTaskCounter = new AtomicInteger();

    private final AtomicInteger doneTaskCounter = new AtomicInteger();

    public static SimpleReloadInstance<Void> of(ResourceManager resourceManager0, List<PreparableReloadListener> listPreparableReloadListener1, Executor executor2, Executor executor3, CompletableFuture<Unit> completableFutureUnit4) {
        return new SimpleReloadInstance<>(executor2, executor3, resourceManager0, listPreparableReloadListener1, (p_10829_, p_10830_, p_10831_, p_10832_, p_10833_) -> p_10831_.reload(p_10829_, p_10830_, InactiveProfiler.INSTANCE, InactiveProfiler.INSTANCE, executor2, p_10833_), completableFutureUnit4);
    }

    protected SimpleReloadInstance(Executor executor0, final Executor executor1, ResourceManager resourceManager2, List<PreparableReloadListener> listPreparableReloadListener3, SimpleReloadInstance.StateFactory<S> simpleReloadInstanceStateFactoryS4, CompletableFuture<Unit> completableFutureUnit5) {
        this.listenerCount = listPreparableReloadListener3.size();
        this.startedTaskCounter.incrementAndGet();
        completableFutureUnit5.thenRun(this.doneTaskCounter::incrementAndGet);
        List<CompletableFuture<S>> $$6 = Lists.newArrayList();
        CompletableFuture<?> $$7 = completableFutureUnit5;
        this.preparingListeners = Sets.newHashSet(listPreparableReloadListener3);
        for (final PreparableReloadListener $$8 : listPreparableReloadListener3) {
            final CompletableFuture<?> $$9 = $$7;
            CompletableFuture<S> $$10 = simpleReloadInstanceStateFactoryS4.create(new PreparableReloadListener.PreparationBarrier() {

                @Override
                public <T> CompletableFuture<T> wait(T p_10858_) {
                    executor1.execute(() -> {
                        SimpleReloadInstance.this.preparingListeners.remove($$8);
                        if (SimpleReloadInstance.this.preparingListeners.isEmpty()) {
                            SimpleReloadInstance.this.allPreparations.complete(Unit.INSTANCE);
                        }
                    });
                    return SimpleReloadInstance.this.allPreparations.thenCombine($$9, (p_10861_, p_10862_) -> p_10858_);
                }
            }, resourceManager2, $$8, p_10842_ -> {
                this.startedTaskCounter.incrementAndGet();
                executor0.execute(() -> {
                    p_10842_.run();
                    this.doneTaskCounter.incrementAndGet();
                });
            }, p_10836_ -> {
                this.startedReloads++;
                executor1.execute(() -> {
                    p_10836_.run();
                    this.finishedReloads++;
                });
            });
            $$6.add($$10);
            $$7 = $$10;
        }
        this.allDone = Util.sequenceFailFast($$6);
    }

    @Override
    public CompletableFuture<?> done() {
        return this.allDone;
    }

    @Override
    public float getActualProgress() {
        int $$0 = this.listenerCount - this.preparingListeners.size();
        float $$1 = (float) (this.doneTaskCounter.get() * 2 + this.finishedReloads * 2 + $$0 * 1);
        float $$2 = (float) (this.startedTaskCounter.get() * 2 + this.startedReloads * 2 + this.listenerCount * 1);
        return $$1 / $$2;
    }

    public static ReloadInstance create(ResourceManager resourceManager0, List<PreparableReloadListener> listPreparableReloadListener1, Executor executor2, Executor executor3, CompletableFuture<Unit> completableFutureUnit4, boolean boolean5) {
        return (ReloadInstance) (boolean5 ? new ProfiledReloadInstance(resourceManager0, listPreparableReloadListener1, executor2, executor3, completableFutureUnit4) : of(resourceManager0, listPreparableReloadListener1, executor2, executor3, completableFutureUnit4));
    }

    protected interface StateFactory<S> {

        CompletableFuture<S> create(PreparableReloadListener.PreparationBarrier var1, ResourceManager var2, PreparableReloadListener var3, Executor var4, Executor var5);
    }
}