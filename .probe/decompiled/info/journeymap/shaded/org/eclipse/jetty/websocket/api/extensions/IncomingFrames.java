package info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions;

public interface IncomingFrames {

    void incomingError(Throwable var1);

    void incomingFrame(Frame var1);
}