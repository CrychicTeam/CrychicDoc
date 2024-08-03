package moe.wolfgirl.probejs.lang.decompiler.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import moe.wolfgirl.probejs.utils.NameUtils;

public class ParsedDocument {

    private static final JavaParser PARSER = new JavaParser();

    private static final String PARAM_SRG = "^p_[a-zA-Z0-9]+_$";

    private final CompilationUnit parsed;

    private final Map<String, String> paramRenames = new HashMap();

    public ParsedDocument(String content) {
        this.parsed = (CompilationUnit) PARSER.parse(content).getResult().orElseThrow();
    }

    public void getParamTransformations() {
        for (ClassOrInterfaceDeclaration classDecl : this.parsed.findAll(ClassOrInterfaceDeclaration.class)) {
            for (BodyDeclaration<?> member : classDecl.getMembers()) {
                if (member instanceof CallableDeclaration) {
                    CallableDeclaration<?> callable = (CallableDeclaration<?>) member;
                    int order = 0;
                    for (Parameter parameter : callable.getParameters()) {
                        if (parameter.getNameAsString().matches("^p_[a-zA-Z0-9]+_$")) {
                            String[] types = NameUtils.extractAlphabets(parameter.getTypeAsString());
                            this.paramRenames.put(parameter.getNameAsString(), "%s%s".formatted(NameUtils.asCamelCase(types), order));
                            order++;
                        }
                    }
                }
            }
        }
    }

    public String getCode() {
        String content = this.parsed.toString();
        for (Entry<String, String> entry : this.paramRenames.entrySet()) {
            String original = (String) entry.getKey();
            String renamed = (String) entry.getValue();
            content = content.replace(original, renamed);
        }
        return content;
    }

    public boolean isMixinClass() {
        for (ClassOrInterfaceDeclaration classDecl : this.parsed.findAll(ClassOrInterfaceDeclaration.class)) {
            for (AnnotationExpr annotation : classDecl.getAnnotations()) {
                if (annotation.getNameAsString().equals("Mixin")) {
                    return true;
                }
            }
        }
        return false;
    }
}