package moe.wolfgirl.probejs.lang.typescript.code.type;

import java.util.Collection;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;

public class TSClassType extends BaseType {

    public ClassPath classPath;

    public TSClassType(ClassPath classPath) {
        this.classPath = classPath;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        return List.of(this.classPath);
    }

    @Override
    public List<String> format(Declaration declaration, BaseType.FormatType input) {
        return List.of(declaration.getSymbol(this.classPath, input == BaseType.FormatType.INPUT));
    }
}