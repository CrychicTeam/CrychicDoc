package moe.wolfgirl.probejs.lang.typescript.code.member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import moe.wolfgirl.probejs.ProbeJS;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;

public class FieldDecl extends CommentableCode {

    public boolean isFinal = false;

    public boolean isStatic = false;

    public String name;

    public BaseType type;

    public FieldDecl(String name, BaseType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        return this.type.getUsedClassPaths();
    }

    @Override
    public List<String> formatRaw(Declaration declaration) {
        List<String> modifiers = new ArrayList();
        if (this.isStatic) {
            modifiers.add("static");
        }
        if (this.isFinal) {
            modifiers.add("readonly");
        }
        return List.of("%s %s: %s".formatted(String.join(" ", modifiers), ProbeJS.GSON.toJson(this.name), this.type.line(declaration, BaseType.FormatType.RETURN)));
    }
}