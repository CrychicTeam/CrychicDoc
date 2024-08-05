package moe.wolfgirl.probejs.lang.java.clazz;

import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.lang.reflect.TypeVariable;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.ClassRegistry;
import moe.wolfgirl.probejs.utils.RemapperUtils;

public record ClassPath(List<String> parts) {

    public ClassPath(String className) {
        this(Arrays.stream(className.split("\\.")).toList());
    }

    public ClassPath(Class<?> clazz) {
        this(transformJavaClass(clazz));
    }

    private static List<String> transformJavaClass(Class<?> clazz) {
        String name = RemapperUtils.getRemappedClassName(clazz);
        String[] parts = name.split("\\.");
        String className = "$" + parts[parts.length - 1];
        parts[parts.length - 1] = className;
        return Arrays.stream(parts).toList();
    }

    public String getName() {
        return (String) this.parts.get(this.parts.size() - 1);
    }

    public String getConcatenated(String sep) {
        return String.join(sep, this.parts);
    }

    public String getClassPath() {
        return this.getConcatenated(".");
    }

    public String getClassPathJava() {
        List<String> copy = new ArrayList(this.parts);
        String last = (String) copy.get(copy.size() - 1);
        if (last.startsWith("$")) {
            last = last.substring(1);
        }
        copy.set(copy.size() - 1, last);
        return String.join(".", copy);
    }

    public String getTypeScriptPath() {
        return this.getConcatenated("/");
    }

    @HideFromJS
    public Class<?> forName() throws ClassNotFoundException {
        return Class.forName(this.getClassPathJava());
    }

    public List<String> getGenerics() throws ClassNotFoundException {
        TypeVariable<?>[] variables = this.forName().getTypeParameters();
        return Arrays.stream(variables).map(TypeVariable::getName).toList();
    }

    @HideFromJS
    public Clazz toClazz() {
        return (Clazz) ClassRegistry.REGISTRY.foundClasses.get(this);
    }

    public List<String> getPackage() {
        List<String> classPath = new ArrayList(this.parts);
        classPath.remove(classPath.size() - 1);
        return classPath;
    }

    public String getConcatenatedPackage(String sep) {
        return String.join(sep, this.getPackage());
    }

    public Path getDirPath(Path base) {
        return base.resolve(this.getConcatenatedPackage("/"));
    }

    public Path makePath(Path base) {
        Path full = this.getDirPath(base);
        if (Files.notExists(full, new LinkOption[0])) {
            UtilsJS.tryIO(() -> Files.createDirectories(full));
        }
        return full;
    }
}