package de.keksuccino.konkrete.json.minidev.asm;

import java.lang.reflect.Method;

class DynamicClassLoader extends ClassLoader {

    private static final String BEAN_AC = BeansAccess.class.getName();

    private static final Class<?>[] DEF_CLASS_SIG = new Class[] { String.class, byte[].class, int.class, int.class };

    DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    public static <T> Class<T> directLoad(Class<? extends T> parent, String clsName, byte[] clsData) {
        DynamicClassLoader loader = new DynamicClassLoader(parent.getClassLoader());
        return (Class<T>) loader.defineClass(clsName, clsData);
    }

    public static <T> T directInstance(Class<? extends T> parent, String clsName, byte[] clsData) throws InstantiationException, IllegalAccessException {
        Class<T> clzz = directLoad(parent, clsName, clsData);
        return (T) clzz.newInstance();
    }

    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return name.equals(BEAN_AC) ? BeansAccess.class : super.loadClass(name, resolve);
    }

    Class<?> defineClass(String name, byte[] bytes) throws ClassFormatError {
        try {
            Method method = ClassLoader.class.getDeclaredMethod("defineClass", DEF_CLASS_SIG);
            method.setAccessible(true);
            return (Class<?>) method.invoke(this.getParent(), name, bytes, 0, bytes.length);
        } catch (Exception var4) {
            return this.defineClass(name, bytes, 0, bytes.length);
        }
    }
}