package info.journeymap.shaded.org.eclipse.jetty.security;

import java.security.GeneralSecurityException;

public class ServerAuthException extends GeneralSecurityException {

    public ServerAuthException() {
    }

    public ServerAuthException(String s) {
        super(s);
    }

    public ServerAuthException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServerAuthException(Throwable throwable) {
        super(throwable);
    }
}