package moe.wolfgirl.probejs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.decompiler.ProbeDecompiler;
import moe.wolfgirl.probejs.lang.java.ClassRegistry;
import moe.wolfgirl.probejs.lang.snippet.SnippetDump;
import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.utils.GameUtils;
import net.minecraft.network.chat.Component;

public class ProbeDump {

    public static final Path SNIPPET_PATH = ProbePaths.WORKSPACE_SETTINGS.resolve("probe.code-snippets");

    public static final Path CLASS_CACHE = ProbePaths.PROBE.resolve("classes.txt");

    final SnippetDump snippetDump = new SnippetDump();

    final Collection<ScriptDump> scriptDumps = new ArrayList();

    final ProbeDecompiler decompiler = new ProbeDecompiler();

    private Consumer<Component> progressReport;

    public void addScript(ScriptDump dump) {
        this.scriptDumps.add(dump);
    }

    public void defaultScripts() {
        this.addScript((ScriptDump) ScriptDump.CLIENT_DUMP.get());
        this.addScript((ScriptDump) ScriptDump.SERVER_DUMP.get());
        this.addScript((ScriptDump) ScriptDump.STARTUP_DUMP.get());
    }

    private void onModChange() throws IOException {
        if (ProbeConfig.INSTANCE.enableDecompiler.get()) {
            this.report(Component.translatable("probejs.dump.decompiling").kjs$gold());
            this.decompiler.fromMods();
            this.decompiler.resultSaver.callback(() -> {
                if (this.decompiler.resultSaver.classCount % 3000 == 0) {
                    this.report(Component.translatable("probejs.dump.decompiled_x_class", this.decompiler.resultSaver.classCount));
                }
            });
            this.decompiler.decompileContext();
            this.decompiler.resultSaver.writeTo(ProbePaths.DECOMPILED);
            ClassRegistry.REGISTRY.fromClasses(this.decompiler.resultSaver.getClasses());
        }
        this.report(Component.translatable("probejs.dump.cleaning"));
        for (ScriptDump scriptDump : this.scriptDumps) {
            scriptDump.removeClasses();
            this.report(Component.translatable("probejs.removed_script", scriptDump.manager.scriptType.toString()));
        }
    }

    private void onRegistryChange() throws IOException {
    }

    private void report(Component component) {
        if (this.progressReport != null) {
            this.progressReport.accept(component);
        }
    }

    public void trigger(Consumer<Component> p) throws IOException {
        this.progressReport = p;
        this.report(Component.translatable("probejs.dump.start").kjs$green());
        this.snippetDump.fromDocs();
        this.snippetDump.writeTo(SNIPPET_PATH);
        this.report(Component.translatable("probejs.dump.snippets_generated"));
        if (GameUtils.modHash() != ProbeConfig.INSTANCE.modHash.get()) {
            this.report(Component.translatable("probejs.dump.mod_changed").kjs$aqua());
            this.onModChange();
            ProbeConfig.INSTANCE.modHash.set(GameUtils.modHash());
        }
        if (GameUtils.registryHash() != ProbeConfig.INSTANCE.registryHash.get()) {
            this.onRegistryChange();
            ProbeConfig.INSTANCE.registryHash.set(GameUtils.registryHash());
        }
        ClassRegistry.REGISTRY.loadFrom(CLASS_CACHE);
        for (ScriptDump scriptDump : this.scriptDumps) {
            ClassRegistry.REGISTRY.fromClasses(scriptDump.retrieveClasses());
        }
        ClassRegistry.REGISTRY.discoverClasses();
        ClassRegistry.REGISTRY.writeTo(CLASS_CACHE);
        this.report(Component.translatable("probejs.dump.class_discovered", ClassRegistry.REGISTRY.foundClasses.keySet().size()));
        List<Thread> dumpThreads = new ArrayList();
        for (ScriptDump scriptDump : this.scriptDumps) {
            Thread t = new Thread(() -> {
                scriptDump.acceptClasses(ClassRegistry.REGISTRY.getFoundClasses());
                try {
                    scriptDump.dump();
                    this.report(Component.translatable("probejs.dump.dump_finished", scriptDump.manager.scriptType.toString()).kjs$green());
                } catch (Throwable var3x) {
                    this.report(Component.translatable("probejs.dump.dump_error", scriptDump.manager.scriptType.toString()).kjs$red());
                    throw new RuntimeException(var3x);
                }
            });
            t.start();
            dumpThreads.add(t);
        }
        Thread reportingThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000L);
                    if (dumpThreads.stream().noneMatch(Thread::isAlive)) {
                        return;
                    }
                    String dumpProgress = (String) this.scriptDumps.stream().filter(sd -> sd.total != 0).map(sd -> "%s/%s".formatted(sd.dumped, sd.total)).collect(Collectors.joining(", "));
                    this.report(Component.translatable("probejs.dump.report_progress").append(Component.literal(dumpProgress).kjs$blue()));
                } catch (InterruptedException var3x) {
                    throw new RuntimeException(var3x);
                }
            }
        });
        reportingThread.start();
    }

    public void cleanup(Consumer<Component> p) throws IOException {
        Files.deleteIfExists(SNIPPET_PATH);
        for (ScriptDump scriptDump : this.scriptDumps) {
            scriptDump.removeClasses();
            p.accept(Component.translatable("probejs.removed_script", scriptDump.manager.scriptType.toString()));
        }
    }
}