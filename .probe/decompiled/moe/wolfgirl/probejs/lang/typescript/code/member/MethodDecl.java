package moe.wolfgirl.probejs.lang.typescript.code.member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.ProbeJS;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSVariableType;

public class MethodDecl extends CommentableCode {

    public String name;

    public boolean isAbstract = false;

    public boolean isStatic = false;

    public boolean isInterface = false;

    public List<TSVariableType> variableTypes;

    public List<ParamDecl> params;

    public BaseType returnType;

    public String content = null;

    public MethodDecl(String name, List<TSVariableType> variableTypes, List<ParamDecl> params, BaseType returnType) {
        this.name = name;
        this.variableTypes = new ArrayList(variableTypes);
        this.params = new ArrayList(params);
        this.returnType = returnType;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        Set<ClassPath> paths = new HashSet(this.returnType.getUsedClassPaths());
        for (TSVariableType variableType : this.variableTypes) {
            paths.addAll(variableType.getUsedClassPaths());
        }
        for (ParamDecl param : this.params) {
            paths.addAll(param.type.getUsedClassPaths());
        }
        return paths;
    }

    @Override
    public List<String> formatRaw(Declaration declaration) {
        List<String> modifiers = new ArrayList();
        if (!this.isInterface) {
            modifiers.add("public");
        }
        if (this.isStatic) {
            modifiers.add("static");
        }
        String head = String.join(" ", modifiers);
        head = "%s %s".formatted(head, ProbeJS.GSON.toJson(this.name));
        if (this.variableTypes.size() != 0) {
            String variables = (String) this.variableTypes.stream().map(type -> type.line(declaration, BaseType.FormatType.VARIABLE)).collect(Collectors.joining(", "));
            head = "%s<%s>".formatted(head, variables);
        }
        String body = ParamDecl.formatParams(this.params, declaration);
        String tail = ": %s".formatted(this.returnType.line(declaration, BaseType.FormatType.RETURN));
        if (this.content != null) {
            tail = "%s {/** %s */}".formatted(tail, this.content);
        }
        return List.of("%s%s%s".formatted(head, body, tail));
    }
}