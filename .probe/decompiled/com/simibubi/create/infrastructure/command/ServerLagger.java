package com.simibubi.create.infrastructure.command;

public class ServerLagger {

    private int tickTime;

    private boolean isLagging = false;

    public void tick() {
        if (this.isLagging && this.tickTime > 0) {
            try {
                Thread.sleep((long) this.tickTime);
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }
    }

    public void setTickTime(int tickTime) {
        this.tickTime = Math.max(tickTime, 0);
    }

    public void setLagging(boolean lagging) {
        this.isLagging = lagging;
    }

    public int getTickTime() {
        return this.tickTime;
    }

    public boolean isLagging() {
        return this.isLagging;
    }
}