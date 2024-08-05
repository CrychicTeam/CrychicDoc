package dev.latvian.mods.kubejs.script.data;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.util.JsonIO;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;

public class DataPackEventJS extends EventJS {

    private final VirtualKubeJSDataPack virtualDataPack;

    private final MultiPackResourceManager wrappedManager;

    public DataPackEventJS(VirtualKubeJSDataPack d, MultiPackResourceManager rm) {
        this.virtualDataPack = d;
        this.wrappedManager = rm;
    }

    public void add(ResourceLocation id, String content) {
        this.virtualDataPack.addData(id, content);
    }

    public void addJson(ResourceLocation id, JsonElement json) {
        if (json != null) {
            id = id.getPath().endsWith(".json") ? id : new ResourceLocation(id.getNamespace(), id.getPath() + ".json");
            this.add(id, JsonIO.toString(json));
        }
    }

    @Override
    protected void afterPosted(EventResult result) {
        for (String namespace : this.virtualDataPack.getNamespaces(PackType.SERVER_DATA)) {
            ((FallbackResourceManager) this.wrappedManager.namespacedManagers.computeIfAbsent(namespace, ns -> new FallbackResourceManager(PackType.SERVER_DATA, ns))).push(this.virtualDataPack);
        }
    }
}