package moe.wolfgirl.probejs.lang.java.clazz.members;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.java.base.ClassPathProvider;
import moe.wolfgirl.probejs.lang.java.base.TypeVariableHolder;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.java.type.impl.VariableType;

public class ConstructorInfo extends TypeVariableHolder implements ClassPathProvider {

    public final List<ParamInfo> params;

    public ConstructorInfo(Constructor<?> constructor) {
        super(constructor.getTypeParameters(), constructor.getAnnotations());
        this.params = (List<ParamInfo>) Arrays.stream(constructor.getParameters()).map(ParamInfo::new).collect(Collectors.toList());
    }

    @Override
    public Collection<ClassPath> getClassPaths() {
        Set<ClassPath> paths = new HashSet();
        for (ParamInfo param : this.params) {
            paths.addAll(param.getClassPaths());
        }
        for (VariableType variableType : this.variableTypes) {
            paths.addAll(variableType.getClassPaths());
        }
        return paths;
    }
}