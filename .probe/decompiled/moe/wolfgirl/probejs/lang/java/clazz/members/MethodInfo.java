package moe.wolfgirl.probejs.lang.java.clazz.members;

import dev.latvian.mods.rhino.JavaMembers;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.java.base.ClassPathProvider;
import moe.wolfgirl.probejs.lang.java.base.TypeVariableHolder;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.java.type.TypeAdapter;
import moe.wolfgirl.probejs.lang.java.type.TypeDescriptor;
import moe.wolfgirl.probejs.lang.java.type.impl.VariableType;

public class MethodInfo extends TypeVariableHolder implements ClassPathProvider {

    public final String name;

    public final List<ParamInfo> params;

    public TypeDescriptor returnType;

    public final MethodInfo.MethodAttributes attributes;

    public MethodInfo(JavaMembers.MethodInfo methodInfo, Map<TypeVariable<?>, Type> remapper) {
        super(methodInfo.method.getTypeParameters(), methodInfo.method.getAnnotations());
        Method method = methodInfo.method;
        this.attributes = new MethodInfo.MethodAttributes(method);
        this.name = methodInfo.name;
        this.params = (List<ParamInfo>) Arrays.stream(method.getParameters()).map(ParamInfo::new).collect(Collectors.toList());
        this.returnType = TypeAdapter.getTypeDescription(method.getAnnotatedReturnType());
        for (Entry<TypeVariable<?>, Type> entry : remapper.entrySet()) {
            TypeVariable<?> symbol = (TypeVariable<?>) entry.getKey();
            TypeDescriptor replacement = TypeAdapter.getTypeDescription((Type) entry.getValue());
            for (ParamInfo param : this.params) {
                param.type = TypeAdapter.consolidateType(param.type, symbol.getName(), replacement);
            }
            this.returnType = TypeAdapter.consolidateType(this.returnType, symbol.getName(), replacement);
        }
    }

    @Override
    public Collection<ClassPath> getClassPaths() {
        Set<ClassPath> paths = new HashSet();
        for (ParamInfo param : this.params) {
            paths.addAll(param.getClassPaths());
        }
        paths.addAll(this.returnType.getClassPaths());
        for (VariableType variableType : this.variableTypes) {
            paths.addAll(variableType.getClassPaths());
        }
        return paths;
    }

    public static class MethodAttributes {

        public final boolean isStatic;

        public final boolean isDefault;

        public final boolean isAbstract;

        public MethodAttributes(Method method) {
            int modifiers = method.getModifiers();
            this.isStatic = Modifier.isStatic(modifiers);
            this.isDefault = method.isDefault();
            this.isAbstract = Modifier.isAbstract(modifiers);
        }
    }
}