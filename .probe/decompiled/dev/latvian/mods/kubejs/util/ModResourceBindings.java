package dev.latvian.mods.kubejs.util;

import dev.architectury.platform.Mod;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.ScriptTypePredicate;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

public class ModResourceBindings {

    private final Map<String, Collection<ModResourceBindings.BindingProvider>> bindings = new HashMap();

    public void addBindings(BindingsEvent event) {
        for (Entry<String, Collection<ModResourceBindings.BindingProvider>> modBindings : this.bindings.entrySet()) {
            String modName = (String) modBindings.getKey();
            Collection<ModResourceBindings.BindingProvider> providers = (Collection<ModResourceBindings.BindingProvider>) modBindings.getValue();
            ArrayList<Object> addedBindings = new ArrayList();
            for (ModResourceBindings.BindingProvider provider : providers) {
                String name = provider.name();
                if (provider.test(event.getType())) {
                    try {
                        event.add(name, provider.generate());
                        addedBindings.add(name);
                    } catch (Exception var11) {
                        KubeJS.LOGGER.error("Error adding binding for script type {} from mod '{}': {}", new Object[] { event.getType(), modName, name, var11 });
                    }
                }
            }
            KubeJS.LOGGER.info("Added bindings for script type {} from mod '{}': {}", new Object[] { event.getType(), modName, addedBindings });
        }
    }

    public void readBindings(Mod mod) throws IOException {
        Optional<Path> resource = mod.findResource("kubejs.bindings.txt");
        if (!resource.isEmpty()) {
            Stream<String> lines = Files.lines((Path) resource.get());
            try {
                List<ModResourceBindings.BindingProvider> providers = lines.map(s -> s.split("#", 2)[0].trim()).filter(line -> !line.isBlank()).map(line -> this.createProvider(mod, line)).filter(Objects::nonNull).toList();
                this.bindings.put(mod.getModId(), providers);
            } catch (Throwable var7) {
                if (lines != null) {
                    try {
                        lines.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if (lines != null) {
                lines.close();
            }
        }
    }

    @Nullable
    private ModResourceBindings.BindingProvider createProvider(Mod mod, String line) {
        String[] split = line.split("\\s+");
        if (split.length < 3) {
            KubeJS.LOGGER.error("Invalid binding for '{}' in line: {}", mod.getModId(), line);
            return null;
        } else {
            ScriptTypePredicate scriptTypeFilter = this.typePredicateOf(split[0]);
            String name = split[1];
            String className = split[2];
            ModResourceBindings.ClassBindingProvider classProvider = new ModResourceBindings.ClassBindingProvider(name, scriptTypeFilter, className);
            if (split.length == 3) {
                return classProvider;
            } else {
                String methodFieldOrConstructor = split[3];
                return (ModResourceBindings.BindingProvider) (methodFieldOrConstructor.equals("<init>") ? new ModResourceBindings.InstanceBindingProvider(classProvider) : new ModResourceBindings.InvokeBindingProvider(classProvider, methodFieldOrConstructor));
            }
        }
    }

    private ScriptTypePredicate typePredicateOf(String typeString) {
        String lower = typeString.toLowerCase();
        return (ScriptTypePredicate) (switch(lower) {
            case "*", "all" ->
                ScriptTypePredicate.ALL;
            case "common" ->
                ScriptTypePredicate.COMMON;
            case "startup_or_client" ->
                ScriptTypePredicate.STARTUP_OR_CLIENT;
            case "startup_or_server" ->
                ScriptTypePredicate.STARTUP_OR_SERVER;
            default ->
                {
                    ScriptType[] var5 = ScriptType.VALUES;
                    int var6 = var5.length;
                    int var7 = 0;
                    while (true) {
                        if (var7 >= var6) {
                            throw new IllegalArgumentException("Unknown script type predicate: " + typeString);
                        }
                        ScriptType type = var5[var7];
                        if (type.name.equals(lower)) {
                            yield type;
                            break;
                        }
                        var7++;
                    }
                }
        });
    }

    sealed interface BindingProvider extends ScriptTypePredicate permits ModResourceBindings.ClassBindingProvider, ModResourceBindings.InstanceBindingProvider, ModResourceBindings.InvokeBindingProvider {

        String name();

        Object generate();
    }

    static record ClassBindingProvider(String name, ScriptTypePredicate filter, String className) implements ModResourceBindings.BindingProvider {

        @Override
        public Object generate() {
            try {
                return this.getClass().getClassLoader().loadClass(this.className);
            } catch (ClassNotFoundException var2) {
                throw new RuntimeException(var2);
            }
        }

        @Override
        public boolean test(ScriptType scriptType) {
            return this.filter.test(scriptType);
        }
    }

    static record InstanceBindingProvider(ModResourceBindings.ClassBindingProvider parent) implements ModResourceBindings.BindingProvider {

        @Override
        public String name() {
            return this.parent().name();
        }

        @Override
        public Object generate() {
            Class<?> clazz = (Class<?>) this.parent().generate();
            try {
                Constructor<?> constructor = clazz.getConstructor();
                return constructor.newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException var3) {
                throw new IllegalStateException("[Bindings] Failed to find default constructor in class '" + clazz.getName() + "'");
            }
        }

        @Override
        public boolean test(ScriptType scriptType) {
            return this.parent().test(scriptType);
        }
    }

    static record InvokeBindingProvider(ModResourceBindings.ClassBindingProvider parent, String methodOrField) implements ModResourceBindings.BindingProvider {

        @Override
        public String name() {
            return this.parent().name();
        }

        @Override
        public Object generate() {
            Class<?> clazz = (Class<?>) this.parent().generate();
            Object f = this.byField(clazz);
            if (f != null) {
                return f;
            } else {
                Object m = this.byMethod(clazz);
                if (m != null) {
                    return m;
                } else {
                    throw new IllegalStateException("[Bindings] Failed to find static field or method '" + this.methodOrField + "' in class '" + clazz.getName() + "'");
                }
            }
        }

        @Override
        public boolean test(ScriptType scriptType) {
            return this.parent().test(scriptType);
        }

        @Nullable
        private Object byField(Class<?> clazz) {
            try {
                Field field = clazz.getField(this.methodOrField);
                if (Modifier.isStatic(field.getModifiers())) {
                    return field.get(null);
                }
            } catch (IllegalAccessException var3) {
                throw new IllegalStateException("[Bindings] Failed to get static field '" + this.methodOrField + "' in class '" + clazz.getName() + "'", var3);
            } catch (NoSuchFieldException var4) {
            }
            return null;
        }

        @Nullable
        private Object byMethod(Class<?> clazz) {
            try {
                Method method = clazz.getMethod(this.methodOrField);
                if (Modifier.isStatic(method.getModifiers())) {
                    return method.invoke(null);
                }
            } catch (InvocationTargetException | IllegalAccessException var3) {
                throw new IllegalStateException("[Bindings] Failed to invoke static method '" + this.methodOrField + "' in class '" + clazz.getName() + "'", var3);
            } catch (NoSuchMethodException var4) {
            }
            return null;
        }
    }
}