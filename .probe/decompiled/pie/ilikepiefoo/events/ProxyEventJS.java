package pie.ilikepiefoo.events;

import dev.latvian.mods.kubejs.event.EventJS;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProxyEventJS extends EventJS {

    public static final Logger LOG = LogManager.getLogger();

    private final Method method;

    private final Object[] args;

    private final Map<String, Object> parameterMap;

    private Optional<Object> result;

    private boolean hasResult = false;

    public ProxyEventJS(Method method, Object[] args) {
        this.method = method;
        this.parameterMap = new HashMap();
        for (int i = 0; i < method.getParameters().length; i++) {
            this.parameterMap.put(method.getParameters()[i].getName(), args[i]);
        }
        this.args = args;
        this.result = Optional.empty();
    }

    public String getMethodName() {
        return this.method.getName();
    }

    public String getReturnType() {
        return this.method.getReturnType().getName();
    }

    public String getGenericReturnType() {
        return this.method.getGenericReturnType().getTypeName();
    }

    public Object[] getArgs() {
        return this.args;
    }

    public Map<String, Object> getParameters() {
        return this.parameterMap;
    }

    public Optional<Object> getResultOptional() {
        return this.result;
    }

    public Object getResult() {
        return this.result.orElse(null);
    }

    public void setResult(Object result) {
        this.hasResult = true;
        this.result = Optional.ofNullable(result);
    }

    public boolean hasResult() {
        return this.hasResult;
    }

    public boolean requiresResult() {
        return !this.method.getReturnType().equals(void.class);
    }

    public Object getArg(int index) {
        return this.args[index];
    }
}