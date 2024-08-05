package info.journeymap.shaded.org.eclipse.jetty.websocket.common.io;

import info.journeymap.shaded.org.eclipse.jetty.util.FutureCallback;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WriteCallback;

public class FutureWriteCallback extends FutureCallback implements WriteCallback {

    private static final Logger LOG = Log.getLogger(FutureWriteCallback.class);

    @Override
    public void writeFailed(Throwable cause) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(".writeFailed", cause);
        }
        this.failed(cause);
    }

    @Override
    public void writeSuccess() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(".writeSuccess");
        }
        this.succeeded();
    }
}