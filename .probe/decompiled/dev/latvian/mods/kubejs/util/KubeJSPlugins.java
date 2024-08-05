package dev.latvian.mods.kubejs.util;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class KubeJSPlugins {

    private static final List<KubeJSPlugin> LIST = new ArrayList();

    private static final List<String> GLOBAL_CLASS_FILTER = new ArrayList();

    private static final ModResourceBindings BINDINGS = new ModResourceBindings();

    public static void load(List<Mod> mods, boolean loadClientPlugins) {
        try {
            for (Mod mod : mods) {
                load(mod, loadClientPlugins);
            }
        } catch (Exception var4) {
            throw new RuntimeException("Failed to load KubeJS plugin", var4);
        }
    }

    private static void load(Mod mod, boolean loadClientPlugins) throws IOException {
        Optional<Path> pp = mod.findResource("kubejs.plugins.txt");
        if (pp.isPresent()) {
            loadFromFile(Files.lines((Path) pp.get()), mod.getModId(), loadClientPlugins);
        }
        Optional<Path> pc = mod.findResource("kubejs.classfilter.txt");
        if (pc.isPresent()) {
            GLOBAL_CLASS_FILTER.addAll(Files.readAllLines((Path) pc.get()));
        }
        BINDINGS.readBindings(mod);
    }

    private static void loadFromFile(Stream<String> contents, String source, boolean loadClientPlugins) {
        KubeJS.LOGGER.info("Found plugin source {}", source);
        contents.map(s -> s.split("#", 2)[0].trim()).filter(s -> !s.isBlank()).flatMap(s -> {
            String[] line = s.split(" ");
            for (int i = 1; i < line.length; i++) {
                if (line[i].equalsIgnoreCase("client")) {
                    if (!loadClientPlugins) {
                        if (DevProperties.get().logSkippedPlugins) {
                            KubeJS.LOGGER.warn("Plugin " + line[0] + " does not load on server side, skipping");
                        }
                        return Stream.empty();
                    }
                } else if (!Platform.isModLoaded(line[i])) {
                    if (DevProperties.get().logSkippedPlugins) {
                        KubeJS.LOGGER.warn("Plugin " + line[0] + " does not have required mod " + line[i] + " loaded, skipping");
                    }
                    return Stream.empty();
                }
            }
            try {
                return Stream.of(Class.forName(line[0]));
            } catch (Throwable var5) {
                KubeJS.LOGGER.error("Failed to load plugin {} from source {}: {}", new Object[] { s, source, var5 });
                var5.printStackTrace();
                return Stream.empty();
            }
        }).filter(KubeJSPlugin.class::isAssignableFrom).forEach(c -> {
            try {
                LIST.add((KubeJSPlugin) c.getDeclaredConstructor().newInstance());
            } catch (Throwable var3) {
                KubeJS.LOGGER.error("Failed to init KubeJS plugin {} from source {}: {}", new Object[] { c.getName(), source, var3 });
            }
        });
    }

    public static ClassFilter createClassFilter(ScriptType type) {
        ClassFilter filter = new ClassFilter();
        for (KubeJSPlugin plugin : LIST) {
            plugin.registerClasses(type, filter);
        }
        for (String s : GLOBAL_CLASS_FILTER) {
            if (s.length() >= 2) {
                if (s.startsWith("+")) {
                    filter.allow(s.substring(1));
                } else if (s.startsWith("-")) {
                    filter.deny(s.substring(1));
                }
            }
        }
        return filter;
    }

    public static void forEachPlugin(Consumer<KubeJSPlugin> callback) {
        LIST.forEach(callback);
    }

    public static <T> void forEachPlugin(T instance, BiConsumer<KubeJSPlugin, T> callback) {
        for (KubeJSPlugin item : LIST) {
            callback.accept(item, instance);
        }
    }

    public static List<KubeJSPlugin> getAll() {
        return Collections.unmodifiableList(LIST);
    }

    public static void addSidedBindings(BindingsEvent event) {
        BINDINGS.addBindings(event);
    }
}