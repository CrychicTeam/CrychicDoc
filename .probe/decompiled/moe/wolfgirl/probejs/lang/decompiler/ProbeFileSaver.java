package moe.wolfgirl.probejs.lang.decompiler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.decompiler.parser.ParsedDocument;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import org.jetbrains.java.decompiler.main.extern.IResultSaver;

public class ProbeFileSaver implements IResultSaver {

    public final Map<ClassPath, ParsedDocument> result = new HashMap();

    private Runnable callback;

    public int classCount = 0;

    public void saveFolder(String path) {
    }

    public void copyFile(String source, String path, String entryName) {
    }

    public void saveClassFile(String path, String qualifiedName, String entryName, String content, int[] mapping) {
        String[] parts = qualifiedName.split("/");
        parts[parts.length - 1] = "$" + parts[parts.length - 1];
        ClassPath classPath = new ClassPath(List.of(parts));
        this.result.put(classPath, new ParsedDocument(content));
        this.classCount++;
        if (this.callback != null) {
            this.callback.run();
        }
    }

    public void createArchive(String path, String archiveName, Manifest manifest) {
    }

    public void saveDirEntry(String path, String archiveName, String entryName) {
    }

    public void copyEntry(String source, String path, String archiveName, String entry) {
    }

    public void saveClassEntry(String path, String archiveName, String qualifiedName, String entryName, String content) {
        ClassPath classPath = new ClassPath(qualifiedName.replace("/", "."));
        this.result.put(classPath, new ParsedDocument(content));
        this.classCount++;
        if (this.callback != null) {
            this.callback.run();
        }
    }

    public void closeArchive(String path, String archiveName) {
    }

    public void writeTo(Path base) throws IOException {
        for (Entry<ClassPath, ParsedDocument> entry : this.result.entrySet()) {
            ClassPath classPath = (ClassPath) entry.getKey();
            ParsedDocument s = (ParsedDocument) entry.getValue();
            s.getParamTransformations();
            Path full = classPath.makePath(base);
            BufferedWriter out = Files.newBufferedWriter(full.resolve(classPath.getName() + ".java"));
            try {
                String[] lines = s.getCode().split("\\n");
                out.write((String) Arrays.stream(lines).filter(l -> !l.strip().startsWith("// $VF: renamed")).collect(Collectors.joining("\n")));
            } catch (Throwable var11) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Throwable var10) {
                        var11.addSuppressed(var10);
                    }
                }
                throw var11;
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet();
        for (Entry<ClassPath, ParsedDocument> entry : this.result.entrySet()) {
            ClassPath classPath = (ClassPath) entry.getKey();
            ParsedDocument parsedDocument = (ParsedDocument) entry.getValue();
            if (!parsedDocument.isMixinClass()) {
                try {
                    classes.add(classPath.forName());
                } catch (Throwable var7) {
                }
            }
        }
        return classes;
    }

    public ProbeFileSaver callback(Runnable callback) {
        this.callback = callback;
        return this;
    }
}