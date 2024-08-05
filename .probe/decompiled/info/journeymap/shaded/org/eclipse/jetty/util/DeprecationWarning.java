package info.journeymap.shaded.org.eclipse.jetty.util;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;

public class DeprecationWarning implements Decorator {

    private static final Logger LOG = Log.getLogger(DeprecationWarning.class);

    @Override
    public <T> T decorate(T o) {
        if (o == null) {
            return null;
        } else {
            Class<?> clazz = o.getClass();
            try {
                Deprecated depr = (Deprecated) clazz.getAnnotation(Deprecated.class);
                if (depr != null) {
                    LOG.warn("Using @Deprecated Class {}", clazz.getName());
                }
            } catch (Throwable var7) {
                LOG.ignore(var7);
            }
            this.verifyIndirectTypes(clazz.getSuperclass(), clazz, "Class");
            for (Class<?> ifaceClazz : clazz.getInterfaces()) {
                this.verifyIndirectTypes(ifaceClazz, clazz, "Interface");
            }
            return o;
        }
    }

    private void verifyIndirectTypes(Class<?> superClazz, Class<?> clazz, String typeName) {
        try {
            while (superClazz != null && superClazz != Object.class) {
                Deprecated supDepr = (Deprecated) superClazz.getAnnotation(Deprecated.class);
                if (supDepr != null) {
                    LOG.warn("Using indirect @Deprecated {} {} - (seen from {})", typeName, superClazz.getName(), clazz);
                }
                superClazz = superClazz.getSuperclass();
            }
        } catch (Throwable var5) {
            LOG.ignore(var5);
        }
    }

    @Override
    public void destroy(Object o) {
    }
}