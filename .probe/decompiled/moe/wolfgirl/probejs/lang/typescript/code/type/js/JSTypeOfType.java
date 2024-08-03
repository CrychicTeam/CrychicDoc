package moe.wolfgirl.probejs.lang.typescript.code.type.js;

import java.util.Collection;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.java.clazz.Clazz;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSClassType;

public class JSTypeOfType extends BaseType {

    public final BaseType inner;

    private final boolean isInterface;

    public JSTypeOfType(BaseType inner) {
        this.inner = inner;
        Clazz clazz = inner instanceof TSClassType classType ? classType.classPath.toClazz() : null;
        this.isInterface = clazz != null && clazz.attribute.isInterface;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        return this.inner.getUsedClassPaths();
    }

    @Override
    public List<String> format(Declaration declaration, BaseType.FormatType input) {
        return List.of(this.isInterface ? this.inner.line(declaration, BaseType.FormatType.RETURN) : "typeof %s".formatted(this.inner.line(declaration, BaseType.FormatType.RETURN)));
    }
}