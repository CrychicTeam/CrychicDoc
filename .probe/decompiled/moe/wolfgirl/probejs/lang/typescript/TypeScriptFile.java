package moe.wolfgirl.probejs.lang.typescript;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import moe.wolfgirl.probejs.ProbeJS;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.code.Code;

public class TypeScriptFile {

    public final Declaration declaration = new Declaration();

    public final List<Code> codeList = new ArrayList();

    public final ClassPath classPath;

    public TypeScriptFile(ClassPath self) {
        if (self != null) {
            this.declaration.addClass(self);
        }
        this.classPath = self;
    }

    public void excludeSymbol(String name) {
        this.declaration.exclude(name);
    }

    public void addCode(Code code) {
        this.codeList.add(code);
        for (ClassPath usedClassPath : code.getUsedClassPaths()) {
            this.declaration.addClass(usedClassPath);
        }
    }

    public String format() {
        List<String> formatted = new ArrayList();
        for (Code code : this.codeList) {
            formatted.addAll(code.format(this.declaration));
        }
        return String.join("\n", formatted);
    }

    public void write(Path writeTo) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(writeTo);
        try {
            this.write(writer);
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

    public void write(BufferedWriter writer) throws IOException {
        boolean written = false;
        for (Reference value : this.declaration.references.values()) {
            if (!value.classPath().equals(this.classPath)) {
                writer.write(value.getImport() + "\n");
                written = true;
            }
        }
        if (!written) {
            writer.write("export {} // Mark the file as a module, do not remove unless there are other import/exports!");
        }
        writer.write("\n");
        writer.write(this.format());
    }

    public void writeAsModule(BufferedWriter writer) throws IOException {
        String modulePath = "packages/" + this.classPath.getTypeScriptPath();
        writer.write("declare module %s {\n".formatted(ProbeJS.GSON.toJson(modulePath)));
        this.write(writer);
        writer.write("}\n");
    }

    public <T extends Code> Optional<T> findCode(Class<T> type) {
        for (Code code : this.codeList) {
            if (type.isInstance(code)) {
                return Optional.of(code);
            }
        }
        return Optional.empty();
    }
}