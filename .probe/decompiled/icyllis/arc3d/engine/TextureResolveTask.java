package icyllis.arc3d.engine;

import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.core.Rect2ic;
import icyllis.arc3d.core.SharedPtr;
import java.util.ArrayList;
import java.util.List;

public final class TextureResolveTask extends RenderTask {

    private final List<TextureResolveTask.Resolve> mResolves = new ArrayList(4);

    public TextureResolveTask(RenderTaskManager taskManager) {
        super(taskManager);
    }

    public void addTexture(@SharedPtr TextureProxy proxy, int resolveFlags) {
        assert this.mTaskManager.getLastRenderTask(proxy) == null || this.mTaskManager.getLastRenderTask(proxy).isClosed();
        assert resolveFlags != 0;
        Rect2ic msaaRect;
        if ((resolveFlags & 1) != 0) {
            assert proxy.needsResolve();
            msaaRect = proxy.getResolveRect();
            proxy.setResolveRect(0, 0, 0, 0);
        } else {
            msaaRect = Rect2i.empty();
        }
        if ((resolveFlags & 2) != 0) {
            assert proxy.isMipmapped() && proxy.isMipmapsDirty();
            proxy.setMipmapsDirty(false);
        }
        this.mResolves.add(new TextureResolveTask.Resolve(resolveFlags, msaaRect.left(), msaaRect.top(), msaaRect.right(), msaaRect.bottom()));
        this.addDependency(proxy, 1188369);
        this.addTarget(proxy);
    }

    @Override
    public boolean execute(OpFlushState flushState) {
        return false;
    }

    private static record Resolve(int flags, int msaaLeft, int msaaTop, int msaaRight, int msaaBottom) {
    }
}