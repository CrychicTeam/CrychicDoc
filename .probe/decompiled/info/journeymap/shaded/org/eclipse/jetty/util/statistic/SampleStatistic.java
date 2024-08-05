package info.journeymap.shaded.org.eclipse.jetty.util.statistic;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

public class SampleStatistic {

    protected final LongAccumulator _max = new LongAccumulator(Math::max, 0L);

    protected final AtomicLong _total = new AtomicLong();

    protected final AtomicLong _count = new AtomicLong();

    protected final LongAdder _totalVariance100 = new LongAdder();

    public void reset() {
        this._max.reset();
        this._total.set(0L);
        this._count.set(0L);
        this._totalVariance100.reset();
    }

    public void set(long sample) {
        long total = this._total.addAndGet(sample);
        long count = this._count.incrementAndGet();
        if (count > 1L) {
            long mean10 = total * 10L / count;
            long delta10 = sample * 10L - mean10;
            this._totalVariance100.add(delta10 * delta10);
        }
        this._max.accumulate(sample);
    }

    public long getMax() {
        return this._max.get();
    }

    public long getTotal() {
        return this._total.get();
    }

    public long getCount() {
        return this._count.get();
    }

    public double getMean() {
        return (double) this._total.get() / (double) this._count.get();
    }

    public double getVariance() {
        long variance100 = this._totalVariance100.sum();
        long count = this._count.get();
        return count > 1L ? (double) variance100 / 100.0 / (double) (count - 1L) : 0.0;
    }

    public double getStdDev() {
        return Math.sqrt(this.getVariance());
    }

    public String toString() {
        return String.format("%s@%x{c=%d,m=%d,t=%d,v100=%d}", this.getClass().getSimpleName(), this.hashCode(), this._count.get(), this._max.get(), this._total.get(), this._totalVariance100.sum());
    }
}