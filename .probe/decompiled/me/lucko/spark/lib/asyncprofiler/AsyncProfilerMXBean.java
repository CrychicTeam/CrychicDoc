package me.lucko.spark.lib.asyncprofiler;

import java.io.IOException;

public interface AsyncProfilerMXBean {

    void start(String var1, long var2) throws IllegalStateException;

    void resume(String var1, long var2) throws IllegalStateException;

    void stop() throws IllegalStateException;

    long getSamples();

    String getVersion();

    String execute(String var1) throws IllegalArgumentException, IllegalStateException, IOException;

    String dumpCollapsed(Counter var1);

    String dumpTraces(int var1);

    String dumpFlat(int var1);
}