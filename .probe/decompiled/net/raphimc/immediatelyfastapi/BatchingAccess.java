package net.raphimc.immediatelyfastapi;

public interface BatchingAccess {

    void beginHudBatching();

    void endHudBatching();

    boolean isHudBatching();

    boolean hasDataToDraw();

    void forceDrawBuffers();
}