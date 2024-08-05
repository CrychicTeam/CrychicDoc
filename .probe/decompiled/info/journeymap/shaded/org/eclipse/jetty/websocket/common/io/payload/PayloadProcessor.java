package info.journeymap.shaded.org.eclipse.jetty.websocket.common.io.payload;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import java.nio.ByteBuffer;

public interface PayloadProcessor {

    void process(ByteBuffer var1);

    void reset(Frame var1);
}