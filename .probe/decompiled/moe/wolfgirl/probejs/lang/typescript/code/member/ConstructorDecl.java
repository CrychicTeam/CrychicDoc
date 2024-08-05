package moe.wolfgirl.probejs.lang.typescript.code.member;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSVariableType;

public class ConstructorDecl extends CommentableCode {

    public final List<TSVariableType> variableTypes;

    public final List<ParamDecl> params;

    public String content = null;

    public ConstructorDecl(List<TSVariableType> variableTypes, List<ParamDecl> params) {
        this.variableTypes = variableTypes;
        this.params = params;
    }

    @Override
    public Collection<ClassPath> getUsedClassPaths() {
        Set<ClassPath> paths = new HashSet();
        for (TSVariableType variable : this.variableTypes) {
            paths.addAll(variable.getUsedClassPaths());
        }
        for (ParamDecl param : this.params) {
            paths.addAll(param.type.getUsedClassPaths());
        }
        return paths;
    }

    @Override
    public List<String> formatRaw(Declaration declaration) {
        String head = "constructor";
        if (this.variableTypes.size() != 0) {
            String variables = (String) this.variableTypes.stream().map(type -> type.line(declaration, BaseType.FormatType.VARIABLE)).collect(Collectors.joining(", "));
            head = "%s<%s>".formatted(head, variables);
        }
        String body = ParamDecl.formatParams(this.params, declaration);
        String tail = "";
        if (this.content != null) {
            tail = "%s {/** %s */}".formatted(tail, this.content);
        }
        return List.of("%s%s%s".formatted(head, body, tail));
    }
}