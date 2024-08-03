package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BatchMode;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.RemoteEndpoint;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;

public interface RemoteEndpointFactory {

    RemoteEndpoint newRemoteEndpoint(LogicalConnection var1, OutgoingFrames var2, BatchMode var3);
}