package dev.latvian.mods.kubejs.script;

import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

public enum ScriptType implements ScriptTypePredicate, ScriptTypeHolder {

    STARTUP("startup", "KubeJS Startup", KubeJSPaths.STARTUP_SCRIPTS, KubeJS::getStartupScriptManager), SERVER("server", "KubeJS Server", KubeJSPaths.SERVER_SCRIPTS, ServerScriptManager::getScriptManager), CLIENT("client", "KubeJS Client", KubeJSPaths.CLIENT_SCRIPTS, KubeJS::getClientScriptManager);

    public static final ScriptType[] VALUES = values();

    public final String name;

    public final ConsoleJS console;

    public final Path path;

    public final String nameStrip;

    public final transient Supplier<ScriptManager> manager;

    public transient Executor executor;

    public static ScriptType getCurrent(Context cx) {
        return (ScriptType) cx.getProperty("Type");
    }

    private ScriptType(String n, String cname, Path path, Supplier<ScriptManager> m) {
        this.name = n;
        this.console = new ConsoleJS(this, LoggerFactory.getLogger(cname));
        this.path = path;
        this.nameStrip = this.name + "_scripts:";
        this.manager = m;
        this.executor = Runnable::run;
    }

    public Path getLogFile() {
        Path dir = Platform.getGameFolder().resolve("logs/kubejs");
        Path file = dir.resolve(this.name + ".log");
        try {
            if (!Files.exists(dir, new LinkOption[0])) {
                Files.createDirectories(dir);
            }
            if (!Files.exists(file, new LinkOption[0])) {
                Path oldFile = dir.resolve(this.name + ".txt");
                if (Files.exists(oldFile, new LinkOption[0])) {
                    Files.move(oldFile, file);
                } else {
                    Files.createFile(file);
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return file;
    }

    public boolean isClient() {
        return this == CLIENT;
    }

    public boolean isServer() {
        return this == SERVER;
    }

    public boolean isStartup() {
        return this == STARTUP;
    }

    @HideFromJS
    public void unload() {
        this.console.resetFile();
        for (EventGroup group : EventGroup.getGroups().values()) {
            for (EventHandler handler : group.getHandlers().values()) {
                handler.clear(this);
            }
        }
    }

    @Override
    public boolean test(ScriptType type) {
        return type == this;
    }

    @Override
    public List<ScriptType> getValidTypes() {
        return List.of(this);
    }

    @NotNull
    public ScriptTypePredicate negate() {
        return switch(this) {
            case STARTUP ->
                ScriptTypePredicate.COMMON;
            case SERVER ->
                ScriptTypePredicate.STARTUP_OR_CLIENT;
            case CLIENT ->
                ScriptTypePredicate.STARTUP_OR_SERVER;
        };
    }

    @Override
    public ScriptType kjs$getScriptType() {
        return this;
    }

    static {
        ConsoleJS.STARTUP = STARTUP.console;
        ConsoleJS.SERVER = SERVER.console;
        ConsoleJS.CLIENT = CLIENT.console;
    }
}