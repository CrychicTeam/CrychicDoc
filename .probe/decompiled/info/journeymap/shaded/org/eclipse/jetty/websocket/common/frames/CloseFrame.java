package info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;

public class CloseFrame extends ControlFrame {

    public CloseFrame() {
        super((byte) 8);
    }

    @Override
    public Frame.Type getType() {
        return Frame.Type.CLOSE;
    }

    public static String truncate(String reason) {
        return StringUtil.truncate(reason, 123);
    }
}