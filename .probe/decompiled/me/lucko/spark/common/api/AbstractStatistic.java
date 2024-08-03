package me.lucko.spark.common.api;

import java.lang.reflect.Array;
import java.util.Arrays;
import me.lucko.spark.api.statistic.Statistic;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class AbstractStatistic<W extends Enum<W> & StatisticWindow> implements Statistic<W> {

    private final String name;

    protected final W[] windows;

    protected AbstractStatistic(String name, Class<W> enumClass) {
        this.name = name;
        this.windows = (W[]) enumClass.getEnumConstants();
    }

    @NonNull
    @Override
    public String name() {
        return this.name;
    }

    @Override
    public W[] getWindows() {
        return (W[]) Arrays.copyOf(this.windows, this.windows.length);
    }

    public abstract static class Double<W extends Enum<W> & StatisticWindow> extends AbstractStatistic<W> implements DoubleStatistic<W> {

        public Double(String name, Class<W> enumClass) {
            super(name, enumClass);
        }

        @Override
        public double[] poll() {
            double[] values = new double[this.windows.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = this.poll(this.windows[i]);
            }
            return values;
        }
    }

    public abstract static class Generic<T, W extends Enum<W> & StatisticWindow> extends AbstractStatistic<W> implements GenericStatistic<T, W> {

        private final Class<T> typeClass;

        public Generic(String name, Class<T> typeClass, Class<W> enumClass) {
            super(name, enumClass);
            this.typeClass = typeClass;
        }

        @Override
        public T[] poll() {
            T[] values = (T[]) ((Object[]) Array.newInstance(this.typeClass, this.windows.length));
            for (int i = 0; i < values.length; i++) {
                values[i] = this.poll(this.windows[i]);
            }
            return values;
        }
    }
}