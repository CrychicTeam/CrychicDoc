package moe.wolfgirl.probejs.lang.java.clazz;

import dev.latvian.mods.rhino.util.HideFromJS;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.java.base.ClassPathProvider;
import moe.wolfgirl.probejs.lang.java.base.TypeVariableHolder;
import moe.wolfgirl.probejs.lang.java.clazz.members.ConstructorInfo;
import moe.wolfgirl.probejs.lang.java.clazz.members.FieldInfo;
import moe.wolfgirl.probejs.lang.java.clazz.members.MethodInfo;
import moe.wolfgirl.probejs.lang.java.clazz.members.ParamInfo;
import moe.wolfgirl.probejs.lang.java.type.TypeAdapter;
import moe.wolfgirl.probejs.lang.java.type.TypeDescriptor;
import moe.wolfgirl.probejs.lang.java.type.impl.VariableType;
import moe.wolfgirl.probejs.utils.RemapperUtils;
import org.jetbrains.annotations.Nullable;

public class Clazz extends TypeVariableHolder implements ClassPathProvider {

    @HideFromJS
    public final Class<?> original;

    public final ClassPath classPath;

    public final List<ConstructorInfo> constructors;

    public final List<FieldInfo> fields;

    public final List<MethodInfo> methods;

    @Nullable
    public final TypeDescriptor superClass;

    public final List<TypeDescriptor> interfaces;

    public final Clazz.ClassAttribute attribute;

    public Clazz(Class<?> clazz) {
        super(clazz.getTypeParameters(), clazz.getAnnotations());
        this.original = clazz;
        this.classPath = new ClassPath(clazz);
        this.constructors = (List<ConstructorInfo>) RemapperUtils.getConstructors(clazz).stream().map(ConstructorInfo::new).collect(Collectors.toList());
        this.fields = (List<FieldInfo>) RemapperUtils.getFields(clazz).stream().map(FieldInfo::new).collect(Collectors.toList());
        this.methods = (List<MethodInfo>) RemapperUtils.getMethods(clazz).stream().filter(m -> !m.method.isSynthetic()).filter(m -> !hasIdenticalParentMethodAndEnsureNotDirectlyImplementsInterfaceSinceTypeScriptDoesNotHaveInterfaceAtRuntimeInTypeDeclarationFilesJustBecauseItSucks(m.method, clazz)).map(method -> {
            Map<TypeVariable<?>, Type> replacement = getGenericTypeReplacementForParentInterfaceMethodsJustBecauseJavaDoNotKnowToReplaceThemWithGenericArgumentsOfThisClass(clazz, method.method);
            return new MethodInfo(method, replacement);
        }).collect(Collectors.toList());
        if (clazz.getSuperclass() != Object.class) {
            this.superClass = TypeAdapter.getTypeDescription(clazz.getAnnotatedSuperclass());
        } else {
            this.superClass = null;
        }
        this.interfaces = (List<TypeDescriptor>) Arrays.stream(clazz.getAnnotatedInterfaces()).map(TypeAdapter::getTypeDescription).collect(Collectors.toList());
        this.attribute = new Clazz.ClassAttribute(clazz);
    }

    @Override
    public Collection<ClassPath> getClassPaths() {
        Set<ClassPath> paths = new HashSet();
        for (ConstructorInfo constructor : this.constructors) {
            paths.addAll(constructor.getClassPaths());
        }
        for (FieldInfo field : this.fields) {
            paths.addAll(field.getClassPaths());
        }
        for (MethodInfo method : this.methods) {
            paths.addAll(method.getClassPaths());
        }
        if (this.superClass != null) {
            paths.addAll(this.superClass.getClassPaths());
        }
        for (TypeDescriptor i : this.interfaces) {
            paths.addAll(i.getClassPaths());
        }
        for (VariableType variableType : this.variableTypes) {
            paths.addAll(variableType.getClassPaths());
        }
        return paths;
    }

    public int hashCode() {
        return this.classPath.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Clazz clazz = (Clazz) o;
            return Objects.equals(this.classPath, clazz.classPath);
        } else {
            return false;
        }
    }

    public Set<ClassPath> getUsedClasses() {
        Set<ClassPath> used = new HashSet();
        for (MethodInfo method : this.methods) {
            used.addAll(method.returnType.getClassPaths());
            for (ParamInfo param : method.params) {
                used.addAll(param.type.getClassPaths());
            }
        }
        for (FieldInfo field : this.fields) {
            used.addAll(field.type.getClassPaths());
        }
        for (ConstructorInfo constructor : this.constructors) {
            for (ParamInfo param : constructor.params) {
                used.addAll(param.type.getClassPaths());
            }
        }
        if (this.superClass != null) {
            used.addAll(this.superClass.getClassPaths());
        }
        for (TypeDescriptor i : this.interfaces) {
            used.addAll(i.getClassPaths());
        }
        for (VariableType variableType : this.variableTypes) {
            used.addAll(variableType.getClassPaths());
        }
        return used;
    }

    private static boolean hasIdenticalParentMethodAndEnsureNotDirectlyImplementsInterfaceSinceTypeScriptDoesNotHaveInterfaceAtRuntimeInTypeDeclarationFilesJustBecauseItSucks(Method method, Class<?> clazz) {
        Class<?> parent = clazz.getSuperclass();
        if (parent == null) {
            return false;
        } else {
            while (parent != null && !parent.isInterface()) {
                try {
                    Method parentMethod = parent.getMethod(method.getName(), method.getParameterTypes());
                    return parentMethod.equals(method);
                } catch (NoSuchMethodException var4) {
                    parent = parent.getSuperclass();
                }
            }
            return false;
        }
    }

    private static Map<TypeVariable<?>, Type> getGenericTypeReplacementForParentInterfaceMethodsJustBecauseJavaDoNotKnowToReplaceThemWithGenericArgumentsOfThisClass(Class<?> thisClass, Method thatMethod) {
        Class<?> targetClass = thatMethod.getDeclaringClass();
        Map<TypeVariable<?>, Type> replacement = new HashMap();
        if (Arrays.stream(thisClass.getInterfaces()).noneMatch(c -> c.equals(targetClass))) {
            Class<?> superInterface = (Class<?>) Arrays.stream(thisClass.getInterfaces()).filter(targetClass::isAssignableFrom).findFirst().orElse(null);
            if (superInterface == null) {
                return Map.of();
            } else {
                Map<TypeVariable<?>, Type> parentType = getGenericTypeReplacementForParentInterfaceMethodsJustBecauseJavaDoNotKnowToReplaceThemWithGenericArgumentsOfThisClass(superInterface, thatMethod);
                Map<TypeVariable<?>, Type> parentReplacement = getInterfaceRemap(thisClass, superInterface);
                for (Entry<TypeVariable<?>, Type> entry : parentType.entrySet()) {
                    TypeVariable<?> variable = (TypeVariable<?>) entry.getKey();
                    Type type = (Type) entry.getValue();
                    replacement.put(variable, type instanceof TypeVariable<?> typeVariable ? (Type) parentReplacement.getOrDefault(typeVariable, typeVariable) : type);
                }
                return replacement;
            }
        } else {
            return getInterfaceRemap(thisClass, targetClass);
        }
    }

    private static Map<TypeVariable<?>, Type> getInterfaceRemap(Class<?> thisClass, Class<?> thatInterface) {
        Map<TypeVariable<?>, Type> replacement = new HashMap();
        int indexOfInterface = -1;
        for (Type type : thisClass.getGenericInterfaces()) {
            if (type instanceof ParameterizedType parameterizedType) {
                if (parameterizedType.getRawType().equals(thatInterface)) {
                    indexOfInterface = 0;
                    for (TypeVariable<?> typeVariable : thatInterface.getTypeParameters()) {
                        replacement.put(typeVariable, parameterizedType.getActualTypeArguments()[indexOfInterface]);
                        indexOfInterface++;
                    }
                }
            } else if (type instanceof Class<?> clazz && clazz.equals(thatInterface)) {
                indexOfInterface = 0;
                for (TypeVariable<?> typeVariable : thatInterface.getTypeParameters()) {
                    replacement.put(typeVariable, Object.class);
                }
            }
        }
        return indexOfInterface == -1 ? Map.of() : replacement;
    }

    public static class ClassAttribute {

        public final Clazz.ClassType type;

        public final boolean isAbstract;

        public final boolean isInterface;

        public final Class<?> raw;

        public ClassAttribute(Class<?> clazz) {
            if (clazz.isInterface()) {
                this.type = Clazz.ClassType.INTERFACE;
            } else if (clazz.isEnum()) {
                this.type = Clazz.ClassType.ENUM;
            } else if (clazz.isRecord()) {
                this.type = Clazz.ClassType.RECORD;
            } else {
                this.type = Clazz.ClassType.CLASS;
            }
            int modifiers = clazz.getModifiers();
            this.isAbstract = Modifier.isAbstract(modifiers);
            this.isInterface = this.type == Clazz.ClassType.INTERFACE;
            this.raw = clazz;
        }
    }

    public static enum ClassType {

        INTERFACE, ENUM, RECORD, CLASS
    }
}