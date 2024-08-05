package org.embeddedt.embeddium.taint.scanning;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import me.jellysquid.mods.sodium.mixin.MixinClassValidator;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;

public class TaintDetector {

    private static final String TAINT_MODE = System.getProperty("embeddium.taint_scan");

    private static final Logger LOGGER = LogManager.getLogger("Embeddium-TaintDetector");

    private static final ClassConstantPoolParser PARSER = new ClassConstantPoolParser("me/jellysquid/mods/sodium");

    private static final Multimap<ModFileInfo, TaintDetector.TargetingClass> DISCOVERED_MODS = ArrayListMultimap.create();

    private static final Set<String> EXCLUDED_MOD_IDS = ImmutableSet.of("embeddium");

    public static void init() {
        if (Objects.equals(TAINT_MODE, "true")) {
            Executor taintScanner = Executors.newSingleThreadExecutor(task -> {
                Thread worker = new Thread(task, "Embeddium Mod Analyzer");
                worker.setPriority(1);
                worker.setDaemon(true);
                return worker;
            });
            CompletableFuture.runAsync(() -> {
                Stopwatch watch = Stopwatch.createStarted();
                LOGGER.info("Scanning for mods that depend on Embeddium code...");
                scanMods();
                watch.stop();
                LOGGER.info("Finished scanning mods in {}", watch);
                presentResults();
            }, taintScanner);
        }
    }

    private static void scanMods() {
        List<Path> classPaths = new ArrayList();
        ModFileInfo self = FMLLoader.getLoadingModList().getModFileById("embeddium");
        Objects.requireNonNull(self, "Embeddium mod file does not exist");
        for (ModFileInfo file : FMLLoader.getLoadingModList().getModFiles()) {
            if (!file.getMods().stream().anyMatch(modInfo -> EXCLUDED_MOD_IDS.contains(modInfo.getModId()))) {
                classPaths.clear();
                file.getFile().scanFile(pathx -> {
                    if (pathx.getFileName().toString().endsWith(".class")) {
                        classPaths.add(pathx);
                    }
                });
                for (Path path : classPaths) {
                    try {
                        TaintDetector.TargetingClass clz = checkClass(path);
                        if (clz != null) {
                            DISCOVERED_MODS.put(file, clz);
                        }
                    } catch (RuntimeException | IOException var7) {
                        LOGGER.error("An error occured scanning class {}, it will be skipped: {}", path, var7);
                    }
                }
            }
        }
    }

    private static TaintDetector.TargetingClass checkClass(Path path) throws IOException {
        byte[] bytecode = Files.readAllBytes(path);
        if (!PARSER.find(bytecode, true)) {
            return null;
        } else {
            ClassNode node = MixinClassValidator.fromBytecode(bytecode);
            TaintDetector.TargetingClass targetingClass = new TaintDetector.TargetingClass();
            targetingClass.className = node.name;
            targetingClass.isMixin = MixinClassValidator.isMixinClass(node);
            return targetingClass;
        }
    }

    private static void presentResults() {
        if (!DISCOVERED_MODS.isEmpty()) {
            StringBuilder theResults = new StringBuilder();
            theResults.append(DISCOVERED_MODS.keySet().size()).append(" mods were found that reference Embeddium internals:\n");
            DISCOVERED_MODS.asMap().forEach((file, listClass) -> {
                theResults.append("Mod file '").append(file.getFile().getFileName()).append("' providing mods [").append((String) file.getMods().stream().map(IModInfo::getModId).collect(Collectors.joining(", "))).append("] with ").append(listClass.size()).append(" classes\n");
                Map<Boolean, List<TaintDetector.TargetingClass>> partitionedClasses = (Map<Boolean, List<TaintDetector.TargetingClass>>) listClass.stream().collect(Collectors.partitioningBy(t -> t.isMixin));
                for (Entry<Boolean, List<TaintDetector.TargetingClass>> partition : partitionedClasses.entrySet()) {
                    if (!((List) partition.getValue()).isEmpty()) {
                        theResults.append("|-- ");
                        theResults.append(partition.getKey() ? "mixin " : "non-mixin ");
                        theResults.append("\n");
                        for (TaintDetector.TargetingClass targetingClass : (List) partition.getValue()) {
                            theResults.append("    |-- ");
                            theResults.append(targetingClass.className);
                            theResults.append('\n');
                        }
                    }
                }
            });
            LOGGER.info(theResults);
        }
    }

    static class TargetingClass {

        String className;

        boolean isMixin;
    }
}