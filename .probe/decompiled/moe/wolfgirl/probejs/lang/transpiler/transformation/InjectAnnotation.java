package moe.wolfgirl.probejs.lang.transpiler.transformation;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import java.util.ArrayList;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.base.AnnotationHolder;
import moe.wolfgirl.probejs.lang.java.clazz.Clazz;
import moe.wolfgirl.probejs.lang.java.clazz.members.ConstructorInfo;
import moe.wolfgirl.probejs.lang.java.clazz.members.FieldInfo;
import moe.wolfgirl.probejs.lang.java.clazz.members.MethodInfo;
import moe.wolfgirl.probejs.lang.typescript.code.member.ClassDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.CommentableCode;
import moe.wolfgirl.probejs.lang.typescript.code.member.ConstructorDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.FieldDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.MethodDecl;

public class InjectAnnotation implements ClassTransformer {

    @Override
    public void transform(Clazz clazz, ClassDecl classDecl) {
        this.applyInfo(clazz, classDecl);
        if (clazz.hasAnnotation(Deprecated.class)) {
            classDecl.newline(new String[] { "@deprecated" });
        }
    }

    @Override
    public void transformMethod(MethodInfo methodInfo, MethodDecl decl) {
        List<Param> params = this.applyInfo(methodInfo, decl);
        if (methodInfo.hasAnnotation(Deprecated.class)) {
            decl.newline(new String[] { "@deprecated" });
        }
        if (!params.isEmpty()) {
            decl.linebreak();
            for (Param param : params) {
                decl.addComment(new String[] { "@param %s - %s".formatted(param.name(), param.value()) });
            }
        }
    }

    @Override
    public void transformField(FieldInfo fieldInfo, FieldDecl decl) {
        this.applyInfo(fieldInfo, decl);
        if (fieldInfo.hasAnnotation(Deprecated.class)) {
            decl.newline(new String[] { "@deprecated" });
        }
    }

    @Override
    public void transformConstructor(ConstructorInfo constructorInfo, ConstructorDecl decl) {
        this.applyInfo(constructorInfo, decl);
        if (constructorInfo.hasAnnotation(Deprecated.class)) {
            decl.newline(new String[] { "@deprecated" });
        }
    }

    public List<Param> applyInfo(AnnotationHolder info, CommentableCode decl) {
        List<Param> params = new ArrayList();
        for (Info annotation : info.getAnnotations(Info.class)) {
            decl.addComment(annotation.value());
            params.addAll(List.of(annotation.params()));
        }
        return params;
    }
}