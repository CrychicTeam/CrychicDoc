package moe.wolfgirl.probejs.lang.typescript.code.type;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;

public class CustomType extends BaseType {

    private final BiFunction<Declaration, BaseType.FormatType, String> formatter;

    private final ClassPath[] imports;

    public CustomType(BiFunction<Declaration, BaseType.FormatType, String> formatter, ClassPath[] imports) {
        this.formatter = formatter;
        this.imports = imports;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        return List.of(this.imports);
    }

    @Override
    public List<String> format(Declaration declaration, BaseType.FormatType input) {
        return Collections.singletonList((String) this.formatter.apply(declaration, input));
    }
}