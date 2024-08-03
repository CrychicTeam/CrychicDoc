package info.journeymap.shaded.org.eclipse.jetty.util.statistic;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

public class CounterStatistic {

    protected final LongAccumulator _max = new LongAccumulator(Math::max, 0L);

    protected final AtomicLong _current = new AtomicLong();

    protected final LongAdder _total = new LongAdder();

    public void reset() {
        this._total.reset();
        this._max.reset();
        long current = this._current.get();
        this._total.add(current);
        this._max.accumulate(current);
    }

    public void reset(long value) {
        this._current.set(value);
        this._total.reset();
        this._max.reset();
        if (value > 0L) {
            this._total.add(value);
            this._max.accumulate(value);
        }
    }

    public long add(long delta) {
        long value = this._current.addAndGet(delta);
        if (delta > 0L) {
            this._total.add(delta);
            this._max.accumulate(value);
        }
        return value;
    }

    public long increment() {
        long value = this._current.incrementAndGet();
        this._total.increment();
        this._max.accumulate(value);
        return value;
    }

    public long decrement() {
        return this._current.decrementAndGet();
    }

    public long getMax() {
        return this._max.get();
    }

    public long getCurrent() {
        return this._current.get();
    }

    public long getTotal() {
        return this._total.sum();
    }

    public String toString() {
        return String.format("%s@%x{c=%d,m=%d,t=%d}", this.getClass().getSimpleName(), this.hashCode(), this._current.get(), this._max.get(), this._total.sum());
    }
}