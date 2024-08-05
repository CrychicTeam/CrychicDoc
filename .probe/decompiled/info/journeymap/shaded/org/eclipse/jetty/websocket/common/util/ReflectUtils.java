package info.journeymap.shaded.org.eclipse.jetty.websocket.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ReflectUtils {

    private static StringBuilder appendTypeName(StringBuilder sb, Type type, boolean ellipses) {
        if (type instanceof Class) {
            Class<?> ctype = (Class<?>) type;
            if (ctype.isArray()) {
                try {
                    int dimensions;
                    for (dimensions = 0; ctype.isArray(); ctype = ctype.getComponentType()) {
                        dimensions++;
                    }
                    sb.append(ctype.getName());
                    for (int i = 0; i < dimensions; i++) {
                        if (ellipses) {
                            sb.append("...");
                        } else {
                            sb.append("[]");
                        }
                    }
                    return sb;
                } catch (Throwable var6) {
                }
            }
            sb.append(ctype.getName());
        } else {
            sb.append(type.toString());
        }
        return sb;
    }

    public static Class<?> findGenericClassFor(Class<?> baseClass, Class<?> ifaceClass) {
        ReflectUtils.GenericRef ref = new ReflectUtils.GenericRef(baseClass, ifaceClass);
        return resolveGenericRef(ref, baseClass) ? ref.genericClass : null;
    }

    private static int findTypeParameterIndex(Class<?> clazz, TypeVariable<?> needVar) {
        TypeVariable<?>[] params = clazz.getTypeParameters();
        for (int i = 0; i < params.length; i++) {
            if (params[i].getName().equals(needVar.getName())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isDefaultConstructable(Class<?> clazz) {
        int mods = clazz.getModifiers();
        if (!Modifier.isAbstract(mods) && Modifier.isPublic(mods)) {
            Class<?>[] noargs = new Class[0];
            try {
                Constructor<?> constructor = clazz.getConstructor(noargs);
                return Modifier.isPublic(constructor.getModifiers());
            } catch (SecurityException | NoSuchMethodException var4) {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean resolveGenericRef(ReflectUtils.GenericRef ref, Class<?> clazz, Type type) {
        if (type instanceof Class) {
            if (type == ref.ifaceClass) {
                ref.setGenericFromType(type, 0);
                return true;
            } else {
                return resolveGenericRef(ref, type);
            }
        } else if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            Type rawType = ptype.getRawType();
            if (rawType == ref.ifaceClass) {
                ref.setGenericFromType(ptype.getActualTypeArguments()[0], 0);
                return true;
            } else {
                return resolveGenericRef(ref, rawType);
            }
        } else {
            return false;
        }
    }

    private static boolean resolveGenericRef(ReflectUtils.GenericRef ref, Type type) {
        if (type != null && type != Object.class) {
            if (type instanceof Class) {
                Class<?> clazz = (Class<?>) type;
                if (clazz.getName().matches("^javax*\\..*")) {
                    return false;
                } else {
                    Type[] ifaces = clazz.getGenericInterfaces();
                    for (Type iface : ifaces) {
                        if (resolveGenericRef(ref, clazz, iface)) {
                            if (ref.needsUnwrap()) {
                                TypeVariable<?> needVar = (TypeVariable<?>) ref.genericType;
                                int typeParamIdx = findTypeParameterIndex(clazz, needVar);
                                if (typeParamIdx >= 0) {
                                    TypeVariable<?>[] params = clazz.getTypeParameters();
                                    if (params.length >= typeParamIdx) {
                                        ref.setGenericFromType(params[typeParamIdx], typeParamIdx);
                                    }
                                } else if (iface instanceof ParameterizedType) {
                                    Type arg = ((ParameterizedType) iface).getActualTypeArguments()[ref.genericIndex];
                                    ref.setGenericFromType(arg, ref.genericIndex);
                                }
                            }
                            return true;
                        }
                    }
                    type = clazz.getGenericSuperclass();
                    return resolveGenericRef(ref, type);
                }
            } else {
                if (type instanceof ParameterizedType) {
                    ParameterizedType ptype = (ParameterizedType) type;
                    Class<?> rawClass = (Class<?>) ptype.getRawType();
                    if (resolveGenericRef(ref, rawClass) && ref.needsUnwrap()) {
                        TypeVariable<?> needVar = (TypeVariable<?>) ref.genericType;
                        int typeParamIdx = findTypeParameterIndex(rawClass, needVar);
                        Type arg = ptype.getActualTypeArguments()[typeParamIdx];
                        ref.setGenericFromType(arg, typeParamIdx);
                        return true;
                    }
                }
                return false;
            }
        } else {
            return false;
        }
    }

    public static String toShortName(Type type) {
        if (type == null) {
            return "<null>";
        } else if (type instanceof Class) {
            String name = ((Class) type).getName();
            return trimClassName(name);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            StringBuilder str = new StringBuilder();
            str.append(trimClassName(((Class) ptype.getRawType()).getName()));
            str.append("<");
            Type[] args = ptype.getActualTypeArguments();
            for (int i = 0; i < args.length; i++) {
                if (i > 0) {
                    str.append(",");
                }
                str.append(args[i]);
            }
            str.append(">");
            return str.toString();
        } else {
            return type.toString();
        }
    }

    public static String toString(Class<?> pojo, Method method) {
        StringBuilder str = new StringBuilder();
        int mod = method.getModifiers() & Modifier.methodModifiers();
        if (mod != 0) {
            str.append(Modifier.toString(mod)).append(' ');
        }
        Type retType = method.getGenericReturnType();
        appendTypeName(str, retType, false).append(' ');
        str.append(pojo.getName());
        str.append("#");
        str.append(method.getName());
        str.append('(');
        Type[] params = method.getGenericParameterTypes();
        for (int j = 0; j < params.length; j++) {
            boolean ellipses = method.isVarArgs() && j == params.length - 1;
            appendTypeName(str, params[j], ellipses);
            if (j < params.length - 1) {
                str.append(", ");
            }
        }
        str.append(')');
        return str.toString();
    }

    public static String trimClassName(String name) {
        int idx = name.lastIndexOf(46);
        name = name.substring(idx + 1);
        idx = name.lastIndexOf(36);
        if (idx >= 0) {
            name = name.substring(idx + 1);
        }
        return name;
    }

    private static class GenericRef {

        private final Class<?> baseClass;

        private final Class<?> ifaceClass;

        Class<?> genericClass;

        public Type genericType;

        private int genericIndex;

        public GenericRef(Class<?> baseClass, Class<?> ifaceClass) {
            this.baseClass = baseClass;
            this.ifaceClass = ifaceClass;
        }

        public boolean needsUnwrap() {
            return this.genericClass == null && this.genericType != null && this.genericType instanceof TypeVariable;
        }

        public void setGenericFromType(Type type, int index) {
            this.genericType = type;
            this.genericIndex = index;
            if (type instanceof Class) {
                this.genericClass = (Class<?>) type;
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("GenericRef [baseClass=");
            builder.append(this.baseClass);
            builder.append(", ifaceClass=");
            builder.append(this.ifaceClass);
            builder.append(", genericType=");
            builder.append(this.genericType);
            builder.append(", genericClass=");
            builder.append(this.genericClass);
            builder.append("]");
            return builder.toString();
        }
    }
}