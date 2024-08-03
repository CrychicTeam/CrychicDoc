package moe.wolfgirl.probejs.lang.java.base;

import java.util.Collection;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;

public interface ClassPathProvider {

    Collection<ClassPath> getClassPaths();
}