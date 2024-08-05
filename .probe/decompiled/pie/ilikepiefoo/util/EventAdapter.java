package pie.ilikepiefoo.util;

import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventResult;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pie.ilikepiefoo.events.ProxyEventJS;

public class EventAdapter<T> implements InvocationHandler {

    public static final Logger LOG = LogManager.getLogger();

    public final String name;

    public final T handler;

    public final Class<T> eventClass;

    public final Set<Method> customMethods;

    public final EventHandler[] handlers;

    public EventAdapter(Class<T> eventClass, String eventName, EventHandler... handlers) {
        this.name = eventName;
        this.eventClass = eventClass;
        this.handlers = handlers;
        if (!this.eventClass.isInterface()) {
            throw new IllegalArgumentException("Event must be an interface!");
        } else {
            this.handler = (T) eventClass.cast(Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { eventClass }, this));
            this.customMethods = (Set<Method>) Arrays.stream(this.eventClass.getMethods()).filter(method -> !Modifier.isStatic(method.getModifiers())).filter(method -> !method.getDeclaringClass().equals(Object.class)).collect(Collectors.toSet());
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!this.customMethods.contains(method)) {
            return method.isDefault() ? InvocationHandler.invokeDefault(proxy, method, args) : method.invoke(this, args);
        } else {
            ProxyEventJS event = new ProxyEventJS(method, args);
            EventResult result = EventResult.PASS;
            for (EventHandler handler : this.handlers) {
                if (result != EventResult.PASS) {
                    break;
                }
                result = handler.post(event, this.name);
            }
            if (event.requiresResult()) {
                if (!event.hasResult()) {
                    throw new IllegalArgumentException("Event requires a result but was not provided one!");
                } else {
                    return event.getResult();
                }
            } else {
                return null;
            }
        }
    }
}