package org.violetmoon.zeta.event.bus;

import com.google.common.base.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.Zeta;

public class ZetaEventBus<E> {

    private final Zeta z;

    private final Class<? extends Annotation> subscriberAnnotation;

    private final Class<? extends E> eventRoot;

    @Nullable
    private final Logger logSpam;

    private final Map<Class<? extends E>, ZetaEventBus<E>.Listeners> listenerMap = new HashMap();

    public ZetaEventBus(Zeta z, Class<? extends Annotation> subscriberAnnotation, Class<? extends E> eventRoot, @Nullable Logger logSpam) {
        Preconditions.checkArgument(eventRoot.isInterface(), "Event roots should be an interface");
        this.z = z;
        this.subscriberAnnotation = subscriberAnnotation;
        this.eventRoot = eventRoot;
        this.logSpam = logSpam;
    }

    public ZetaEventBus<E> subscribe(@NotNull Object target) {
        Preconditions.checkNotNull(target, "null passed to subscribe");
        Object receiver;
        Class<?> owningClazz;
        if (target instanceof Class<?> clazz) {
            receiver = null;
            owningClazz = clazz;
        } else {
            receiver = target;
            owningClazz = target.getClass();
        }
        this.streamAnnotatedMethods(owningClazz, receiver == null).forEach(m -> this.getListenersFor(m).subscribe(receiver, owningClazz, m));
        return this;
    }

    public ZetaEventBus<E> unsubscribe(@NotNull Object target) {
        Preconditions.checkNotNull(target, "null passed to unsubscribe");
        Object receiver;
        Class<?> owningClazz;
        if (target instanceof Class<?> clazz) {
            receiver = null;
            owningClazz = clazz;
        } else {
            receiver = target;
            owningClazz = target.getClass();
        }
        this.streamAnnotatedMethods(owningClazz, receiver == null).forEach(m -> this.getListenersFor(m).unsubscribe(receiver, owningClazz, m));
        return this;
    }

    public <T extends E> T fire(@NotNull T event) {
        ZetaEventBus<E>.Listeners subs = (ZetaEventBus.Listeners) this.listenerMap.get(event.getClass());
        if (subs != null) {
            if (this.logSpam != null) {
                this.logSpam.info("Dispatching {} to {} listener{}", this.logspamSimpleName(event.getClass()), subs.size(), subs.size() > 1 ? "s" : "");
            }
            subs.doFire((E) event);
        }
        return event;
    }

    public <T extends E> T fire(@NotNull T event, Class<? super T> firedAs) {
        ZetaEventBus<E>.Listeners subs = (ZetaEventBus.Listeners) this.listenerMap.get(firedAs);
        if (subs != null) {
            if (this.logSpam != null) {
                this.logSpam.info("Dispatching {} (as {}) to {} listener{}", this.logspamSimpleName(event.getClass()), this.logspamSimpleName(firedAs), subs.size(), subs.size() > 1 ? "s" : "");
            }
            subs.doFire((E) event);
        }
        return event;
    }

    private String logspamSimpleName(Class<?> clazz) {
        String[] split = clazz.getName().split("\\.");
        return split[split.length - 1];
    }

    public <T extends E> T fireExternal(@NotNull T event, Class<? super T> firedAs) {
        event = this.fire(event, firedAs);
        if (event instanceof Cancellable cancellable && cancellable.isCanceled()) {
            return event;
        }
        return this.z.fireExternalEvent(event);
    }

    private Stream<Method> streamAnnotatedMethods(Class<?> owningClazz, boolean wantStatic) {
        return Arrays.stream(owningClazz.getMethods()).filter(m -> m.isAnnotationPresent(this.subscriberAnnotation) && (m.getModifiers() & 8) != 0 == wantStatic);
    }

    private ZetaEventBus<E>.Listeners getListenersFor(Method method) {
        if (method.getParameterCount() != 1) {
            throw this.arityERR(method);
        } else {
            Class<?> eventType = method.getParameterTypes()[0];
            if (!this.eventRoot.isAssignableFrom(eventType)) {
                throw this.typeERR(method);
            } else {
                return (ZetaEventBus.Listeners) this.listenerMap.computeIfAbsent(eventType, __ -> new ZetaEventBus.Listeners());
            }
        }
    }

    private RuntimeException arityERR(Method method) {
        return methodProblem("Method annotated with @" + this.subscriberAnnotation.getSimpleName() + " should take 1 parameter.", method, null);
    }

    private RuntimeException typeERR(Method method) {
        return methodProblem("Method annotated with @" + this.subscriberAnnotation.getSimpleName() + " should take an implementor of " + this.eventRoot.getSimpleName() + ".", method, null);
    }

    private RuntimeException unreflectERR(Method method, Throwable cause) {
        return methodProblem("Exception unreflecting a @" + this.subscriberAnnotation.getSimpleName() + " method, is it public?", method, cause);
    }

    private static RuntimeException methodProblem(String problem, Method method, @Nullable Throwable cause) {
        return new RuntimeException("%s%nMethod class: %s%nMethod name: %s".formatted(problem, method.getDeclaringClass().getName(), method.getName()), cause);
    }

    private class Listeners {

        private final Map<ZetaEventBus.Listeners.Subscriber, MethodHandle> handles = new LinkedHashMap();

        void subscribe(@Nullable Object receiver, Class<?> owningClazz, Method method) {
            try {
                this.handles.computeIfAbsent(new ZetaEventBus.Listeners.Subscriber(receiver, owningClazz, method), ZetaEventBus.Listeners.Subscriber::unreflect);
            } catch (Exception var5) {
                throw ZetaEventBus.this.unreflectERR(method, var5);
            }
        }

        void unsubscribe(@Nullable Object receiver, Class<?> owningClazz, Method method) {
            this.handles.remove(new ZetaEventBus.Listeners.Subscriber(receiver, owningClazz, method));
        }

        int size() {
            return this.handles.size();
        }

        void doFire(E event) {
            try {
                if (event instanceof Cancellable cancellable) {
                    this.doFireCancellable(cancellable);
                } else {
                    this.doFireNonCancellable(event);
                }
            } catch (Throwable var3) {
                throw new RuntimeException("Exception while firing event " + event + ": ", var3);
            }
        }

        void doFireCancellable(Cancellable event) throws Throwable {
            for (MethodHandle handle : this.handles.values()) {
                handle.invoke(event);
                if (event.isCanceled()) {
                    break;
                }
            }
        }

        void doFireNonCancellable(E event) throws Throwable {
            for (MethodHandle handle : this.handles.values()) {
                handle.invoke(event);
            }
        }

        private static record Subscriber(@Nullable Object receiver, Class<?> owningClazz, Method method) {

            public boolean equals(Object object) {
                if (this == object) {
                    return true;
                } else if (object != null && this.getClass() == object.getClass()) {
                    ZetaEventBus.Listeners.Subscriber that = (ZetaEventBus.Listeners.Subscriber) object;
                    return this.receiver == that.receiver && Objects.equals(this.owningClazz, that.owningClazz) && Objects.equals(this.method, that.method);
                } else {
                    return false;
                }
            }

            public int hashCode() {
                return System.identityHashCode(this.receiver) + this.owningClazz.hashCode() + this.method.hashCode();
            }

            MethodHandle unreflect() {
                MethodHandle handle;
                try {
                    handle = MethodHandles.publicLookup().unreflect(this.method);
                } catch (Exception var3) {
                    throw new RuntimeException(var3);
                }
                if (this.receiver != null) {
                    handle = handle.bindTo(this.receiver);
                }
                return handle;
            }
        }
    }
}