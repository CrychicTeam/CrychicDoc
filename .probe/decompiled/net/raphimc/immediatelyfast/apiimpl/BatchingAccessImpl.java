package net.raphimc.immediatelyfast.apiimpl;

import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import net.raphimc.immediatelyfastapi.BatchingAccess;

public class BatchingAccessImpl implements BatchingAccess {

    @Override
    public void beginHudBatching() {
        BatchingBuffers.beginHudBatching();
    }

    @Override
    public void endHudBatching() {
        BatchingBuffers.endHudBatching();
    }

    @Override
    public boolean isHudBatching() {
        return BatchingBuffers.isHudBatching();
    }

    @Override
    public boolean hasDataToDraw() {
        return BatchingBuffers.hasDataToDraw();
    }

    @Override
    public void forceDrawBuffers() {
        BatchingBuffers.forceDrawBuffers();
    }
}