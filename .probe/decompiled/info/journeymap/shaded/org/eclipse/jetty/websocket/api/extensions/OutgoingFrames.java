package info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BatchMode;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WriteCallback;

public interface OutgoingFrames {

    void outgoingFrame(Frame var1, WriteCallback var2, BatchMode var3);
}