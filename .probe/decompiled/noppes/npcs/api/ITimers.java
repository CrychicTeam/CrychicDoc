package noppes.npcs.api;

public interface ITimers {

    void start(int var1, int var2, boolean var3);

    void forceStart(int var1, int var2, boolean var3);

    boolean has(int var1);

    boolean stop(int var1);

    void reset(int var1);

    void clear();
}