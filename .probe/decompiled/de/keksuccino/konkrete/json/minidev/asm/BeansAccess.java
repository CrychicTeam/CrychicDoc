package de.keksuccino.konkrete.json.minidev.asm;

import de.keksuccino.konkrete.json.minidev.asm.ex.NoSuchFieldException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BeansAccess<T> {

    private HashMap<String, Accessor> map;

    private Accessor[] accs;

    private static ConcurrentHashMap<Class<?>, BeansAccess<?>> cache = new ConcurrentHashMap();

    protected void setAccessor(Accessor[] accs) {
        int i = 0;
        this.accs = accs;
        this.map = new HashMap();
        for (Accessor acc : accs) {
            acc.index = i++;
            this.map.put(acc.getName(), acc);
        }
    }

    public HashMap<String, Accessor> getMap() {
        return this.map;
    }

    public Accessor[] getAccessors() {
        return this.accs;
    }

    public static <P> BeansAccess<P> get(Class<P> type) {
        return get(type, null);
    }

    public static <P> BeansAccess<P> get(Class<P> type, FieldFilter filter) {
        BeansAccess<P> access = (BeansAccess<P>) cache.get(type);
        if (access != null) {
            return access;
        } else {
            Accessor[] accs = ASMUtil.getAccessors(type, filter);
            String className = type.getName();
            String accessClassName;
            if (className.startsWith("java.util.")) {
                accessClassName = "net.minidev.asm." + className + "AccAccess";
            } else {
                accessClassName = className.concat("AccAccess");
            }
            DynamicClassLoader loader = new DynamicClassLoader(type.getClassLoader());
            Class<?> accessClass = null;
            try {
                accessClass = loader.loadClass(accessClassName);
            } catch (ClassNotFoundException var11) {
            }
            LinkedList<Class<?>> parentClasses = getParents(type);
            if (accessClass == null) {
                BeansAccessBuilder builder = new BeansAccessBuilder(type, accs, loader);
                for (Class<?> c : parentClasses) {
                    builder.addConversion((Iterable<Class<?>>) BeansAccessConfig.classMapper.get(c));
                }
                accessClass = builder.bulid();
            }
            try {
                BeansAccess<P> accessx = (BeansAccess<P>) accessClass.newInstance();
                accessx.setAccessor(accs);
                cache.putIfAbsent(type, accessx);
                for (Class<?> c : parentClasses) {
                    addAlias(accessx, (HashMap<String, String>) BeansAccessConfig.classFiledNameMapper.get(c));
                }
                return accessx;
            } catch (Exception var12) {
                throw new RuntimeException("Error constructing accessor class: " + accessClassName, var12);
            }
        }
    }

    private static LinkedList<Class<?>> getParents(Class<?> type) {
        LinkedList<Class<?>> m = new LinkedList();
        while (type != null && !type.equals(Object.class)) {
            m.addLast(type);
            for (Class<?> c : type.getInterfaces()) {
                m.addLast(c);
            }
            type = type.getSuperclass();
        }
        m.addLast(Object.class);
        return m;
    }

    private static void addAlias(BeansAccess<?> access, HashMap<String, String> m) {
        if (m != null) {
            HashMap<String, Accessor> changes = new HashMap();
            for (Entry<String, String> e : m.entrySet()) {
                Accessor a1 = (Accessor) access.map.get(e.getValue());
                if (a1 != null) {
                    changes.put((String) e.getValue(), a1);
                }
            }
            access.map.putAll(changes);
        }
    }

    public abstract void set(T var1, int var2, Object var3);

    public abstract Object get(T var1, int var2);

    public abstract T newInstance();

    public void set(T object, String methodName, Object value) {
        int i = this.getIndex(methodName);
        if (i == -1) {
            throw new NoSuchFieldException(methodName + " in " + object.getClass() + " to put value : " + value);
        } else {
            this.set(object, i, value);
        }
    }

    public Object get(T object, String methodName) {
        return this.get(object, this.getIndex(methodName));
    }

    public int getIndex(String name) {
        Accessor ac = (Accessor) this.map.get(name);
        return ac == null ? -1 : ac.index;
    }
}