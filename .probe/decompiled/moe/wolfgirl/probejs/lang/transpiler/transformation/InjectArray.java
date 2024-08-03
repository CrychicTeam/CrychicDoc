package moe.wolfgirl.probejs.lang.transpiler.transformation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.java.clazz.Clazz;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.Code;
import moe.wolfgirl.probejs.lang.typescript.code.member.ClassDecl;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSParamType;

public class InjectArray implements ClassTransformer {

    @Override
    public void transform(Clazz clazz, ClassDecl classDecl) {
        if (this.isDirectlyImplementing(clazz.original, Iterable.class)) {
            BaseType iterType = (BaseType) classDecl.methods.stream().filter(m -> m.name.equals("iterator")).filter(m -> m.returnType instanceof TSParamType).map(m -> (BaseType) ((TSParamType) m.returnType).params.get(0)).findFirst().orElse(null);
            if (iterType == null) {
                return;
            }
            classDecl.bodyCode.add(new InjectArray.FormattedLine("[Symbol.iterator](): IterableIterator<%s>;", iterType));
        }
        if (this.isDirectlyImplementing(clazz.original, List.class)) {
            BaseType iterType = (BaseType) classDecl.methods.stream().filter(m -> m.name.equals("iterator") && m.params.isEmpty()).filter(m -> m.returnType instanceof TSParamType).map(m -> (BaseType) ((TSParamType) m.returnType).params.get(0)).findFirst().orElse(null);
            if (iterType == null) {
                return;
            }
            classDecl.bodyCode.add(new InjectArray.FormattedLine("[index: number]: %s", iterType));
        }
        if (this.isDirectlyImplementing(clazz.original, Map.class)) {
            BaseType valueType = (BaseType) classDecl.methods.stream().filter(m -> m.name.equals("get") && m.params.size() == 1).map(m -> m.returnType).findFirst().orElse(null);
            if (valueType == null) {
                return;
            }
            classDecl.bodyCode.add(new InjectArray.FormattedLine("[index: string | number]: %s", valueType));
        }
    }

    private boolean isDirectlyImplementing(Class<?> toExamine, Class<?> target) {
        if (!target.isAssignableFrom(toExamine)) {
            return false;
        } else {
            Class<?> superClass = toExamine.getSuperclass();
            return superClass != null && superClass != Object.class ? !target.isAssignableFrom(superClass) : true;
        }
    }

    static class FormattedLine extends Code {

        private final String line;

        private final BaseType type;

        FormattedLine(String line, BaseType type) {
            this.line = line;
            this.type = type;
        }

        @Override
        public Collection<ClassPath> getUsedClassPaths() {
            return this.type.getUsedClassPaths();
        }

        @Override
        public List<String> format(Declaration declaration) {
            return List.of(this.line.formatted(this.type.line(declaration, BaseType.FormatType.RETURN)));
        }
    }
}