package moe.wolfgirl.probejs.lang.typescript.code.type;

import java.util.Collection;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;

public class ContextShield extends BaseType {

    private final BaseType inner;

    private final BaseType.FormatType formatType;

    public ContextShield(BaseType inner, BaseType.FormatType formatType) {
        this.inner = inner;
        this.formatType = formatType;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        return this.inner.getUsedClassPaths();
    }

    @Override
    public List<String> format(Declaration declaration, BaseType.FormatType input) {
        return this.inner.format(declaration, this.formatType);
    }
}