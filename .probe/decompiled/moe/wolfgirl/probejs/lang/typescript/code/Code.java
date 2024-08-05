package moe.wolfgirl.probejs.lang.typescript.code;

import java.util.Collection;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.Declaration;

public abstract class Code {

    public abstract Collection<ClassPath> getUsedClassPaths();

    public abstract List<String> format(Declaration declaration);

    public String line(Declaration declaration) {
        return (String) this.format(declaration).get(0);
    }
}