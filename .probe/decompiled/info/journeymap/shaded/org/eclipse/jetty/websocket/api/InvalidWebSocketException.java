package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

public class InvalidWebSocketException extends WebSocketException {

    public InvalidWebSocketException(String message) {
        super(message);
    }

    public InvalidWebSocketException(String message, Throwable cause) {
        super(message, cause);
    }
}