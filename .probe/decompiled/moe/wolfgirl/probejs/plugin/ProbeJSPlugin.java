package moe.wolfgirl.probejs.plugin;

import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.snippet.SnippetDump;
import moe.wolfgirl.probejs.lang.transpiler.Transpiler;
import moe.wolfgirl.probejs.lang.transpiler.TypeConverter;
import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.TypeScriptFile;

public class ProbeJSPlugin extends KubeJSPlugin {

    @HideFromJS
    public static void forEachPlugin(Consumer<ProbeJSPlugin> consumer) {
        KubeJSPlugins.forEachPlugin(plugin -> {
            if (plugin instanceof ProbeJSPlugin probePlugin) {
                consumer.accept(probePlugin);
            }
        });
    }

    public void addPredefinedTypes(TypeConverter converter) {
    }

    public void denyTypes(Transpiler transpiler) {
    }

    public void modifyClasses(ScriptDump scriptDump, Map<ClassPath, TypeScriptFile> globalClasses) {
    }

    public void addGlobals(ScriptDump scriptDump) {
    }

    public void assignType(ScriptDump scriptDump) {
    }

    @HideFromJS
    public Set<Class<?>> provideJavaClass(ScriptDump scriptDump) {
        return Set.of();
    }

    public Set<Pair<String, String>> disableEventDumps(ScriptDump dump) {
        return Set.of();
    }

    public void addVSCodeSnippets(SnippetDump dump) {
    }
}