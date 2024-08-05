package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.annotated;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.InvalidWebSocketException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.ParamList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class InvalidSignatureException extends InvalidWebSocketException {

    public static InvalidSignatureException build(Method method, Class<? extends Annotation> annoClass, ParamList... paramlists) {
        StringBuilder err = new StringBuilder();
        err.append("Invalid declaration of ");
        err.append(method);
        err.append(System.lineSeparator());
        err.append("Acceptable method declarations for @");
        err.append(annoClass.getSimpleName());
        err.append(" are:");
        ParamList[] var4 = paramlists;
        int var5 = paramlists.length;
        for (int var6 = 0; var6 < var5; var6++) {
            for (Class<?>[] params : var4[var6]) {
                err.append(System.lineSeparator());
                err.append("public void ").append(method.getName());
                err.append('(');
                boolean delim = false;
                for (Class<?> type : params) {
                    if (delim) {
                        err.append(',');
                    }
                    err.append(' ');
                    err.append(type.getName());
                    if (type.isArray()) {
                        err.append("[]");
                    }
                    delim = true;
                }
                err.append(')');
            }
        }
        return new InvalidSignatureException(err.toString());
    }

    public InvalidSignatureException(String message) {
        super(message);
    }

    public InvalidSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}