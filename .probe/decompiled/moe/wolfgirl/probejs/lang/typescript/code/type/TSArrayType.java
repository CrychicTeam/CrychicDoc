package moe.wolfgirl.probejs.lang.typescript.code.type;

import java.util.Collection;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;

public class TSArrayType extends BaseType {

    public BaseType component;

    public TSArrayType(BaseType component) {
        this.component = component;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        return this.component.getUsedClassPaths();
    }

    @Override
    public List<String> format(Declaration declaration, BaseType.FormatType input) {
        return List.of("(%s)[]".formatted(this.component.line(declaration, input)));
    }
}