package moe.wolfgirl.probejs.lang.java;

import dev.latvian.mods.rhino.util.HideFromJS;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.java.clazz.Clazz;
import moe.wolfgirl.probejs.lang.java.clazz.members.ConstructorInfo;
import moe.wolfgirl.probejs.lang.java.clazz.members.FieldInfo;
import moe.wolfgirl.probejs.lang.java.clazz.members.MethodInfo;
import moe.wolfgirl.probejs.lang.java.clazz.members.ParamInfo;
import moe.wolfgirl.probejs.lang.java.type.TypeDescriptor;
import moe.wolfgirl.probejs.lang.java.type.impl.VariableType;

@HideFromJS
public class ClassRegistry {

    public static final ClassRegistry REGISTRY = new ClassRegistry();

    public Map<ClassPath, Clazz> foundClasses = new HashMap();

    public void fromPackage(Collection<ClassPath> classPaths) {
        for (ClassPath pack : classPaths) {
            if (!this.foundClasses.containsKey(pack)) {
                this.foundClasses.put(pack, pack.toClazz());
            }
        }
    }

    public void fromClazz(Collection<Clazz> classes) {
        for (Clazz c : classes) {
            if (!this.foundClasses.containsKey(c.classPath)) {
                this.foundClasses.put(c.classPath, c);
            }
        }
    }

    public void fromClasses(Collection<Class<?>> classes) {
        for (Class<?> c : classes) {
            if (!c.isSynthetic() && !c.isAnonymousClass() && !this.foundClasses.containsKey(new ClassPath(c))) {
                try {
                    Clazz clazz = new Clazz(c);
                    this.foundClasses.put(clazz.classPath, clazz);
                } catch (Throwable var5) {
                }
            }
        }
    }

    private Set<Class<?>> retrieveClass(Clazz clazz) {
        Set<Class<?>> classes = new HashSet();
        for (ConstructorInfo constructor : clazz.constructors) {
            for (ParamInfo param : constructor.params) {
                classes.addAll(param.type.getClasses());
            }
            for (VariableType variableType : constructor.variableTypes) {
                classes.addAll(variableType.getClasses());
            }
        }
        for (MethodInfo method : clazz.methods) {
            for (ParamInfo param : method.params) {
                classes.addAll(param.type.getClasses());
            }
            for (VariableType variableType : method.variableTypes) {
                classes.addAll(variableType.getClasses());
            }
            classes.addAll(method.returnType.getClasses());
        }
        for (FieldInfo field : clazz.fields) {
            classes.addAll(field.type.getClasses());
        }
        for (VariableType variableType : clazz.variableTypes) {
            classes.addAll(variableType.getClasses());
        }
        if (clazz.superClass != null) {
            classes.addAll(clazz.superClass.getClasses());
        }
        for (TypeDescriptor i : clazz.interfaces) {
            classes.addAll(i.getClasses());
        }
        return classes;
    }

    public void discoverClasses() {
        Set<Clazz> currentClasses = new HashSet(this.foundClasses.values());
        while (!currentClasses.isEmpty()) {
            Set<Class<?>> fetchedClass = new HashSet();
            for (Clazz currentClass : currentClasses) {
                fetchedClass.addAll(this.retrieveClass(currentClass));
            }
            fetchedClass.removeIf(clazzx -> this.foundClasses.containsKey(new ClassPath(clazzx)));
            currentClasses.clear();
            for (Class<?> c : fetchedClass) {
                try {
                    Clazz clazz = new Clazz(c);
                    this.foundClasses.put(clazz.classPath, clazz);
                    currentClasses.add(clazz);
                } catch (Throwable var6) {
                }
            }
        }
    }

    public Collection<Clazz> getFoundClasses() {
        return this.foundClasses.values();
    }

    public void writeTo(Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path);
        try {
            for (ClassPath classPath : this.foundClasses.keySet()) {
                writer.write(classPath.getClassPathJava() + "\n");
            }
        } catch (Throwable var6) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }
            throw var6;
        }
        if (writer != null) {
            writer.close();
        }
    }

    public void loadFrom(Path path) {
        try {
            BufferedReader reader = Files.newBufferedReader(path);
            try {
                for (String className : reader.lines()::iterator) {
                    try {
                        Class<?> loaded = Class.forName(className);
                        this.fromClasses(Collections.singleton(loaded));
                    } catch (Throwable var7) {
                    }
                }
            } catch (Throwable var8) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var6) {
                        var8.addSuppressed(var6);
                    }
                }
                throw var8;
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException var9) {
        }
    }
}