package info.journeymap.shaded.org.eclipse.jetty.websocket.common.message;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface MessageAppender {

    void appendFrame(ByteBuffer var1, boolean var2) throws IOException;

    void messageComplete();
}