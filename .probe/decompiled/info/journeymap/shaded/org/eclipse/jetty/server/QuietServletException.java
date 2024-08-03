package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.io.QuietException;
import info.journeymap.shaded.org.javax.servlet.ServletException;

public class QuietServletException extends ServletException implements QuietException {

    public QuietServletException() {
    }

    public QuietServletException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public QuietServletException(String message) {
        super(message);
    }

    public QuietServletException(Throwable rootCause) {
        super(rootCause);
    }
}