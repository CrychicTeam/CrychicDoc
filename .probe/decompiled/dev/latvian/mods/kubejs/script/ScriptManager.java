package dev.latvian.mods.kubejs.script;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.platform.MiscPlatformHelper;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ClassFilter;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import dev.latvian.mods.kubejs.util.LogType;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.ClassShutter;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaClass;
import dev.latvian.mods.rhino.NativeObject;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.mod.util.MinecraftRemapper;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;

public class ScriptManager implements ClassShutter {

    private static final ThreadLocal<Context> CURRENT_CONTEXT = new ThreadLocal();

    public final ScriptType scriptType;

    public final Map<String, ScriptPack> packs;

    private final ClassFilter classFilter;

    public boolean firstLoad;

    public Context context;

    public Scriptable topLevelScope;

    private Map<String, Either<NativeJavaClass, Boolean>> javaClassCache;

    public boolean canListenEvents;

    @Nullable
    public static Context getCurrentContext() {
        return (Context) CURRENT_CONTEXT.get();
    }

    public ScriptManager(ScriptType t) {
        this.scriptType = t;
        this.packs = new LinkedHashMap();
        this.firstLoad = true;
        this.classFilter = KubeJSPlugins.createClassFilter(this.scriptType);
    }

    public void unload() {
        this.packs.clear();
        this.scriptType.unload();
        this.javaClassCache = null;
    }

    public void reload(@Nullable ResourceManager resourceManager) {
        KubeJSPlugins.forEachPlugin(KubeJSPlugin::clearCaches);
        this.unload();
        this.scriptType.console.writeToFile(LogType.INIT, "KubeJS " + KubeJS.thisMod.getVersion() + "; MC 2001 " + PlatformWrapper.getName());
        this.scriptType.console.writeToFile(LogType.INIT, "Loaded plugins:");
        for (KubeJSPlugin plugin : KubeJSPlugins.getAll()) {
            this.scriptType.console.writeToFile(LogType.INIT, "- " + plugin.getClass().getName());
        }
        this.loadFromDirectory();
        if (resourceManager != null) {
            this.loadFromResources(resourceManager);
        }
        this.load();
    }

    private void loadFile(ScriptPack pack, ScriptFileInfo fileInfo, ScriptSource source) {
        try {
            fileInfo.preload(source);
            String skip = fileInfo.skipLoading();
            if (skip.isEmpty()) {
                pack.scripts.add(new ScriptFile(pack, fileInfo, source));
            } else {
                this.scriptType.console.info("Skipped " + fileInfo.location + ": " + skip);
            }
        } catch (Throwable var5) {
            this.scriptType.console.error("Failed to pre-load script file '" + fileInfo.location + "'", var5);
        }
    }

    private void loadFromResources(ResourceManager resourceManager) {
        Map<String, List<ResourceLocation>> packMap = new HashMap();
        for (ResourceLocation resource : resourceManager.listResources("kubejs", s -> s.getPath().endsWith(".js") || s.getPath().endsWith(".ts") && !s.getPath().endsWith(".d.ts")).keySet()) {
            ((List) packMap.computeIfAbsent(resource.getNamespace(), s -> new ArrayList())).add(resource);
        }
        for (Entry<String, List<ResourceLocation>> entry : packMap.entrySet()) {
            ScriptPack pack = new ScriptPack(this, new ScriptPackInfo((String) entry.getKey(), "kubejs/"));
            for (ResourceLocation id : (List) entry.getValue()) {
                pack.info.scripts.add(new ScriptFileInfo(pack.info, id.getPath().substring(7)));
            }
            for (ScriptFileInfo fileInfo : pack.info.scripts) {
                ScriptSource.FromResource scriptSource = info -> resourceManager.m_215593_(info.id);
                this.loadFile(pack, fileInfo, scriptSource);
            }
            pack.scripts.sort(null);
            this.packs.put(pack.info.namespace, pack);
        }
    }

    public void loadFromDirectory() {
        if (Files.notExists(this.scriptType.path, new LinkOption[0])) {
            try {
                Files.createDirectories(this.scriptType.path);
            } catch (Exception var6) {
                this.scriptType.console.error("Failed to create script directory", var6);
            }
            try {
                OutputStream out = Files.newOutputStream(this.scriptType.path.resolve("example.js"));
                try {
                    out.write(("// priority: 0\n\n// Visit the wiki for more info - https://kubejs.com/\n\nconsole.info('Hello, World! (Loaded " + this.scriptType.name + " scripts)')\n\n").getBytes(StandardCharsets.UTF_8));
                } catch (Throwable var7) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Throwable var5) {
                            var7.addSuppressed(var5);
                        }
                    }
                    throw var7;
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception var8) {
                this.scriptType.console.error("Failed to write example.js", var8);
            }
        }
        ScriptPack pack = new ScriptPack(this, new ScriptPackInfo(this.scriptType.path.getFileName().toString(), ""));
        KubeJS.loadScripts(pack, this.scriptType.path, "");
        for (ScriptFileInfo fileInfo : pack.info.scripts) {
            ScriptSource.FromPath scriptSource = info -> this.scriptType.path.resolve(info.file);
            this.loadFile(pack, fileInfo, scriptSource);
        }
        pack.scripts.sort(null);
        this.packs.put(pack.info.namespace, pack);
    }

    public boolean isClassAllowed(String name) {
        return this.classFilter.isAllowed(name);
    }

    public void load() {
        MinecraftRemapper remapper = RemappingHelper.getMinecraftRemapper();
        long startAll = System.currentTimeMillis();
        this.context = Context.enter();
        this.topLevelScope = this.context.initStandardObjects();
        CURRENT_CONTEXT.set(this.context);
        this.context.setProperty("Type", this.scriptType);
        this.context.setProperty("Console", this.scriptType.console);
        this.context.setClassShutter(this);
        this.context.setRemapper(remapper);
        this.context.setApplicationClassLoader(KubeJS.class.getClassLoader());
        if (MiscPlatformHelper.get().isDataGen()) {
            this.firstLoad = false;
            this.scriptType.console.info("Skipping KubeJS script loading (DataGen)");
        } else {
            this.canListenEvents = true;
            TypeWrappers typeWrappers = this.context.getTypeWrappers();
            BindingsEvent bindingsEvent = new BindingsEvent(this, this.topLevelScope);
            CustomJavaToJsWrappersEvent customJavaToJsWrappersEvent = new CustomJavaToJsWrappersEvent(this);
            for (KubeJSPlugin plugin : KubeJSPlugins.getAll()) {
                plugin.registerTypeWrappers(this.scriptType, typeWrappers);
                plugin.registerBindings(bindingsEvent);
                plugin.registerCustomJavaToJsWrappers(customJavaToJsWrappersEvent);
            }
            KubeJSPlugins.addSidedBindings(bindingsEvent);
            for (ResourceKey<? extends Registry<?>> reg : BuiltInRegistries.REGISTRY.registryKeySet()) {
                RegistryInfo<?> info = RegistryInfo.of(reg);
                if (info.autoWrap && info.objectBaseClass != Object.class && info.objectBaseClass != null) {
                    try {
                        typeWrappers.register(info.objectBaseClass, UtilsJS.cast(info));
                    } catch (IllegalArgumentException var17) {
                        this.scriptType.console.info("Skipped registry type wrapper for " + info.key.location());
                    }
                }
            }
            int i = 0;
            int t = 0;
            for (ScriptPack pack : this.packs.values()) {
                try {
                    pack.scope = new NativeObject(this.context);
                    pack.scope.setParentScope(this.topLevelScope);
                    for (ScriptFile file : pack.scripts) {
                        t++;
                        long start = System.currentTimeMillis();
                        try {
                            file.load();
                            i++;
                            this.scriptType.console.info("Loaded script " + file.info.location + " in " + (double) (System.currentTimeMillis() - start) / 1000.0 + " s");
                        } catch (Throwable var16) {
                            this.scriptType.console.error("", var16);
                        }
                    }
                } catch (Throwable var18) {
                    this.scriptType.console.error("Failed to read script pack " + pack.info.namespace, var18);
                }
            }
            this.scriptType.console.info("Loaded " + i + "/" + t + " KubeJS " + this.scriptType.name + " scripts in " + (double) (System.currentTimeMillis() - startAll) / 1000.0 + " s with " + this.scriptType.console.errors.size() + " errors and " + this.scriptType.console.warnings.size() + " warnings");
            this.firstLoad = false;
            this.canListenEvents = false;
        }
    }

    public NativeJavaClass loadJavaClass(String name, boolean error) {
        if (name != null && !name.equals("null") && !name.isEmpty()) {
            if (this.javaClassCache == null) {
                this.javaClassCache = new HashMap();
            }
            Either<NativeJavaClass, Boolean> either = (Either<NativeJavaClass, Boolean>) this.javaClassCache.get(name);
            if (either == null) {
                either = Either.right(false);
                if (!this.isClassAllowed(name)) {
                    either = Either.right(true);
                } else {
                    try {
                        either = Either.left(new NativeJavaClass(this.context, this.topLevelScope, Class.forName(name)));
                        this.scriptType.console.info("Loaded Java class '%s'".formatted(name));
                    } catch (Exception var8) {
                        String name1 = RemappingHelper.getMinecraftRemapper().getUnmappedClass(name);
                        if (!name1.isEmpty()) {
                            if (!this.isClassAllowed(name1)) {
                                either = Either.right(true);
                            } else {
                                try {
                                    either = Either.left(new NativeJavaClass(this.context, this.topLevelScope, Class.forName(name1)));
                                    this.scriptType.console.info("Loaded Java class '%s'".formatted(name));
                                } catch (Exception var7) {
                                }
                            }
                        }
                    }
                }
                this.javaClassCache.put(name, either);
            }
            NativeJavaClass l = (NativeJavaClass) either.left().orElse(null);
            if (l != null) {
                return l;
            } else if (error) {
                Boolean found = (Boolean) either.right().orElse(false);
                throw Context.reportRuntimeError((found ? "Failed to load Java class '%s': Class is not allowed by class filter!" : "Failed to load Java class '%s': Class could not be found!").formatted(name), this.context);
            } else {
                return null;
            }
        } else if (error) {
            throw Context.reportRuntimeError("Class name can't be empty!", this.context);
        } else {
            return null;
        }
    }

    @Override
    public boolean visibleToScripts(String fullClassName, int type) {
        return type != 2 || this.isClassAllowed(fullClassName);
    }
}