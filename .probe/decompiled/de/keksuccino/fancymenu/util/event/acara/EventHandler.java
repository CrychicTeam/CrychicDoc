package de.keksuccino.fancymenu.util.event.acara;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class EventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final EventHandler INSTANCE = new EventHandler();

    private final Map<Class<? extends EventBase>, List<EventHandler.ListenerContainer>> events = new HashMap();

    public void postEvent(EventBase event) {
        if (this.eventsRegisteredForType(event.getClass())) {
            List<EventHandler.ListenerContainer> l = new ArrayList((Collection) this.events.get(event.getClass()));
            l.sort((o1, o2) -> {
                if (o1.priority < o2.priority) {
                    return 1;
                } else {
                    return o1.priority > o2.priority ? -1 : 0;
                }
            });
            for (EventHandler.ListenerContainer c : l) {
                c.notifyListener(event);
            }
        }
    }

    public void registerListenersOf(Class<?> clazz) {
        this.registerListenerMethods(this.getEventMethodsOf(clazz));
    }

    public void registerListenersOf(Object object) {
        this.registerListenerMethods(this.getEventMethodsOf(object));
    }

    protected void registerListenerMethods(List<EventHandler.EventMethod> methods) {
        for (EventHandler.EventMethod m : methods) {
            Consumer<EventBase> listener = event -> {
                try {
                    m.method.invoke(m.parentObject, event);
                } catch (Exception var3x) {
                    throw new RuntimeException(var3x);
                }
            };
            EventHandler.ListenerContainer container = new EventHandler.ListenerContainer(m.eventType, listener, m.priority);
            container.listenerParentClassName = m.parentClass.getName();
            container.listenerMethodName = m.method.getName();
            this.registerListener(container);
        }
    }

    protected List<EventHandler.EventMethod> getEventMethodsOf(Object objectOrClass) {
        List<EventHandler.EventMethod> l = new ArrayList();
        try {
            if (objectOrClass != null) {
                boolean isClass = objectOrClass instanceof Class;
                Class<?> c = isClass ? (Class) objectOrClass : objectOrClass.getClass();
                for (Method m : c.getMethods()) {
                    if (isClass && Modifier.isStatic(m.getModifiers())) {
                        EventHandler.EventMethod em = EventHandler.EventMethod.tryCreateFrom(new EventHandler.AnalyzedMethod(m, c));
                        if (em != null && this.hasEventListenerAnnotation(em)) {
                            l.add(em);
                        }
                    }
                    if (!isClass && !Modifier.isStatic(m.getModifiers())) {
                        EventHandler.EventMethod em = EventHandler.EventMethod.tryCreateFrom(new EventHandler.AnalyzedMethod(m, objectOrClass));
                        if (em != null && this.hasEventListenerAnnotation(em)) {
                            l.add(em);
                        }
                    }
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }
        return l;
    }

    protected boolean hasEventListenerAnnotation(@NotNull EventHandler.EventMethod m) {
        for (Annotation a : m.annotations) {
            if (a instanceof EventListener) {
                return true;
            }
        }
        return false;
    }

    public void registerListener(Consumer<EventBase> listener, Class<? extends EventBase> eventType) {
        this.registerListener(listener, eventType, 0);
    }

    public void registerListener(Consumer<EventBase> listener, Class<? extends EventBase> eventType, int priority) {
        this.registerListener(new EventHandler.ListenerContainer(eventType, listener, priority));
    }

    protected void registerListener(EventHandler.ListenerContainer listenerContainer) {
        try {
            if (!this.eventsRegisteredForType(listenerContainer.eventType)) {
                this.events.put(listenerContainer.eventType, new ArrayList());
            }
            ((List) this.events.get(listenerContainer.eventType)).add(listenerContainer);
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public boolean eventsRegisteredForType(Class<? extends EventBase> eventType) {
        return eventType == null ? false : this.events.containsKey(eventType);
    }

    protected static class AnalyzedMethod {

        protected Method method;

        protected Object parentObject;

        protected Class<?> parentClass;

        protected boolean isStatic;

        protected List<Annotation> annotations = new ArrayList();

        protected AnalyzedMethod() {
        }

        protected AnalyzedMethod(Method method, Object parentObjectOrClass) {
            this.method = method;
            this.parentObject = parentObjectOrClass;
            this.parentClass = this.tryGetParentClass();
            this.isStatic = Modifier.isStatic(method.getModifiers());
            collectMethodAnnotations(this.isStatic ? null : this.parentObject.getClass(), this.method, this.annotations);
        }

        protected Class<?> tryGetParentClass() {
            return this.parentObject instanceof Class ? (Class) this.parentObject : this.parentObject.getClass();
        }

        protected static void collectMethodAnnotations(Class<?> c, Method m, List<Annotation> addToList) {
            try {
                addToList.addAll(Arrays.asList(m.getAnnotations()));
                if (!Modifier.isStatic(m.getModifiers()) && c != null) {
                    Class<?> sc = c.getSuperclass();
                    if (sc != null) {
                        try {
                            Method sm = sc.getMethod(m.getName(), m.getParameterTypes());
                            collectMethodAnnotations(sc, sm, addToList);
                        } catch (Exception var5) {
                        }
                    }
                }
            } catch (Exception var6) {
            }
        }
    }

    protected static class EventMethod extends EventHandler.AnalyzedMethod {

        protected final int priority;

        protected final Class<? extends EventBase> eventType;

        protected static EventHandler.EventMethod tryCreateFrom(EventHandler.AnalyzedMethod method) {
            EventHandler.EventMethod em = new EventHandler.EventMethod(method);
            return em.eventType != null ? em : null;
        }

        protected EventMethod(EventHandler.AnalyzedMethod method) {
            this.method = method.method;
            this.parentObject = method.parentObject;
            this.parentClass = method.parentClass;
            this.isStatic = method.isStatic;
            this.annotations = method.annotations;
            this.priority = this.tryGetPriority();
            this.eventType = this.tryGetEventType();
        }

        protected Class<? extends EventBase> tryGetEventType() {
            try {
                if (this.method != null) {
                    Class<?>[] params = this.method.getParameterTypes();
                    if (params.length > 0) {
                        Class<?> firstParam = params[0];
                        if (EventBase.class.isAssignableFrom(firstParam)) {
                            return (Class<? extends EventBase>) firstParam;
                        }
                    }
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
            return null;
        }

        protected int tryGetPriority() {
            try {
                for (Annotation a : this.annotations) {
                    if (a instanceof EventListener) {
                        return ((EventListener) a).priority();
                    }
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
            return 0;
        }
    }

    protected static class ListenerContainer {

        protected final Consumer<EventBase> listener;

        protected final Class<? extends EventBase> eventType;

        protected final int priority;

        protected String listenerParentClassName = "[unknown]";

        protected String listenerMethodName = "[unknown]";

        protected ListenerContainer(Class<? extends EventBase> eventType, Consumer<EventBase> listener, int priority) {
            this.listener = listener;
            this.eventType = eventType;
            this.priority = priority;
        }

        protected void notifyListener(EventBase event) {
            try {
                this.listener.accept(event);
            } catch (Exception var3) {
                EventHandler.LOGGER.error("##################################");
                EventHandler.LOGGER.error("[ACARA] Failed to notify event listener!");
                EventHandler.LOGGER.error("[ACARA] Event Type: " + this.eventType.getName());
                EventHandler.LOGGER.error("[ACARA] Listener Parent Class Name: " + this.listenerParentClassName);
                EventHandler.LOGGER.error("[ACARA] Listener Method Name In Parent Class: " + this.listenerMethodName);
                EventHandler.LOGGER.error("##################################");
                var3.printStackTrace();
            }
        }
    }
}