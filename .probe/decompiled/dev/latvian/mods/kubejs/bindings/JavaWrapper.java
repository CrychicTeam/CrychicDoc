package dev.latvian.mods.kubejs.bindings;

import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

@Info("Methods for working with Java classes. Reflection my beloved ♥")
public class JavaWrapper {

    private final ScriptManager manager;

    public JavaWrapper(ScriptManager manager) {
        this.manager = manager;
    }

    @Info("Loads the specified class, and throws error if class it not found or allowed.\nThe returned object can have public static methods and fields accessed directly from it.\nConstructors can be used with the new keyword.\n")
    public Object loadClass(String className) {
        return this.manager.loadJavaClass(className, true);
    }

    @Info("Loads the specified class, and returns null if class is not found or allowed.\nThe returned object can have public static methods and fields accessed directly from it.\nConstructors can be used with the new keyword.\n")
    @Nullable
    public Object tryLoadClass(String className) {
        return this.manager.loadJavaClass(className, false);
    }

    @Info("Creates a custom ConsoleJS instance for you to use to, well, log stuff")
    public ConsoleJS createConsole(String name) {
        return new ConsoleJS(this.manager.scriptType, LoggerFactory.getLogger(name));
    }
}