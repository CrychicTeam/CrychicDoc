package icyllis.arc3d.engine;

import icyllis.arc3d.core.RefCnt;

public abstract class ReleaseCallback extends RefCnt {

    @Override
    protected final void deallocate() {
        this.invoke();
    }

    public abstract void invoke();
}