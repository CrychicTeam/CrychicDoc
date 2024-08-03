package moe.wolfgirl.probejs.lang.typescript.code.type.js;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;

public class JSPrimitiveType extends BaseType {

    public final String content;

    public JSPrimitiveType(String content) {
        this.content = content;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        return List.of();
    }

    @Override
    public List<String> format(Declaration declaration, BaseType.FormatType input) {
        return List.of(this.content);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            JSPrimitiveType that = (JSPrimitiveType) o;
            return Objects.equals(this.content, that.content);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.content });
    }
}