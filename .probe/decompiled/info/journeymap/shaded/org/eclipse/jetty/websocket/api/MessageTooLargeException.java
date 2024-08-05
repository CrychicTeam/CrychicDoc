package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

public class MessageTooLargeException extends CloseException {

    public MessageTooLargeException(String message) {
        super(1009, message);
    }

    public MessageTooLargeException(String message, Throwable t) {
        super(1009, message, t);
    }

    public MessageTooLargeException(Throwable t) {
        super(1009, t);
    }
}