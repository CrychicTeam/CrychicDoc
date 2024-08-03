package moe.wolfgirl.probejs.lang.typescript.code.member;

import java.util.Collection;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;

public class TypeDecl extends CommentableCode {

    public BaseType type;

    public final String symbol;

    public TypeDecl(String symbol, BaseType type) {
        this.symbol = symbol;
        this.type = type;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        return this.type.getUsedClassPaths();
    }

    @Override
    public List<String> formatRaw(Declaration declaration) {
        return List.of("export type %s = %s;".formatted(this.symbol, this.type.line(declaration, BaseType.FormatType.INPUT)));
    }
}