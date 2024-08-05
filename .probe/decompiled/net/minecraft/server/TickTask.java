package net.minecraft.server;

public class TickTask implements Runnable {

    private final int tick;

    private final Runnable runnable;

    public TickTask(int int0, Runnable runnable1) {
        this.tick = int0;
        this.runnable = runnable1;
    }

    public int getTick() {
        return this.tick;
    }

    public void run() {
        this.runnable.run();
    }
}