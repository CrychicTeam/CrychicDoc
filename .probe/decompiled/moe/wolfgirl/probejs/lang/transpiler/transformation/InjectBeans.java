package moe.wolfgirl.probejs.lang.transpiler.transformation;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import moe.wolfgirl.probejs.ProbeJS;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.java.clazz.Clazz;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.Code;
import moe.wolfgirl.probejs.lang.typescript.code.member.ClassDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.MethodDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.ParamDecl;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import moe.wolfgirl.probejs.utils.NameUtils;

public class InjectBeans implements ClassTransformer {

    @Override
    public void transform(Clazz clazz, ClassDecl classDecl) {
        Set<String> names = new HashSet();
        for (MethodDecl method : classDecl.methods) {
            names.add(method.name);
        }
        for (MethodDecl method : classDecl.methods) {
            if (method.name.startsWith("set") && method.params.size() == 1) {
                if (method.name.length() != 3) {
                    String beanName = NameUtils.firstLower(method.name.substring(3));
                    if (!names.contains(beanName)) {
                        classDecl.bodyCode.add(new InjectBeans.BeanDecl("set %s(value: %s)", beanName, Types.ignoreContext(((ParamDecl) method.params.get(0)).type, BaseType.FormatType.INPUT)));
                    }
                }
            } else if (method.params.isEmpty()) {
                if (method.name.startsWith("get")) {
                    if (method.name.length() != 3) {
                        String beanName = NameUtils.firstLower(method.name.substring(3));
                        if (!names.contains(beanName)) {
                            classDecl.bodyCode.add(new InjectBeans.BeanDecl("get %s(): %s", beanName, method.returnType));
                        }
                    }
                } else if (method.name.startsWith("is") && method.name.length() != 2) {
                    String beanName = NameUtils.firstLower(method.name.substring(2));
                    if (!names.contains(beanName)) {
                        classDecl.bodyCode.add(new InjectBeans.BeanDecl("get %s(): %s", beanName, Types.BOOLEAN));
                    }
                }
            }
        }
    }

    public static class BeanDecl extends Code {

        public String formattingString;

        public String name;

        public BaseType baseType;

        BeanDecl(String formattingString, String name, BaseType baseType) {
            this.formattingString = formattingString;
            this.name = name;
            this.baseType = baseType;
        }

        @Override
        public Collection<ClassPath> getUsedClassPaths() {
            return this.baseType.getUsedClassPaths();
        }

        @Override
        public List<String> format(Declaration declaration) {
            return List.of(this.formattingString.formatted(ProbeJS.GSON.toJson(this.name), this.baseType.line(declaration)));
        }
    }
}