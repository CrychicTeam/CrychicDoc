package me.lucko.spark.common.platform.world;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface CountMap<T> {

    void increment(T var1);

    void add(T var1, int var2);

    AtomicInteger total();

    Map<T, AtomicInteger> asMap();

    public static class EnumKeyed<T extends Enum<T>> extends CountMap.Simple<T> {

        public EnumKeyed(Class<T> keyClass) {
            super(new EnumMap(keyClass));
        }
    }

    public static class Simple<T> implements CountMap<T> {

        private final Map<T, AtomicInteger> counts;

        private final AtomicInteger total;

        public Simple(Map<T, AtomicInteger> counts) {
            this.counts = counts;
            this.total = new AtomicInteger();
        }

        @Override
        public void increment(T key) {
            AtomicInteger counter = (AtomicInteger) this.counts.get(key);
            if (counter == null) {
                counter = new AtomicInteger();
                this.counts.put(key, counter);
            }
            counter.incrementAndGet();
            this.total.incrementAndGet();
        }

        @Override
        public void add(T key, int delta) {
            AtomicInteger counter = (AtomicInteger) this.counts.get(key);
            if (counter == null) {
                counter = new AtomicInteger();
                this.counts.put(key, counter);
            }
            counter.addAndGet(delta);
            this.total.addAndGet(delta);
        }

        @Override
        public AtomicInteger total() {
            return this.total;
        }

        @Override
        public Map<T, AtomicInteger> asMap() {
            return this.counts;
        }
    }
}