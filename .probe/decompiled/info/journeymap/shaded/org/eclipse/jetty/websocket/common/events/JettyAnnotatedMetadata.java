package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events;

import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.annotated.CallableMethod;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.annotated.OptionalSessionCallableMethod;

public class JettyAnnotatedMetadata {

    public CallableMethod onConnect;

    public OptionalSessionCallableMethod onBinary;

    public OptionalSessionCallableMethod onText;

    public OptionalSessionCallableMethod onFrame;

    public OptionalSessionCallableMethod onError;

    public OptionalSessionCallableMethod onClose;

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("JettyPojoMetadata[");
        s.append("onConnect=").append(this.onConnect);
        s.append(",onBinary=").append(this.onBinary);
        s.append(",onText=").append(this.onText);
        s.append(",onFrame=").append(this.onFrame);
        s.append(",onError=").append(this.onError);
        s.append(",onClose=").append(this.onClose);
        s.append("]");
        return s.toString();
    }
}