package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.annotated;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.InvalidWebSocketException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.ParamList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class AbstractMethodAnnotationScanner<T> {

    protected void assertIsPublicNonStatic(Method method) {
        int mods = method.getModifiers();
        if (!Modifier.isPublic(mods)) {
            StringBuilder err = new StringBuilder();
            err.append("Invalid declaration of ");
            err.append(method);
            err.append(System.lineSeparator());
            err.append("Method modifier must be public");
            throw new InvalidWebSocketException(err.toString());
        } else if (Modifier.isStatic(mods)) {
            StringBuilder err = new StringBuilder();
            err.append("Invalid declaration of ");
            err.append(method);
            err.append(System.lineSeparator());
            err.append("Method modifier may not be static");
            throw new InvalidWebSocketException(err.toString());
        }
    }

    protected void assertIsReturn(Method method, Class<?> type) {
        if (!type.equals(method.getReturnType())) {
            StringBuilder err = new StringBuilder();
            err.append("Invalid declaration of ");
            err.append(method);
            err.append(System.lineSeparator());
            err.append("Return type must be ").append(type);
            throw new InvalidWebSocketException(err.toString());
        }
    }

    protected void assertIsVoidReturn(Method method) {
        this.assertIsReturn(method, void.class);
    }

    protected void assertUnset(CallableMethod callable, Class<? extends Annotation> annoClass, Method method) {
        if (callable != null) {
            StringBuilder err = new StringBuilder();
            err.append("Duplicate @").append(annoClass.getSimpleName()).append(" declaration on ");
            err.append(method);
            err.append(System.lineSeparator());
            err.append("@").append(annoClass.getSimpleName()).append(" previously declared at ");
            err.append(callable.getMethod());
            throw new InvalidWebSocketException(err.toString());
        }
    }

    protected void assertValidSignature(Method method, Class<? extends Annotation> annoClass, ParamList validParams) {
        this.assertIsPublicNonStatic(method);
        this.assertIsReturn(method, void.class);
        boolean valid = false;
        Class<?>[] actual = method.getParameterTypes();
        for (Class<?>[] params : validParams) {
            if (this.isSameParameters(actual, params)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            throw InvalidSignatureException.build(method, annoClass, validParams);
        }
    }

    public boolean isAnnotation(Annotation annotation, Class<? extends Annotation> annotationClass) {
        return annotation.annotationType().equals(annotationClass);
    }

    public boolean isSameParameters(Class<?>[] actual, Class<?>[] params) {
        if (actual.length != params.length) {
            return false;
        } else {
            int len = params.length;
            for (int i = 0; i < len; i++) {
                if (!actual[i].equals(params[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    protected boolean isSignatureMatch(Method method, ParamList validParams) {
        this.assertIsPublicNonStatic(method);
        this.assertIsReturn(method, void.class);
        Class<?>[] actual = method.getParameterTypes();
        for (Class<?>[] params : validParams) {
            if (this.isSameParameters(actual, params)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isTypeAnnotated(Class<?> pojo, Class<? extends Annotation> expectedAnnotation) {
        return pojo.getAnnotation(expectedAnnotation) != null;
    }

    public abstract void onMethodAnnotation(T var1, Class<?> var2, Method var3, Annotation var4);

    public void scanMethodAnnotations(T metadata, Class<?> pojo) {
        for (Class<?> clazz = pojo; clazz != null && Object.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
            for (Method method : clazz.getDeclaredMethods()) {
                Annotation[] annotations = method.getAnnotations();
                if (annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        this.onMethodAnnotation(metadata, clazz, method, annotation);
                    }
                }
            }
        }
    }
}