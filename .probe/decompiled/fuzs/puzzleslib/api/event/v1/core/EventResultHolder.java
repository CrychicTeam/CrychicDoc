package fuzs.puzzleslib.api.event.v1.core;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

public final class EventResultHolder<T> {

    private static final EventResultHolder<?> PASS = new EventResultHolder();

    @NotNull
    private final EventResult result;

    @NotNull
    private final T value;

    private EventResultHolder() {
        this.result = EventResult.PASS;
        this.value = null;
    }

    private EventResultHolder(@NotNull EventResult result, @NotNull T value) {
        Objects.requireNonNull(result, "result is null");
        Objects.requireNonNull(value, "value is null");
        this.result = result;
        this.value = value;
    }

    public static <T> EventResultHolder<T> pass() {
        return (EventResultHolder<T>) PASS;
    }

    public static <T> EventResultHolder<T> interrupt(@NotNull T value) {
        return new EventResultHolder<>(EventResult.INTERRUPT, value);
    }

    public static <T> EventResultHolder<T> allow(@NotNull T value) {
        return new EventResultHolder<>(EventResult.ALLOW, value);
    }

    public static <T> EventResultHolder<T> deny(@NotNull T value) {
        return new EventResultHolder<>(EventResult.DENY, value);
    }

    public boolean isInterrupt() {
        return this.result.isInterrupt();
    }

    public boolean isPass() {
        return this.result.isPass();
    }

    public EventResultHolder<T> ifInterrupt(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (this.isInterrupt() && this.result.getAsBoolean()) {
            action.accept(this.value);
        }
        return this;
    }

    public EventResultHolder<T> ifAllow(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (this.isInterrupt() && this.result.getAsBoolean()) {
            action.accept(this.value);
        }
        return this;
    }

    public EventResultHolder<T> ifDeny(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (this.isInterrupt() && !this.result.getAsBoolean()) {
            action.accept(this.value);
        }
        return this;
    }

    public EventResultHolder<T> filter(Predicate<? super T> filter) {
        Objects.requireNonNull(filter, "filter is null");
        if (!this.isInterrupt()) {
            return this;
        } else {
            return filter.test(this.value) ? this : pass();
        }
    }

    public <U> EventResultHolder<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return !this.isInterrupt() ? pass() : new EventResultHolder<>(this.result, mapper.apply(this.value));
    }

    public <U> EventResultHolder<U> flatMap(Function<? super T, ? extends EventResultHolder<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (!this.isInterrupt()) {
            return pass();
        } else {
            EventResultHolder<U> holder = (EventResultHolder<U>) mapper.apply(this.value);
            Objects.requireNonNull(holder, "holder is null");
            return holder;
        }
    }

    public Optional<T> getInterrupt() {
        return this.isInterrupt() && this.result.getAsBoolean() ? Optional.of(this.value) : Optional.empty();
    }

    public Optional<T> getAllow() {
        return this.isInterrupt() && this.result.getAsBoolean() ? Optional.of(this.value) : Optional.empty();
    }

    public Optional<T> getDeny() {
        return this.isInterrupt() && !this.result.getAsBoolean() ? Optional.of(this.value) : Optional.empty();
    }
}