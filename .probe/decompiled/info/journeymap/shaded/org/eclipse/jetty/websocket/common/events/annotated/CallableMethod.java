package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.annotated;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.util.ReflectUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class CallableMethod {

    private static final Logger LOG = Log.getLogger(CallableMethod.class);

    protected final Class<?> pojo;

    protected final Method method;

    protected Class<?>[] paramTypes;

    public CallableMethod(Class<?> pojo, Method method) {
        Objects.requireNonNull(pojo, "Pojo cannot be null");
        Objects.requireNonNull(method, "Method cannot be null");
        this.pojo = pojo;
        this.method = method;
        this.paramTypes = method.getParameterTypes();
    }

    public Object call(Object obj, Object... args) {
        if (this.pojo == null || this.method == null) {
            LOG.warn("Cannot execute call: pojo={}, method={}", this.pojo, this.method);
            return null;
        } else if (obj == null) {
            String err = String.format("Cannot call %s on null object", this.method);
            LOG.warn(new RuntimeException(err));
            return null;
        } else if (args.length < this.paramTypes.length) {
            throw new IllegalArgumentException("Call arguments length [" + args.length + "] must always be greater than or equal to captured args length [" + this.paramTypes.length + "]");
        } else {
            try {
                return this.method.invoke(obj, args);
            } catch (Throwable var5) {
                String err = this.formatMethodCallError(args);
                throw this.unwrapRuntimeException(err, var5);
            }
        }
    }

    private RuntimeException unwrapRuntimeException(String err, Throwable t) {
        Throwable ret = t;
        while (ret instanceof InvocationTargetException) {
            ret = ((InvocationTargetException) ret).getCause();
        }
        return ret instanceof RuntimeException ? (RuntimeException) ret : new RuntimeException(err, ret);
    }

    public String formatMethodCallError(Object... args) {
        StringBuilder err = new StringBuilder();
        err.append("Cannot call method ");
        err.append(ReflectUtils.toString(this.pojo, this.method));
        err.append(" with args: [");
        boolean delim = false;
        for (Object arg : args) {
            if (delim) {
                err.append(", ");
            }
            if (arg == null) {
                err.append("<null>");
            } else {
                err.append(arg.getClass().getName());
            }
            delim = true;
        }
        err.append("]");
        return err.toString();
    }

    public Method getMethod() {
        return this.method;
    }

    public Class<?>[] getParamTypes() {
        return this.paramTypes;
    }

    public Class<?> getPojo() {
        return this.pojo;
    }

    public String toString() {
        return String.format("%s[%s]", this.getClass().getSimpleName(), this.method.toGenericString());
    }
}