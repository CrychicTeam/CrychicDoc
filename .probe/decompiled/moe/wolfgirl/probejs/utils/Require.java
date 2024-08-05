package moe.wolfgirl.probejs.utils;

import dev.latvian.mods.kubejs.bindings.JavaWrapper;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.ScriptableObject;
import dev.latvian.mods.rhino.Undefined;
import java.util.Arrays;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;

public class Require extends BaseFunction {

    private final JavaWrapper innerWrapper;

    public Require(ScriptManager manager) {
        this.innerWrapper = new JavaWrapper(manager);
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        String result = (String) Context.jsToJava(cx, args[0], String.class);
        String[] parts = result.split("/", 2);
        ClassPath path = new ClassPath(Arrays.stream(parts[1].split("/")).toList());
        Object loaded = this.innerWrapper.tryLoadClass(path.getClassPathJava());
        return new Require.RequireWrapper(path, loaded == null ? Undefined.instance : loaded);
    }

    public static class RequireWrapper extends ScriptableObject {

        private final ClassPath path;

        private final Object clazz;

        public RequireWrapper(ClassPath path, Object clazz) {
            this.path = path;
            this.clazz = clazz;
        }

        @Override
        public String getClassName() {
            return this.path.getClassPathJava();
        }

        @Override
        public Object get(Context cx, String name, Scriptable start) {
            return name.equals(this.path.getName()) ? this.clazz : super.get(cx, name, start);
        }
    }
}