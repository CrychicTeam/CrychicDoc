package dev.latvian.mods.kubejs.script;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;

public class ScriptPackInfo {

    public final String namespace;

    public final Component displayName;

    public final List<ScriptFileInfo> scripts;

    public final String pathStart;

    public ScriptPackInfo(String n, String p) {
        this.namespace = n;
        this.scripts = new ArrayList();
        this.pathStart = p;
        this.displayName = Component.literal(this.namespace);
    }
}