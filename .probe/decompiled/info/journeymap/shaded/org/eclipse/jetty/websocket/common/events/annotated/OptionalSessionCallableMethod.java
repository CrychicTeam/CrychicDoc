package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.annotated;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.Session;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;

public class OptionalSessionCallableMethod extends CallableMethod {

    private final boolean wantsSession;

    private final boolean streaming;

    public OptionalSessionCallableMethod(Class<?> pojo, Method method) {
        super(pojo, method);
        boolean foundConnection = false;
        boolean foundStreaming = false;
        if (this.paramTypes != null) {
            for (Class<?> paramType : this.paramTypes) {
                if (Session.class.isAssignableFrom(paramType)) {
                    foundConnection = true;
                }
                if (Reader.class.isAssignableFrom(paramType) || InputStream.class.isAssignableFrom(paramType)) {
                    foundStreaming = true;
                }
            }
        }
        this.wantsSession = foundConnection;
        this.streaming = foundStreaming;
    }

    public void call(Object obj, Session connection, Object... args) {
        if (this.wantsSession) {
            Object[] fullArgs = new Object[args.length + 1];
            fullArgs[0] = connection;
            System.arraycopy(args, 0, fullArgs, 1, args.length);
            this.call(obj, fullArgs);
        } else {
            this.call(obj, args);
        }
    }

    public boolean isSessionAware() {
        return this.wantsSession;
    }

    public boolean isStreaming() {
        return this.streaming;
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", this.getClass().getSimpleName(), this.method.toGenericString());
    }
}