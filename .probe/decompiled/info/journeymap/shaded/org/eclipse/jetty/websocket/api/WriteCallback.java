package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

public interface WriteCallback {

    void writeFailed(Throwable var1);

    void writeSuccess();
}