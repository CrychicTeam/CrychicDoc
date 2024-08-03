package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BatchMode;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.CloseInfo;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.WebSocketSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;

public interface EventDriver extends IncomingFrames {

    WebSocketPolicy getPolicy();

    WebSocketSession getSession();

    BatchMode getBatchMode();

    void onBinaryFrame(ByteBuffer var1, boolean var2) throws IOException;

    void onBinaryMessage(byte[] var1);

    void onClose(CloseInfo var1);

    void onConnect();

    void onContinuationFrame(ByteBuffer var1, boolean var2) throws IOException;

    void onError(Throwable var1);

    void onFrame(Frame var1);

    void onInputStream(InputStream var1) throws IOException;

    void onPing(ByteBuffer var1);

    void onPong(ByteBuffer var1);

    void onReader(Reader var1) throws IOException;

    void onTextFrame(ByteBuffer var1, boolean var2) throws IOException;

    void onTextMessage(String var1);

    void openSession(WebSocketSession var1);
}