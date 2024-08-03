package info.journeymap.shaded.kotlin.spark;

import java.util.HashMap;
import java.util.Map;

public class ExceptionMapper {

    private static ExceptionMapper defaultInstance;

    private Map<Class<? extends Exception>, ExceptionHandlerImpl> exceptionMap = new HashMap();

    public static synchronized ExceptionMapper getInstance() {
        if (defaultInstance == null) {
            defaultInstance = new ExceptionMapper();
        }
        return defaultInstance;
    }

    public void map(Class<? extends Exception> exceptionClass, ExceptionHandlerImpl handler) {
        this.exceptionMap.put(exceptionClass, handler);
    }

    public ExceptionHandlerImpl getHandler(Class<? extends Exception> exceptionClass) {
        if (this.exceptionMap.containsKey(exceptionClass)) {
            return (ExceptionHandlerImpl) this.exceptionMap.get(exceptionClass);
        } else {
            Class<?> superclass = exceptionClass.getSuperclass();
            while (!this.exceptionMap.containsKey(superclass)) {
                superclass = superclass.getSuperclass();
                if (superclass == null) {
                    this.exceptionMap.put(exceptionClass, null);
                    return null;
                }
            }
            ExceptionHandlerImpl handler = (ExceptionHandlerImpl) this.exceptionMap.get(superclass);
            this.exceptionMap.put(exceptionClass, handler);
            return handler;
        }
    }

    public ExceptionHandlerImpl getHandler(Exception exception) {
        return this.getHandler(exception.getClass());
    }

    public void clear() {
        this.exceptionMap.clear();
    }
}