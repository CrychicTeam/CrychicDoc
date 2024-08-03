package dev.latvian.mods.kubejs.script;

import dev.latvian.mods.kubejs.util.UtilsJS;

public class ScriptFile implements Comparable<ScriptFile> {

    public final ScriptPack pack;

    public final ScriptFileInfo info;

    public final ScriptSource source;

    public ScriptFile(ScriptPack p, ScriptFileInfo i, ScriptSource s) {
        this.pack = p;
        this.info = i;
        this.source = s;
    }

    public void load() throws Throwable {
        this.pack.manager.context.evaluateString(this.pack.scope, String.join("\n", this.info.lines), this.info.location, 1, null);
        this.info.lines = UtilsJS.EMPTY_STRING_ARRAY;
    }

    public int compareTo(ScriptFile o) {
        return Integer.compare(o.info.getPriority(), this.info.getPriority());
    }
}