package me.lucko.spark.common.tick;

public interface TickHook extends AutoCloseable {

    void start();

    void close();

    int getCurrentTick();

    void addCallback(TickHook.Callback var1);

    void removeCallback(TickHook.Callback var1);

    public interface Callback {

        void onTick(int var1);
    }
}