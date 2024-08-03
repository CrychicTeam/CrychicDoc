package dev.latvian.mods.kubejs.script.data;

import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VirtualKubeJSDataPack extends AbstractPackResources implements ExportablePackResources {

    public final boolean high;

    private final Map<ResourceLocation, String> locationToData;

    private final Map<String, String> pathToData;

    private final Set<String> namespaces;

    public VirtualKubeJSDataPack(boolean h) {
        super("dummy", false);
        this.high = h;
        this.locationToData = new HashMap();
        this.pathToData = new HashMap();
        this.namespaces = new HashSet();
    }

    public void addData(ResourceLocation id, String data) {
        this.locationToData.put(id, data);
        this.pathToData.put("data/" + id.getNamespace() + "/" + id.getPath(), data);
        this.namespaces.add(id.getNamespace());
        if (DevProperties.get().dataPackOutput) {
            ConsoleJS.SERVER.info("Registered virtual file [" + (this.high ? "high" : "low") + " priority] '" + id + "': " + data);
        }
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... path) {
        String var2 = path.length == 1 ? path[0] : "";
        return switch(var2) {
            case "pack.mcmeta" ->
                GeneratedData.PACK_META;
            case "pack.png" ->
                GeneratedData.PACK_ICON;
            default ->
                null;
        };
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        if (type != PackType.SERVER_DATA) {
            return null;
        } else {
            String s = (String) this.locationToData.get(location);
            if (s != null) {
                if (DevProperties.get().dataPackOutput) {
                    ConsoleJS.SERVER.info("Served virtual file [" + (this.high ? "high" : "low") + " priority] '" + location + "': " + s);
                }
                return () -> new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
            } else {
                return null;
            }
        }
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, PackResources.ResourceOutput visitor) {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        for (ResourceLocation r : this.locationToData.keySet()) {
            if (!r.getPath().endsWith(".mcmeta") && r.getNamespace().equals(namespace) && r.getPath().startsWith(path)) {
                visitor.accept(r, this.getResource(packType, r));
            }
        }
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return new HashSet(this.namespaces);
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) {
        return null;
    }

    public String toString() {
        return this.packId();
    }

    @NotNull
    @Override
    public String packId() {
        return "KubeJS Virtual Data Pack [" + (this.high ? "high" : "low") + " priority]";
    }

    @Override
    public void export(Path root) throws IOException {
        for (Entry<String, String> file : this.pathToData.entrySet()) {
            Path path = root.resolve((String) file.getKey());
            Files.createDirectories(path.getParent());
            Files.writeString(path, (CharSequence) file.getValue(), StandardCharsets.UTF_8);
        }
    }

    @Override
    public void close() {
    }

    public boolean hasNamespace(String key) {
        return this.namespaces.contains(key);
    }
}