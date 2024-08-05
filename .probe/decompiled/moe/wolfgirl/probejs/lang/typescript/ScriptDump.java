package moe.wolfgirl.probejs.lang.typescript;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.Supplier;
import moe.wolfgirl.probejs.ProbeJS;
import moe.wolfgirl.probejs.ProbePaths;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.java.clazz.Clazz;
import moe.wolfgirl.probejs.lang.transpiler.Transpiler;
import moe.wolfgirl.probejs.lang.typescript.code.Code;
import moe.wolfgirl.probejs.lang.typescript.code.member.ClassDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.TypeDecl;
import moe.wolfgirl.probejs.lang.typescript.code.ts.Wrapped;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSJoinedType;
import moe.wolfgirl.probejs.plugin.ProbeJSPlugin;
import moe.wolfgirl.probejs.utils.JsonUtils;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.FileUtils;

public class ScriptDump {

    public static final Supplier<ScriptDump> SERVER_DUMP = () -> new ScriptDump(ServerScriptManager.getScriptManager(), ProbePaths.PROBE.resolve("server"), KubeJSPaths.SERVER_SCRIPTS, clazz -> {
        for (OnlyIn annotation : clazz.getAnnotations(OnlyIn.class)) {
            if (annotation.value().isClient()) {
                return false;
            }
        }
        return true;
    });

    public static final Supplier<ScriptDump> CLIENT_DUMP = () -> new ScriptDump(KubeJS.getClientScriptManager(), ProbePaths.PROBE.resolve("client"), KubeJSPaths.CLIENT_SCRIPTS, clazz -> {
        for (OnlyIn annotation : clazz.getAnnotations(OnlyIn.class)) {
            if (annotation.value().isDedicatedServer()) {
                return false;
            }
        }
        return true;
    });

    public static final Supplier<ScriptDump> STARTUP_DUMP = () -> new ScriptDump(KubeJS.getStartupScriptManager(), ProbePaths.PROBE.resolve("startup"), KubeJSPaths.STARTUP_SCRIPTS, clazz -> true);

    public final ScriptType scriptType;

    public final ScriptManager manager;

    public final Path basePath;

    public final Path scriptPath;

    public final Map<String, Pair<Collection<String>, Wrapped.Global>> globals;

    public final Transpiler transpiler;

    public final Set<Clazz> recordedClasses = new HashSet();

    private final Predicate<Clazz> accept;

    private final Multimap<ClassPath, BaseType> convertibles = ArrayListMultimap.create();

    public int dumped = 0;

    public int total = 0;

    public ScriptDump(ScriptManager manager, Path basePath, Path scriptPath, Predicate<Clazz> scriptPredicate) {
        this.scriptType = manager.scriptType;
        this.manager = manager;
        this.basePath = basePath;
        this.scriptPath = scriptPath;
        this.transpiler = new Transpiler(manager);
        this.globals = new HashMap();
        this.accept = scriptPredicate;
    }

    public void acceptClasses(Collection<Clazz> classes) {
        for (Clazz clazz : classes) {
            if (this.accept.test(clazz)) {
                this.recordedClasses.add(clazz);
            }
        }
    }

    public Set<Class<?>> retrieveClasses() {
        Set<Class<?>> classes = new HashSet();
        ProbeJSPlugin.forEachPlugin(plugin -> classes.addAll(plugin.provideJavaClass(this)));
        return classes;
    }

    public void assignType(Class<?> classPath, BaseType type) {
        this.assignType(new ClassPath(classPath), type);
    }

    public void assignType(ClassPath classPath, BaseType type) {
        this.convertibles.put(classPath, type);
    }

    public void addGlobal(String identifier, Code... content) {
        this.addGlobal(identifier, List.of(), content);
    }

    public void addGlobal(String identifier, Collection<String> excludedNames, Code... content) {
        Wrapped.Global global = new Wrapped.Global();
        for (Code code : content) {
            global.addCode(code);
        }
        this.globals.put(identifier, new Pair(excludedNames, global));
    }

    public Path ensurePath(String path) {
        return this.ensurePath(path, false);
    }

    public Path ensurePath(String path, boolean script) {
        Path full = (script ? this.scriptPath : this.basePath).resolve(path);
        if (Files.notExists(full, new LinkOption[0])) {
            UtilsJS.tryIO(() -> Files.createDirectories(full));
        }
        return full;
    }

    public Path getTypeFolder() {
        return this.ensurePath("probe-types");
    }

    public Path getPackageFolder() {
        return this.ensurePath("probe-types/packages");
    }

    public Path getGlobalFolder() {
        return this.ensurePath("probe-types/global");
    }

    public Path getSource() {
        return this.ensurePath("src", true);
    }

    public void dumpClasses() throws IOException {
        this.dumped = 0;
        this.total = 0;
        ProbeJSPlugin.forEachPlugin(plugin -> plugin.assignType(this));
        Map<String, BufferedWriter> files = new HashMap();
        Map<ClassPath, TypeScriptFile> globalClasses = this.transpiler.dump(this.recordedClasses);
        ProbeJSPlugin.forEachPlugin(plugin -> plugin.modifyClasses(this, globalClasses));
        this.total = globalClasses.size();
        for (Entry<ClassPath, TypeScriptFile> entry : globalClasses.entrySet()) {
            try {
                ClassPath classPath = (ClassPath) entry.getKey();
                TypeScriptFile output = (TypeScriptFile) entry.getValue();
                ClassDecl classDecl = (ClassDecl) output.findCode(ClassDecl.class).orElse(null);
                if (classDecl != null) {
                    String symbol = classPath.getName() + "_";
                    String exportedSymbol = "%s$Type".formatted(classPath.getName());
                    BaseType exportedType = Types.type(classPath);
                    BaseType thisType = Types.type(classPath);
                    List<String> generics = classDecl.variableTypes.stream().map(v -> v.symbol).toList();
                    if (generics.size() != 0) {
                        String suffix = "<%s>".formatted(String.join(", ", generics));
                        symbol = symbol + suffix;
                        exportedSymbol = exportedSymbol + suffix;
                        thisType = Types.parameterized(thisType, (BaseType[]) generics.stream().map(Types::generic).toArray(BaseType[]::new));
                        exportedType = Types.parameterized(exportedType, (BaseType[]) generics.stream().map(Types::generic).toArray(BaseType[]::new));
                    }
                    exportedType = Types.ignoreContext(exportedType, BaseType.FormatType.INPUT);
                    thisType = Types.ignoreContext(thisType, BaseType.FormatType.RETURN);
                    List<BaseType> allTypes = new ArrayList(this.convertibles.get(classPath));
                    allTypes.add(thisType);
                    TypeDecl convertibleType = new TypeDecl(exportedSymbol, new JSJoinedType.Union(allTypes));
                    TypeDecl globalType = new TypeDecl(symbol, exportedType);
                    Wrapped.Global typeExport = new Wrapped.Global();
                    typeExport.addCode(globalType);
                    convertibleType.addComment(new String[] { "Class-specific type exported by ProbeJS, use global Type_\ntypes for convenience unless there's a naming conflict.\n" });
                    typeExport.addComment(new String[] { "Global type exported for convenience, use class-specific\ntypes if there's a naming conflict.\n" });
                    output.addCode(convertibleType);
                    output.addCode(typeExport);
                    String fileKey = "%s.%s".formatted(classPath.parts().get(0), classPath.parts().get(1));
                    BufferedWriter writer = (BufferedWriter) files.computeIfAbsent(fileKey, keyx -> {
                        try {
                            return Files.newBufferedWriter(this.getPackageFolder().resolve(keyx + ".d.ts"));
                        } catch (IOException var3) {
                            ProbeJS.LOGGER.error("Failed to write %s.d.ts".formatted(keyx));
                            return null;
                        }
                    });
                    if (writer != null) {
                        output.writeAsModule(writer);
                    }
                    this.dumped++;
                }
            } catch (Throwable var20) {
                var20.printStackTrace();
            }
        }
        BufferedWriter writer = Files.newBufferedWriter(this.getPackageFolder().resolve("index.d.ts"));
        try {
            for (Entry<String, BufferedWriter> entry : files.entrySet()) {
                String key = (String) entry.getKey();
                BufferedWriter value = (BufferedWriter) entry.getValue();
                writer.write("/// <reference path=%s />\n".formatted(ProbeJS.GSON.toJson(key + ".d.ts")));
                value.close();
            }
        } catch (Throwable var21) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Throwable var19) {
                    var21.addSuppressed(var19);
                }
            }
            throw var21;
        }
        if (writer != null) {
            writer.close();
        }
    }

    public void dumpGlobal() throws IOException {
        ProbeJSPlugin.forEachPlugin(plugin -> plugin.addGlobals(this));
        BufferedWriter writer = Files.newBufferedWriter(this.getGlobalFolder().resolve("index.d.ts"));
        try {
            for (Entry<String, Pair<Collection<String>, Wrapped.Global>> entry : this.globals.entrySet()) {
                String identifier = (String) entry.getKey();
                Pair<Collection<String>, Wrapped.Global> pair = (Pair<Collection<String>, Wrapped.Global>) entry.getValue();
                Wrapped.Global global = (Wrapped.Global) pair.getSecond();
                Collection<String> excluded = (Collection<String>) pair.getFirst();
                TypeScriptFile globalFile = new TypeScriptFile(null);
                for (String s : excluded) {
                    globalFile.excludeSymbol(s);
                }
                globalFile.addCode(global);
                globalFile.write(this.getGlobalFolder().resolve(identifier + ".d.ts"));
                writer.write("export * from %s\n".formatted(ProbeJS.GSON.toJson("./" + identifier)));
            }
        } catch (Throwable var12) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Throwable var11) {
                    var12.addSuppressed(var11);
                }
            }
            throw var12;
        }
        if (writer != null) {
            writer.close();
        }
    }

    public void dumpJSConfig() throws IOException {
        writeMergedConfig(this.scriptPath.resolve("jsconfig.json"), "{\n    \"compilerOptions\": {\n        \"module\": \"commonjs\",\n        \"target\": \"ES2015\",\n        \"lib\": [\n            \"ES5\",\n            \"ES2015\"\n        ],\n        \"rootDir\": \"./src\",\n        \"typeRoots\": [\n            \"../../.probe/%s/probe-types\"\n        ],\n        \"baseUrl\": \"../../.probe/%s/probe-types\",\n        \"skipLibCheck\": true\n    },\n    \"include\": [\n        \"./src/**/*\",\n    ]\n}\n".formatted(this.basePath.getFileName(), this.basePath.getFileName()));
    }

    public void removeClasses() throws IOException {
        FileUtils.deleteDirectory(this.getTypeFolder().toFile());
    }

    public void dump() throws IOException, ClassNotFoundException {
        this.getSource();
        this.dumpClasses();
        this.dumpGlobal();
        this.dumpJSConfig();
    }

    private static void write(Path writeTo, String content) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(writeTo);
        try {
            writer.write(content);
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

    private static void writeMergedConfig(Path path, String config) throws IOException {
        JsonObject updates = (JsonObject) ProbeJS.GSON.fromJson(config, JsonObject.class);
        JsonObject read = Files.exists(path, new LinkOption[0]) ? (JsonObject) ProbeJS.GSON.fromJson(Files.newBufferedReader(path), JsonObject.class) : new JsonObject();
        if (read == null) {
            read = new JsonObject();
        }
        JsonObject original = (JsonObject) JsonUtils.mergeJsonRecursively(read, updates);
        JsonWriter jsonWriter = ProbeJS.GSON_WRITER.newJsonWriter(Files.newBufferedWriter(path));
        jsonWriter.setIndent("    ");
        ProbeJS.GSON_WRITER.toJson(original, JsonObject.class, jsonWriter);
        jsonWriter.close();
    }
}