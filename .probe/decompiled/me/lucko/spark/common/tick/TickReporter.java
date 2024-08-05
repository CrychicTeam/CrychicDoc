package me.lucko.spark.common.tick;

public interface TickReporter extends AutoCloseable {

    void start();

    void close();

    void addCallback(TickReporter.Callback var1);

    void removeCallback(TickReporter.Callback var1);

    public interface Callback {

        void onTick(double var1);
    }
}