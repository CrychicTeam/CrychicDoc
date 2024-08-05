package dev.latvian.mods.kubejs.registry;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public class RegistryEventJS<T> extends StartupEventJS {

    private final RegistryInfo<T> registry;

    public final List<BuilderBase<? extends T>> created;

    public RegistryEventJS(RegistryInfo<T> r) {
        this.registry = r;
        this.created = new LinkedList();
    }

    public BuilderBase<? extends T> create(String id, String type) {
        BuilderType<T> t = (BuilderType<T>) this.registry.types.get(type);
        if (t == null) {
            throw new IllegalArgumentException("Unknown type '" + type + "' for object '" + id + "'!");
        } else {
            BuilderBase b = t.factory().createBuilder(UtilsJS.getMCID(((ScriptManager) ScriptType.STARTUP.manager.get()).context, KubeJS.appendModId(id)));
            if (b == null) {
                throw new IllegalArgumentException("Unknown type '" + type + "' for object '" + id + "'!");
            } else {
                this.registry.addBuilder(b);
                this.created.add(b);
                return b;
            }
        }
    }

    public BuilderBase<? extends T> create(String id) {
        BuilderType t = this.registry.getDefaultType();
        if (t == null) {
            throw new IllegalArgumentException("Registry for type '" + this.registry.key.location() + "' doesn't have any builders registered!");
        } else {
            BuilderBase b = t.factory().createBuilder(UtilsJS.getMCID(((ScriptManager) ScriptType.STARTUP.manager.get()).context, KubeJS.appendModId(id)));
            if (b == null) {
                throw new IllegalArgumentException("Unknown type '" + t.type() + "' for object '" + id + "'!");
            } else {
                this.registry.addBuilder(b);
                this.created.add(b);
                return b;
            }
        }
    }

    @Deprecated
    public CustomBuilderObject custom(String id, Object object) {
        return this.createCustom(id, () -> object);
    }

    public CustomBuilderObject createCustom(String id, Supplier<Object> object) {
        if (object == null) {
            throw new IllegalArgumentException("Tried to register a null object with id: " + id);
        } else {
            ResourceLocation rl = UtilsJS.getMCID(((ScriptManager) ScriptType.STARTUP.manager.get()).context, KubeJS.appendModId(id));
            CustomBuilderObject b = new CustomBuilderObject(rl, object, this.registry);
            this.registry.addBuilder(b);
            this.created.add(b);
            return b;
        }
    }
}