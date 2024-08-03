package dev.architectury.event;

import com.google.common.reflect.AbstractInvocationHandler;
import dev.architectury.annotations.ForgeEvent;
import dev.architectury.annotations.ForgeEventCancellable;
import dev.architectury.event.forge.EventFactoryImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class EventFactory {

    private EventFactory() {
    }

    public static <T> Event<T> of(Function<List<T>, T> function) {
        return new EventFactory.EventImpl<>(function);
    }

    @SafeVarargs
    public static <T> Event<T> createLoop(T... typeGetter) {
        if (typeGetter.length != 0) {
            throw new IllegalStateException("array must be empty!");
        } else {
            return createLoop(typeGetter.getClass().getComponentType());
        }
    }

    private static <T, R> R invokeMethod(T listener, Method method, Object[] args) throws Throwable {
        return (R) MethodHandles.lookup().unreflect(method).bindTo(listener).invokeWithArguments(args);
    }

    public static <T> Event<T> createLoop(Class<T> clazz) {
        return of(listeners -> Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[] { clazz }, new AbstractInvocationHandler() {

            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (T listener : listeners) {
                    EventFactory.invokeMethod(listener, method, args);
                }
                return null;
            }
        }));
    }

    @SafeVarargs
    public static <T> Event<T> createEventResult(T... typeGetter) {
        if (typeGetter.length != 0) {
            throw new IllegalStateException("array must be empty!");
        } else {
            return createEventResult(typeGetter.getClass().getComponentType());
        }
    }

    public static <T> Event<T> createEventResult(Class<T> clazz) {
        return of(listeners -> Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[] { clazz }, new AbstractInvocationHandler() {

            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (T listener : listeners) {
                    EventResult result = (EventResult) Objects.requireNonNull(EventFactory.invokeMethod(listener, method, args));
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return EventResult.pass();
            }
        }));
    }

    @SafeVarargs
    public static <T> Event<T> createCompoundEventResult(T... typeGetter) {
        if (typeGetter.length != 0) {
            throw new IllegalStateException("array must be empty!");
        } else {
            return createCompoundEventResult(typeGetter.getClass().getComponentType());
        }
    }

    public static <T> Event<T> createCompoundEventResult(Class<T> clazz) {
        return of(listeners -> Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[] { clazz }, new AbstractInvocationHandler() {

            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (T listener : listeners) {
                    CompoundEventResult result = (CompoundEventResult) Objects.requireNonNull(EventFactory.invokeMethod(listener, method, args));
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return CompoundEventResult.pass();
            }
        }));
    }

    @SafeVarargs
    public static <T> Event<Consumer<T>> createConsumerLoop(T... typeGetter) {
        if (typeGetter.length != 0) {
            throw new IllegalStateException("array must be empty!");
        } else {
            return createConsumerLoop(typeGetter.getClass().getComponentType());
        }
    }

    public static <T> Event<Consumer<T>> createConsumerLoop(Class<T> clazz) {
        Event<Consumer<T>> event = of(listeners -> (Consumer) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[] { Consumer.class }, new AbstractInvocationHandler() {

            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (Consumer<T> listener : listeners) {
                    EventFactory.invokeMethod(listener, method, args);
                }
                return null;
            }
        }));
        Class<?> superClass = clazz;
        while (!superClass.isAnnotationPresent(ForgeEvent.class)) {
            superClass = superClass.getSuperclass();
            if (superClass == null) {
                return event;
            }
        }
        return attachToForge(event);
    }

    @SafeVarargs
    public static <T> Event<EventActor<T>> createEventActorLoop(T... typeGetter) {
        if (typeGetter.length != 0) {
            throw new IllegalStateException("array must be empty!");
        } else {
            return createEventActorLoop(typeGetter.getClass().getComponentType());
        }
    }

    public static <T> Event<EventActor<T>> createEventActorLoop(Class<T> clazz) {
        Event<EventActor<T>> event = of(listeners -> (EventActor) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[] { EventActor.class }, new AbstractInvocationHandler() {

            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (EventActor<T> listener : listeners) {
                    EventResult result = EventFactory.invokeMethod(listener, method, args);
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return EventResult.pass();
            }
        }));
        Class<?> superClass = clazz;
        while (!superClass.isAnnotationPresent(ForgeEventCancellable.class)) {
            superClass = superClass.getSuperclass();
            if (superClass == null) {
                superClass = clazz;
                while (!superClass.isAnnotationPresent(ForgeEvent.class)) {
                    superClass = superClass.getSuperclass();
                    if (superClass == null) {
                        return event;
                    }
                }
                return attachToForgeEventActor(event);
            }
        }
        return attachToForgeEventActorCancellable(event);
    }

    @ExpectPlatform
    @Internal
    @Transformed
    public static <T> Event<Consumer<T>> attachToForge(Event<Consumer<T>> event) {
        return EventFactoryImpl.attachToForge(event);
    }

    @ExpectPlatform
    @Internal
    @Transformed
    public static <T> Event<EventActor<T>> attachToForgeEventActor(Event<EventActor<T>> event) {
        return EventFactoryImpl.attachToForgeEventActor(event);
    }

    @ExpectPlatform
    @Internal
    @Transformed
    public static <T> Event<EventActor<T>> attachToForgeEventActorCancellable(Event<EventActor<T>> event) {
        return EventFactoryImpl.attachToForgeEventActorCancellable(event);
    }

    private static class EventImpl<T> implements Event<T> {

        private final Function<List<T>, T> function;

        private T invoker = (T) null;

        private ArrayList<T> listeners;

        public EventImpl(Function<List<T>, T> function) {
            this.function = function;
            this.listeners = new ArrayList();
        }

        @Override
        public T invoker() {
            if (this.invoker == null) {
                this.update();
            }
            return this.invoker;
        }

        @Override
        public void register(T listener) {
            this.listeners.add(listener);
            this.invoker = null;
        }

        @Override
        public void unregister(T listener) {
            this.listeners.remove(listener);
            this.listeners.trimToSize();
            this.invoker = null;
        }

        @Override
        public boolean isRegistered(T listener) {
            return this.listeners.contains(listener);
        }

        @Override
        public void clearListeners() {
            this.listeners.clear();
            this.listeners.trimToSize();
            this.invoker = null;
        }

        public void update() {
            if (this.listeners.size() == 1) {
                this.invoker = (T) this.listeners.get(0);
            } else {
                this.invoker = (T) this.function.apply(this.listeners);
            }
        }
    }
}