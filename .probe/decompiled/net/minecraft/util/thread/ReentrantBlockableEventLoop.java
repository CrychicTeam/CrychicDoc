package net.minecraft.util.thread;

public abstract class ReentrantBlockableEventLoop<R extends Runnable> extends BlockableEventLoop<R> {

    private int reentrantCount;

    public ReentrantBlockableEventLoop(String string0) {
        super(string0);
    }

    @Override
    public boolean scheduleExecutables() {
        return this.runningTask() || super.scheduleExecutables();
    }

    protected boolean runningTask() {
        return this.reentrantCount != 0;
    }

    @Override
    public void doRunTask(R r0) {
        this.reentrantCount++;
        try {
            super.doRunTask(r0);
        } finally {
            this.reentrantCount--;
        }
    }
}