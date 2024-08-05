package info.journeymap.shaded.org.eclipse.jetty.io;

import java.io.EOFException;

public class EofException extends EOFException implements QuietException {

    public EofException() {
    }

    public EofException(String reason) {
        super(reason);
    }

    public EofException(Throwable th) {
        if (th != null) {
            this.initCause(th);
        }
    }
}