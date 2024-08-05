package com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari;

public class Context {

    private int stateIdx;

    private int mps;

    public Context(int state, int mps) {
        this.stateIdx = state;
        this.mps = mps;
    }

    public int getState() {
        return this.stateIdx;
    }

    public int getMps() {
        return this.mps;
    }

    public void setMps(int mps) {
        this.mps = mps;
    }

    public void setState(int state) {
        this.stateIdx = state;
    }
}