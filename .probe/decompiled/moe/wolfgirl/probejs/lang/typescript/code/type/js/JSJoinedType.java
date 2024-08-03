package moe.wolfgirl.probejs.lang.typescript.code.type.js;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;

public abstract class JSJoinedType extends BaseType {

    public final String delimiter;

    public final List<BaseType> types;

    protected JSJoinedType(String delimiter, List<BaseType> types) {
        this.delimiter = " %s ".formatted(delimiter);
        this.types = types;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        Set<ClassPath> paths = new HashSet();
        for (BaseType type : this.types) {
            paths.addAll(type.getUsedClassPaths());
        }
        return paths;
    }

    @Override
    public List<String> format(Declaration declaration, BaseType.FormatType input) {
        return List.of((String) this.types.stream().map(type -> "(%s)".formatted(type.line(declaration, input))).collect(Collectors.joining(this.delimiter)));
    }

    public static class Intersection extends JSJoinedType {

        public Intersection(List<BaseType> types) {
            super("&", types);
        }
    }

    public static class Union extends JSJoinedType {

        public Union(List<BaseType> types) {
            super("|", types);
        }
    }
}