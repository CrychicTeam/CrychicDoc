package com.mojang.realmsclient.gui.task;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.util.TimeSource;
import org.slf4j.Logger;

public class DataFetcher {

    static final Logger LOGGER = LogUtils.getLogger();

    final Executor executor;

    final TimeUnit resolution;

    final TimeSource timeSource;

    public DataFetcher(Executor executor0, TimeUnit timeUnit1, TimeSource timeSource2) {
        this.executor = executor0;
        this.resolution = timeUnit1;
        this.timeSource = timeSource2;
    }

    public <T> DataFetcher.Task<T> createTask(String string0, Callable<T> callableT1, Duration duration2, RepeatedDelayStrategy repeatedDelayStrategy3) {
        long $$4 = this.resolution.convert(duration2);
        if ($$4 == 0L) {
            throw new IllegalArgumentException("Period of " + duration2 + " too short for selected resolution of " + this.resolution);
        } else {
            return new DataFetcher.Task<>(string0, callableT1, $$4, repeatedDelayStrategy3);
        }
    }

    public DataFetcher.Subscription createSubscription() {
        return new DataFetcher.Subscription();
    }

    static record ComputationResult<T>(Either<T, Exception> f_238822_, long f_238664_) {

        private final Either<T, Exception> value;

        private final long time;

        ComputationResult(Either<T, Exception> f_238822_, long f_238664_) {
            this.value = f_238822_;
            this.time = f_238664_;
        }
    }

    class SubscribedTask<T> {

        private final DataFetcher.Task<T> task;

        private final Consumer<T> output;

        private long lastCheckTime = -1L;

        SubscribedTask(DataFetcher.Task<T> dataFetcherTaskT0, Consumer<T> consumerT1) {
            this.task = dataFetcherTaskT0;
            this.output = consumerT1;
        }

        void update(long long0) {
            this.task.updateIfNeeded(long0);
            this.runCallbackIfNeeded();
        }

        void runCallbackIfNeeded() {
            DataFetcher.SuccessfulComputationResult<T> $$0 = this.task.lastResult;
            if ($$0 != null && this.lastCheckTime < $$0.time) {
                this.output.accept($$0.value);
                this.lastCheckTime = $$0.time;
            }
        }

        void runCallback() {
            DataFetcher.SuccessfulComputationResult<T> $$0 = this.task.lastResult;
            if ($$0 != null) {
                this.output.accept($$0.value);
                this.lastCheckTime = $$0.time;
            }
        }

        void reset() {
            this.task.reset();
            this.lastCheckTime = -1L;
        }
    }

    public class Subscription {

        private final List<DataFetcher.SubscribedTask<?>> subscriptions = new ArrayList();

        public <T> void subscribe(DataFetcher.Task<T> dataFetcherTaskT0, Consumer<T> consumerT1) {
            DataFetcher.SubscribedTask<T> $$2 = DataFetcher.this.new SubscribedTask<>(dataFetcherTaskT0, consumerT1);
            this.subscriptions.add($$2);
            $$2.runCallbackIfNeeded();
        }

        public void forceUpdate() {
            for (DataFetcher.SubscribedTask<?> $$0 : this.subscriptions) {
                $$0.runCallback();
            }
        }

        public void tick() {
            for (DataFetcher.SubscribedTask<?> $$0 : this.subscriptions) {
                $$0.update(DataFetcher.this.timeSource.get(DataFetcher.this.resolution));
            }
        }

        public void reset() {
            for (DataFetcher.SubscribedTask<?> $$0 : this.subscriptions) {
                $$0.reset();
            }
        }
    }

    static record SuccessfulComputationResult<T>(T f_238529_, long f_238539_) {

        private final T value;

        private final long time;

        SuccessfulComputationResult(T f_238529_, long f_238539_) {
            this.value = f_238529_;
            this.time = f_238539_;
        }
    }

    public class Task<T> {

        private final String id;

        private final Callable<T> updater;

        private final long period;

        private final RepeatedDelayStrategy repeatStrategy;

        @Nullable
        private CompletableFuture<DataFetcher.ComputationResult<T>> pendingTask;

        @Nullable
        DataFetcher.SuccessfulComputationResult<T> lastResult;

        private long nextUpdate = -1L;

        Task(String string0, Callable<T> callableT1, long long2, RepeatedDelayStrategy repeatedDelayStrategy3) {
            this.id = string0;
            this.updater = callableT1;
            this.period = long2;
            this.repeatStrategy = repeatedDelayStrategy3;
        }

        void updateIfNeeded(long long0) {
            if (this.pendingTask != null) {
                DataFetcher.ComputationResult<T> $$1 = (DataFetcher.ComputationResult<T>) this.pendingTask.getNow(null);
                if ($$1 == null) {
                    return;
                }
                this.pendingTask = null;
                long $$2 = $$1.time;
                $$1.value().ifLeft(p_239691_ -> {
                    this.lastResult = new DataFetcher.SuccessfulComputationResult<>((T) p_239691_, $$2);
                    this.nextUpdate = $$2 + this.period * this.repeatStrategy.delayCyclesAfterSuccess();
                }).ifRight(p_239281_ -> {
                    long $$2x = this.repeatStrategy.delayCyclesAfterFailure();
                    DataFetcher.LOGGER.warn("Failed to process task {}, will repeat after {} cycles", new Object[] { this.id, $$2x, p_239281_ });
                    this.nextUpdate = $$2 + this.period * $$2x;
                });
            }
            if (this.nextUpdate <= long0) {
                this.pendingTask = CompletableFuture.supplyAsync(() -> {
                    try {
                        T $$0 = (T) this.updater.call();
                        long $$1x = DataFetcher.this.timeSource.get(DataFetcher.this.resolution);
                        return new DataFetcher.ComputationResult(Either.left($$0), $$1x);
                    } catch (Exception var4x) {
                        long $$3 = DataFetcher.this.timeSource.get(DataFetcher.this.resolution);
                        return new DataFetcher.ComputationResult(Either.right(var4x), $$3);
                    }
                }, DataFetcher.this.executor);
            }
        }

        public void reset() {
            this.pendingTask = null;
            this.lastResult = null;
            this.nextUpdate = -1L;
        }
    }
}