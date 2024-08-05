package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

public class WebSocketException extends RuntimeException {

    public WebSocketException() {
    }

    public WebSocketException(String message) {
        super(message);
    }

    public WebSocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebSocketException(Throwable cause) {
        super(cause);
    }
}