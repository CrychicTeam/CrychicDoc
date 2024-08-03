package info.journeymap.shaded.org.eclipse.jetty.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Uptime {

    public static final int NOIMPL = -1;

    private static final Uptime INSTANCE = new Uptime();

    private Uptime.Impl impl;

    public static Uptime getInstance() {
        return INSTANCE;
    }

    private Uptime() {
        try {
            this.impl = new Uptime.DefaultImpl();
        } catch (UnsupportedOperationException var2) {
            System.err.printf("Defaulting Uptime to NOIMPL due to (%s) %s%n", var2.getClass().getName(), var2.getMessage());
            this.impl = null;
        }
    }

    public Uptime.Impl getImpl() {
        return this.impl;
    }

    public void setImpl(Uptime.Impl impl) {
        this.impl = impl;
    }

    public static long getUptime() {
        Uptime u = getInstance();
        return u != null && u.impl != null ? u.impl.getUptime() : -1L;
    }

    public static class DefaultImpl implements Uptime.Impl {

        public Object mxBean;

        public Method uptimeMethod;

        public DefaultImpl() {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            try {
                Class<?> mgmtFactory = Class.forName("java.lang.management.ManagementFactory", true, cl);
                Class<?> runtimeClass = Class.forName("java.lang.management.RuntimeMXBean", true, cl);
                Class<?>[] noparams = new Class[0];
                Method mxBeanMethod = mgmtFactory.getMethod("getRuntimeMXBean", noparams);
                if (mxBeanMethod == null) {
                    throw new UnsupportedOperationException("method getRuntimeMXBean() not found");
                } else {
                    this.mxBean = mxBeanMethod.invoke(mgmtFactory);
                    if (this.mxBean == null) {
                        throw new UnsupportedOperationException("getRuntimeMXBean() method returned null");
                    } else {
                        this.uptimeMethod = runtimeClass.getMethod("getUptime", noparams);
                        if (this.mxBean == null) {
                            throw new UnsupportedOperationException("method getUptime() not found");
                        }
                    }
                }
            } catch (NoClassDefFoundError | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException var6) {
                throw new UnsupportedOperationException("Implementation not available in this environment", var6);
            }
        }

        @Override
        public long getUptime() {
            try {
                return (Long) this.uptimeMethod.invoke(this.mxBean);
            } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var2) {
                return -1L;
            }
        }
    }

    public interface Impl {

        long getUptime();
    }
}